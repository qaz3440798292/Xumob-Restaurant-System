-- =============================================
-- 职位数据初始化
-- =============================================
-- 职位级别: 普通员工(1) < 精英员工(2) < 值班主管(3) < 顾客体验经理(4) < 餐厅总经理(5)

-- 职位表添加 level 字段（如已存在请忽略）
-- ALTER TABLE `position` ADD COLUMN `level` INT DEFAULT 0 COMMENT '职级(越大越高)' AFTER `code`;

-- 初始化职位数据
INSERT INTO `position` (`name`, `code`, `level`, `description`) VALUES 
('普通员工', 'CREW', 1, '基础工作岗位'),
('精英员工', 'CREW_TRAINER', 2, '负责培训新员工'),
('值班主管', 'QSC_SHIFT_LEADER', 3, '负责值班期间的管理工作'),
('顾客体验经理', 'CUSTOMER_EXPERIENCE_MANAGER', 4, '负责顾客体验管理'),
('餐厅总经理', 'RESTAURANT_GENERAL_MANAGER', 5, '负责餐厅全面管理');
