<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray layui-admin-crud">
<section class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-header">角色管理</div>
        <div class="layui-card-body">
            <table id="tableList" class="layui-hide" lay-filter="tableList"></table>
        </div>
    </div>

    <script type="text/html" id="tableListToolBar">
        <div class="layui-btn-container">
            <button class="layui-btn layui-btn-sm" lay-event="create">添加</button>
            <button class="layui-btn layui-btn-sm" lay-event="batchLock">禁用</button>
        </div>
    </script>

    <script type="text/html" id="tableListRowToolBar">
        <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="lock">禁用</a>
    </script>
</section>
</body>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/system/role.js" type="text/javascript" charset="UTF-8"></script>
</html>