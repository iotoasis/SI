/*
Navicat MySQL Data Transfer

Source Server         : LWM2M-OFFICE-FOR-PUBLIC
Source Server Version : 50541
Source Host           : 10.10.222.139:3306
Source Database       : hdp

Target Server Type    : MYSQL
Target Server Version : 50541
File Encoding         : 65001

Date: 2017-03-21 18:36:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hdp_device_group
-- ----------------------------
DROP TABLE IF EXISTS `hdp_device_group`;
CREATE TABLE `hdp_device_group` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(30) NOT NULL,
  `DESCRIPTION` varchar(100) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_device_group
-- ----------------------------

-- ----------------------------
-- Table structure for hdp_device_group_model
-- ----------------------------
DROP TABLE IF EXISTS `hdp_device_group_model`;
CREATE TABLE `hdp_device_group_model` (
  `DEVICE_MODEL_ID` bigint(20) NOT NULL,
  `DEVICE_GROUP_ID` bigint(20) NOT NULL,
  `PROFILE_VER` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`DEVICE_MODEL_ID`,`DEVICE_GROUP_ID`),
  KEY `fk_hdp_device_group_model2` (`DEVICE_GROUP_ID`),
  CONSTRAINT `fk_hdp_device_group_model1` FOREIGN KEY (`DEVICE_MODEL_ID`) REFERENCES `hdp_device_model` (`ID`),
  CONSTRAINT `fk_hdp_device_group_model2` FOREIGN KEY (`DEVICE_GROUP_ID`) REFERENCES `hdp_device_group` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_device_group_model
-- ----------------------------

-- ----------------------------
-- Table structure for hdp_device_model
-- ----------------------------
DROP TABLE IF EXISTS `hdp_device_model`;
CREATE TABLE `hdp_device_model` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `OUI` char(6) NOT NULL,
  `MANUFACTURER` varchar(30) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `DEVICE_TYPE` varchar(10) NOT NULL,
  `ICON_URL` varchar(256) DEFAULT NULL,
  `PROFILE_VER` varchar(100) NOT NULL,
  `DEVICE_COUNT` int(11) NOT NULL DEFAULT '0',
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `APPLY_YN` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'N',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_device_model
-- ----------------------------
INSERT INTO `hdp_device_model` VALUES ('97', '000001', 'HERIT', 'LWM2M Client(raspberry)', 'Device', '/images/hitdm/model/SK_ENV.jpg', '1.0', '0', '', 'Y', '2017-01-03 18:25:39', '2017-01-03 18:25:39');
INSERT INTO `hdp_device_model` VALUES ('98', '000001', 'HERIT', 'LWM2M device', 'normal', '/images/hitdm/model/actuator_02.jpg', '1.0', '0', '', 'Y', '2017-02-21 16:38:17', '2017-02-21 16:38:17');

-- ----------------------------
-- Table structure for hdp_dmodel_ui_component
-- ----------------------------
DROP TABLE IF EXISTS `hdp_dmodel_ui_component`;
CREATE TABLE `hdp_dmodel_ui_component` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_MODEL_ID` bigint(20) NOT NULL,
  `UI_COMPONENT_TYPE` smallint(6) NOT NULL,
  `ORDER` smallint(6) NOT NULL,
  `OPTION_DATA` varchar(512) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `DEVICE_MODEL_ID` (`DEVICE_MODEL_ID`),
  CONSTRAINT `HDP_DMODEL_UI_COMPONENT_ibfk_1` FOREIGN KEY (`DEVICE_MODEL_ID`) REFERENCES `hdp_device_model` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_dmodel_ui_component
-- ----------------------------

-- ----------------------------
-- Table structure for hdp_firmware
-- ----------------------------
DROP TABLE IF EXISTS `hdp_firmware`;
CREATE TABLE `hdp_firmware` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_MODEL_ID` bigint(20) NOT NULL,
  `PACKAGE` varchar(256) NOT NULL,
  `FIRMWARE_TYPE` tinyint(4) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DEVICE_MODEL_ID` (`DEVICE_MODEL_ID`),
  CONSTRAINT `HDP_FIRMWARE_ibfk_1` FOREIGN KEY (`DEVICE_MODEL_ID`) REFERENCES `hdp_device_model` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_firmware
-- ----------------------------
INSERT INTO `hdp_firmware` VALUES ('19', '97', 'lwm2m.rsapberry.package', '0', '2017-01-11 16:35:30', '2017-02-21 09:37:07', 'Herit LWM2M Firmware Package');

-- ----------------------------
-- Table structure for hdp_firmware_update
-- ----------------------------
DROP TABLE IF EXISTS `hdp_firmware_update`;
CREATE TABLE `hdp_firmware_update` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VERSION` varchar(100) NOT NULL,
  `FIRMWARE_ID` bigint(20) NOT NULL,
  `PACKAGE` varchar(256) NOT NULL,
  `GROUP_TYPE` char(1) NOT NULL,
  `GROUP_ID` bigint(20) NOT NULL,
  `SCHEDULE_TIME` datetime NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `HDP_FIRMWARE_UPDATE_ibfk_1` (`FIRMWARE_ID`,`VERSION`),
  CONSTRAINT `HDP_FIRMWARE_UPDATE_ibfk_1` FOREIGN KEY (`FIRMWARE_ID`, `VERSION`) REFERENCES `hdp_firmware_ver` (`FIRMWARE_ID`, `VERSION`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_firmware_update
-- ----------------------------
INSERT INTO `hdp_firmware_update` VALUES ('23', '1.1', '19', 'lwm2m.rsapberry.package', 'M', '97', '2017-03-10 00:00:00', '2017-02-17 19:00:24', '2017-02-17 19:00:24');
INSERT INTO `hdp_firmware_update` VALUES ('24', '1.0', '19', 'lwm2m.rsapberry.package', 'G', '97', '2017-01-13 00:00:01', '2017-01-12 14:25:00', '2017-01-12 15:24:03');

-- ----------------------------
-- Table structure for hdp_firmware_ver
-- ----------------------------
DROP TABLE IF EXISTS `hdp_firmware_ver`;
CREATE TABLE `hdp_firmware_ver` (
  `FIRMWARE_ID` bigint(20) NOT NULL,
  `VERSION` varchar(100) NOT NULL,
  `FILE_URL` varchar(256) NOT NULL,
  `FILE_SIZE` int(4) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  `CHECKSUM` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`FIRMWARE_ID`,`VERSION`),
  CONSTRAINT `HDP_FIRMWARE_VER_ibfk_1` FOREIGN KEY (`FIRMWARE_ID`) REFERENCES `hdp_firmware` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_firmware_ver
-- ----------------------------
INSERT INTO `hdp_firmware_ver` VALUES ('19', '1.0', '{file_url}/lwm2m.raspberry/1.0.bin', '3468', '2017-01-12 18:30:00', '2017-01-12 18:30:00', 'checksum_tbd');
INSERT INTO `hdp_firmware_ver` VALUES ('19', '1.1', '{file_url}/lwm2m.raspberry/1.1.bin', '3468', '2017-02-12 13:24:00', '2017-02-12 13:24:00', 'checksum_tbd');

-- ----------------------------
-- Table structure for hdp_monitor_layout
-- ----------------------------
DROP TABLE IF EXISTS `hdp_monitor_layout`;
CREATE TABLE `hdp_monitor_layout` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEVICE_MODEL_ID` bigint(20) DEFAULT NULL,
  `COMPONENT_NAME` varchar(100) NOT NULL,
  `COMPONENT_SHORT_NAME` varchar(20) NOT NULL,
  `ROW_NUM` smallint(6) NOT NULL,
  `COLUMN_NUM` smallint(6) NOT NULL,
  `COLUMN_SIZE` smallint(6) NOT NULL,
  `JSFILES` varchar(200) DEFAULT NULL,
  `RESOURCE_URIS` varchar(256) DEFAULT NULL,
  `TITLE` varchar(30) DEFAULT NULL,
  `USE_YN` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'Y',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  `PARAMETERS` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `DEVICE_MODEL_ID` (`DEVICE_MODEL_ID`),
  CONSTRAINT `HDP_MONITOR_LAYOUT_ibfk_1` FOREIGN KEY (`DEVICE_MODEL_ID`) REFERENCES `hdp_device_model` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1289 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_monitor_layout
-- ----------------------------
INSERT INTO `hdp_monitor_layout` VALUES ('1', null, 'total.systemCondition.basic', 'tscb', '1', '1', '3', null, null, '{null}', 'Y', '2015-04-13 18:30:55', '2015-04-13 18:30:55', null);
INSERT INTO `hdp_monitor_layout` VALUES ('2', null, 'total.systemCondition.regCount', 'tscrc', '1', '2', '5', 'plugins/flot/jquery.flot.js;plugins/flot/jquery.flot.time.js;plugins/flot/jquery.flot.pie.js', null, '{null}', 'Y', '2015-04-13 18:31:39', '2015-04-13 18:31:39', null);
INSERT INTO `hdp_monitor_layout` VALUES ('3', null, 'total.systemCondition.regStat', 'tscrs', '1', '3', '4', 'plugins/morris/raphael-2.1.0.min.js;plugins/morris/morris.js', null, '{null}', 'Y', '2015-04-13 18:31:39', '2015-04-13 18:31:39', null);
INSERT INTO `hdp_monitor_layout` VALUES ('4', null, 'total.status.map', 'tsm', '2', '1', '12', 'https://maps.googleapis.com/maps/api/js?key=AIzaSyDQTpXj82d8UpCi97wzo_nKXL7nYrd4G70;herit/common/map_google.js', null, '{null}', 'Y', '2015-04-13 18:38:25', '2015-04-13 18:38:25', null);
INSERT INTO `hdp_monitor_layout` VALUES ('5', null, 'total.errorStat.errorCount', 'tesec', '3', '1', '6', null, null, '{null}', 'Y', '2015-04-13 18:38:25', '2015-04-13 18:38:25', null);
INSERT INTO `hdp_monitor_layout` VALUES ('6', null, 'total.errorStat.errorGraph', 'teseg', '3', '2', '6', null, null, '{null}', 'Y', '2015-04-13 18:38:25', '2015-04-13 18:38:25', null);
INSERT INTO `hdp_monitor_layout` VALUES ('7', null, 'total.errorDetail.errorDevice', 'teded', '4', '1', '6', null, null, '{null}', 'Y', '2015-04-13 18:38:25', '2015-04-13 18:38:25', null);
INSERT INTO `hdp_monitor_layout` VALUES ('8', null, 'total.errorDetail.errorMessage', 'tedem', '4', '2', '6', null, null, '{null}', 'Y', '2015-04-13 18:38:26', '2015-04-13 18:38:26', null);
INSERT INTO `hdp_monitor_layout` VALUES ('9', null, 'total.fieldMonitoring.basic', 'tfmb', '5', '1', '12', null, null, '{null}', 'N', '2015-04-13 18:38:26', '2015-04-13 18:38:26', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1258', '97', 'device.status.single_num_bar', 'dssnb', '3', '6', '2', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/3/-/10', 'memory_free', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1259', '97', 'device.summary.model', 'dsm', '1', '1', '3', '', '/3/-/1', 'Model Info', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1260', '97', 'device.summary.device', 'dsd', '1', '2', '3', '', '/3/-/2', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1261', '97', 'device.summary.status', 'dss', '1', '3', '2', '', '', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1262', '97', 'device.summary.today', 'dst', '1', '4', '4', '', '', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1263', '97', 'device.status.map', 'dsma', '2', '1', '6', 'https://maps.googleapis.com/maps/api/js?key=AIzaSyDQTpXj82d8UpCi97wzo_nKXL7nYrd4G70;herit/common/map_google.js', '/6/-/0;/6/-/1', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1267', '97', 'device.status.double_line_graph_lwm2m', 'dsdlg_lwm2m', '2', '5', '6', 'plugins/flot/jquery.flot.js;plugins/flot/jquery.flot.pie.js;plugins/flot/jquery.flot.time.js;herit/common/singleLine_graph.js;herit/common/traffic_graph_lwm2m.js', '/7/-/2;/7/-/3', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1268', '97', 'device.status.single_num_bar', 'dssnb', '3', '1', '2', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/6/-/0', '위도', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1269', '97', 'device.status.single_num_bar', 'dssnb', '3', '2', '2', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/6/-/1', '경도', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1270', '97', 'device.status.single_num_bar_lwm2m_ip', 'dssnb_lwm2m_ip', '3', '3', '2', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/4/-/4', 'IP 주소', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1271', '97', 'device.status.reboot_lwm2m', 'dsr_lwm2m', '2', '4', '2', '', '/3/-/4', '제어', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1272', '97', 'device.status.single_switch_lwm2m', 'dsss_lwm2m', '2', '3', '2', 'plugins/switchery/switchery.js', '/1024/12/1', 'LED 스위치', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1273', '97', 'device.status.single_line_graph', 'dsslg', '4', '1', '6', 'plugins/flot/jquery.flot.js;plugins/flot/jquery.flot.threshold.js;plugins/flot/jquery.flot.pie.js;plugins/flot/jquery.flot.time.js;herit/common/singleLine_graph.js;', '/1024/10/3', '온도', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1274', '97', 'device.status.single_line_graph', 'dsslg', '4', '2', '6', 'plugins/flot/jquery.flot.js;plugins/flot/jquery.flot.threshold.js;plugins/flot/jquery.flot.pie.js;plugins/flot/jquery.flot.time.js;herit/common/singleLine_graph.js;', '/1024/10/1', '습도', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1275', '97', 'device.history.control', 'dhc', '5', '1', '6', '', '/1024/12/1', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1276', '97', 'device.history.error', 'dhe', '5', '2', '6', '', '', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', null);
INSERT INTO `hdp_monitor_layout` VALUES ('1277', '97', 'device.status.single_switch_lwm2m', 'dsss_lwm2m', '2', '2', '2', 'plugins/switchery/switchery.js', '/1024/12/3', '사운드', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1278', '97', 'device.status.single_num_bar', 'dssnb', '3', '4', '2', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/1024/11/1', 'Touch 센서', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1279', '97', 'device.status.single_num_bar', 'dssnb', '3', '5', '2', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/1024/11/3', '초음파 센서', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1280', '98', 'device.summary.model', 'dsm', '1', '1', '3', '', '/3/-/1', 'Model Info', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1281', '98', 'device.summary.device_lwm2m', 'dsd_lwm2m', '1', '2', '3', '', '/3/-/2', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1282', '98', 'device.summary.status', 'dss', '1', '3', '2', '', '', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1283', '98', 'device.summary.today', 'dst', '1', '4', '4', '', '', '{null}', 'Y', '2017-01-04 09:56:03', '2017-01-04 09:56:03', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1284', '98', 'device.status.map', 'dsma', '2', '1', '6', 'https://maps.googleapis.com/maps/api/js?key=AIzaSyDQTpXj82d8UpCi97wzo_nKXL7nYrd4G70;herit/common/map_google.js', '/6/-/0;/6/-/1', '{null}', 'Y', '2017-01-03 10:52:55', '2017-01-03 10:52:55', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1285', '98', 'device.status.single_num_bar_lwm2m_location', 'dssnb_lwm2m_location', '2', '2', '3', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/6/-/0', '위도', 'Y', '2017-01-03 15:47:33', '2017-01-03 15:47:33', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1286', '98', 'device.status.single_num_bar_lwm2m_location', 'dssnb_lwm2m_location', '2', '4', '3', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/6/-/1', '경도', 'Y', '2017-01-03 15:47:33', '2017-01-03 15:47:33', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1287', '98', 'device.status.single_num_bar', 'dssnb', '2', '3', '3', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/3/-/9', '배터리', 'Y', '2017-01-03 15:47:33', '2017-01-03 15:47:33', '');
INSERT INTO `hdp_monitor_layout` VALUES ('1288', '98', 'device.status.single_num_bar', 'dssnb', '2', '5', '3', 'plugins/peity/jquery.peity.min.js;herit/component/device.summary.status_sk.js', '/3/-/10', '메모리', 'Y', '2017-01-03 15:47:33', '2017-01-03 15:47:33', '');

-- ----------------------------
-- Table structure for hdp_mo_error_code
-- ----------------------------
DROP TABLE IF EXISTS `hdp_mo_error_code`;
CREATE TABLE `hdp_mo_error_code` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MO_PROFILE_ID` bigint(20) NOT NULL,
  `ERROR_CODE` varchar(10) NOT NULL,
  `ERROR_GRADE` tinyint(4) NOT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `ERROR_NAME` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `HDP_MO_ERROR_CODE_ibfk_1` (`MO_PROFILE_ID`),
  CONSTRAINT `HDP_MO_ERROR_CODE_ibfk_1` FOREIGN KEY (`MO_PROFILE_ID`) REFERENCES `hdp_mo_profile` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_mo_error_code
-- ----------------------------

-- ----------------------------
-- Table structure for hdp_mo_noti_condition
-- ----------------------------
DROP TABLE IF EXISTS `hdp_mo_noti_condition`;
CREATE TABLE `hdp_mo_noti_condition` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MO_PROFILE_ID` bigint(20) NOT NULL,
  `CONDITION_TYPE` char(1) NOT NULL,
  `CONDITION` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `MO_PROFILE_ID` (`MO_PROFILE_ID`),
  CONSTRAINT `HDP_MO_NOTI_CONDITION_ibfk_1` FOREIGN KEY (`MO_PROFILE_ID`) REFERENCES `hdp_mo_profile` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=255 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_mo_noti_condition
-- ----------------------------

-- ----------------------------
-- Table structure for hdp_mo_option_data
-- ----------------------------
DROP TABLE IF EXISTS `hdp_mo_option_data`;
CREATE TABLE `hdp_mo_option_data` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MO_PROFILE_ID` bigint(20) NOT NULL,
  `ORDER` smallint(4) NOT NULL,
  `DATA` varchar(256) NOT NULL,
  `DISPLAY_DATA` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `HDP_MO_OPTION_DATA_ibfk_1` (`MO_PROFILE_ID`),
  CONSTRAINT `HDP_MO_OPTION_DATA_ibfk_1` FOREIGN KEY (`MO_PROFILE_ID`) REFERENCES `hdp_mo_profile` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=572 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_mo_option_data
-- ----------------------------

-- ----------------------------
-- Table structure for hdp_mo_profile
-- ----------------------------
DROP TABLE IF EXISTS `hdp_mo_profile`;
CREATE TABLE `hdp_mo_profile` (
  `RESOURCE_URI` varchar(256) NOT NULL,
  `PROFILE_VER` varchar(100) NOT NULL,
  `DATA_TYPE` char(1) CHARACTER SET latin1 NOT NULL,
  `UNIT` varchar(3) DEFAULT NULL,
  `NOTI_TYPE` tinyint(4) NOT NULL DEFAULT '0',
  `OPERATION` varchar(3) NOT NULL DEFAULT 'R',
  `DEVICE_MODEL_ID` bigint(20) NOT NULL,
  `IS_DIAGNOSTIC` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'N',
  `IS_MANDATORY` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'N',
  `DISPLAY_NAME` varchar(120) DEFAULT NULL,
  `IS_MULTIPLE` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'N',
  `IS_HISTORICAL` char(1) CHARACTER SET latin1 DEFAULT NULL,
  `IS_ERROR` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'N',
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DEFAULT_VALUE` varchar(512) DEFAULT NULL,
  `DESCRIPTION` varchar(100) DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  `IS_DISPLAY` char(1) CHARACTER SET latin1 DEFAULT NULL,
  `DISPLAY_TYPE` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `HDP_MO_PROFILE_ibfk_1` (`DEVICE_MODEL_ID`),
  CONSTRAINT `HDP_MO_PROFILE_ibfk_1` FOREIGN KEY (`DEVICE_MODEL_ID`) REFERENCES `hdp_device_model` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2371 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_mo_profile
-- ----------------------------
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/0', '1.0', 'S', '', '0', '', '97', 'N', 'Y', 'LWM2M  Server URI', 'N', 'N', 'Y', '2247', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/1', '1.0', 'B', '', '0', '', '97', 'N', 'Y', 'Bootstrap Server', 'N', 'N', 'Y', '2248', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/2', '1.0', 'I', '', '0', '', '97', 'N', 'Y', 'Security Mode', 'N', 'N', 'Y', '2249', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/3', '1.0', '', '', '0', '', '97', 'N', 'Y', 'Public Key or Identity', 'N', 'N', 'Y', '2250', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/4', '1.0', '', '', '0', '', '97', 'N', 'Y', 'Server Public Key or Identity', 'N', 'N', 'Y', '2251', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/5', '1.0', '', '', '0', '', '97', 'N', 'Y', 'Secret Key', 'N', 'N', 'Y', '2252', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/6', '1.0', 'I', '', '0', '', '97', 'N', 'Y', 'SMS Security Mode', 'N', 'N', 'Y', '2253', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/7', '1.0', '', '', '0', '', '97', 'N', 'Y', 'SMS Binding Key Parameters', 'N', 'N', 'Y', '2254', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/8', '1.0', '', '', '0', '', '97', 'N', 'Y', 'SMS Binding Secret Keys', 'N', 'N', 'Y', '2255', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/9', '1.0', 'S', '', '0', '', '97', 'N', 'Y', 'LWM2M Server SMS Number', 'N', 'N', 'Y', '2256', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/10', '1.0', 'I', '', '0', '', '97', 'N', 'N', 'Short Server ID', 'N', 'N', 'Y', '2257', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/0/-/11', '1.0', 'I', 's', '0', '', '97', 'N', 'Y', 'Client Hold Off Time', 'N', 'N', 'Y', '2258', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/0', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'Short Server ID', 'N', 'N', 'Y', '2259', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/1', '1.0', 'I', 's', '0', 'RW', '97', 'N', 'Y', 'Lifetime', 'N', 'N', 'Y', '2260', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/2', '1.0', 'I', 's', '0', 'RW', '97', 'N', 'N', 'Default Minimum Period', 'N', 'N', 'Y', '2261', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/3', '1.0', 'I', 's', '0', 'RW', '97', 'N', 'N', 'Default Maximum Period', 'N', 'N', 'Y', '2262', null, '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/4', '1.0', 'S', '', '0', 'E', '97', 'N', 'N', 'Disable', 'N', 'N', 'Y', '2263', null, '', '2017-01-03 18:48:06', '2017-01-03 18:48:06', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/5', '1.0', 'I', 's', '0', 'RW', '97', 'N', 'N', 'Disable Timeout', 'N', 'N', 'Y', '2264', null, '', '2017-01-03 18:48:06', '2017-01-03 18:48:06', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/6', '1.0', 'B', '', '0', 'RW', '97', 'N', 'Y', 'Notification Storing When Disabled or Offline', 'N', 'N', 'Y', '2265', null, '', '2017-01-03 18:48:06', '2017-01-03 18:48:06', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/7', '1.0', 'S', '', '0', 'RW', '97', 'N', 'Y', 'Binding', 'N', 'N', 'Y', '2266', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/8', '1.0', 'S', '', '0', 'E', '97', 'N', 'Y', 'Registration Update Trigger', 'N', 'N', 'Y', '2267', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/2/-/0', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'Object ID', 'N', 'N', 'Y', '2268', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/2/-/1', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'Object Instance ID', 'N', 'N', 'Y', '2269', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/2/-/2', '1.0', 'I', '', '0', 'RW', '97', 'N', 'N', 'ACL', 'Y', 'N', 'Y', '2270', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/2/-/3', '1.0', 'I', '', '0', 'RW', '97', 'N', 'Y', 'Access Control Owner', 'N', 'N', 'Y', '2271', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/0', '1.0', 'S', '', '0', 'R', '97', 'N', 'N', 'Manufacturer', 'N', 'N', 'Y', '2272', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/1', '1.0', 'S', '', '0', 'R', '97', 'N', 'N', 'Model Number', 'N', 'N', 'Y', '2273', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/2', '1.0', 'S', '', '0', 'R', '97', 'N', 'N', 'Serial Number', 'N', 'N', 'Y', '2274', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/3', '1.0', 'S', '', '0', 'R', '97', 'N', 'N', 'Firmware Version', 'N', 'N', 'Y', '2275', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/4', '1.0', 'S', '', '0', 'E', '97', 'N', 'Y', 'Reboot', 'N', 'Y', 'Y', '2276', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/5', '1.0', 'S', '', '0', 'E', '97', 'N', 'N', 'Factory Reset', 'N', 'N', 'Y', '2277', null, '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/6', '1.0', 'I', '', '0', 'R', '97', 'N', 'N', 'Available Power Sources', 'Y', 'N', 'Y', '2278', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/7', '1.0', 'I', 'mV', '0', 'R', '97', 'N', 'N', 'Power Source Voltage', 'Y', 'N', 'Y', '2279', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/8', '1.0', 'I', 'mA', '0', 'R', '97', 'N', 'N', 'Power Source Current', 'Y', 'N', 'Y', '2280', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/9', '1.0', 'I', '%', '0', 'R', '97', 'N', 'N', 'Battery Level', 'N', 'N', 'Y', '2281', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/10', '1.0', 'I', 'MB', '0', 'R', '97', 'N', 'N', 'Memory Free', 'N', 'N', 'Y', '2282', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/11', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'Error Code', 'Y', 'N', 'Y', '2283', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/12', '1.0', 'S', '', '0', 'E', '97', 'N', 'N', 'Reset Error Code', 'N', 'N', 'Y', '2284', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/13', '1.0', 'T', '', '0', 'RW', '97', 'N', 'N', 'Current Time', 'N', 'N', 'Y', '2285', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/14', '1.0', 'S', '', '0', 'RW', '97', 'N', 'N', 'UTC Offset', 'N', 'N', 'Y', '2286', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/15', '1.0', 'S', '', '0', 'RW', '97', 'N', 'N', 'Timezone', 'N', 'N', 'Y', '2287', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/16', '1.0', 'S', '', '0', 'R', '97', 'N', 'Y', 'Supported Binding and Modes', 'N', 'N', 'Y', '2288', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/0', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'Network Bearer', 'N', 'N', 'Y', '2289', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/1', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'Available Network Bearer', 'Y', 'N', 'Y', '2290', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/2', '1.0', 'I', 'dBm', '0', 'R', '97', 'N', 'Y', 'Radio Signal Strength', 'N', 'N', 'Y', '2291', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/3', '1.0', 'I', '', '0', 'R', '97', 'N', 'N', 'Link Quality', 'N', 'N', 'Y', '2292', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/4', '1.0', 'S', '', '0', 'R', '97', 'N', 'Y', 'IP Addresses', 'Y', 'N', 'Y', '2293', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/5', '1.0', 'S', '', '0', 'R', '97', 'N', 'N', 'Router IP Addresse', 'Y', 'N', 'Y', '2294', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/6', '1.0', 'I', '%', '0', 'R', '97', 'N', 'N', 'Link Utilization', 'N', 'N', 'Y', '2295', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/7', '1.0', 'S', '', '0', 'R', '97', 'N', 'N', 'APN', 'Y', 'N', 'Y', '2296', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/8', '1.0', 'I', '', '0', 'R', '97', 'N', 'N', 'Cell ID', 'N', 'N', 'Y', '2297', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/9', '1.0', 'I', '%', '0', 'R', '97', 'N', 'N', 'SMNC', 'N', 'N', 'Y', '2298', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/4/-/10', '1.0', 'I', '', '0', 'R', '97', 'N', 'N', 'SMCC', 'N', 'N', 'Y', '2299', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/5/-/0', '1.0', '', '', '0', 'W', '97', 'N', 'Y', 'Package', 'N', 'N', 'Y', '2300', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/5/-/1', '1.0', 'S', '', '0', 'W', '97', 'N', 'Y', 'Package URI', 'N', 'N', 'Y', '2301', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/5/-/2', '1.0', 'S', '', '0', 'E', '97', 'N', 'Y', 'Update', 'N', 'N', 'Y', '2302', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/5/-/3', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'State', 'N', 'N', 'Y', '2303', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/5/-/4', '1.0', 'B', '', '0', 'RW', '97', 'N', 'N', 'Update Supported Objects', 'N', 'N', 'Y', '2304', null, '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/5/-/5', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'Update Result', 'N', 'N', 'Y', '2305', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/0', '1.0', 'S', 'Deg', '0', 'R', '97', 'N', 'Y', 'Latitude', 'N', 'N', 'Y', '2306', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/1', '1.0', 'S', 'Deg', '0', 'R', '97', 'N', 'Y', 'Longitude', 'N', 'N', 'Y', '2307', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/2', '1.0', 'S', 'm', '0', 'R', '97', 'N', 'N', 'Altitude', 'N', 'N', 'Y', '2308', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/3', '1.0', 'S', 'm', '0', 'R', '97', 'N', 'N', 'Uncertainty', 'N', 'N', 'Y', '2309', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/4', '1.0', '', 'ref', '0', 'R', '97', 'N', 'N', 'Velocity', 'N', 'N', 'Y', '2310', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/5', '1.0', 'T', '', '0', 'R', '97', 'N', 'Y', 'Timestamp', 'N', 'N', 'Y', '2311', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/7/-/0', '1.0', 'I', '', '0', 'R', '97', 'N', 'N', 'SMS Tx Counter', 'N', 'N', 'Y', '2312', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/7/-/1', '1.0', 'I', '', '0', 'R', '97', 'N', 'N', 'SMS Rx Counter', 'N', 'N', 'Y', '2313', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/7/-/2', '1.0', 'I', 'ref', '0', 'R', '97', 'N', 'N', 'Tx Data', 'N', 'N', 'Y', '2314', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/7/-/3', '1.0', 'I', 'ref', '0', 'R', '97', 'N', 'N', 'Rx Data', 'N', 'N', 'Y', '2315', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/7/-/4', '1.0', 'I', 'ref', '0', 'R', '97', 'N', 'N', 'Max Message Size', 'N', 'N', 'Y', '2316', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/7/-/5', '1.0', 'I', 'ref', '0', 'R', '97', 'N', 'N', 'Average Message Size', 'N', 'N', 'Y', '2317', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/7/-/6', '1.0', 'S', '', '0', 'E', '97', 'N', 'Y', 'StartOrStop', 'N', 'N', 'Y', '2318', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/7/-/7', '1.0', 'I', '', '0', 'RW', '97', 'N', 'N', 'Collection Duration', 'N', 'N', 'Y', '2319', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/0', '1.0', 'S', '', '0', 'R', '97', 'N', 'Y', 'PkgName', 'N', 'N', 'Y', '2320', null, '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/1', '1.0', 'S', '', '0', 'R', '97', 'N', 'Y', 'PkgVersion', 'N', 'N', 'Y', '2321', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/2', '1.0', '', '', '0', 'W', '97', 'N', 'Y', 'Package', 'N', 'N', 'Y', '2322', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/3', '1.0', 'S', '', '0', 'W', '97', 'N', 'Y', 'Package URI', 'N', 'N', 'Y', '2323', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/4', '1.0', 'S', '', '0', 'E', '97', 'N', 'Y', 'Install/Update', 'N', 'N', 'Y', '2324', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/5', '1.0', 'S', '', '0', 'W', '97', 'N', 'N', 'Installation Options', 'N', 'N', 'Y', '2325', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/6', '1.0', 'S', '', '0', 'E', '97', 'N', 'Y', 'Uninstall', 'N', 'N', 'Y', '2326', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/7', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'Update State', 'N', 'N', 'Y', '2327', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/8', '1.0', 'B', '', '0', 'RW', '97', 'N', 'N', 'Update Supported Objects', 'N', 'N', 'Y', '2328', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/9', '1.0', 'I', '', '0', 'R', '97', 'N', 'Y', 'Update Result', 'N', 'N', 'Y', '2329', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/10', '1.0', 'S', '', '0', 'E', '97', 'N', 'Y', 'Activate', 'N', 'N', 'Y', '2330', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/11', '1.0', 'S', '', '0', 'E', '97', 'N', 'Y', 'Deactivate', 'N', 'N', 'Y', '2331', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/9/-/12', '1.0', 'B', '', '0', 'R', '97', 'N', 'Y', 'Activation Result', 'N', 'N', 'Y', '2332', null, '', '2017-01-03 18:48:10', '2017-01-03 18:48:10', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1024/10/1', '1.0', 'I', 'Pe', '3', 'R', '97', 'N', 'N', 'Humidity', '', 'Y', 'Y', '2333', '', null, '2017-01-03 18:54:32', '2017-01-03 18:55:15', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1024/10/3', '1.0', 'I', 'Ce', '3', 'R', '97', 'N', 'N', 'Temperature', '', 'Y', 'Y', '2334', '', null, '2017-01-03 18:55:41', '2017-01-03 18:55:41', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1024/11/1', '1.0', 'I', '', '3', 'R', '97', 'N', 'N', 'Touch Sensor', '', 'Y', 'Y', '2335', '0', null, '2017-01-03 18:56:25', '2017-01-03 18:56:25', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1024/11/3', '1.0', 'I', 'CM', '3', 'R', '97', 'N', 'N', 'UltraSonic Sensor', '', 'Y', 'Y', '2336', '0', null, '2017-01-03 18:57:54', '2017-01-03 18:57:54', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1024/12/1', '1.0', 'I', '', '0', 'W', '97', 'Y', 'N', 'LED', '', 'Y', 'Y', '2337', '0', null, '2017-01-03 18:58:40', '2017-01-03 18:58:40', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1024/12/3', '1.0', 'I', null, '0', 'W', '97', 'N', 'N', 'SOUND', 'N', 'Y', 'Y', '2338', null, null, '2017-01-06 10:55:22', '2017-01-06 10:55:22', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/0', '1.0', 'I', '', '0', 'R', '98', 'N', 'Y', 'Short Server ID', 'N', 'N', 'Y', '2339', '', '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/1', '1.0', 'I', 's', '0', 'RW', '98', 'N', 'Y', 'Lifetime', 'N', 'N', 'Y', '2340', '', '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/2', '1.0', 'I', 's', '0', 'RW', '98', 'N', 'N', 'Default Minimum Period', 'N', 'N', 'Y', '2341', '', '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/3', '1.0', 'I', 's', '0', 'RW', '98', 'N', 'N', 'Default Maximum Period', 'N', 'N', 'Y', '2342', '', '', '2017-01-03 18:48:05', '2017-01-03 18:48:05', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/4', '1.0', 'S', '', '0', 'E', '98', 'N', 'N', 'Disable', 'N', 'N', 'Y', '2343', '', '', '2017-01-03 18:48:06', '2017-01-03 18:48:06', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/5', '1.0', 'I', 's', '0', 'RW', '98', 'N', 'N', 'Disable Timeout', 'N', 'N', 'Y', '2344', '', '', '2017-01-03 18:48:06', '2017-01-03 18:48:06', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/6', '1.0', 'B', '', '0', 'RW', '98', 'N', 'Y', 'Notification Storing When Disabled or Offline', 'N', 'N', 'Y', '2345', '', '', '2017-01-03 18:48:06', '2017-01-03 18:48:06', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/7', '1.0', 'S', '', '0', 'RW', '98', 'N', 'Y', 'Binding', 'N', 'N', 'Y', '2346', '', '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/1/-/8', '1.0', 'S', '', '0', 'E', '98', 'N', 'Y', 'Registration Update Trigger', 'N', 'N', 'Y', '2347', '', '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/0', '1.0', 'S', '', '0', 'R', '98', 'N', 'N', 'Manufacturer', 'N', 'N', 'Y', '2348', '', '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/1', '1.0', 'S', '', '0', 'R', '98', 'N', 'N', 'Model Number', 'N', 'N', 'Y', '2349', '', '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/2', '1.0', 'S', '', '0', 'R', '98', 'N', 'N', 'Serial Number', 'N', 'N', 'Y', '2350', '', '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/3', '1.0', 'S', '', '0', 'R', '98', 'N', 'N', 'Firmware Version', 'N', 'N', 'Y', '2351', '', '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/4', '1.0', 'S', '', '0', 'E', '98', 'N', 'Y', 'Reboot', 'N', 'Y', 'Y', '2352', '', '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/5', '1.0', 'S', '', '0', 'E', '98', 'N', 'N', 'Factory Reset', 'N', 'N', 'Y', '2353', '', '', '2017-01-03 18:48:07', '2017-01-03 18:48:07', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/6', '1.0', 'I', '', '0', 'R', '98', 'N', 'N', 'Available Power Sources', 'Y', 'N', 'Y', '2354', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/7', '1.0', 'I', 'mV', '0', 'R', '98', 'N', 'N', 'Power Source Voltage', 'Y', 'N', 'Y', '2355', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/8', '1.0', 'I', 'mA', '0', 'R', '98', 'N', 'N', 'Power Source Current', 'Y', 'N', 'Y', '2356', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/9', '1.0', 'I', 'Pe', '0', 'R', '98', 'N', 'N', 'Battery Level', 'N', 'N', 'Y', '2357', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/10', '1.0', 'I', 'MB', '0', 'R', '98', 'N', 'N', 'Memory Free', 'N', 'N', 'Y', '2358', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/11', '1.0', 'I', '', '0', 'R', '98', 'N', 'Y', 'Error Code', 'Y', 'N', 'Y', '2359', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/12', '1.0', 'S', '', '0', 'E', '98', 'N', 'N', 'Reset Error Code', 'N', 'N', 'Y', '2360', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/13', '1.0', 'T', '', '0', 'RW', '98', 'N', 'N', 'Current Time', 'N', 'N', 'Y', '2361', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/14', '1.0', 'S', '', '0', 'RW', '98', 'N', 'N', 'UTC Offset', 'N', 'N', 'Y', '2362', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/15', '1.0', 'S', '', '0', 'RW', '98', 'N', 'N', 'Timezone', 'N', 'N', 'Y', '2363', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/3/-/16', '1.0', 'S', '', '0', 'R', '98', 'N', 'Y', 'Supported Binding and Modes', 'N', 'N', 'Y', '2364', '', '', '2017-01-03 18:48:08', '2017-01-03 18:48:08', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/0', '1.0', 'S', 'Deg', '0', 'R', '98', 'N', 'Y', 'Latitude', 'N', 'N', 'Y', '2365', '', '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/1', '1.0', 'S', 'Deg', '0', 'R', '98', 'N', 'Y', 'Longitude', 'N', 'N', 'Y', '2366', '', '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/2', '1.0', 'S', 'm', '0', 'R', '98', 'N', 'N', 'Altitude', 'N', 'N', 'Y', '2367', '', '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/3', '1.0', 'S', 'm', '0', 'R', '98', 'N', 'N', 'Uncertainty', 'N', 'N', 'Y', '2368', '', '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/4', '1.0', '', 'ref', '0', 'R', '98', 'N', 'N', 'Velocity', 'N', 'N', 'Y', '2369', '', '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);
INSERT INTO `hdp_mo_profile` VALUES ('/6/-/5', '1.0', 'T', '', '0', 'R', '98', 'N', 'Y', 'Timestamp', 'N', 'N', 'Y', '2370', '', '', '2017-01-03 18:48:09', '2017-01-03 18:48:09', 'Y', null);

-- ----------------------------
-- Table structure for hdp_mo_profile_ext
-- ----------------------------
DROP TABLE IF EXISTS `hdp_mo_profile_ext`;
CREATE TABLE `hdp_mo_profile_ext` (
  `MO_PROFILE_ID` bigint(20) NOT NULL,
  `DM_TYPE` smallint(6) NOT NULL,
  `EXT_RESOURCE_URI` varchar(256) NOT NULL,
  PRIMARY KEY (`MO_PROFILE_ID`,`DM_TYPE`),
  CONSTRAINT `HDP_MO_PROFILE_EXT_ibfk_1` FOREIGN KEY (`MO_PROFILE_ID`) REFERENCES `hdp_mo_profile` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdp_mo_profile_ext
-- ----------------------------

-- ----------------------------
-- Table structure for mng_account
-- ----------------------------
DROP TABLE IF EXISTS `mng_account`;
CREATE TABLE `mng_account` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MNG_ACCOUNT_GROUP_ID` int(11) NOT NULL,
  `NAME` varchar(30) NOT NULL,
  `LOGIN_ID` varchar(20) NOT NULL,
  `LOGIN_PWD` varchar(200) NOT NULL,
  `AGO_PWD` varchar(200) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  `LAST_ACCESS_TIME` datetime DEFAULT NULL,
  `EMAIL` varchar(255) DEFAULT NULL,
  `PHONE` varchar(12) DEFAULT NULL,
  `MOBILE` varchar(12) DEFAULT NULL,
  `DEPARTMENT` varchar(30) DEFAULT NULL,
  `DISABLED` int(11) NOT NULL DEFAULT '1',
  `FAIL_COUNT` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `LOGIN_ID` (`LOGIN_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mng_account
-- ----------------------------
INSERT INTO `mng_account` VALUES ('1', '2', 'TEST002', 'iot', 'KkhV8d7nZxvZCmi4FDtlbnZCaNmNqGlT+su1XjgF/bbNb9mV7x/R2i1AwBJ6ThcrSNSWi49l5vkWqEUb2pcVng==', 'KkhV8d7nZxvZCmi4FDtlbnZCaNmNqGlT+su1XjgF/bbNb9mV7x/R2i1AwBJ6ThcrSNSWi49l5vkWqEUb2pcVng==', '2014-12-03 16:21:16', '2014-12-03 16:21:16', null, 'jack@cotconnected.com', '', '', '', '1', '0');

-- ----------------------------
-- Table structure for mng_account_group
-- ----------------------------
DROP TABLE IF EXISTS `mng_account_group`;
CREATE TABLE `mng_account_group` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GROUP_NAME` varchar(40) NOT NULL,
  `GROUP_CODE` varchar(20) NOT NULL,
  `DESCRIPTION` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `GROUP_CODE` (`GROUP_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mng_account_group
-- ----------------------------
INSERT INTO `mng_account_group` VALUES ('1', 'ADMIN_SUPER', 'ADMIN_000', 'TOP LEVEL ADMIN');
INSERT INTO `mng_account_group` VALUES ('2', 'ADMIN_GENERAL', 'ADMIN_001', 'ADMIN');
INSERT INTO `mng_account_group` VALUES ('3', 'USER_GENERAL', 'USER_000', 'USER');

-- ----------------------------
-- Table structure for mng_account_profile
-- ----------------------------
DROP TABLE IF EXISTS `mng_account_profile`;
CREATE TABLE `mng_account_profile` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ACCOUNT_ID` int(11) NOT NULL,
  `PROFILE_TYPE` smallint(6) NOT NULL,
  `PROFILE_DATA` varchar(10240) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mng_account_profile
-- ----------------------------

-- ----------------------------
-- Table structure for mng_group_right
-- ----------------------------
DROP TABLE IF EXISTS `mng_group_right`;
CREATE TABLE `mng_group_right` (
  `MNG_ACCOUNT_GROUP_ID` int(11) NOT NULL,
  `MENU_ID` varchar(20) NOT NULL,
  `RIGHT_C` smallint(6) NOT NULL DEFAULT '0',
  `RIGHT_R` smallint(6) NOT NULL DEFAULT '0',
  `RIGHT_U` smallint(6) NOT NULL DEFAULT '0',
  `RIGHT_D` smallint(6) NOT NULL DEFAULT '0',
  PRIMARY KEY (`MNG_ACCOUNT_GROUP_ID`,`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mng_group_right
-- ----------------------------
INSERT INTO `mng_group_right` VALUES ('1', 'MENU_000', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('1', 'MENU_050', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('1', 'MENU_100', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('1', 'MENU_200', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('1', 'MENU_300', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('1', 'MENU_400', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('1', 'MENU_500', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('1', 'MENU_600', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('2', 'MENU_000', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('2', 'MENU_100', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('2', 'MENU_200', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('2', 'MENU_300', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('2', 'MENU_400', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('2', 'MENU_500', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('2', 'MENU_600', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('3', 'MENU_000', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('3', 'MENU_100', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('3', 'MENU_200', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('3', 'MENU_300', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('3', 'MENU_400', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('3', 'MENU_500', '1', '1', '1', '1');
INSERT INTO `mng_group_right` VALUES ('3', 'MENU_600', '1', '1', '1', '1');

-- ----------------------------
-- Table structure for mng_ip_limit
-- ----------------------------
DROP TABLE IF EXISTS `mng_ip_limit`;
CREATE TABLE `mng_ip_limit` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `IP` varchar(15) NOT NULL,
  `LOGIN_ID` varchar(20) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mng_ip_limit
-- ----------------------------

-- ----------------------------
-- Table structure for mng_menu_master
-- ----------------------------
DROP TABLE IF EXISTS `mng_menu_master`;
CREATE TABLE `mng_menu_master` (
  `MENU_ID` varchar(20) NOT NULL,
  `MENU_NAME` varchar(60) NOT NULL,
  `URL_PATH` varchar(50) NOT NULL,
  `ORDER_BY` int(11) NOT NULL DEFAULT '1',
  `DESCRIPTION` varchar(200) DEFAULT NULL,
  `DISABLED` int(11) NOT NULL DEFAULT '1',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`MENU_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mng_menu_master
-- ----------------------------
INSERT INTO `mng_menu_master` VALUES ('MENU_000', 'DM v2', '/v2/**', '0', 'DM v2', '1', '2015-04-13 00:00:00', '2015-04-13 00:00:00');
INSERT INTO `mng_menu_master` VALUES ('MENU_050', 'Gas Valve Service', '/sgvmng/**', '1', 'Gas Valve Service', '1', '2014-12-03 16:24:22', '2014-12-03 16:24:22');
INSERT INTO `mng_menu_master` VALUES ('MENU_100', 'Home Network Management', '/home/**', '2', 'Main Page', '1', '2014-12-03 16:24:22', '2014-12-03 16:24:22');
INSERT INTO `mng_menu_master` VALUES ('MENU_200', 'Device Management', '/device/**', '3', 'Device Management', '1', '2014-12-03 16:24:22', '2014-12-03 16:24:22');
INSERT INTO `mng_menu_master` VALUES ('MENU_300', 'Open API Management', '/openapi/**', '4', 'Open API Management', '1', '2014-12-03 16:24:22', '2014-12-03 16:24:22');
INSERT INTO `mng_menu_master` VALUES ('MENU_400', 'Operation Management', '/admin/**', '5', 'Operation Management', '1', '2014-12-03 16:24:22', '2014-12-03 16:24:22');
INSERT INTO `mng_menu_master` VALUES ('MENU_500', 'Configuration Management', '/env/**', '6', 'Configuration Management', '1', '2014-12-03 16:24:22', '2014-12-03 16:24:22');
INSERT INTO `mng_menu_master` VALUES ('MENU_600', 'DM Device Management', '/device/**', '6', 'DM Device Management', '1', '2014-12-03 16:25:21', '2014-12-03 16:25:21');

-- ----------------------------
-- Table structure for mng_uri_resource
-- ----------------------------
DROP TABLE IF EXISTS `mng_uri_resource`;
CREATE TABLE `mng_uri_resource` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `URI` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) NOT NULL,
  `AUTH_CHECK` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `URI` (`URI`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of mng_uri_resource
-- ----------------------------
INSERT INTO `mng_uri_resource` VALUES ('1', '/security/authenticate.do', 'User Authorization URI', 'Y');
INSERT INTO `mng_uri_resource` VALUES ('2', '/index.do', 'Initialization method ', 'Y');
INSERT INTO `mng_uri_resource` VALUES ('3', '/api/gw/device/read.do', '', 'Y');
INSERT INTO `mng_uri_resource` VALUES ('4', '/api/gw/device/write.do', '', 'Y');
INSERT INTO `mng_uri_resource` VALUES ('5', '/api/gw/device/execute.do', '', 'Y');
INSERT INTO `mng_uri_resource` VALUES ('6', '/api/gw/device/setattribute.do', '', 'Y');
INSERT INTO `mng_uri_resource` VALUES ('7', '/v2/login.do', 'v2 login page', 'Y');
INSERT INTO `mng_uri_resource` VALUES ('8', '/v3/dashboard.do', 'v3 dashboard', 'Y');
