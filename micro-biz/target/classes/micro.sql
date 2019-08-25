/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : micro

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 06/07/2019 09:37:17
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `category` tinyint(1) NULL DEFAULT NULL,
  `tenant_id` bigint(20) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `deleted` double(1, 0) UNSIGNED ZEROFILL NULL DEFAULT 0,
  `de` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dd` double(11, 3) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id`) USING BTREE COMMENT '普通索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '账号' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ali_pay_notify
-- ----------------------------
DROP TABLE IF EXISTS `ali_pay_notify`;
CREATE TABLE `ali_pay_notify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notify_time` datetime(0) NULL DEFAULT NULL,
  `notify_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `notify_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trade_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_trade_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_biz_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `buyer_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `buyer_logon_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trade_status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `total_amount` decimal(10, 2) NULL DEFAULT NULL,
  `receipt_amount` decimal(10, 2) NULL DEFAULT NULL,
  `buyer_pay_amount` decimal(10, 2) NULL DEFAULT NULL,
  `refund_fee` decimal(10, 2) NULL DEFAULT NULL,
  `gmt_create` datetime(0) NULL DEFAULT NULL,
  `gmt_payment` datetime(0) NULL DEFAULT NULL,
  `gmt_refund` datetime(0) NULL DEFAULT NULL,
  `gmt_close` datetime(0) NULL DEFAULT NULL,
  `data` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '阿里支付通知记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for area
