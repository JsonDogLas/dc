/*
Navicat MySQL Data Transfer

Source Server         : anzhuang
Source Server Version : 50537
Source Host           : localhost:3306
Source Database       : api

Target Server Type    : MYSQL
Target Server Version : 50537
File Encoding         : 65001

Date: 2018-09-03 15:38:47
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `api_sign_file`
-- ----------------------------
DROP TABLE IF EXISTS `api_sign_file`;
CREATE TABLE `api_sign_file` (
  `id` varchar(32) NOT NULL,
  `sign_file_id` varchar(32) NOT NULL,
  `status` varchar(1) NOT NULL,
  `title` varchar(500) NOT NULL,
  `create_time` datetime NOT NULL,
  `create_user` varchar(32) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` varchar(255) DEFAULT NULL,
  `spare1` varchar(100) DEFAULT NULL,
  `spare2` varchar(100) DEFAULT NULL,
  `spare3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of api_sign_file
-- ----------------------------

-- ----------------------------
-- Table structure for `api_user`
-- ----------------------------
DROP TABLE IF EXISTS `api_user`;
CREATE TABLE `api_user` (
  `id` varchar(32) NOT NULL,
  `key_id` varchar(100) NOT NULL,
  `app_id` varchar(32) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_user` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of api_user
-- ----------------------------


-- ----------------------------
-- Table structure for `p_g_file`
-- ----------------------------
DROP TABLE IF EXISTS `api_g_file`;
CREATE TABLE `api_g_file` (
  `id` varchar(32) NOT NULL,
  `file_name` varchar(300) NOT NULL COMMENT '文件名称（带后缀）',
  `ext_name` varchar(20) NOT NULL COMMENT '文件后缀',
  `virtual_path` varchar(300) NOT NULL COMMENT '文件虚拟路径',
  `size` longblob NOT NULL COMMENT '文件大小',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `type` varchar(1) NOT NULL COMMENT '类型9API系统缓存文件 10API系统ftp文件 11API签章文件',
  `create_user` varchar(40) NOT NULL,
  `create_time` datetime NOT NULL,
  `update_user` varchar(40) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `spare1` varchar(100) DEFAULT NULL,
  `spare2` varchar(100) DEFAULT NULL,
  `spare3` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of p_g_file
-- ----------------------------

-- ----------------------------
-- Table structure for `yq_sign_file_log`
-- ----------------------------
DROP TABLE IF EXISTS `api_sign_file_log`;
CREATE TABLE `api_sign_file_log` (
  `id` varchar(32) NOT NULL,
  `sign_file_id` varchar(32) DEFAULT NULL COMMENT '签名文件id（api_sign_file）',
  `original_file_id` varchar(32) NOT NULL COMMENT '原始文件id（api_g_file）',
  `after_sign_file_id` varchar(32) NOT NULL COMMENT '签名后的文件id',
  `sign_type` varchar(1) NOT NULL COMMENT '签名类型 1 修订签 2验证签 3普通签',
  `key_id` varchar(100) DEFAULT NULL COMMENT '证书序列号/key id',
  `stamp_file_ids` varchar(640) NOT NULL COMMENT '签章id集合（api_g_file）英文逗号隔开',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_user` varchar(40) NOT NULL COMMENT '创建用户id',
  `spare1` varchar(100) DEFAULT NULL COMMENT '备用字段1',
  `spare2` varchar(100) DEFAULT NULL COMMENT '备用字段二',
  `spare3` varchar(100) DEFAULT NULL COMMENT '备用字段3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of yq_sign_file_log
-- ----------------------------
