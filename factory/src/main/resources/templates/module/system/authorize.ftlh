<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray layui-admin-crud">
<section class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <div class="layui-tab">
                <ul class="layui-tab-title">
                    <li class="layui-this">角色授权</li>
                    <li>用户授权</li>
                </ul>
                <div class="layui-tab-content">
                    <div class="layui-tab-item layui-show">
                        <div class="layui-row">
                            <div class="layui-col-md3">
                                <div id="roleTree"></div>
                            </div>
                            <div class="layui-col-md9">
                                <div id="roleMenuTree"></div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-tab-item">
                        <div class="layui-row">
                            <div class="layui-col-md3">
                                <div id="userTree"></div>
                            </div>
                            <div class="layui-col-md9">
                                <div id="userMenuTree"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
</body>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/system/authorize.js" type="text/javascript" charset="UTF-8"></script>
</html>