/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50531
 Source Host           : localhost:3306
 Source Schema         : mysystem

 Target Server Type    : MySQL
 Target Server Version : 50531
 File Encoding         : 65001

 Date: 08/12/2025 21:24:18
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for admin
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `idnum` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '学号',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `gradeId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '班级',
  `birth` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出生年月',
  `contactInfo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系方式',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `role` int(11) NOT NULL DEFAULT 1 COMMENT '角色标识\r\n\r\n',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES (7, '1', '1', 'Female', NULL, '1', '2025-01-11', '1', '2', 1, '1', 'http://localhost:9090/files/1745311389145-logo2.png');
INSERT INTO `admin` VALUES (12, '23', '2', 'Male', NULL, '2', '2025-01-01', '1', '1', 1, '0817', 'http://localhost:9090/files/default.jpg');
INSERT INTO `admin` VALUES (16, '4', '3', 'Female', NULL, NULL, '2025-04-21', '1', '1', 1, '3', 'http://localhost:9090/files/default.jpg');
INSERT INTO `admin` VALUES (17, '5', '5', 'Male', NULL, NULL, '2025-04-21', '5', '5', 1, '5', 'http://localhost:9090/files/default.jpg');
INSERT INTO `admin` VALUES (18, '7', '5', 'Male', NULL, '3', '2025-05-13', '31', '123', 1, '231', 'http://localhost:9090/files/1748008538686-logo2.png');
INSERT INTO `admin` VALUES (19, NULL, NULL, 'Male', NULL, NULL, NULL, NULL, NULL, 1, '123456', 'http://localhost:9090/files/default.jpg');
INSERT INTO `admin` VALUES (20, '33', '3', 'Female', NULL, '2', '2025-12-23', 'ds', NULL, 1, '1', 'http://localhost:9090/files/default.jpg');
INSERT INTO `admin` VALUES (21, 'john', '111', 'Male', NULL, '2', NULL, NULL, NULL, 1, '1', 'http://localhost:9090/files/default.jpg');

-- ----------------------------
-- Table structure for collect
-- ----------------------------
DROP TABLE IF EXISTS `collect`;
CREATE TABLE `collect`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `courseresourceId` int(20) NOT NULL COMMENT '所属课程资源',
  `adminId` int(20) NOT NULL COMMENT '所属用户',
  `createtime` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建时间',
  `chapterId` int(20) NOT NULL COMMENT '所属章节',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of collect
-- ----------------------------
INSERT INTO `collect` VALUES (14, 59, 7, '2025-04-18 17:37:38', 1);
INSERT INTO `collect` VALUES (16, 59, 6, '2025-04-18 23:12:44', 1);
INSERT INTO `collect` VALUES (18, 70, 6, '2025-04-19 13:11:19', 1);
INSERT INTO `collect` VALUES (19, 78, 6, '2025-04-19 17:53:56', 1);
INSERT INTO `collect` VALUES (27, 90, 6, '2025-04-20 00:15:31', 1);
INSERT INTO `collect` VALUES (28, 83, 6, '2025-04-20 00:15:32', 1);
INSERT INTO `collect` VALUES (29, 80, 6, '2025-04-20 00:15:32', 1);
INSERT INTO `collect` VALUES (30, 88, 6, '2025-04-20 00:15:33', 1);
INSERT INTO `collect` VALUES (33, 100, 6, '2025-04-20 17:34:21', 1);
INSERT INTO `collect` VALUES (34, 118, 6, '2025-04-20 17:34:28', 4);
INSERT INTO `collect` VALUES (35, 115, 6, '2025-04-20 17:34:33', 3);
INSERT INTO `collect` VALUES (37, 109, 6, '2025-04-20 17:55:53', 2);
INSERT INTO `collect` VALUES (38, 97, 6, '2025-04-20 21:11:44', 1);
INSERT INTO `collect` VALUES (40, 97, 7, '2025-04-22 16:52:38', 1);
INSERT INTO `collect` VALUES (41, 98, 7, '2025-04-22 16:52:38', 1);
INSERT INTO `collect` VALUES (43, 119, 7, '2025-04-22 16:52:43', 4);
INSERT INTO `collect` VALUES (44, 117, 7, '2025-04-22 16:52:43', 4);
INSERT INTO `collect` VALUES (51, 120, 7, '2025-05-23 22:36:55', 1);
INSERT INTO `collect` VALUES (53, 95, 6, '2025-05-27 15:29:57', 1);
INSERT INTO `collect` VALUES (54, 98, 6, '2025-05-27 15:34:04', 1);

