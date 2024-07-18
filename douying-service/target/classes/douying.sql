/*
SQLyog Community v13.2.1 (64 bit)
MySQL - 8.0.30 : Database - douying
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`douying` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `douying`;

/*Table structure for table `captcha` */

DROP TABLE IF EXISTS `captcha`;

CREATE TABLE `captcha` (
  `uuid` char(36) NOT NULL COMMENT 'uuid',
  `code` varchar(6) NOT NULL COMMENT '验证码',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统验证码';

/*Data for the table `captcha` */

insert  into `captcha`(`uuid`,`code`,`expire_time`) values 
('95f4c18f-f156-40e9-87c1-b94be6db437f','4fepx','2024-06-24 17:45:01'),
('e21c7900-6cce-4889-a22c-9c8d4b478494','bgn88','2024-06-24 17:44:18');

/*Table structure for table `favorites` */

DROP TABLE IF EXISTS `favorites`;

CREATE TABLE `favorites` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `gmt_created` datetime DEFAULT NULL,
  `gmt_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `favorites` */

insert  into `favorites`(`id`,`name`,`description`,`user_id`,`is_deleted`,`gmt_created`,`gmt_updated`) values 
(27,'音乐','流行动感音乐',18,0,'2024-06-15 21:35:45','2024-06-16 15:54:36'),
(28,'游戏','LOL精彩',18,0,'2024-06-16 18:10:36','2024-06-16 18:10:36'),
(30,'英雄联盟','游戏集锦',19,0,'2024-06-24 00:20:41','2024-06-24 00:20:41'),
(31,'默认收藏夹',NULL,20,0,'2024-06-24 17:40:50','2024-06-24 17:40:50');

/*Table structure for table `favorites_video` */

DROP TABLE IF EXISTS `favorites_video`;

CREATE TABLE `favorites_video` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `favorites_id` bigint NOT NULL,
  `video_id` bigint DEFAULT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_f_id_v_id_u_id` (`favorites_id`,`video_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `favorites_video` */

insert  into `favorites_video`(`id`,`favorites_id`,`video_id`,`user_id`) values 
(39,27,4848,18),
(38,30,4834,19),
(40,30,4841,19);

/*Table structure for table `file` */

DROP TABLE IF EXISTS `file`;

CREATE TABLE `file` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_key` varchar(255) DEFAULT NULL COMMENT '文件链接',
  `format` varchar(20) DEFAULT NULL COMMENT '文件的格式',
  `type` varchar(20) DEFAULT NULL COMMENT '文件类型',
  `duration` varchar(20) DEFAULT NULL COMMENT '文件的持续时间',
  `size` bigint DEFAULT NULL COMMENT '文件大小',
  `user_id` bigint DEFAULT NULL COMMENT '归属的用户ID',
  `is_deleted` tinyint DEFAULT NULL,
  `gmt_created` datetime DEFAULT NULL,
  `gmt_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=197 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `file` */

insert  into `file`(`id`,`file_key`,`format`,`type`,`duration`,`size`,`user_id`,`is_deleted`,`gmt_created`,`gmt_updated`) values 
(119,'lhmc8SAydmd3Q_5EPOuZUz2Hzb0B','video/mp4','视频',NULL,8713994,18,NULL,'2024-06-23 20:29:43','2024-06-23 20:29:43'),
(120,'lhmc8SAydmd3Q_5EPOuZUz2Hzb0B?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-23 20:31:46','2024-06-23 20:31:46'),
(121,'lsqPDm5WAWJePMTfvAHVrNud76IJ','video/mp4','视频',NULL,17669222,18,NULL,'2024-06-23 21:55:44','2024-06-23 21:55:44'),
(122,'lsqPDm5WAWJePMTfvAHVrNud76IJ?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-23 21:56:23','2024-06-23 21:56:23'),
(123,'Fl8me-zwib6AcEydN2eZ45yJkAuS','video/mp4','视频',NULL,3900355,NULL,NULL,'2024-06-23 22:15:20','2024-06-23 22:15:20'),
(124,'Fl8me-zwib6AcEydN2eZ45yJkAuS?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-23 22:16:04','2024-06-23 22:16:04'),
(125,'FvvIvaUr2gI-ksnDBi_BjHoQnie1','video/mp4','视频',NULL,3270147,18,NULL,'2024-06-23 22:34:01','2024-06-23 22:34:01'),
(126,'FvvIvaUr2gI-ksnDBi_BjHoQnie1?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-23 22:34:32','2024-06-23 22:34:32'),
(127,'ltw9cdFNpUcAD_0d8mXY_gnDaxNW','video/mp4','视频',NULL,26376781,NULL,NULL,'2024-06-24 00:05:19','2024-06-24 00:05:19'),
(128,'ltw9cdFNpUcAD_0d8mXY_gnDaxNW?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-24 00:06:59','2024-06-24 00:06:59'),
(129,'Fhs0QsGpce4tD9wFMBiHFOjAw5Io','video/mp4','视频',NULL,3816967,19,NULL,'2024-06-24 00:34:55','2024-06-24 00:34:55'),
(130,'Fhs0QsGpce4tD9wFMBiHFOjAw5Io?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 00:36:04','2024-06-24 00:36:04'),
(131,'FvBrbVK8WjOy6UQ4GmW1q_Rdvwlj','video/mp4','视频',NULL,3324350,18,NULL,'2024-06-24 01:51:19','2024-06-24 01:51:19'),
(132,'FvBrbVK8WjOy6UQ4GmW1q_Rdvwlj?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-24 01:52:21','2024-06-24 01:52:21'),
(133,'lk6cSm9Er2f2iA2DAyKSmh6w_HUY','video/mp4','视频',NULL,23993606,19,NULL,'2024-06-24 01:59:04','2024-06-24 01:59:04'),
(134,'lk6cSm9Er2f2iA2DAyKSmh6w_HUY?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 01:59:31','2024-06-24 01:59:31'),
(135,'FkO9vvg9z_kRAmBLVldvHtW1ggxG','video/mp4','视频',NULL,1171584,19,NULL,'2024-06-24 02:40:49','2024-06-24 02:40:49'),
(136,'FkO9vvg9z_kRAmBLVldvHtW1ggxG?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 02:41:31','2024-06-24 02:41:31'),
(137,'FlduslgPii4uk8xTQkzWqr6Y-ZE4','video/mp4','视频',NULL,2890176,18,NULL,'2024-06-24 12:17:55','2024-06-24 12:17:55'),
(138,'FlduslgPii4uk8xTQkzWqr6Y-ZE4?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-24 12:18:50','2024-06-24 12:18:50'),
(139,'lvEepyyij5sVjios6dGUgDZSURDM','video/mp4','视频',NULL,8475698,19,NULL,'2024-06-24 12:36:05','2024-06-24 12:36:05'),
(140,'lvEepyyij5sVjios6dGUgDZSURDM?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 12:36:51','2024-06-24 12:36:51'),
(141,'ljsDPPv8HWamwUTx5ZXyZxXqwQEW','video/mp4','视频',NULL,24004787,NULL,NULL,'2024-06-24 13:12:56','2024-06-24 13:12:56'),
(142,'ljsDPPv8HWamwUTx5ZXyZxXqwQEW?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 13:13:50','2024-06-24 13:13:50'),
(143,'li0UuOXKaDEnEjJXRHKg87OgIXqv','video/mp4','视频',NULL,11974741,NULL,NULL,'2024-06-24 13:23:41','2024-06-24 13:23:41'),
(144,'li0UuOXKaDEnEjJXRHKg87OgIXqv?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 13:23:57','2024-06-24 13:23:57'),
(145,'lupKnxz4WKNKi5he3WqRc-_QPw7h','video/mp4','视频',NULL,41544025,19,NULL,'2024-06-24 13:39:48','2024-06-24 13:39:48'),
(146,'lupKnxz4WKNKi5he3WqRc-_QPw7h?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 13:40:22','2024-06-24 13:40:22'),
(147,'liEvxIOnGw1DTUQSpB0CxOohHdzJ','video/mp4','视频',NULL,26266699,NULL,NULL,'2024-06-24 13:47:34','2024-06-24 13:47:34'),
(148,'liEvxIOnGw1DTUQSpB0CxOohHdzJ?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 13:49:17','2024-06-24 13:49:17'),
(149,'FmB_vYiy8aAEXS-8JbItWh182fLz','video/mp4','视频',NULL,2502468,19,NULL,'2024-06-24 14:08:49','2024-06-24 14:08:49'),
(150,'FmB_vYiy8aAEXS-8JbItWh182fLz?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 14:09:27','2024-06-24 14:09:27'),
(151,'lhYm7aPHNlj-tpAhOY3vrcJzupxq','video/mp4','视频',NULL,25680829,18,NULL,'2024-06-24 16:04:35','2024-06-24 16:04:35'),
(152,'lhYm7aPHNlj-tpAhOY3vrcJzupxq?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-24 16:05:05','2024-06-24 16:05:05'),
(153,'Foy0XARV6MNR37RS_SVSBzul-TB9','video/mp4','视频',NULL,1410941,18,NULL,'2024-06-24 16:25:19','2024-06-24 16:25:19'),
(154,'Foy0XARV6MNR37RS_SVSBzul-TB9?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-24 16:26:13','2024-06-24 16:26:13'),
(155,'Fl8me-zwib6AcEydN2eZ45yJkAuS','video/mp4','视频',NULL,3900355,19,NULL,'2024-06-24 16:37:54','2024-06-24 16:37:54'),
(156,'Fl8me-zwib6AcEydN2eZ45yJkAuS?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 16:38:23','2024-06-24 16:38:23'),
(157,'ltQx14o1ZXS_wz3v396g_EfpnY-u','video/mp4','视频',NULL,76903705,19,NULL,'2024-06-24 16:58:36','2024-06-24 16:58:36'),
(158,'ltQx14o1ZXS_wz3v396g_EfpnY-u?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 17:01:53','2024-06-24 17:01:53'),
(159,'luWnQRr9OY24iTuVB_RTyxqaAlbO','video/mp4','视频',NULL,27316070,NULL,NULL,'2024-06-24 17:08:40','2024-06-24 17:08:40'),
(160,'li0UuOXKaDEnEjJXRHKg87OgIXqv','video/mp4','视频',NULL,11974741,19,NULL,'2024-06-24 17:10:01','2024-06-24 17:10:01'),
(161,'li0UuOXKaDEnEjJXRHKg87OgIXqv?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 17:10:15','2024-06-24 17:10:15'),
(162,'ljsDPPv8HWamwUTx5ZXyZxXqwQEW','video/mp4','视频',NULL,24004787,19,NULL,'2024-06-24 17:19:36','2024-06-24 17:19:36'),
(163,'ljsDPPv8HWamwUTx5ZXyZxXqwQEW?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 17:19:55','2024-06-24 17:19:55'),
(164,'FgcXEWhxc4nGLC8yvlVxqc3w1JzC','video/mp4','视频',NULL,4174428,NULL,NULL,'2024-06-24 17:22:41','2024-06-24 17:22:41'),
(165,'FgcXEWhxc4nGLC8yvlVxqc3w1JzC?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 17:23:07','2024-06-24 17:23:07'),
(166,'luWnQRr9OY24iTuVB_RTyxqaAlbO','video/mp4','视频',NULL,27316070,NULL,NULL,'2024-06-24 17:30:00','2024-06-24 17:30:00'),
(167,'luWnQRr9OY24iTuVB_RTyxqaAlbO?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 17:30:04','2024-06-24 17:30:04'),
(168,'ljsDPPv8HWamwUTx5ZXyZxXqwQEW','video/mp4','视频',NULL,24004787,19,NULL,'2024-06-24 17:38:23','2024-06-24 17:38:23'),
(169,'ljsDPPv8HWamwUTx5ZXyZxXqwQEW?vframe/jpg/offset/1','image/*','图片',NULL,NULL,19,NULL,'2024-06-24 17:38:38','2024-06-24 17:38:38'),
(170,'ltQx14o1ZXS_wz3v396g_EfpnY-u','video/mp4','视频',NULL,76903705,20,NULL,'2024-06-24 17:43:00','2024-06-24 17:43:00'),
(171,'ltQx14o1ZXS_wz3v396g_EfpnY-u?vframe/jpg/offset/1','image/*','图片',NULL,NULL,18,NULL,'2024-06-24 17:43:33','2024-06-24 17:43:33'),
(172,'FmB_vYiy8aAEXS-8JbItWh182fLz','video/mp4','视频',NULL,2502468,20,NULL,'2024-06-26 12:52:34','2024-06-26 12:52:34'),
(173,'FmB_vYiy8aAEXS-8JbItWh182fLz?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 12:53:17','2024-06-26 12:53:17'),
(174,'FmB_vYiy8aAEXS-8JbItWh182fLz','video/mp4','视频',NULL,2502468,NULL,NULL,'2024-06-26 12:57:19','2024-06-26 12:57:19'),
(175,'FmB_vYiy8aAEXS-8JbItWh182fLz?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 12:57:49','2024-06-26 12:57:49'),
(176,'FmB_vYiy8aAEXS-8JbItWh182fLz','video/mp4','视频',NULL,2502468,20,NULL,'2024-06-26 13:04:08','2024-06-26 13:04:08'),
(177,'FmB_vYiy8aAEXS-8JbItWh182fLz?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 13:05:10','2024-06-26 13:05:10'),
(178,'FmB_vYiy8aAEXS-8JbItWh182fLz','video/mp4','视频',NULL,2502468,NULL,NULL,'2024-06-26 13:26:04','2024-06-26 13:26:04'),
(179,'FmB_vYiy8aAEXS-8JbItWh182fLz?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 13:26:35','2024-06-26 13:26:35'),
(180,'ljsDPPv8HWamwUTx5ZXyZxXqwQEW','video/mp4','视频',NULL,24004787,NULL,NULL,'2024-06-26 13:26:53','2024-06-26 13:26:53'),
(181,'ljsDPPv8HWamwUTx5ZXyZxXqwQEW?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 13:27:16','2024-06-26 13:27:16'),
(182,'FmB_vYiy8aAEXS-8JbItWh182fLz','video/mp4','视频',NULL,2502468,20,NULL,'2024-06-26 13:29:27','2024-06-26 13:29:27'),
(183,'FmB_vYiy8aAEXS-8JbItWh182fLz?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 13:29:54','2024-06-26 13:29:54'),
(184,'FmB_vYiy8aAEXS-8JbItWh182fLz','video/mp4','视频',NULL,2502468,20,NULL,'2024-06-26 13:33:09','2024-06-26 13:33:09'),
(185,'FmB_vYiy8aAEXS-8JbItWh182fLz?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 13:33:54','2024-06-26 13:33:54'),
(186,'lhmc8SAydmd3Q_5EPOuZUz2Hzb0B','video/mp4','视频',NULL,8713994,NULL,NULL,'2024-06-26 13:56:39','2024-06-26 13:56:39'),
(187,'lhmc8SAydmd3Q_5EPOuZUz2Hzb0B?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 13:57:21','2024-06-26 13:57:21'),
(188,'Foy0XARV6MNR37RS_SVSBzul-TB9','video/mp4','视频',NULL,1410941,20,NULL,'2024-06-26 14:27:42','2024-06-26 14:27:42'),
(189,'Foy0XARV6MNR37RS_SVSBzul-TB9?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 14:28:07','2024-06-26 14:28:07'),
(190,'FmB_vYiy8aAEXS-8JbItWh182fLz','video/mp4','视频',NULL,2502468,20,NULL,'2024-06-26 14:28:36','2024-06-26 14:28:36'),
(191,'FmB_vYiy8aAEXS-8JbItWh182fLz?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 14:28:53','2024-06-26 14:28:53'),
(192,'FgcXEWhxc4nGLC8yvlVxqc3w1JzC','video/mp4','视频',NULL,4174428,20,NULL,'2024-06-26 14:29:08','2024-06-26 14:29:08'),
(193,'FgcXEWhxc4nGLC8yvlVxqc3w1JzC?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 14:29:47','2024-06-26 14:29:47'),
(194,'li0UuOXKaDEnEjJXRHKg87OgIXqv','video/mp4','视频',NULL,11974741,NULL,NULL,'2024-06-26 15:01:41','2024-06-26 15:01:41'),
(195,'li0UuOXKaDEnEjJXRHKg87OgIXqv?vframe/jpg/offset/1','image/*','图片',NULL,NULL,20,NULL,'2024-06-26 15:01:53','2024-06-26 15:01:53'),
(196,'lhYm7aPHNlj-tpAhOY3vrcJzupxq','video/mp4','视频',NULL,25680829,20,NULL,'2024-06-26 15:02:28','2024-06-26 15:02:28');

/*Table structure for table `follow` */

DROP TABLE IF EXISTS `follow`;

CREATE TABLE `follow` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `follow_id` bigint NOT NULL,
  `gmt_created` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_user_id_follow_id` (`user_id`,`follow_id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `follow` */

insert  into `follow`(`id`,`user_id`,`follow_id`,`gmt_created`) values 
(98,19,18,'2024-06-22 00:30:59'),
(102,18,19,'2024-06-24 17:45:56'),
(104,20,18,'2024-06-24 17:48:42');

/*Table structure for table `permission` */

DROP TABLE IF EXISTS `permission`;

CREATE TABLE `permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `p_id` bigint DEFAULT '0',
  `path` varchar(255) DEFAULT NULL,
  `href` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `is_menu` tinyint DEFAULT NULL,
  `target` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT '1',
  `state` tinyint DEFAULT '0',
  `is_deleted` tinyint DEFAULT '0',
  `gmt_created` datetime DEFAULT NULL,
  `gmt_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `permission` */

insert  into `permission`(`id`,`p_id`,`path`,`href`,`icon`,`name`,`is_menu`,`target`,`sort`,`state`,`is_deleted`,`gmt_created`,`gmt_updated`) values 
(1,0,NULL,NULL,'fa fa-align-justify','业务管理',-1,'_self',5,0,0,NULL,NULL),
(3,1,'','','fa fa-user','用户管理',-1,'_self',5,0,0,NULL,NULL),
(4,3,'','page/user-table.html','fa fa-user-o','用户',0,'_self',5,0,0,NULL,NULL),
(32,0,'','','fa fa-gears','系统设置',-1,'_self',5,0,0,NULL,NULL),
(73,32,'','','fa fa-shield','权限管理',-1,'_self',5,0,0,NULL,NULL),
(74,73,'permission:list','page/permission.html','fa fa-shield','权限',0,'_self',5,0,0,NULL,NULL),
(75,74,'permission:add','','fa fa-save','添加权限',1,'_self',5,0,0,NULL,NULL),
(76,74,'permission:update','','fa fa-gear','修改权限',1,'_self',5,0,0,NULL,NULL),
(77,74,'permission:delete','','fa fa-remove','删除权限',1,'_self',5,0,0,NULL,NULL),
(78,74,'permission:treeSelect','','fa fa-list','树形下拉选择框',1,'_self',5,0,0,NULL,NULL),
(79,74,'permission:treeList','','fa fa-tree','树形',1,'_self',5,0,0,NULL,NULL),
(80,73,'role:list','page/role.html','fa fa-user-circle','角色管理',0,'_self',5,0,0,NULL,NULL),
(81,80,'role:add','','fa fa-save','添加角色',1,'_self',5,0,0,NULL,NULL),
(82,80,'role:update','','fa fa-gear','修改角色',1,'_self',5,0,0,NULL,NULL),
(84,80,'role:delete','','fa fa-remove','删除角色',1,'_self',5,0,0,NULL,NULL),
(85,80,'role:authority','','fa fa-user-secret','分配权限',1,'_self',5,0,0,NULL,NULL),
(86,80,'role:getPermission','','fa fa-info','获取角色权限',1,'_self',5,0,0,NULL,NULL),
(87,89,'role:getRole','','fa fa-info','获取用户角色',1,'_self',5,0,0,NULL,NULL),
(88,89,'role:initRole','','fa fa-user-o','获取角色',1,'_self',5,0,0,NULL,NULL),
(89,73,'user:list','page/user-role.html','fa fa-user-o','分配角色',0,'_self',5,0,0,NULL,NULL),
(91,89,'user:assignRole','','fa fa-info','分配用户角色',1,'_self',5,0,0,NULL,NULL),
(92,4,'user:update','','fa fa-gear','编辑用户',1,'_self',5,0,0,NULL,NULL),
(93,4,'user:delete','','fa fa-remove','删除用户',1,'_self',5,0,0,NULL,NULL),
(109,4,'admin:user:list','','fa fa-500px','用户列表',1,NULL,5,0,0,NULL,NULL),
(110,4,'admin:user:page','','fa fa-500px','用户角色信息',1,NULL,5,0,0,NULL,NULL),
(111,1,'','','fa fa-file-video-o','视频',-1,NULL,2,0,0,'2023-10-29 13:02:31','2023-10-29 13:03:45'),
(112,111,'admin:video:page','page/video/video.html','fa fa-file-video-o','视频',0,NULL,2,0,0,'2023-10-29 13:03:35','2023-10-29 13:13:55'),
(113,112,'admin:video:delete','','fa fa-remove','删除视频',1,NULL,1,0,0,'2023-10-29 13:04:29','2023-10-29 13:04:29'),
(114,112,'admin:video:get','','fa fa-info','获取视频',1,NULL,1,0,0,'2023-10-29 13:04:46','2023-10-29 13:04:46'),
(115,1,'','page/video/type.html','fa fa-align-center','分类',-1,NULL,1,0,0,'2023-11-03 22:03:36','2023-11-03 22:03:36'),
(116,115,'admin:type:get','','fa ','查看分类详情',1,NULL,1,0,0,'2023-11-03 22:04:04','2023-11-03 22:04:04'),
(118,115,'admin:type:add','','fa ','添加分类',1,NULL,1,0,0,'2023-11-03 22:04:41','2023-11-03 22:04:41'),
(119,115,'admin:type:update','','fa ','修改分类',1,NULL,1,0,0,'2023-11-03 22:04:53','2023-11-03 22:04:53'),
(120,115,'admin:type:delete','','fa ','删除分类',1,NULL,1,0,0,'2023-11-03 22:05:04','2023-11-03 22:05:04'),
(121,115,'admin:type:page','','fa fa-align-center','分页查询分类',1,NULL,1,0,0,'2023-11-03 22:09:25','2023-11-03 22:09:25'),
(122,32,'','page/setting.html','fa fa-cog','系统配置',-1,NULL,1,0,0,'2023-11-04 19:24:45','2023-11-04 19:57:46'),
(123,122,'admin:setting:update','','fa ','修改配置',1,NULL,1,0,0,'2023-11-04 19:58:00','2023-11-04 19:58:00'),
(124,122,'admin:setting:get','','fa ','获取配置',1,NULL,1,0,0,'2023-11-04 19:58:16','2023-11-04 19:58:16'),
(125,112,'admin:video:violations','','fa ','下架视频',1,NULL,1,0,0,'2023-11-06 19:56:50','2023-11-06 19:56:50'),
(126,32,'admin:video:statistics','','fa ','首页',1,NULL,1,0,0,'2023-11-07 13:36:33','2023-11-07 13:36:33');

/*Table structure for table `role` */

DROP TABLE IF EXISTS `role`;

CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `role` */

insert  into `role`(`id`,`name`,`remark`) values 
(1,'超级管理员','拥有权限'),
(16,'审核员','具有审核权限');

/*Table structure for table `role_permission` */

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `permission_id` bigint DEFAULT NULL,
  `role_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1593 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `role_permission` */

insert  into `role_permission`(`id`,`permission_id`,`role_id`) values 
(1513,1,16),
(1514,3,16),
(1515,4,16),
(1516,92,16),
(1517,93,16),
(1518,109,16),
(1519,110,16),
(1520,111,16),
(1521,112,16),
(1522,113,16),
(1523,114,16),
(1524,125,16),
(1525,115,16),
(1526,116,16),
(1527,118,16),
(1528,119,16),
(1529,120,16),
(1530,121,16),
(1531,32,16),
(1532,73,16),
(1533,74,16),
(1534,75,16),
(1535,76,16),
(1536,77,16),
(1537,78,16),
(1538,79,16),
(1539,80,16),
(1540,81,16),
(1541,82,16),
(1542,84,16),
(1543,85,16),
(1544,86,16),
(1545,89,16),
(1546,87,16),
(1547,88,16),
(1548,91,16),
(1549,122,16),
(1550,123,16),
(1551,124,16),
(1552,126,16),
(1553,1,1),
(1554,3,1),
(1555,4,1),
(1556,92,1),
(1557,93,1),
(1558,109,1),
(1559,110,1),
(1560,111,1),
(1561,112,1),
(1562,113,1),
(1563,114,1),
(1564,125,1),
(1565,115,1),
(1566,116,1),
(1567,118,1),
(1568,119,1),
(1569,120,1),
(1570,121,1),
(1571,32,1),
(1572,73,1),
(1573,74,1),
(1574,75,1),
(1575,76,1),
(1576,77,1),
(1577,78,1),
(1578,79,1),
(1579,80,1),
(1580,81,1),
(1581,82,1),
(1582,84,1),
(1583,85,1),
(1584,86,1),
(1585,89,1),
(1586,87,1),
(1587,88,1),
(1588,91,1),
(1589,122,1),
(1590,123,1),
(1591,124,1),
(1592,126,1);

/*Table structure for table `sys_setting` */

DROP TABLE IF EXISTS `sys_setting`;

CREATE TABLE `sys_setting` (
  `id` int NOT NULL,
  `audit_policy` longtext,
  `hot_limit` double NOT NULL DEFAULT '1000',
  `audit_open` tinyint DEFAULT '0' COMMENT '审核开关,0为关，1为开',
  `allow_ip` varchar(255) DEFAULT NULL,
  `auth` tinyint DEFAULT '1' COMMENT '回源鉴权开关,0关闭，1开启,默认为1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `sys_setting` */

insert  into `sys_setting`(`id`,`audit_policy`,`hot_limit`,`audit_open`,`allow_ip`,`auth`) values 
(1,'{\"successScore\":{\"minPulp\":\"0.1\",\"maxPulp\":\"0.9999\",\"minTerror\":\"0.1\",\"maxTerror\":\"0.9999\",\"minPolitician\":\"0.1\",\"maxPolitician\":\"0.9999\",\"auditStatus\":\"0\"},\"manualScore\":{\"minPulp\":\"0\",\"maxPulp\":\"0\",\"minTerror\":\"0\",\"maxTerror\":\"0\",\"minPolitician\":\"0\",\"maxPolitician\":\"0\",\"auditStatus\":\"3\"},\"passScore\":{\"minPulp\":\"0\",\"maxPulp\":\"0\",\"minTerror\":\"0\",\"maxTerror\":\"0\",\"minPolitician\":\"0\",\"maxPolitician\":\"0\",\"auditStatus\":\"2\"}}',500,1,'http://127.0.0.1:5378/',1);

/*Table structure for table `type` */

DROP TABLE IF EXISTS `type`;

CREATE TABLE `type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `open` tinyint(1) DEFAULT '0',
  `icon` varchar(255) DEFAULT NULL,
  `sort` int DEFAULT '0',
  `label_names` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `gmt_created` datetime DEFAULT NULL,
  `gmt_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `type` */

insert  into `type`(`id`,`name`,`description`,`open`,`icon`,`sort`,`label_names`,`is_deleted`,`gmt_created`,`gmt_updated`) values 
(13,'音乐','歌曲视频',1,'mdi-music',0,'音乐',0,'2024-06-15 00:53:32','2024-06-23 21:41:05'),
(14,'游戏','精彩游戏视频',1,'mdi-gamepad-variant-outline',0,'游戏',0,'2024-06-15 00:54:03','2024-06-21 21:27:57'),
(15,'二次元/动漫','动漫视频',1,'mdi-television-classic-off',0,'精彩游戏视频',0,'2024-06-15 00:54:38','2024-06-21 21:28:40'),
(16,'运动','运动视频',1,'mdi-basketball',0,'运动精彩视频',0,'2024-06-15 00:55:12','2024-06-21 21:32:45'),
(17,'科技','科技动态视频',1,'mdi-desktop-classic',0,'最新技术',0,'2024-06-15 00:55:34','2024-06-21 21:29:56'),
(18,'旅行','周游世界',1,'mdi-plane-car',0,'美丽风景',0,'2024-06-16 17:45:42','2024-06-21 21:30:43'),
(19,'搞笑','搞笑视频，带给生活新乐趣',1,'mdi-robot-happy',0,'美好生活',0,'2024-06-16 17:46:23','2024-06-21 21:32:36'),
(20,'美女','优美气质',1,'',0,'美丽',0,'2024-06-22 20:48:43','2024-06-22 20:48:43');

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(50) NOT NULL,
  `nick_name` varchar(50) NOT NULL DEFAULT '',
  `password` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT '',
  `sex` tinyint(1) DEFAULT '1',
  `avatar` varchar(255) DEFAULT '',
  `default_favorites_id` bigint DEFAULT NULL,
  `is_deleted` tinyint DEFAULT '0',
  `gmt_created` datetime DEFAULT NULL,
  `gmt_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user` */

insert  into `user`(`id`,`email`,`nick_name`,`password`,`description`,`sex`,`avatar`,`default_favorites_id`,`is_deleted`,`gmt_created`,`gmt_updated`) values 
(1,'love@qq.com','love','55555','我是管理员',0,'',4,NULL,NULL,'2023-11-07 17:16:51'),
(18,'2934128626@qq.com','喵喵','CQL.25880408.WXW','就不告诉你',1,'0',27,0,'2024-06-15 21:35:45','2024-06-16 17:09:33'),
(19,'264591481@qq.com','小火箭','CQL.25880408.WXW','这个人很懒...',1,'0',29,0,'2024-06-16 18:24:16','2024-06-24 02:42:09'),
(20,'13755434791@163.com','小白龙','CQL.25880408.WXW','这个人很懒...',1,'',31,0,'2024-06-24 17:40:50','2024-06-24 17:40:50');

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=334 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user_role` */

insert  into `user_role`(`id`,`role_id`,`user_id`) values 
(332,1,1),
(333,16,1);

/*Table structure for table `user_subscribe` */

DROP TABLE IF EXISTS `user_subscribe`;

CREATE TABLE `user_subscribe` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `type_id` bigint DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1805138985089511434 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user_subscribe` */

insert  into `user_subscribe`(`id`,`type_id`,`user_id`) values 
(1805138860183138305,16,18),
(1805138860183138306,17,18),
(1805138860183138307,18,18),
(1805138860183138308,19,18),
(1805138860183138309,20,18),
(1805138860183138310,13,18),
(1805138860183138311,14,18),
(1805138860183138312,15,18),
(1805138985089511426,16,19),
(1805138985089511427,17,19),
(1805138985089511428,18,19),
(1805138985089511429,19,19),
(1805138985089511430,20,19),
(1805138985089511431,13,19),
(1805138985089511432,14,19),
(1805138985089511433,15,19);

/*Table structure for table `video` */

DROP TABLE IF EXISTS `video`;

CREATE TABLE `video` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `yv` varchar(255) DEFAULT NULL COMMENT '视频文件的唯一编号',
  `title` varchar(255) NOT NULL COMMENT '视频',
  `description` varchar(255) DEFAULT NULL COMMENT '视频描述',
  `url` varchar(255) DEFAULT NULL COMMENT '视频文件的文件编号',
  `user_id` bigint NOT NULL COMMENT '视频的所有者',
  `type_id` bigint NOT NULL COMMENT '类别id',
  `open` tinyint(1) NOT NULL DEFAULT '0' COMMENT '公开/私密，0：公开，1：私密，默认为0',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面地址',
  `audit_status` int NOT NULL COMMENT '审核状态',
  `msg` varchar(255) DEFAULT NULL COMMENT '视频信息',
  `audit_queue_status` tinyint NOT NULL DEFAULT '0' COMMENT '审核队列状态',
  `start_count` bigint DEFAULT '0' COMMENT '观看次数',
  `share_count` bigint DEFAULT '0' COMMENT '分享的次数',
  `history_count` bigint DEFAULT '0' COMMENT '历史浏览量',
  `favorites_count` bigint DEFAULT '0' COMMENT '点赞次数',
  `label_names` varchar(255) DEFAULT NULL COMMENT '标签名称',
  `video_type` varchar(20) DEFAULT NULL COMMENT '视频类别',
  `duration` varchar(20) DEFAULT NULL COMMENT '视频时长',
  `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除，0：未删除，1：删除，默认为0',
  `gmt_created` datetime DEFAULT NULL,
  `gmt_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4872 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `video` */

insert  into `video`(`id`,`yv`,`title`,`description`,`url`,`user_id`,`type_id`,`open`,`cover`,`audit_status`,`msg`,`audit_queue_status`,`start_count`,`share_count`,`history_count`,`favorites_count`,`label_names`,`video_type`,`duration`,`is_deleted`,`gmt_created`,`gmt_updated`) values 
(4839,'YVba4d44019961c099695aa4e5','美女走秀','黑丝大长腿美女走秀','129',19,20,0,'130',0,'通过',0,2,0,4,0,'大长腿,美女,黑丝',NULL,'00:12',0,'2024-06-24 00:36:04','2024-06-26 04:17:10'),
(4841,'YV837f45b4b800e22a2bf68b0e','karsa','卡萨谈论RNG','133',19,14,0,'134',0,'通过',0,1,0,3,1,'LOL,英雄联盟,Karsa',NULL,'01:37',0,'2024-06-24 01:59:32','2024-06-26 02:23:52'),
(4842,'YV43a94f2c9ecf7f3f0249692f','短裙美女','黑丝超短裙','135',19,20,0,'136',0,'通过',0,0,0,3,0,'短裙,黑丝,甜妹',NULL,'00:12',0,'2024-06-24 02:41:32','2024-06-25 21:05:38'),
(4844,'YVace14fb5b18aff9bd9306d17','手机隔空充电','手机不用充电器即可完成充电','139',19,17,0,'140',0,'通过',0,1,0,3,0,'手机,隔空充电',NULL,'01:38',0,'2024-06-24 12:36:51','2024-06-25 21:13:28'),
(4845,'YV952747d38e73f875323a77b5','小奶龙','小奶龙叫你睡觉','141',19,19,0,'142',0,'通过',0,1,0,3,0,'小奶龙,小恐龙,睡觉',NULL,'02:48',0,'2024-06-24 13:13:51','2024-06-24 17:45:48'),
(4846,'YV2ca248c9aeefc5a254d1e585','搭积木','搭积木','143',19,14,0,'144',0,'通过',0,0,0,1,0,'积木,游戏',NULL,'02:34',0,'2024-06-24 13:23:58','2024-06-26 01:59:38'),
(4847,'YV9ba54a2fa9de5880d737b6a1','篮球','篮球挑战赛','145',19,16,0,'146',0,'通过',0,1,0,4,0,'篮球,basketball,校园',NULL,'01:59',0,'2024-06-24 13:40:22','2024-06-26 04:17:50'),
(4848,'YVd8f0461ca350d9f0ede7e930','诺言-郭有才','郭有才唱诺言','147',19,13,0,'148',0,'通过',0,1,0,5,1,'诺言,郭有才,音乐',NULL,'01:49',0,'2024-06-24 13:49:17','2024-06-26 04:17:58'),
(4849,'YV0dd34c53851d94a62f32364d','旅行','美好风景','149',19,18,0,'150',0,'通过',0,0,0,2,0,'风景,旅行,美景',NULL,'00:17',0,'2024-06-24 14:09:28','2024-06-25 21:08:07'),
(4852,'YV17514090b736a5769c85ca23','盖伦','盖伦大宝剑','155',19,14,0,'156',0,'通过',0,0,0,2,0,'LOL 盖伦 LPL',NULL,'00:23',0,'2024-06-24 16:38:24','2024-06-26 04:14:56'),
(4853,'YV96b944c8a4f9e4c1d158806b','斗罗大陆','斗罗大陆','157',19,15,0,'158',0,'通过',0,1,0,2,0,'斗罗大陆',NULL,'01:03',0,'2024-06-24 17:01:54','2024-06-25 19:48:15'),
(4854,'YV3a544c159142ddeea1a7da4e','积木','积木','160',19,14,0,'161',0,'通过',0,0,0,1,0,'积木',NULL,'02:34',0,'2024-06-24 17:10:15','2024-06-26 13:35:56'),
(4857,'YVf4a64ad09f3343991dff52c9','动漫','动漫','166',19,15,0,'167',0,'通过',0,0,0,1,0,'动漫 二次元',NULL,'00:16',0,'2024-06-24 17:30:05','2024-06-24 17:46:06'),
(4858,'YV877c48b3820b441c2a415e1e','泡泡龙','泡泡龙打瞌睡','168',19,19,0,'169',0,'通过',0,0,0,0,0,'泡泡龙',NULL,'02:48',0,'2024-06-24 17:38:38','2024-06-24 17:38:43'),
(4868,'YVcbec471db1b4f7f54602e2da','meiko','LPL大满贯','188',20,14,0,'189',0,'通过',0,0,0,0,0,'LPL',NULL,'00:10',0,'2024-06-26 14:28:08','2024-06-26 14:28:11'),
(4869,'YVa38440718ea28d181d16def3','旅游','美景','190',20,18,0,'191',0,'通过',0,0,0,0,0,'旅游,美景',NULL,'00:17',0,'2024-06-26 14:28:53','2024-06-26 14:29:01'),
(4870,'YV15df4d149b25e36f62f73b80','斗罗大量','斗罗大陆','192',20,15,0,'193',0,'通过',0,0,0,0,0,'斗罗大陆',NULL,'00:18',0,'2024-06-26 14:29:48','2024-06-26 14:29:57'),
(4871,'YVa64c4962aba2cc1354cffbf2','积木','积木','194',20,14,0,'195',0,'通过',0,0,0,0,0,'积木',NULL,'02:34',0,'2024-06-26 15:01:54','2024-06-26 15:01:59');

/*Table structure for table `video_share` */

DROP TABLE IF EXISTS `video_share`;

CREATE TABLE `video_share` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `video_id` bigint DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `user_id` bigint DEFAULT NULL,
  `gmt_created` datetime DEFAULT NULL,
  `gmt_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_video_id_ip` (`video_id`,`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `video_share` */

/*Table structure for table `video_star` */

DROP TABLE IF EXISTS `video_star`;

CREATE TABLE `video_star` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `video_id` bigint NOT NULL,
  `user_id` bigint DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `gmt_created` datetime DEFAULT NULL,
  `gmt_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_video_id_user_id` (`video_id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=257 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `video_star` */

insert  into `video_star`(`id`,`video_id`,`user_id`,`is_deleted`,`gmt_created`,`gmt_updated`) values 
(247,4839,19,0,'2024-06-24 00:36:33','2024-06-24 00:36:33'),
(249,4841,19,0,'2024-06-24 02:42:42','2024-06-24 02:42:42'),
(251,4844,19,0,'2024-06-24 12:38:39','2024-06-24 12:38:39'),
(252,4847,19,0,'2024-06-24 13:41:53','2024-06-24 13:41:53'),
(253,4848,19,0,'2024-06-24 13:50:10','2024-06-24 13:50:10'),
(254,4845,18,0,'2024-06-24 13:53:19','2024-06-24 13:53:19'),
(255,4853,19,0,'2024-06-24 17:03:26','2024-06-24 17:03:26'),
(256,4839,20,0,'2024-06-26 04:17:10','2024-06-26 04:17:10');

/*Table structure for table `video_type` */

DROP TABLE IF EXISTS `video_type`;

CREATE TABLE `video_type` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `video_id` bigint NOT NULL,
  `type_id` bigint NOT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `gmt_created` datetime DEFAULT NULL,
  `gmt_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `video_type` */

insert  into `video_type`(`id`,`video_id`,`type_id`,`is_deleted`,`gmt_created`,`gmt_updated`) values 
(1,4837,15,0,'2024-06-23 22:34:33','2024-06-23 22:34:33'),
(2,4838,13,0,'2024-06-24 00:07:00','2024-06-24 00:07:00'),
(3,4839,20,0,'2024-06-24 00:36:04','2024-06-24 00:36:04'),
(4,4840,20,0,'2024-06-24 01:52:22','2024-06-24 01:52:22'),
(5,4841,14,0,'2024-06-24 01:59:32','2024-06-24 01:59:32'),
(6,4842,20,0,'2024-06-24 02:41:32','2024-06-24 02:41:32'),
(7,4843,20,0,'2024-06-24 12:18:50','2024-06-24 12:18:50'),
(8,4844,17,0,'2024-06-24 12:36:51','2024-06-24 12:36:51'),
(9,4845,19,0,'2024-06-24 13:13:51','2024-06-24 13:13:51'),
(10,4846,14,0,'2024-06-24 13:23:58','2024-06-24 13:23:58'),
(11,4847,16,0,'2024-06-24 13:40:23','2024-06-24 13:40:23'),
(12,4848,13,0,'2024-06-24 13:49:17','2024-06-24 13:49:17'),
(13,4849,18,0,'2024-06-24 14:09:28','2024-06-24 14:09:28'),
(14,4850,14,0,'2024-06-24 16:05:06','2024-06-24 16:05:06'),
(15,4851,14,0,'2024-06-24 16:26:14','2024-06-24 16:26:14'),
(16,4852,14,0,'2024-06-24 16:38:24','2024-06-24 16:38:24'),
(17,4853,15,0,'2024-06-24 17:01:54','2024-06-24 17:01:54'),
(18,4854,14,0,'2024-06-24 17:10:15','2024-06-24 17:10:15'),
(19,4855,19,0,'2024-06-24 17:19:56','2024-06-24 17:19:56'),
(20,4856,19,0,'2024-06-24 17:23:08','2024-06-24 17:23:08'),
(21,4857,15,0,'2024-06-24 17:30:05','2024-06-24 17:30:05'),
(22,4858,19,0,'2024-06-24 17:38:38','2024-06-24 17:38:38'),
(23,4859,14,0,'2024-06-24 17:43:34','2024-06-24 17:43:34'),
(24,4860,18,0,'2024-06-26 12:53:18','2024-06-26 12:53:18'),
(25,4861,18,0,'2024-06-26 12:57:49','2024-06-26 12:57:49'),
(26,4862,18,0,'2024-06-26 13:05:11','2024-06-26 13:05:11'),
(27,4863,18,0,'2024-06-26 13:26:36','2024-06-26 13:26:36'),
(28,4864,15,0,'2024-06-26 13:27:17','2024-06-26 13:27:17'),
(29,4865,18,0,'2024-06-26 13:29:55','2024-06-26 13:29:55'),
(30,4866,18,0,'2024-06-26 13:33:54','2024-06-26 13:33:54'),
(31,4867,14,0,'2024-06-26 13:57:21','2024-06-26 13:57:21'),
(32,4868,14,0,'2024-06-26 14:28:08','2024-06-26 14:28:08'),
(33,4869,18,0,'2024-06-26 14:28:53','2024-06-26 14:28:53'),
(34,4870,15,0,'2024-06-26 14:29:48','2024-06-26 14:29:48'),
(35,4871,14,0,'2024-06-26 15:01:54','2024-06-26 15:01:54');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
