/*
Navicat MySQL Data Transfer

Source Server         : LWM2M-OFFICE-FOR-PUBLIC
Source Server Version : 50541
Source Host           : 10.10.222.139:3306
Source Database       : hdm

Target Server Type    : MYSQL
Target Server Version : 50541
File Encoding         : 65001

Date: 2017-03-21 18:36:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for hdm_device
-- ----------------------------
DROP TABLE IF EXISTS `hdm_device`;
CREATE TABLE `hdm_device` (
  `DEVICE_ID` varchar(50) NOT NULL,
  `PARENT_ID` varchar(50) NOT NULL,
  `DEVICE_TYPE` varchar(10) NOT NULL,
  `MODEL_NAME` varchar(50) NOT NULL,
  `OUI` char(6) NOT NULL,
  `MANUFACTURER` varchar(30) NOT NULL,
  `PROFILE_VER` varchar(100) DEFAULT NULL,
  `DEVICE_GROUP_ID` bigint(20) DEFAULT NULL,
  `EMBEDED_URI` varchar(255) DEFAULT NULL,
  `SN` varchar(50) NOT NULL,
  `AUTH_ID` varchar(255) DEFAULT NULL,
  `AUTH_PWD` varchar(255) DEFAULT NULL,
  `BS_STATUS` tinyint(4) NOT NULL,
  `ERR_GRADE` tinyint(4) NOT NULL DEFAULT '0',
  `REG_TIME` datetime DEFAULT NULL,
  `REG_UPDATE_TIME` datetime DEFAULT NULL,
  `AUTO_UPGRADE_YN` char(1) CHARACTER SET latin1 NOT NULL DEFAULT 'Y',
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  `DM_TYPE` smallint(6) NOT NULL,
  `EXT_SERVER_HOST` varchar(256) DEFAULT NULL,
  `EXT_DEVICE_ID` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`DEVICE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdm_device
-- ----------------------------
INSERT INTO `hdm_device` VALUES ('000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Device', 'LWM2M Client(raspberry)', '000001', 'HERIT', null, null, null, '90:9F:33:EF:D8:ED', 'testlwm2mclient', '1234567890abcdef1234567890abcdef', '1', '0', '2017-01-03 19:02:15', '2017-01-03 19:02:15', 'Y', '2017-01-03 19:02:15', '2017-01-03 19:02:15', '2', null, null);
INSERT INTO `hdm_device` VALUES ('000001_LWM2M device_test', '000001_LWM2M device_test', 'normal', 'LWM2M device', '000001', 'HERIT', '', null, '', 'test', 'lwm2mtest', '1234567890abcdef1234567890abcdef', '0', '0', '2017-01-03 19:02:15', '2017-01-03 19:02:15', 'Y', '2017-02-21 17:06:34', '2017-02-21 17:06:34', '2', '', '');

-- ----------------------------
-- Table structure for hdm_device_firmware
-- ----------------------------
DROP TABLE IF EXISTS `hdm_device_firmware`;
CREATE TABLE `hdm_device_firmware` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PACKAGE` varchar(256) NOT NULL,
  `DEVICE_ID` varchar(50) NOT NULL,
  `VERSION` varchar(10) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  `UP_VERSION` varchar(10) DEFAULT NULL,
  `UP_STATUS` tinyint(4) DEFAULT NULL,
  `UP_RESULT` tinyint(4) DEFAULT NULL,
  `UP_SET_TIME` datetime DEFAULT NULL,
  `UP_STATUS_TIME` datetime DEFAULT NULL,
  `OBJECT_URI` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdm_device_firmware
-- ----------------------------

-- ----------------------------
-- Table structure for hdm_device_mo_data
-- ----------------------------
DROP TABLE IF EXISTS `hdm_device_mo_data`;
CREATE TABLE `hdm_device_mo_data` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `RESOURCE_URI` varchar(256) NOT NULL,
  `DEVICE_ID` varchar(50) NOT NULL,
  `resource_name` varchar(120) DEFAULT NULL,
  `DATA` varchar(1024) DEFAULT NULL,
  `ATTRIBUTE` varchar(100) NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATE_TIME` datetime NOT NULL,
  `ATTR_CUSTOMIZED_YN` char(1) CHARACTER SET latin1 NOT NULL,
  `ATTR_UPDATE_TIME` datetime NOT NULL,
  `DATA_TIME` datetime(3) NOT NULL,
  `BASE_RESOURCE_URI` varchar(256) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=39258 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hdm_device_mo_data
-- ----------------------------
INSERT INTO `hdm_device_mo_data` VALUES ('39095', '/1/-/0', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Short Server ID', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/1/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39096', '/1/-/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Lifetime', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/1/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39097', '/1/-/2', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Default Minimum Period', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/1/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39098', '/1/-/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Default Maximum Period', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/1/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39099', '/1/-/4', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Disable', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/1/-/4');
INSERT INTO `hdm_device_mo_data` VALUES ('39100', '/1/-/5', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Disable Timeout', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/1/-/5');
INSERT INTO `hdm_device_mo_data` VALUES ('39101', '/1/-/6', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Notification Storing When Disabled or Offline', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/1/-/6');
INSERT INTO `hdm_device_mo_data` VALUES ('39102', '/1/-/7', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Binding', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/1/-/7');
INSERT INTO `hdm_device_mo_data` VALUES ('39103', '/1/-/8', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Registration Update Trigger', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/1/-/8');
INSERT INTO `hdm_device_mo_data` VALUES ('39104', '/2/-/0', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Object ID', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/2/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39105', '/2/-/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Object Instance ID', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/2/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39106', '/2/-/2', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'ACL', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/2/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39107', '/2/-/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Access Control Owner', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/2/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39108', '/3/-/0', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Manufacturer', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/3/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39109', '/3/-/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Model Number', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/3/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39110', '/3/-/2', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Serial Number', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/3/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39111', '/3/-/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Firmware Version', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/3/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39112', '/3/-/4', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Reboot', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/3/-/4');
INSERT INTO `hdm_device_mo_data` VALUES ('39113', '/3/-/5', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Factory Reset', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/3/-/5');
INSERT INTO `hdm_device_mo_data` VALUES ('39114', '/3/-/6', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Available Power Sources', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/3/-/6');
INSERT INTO `hdm_device_mo_data` VALUES ('39115', '/3/-/7', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Power Source Voltage', null, '', '2017-01-06 10:59:04', '2017-01-06 10:59:04', 'N', '2017-01-06 10:59:04', '2017-01-06 10:59:04.000', '/3/-/7');
INSERT INTO `hdm_device_mo_data` VALUES ('39116', '/3/-/8', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Power Source Current', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/3/-/8');
INSERT INTO `hdm_device_mo_data` VALUES ('39117', '/3/-/9', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Battery Level', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/3/-/9');
INSERT INTO `hdm_device_mo_data` VALUES ('39118', '/3/-/10', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Memory Free', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/3/-/10');
INSERT INTO `hdm_device_mo_data` VALUES ('39119', '/3/-/11', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Error Code', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/3/-/11');
INSERT INTO `hdm_device_mo_data` VALUES ('39120', '/3/-/12', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Reset Error Code', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/3/-/12');
INSERT INTO `hdm_device_mo_data` VALUES ('39121', '/3/-/13', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Current Time', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/3/-/13');
INSERT INTO `hdm_device_mo_data` VALUES ('39122', '/3/-/14', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'UTC Offset', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/3/-/14');
INSERT INTO `hdm_device_mo_data` VALUES ('39123', '/3/-/15', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Timezone', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/3/-/15');
INSERT INTO `hdm_device_mo_data` VALUES ('39124', '/3/-/16', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Supported Binding and Modes', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/3/-/16');
INSERT INTO `hdm_device_mo_data` VALUES ('39125', '/4/-/0', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Network Bearer', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39126', '/4/-/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Available Network Bearer', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39127', '/4/-/2', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Radio Signal Strength', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39128', '/4/-/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Link Quality', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39129', '/4/-/4', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'IP Addresses', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/4');
INSERT INTO `hdm_device_mo_data` VALUES ('39130', '/4/-/5', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Router IP Addresse', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/5');
INSERT INTO `hdm_device_mo_data` VALUES ('39131', '/4/-/6', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Link Utilization', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/6');
INSERT INTO `hdm_device_mo_data` VALUES ('39132', '/4/-/7', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'APN', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/7');
INSERT INTO `hdm_device_mo_data` VALUES ('39133', '/4/-/8', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Cell ID', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/8');
INSERT INTO `hdm_device_mo_data` VALUES ('39134', '/4/-/9', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'SMNC', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/9');
INSERT INTO `hdm_device_mo_data` VALUES ('39135', '/4/-/10', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'SMCC', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/4/-/10');
INSERT INTO `hdm_device_mo_data` VALUES ('39136', '/5/-/0', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Package', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/5/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39137', '/5/-/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Package URI', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/5/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39138', '/5/-/2', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Update', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/5/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39139', '/5/-/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'State', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/5/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39140', '/5/-/4', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Update Supported Objects', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/5/-/4');
INSERT INTO `hdm_device_mo_data` VALUES ('39141', '/5/-/5', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Update Result', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/5/-/5');
INSERT INTO `hdm_device_mo_data` VALUES ('39142', '/6/-/0', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Latitude', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/6/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39143', '/6/-/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Longitude', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/6/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39144', '/6/-/2', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Altitude', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/6/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39145', '/6/-/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Uncertainty', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/6/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39146', '/6/-/4', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Velocity', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/6/-/4');
INSERT INTO `hdm_device_mo_data` VALUES ('39147', '/6/-/5', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Timestamp', null, '', '2017-01-06 10:59:05', '2017-01-06 10:59:05', 'N', '2017-01-06 10:59:05', '2017-01-06 10:59:05.000', '/6/-/5');
INSERT INTO `hdm_device_mo_data` VALUES ('39148', '/7/-/0', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'SMS Tx Counter', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/7/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39149', '/7/-/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'SMS Rx Counter', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/7/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39150', '/7/-/2', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Tx Data', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/7/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39151', '/7/-/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Rx Data', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/7/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39152', '/7/-/4', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Max Message Size', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/7/-/4');
INSERT INTO `hdm_device_mo_data` VALUES ('39153', '/7/-/5', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Average Message Size', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/7/-/5');
INSERT INTO `hdm_device_mo_data` VALUES ('39154', '/7/-/6', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'StartOrStop', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/7/-/6');
INSERT INTO `hdm_device_mo_data` VALUES ('39155', '/7/-/7', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Collection Duration', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/7/-/7');
INSERT INTO `hdm_device_mo_data` VALUES ('39156', '/1024/10/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Humidity', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/1024/10/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39157', '/1024/10/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Temperature', null, '', '2017-01-06 10:59:06', '2017-01-06 10:59:06', 'N', '2017-01-06 10:59:06', '2017-01-06 10:59:06.000', '/1024/10/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39158', '/1024/11/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'Touch Sensor', null, '', '2017-01-06 10:59:07', '2017-01-06 10:59:07', 'N', '2017-01-06 10:59:07', '2017-01-06 10:59:07.000', '/1024/11/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39159', '/1024/11/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'UltraSonic Sensor', null, '', '2017-01-06 10:59:07', '2017-01-06 10:59:07', 'N', '2017-01-06 10:59:07', '2017-01-06 10:59:07.000', '/1024/11/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39160', '/1024/12/1', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'LED', null, '', '2017-01-06 10:59:07', '2017-01-06 10:59:07', 'N', '2017-01-06 10:59:07', '2017-01-06 10:59:07.000', '/1024/12/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39161', '/1024/12/3', '000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', 'SOUND', null, '', '2017-01-06 10:59:07', '2017-01-06 10:59:07', 'N', '2017-01-06 10:59:07', '2017-01-06 10:59:07.000', '/1024/12/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39226', '/1/-/0', '000001_LWM2M device_test', 'Short Server ID', null, '', '2017-03-21 14:58:12', '2017-03-21 14:58:12', 'N', '2017-03-21 14:58:12', '2017-03-21 14:58:12.000', '/1/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39227', '/1/-/1', '000001_LWM2M device_test', 'Lifetime', null, '', '2017-03-21 14:58:12', '2017-03-21 14:58:12', 'N', '2017-03-21 14:58:12', '2017-03-21 14:58:12.000', '/1/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39228', '/1/-/2', '000001_LWM2M device_test', 'Default Minimum Period', null, '', '2017-03-21 14:58:12', '2017-03-21 14:58:12', 'N', '2017-03-21 14:58:12', '2017-03-21 14:58:12.000', '/1/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39229', '/1/-/3', '000001_LWM2M device_test', 'Default Maximum Period', null, '', '2017-03-21 14:58:12', '2017-03-21 14:58:12', 'N', '2017-03-21 14:58:12', '2017-03-21 14:58:12.000', '/1/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39230', '/1/-/4', '000001_LWM2M device_test', 'Disable', null, '', '2017-03-21 14:58:12', '2017-03-21 14:58:12', 'N', '2017-03-21 14:58:12', '2017-03-21 14:58:12.000', '/1/-/4');
INSERT INTO `hdm_device_mo_data` VALUES ('39231', '/1/-/5', '000001_LWM2M device_test', 'Disable Timeout', null, '', '2017-03-21 14:58:12', '2017-03-21 14:58:12', 'N', '2017-03-21 14:58:12', '2017-03-21 14:58:12.000', '/1/-/5');
INSERT INTO `hdm_device_mo_data` VALUES ('39232', '/1/-/6', '000001_LWM2M device_test', 'Notification Storing When Disabled or Offline', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/1/-/6');
INSERT INTO `hdm_device_mo_data` VALUES ('39233', '/1/-/7', '000001_LWM2M device_test', 'Binding', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/1/-/7');
INSERT INTO `hdm_device_mo_data` VALUES ('39234', '/1/-/8', '000001_LWM2M device_test', 'Registration Update Trigger', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/1/-/8');
INSERT INTO `hdm_device_mo_data` VALUES ('39235', '/3/-/0', '000001_LWM2M device_test', 'Manufacturer', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/3/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39236', '/3/-/1', '000001_LWM2M device_test', 'Model Number', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/3/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39237', '/3/-/2', '000001_LWM2M device_test', 'Serial Number', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/3/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39238', '/3/-/3', '000001_LWM2M device_test', 'Firmware Version', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/3/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39239', '/3/-/4', '000001_LWM2M device_test', 'Reboot', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/3/-/4');
INSERT INTO `hdm_device_mo_data` VALUES ('39240', '/3/-/5', '000001_LWM2M device_test', 'Factory Reset', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/3/-/5');
INSERT INTO `hdm_device_mo_data` VALUES ('39241', '/3/-/6', '000001_LWM2M device_test', 'Available Power Sources', null, '', '2017-03-21 14:58:13', '2017-03-21 14:58:13', 'N', '2017-03-21 14:58:13', '2017-03-21 14:58:13.000', '/3/-/6');
INSERT INTO `hdm_device_mo_data` VALUES ('39242', '/3/-/7', '000001_LWM2M device_test', 'Power Source Voltage', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/7');
INSERT INTO `hdm_device_mo_data` VALUES ('39243', '/3/-/8', '000001_LWM2M device_test', 'Power Source Current', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/8');
INSERT INTO `hdm_device_mo_data` VALUES ('39244', '/3/-/9', '000001_LWM2M device_test', 'Battery Level', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/9');
INSERT INTO `hdm_device_mo_data` VALUES ('39245', '/3/-/10', '000001_LWM2M device_test', 'Memory Free', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/10');
INSERT INTO `hdm_device_mo_data` VALUES ('39246', '/3/-/11', '000001_LWM2M device_test', 'Error Code', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/11');
INSERT INTO `hdm_device_mo_data` VALUES ('39247', '/3/-/12', '000001_LWM2M device_test', 'Reset Error Code', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/12');
INSERT INTO `hdm_device_mo_data` VALUES ('39248', '/3/-/13', '000001_LWM2M device_test', 'Current Time', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/13');
INSERT INTO `hdm_device_mo_data` VALUES ('39249', '/3/-/14', '000001_LWM2M device_test', 'UTC Offset', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/14');
INSERT INTO `hdm_device_mo_data` VALUES ('39250', '/3/-/15', '000001_LWM2M device_test', 'Timezone', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/15');
INSERT INTO `hdm_device_mo_data` VALUES ('39251', '/3/-/16', '000001_LWM2M device_test', 'Supported Binding and Modes', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/3/-/16');
INSERT INTO `hdm_device_mo_data` VALUES ('39252', '/6/-/0', '000001_LWM2M device_test', 'Latitude', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/6/-/0');
INSERT INTO `hdm_device_mo_data` VALUES ('39253', '/6/-/1', '000001_LWM2M device_test', 'Longitude', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/6/-/1');
INSERT INTO `hdm_device_mo_data` VALUES ('39254', '/6/-/2', '000001_LWM2M device_test', 'Altitude', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/6/-/2');
INSERT INTO `hdm_device_mo_data` VALUES ('39255', '/6/-/3', '000001_LWM2M device_test', 'Uncertainty', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/6/-/3');
INSERT INTO `hdm_device_mo_data` VALUES ('39256', '/6/-/4', '000001_LWM2M device_test', 'Velocity', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/6/-/4');
INSERT INTO `hdm_device_mo_data` VALUES ('39257', '/6/-/5', '000001_LWM2M device_test', 'Timestamp', null, '', '2017-03-21 14:58:14', '2017-03-21 14:58:14', 'N', '2017-03-21 14:58:14', '2017-03-21 14:58:14.000', '/6/-/5');

-- ----------------------------
-- Table structure for hit_dev_crypto_inf_tbl
-- ----------------------------
DROP TABLE IF EXISTS `hit_dev_crypto_inf_tbl`;
CREATE TABLE `hit_dev_crypto_inf_tbl` (
  `DEVICE_ID` varchar(50) NOT NULL,
  `ALGORITHM` int(11) NOT NULL,
  `SYMMETRIC_KEY` varchar(64) DEFAULT NULL,
  `EXPIRED_TIME` datetime DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DESCRIPTION` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`DEVICE_ID`,`ALGORITHM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hit_dev_crypto_inf_tbl
-- ----------------------------

-- ----------------------------
-- Table structure for hit_dev_inf_tbl
-- ----------------------------
DROP TABLE IF EXISTS `hit_dev_inf_tbl`;
CREATE TABLE `hit_dev_inf_tbl` (
  `DEVICE_ID` varchar(50) NOT NULL,
  `DEVICE_ALIASE` varchar(50) DEFAULT NULL,
  `SERVER_SEED` varchar(32) DEFAULT NULL,
  `DEVICE_SEED` varchar(32) DEFAULT NULL,
  `DEVICE_PUBLIC_KEY` varchar(500) DEFAULT NULL,
  `IP_ADDRESS` varchar(40) DEFAULT NULL,
  `PORT` int(11) DEFAULT NULL,
  `STATUS` int(11) NOT NULL,
  `SYSTEM_ID` varchar(16) NOT NULL,
  `LAST_CONNECTED_TIME` datetime DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `DESCRIPTION` varchar(50) DEFAULT NULL,
  `TIMESTAMP` bigint(20) DEFAULT NULL,
  `SERVER_PORT` int(11) DEFAULT NULL,
  PRIMARY KEY (`DEVICE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hit_dev_inf_tbl
-- ----------------------------
INSERT INTO `hit_dev_inf_tbl` VALUES ('000001_LWM2M Client(raspberry)_90:9F:33:EF:D8:ED', null, null, null, null, null, null, '0', 'DGP2', '2017-01-04 09:48:45', '2017-01-04 09:48:45', '2017-01-04 09:48:45', null, null, null);
INSERT INTO `hit_dev_inf_tbl` VALUES ('000001_LWM2M device_test', '', '', '', '', '', null, '1', 'DGP2', '2017-01-04 09:48:45', '2017-01-04 09:48:45', '2017-01-04 09:48:45', '', null, null);

-- ----------------------------
-- Table structure for hit_dev_req_inf_tbl
-- ----------------------------
DROP TABLE IF EXISTS `hit_dev_req_inf_tbl`;
CREATE TABLE `hit_dev_req_inf_tbl` (
  `IDX` bigint(20) NOT NULL AUTO_INCREMENT,
  `SESSION_ID` varchar(16) DEFAULT NULL,
  `REQUEST_ID` varchar(16) DEFAULT NULL,
  `DEVICE_ID` varchar(50) NOT NULL,
  `STATUS` int(11) DEFAULT NULL,
  `RESULT` int(11) DEFAULT NULL,
  `UPDATE_TIME` datetime NOT NULL,
  `CREATE_TIME` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `RETRY` int(11) DEFAULT '0',
  `REQUEST_CONTENTS` varchar(3000) DEFAULT NULL,
  `REPORT_URI` varchar(250) DEFAULT NULL,
  `REQUEST_SYSTEM` varchar(16) DEFAULT NULL,
  `SERVICE_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`IDX`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of hit_dev_req_inf_tbl
-- ----------------------------
