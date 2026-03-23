CREATE DATABASE IF NOT EXISTS meirong_yuyue DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE meirong_yuyue;

CREATE TABLE IF NOT EXISTS `yonghu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `yonghu_name` varchar(50) DEFAULT NULL COMMENT '用户姓名',
  `yonghu_phone` varchar(20) DEFAULT NULL COMMENT '用户手机号',
  `yonghu_id_number` varchar(20) DEFAULT NULL COMMENT '用户身份证号',
  `yonghu_photo` varchar(255) DEFAULT NULL COMMENT '照片',
  `sex_types` int(11) DEFAULT '2' COMMENT '性别(1:男,2:女)',
  `yonghu_email` varchar(100) DEFAULT NULL COMMENT '电子邮箱',
  `new_money` decimal(10,2) DEFAULT '0.00' COMMENT '余额',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `yonghu_phone` (`yonghu_phone`),
  KEY `yonghu_id_number` (`yonghu_id_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `meirong_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `service_name` varchar(100) NOT NULL COMMENT '服务名称',
  `service_category` int(11) DEFAULT NULL COMMENT '服务分类(1:面部护理,2:身体护理,3:美甲美睫,4:其他服务)',
  `service_old_money` decimal(10,2) DEFAULT NULL COMMENT '服务原价',
  `service_new_money` decimal(10,2) NOT NULL COMMENT '服务现价',
  `service_duration` int(11) DEFAULT NULL COMMENT '服务时长(分钟)',
  `service_photo` varchar(255) DEFAULT NULL COMMENT '服务照片',
  `service_process` text COMMENT '服务流程',
  `service_products` text COMMENT '使用产品',
  `service_notes` text COMMENT '注意事项',
  `service_content` text COMMENT '服务简介',
  `service_clicknum` int(11) DEFAULT '0' COMMENT '点击次数',
  `service_rating` decimal(3,2) DEFAULT '5.00' COMMENT '服务评分',
  `shangxia_types` int(11) DEFAULT '1' COMMENT '上下架状态(1:上架,2:下架)',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `service_category` (`service_category`),
  KEY `shangxia_types` (`shangxia_types`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美容服务表';

CREATE TABLE IF NOT EXISTS `meirong_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_uuid_number` varchar(50) NOT NULL COMMENT '订单号',
  `service_id` int(11) NOT NULL COMMENT '服务项目',
  `staff_id` int(11) DEFAULT NULL COMMENT '服务师',
  `yonghu_id` int(11) NOT NULL COMMENT '用户',
  `order_true_price` decimal(10,2) NOT NULL COMMENT '实付价格',
  `order_status` int(11) DEFAULT '1' COMMENT '订单状态(1:待确认,2:已确认,3:服务中,4:已完成,5:已取消)',
  `shijianduan` varchar(50) DEFAULT NULL COMMENT '预约时间段',
  `buy_time` date DEFAULT NULL COMMENT '预约日期',
  `insert_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `pay_status` int(11) DEFAULT '0' COMMENT '支付状态(0:未支付,1:已支付)',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `order_comment` text COMMENT '服务评价',
  `order_rating` decimal(3,2) DEFAULT NULL COMMENT '服务评分',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_uuid_number` (`order_uuid_number`),
  KEY `service_id` (`service_id`),
  KEY `yonghu_id` (`yonghu_id`),
  KEY `order_status` (`order_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='美容服务预约表';

INSERT INTO `yonghu` (`username`, `password`, `yonghu_name`, `yonghu_phone`, `yonghu_id_number`, `sex_types`, `yonghu_email`) VALUES
('admin', 'admin', '管理员', '13800138000', '110101199001011234', 1, 'admin@example.com'),
('test', '123456', '测试用户', '13900139000', '110101199001011235', 2, 'test@example.com');

CREATE TABLE IF NOT EXISTS `staff` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `staff_name` varchar(50) NOT NULL COMMENT '服务师姓名',
  `staff_phone` varchar(20) DEFAULT NULL COMMENT '服务师手机号',
  `staff_email` varchar(100) DEFAULT NULL COMMENT '服务师邮箱',
  `staff_avatar` varchar(255) DEFAULT NULL COMMENT '服务师头像',
  `staff_skills` varchar(255) DEFAULT NULL COMMENT '服务师技能标签',
  `staff_rating` decimal(3,2) DEFAULT '5.00' COMMENT '服务师评分',
  `staff_status` int(11) DEFAULT '1' COMMENT '服务师状态(1:启用,2:禁用)',
  `staff_introduction` text COMMENT '服务师简介',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `staff_status` (`staff_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务师表';

INSERT INTO `meirong_service` (`service_name`, `service_category`, `service_old_money`, `service_new_money`, `service_duration`, `service_content`, `shangxia_types`) VALUES
('深层清洁面部护理', 1, 299.00, 199.00, 60, '深层清洁毛孔，去除黑头粉刺，让肌肤清爽透亮', 1),
('补水保湿护理', 1, 399.00, 299.00, 90, '为肌肤补充水分，改善干燥缺水问题，恢复肌肤水润光泽', 1),
('美白淡斑护理', 1, 599.00, 499.00, 120, '淡化色斑，提亮肤色，让肌肤白皙透亮', 1),
('全身精油按摩', 2, 399.00, 299.00, 60, '使用天然精油进行全身按摩，舒缓疲劳，放松身心', 1),
('背部理疗', 2, 299.00, 199.00, 45, '针对背部肌肉进行深度放松，缓解肩颈酸痛', 1),
('美甲套餐', 3, 199.00, 99.00, 90, '包含修甲、涂色、手部护理全套服务', 1),
('睫毛嫁接', 3, 399.00, 299.00, 120, '自然睫毛嫁接，让眼睛更有神采', 1),
('美睫护理', 3, 99.00, 69.00, 30, '睫毛清洁护理，保持睫毛健康', 1);

INSERT INTO `staff` (`staff_name`, `staff_phone`, `staff_email`, `staff_skills`, `staff_introduction`) VALUES
('张美容', '13800138001', 'zhang@example.com', '面部护理,补水保湿', '拥有5年美容经验，擅长面部护理和补水保湿服务'),
('李按摩', '13800138002', 'li@example.com', '精油按摩,背部理疗', '专业按摩师，擅长精油按摩和背部理疗'),
('王美甲', '13800138003', 'wang@example.com', '美甲,美睫', '资深美甲师，擅长各种美甲和睫毛嫁接服务');