-- ----------------------------
-- Table structure for courseresource
-- ----------------------------
DROP TABLE IF EXISTS `courseresource`;
CREATE TABLE `courseresource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '资源标题',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件存储路径',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件类型',
  `time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `uploaderId` int(11) NULL DEFAULT NULL COMMENT '上传者id',
  `img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '预览图路径',
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件所属类别\r\n',
  `downloadcount` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '下载次数',
  `likecount` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '点赞次数',
  `collectcount` int(11) UNSIGNED NULL DEFAULT 0 COMMENT '收藏次数',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uploaderId`(`uploaderId`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 126 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of courseresource
-- ----------------------------
INSERT INTO `courseresource` VALUES (95, 'Installation of Sunflowers', 'http://localhost:9090/files/chapter/1/1745129245962-01.5向日葵的安装【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 06:07:26', NULL, 'http://localhost:9090/files/defaultresource.jpg', '1', 6, 14, 1);
INSERT INTO `courseresource` VALUES (97, 'Career development of programmers', 'http://localhost:9090/files/chapter/1/1745129354095-01.2程序员职业发展【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 06:09:14', NULL, 'http://localhost:9090/files/defaultresource.jpg', '5', 4, 10, 2);
INSERT INTO `courseresource` VALUES (98, 'What is a program - compilation and debugging', 'http://localhost:9090/files/chapter/1/1745129375129-01.6什么是程序-编译与调试【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 06:09:35', NULL, 'http://localhost:9090/files/defaultresource.jpg', '1', 6, 12, 2);
INSERT INTO `courseresource` VALUES (99, 'Installation package link', 'http://localhost:9090/files/chapter/3/1745129407196-1.3【课件】下载课件里面含安装包链接【公众号：研料库，料最全】.pdf', 'document', '2025-04-20 06:10:07', NULL, 'http://localhost:9090/files/defaultresource.jpg', '3', 5, 13, 0);
INSERT INTO `courseresource` VALUES (100, ' CLion installation tutorial for Mac computers', 'http://localhost:9090/files/chapter/1/1745129447780-1.4【课件】Mac电脑的CLion安装教程【公众号：研料库，料最全】.pdf', 'document', '2025-04-20 06:10:47', NULL, 'http://localhost:9090/files/defaultresource.jpg', '1', 0, 6, 1);
INSERT INTO `courseresource` VALUES (101, 'Data types - Constants - Variables (Integer - Floating-point - Character)', 'http://localhost:9090/files/chapter/2/1745140188174-02.1数据类型-常量-变量(整型-浮点-字符)【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 09:09:48', NULL, 'http://localhost:9090/files/defaultresource.jpg', '2', 0, 8, 0);
INSERT INTO `courseresource` VALUES (104, ' Hybrid Operations - Explanation of printf', 'http://localhost:9090/files/chapter/2/1745140423412-02.2混合运算-printf讲解【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 09:13:43', NULL, 'http://localhost:9090/files/defaultresource.jpg', '2', 0, 8, 0);
INSERT INTO `courseresource` VALUES (106, 'Integer base conversion', 'http://localhost:9090/files/chapter/2/1745140447620-02.3整型进制转换【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 09:14:07', NULL, 'http://localhost:9090/files/defaultresource.jpg', '2', 5, 9, 0);
INSERT INTO `courseresource` VALUES (107, 'scanf reads standard input', 'http://localhost:9090/files/chapter/2/1745140460180-02.4scanf读取标准输入【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 09:14:20', NULL, 'http://localhost:9090/files/defaultresource.jpg', '2', 0, 1, 0);
INSERT INTO `courseresource` VALUES (108, 'Data types - Constants - Variables (Integer - Floating-point - Character)', 'http://localhost:9090/files/chapter/2/1745140557107-2.1【课件】数据类型-常量-变量(整型-浮点-字符)【公众号：研料库，料最全】.pdf', 'document', '2025-04-20 09:15:57', NULL, 'http://localhost:9090/files/defaultresource.jpg', '2', 0, 0, 0);
INSERT INTO `courseresource` VALUES (109, '混合运算-printf讲解', 'http://localhost:9090/files/chapter/2/1745140573946-2.2【课件】混合运算-printf讲解【公众号：研料库，料最全】.pdf', 'document', '2025-04-20 09:16:13', NULL, 'http://localhost:9090/files/defaultresource.jpg', '2', 0, 6, 1);
INSERT INTO `courseresource` VALUES (110, 'scanf读取标准输入', 'http://localhost:9090/files/chapter/2/1745140727421-2.4【课件】scanf读取标准输入【公众号：研料库，料最全】.pdf', 'document', '2025-04-20 09:18:47', NULL, 'http://localhost:9090/files/defaultresource.jpg', '2', 0, 10, 0);
INSERT INTO `courseresource` VALUES (111, '整型进制转换', 'http://localhost:9090/files/chapter/2/1745140747579-2.3【课件】整型进制转换【公众号：研料库，料最全】.pdf', 'document', '2025-04-20 09:19:07', NULL, 'http://localhost:9090/files/defaultresource.jpg', '2', 0, 0, 0);
INSERT INTO `courseresource` VALUES (112, '算术运算符与关系运算符', 'http://localhost:9090/files/chapter/3/1745140996759-03.2算术运算符与关系运算符【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 09:23:16', NULL, 'http://localhost:9090/files/defaultresource.jpg', '3', 0, 3, 0);
INSERT INTO `courseresource` VALUES (113, '逻辑运算符与赋值运算符，求字节运算符', 'http://localhost:9090/files/chapter/3/1745141018002-03.3逻辑运算符与赋值运算符，求字节运算符【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 09:23:38', NULL, 'http://localhost:9090/files/defaultresource.jpg', '3', 0, 11, 0);
INSERT INTO `courseresource` VALUES (114, '逻辑运算符与赋值运算符，求字节运算符', 'http://localhost:9090/files/chapter/3/1745141070522-3.3【课件】逻辑运算符与赋值运算符，求字节运算符【公众号：研料库，料最全】.pdf', 'document', '2025-04-20 09:24:30', NULL, 'http://localhost:9090/files/defaultresource.jpg', '3', 0, 1, 0);
INSERT INTO `courseresource` VALUES (115, '算术运算符与关系运算符', 'http://localhost:9090/files/chapter/3/1745141082359-3.2【课件】算术运算符与关系运算符【公众号：研料库，料最全】.pdf', 'document', '2025-04-20 09:24:42', NULL, 'http://localhost:9090/files/defaultresource.jpg', '3', 0, 1, 1);
INSERT INTO `courseresource` VALUES (116, '选择if-else讲解', 'http://localhost:9090/files/chapter/4/1745141246456-04.2选择if-else讲解【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 09:27:26', NULL, 'http://localhost:9090/files/defaultresource.jpg', '4', 0, 10, 0);
INSERT INTO `courseresource` VALUES (117, '循环while，for讲解，continue，break', 'http://localhost:9090/files/chapter/4/1745141259420-04.3循环while，for讲解，continue，break讲解【公众号：研料库，料最全】.mp4', 'video', '2025-04-20 09:27:39', NULL, 'http://localhost:9090/files/defaultresource.jpg', '4', 0, 5, 1);
INSERT INTO `courseresource` VALUES (118, '选择if-else讲解', 'http://localhost:9090/files/chapter/4/1745141289565-4.2【课件】选择if-else讲解【公众号：研料库，料最全】.pdf', 'document', '2025-04-20 09:28:09', NULL, 'http://localhost:9090/files/defaultresource.jpg', '4', 0, 3, 1);
INSERT INTO `courseresource` VALUES (125, 'career development', 'http://localhost:9090/files/1765183679529-Career development.mp4', NULL, '2025-12-08 08:48:00', NULL, 'http://localhost:9090/files/defaultresource.jpg', 'Other', 0, 0, 0);

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
  `id` int(11) NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件路径',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '文件名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of file
-- ----------------------------

-- ----------------------------
-- Table structure for grade
-- ----------------------------
DROP TABLE IF EXISTS `grade`;
CREATE TABLE `grade`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `gradeName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '班级名称',
  `brief` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '班级简介',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of grade
-- ----------------------------
INSERT INTO `grade` VALUES (1, 'E231', 'Class 1, Big Data Major, Grade 2023');
INSERT INTO `grade` VALUES (2, 'E232', 'Class 2, Big Data Major, Grade 2023');
INSERT INTO `grade` VALUES (3, 'E233', 'Class 3, Big Data Major, Grade 2023');
INSERT INTO `grade` VALUES (4, 'E234', 'Class 1, Big Data Major, Grade 2023');

-- ----------------------------
-- Table structure for manager
-- ----------------------------
DROP TABLE IF EXISTS `manager`;
CREATE TABLE `manager`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `IDNUM` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职工号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `role` int(11) NOT NULL DEFAULT 3 COMMENT '角色标识',
  `gradeId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '班级',
  `birth` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出生日期\r\n',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `contactInfo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系方式\r\n',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of manager
-- ----------------------------
INSERT INTO `manager` VALUES (9, 'manager', NULL, '1', '1', 3, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '留言内容',
  `userId` int(11) NULL DEFAULT NULL COMMENT '用户ID',
  `role` int(11) NULL DEFAULT NULL COMMENT '用户角色',
  `time` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '留言时间',
  `parentId` int(11) NULL DEFAULT NULL COMMENT '父id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '留言表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of message
-- ----------------------------
INSERT INTO `message` VALUES (5, '11', 6, 2, '2025-05-23 18:08:00', 4);
INSERT INTO `message` VALUES (6, '111', 6, 2, '2025-05-23 18:08:02', 0);
INSERT INTO `message` VALUES (7, '11', 7, 1, '2025-05-23 22:37:04', 0);
INSERT INTO `message` VALUES (8, '21', 9, 3, '2025-05-27 15:25:23', 0);
INSERT INTO `message` VALUES (10, '21', 6, 2, '2025-05-27 15:32:45', 0);
INSERT INTO `message` VALUES (12, '11', 6, 2, '2025-05-27 15:34:11', 0);
INSERT INTO `message` VALUES (13, '1', 7, 1, '2025-12-08 15:48:28', 0);
INSERT INTO `message` VALUES (14, '111', 7, 1, '2025-12-08 16:32:24', 0);
INSERT INTO `message` VALUES (15, '1', 7, 1, '2025-12-08 16:44:23', 0);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户名',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '性别',
  `IDNUM` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '职工号',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `gradeId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '班级',
  `birth` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '出生日期\r\n',
  `contactInfo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '联系方式\r\n',
  `role` int(11) NOT NULL DEFAULT 2 COMMENT '角色标识',
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 'Professor Wang', 'Male', 'T001', '4314', '1', '1988-05-20', '987654321', 2, 'wangwu@example.com', 'http://localhost:9090/files/1741855813926-深红之网.png');
INSERT INTO `teacher` VALUES (4, 'Wang Wu', 'Male', 'T003', 'password789', '2', '1985-11-15', '555555555', 2, 'wangwu@example.com', 'http://localhost:9090/files/default.jpg');
INSERT INTO `teacher` VALUES (6, '2', '1', '1', '1', '1', '2025-01-15', '1', 2, '1', 'http://localhost:9090/files/1745079404898-1743484003988-logo2 (1).png');
INSERT INTO `teacher` VALUES (7, '1', 'Male', '1', '1', '1', '2025-01-07', '1', 2, '1', 'http://localhost:9090/files/default.jpg');
INSERT INTO `teacher` VALUES (8, '3', 'Female', '3', '3', '3', '2025-03-24', '3', 2, '3', 'http://localhost:9090/files/default.jpg');

-- ----------------------------
-- Table structure for useraction
-- ----------------------------
DROP TABLE IF EXISTS `useraction`;
CREATE TABLE `useraction`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `resource_id` int(11) NOT NULL COMMENT '课程资源ID',
  `liked` tinyint(1) NULL DEFAULT 0 COMMENT '是否点赞，0-未点赞，1-已点赞',
  `collected` tinyint(1) NULL DEFAULT 0 COMMENT '是否收藏，0-未收藏，1-已收藏',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_resource`(`user_id`, `resource_id`) USING BTREE COMMENT '用户和资源唯一组合',
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_resource_id`(`resource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户交互表-记录用户与课程资源的交互行为（点赞、收藏）' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of useraction
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
