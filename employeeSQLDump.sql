/*
Navicat MySQL Data Transfer

Source Server         : Konekcija
Source Server Version : 50535
Source Host           : localhost:3306
Source Database       : hibernate_example

Target Server Type    : MYSQL
Target Server Version : 50535
File Encoding         : 65001

Date: 2016-01-18 19:31:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `employee`
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `employee_id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `age` int(11) NOT NULL,
  `address` varchar(50) NOT NULL,
  `income` int(11) NOT NULL,
  PRIMARY KEY (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES ('11', 'Sasa Rusmir', '26', 'Test', '1000');
