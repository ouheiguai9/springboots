-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
INSERT INTO `t_sys_role` VALUES ('init_data_role_00001', '2020-12-22 10:37:09', '2020-12-22 12:54:21', b'0', '系统管理员', '系统管理员', NULL, NULL);
INSERT INTO `t_sys_role` VALUES ('init_data_role_00002', '2020-12-22 12:53:57', '2020-12-22 12:53:57', b'0', '工厂老板', '工厂老板', NULL, NULL);
INSERT INTO `t_sys_role` VALUES ('init_data_role_00003', '2020-12-22 12:54:18', '2020-12-22 12:54:18', b'0', '租赁厂老板', '租赁厂老板', NULL, NULL);

-- ----------------------------
-- Records of t_sys_menu
-- ----------------------------
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00001', '2020-12-22 16:10:34', '2020-12-22 16:10:34', b'0', 'system', '系统核心功能请勿随意修改和授权', 'layui-icon-windows', '系统功能', 0, NULL, NULL, NULL);
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00002', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'role', '系统核心功能请勿随意修改和授权', 'layui-icon-group', '角色管理', 3, NULL, NULL, 'init_data_menu_00001');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00003', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'menu', '系统核心功能请勿随意修改和授权', 'layui-icon-user', '菜单管理', 1, NULL, NULL, 'init_data_menu_00001');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00004', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'role', '系统核心功能请勿随意修改和授权', 'layui-icon-user', '权限管理', 0, NULL, NULL, 'init_data_menu_00001');
INSERT INTO `t_sys_menu` VALUES ('init_data_menu_00005', '2020-12-22 16:15:11', '2020-12-22 16:15:11', b'0', 'user', '系统核心功能请勿随意修改和授权', 'layui-icon-user', '用户管理', 2, NULL, NULL, 'init_data_menu_00001');