-- ----------------------------
DROP TABLE IF EXISTS `area`;
CREATE TABLE `area`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `code` bigint(12) UNSIGNED NOT NULL COMMENT '代码',
  `name` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' COMMENT '名称',
  `black_area` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0表示为国务院承认区域，1表示为黑区。事实上存在，但是国务院不承认的黑区，黑区没有统一代码，因使用部门而异',
  `level_type` tinyint(1) NOT NULL COMMENT '级别0-5:国省市县镇村',
  `parent_code` bigint(12) NULL DEFAULT NULL COMMENT '父级区划代码',
  `province_code` bigint(12) UNSIGNED NULL DEFAULT NULL COMMENT '省级（省份、直辖市、自治区）CODE',
  `province_name` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '省级（省份、直辖市、自治区）名称',
  `city_code` bigint(12) UNSIGNED NULL DEFAULT NULL COMMENT '地级（城市）CODE',
  `city_name` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '地级（城市）名称',
  `district_code` bigint(12) UNSIGNED NULL DEFAULT NULL COMMENT '县级（市辖区、县（旗）、县级市、自治县（自治旗）、特区、林区）CODE',
  `district_name` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '县级（市辖区、县（旗）、县级市、自治县（自治旗）、特区、林区）名称',
  `town_code` bigint(12) UNSIGNED NULL DEFAULT NULL COMMENT '乡级（镇、乡、民族乡、县辖区、街道）CODE',
  `town_name` char(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '乡级（镇、乡、民族乡、县辖区、街道）名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code`(`code`) USING BTREE COMMENT '唯一索引',
  INDEX `parent_code`(`parent_code`) USING BTREE COMMENT '父级CODE',
  INDEX `province_code`(`province_code`) USING BTREE COMMENT '省级CODE',
  INDEX `city_code`(`city_code`) USING BTREE COMMENT '市级CODE',
  INDEX `district_code`(`district_code`) USING BTREE COMMENT '县级CODE',
  INDEX `town_code`(`town_code`) USING BTREE COMMENT '乡镇CODE'
) ENGINE = InnoDB AUTO_INCREMENT = 791532 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '六级联动:国省市县乡村\r\n\r\n\r\n-- 国家/省级:32\r\ninsert into area_2019(code,name,level,parent_code)\r\nselect code,name,level,parent_code from area_code_2019 WHERE LEVEL IN(0,1);\r\n\r\n-- 市级所属省份CODE和名称:343\r\ninsert into area_2019(code,name,level,parent_code,province_code,province_name)\r\nselect a1.code,a1.name,a1.level,a1.parent_code,\r\na2.code AS province_code,a2.name AS province_name\r\nfrom area_code_2019 as a1\r\nLEFT JOIN area_2019 a2 on a1.parent_code=a2.code\r\nWHERE a1.LEVEL=2;\r\n\r\n-- 县级所属市CODE和名称:3282\r\ninsert into area_2019(code,name,level,parent_code,province_code,province_name,city_code,city_name)\r\nselect a1.code,a1.name,a1.level,a1.parent_code,\r\na2.province_code,a2.province_name,\r\na2.code AS city_code,a2.name AS city_name\r\nfrom area_code_2019 as a1\r\nLEFT JOIN area_2019 a2 on a1.parent_code=a2.code\r\nWHERE a1.LEVEL=3;\r\n\r\n-- 镇级所属县CODE和名称:43563\r\ninsert into area_2019(code,name,level,parent_code,province_code,province_name,city_code,city_name,area_code,area_name)\r\nselect a1.code,a1.name,a1.level,a1.parent_code,\r\na2.province_code,a2.province_name,\r\na2.city_code,a2.city_name,\r\na2.code AS area_code,a2.name AS area_name\r\nfrom area_code_2019 as a1\r\nLEFT JOIN area_2019 a2 on a1.parent_code=a2.code\r\nWHERE a1.LEVEL=4;\r\n\r\n-- 填充市级所属省份CODE和名称:666260\r\ninsert into area_2019(code,name,level,parent_code,province_code,province_name,city_code,city_name,area_code,area_name,town_code,town_name)\r\nselect a1.code,a1.name,a1.level,a1.parent_code,\r\na2.province_code,a2.province_name,\r\na2.city_code,a2.city_name,\r\na2.area_code,a2.area_name,\r\na2.code AS town_code,a2.name AS town_name\r\nfrom area_code_2019 as a1\r\nLEFT JOIN area_2019 a2 on a1.parent_code=a2.code\r\nWHERE a1.LEVEL=5;\r\n' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for delivery_address
-- ----------------------------
DROP TABLE IF EXISTS `delivery_address`;
CREATE TABLE `delivery_address`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tel_backup` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `zip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `defaul_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `country` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `province` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `area` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `street` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '发货地址' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) NULL DEFAULT NULL,
  `brand_id` bigint(20) NULL DEFAULT NULL,
  `specification_id` bigint(20) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logo_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(1) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods_attribute
-- ----------------------------
DROP TABLE IF EXISTS `goods_attribute`;
CREATE TABLE `goods_attribute`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NULL DEFAULT NULL,
  `specification_id` bigint(20) NULL DEFAULT NULL,
  `category` tinyint(1) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(1) NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `size` double NULL DEFAULT NULL,
  `width` double NULL DEFAULT NULL,
  `height` double NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品属性' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods_brand
-- ----------------------------
DROP TABLE IF EXISTS `goods_brand`;
CREATE TABLE `goods_brand`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logo_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(255) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品品牌' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods_category
-- ----------------------------
DROP TABLE IF EXISTS `goods_category`;
CREATE TABLE `goods_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `keywords` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sort` int(10) NULL DEFAULT NULL,
  `status` tinyint(1) NULL DEFAULT NULL,
  `parent1_id` bigint(20) NULL DEFAULT NULL,
  `parent2_id` bigint(20) NULL DEFAULT NULL,
  `parent3_id` bigint(20) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods_comment
-- ----------------------------
DROP TABLE IF EXISTS `goods_comment`;
CREATE TABLE `goods_comment`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NULL DEFAULT NULL,
  `member_id` bigint(20) NULL DEFAULT NULL,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `content` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` tinyint(1) NULL DEFAULT NULL,
  `auditor_id` bigint(20) NULL DEFAULT NULL,
  `auditor_time` datetime(0) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `support_count` int(10) NULL DEFAULT NULL,
  `oppose_count` int(10) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品评论' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods_comment_member
