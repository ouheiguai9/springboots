<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray layui-admin-crud">
<section id="listSection" class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body layui-form" lay-filter="editForm">
            <table id="tableList" class="layui-hide" lay-filter="tableList"></table>
            <button id="virtualBtn" class="layui-hide" lay-submit lay-filter="editForm"></button>
        </div>
    </div>

    <script type="text/html" id="tableListToolBar">
        <div class="layui-btn-container">
            <button class="layui-btn layui-btn-sm" lay-event="create">添加</button>
        </div>
    </script>

    <script type="text/html" id="tableListRowToolBar">
        <div class="layui-btn-group">
            <a class="j-locked-btn layui-btn layui-btn-xs" lay-event="edit">修改</a>
            <a class="j-locked-btn layui-btn layui-btn-xs layui-hide" lay-event="save" lay-submit>保存</a>
            <a class="j-locked-btn layui-btn layui-btn-xs layui-hide" lay-event="cancel">取消</a>
            <a class="layui-btn layui-btn-danger layui-btn-xs{{ d.id?'':' layui-hide'}}" lay-event="delete">删除</a>
        </div>
    </script>
</section>
</body>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/factory/factorySchedule.js" type="text/javascript" charset="UTF-8"></script>
</html>