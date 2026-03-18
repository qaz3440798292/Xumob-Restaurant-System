-- =============================================
-- XUMOB 餐厅管理系统 - 数据库表结构
-- =============================================

-- =============================================
-- 1. 用户相关表
-- =============================================

-- 用户表（顾客）
CREATE TABLE `sys_user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码',
    `phone` VARCHAR(20) COMMENT '手机号',
    `email` VARCHAR(100) COMMENT '邮箱',
    `nickname` VARCHAR(50) COMMENT '昵称',
    `avatar` VARCHAR(255) COMMENT '头像URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 会员类型表
CREATE TABLE `member_type` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会员类型ID',
    `name` VARCHAR(50) NOT NULL COMMENT '类型名称',
    `discount` DECIMAL(4,2) DEFAULT 1.00 COMMENT '折扣率',
    `description` VARCHAR(255) COMMENT '描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员类型表';

-- 会员表
CREATE TABLE `member` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会员ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `member_type_id` BIGINT COMMENT '会员类型ID',
    `card_number` VARCHAR(50) UNIQUE COMMENT '会员卡号',
    `balance` DECIMAL(10,2) DEFAULT 0.00 COMMENT '账户余额',
    `points` INT DEFAULT 0 COMMENT '积分',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    `expire_time` DATETIME COMMENT '过期时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`),
    FOREIGN KEY (`member_type_id`) REFERENCES `member_type`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- 会员优惠券表
CREATE TABLE `member_coupon` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `member_id` BIGINT NOT NULL COMMENT '会员ID',
    `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-未使用 1-已使用 2-已过期',
    `use_time` DATETIME COMMENT '使用时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`member_id`) REFERENCES `member`(`id`),
    FOREIGN KEY (`coupon_id`) REFERENCES `coupon`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员优惠券表';

-- =============================================
-- 2. 餐品相关表
-- =============================================

-- 餐品分类表
CREATE TABLE `food_category` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
    `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID',
    `sort` INT DEFAULT 0 COMMENT '排序',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐品分类表';

-- 餐品表
CREATE TABLE `food` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '餐品ID',
    `name` VARCHAR(100) NOT NULL COMMENT '餐品名称',
    `category_id` BIGINT COMMENT '分类ID',
    `image` VARCHAR(255) COMMENT '图片URL',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `description` VARCHAR(255) COMMENT '描述',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-锁键 1-解锁 2-上架 3-下架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`category_id`) REFERENCES `food_category`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐品表';

-- 餐品配方表（半成品组成）
CREATE TABLE `food_recipe` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `food_id` BIGINT NOT NULL COMMENT '餐品ID',
    `ingredient_id` BIGINT NOT NULL COMMENT '货品ID(半成品)',
    `quantity` DECIMAL(10,2) NOT NULL COMMENT '用量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`food_id`) REFERENCES `food`(`id`),
    FOREIGN KEY (`ingredient_id`) REFERENCES `goods`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='餐品配方表';

-- =============================================
-- 3. 库存/货品相关表
-- =============================================

-- 货品表
CREATE TABLE `goods` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '货品ID',
    `name` VARCHAR(100) NOT NULL COMMENT '货品名称',
    `category_id` BIGINT COMMENT '分类ID',
    `unit` VARCHAR(20) DEFAULT '个' COMMENT '单位',
    `cost_price` DECIMAL(10,2) COMMENT '成本价',
    `supplier` VARCHAR(100) COMMENT '供应商',
    `type` TINYINT DEFAULT 0 COMMENT '类型: 0-原料 1-半成品 2-包装',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='货品表';

-- 库存表
CREATE TABLE `stock` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `goods_id` BIGINT NOT NULL COMMENT '货品ID',
    `quantity` DECIMAL(10,2) DEFAULT 0 COMMENT '库存数量',
    `warning_quantity` DECIMAL(10,2) DEFAULT 10 COMMENT '预警数量',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`goods_id`) REFERENCES `goods`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- Waste上报表
CREATE TABLE `waste_log` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `goods_id` BIGINT NOT NULL COMMENT '货品ID',
    `quantity` DECIMAL(10,2) NOT NULL COMMENT '报废数量',
    `reason` VARCHAR(255) COMMENT '报废原因',
    `operator_id` BIGINT COMMENT '操作人ID',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`goods_id`) REFERENCES `goods`(`id`),
    FOREIGN KEY (`operator_id`) REFERENCES `employee`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Waste上报日志表';

-- =============================================
-- 4. 订单相关表
-- =============================================

-- 订单表
CREATE TABLE `orders` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号(18位数字英文组合)',
    `user_id` BIGINT COMMENT '用户ID',
    `member_id` BIGINT COMMENT '会员ID',
    `type` TINYINT NOT NULL COMMENT '订单类型: 0-外带 1-堂食 2-外卖',
    `status` TINYINT DEFAULT 0 COMMENT '订单状态: 0-待支付 1-已支付 2-进行中 3-已完成 4-已取消',
    `total_price` DECIMAL(10,2) NOT NULL COMMENT '订单总价',
    `actual_price` DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    `discount_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    `coupon_id` BIGINT COMMENT '优惠券ID',
    `rider_id` BIGINT COMMENT '骑手ID',
    `table_number` VARCHAR(20) COMMENT '桌号(堂食用)',
    `remark` VARCHAR(255) COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `pay_time` DATETIME COMMENT '支付时间',
    `complete_time` DATETIME COMMENT '完成时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`),
    FOREIGN KEY (`member_id`) REFERENCES `member`(`id`),
    FOREIGN KEY (`coupon_id`) REFERENCES `coupon`(`id`),
    FOREIGN KEY (`rider_id`) REFERENCES `rider`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单详情表