-- ----------------------------
DROP TABLE IF EXISTS `goods_comment_member`;
CREATE TABLE `goods_comment_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NULL DEFAULT NULL,
  `comment_id` bigint(20) NULL DEFAULT NULL,
  `member_id` bigint(20) NULL DEFAULT NULL,
  `category` tinyint(1) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品评论人员记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods_specification
-- ----------------------------
DROP TABLE IF EXISTS `goods_specification`;
CREATE TABLE `goods_specification`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NULL DEFAULT NULL,
  `status` tinyint(1) NULL DEFAULT NULL,
  `sort` int(10) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `image_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `intro` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `stock` int(10) NULL DEFAULT NULL,
  `warn_stock` int(10) NULL DEFAULT NULL,
  `price` decimal(10, 2) NULL DEFAULT NULL,
  `cost_price` decimal(10, 2) NULL DEFAULT NULL,
  `market_price` decimal(10, 2) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品规格' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for goods_statistics
-- ----------------------------
DROP TABLE IF EXISTS `goods_statistics`;
CREATE TABLE `goods_statistics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goods_id` bigint(20) NULL DEFAULT NULL,
  `visit_count` int(10) NULL DEFAULT NULL,
  `reply_count` int(10) NULL DEFAULT NULL,
  `sale_quantity` int(10) NULL DEFAULT NULL,
  `sale_amount` decimal(10, 2) NULL DEFAULT NULL,
  `purchase_quantity` int(10) NULL DEFAULT NULL,
  `purchase_amount` decimal(10, 2) NULL DEFAULT NULL,
  `cost_price` decimal(10, 2) NULL DEFAULT NULL,
  `gross_profit` decimal(10, 2) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `category` tinyint(1) NULL DEFAULT NULL,
  `platform` tinyint(1) NULL DEFAULT NULL,
  `member_id` bigint(20) NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tenant_id` bigint(20) NULL DEFAULT NULL,
  `created` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `editor` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除、1=已删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`account`) USING BTREE COMMENT '角色ID',
  INDEX `permission_id`(`category`) USING BTREE COMMENT '权限ID'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `state` tinyint(1) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `head_img_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别(性别：0为未知、1为男性、2为女性)',
  `age` tinyint(3) NULL DEFAULT NULL COMMENT '年龄',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mobile` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号码',
  `id_card` varchar(18) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `salt` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码加盐',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `birth` date NULL DEFAULT NULL COMMENT '生日',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '住址',
  `province_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属省份ID',
  `province_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属省份名称',
  `city_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属市ID',
  `city_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属市名称',
  `district_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属县ID',
  `district_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属县名称',
  `town_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `town_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member_group
-- ----------------------------
DROP TABLE IF EXISTS `member_group`;
CREATE TABLE `member_group`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE COMMENT '父级用户组ID',
  INDEX `code`(`code`) USING BTREE COMMENT '用户组CODE代码'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户组' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member_group_member
-- ----------------------------
DROP TABLE IF EXISTS `member_group_member`;
CREATE TABLE `member_group_member`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_group_id` bigint(20) NULL DEFAULT NULL,
  `member_id` bigint(20) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `member_group_id`(`member_group_id`) USING BTREE COMMENT '用户组ID',
  INDEX `member_id`(`member_id`) USING BTREE COMMENT '用户ID'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户组成员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member_group_role
