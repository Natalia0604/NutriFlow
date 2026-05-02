ALTER TABLE custom_foods
    ADD COLUMN sodium     DECIMAL(7, 2) NULL COMMENT 'mg' AFTER fat,
    ADD COLUMN size_label VARCHAR(50)   NULL COMMENT '份量說明' AFTER food_name;