CREATE TABLE `order_item` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `food_id` BIGINT NOT NULL COMMENT '餐品ID',
    `food_name` VARCHAR(100) COMMENT '餐品名称',
    `quantity` INT NOT NULL COMMENT '数量',
    `unit_price` DECIMAL(10,2) NOT NULL COMMENT '单价',
    `subtotal` DECIMAL(10,2) NOT NULL COMMENT '小计',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`),
    FOREIGN KEY (`food_id`) REFERENCES `food`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

-- 订单配送状态表
CREATE TABLE `order_delivery` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `order_id` BIGINT NOT NULL COMMENT '订单ID',
    `rider_arrive_time` DATETIME COMMENT '骑手到店时间',
    `cooking_start_time` DATETIME COMMENT '开始出餐时间',
    `delivery_start_time` DATETIME COMMENT '开始送餐时间',
    `delivery_complete_time` DATETIME COMMENT '送达时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单配送状态表';

-- =============================================
-- 5. 优惠相关表
-- =============================================

-- 优惠券表
CREATE TABLE `coupon` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '优惠券ID',
    `name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    `code` VARCHAR(50) UNIQUE COMMENT '优惠券码',
    `type` TINYINT DEFAULT 0 COMMENT '类型: 0-满减券 1-折扣券',
    `value` DECIMAL(10,2) NOT NULL COMMENT '优惠值(满减金额或折扣率)',
    `min_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低消费金额',
    `target_type` TINYINT DEFAULT 0 COMMENT '优惠范围: 0-全场 1-指定商品 2-指定分类',
    `target_id` BIGINT COMMENT '指定商品/分类ID',
    `total_count` INT COMMENT '发放总数',
    `remain_count` INT COMMENT '剩余数量',
    `member_type_id` BIGINT COMMENT '指定会员类型(为空则全体可用)',
    `expire_time` DATETIME COMMENT '过期时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用 1-正常',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- =============================================
-- 6. 骑手相关表
-- =============================================

-- 骑手表
CREATE TABLE `rider` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '骑手ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-下线 1-上线',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='骑手表';

-- =============================================
-- 7. 员工相关表
-- =============================================

-- 职位表
CREATE TABLE `position` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '职位ID',
    `name` VARCHAR(50) NOT NULL COMMENT '职位名称',
    `description` VARCHAR(255) COMMENT '职位描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位表';

-- 权限表
CREATE TABLE `permission` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权限ID',
    `name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `code` VARCHAR(100) NOT NULL UNIQUE COMMENT '权限代码',
    `description` VARCHAR(255) COMMENT '权限描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 职位权限关联表
CREATE TABLE `position_permission` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `position_id` BIGINT NOT NULL COMMENT '职位ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    FOREIGN KEY (`position_id`) REFERENCES `position`(`id`),
    FOREIGN KEY (`permission_id`) REFERENCES `permission`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='职位权限关联表';

-- 员工表
CREATE TABLE `employee` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '员工ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `position_id` BIGINT COMMENT '职位ID',
    `name` VARCHAR(50) NOT NULL COMMENT '姓名',
    `phone` VARCHAR(20) COMMENT '手机号',
    `id_card` VARCHAR(20) COMMENT '身份证号',
    `salary` DECIMAL(10,2) COMMENT '薪资',
    `hire_date` DATE COMMENT '入职日期',
    `employment_type` TINYINT DEFAULT 0 COMMENT '用工类型: 0-全职 1-兼职',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 0-离职 1-在职',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (`user_id`) REFERENCES `sys_user`(`id`),
    FOREIGN KEY (`position_id`) REFERENCES `position`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工表';

-- =============================================
-- 8. 财务相关表
-- =============================================

-- 财务记录表
CREATE TABLE `finance` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '财务ID',
    `type` TINYINT NOT NULL COMMENT '类型: 0-收入 1-支出',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `source` VARCHAR(50) COMMENT '来源',
    `order_id` BIGINT COMMENT '订单ID',
    `description` VARCHAR(255) COMMENT '描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (`order_id`) REFERENCES `orders`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务记录表';

-- =============================================
-- 9. 数据统计相关表
-- =============================================

-- 销售统计表
CREATE TABLE `daily_stats` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `stat_date` DATE NOT NULL COMMENT '统计日期',
    `total_orders` INT DEFAULT 0 COMMENT '订单数',
    `total_sales` DECIMAL(12,2) DEFAULT 0.00 COMMENT '营业额',
    `member_sales` DECIMAL(12,2) DEFAULT 0.00 COMMENT '会员销售额',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='每日销售统计表';