-- ----------------------------
DROP TABLE IF EXISTS `member_group_role`;
CREATE TABLE `member_group_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_group_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `member_group_id`(`member_group_id`) USING BTREE COMMENT '用户组ID',
  INDEX `role_id`(`role_id`) USING BTREE COMMENT '角色ID'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户组角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member_role
-- ----------------------------
DROP TABLE IF EXISTS `member_role`;
CREATE TABLE `member_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `member_id`(`member_id`) USING BTREE COMMENT '用户ID',
  INDEX `role_id`(`role_id`) USING BTREE COMMENT '角色ID'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) NULL DEFAULT NULL,
  `address_id` bigint(20) NULL DEFAULT NULL,
  `logistics_id` bigint(20) NULL DEFAULT NULL,
  `invoice_id` bigint(20) NULL DEFAULT NULL,
  `goods_count` int(11) NULL DEFAULT NULL,
  `goods_amount_total` decimal(10, 2) NULL DEFAULT NULL,
  `order_status` tinyint(1) NULL DEFAULT NULL,
  `order_amount_total` decimal(10, 2) NULL DEFAULT NULL,
  `logistics_fee` decimal(10, 2) NULL DEFAULT NULL,
  `invoice` tinyint(1) NULL DEFAULT NULL,
  `pay_channel` tinyint(1) NULL DEFAULT NULL,
  `order_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_trade_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `pay_time` datetime(0) NULL DEFAULT NULL,
  `delivery_time` datetime(0) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_goods
-- ----------------------------
DROP TABLE IF EXISTS `order_goods`;
CREATE TABLE `order_goods`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NULL DEFAULT NULL,
  `goods_id` bigint(20) NULL DEFAULT NULL,
  `goods_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goods_price` decimal(10, 2) NULL DEFAULT NULL,
  `goods_marque` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goods_store_barcode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goods_mode_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `goods_mode_params` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `discount_rate` decimal(10, 2) NULL DEFAULT NULL,
  `discount_amount` decimal(10, 2) NULL DEFAULT NULL,
  `good_number` int(11) NULL DEFAULT NULL,
  `subtotal` decimal(10, 2) NULL DEFAULT NULL,
  `goods_exists` tinyint(1) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单商品快照' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_invoice
-- ----------------------------
DROP TABLE IF EXISTS `order_invoice`;
CREATE TABLE `order_invoice`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NULL DEFAULT NULL,
  `vat` int(11) NULL DEFAULT NULL,
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `amount` decimal(10, 2) NULL DEFAULT NULL,
  `tax_no` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tax` decimal(10, 2) NULL DEFAULT NULL,
  `vat_company_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `vat_company_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `vat_tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `vat_bank_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `vat_bank_account` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单发票' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_logistics
-- ----------------------------
DROP TABLE IF EXISTS `order_logistics`;
CREATE TABLE `order_logistics`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NULL DEFAULT NULL,
  `consignee_real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consignee_tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consignee_tel_backup` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consignee_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consignee_zip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logistics_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logistics_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `express_no` int(11) NULL DEFAULT NULL,
  `order_logistics_status` tinyint(1) NULL DEFAULT NULL,
  `logistics_fee` decimal(10, 2) NULL DEFAULT NULL,
  `agency_fee` decimal(10, 2) NULL DEFAULT NULL,
  `delivery_amount` decimal(10, 2) NULL DEFAULT NULL,
  `logistics_settlement_status` tinyint(1) NULL DEFAULT NULL,
  `logistics_intro_last` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logistics_intro` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logistics_create_time` datetime(0) NULL DEFAULT NULL,
  `logistics_update_time` datetime(0) NULL DEFAULT NULL,
  `logistics_settlement_time` datetime(0) NULL DEFAULT NULL,
  `logistics_pay_channel` tinyint(1) NULL DEFAULT NULL,
  `logistics_pay_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `reconciliation_status` tinyint(1) NULL DEFAULT NULL,
  `reconciliation_time` datetime(0) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单物流' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for order_returns
-- ----------------------------
DROP TABLE IF EXISTS `order_returns`;
CREATE TABLE `order_returns`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NULL DEFAULT NULL,
  `returns_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `express_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consignee_real_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consignee_tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consignee_tel_backup` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consignee_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `consignee_zip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logistics_type` tinyint(1) NULL DEFAULT NULL,
  `logistics_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logistics_fee` decimal(10, 2) NULL DEFAULT NULL,
  `order_logistics_status` tinyint(1) NULL DEFAULT NULL,
  `logistics_intro_last` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logistics_intro` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `logistics_update_time` datetime(0) NULL DEFAULT NULL,
  `logistics_create_time` datetime(0) NULL DEFAULT NULL,
  `returns_type` tinyint(1) NULL DEFAULT NULL,
  `handling_way` tinyint(1) NULL DEFAULT NULL,
  `returns_amount` decimal(10, 2) NULL DEFAULT NULL,
  `seller_punish_fee` decimal(10, 2) NULL DEFAULT NULL,
  `return_submit_time` datetime(0) NULL DEFAULT NULL,
  `handling_time` datetime(0) NULL DEFAULT NULL,
  `returns_reason` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单退货' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `category` tinyint(1) NULL DEFAULT NULL,
  `uri` bigint(20) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE COMMENT '父级权限ID',
  INDEX `code`(`code`) USING BTREE COMMENT '权限CODE代码'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) NULL DEFAULT NULL,
  `code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `parent_id`(`parent_id`) USING BTREE COMMENT '父级权限ID',
  INDEX `code`(`code`) USING BTREE COMMENT '权限CODE代码'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL,
  `permission_id` bigint(20) NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE COMMENT '角色ID',
  INDEX `permission_id`(`permission_id`) USING BTREE COMMENT '权限ID'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tenant
