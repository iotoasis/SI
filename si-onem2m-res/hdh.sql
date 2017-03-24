/*
Navicat MySQL Data Transfer

Source Server         : LWM2M-OFFICE-FOR-PUBLIC
Source Server Version : 50541
Source Host           : 10.10.222.139:3306
Source Database       : hdh

Target Server Type    : MYSQL
Target Server Version : 50541
File Encoding         : 65001

Date: 2017-03-21 18:35:41
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hdh_alarm_hist
-- ----------------------------
DROP TABLE IF EXISTS `hdh_alarm_hist`;
CREATE TABLE `hdh_alarm_hist` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_ID` varchar(50) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `SN` varchar(50) NOT NULL,
  `ALARM_TYPE` smallint(6) NOT NULL,
  `DATA` varchar(512) NOT NULL,
  `ALARM_TIME` datetime NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdh_alarm_hist
-- ----------------------------

-- ----------------------------
-- Table structure for hdh_control_hist
-- ----------------------------
DROP TABLE IF EXISTS `hdh_control_hist`;
CREATE TABLE `hdh_control_hist` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_ID` varchar(50) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `SN` varchar(50) NOT NULL,
  `RESOURCE_URI` varchar(256) NOT NULL,
  `RESOURCE_NAME` varchar(30) NOT NULL,
  `CTL_TYPE` char(1) NOT NULL,
  `CTL_DATA` varchar(256) NOT NULL,
  `CTL_RESULT` varchar(5) DEFAULT NULL,
  `ERROR_CODE` varchar(3) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `BASE_RESOURCE_URI` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdh_control_hist
-- ----------------------------
INSERT INTO `hdh_control_hist` VALUES ('1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'LWM2M Client(raspberry)', '90:9F:33:EF:D8:ED', '/1024/12/3', 'sound', 'C', 'ON', 'OK', '0', '2017-03-21 10:21:50', '/1024/12/3');
INSERT INTO `hdh_control_hist` VALUES ('2', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'LWM2M Client(raspberry)', '90:9F:33:EF:D8:ED', '/1024/12/3', 'sound', 'C', 'OFF', 'OK', '0', '2017-03-21 10:21:50', '/1024/12/3');

-- ----------------------------
-- Table structure for hdh_device_event_hist
-- ----------------------------
DROP TABLE IF EXISTS `hdh_device_event_hist`;
CREATE TABLE `hdh_device_event_hist` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_ID` varchar(50) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `SN` varchar(50) NOT NULL,
  `RESOURCE_URI` varchar(256) NOT NULL,
  `RESOURCE_NAME` varchar(30) NOT NULL,
  `EVENT_TYPE` tinyint(4) NOT NULL,
  `EVENT_DATA` varchar(1024) NOT NULL,
  `EVENT_TIME` datetime NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `BASE_RESOURCE_URI` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdh_device_event_hist
-- ----------------------------

-- ----------------------------
-- Table structure for hdh_device_logfile
-- ----------------------------
DROP TABLE IF EXISTS `hdh_device_logfile`;
CREATE TABLE `hdh_device_logfile` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_ID` varchar(50) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `SN` varchar(50) NOT NULL,
  `PACKAGE` varchar(256) NOT NULL,
  `PATH` varchar(256) NOT NULL,
  `FILESIZE` int(4) NOT NULL,
  `START_TIME` datetime NOT NULL,
  `END_TIME` datetime NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdh_device_logfile
-- ----------------------------

-- ----------------------------
-- Table structure for hdh_error_hist
-- ----------------------------
DROP TABLE IF EXISTS `hdh_error_hist`;
CREATE TABLE `hdh_error_hist` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_ID` varchar(50) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `SN` varchar(50) NOT NULL,
  `RESOURCE_URI` varchar(256) NOT NULL,
  `RESOURCE_NAME` varchar(30) NOT NULL,
  `ERROR_DATA` varchar(512) NOT NULL,
  `ERROR_TIME` datetime NOT NULL,
  `ERROR_CODE` varchar(10) NOT NULL,
  `ERROR_GRADE` tinyint(4) NOT NULL,
  `ERROR_NAME` varchar(100) NOT NULL,
  `CREATE_YN` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'Y' COMMENT '????:Y, ????:N',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `BASE_RESOURCE_URI` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdh_error_hist
-- ----------------------------

-- ----------------------------
-- Table structure for hdh_fw_up_hist
-- ----------------------------
DROP TABLE IF EXISTS `hdh_fw_up_hist`;
CREATE TABLE `hdh_fw_up_hist` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_ID` varchar(50) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `SN` varchar(50) NOT NULL,
  `PACKAGE` varchar(256) NOT NULL,
  `VERSION` varchar(10) NOT NULL,
  `STATUS` tinyint(4) NOT NULL,
  `STATUS_TIME` datetime NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `RESULT` tinyint(4) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdh_fw_up_hist
-- ----------------------------

-- ----------------------------
-- Table structure for hdh_status_hist
-- ----------------------------
DROP TABLE IF EXISTS `hdh_status_hist`;
CREATE TABLE `hdh_status_hist` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_ID` varchar(50) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `SN` varchar(50) NOT NULL,
  `RESOURCE_URI` varchar(256) NOT NULL,
  `RESOURCE_NAME` varchar(30) NOT NULL,
  `DATA` varchar(1024) NOT NULL,
  `ACTION_TYPE` char(1) CHARACTER SET latin1 DEFAULT NULL COMMENT 'D:DELETE',
  `TRIGGER_TYPE` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'N',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DATA_TIME` datetime(3) NOT NULL,
  `BASE_RESOURCE_URI` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdh_status_hist
-- ----------------------------

-- ----------------------------
-- Table structure for hds_alarm_day
-- ----------------------------
DROP TABLE IF EXISTS `hds_alarm_day`;
CREATE TABLE `hds_alarm_day` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STAT_DATE` datetime NOT NULL,
  `STAT_DIV` smallint(6) NOT NULL,
  `STAT_COUNT` int(11) NOT NULL,
  `ALARM_TYPE` smallint(6) DEFAULT NULL,
  `DEVICE_MODEL_ID` bigint(20) DEFAULT NULL,
  `MODEL_NAME` varchar(50) DEFAULT NULL,
  `MANUFACTURER` varchar(30) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_alarm_day
-- ----------------------------

-- ----------------------------
-- Table structure for hds_alarm_month
-- ----------------------------
DROP TABLE IF EXISTS `hds_alarm_month`;
CREATE TABLE `hds_alarm_month` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STAT_MONTH` datetime NOT NULL,
  `STAT_DIV` smallint(6) NOT NULL,
  `STAT_COUNT` int(11) NOT NULL,
  `ALARM_TYPE` smallint(6) DEFAULT NULL,
  `DEVICE_MODEL_ID` bigint(20) DEFAULT NULL,
  `MODEL_NAME` varchar(50) DEFAULT NULL,
  `MANUFACTURER` varchar(30) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_alarm_month
-- ----------------------------

-- ----------------------------
-- Table structure for hds_error_device_day
-- ----------------------------
DROP TABLE IF EXISTS `hds_error_device_day`;
CREATE TABLE `hds_error_device_day` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STAT_DATE` datetime NOT NULL,
  `MINOR_COUNT` int(11) NOT NULL,
  `MAJOR_COUNT` int(11) NOT NULL,
  `CRITICAL_COUNT` int(11) NOT NULL,
  `FATAL_COUNT` int(11) NOT NULL,
  `TOTAL_COUNT` int(11) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_error_device_day
-- ----------------------------

-- ----------------------------
-- Table structure for hds_error_device_month
-- ----------------------------
DROP TABLE IF EXISTS `hds_error_device_month`;
CREATE TABLE `hds_error_device_month` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STAT_MONTH` datetime NOT NULL,
  `MINOR_COUNT` int(11) NOT NULL,
  `MAJOR_COUNT` int(11) NOT NULL,
  `CRITICAL_COUNT` int(11) NOT NULL,
  `FATAL_COUNT` int(11) NOT NULL,
  `TOTAL_COUNT` int(11) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_error_device_month
-- ----------------------------

-- ----------------------------
-- Table structure for hds_error_duration_day
-- ----------------------------
DROP TABLE IF EXISTS `hds_error_duration_day`;
CREATE TABLE `hds_error_duration_day` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STAT_DATE` datetime NOT NULL,
  `DEVICE_ID` varchar(50) NOT NULL,
  `SN` varchar(50) NOT NULL,
  `DEVICE_MODEL_ID` bigint(20) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `MANUFACTURER` varchar(30) NOT NULL,
  `DURATION` int(11) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_error_duration_day
-- ----------------------------

-- ----------------------------
-- Table structure for hds_error_duration_month
-- ----------------------------
DROP TABLE IF EXISTS `hds_error_duration_month`;
CREATE TABLE `hds_error_duration_month` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STAT_MONTH` datetime NOT NULL,
  `DEVICE_ID` varchar(50) NOT NULL,
  `SN` varchar(50) NOT NULL,
  `DEVICE_MODEL_ID` bigint(20) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `MANUFACTURER` varchar(30) NOT NULL,
  `DURATION` int(11) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_error_duration_month
-- ----------------------------

-- ----------------------------
-- Table structure for hds_register_day
-- ----------------------------
DROP TABLE IF EXISTS `hds_register_day`;
CREATE TABLE `hds_register_day` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_MODEL_ID` bigint(20) DEFAULT NULL,
  `MANUFACTURER` varchar(30) DEFAULT NULL,
  `OUI` char(6) CHARACTER SET latin1 DEFAULT NULL,
  `MODEL_NAME` varchar(50) DEFAULT NULL,
  `STAT_DATE` datetime NOT NULL,
  `STAT_COUNT` int(11) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_register_day
-- ----------------------------

-- ----------------------------
-- Table structure for hds_register_month
-- ----------------------------
DROP TABLE IF EXISTS `hds_register_month`;
CREATE TABLE `hds_register_month` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_MODEL_ID` bigint(20) DEFAULT NULL,
  `MANUFACTURER` varchar(30) DEFAULT NULL,
  `OUI` char(6) CHARACTER SET latin1 DEFAULT NULL,
  `MODEL_NAME` varchar(50) DEFAULT NULL,
  `STAT_MONTH` datetime NOT NULL,
  `STAT_COUNT` int(11) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_register_month
-- ----------------------------

-- ----------------------------
-- Table structure for hds_usage_day
-- ----------------------------
DROP TABLE IF EXISTS `hds_usage_day`;
CREATE TABLE `hds_usage_day` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STAT_DATE` datetime NOT NULL,
  `ERROR_COUNT` int(11) NOT NULL,
  `CONTROL_COUNT` int(11) NOT NULL,
  `FW_COUNT` int(11) NOT NULL,
  `TOTAL_COUNT` int(11) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_usage_day
-- ----------------------------

-- ----------------------------
-- Table structure for hds_usage_month
-- ----------------------------
DROP TABLE IF EXISTS `hds_usage_month`;
CREATE TABLE `hds_usage_month` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STAT_MONTH` datetime NOT NULL,
  `ERROR_COUNT` int(11) NOT NULL,
  `CONTROL_COUNT` int(11) NOT NULL,
  `STATUS_COUNT` int(11) NOT NULL,
  `FW_COUNT` int(11) NOT NULL,
  `TOTAL_COUNT` int(11) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hds_usage_month
-- ----------------------------
