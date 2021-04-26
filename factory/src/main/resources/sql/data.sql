-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
INSERT INTO `t_sys_role` VALUES ('init_data_role_00001', '2020-12-22 10:37:09', '2020-12-22 12:54:21', b'0', '系统管理员', '系统管理员', NULL, NULL);
INSERT INTO `t_sys_role` VALUES ('init_data_role_00002', '2020-12-22 12:53:57', '2020-12-22 12:53:57', b'0', '工厂老板', '工厂老板', NULL, NULL);
INSERT INTO `t_sys_role` VALUES ('init_data_role_00003', '2020-12-22 12:54:18', '2020-12-22 12:54:18', b'0', '租赁厂老板', '租赁厂老板', NULL, NULL);

-- ----------------------------
-- Records of t_sys_menu
-- ----------------------------
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00001', '2020-12-22 16:10:34', '2020-12-22 16:10:34', b'0', 'system', '系统核心功能请勿随意修改和授权', 'layui-icon-windows', '系统功能', 100, NULL, NULL, NULL);
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00002', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'user', '系统核心功能请勿随意修改和授权', 'layui-icon-user', '用户管理', 0, NULL, NULL, 'init_data_menu_00001');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00003', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'role', '系统核心功能请勿随意修改和授权', 'layui-icon-group', '角色管理', 1, NULL, NULL, 'init_data_menu_00001');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00004', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'menu', '系统核心功能请勿随意修改和授权', 'layui-icon-menu-fill', '菜单管理', 2, NULL, NULL, 'init_data_menu_00001');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00005', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'device', '系统核心功能请勿随意修改和授权', 'layui-icon-component', '设备管理', 3, NULL, NULL, 'init_data_menu_00001');

INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00101', '2020-12-22 16:10:34', '2020-12-22 16:10:34', b'0', 'factory', '系统核心功能请勿随意修改和授权', 'layui-icon-home', '设备工厂', 200, NULL, NULL, NULL);
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00102', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'factoryMachine', '系统核心功能请勿随意修改和授权', 'layui-icon-templeate-1', '设备管理', 101, NULL, NULL, 'init_data_menu_00101');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00103', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'factorySchedual', '系统核心功能请勿随意修改和授权', 'layui-icon-tabs', '班次管理', 102, NULL, NULL, 'init_data_menu_00101');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00104', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'factoryAbnormal', '系统核心功能请勿随意修改和授权', 'layui-icon-chart', '异常分析', 103, NULL, NULL, 'init_data_menu_00101');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00105', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'factoryAnalysis', '系统核心功能请勿随意修改和授权', 'layui-icon-chart-screen', '效率分析', 104, NULL, NULL, 'init_data_menu_00101');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00106', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'factoryView', '系统核心功能请勿随意修改和授权', 'layui-icon-console', '全厂总览', 104, NULL, NULL, 'init_data_menu_00101');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00107', '2021-04-26 16:15:11', '2021-04-26 16:15:11', b'0', 'factoryWorkshop', '系统核心功能请勿随意修改和授权', 'layui-icon-layer', '车间管理', 102, NULL, NULL, 'init_data_menu_00101');