-- ----------------------------
DROP TABLE IF EXISTS `tenant`;
CREATE TABLE `tenant`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `intro` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '租户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '分布式事务记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for wx_pay_notify
-- ----------------------------
DROP TABLE IF EXISTS `wx_pay_notify`;
CREATE TABLE `wx_pay_notify`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `return_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `return_msg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mch_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `result_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `err_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `err_code_des` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `openid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `trade_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `total_fee` int(11) NULL DEFAULT NULL,
  `settlement_total_fee` int(11) NULL DEFAULT NULL,
  `transaction_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `out_trade_no` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `time_end` datetime(0) NULL DEFAULT NULL,
  `data` varchar(3000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `created` datetime(0) NULL DEFAULT NULL,
  `creator` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `edited` datetime(0) NULL DEFAULT NULL,
  `editor` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deleted` tinyint(1) UNSIGNED ZEROFILL NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '微信支付通知记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- View structure for view_member_permission
-- ----------------------------
DROP VIEW IF EXISTS `view_member_permission`;
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `view_member_permission` AS select `mgm`.`member_id` AS `member_id`,`p`.`id` AS `id`,`p`.`parent_id` AS `parent_id`,`p`.`code` AS `code`,`p`.`name` AS `name`,`p`.`intro` AS `intro`,`p`.`category` AS `category`,`p`.`uri` AS `uri`,`p`.`created` AS `created`,`p`.`creator` AS `creator`,`p`.`edited` AS `edited`,`p`.`editor` AS `editor`,`p`.`deleted` AS `deleted` from (((`member_group_member` `mgm` left join `member_group_role` `mgr` on((`mgm`.`member_group_id` = `mgr`.`member_group_id`))) left join `role_permission` `rp` on((`mgr`.`role_id` = `rp`.`role_id`))) left join `permission` `p` on((`rp`.`permission_id` = `p`.`id`))) group by `mgm`.`member_id`,`p`.`id`;

-- ----------------------------
-- View structure for view_role_code_permission
-- ----------------------------
DROP VIEW IF EXISTS `view_role_code_permission`;
CREATE ALGORITHM = UNDEFINED DEFINER = `root`@`localhost` SQL SECURITY DEFINER VIEW `view_role_code_permission` AS select `r`.`code` AS `role_code`,`p`.`id` AS `id`,`p`.`parent_id` AS `parent_id`,`p`.`code` AS `code`,`p`.`name` AS `name`,`p`.`intro` AS `intro`,`p`.`category` AS `category`,`p`.`uri` AS `uri`,`p`.`created` AS `created`,`p`.`creator` AS `creator`,`p`.`edited` AS `edited`,`p`.`editor` AS `editor`,`p`.`deleted` AS `deleted` from ((`role` `r` left join `role_permission` `rp` on((`r`.`id` = `rp`.`role_id`))) left join `permission` `p` on((`rp`.`permission_id` = `p`.`id`))) group by `r`.`code`,`p`.`id`;

SET FOREIGN_KEY_CHECKS = 1;
