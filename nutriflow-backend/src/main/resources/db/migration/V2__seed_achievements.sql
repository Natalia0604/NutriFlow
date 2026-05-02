INSERT INTO achievements (code, name, description, icon, condition_json, sort_order) VALUES
('STREAK_7',      '7 天連續',    '連續記錄 7 天餐點',        '🔥', '{"type":"streak","days":7}',                  1),
('VEGGIE_MASTER', '蔬菜達人',    '單日蔬菜攝取≥3 份',        '🥗', '{"type":"daily","veg_servings":3}',            2),
('PROTEIN_KING',  '蛋白質王',    '連續 7 天達蛋白質目標',    '💪', '{"type":"streak_nutrition","days":7}',         3),
('PHOTO_10',      '拍照記錄×10', '使用拍照記錄 10 次',       '📷', '{"type":"count","action":"photo","n":10}',     4),
('CAT_FRIEND',    '小橘的朋友',  '完成完整建檔流程',         '🐱', '{"type":"setup_complete"}',                    5);
