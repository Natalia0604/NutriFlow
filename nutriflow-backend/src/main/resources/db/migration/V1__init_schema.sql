-- ============================================
-- NutriFlow Database Schema v1.0
-- Engine: MySQL 8.0+  Charset: utf8mb4
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- users
CREATE TABLE users (
    id               BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    email            VARCHAR(255)     NOT NULL,
    password_hash    VARCHAR(255)     NOT NULL,
    gender           ENUM('male','female') NULL,
    age              INT              NULL,
    weight           DECIMAL(5,2)     NULL COMMENT 'kg',
    height           DECIMAL(5,2)     NULL COMMENT 'cm',
    activity_level   VARCHAR(20)      NULL
        COMMENT 'sedentary|lightly_active|moderately_active|very_active|extra_active',
    has_weight_goal  BOOLEAN          NOT NULL DEFAULT FALSE,
    target_weight    DECIMAL(5,2)     NULL COMMENT 'kg',
    goal_weeks       INT              NULL,
    is_setup_complete BOOLEAN         NOT NULL DEFAULT FALSE,
    refresh_token    VARCHAR(512)     NULL,
    created_at       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE INDEX UNQ_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='使用者帳號';

-- cat_profiles
CREATE TABLE cat_profiles (
    id               BIGINT UNSIGNED  NOT NULL AUTO_INCREMENT,
    user_id          BIGINT UNSIGNED  NOT NULL,
    breed            VARCHAR(20)      NOT NULL
        COMMENT 'orange|calico|black|white|tabby|ragdoll',
    name             VARCHAR(50)      NOT NULL,
    knuckle_px_ratio DECIMAL(6,4)     NULL COMMENT '拳頭像素/信用卡像素',
    bowl_width_mm    DECIMAL(6,2)     NULL COMMENT '碗盤校正寬度 mm',
    created_at       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE INDEX UNQ_cat_profiles_user (user_id),
    CONSTRAINT FK_cat_profiles_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='貓咪設定';

-- food_preferences
CREATE TABLE food_preferences (
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id     BIGINT UNSIGNED NOT NULL,
    food_name   VARCHAR(100)    NOT NULL,
    category    VARCHAR(20)     NOT NULL
        COMMENT 'protein|grain|vegetable|fruit|other',
    is_custom   BOOLEAN         NOT NULL DEFAULT FALSE,
    is_allergy  BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE INDEX UNQ_food_pref_user_food (user_id, food_name),
    INDEX IDX_food_pref_user (user_id),
    CONSTRAINT FK_food_pref_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='使用者食物偏好與過敏原';

-- week_menus
CREATE TABLE week_menus (
    id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id      BIGINT UNSIGNED NOT NULL,
    week_start   DATE            NOT NULL COMMENT '週一日期',
    ai_comment   TEXT            NULL,
    generated_by VARCHAR(10)     NOT NULL DEFAULT 'MANUAL'
        COMMENT 'AI|MANUAL|SKIP',
    created_at   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE INDEX UNQ_week_menus_user_week (user_id, week_start),
    INDEX IDX_week_menus_user (user_id),
    CONSTRAINT FK_week_menus_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='週菜單主表';

-- meal_cells
CREATE TABLE meal_cells (
    id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    week_menu_id BIGINT UNSIGNED NOT NULL,
    day_index    INT             NOT NULL COMMENT '0=週一 6=週日',
    meal_index   INT             NOT NULL COMMENT '0=早 1=午 2=晚',
    meal_type    VARCHAR(15)     NOT NULL DEFAULT 'EATING_OUT'
        COMMENT 'EATING_OUT|HOME_COOK',
    logged       BOOLEAN         NOT NULL DEFAULT FALSE,
    log_time     TIME            NULL,
    note         VARCHAR(255)    NULL,
    source       VARCHAR(10)     NOT NULL DEFAULT 'MANUAL'
        COMMENT 'AI|MANUAL|PHOTO',
    created_at   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE INDEX UNQ_meal_cells_menu_day_meal (week_menu_id, day_index, meal_index),
    INDEX IDX_meal_cells_menu (week_menu_id),
    CONSTRAINT FK_meal_cells_menu
        FOREIGN KEY (week_menu_id) REFERENCES week_menus (id) ON DELETE CASCADE,
    CONSTRAINT CHK_day_index  CHECK (day_index  BETWEEN 0 AND 6),
    CONSTRAINT CHK_meal_index CHECK (meal_index BETWEEN 0 AND 2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='週菜單格子，每格代表某天某餐';

-- meal_items
CREATE TABLE meal_items (
    id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    meal_cell_id BIGINT UNSIGNED NOT NULL,
    food_name    VARCHAR(200)    NOT NULL,
    kcal         DECIMAL(7,2)    NULL COMMENT 'kcal',
    protein      DECIMAL(6,2)    NULL COMMENT 'g',
    carbs        DECIMAL(6,2)    NULL COMMENT 'g',
    fat          DECIMAL(6,2)    NULL COMMENT 'g',
    sodium       DECIMAL(7,2)    NULL COMMENT 'mg',
    data_source  VARCHAR(20)     NULL
        COMMENT 'OFFICIAL|OPEN_FOOD_FACTS|AI|PHOTO|MANUAL',
    confidence   DECIMAL(4,2)    NULL COMMENT 'AI 信心度 0.0~1.0',
    sort_order   INT             NOT NULL DEFAULT 0,
    created_at   DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX IDX_meal_items_cell (meal_cell_id),
    CONSTRAINT FK_meal_items_cell
        FOREIGN KEY (meal_cell_id) REFERENCES meal_cells (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='格子內食物項目，支援一格多道食物';

-- meal_logs
CREATE TABLE meal_logs (
    id           BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id      BIGINT UNSIGNED NOT NULL,
    meal_cell_id BIGINT UNSIGNED NULL COMMENT '關聯週菜單格子（可為空）',
    meal_date    DATE            NOT NULL,
    meal_index   INT             NOT NULL COMMENT '0=早 1=午 2=晚',
    meal_type    VARCHAR(15)     NOT NULL DEFAULT 'EATING_OUT',
    store_name   VARCHAR(100)    NULL,
    note         VARCHAR(255)    NULL,
    logged_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX IDX_meal_logs_user_date (user_id, meal_date),
    INDEX IDX_meal_logs_date (meal_date),
    CONSTRAINT FK_meal_logs_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_meal_logs_cell
        FOREIGN KEY (meal_cell_id) REFERENCES meal_cells (id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='今日餐點實際記錄（計畫的執行）';

-- meal_log_items
CREATE TABLE meal_log_items (
    id              BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    meal_log_id     BIGINT UNSIGNED NOT NULL,
    food_name       VARCHAR(200)    NOT NULL,
    kcal            DECIMAL(7,2)    NULL,
    kcal_range_min  DECIMAL(7,2)    NULL COMMENT '估算區間下限',
    kcal_range_max  DECIMAL(7,2)    NULL COMMENT '估算區間上限',
    protein         DECIMAL(6,2)    NULL COMMENT 'g',
    carbs           DECIMAL(6,2)    NULL COMMENT 'g',
    fat             DECIMAL(6,2)    NULL COMMENT 'g',
    sodium          DECIMAL(7,2)    NULL COMMENT 'mg',
    data_source     VARCHAR(20)     NOT NULL DEFAULT 'MANUAL'
        COMMENT 'OFFICIAL|AI|PHOTO|MANUAL',
    confidence      DECIMAL(4,2)    NULL,
    photo_used      BOOLEAN         NOT NULL DEFAULT FALSE,
    reference_type  VARCHAR(10)     NULL COMMENT 'CARD|FIST|NONE',
    sort_order      INT             NOT NULL DEFAULT 0,
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX IDX_log_items_log (meal_log_id),
    CONSTRAINT FK_log_items_log
        FOREIGN KEY (meal_log_id) REFERENCES meal_logs (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='餐點記錄的食物項目，含估算區間';

-- weight_records
CREATE TABLE weight_records (
    id          BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id     BIGINT UNSIGNED NOT NULL,
    weight      DECIMAL(5,2)    NOT NULL COMMENT 'kg',
    record_date DATE            NOT NULL,
    note        VARCHAR(255)    NULL,
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE INDEX UNQ_weight_user_date (user_id, record_date),
    INDEX IDX_weight_user (user_id),
    CONSTRAINT FK_weight_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='每日體重記錄';

-- official_nutrition
CREATE TABLE official_nutrition (
    id            BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    store_name    VARCHAR(100)    NOT NULL,
    item_name     VARCHAR(200)    NOT NULL,
    size_label    VARCHAR(50)     NULL,
    kcal          DECIMAL(7,2)    NOT NULL,
    protein       DECIMAL(6,2)    NULL COMMENT 'g',
    carbs         DECIMAL(6,2)    NULL COMMENT 'g',
    fat           DECIMAL(6,2)    NULL COMMENT 'g',
    sodium        DECIMAL(7,2)    NULL COMMENT 'mg',
    sugar         DECIMAL(6,2)    NULL COMMENT 'g',
    data_url      VARCHAR(500)    NULL,
    last_verified DATE            NULL,
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX IDX_official_store (store_name),
    INDEX IDX_official_item  (item_name),
    FULLTEXT INDEX FT_official_search (store_name, item_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='連鎖店官方熱量資料（系統維護）';

-- custom_foods
CREATE TABLE custom_foods (
    id         BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id    BIGINT UNSIGNED NOT NULL,
    food_name  VARCHAR(200)    NOT NULL,
    kcal       DECIMAL(7,2)    NOT NULL,
    protein    DECIMAL(6,2)    NULL COMMENT 'g',
    carbs      DECIMAL(6,2)    NULL COMMENT 'g',
    fat        DECIMAL(6,2)    NULL COMMENT 'g',
    serving_g  DECIMAL(6,2)    NULL COMMENT '基準份量 g',
    note       VARCHAR(255)    NULL,
    created_at DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX IDX_custom_foods_user (user_id),
    CONSTRAINT FK_custom_foods_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='使用者自訂食物（常吃的家常菜等）';

-- achievements
CREATE TABLE achievements (
    id             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    code           VARCHAR(50)     NOT NULL,
    name           VARCHAR(100)    NOT NULL,
    description    VARCHAR(255)    NOT NULL,
    icon           VARCHAR(10)     NOT NULL,
    condition_json JSON            NULL
        COMMENT '例：{"type":"streak","days":7}',
    sort_order     INT             NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    UNIQUE INDEX UNQ_achievements_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='成就徽章定義表';

-- user_achievements
CREATE TABLE user_achievements (
    id             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    user_id        BIGINT UNSIGNED NOT NULL,
    achievement_id BIGINT UNSIGNED NOT NULL,
    earned_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE INDEX UNQ_user_achievement (user_id, achievement_id),
    CONSTRAINT FK_ua_user
        FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT FK_ua_achievement
        FOREIGN KEY (achievement_id) REFERENCES achievements (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
    COMMENT='使用者已解鎖的成就徽章';

-- Daily nutrition view
CREATE OR REPLACE VIEW v_daily_nutrition AS
SELECT
    ml.user_id,
    ml.meal_date,
    COUNT(DISTINCT ml.id)  AS meal_count,
    SUM(mli.kcal)          AS total_kcal,
    SUM(mli.protein)       AS total_protein,
    SUM(mli.carbs)         AS total_carbs,
    SUM(mli.fat)           AS total_fat,
    SUM(mli.sodium)        AS total_sodium
FROM meal_logs ml
JOIN meal_log_items mli ON mli.meal_log_id = ml.id
GROUP BY ml.user_id, ml.meal_date;

SET FOREIGN_KEY_CHECKS = 1;
