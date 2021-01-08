<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray layui-admin-crud">
<div class="layui-card">
    <div class="layui-card-header">
        <h1 class="local-time-title"></h1>
    </div>
    <div class="layui-card-body">
        <div class="layui-row">
            <div class="layui-col-md8 layui-col-md-offset2">
                <div class="layui-row">
                    <div class="layui-col-xs3 total-title-card">
                        <span class="layui-text">故障设备<label>0</label>台</span>
                    </div>
                    <div class="layui-col-xs3 total-title-card">
                        <span class="layui-text">暂停设备<label>0</label>台</span>
                    </div>
                    <div class="layui-col-xs3 total-title-card">
                        <span class="layui-text">运行设备<label>0</label>台</span>
                    </div>
                    <div class="layui-col-xs3 total-title-card">
                        <span class="layui-text">离线设备<label>0</label>台</span>
                    </div>
                </div>
            </div>
        </div>
        <table class="layui-table">
            <thead>
            <tr>
                <th>设备名称</th>
                <th>设备类型</th>
                <th>状态</th>
                <th>持续时间</th>
                <th>操作员</th>
            </tr>
            </thead>
            <tbody>
            <script type="text/html" id="trTemplete">
                <tr>
                    <td><a href="javascript:void(1)" class="layui-text-blue">{{d.name}}</a></td>
                    <td>{{d.type}}</td>
                    <td><span class="layui-badge-dot layui-badge-dot-bg {{d.color}}"></span>{{d.status}}</td>
                    <td>{{d.duration}}</td>
                    <td>{{d.operator}}</td>
                </tr>
            </script>
            <tr id="noRowTr">
                <td colspan="5" style="text-align: center">未添加设备</td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
</body>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/factory/view.js" type="text/javascript" charset="UTF-8"></script>
</html>