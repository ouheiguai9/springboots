<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray layui-admin-crud" style="overflow:auto;">
<section id="listCard" class="layui-card layui-hide">
    <div class="layui-card-header">
    </div>
    <div class="layui-card-body">
        <table class="layui-table">
            <thead>
            <tr>
                <th>本厂编号</th>
                <th>设备名称</th>
                <th>状态</th>
                <th>异常次数</th>
                <th>操作员</th>
            </tr>
            </thead>
            <tbody>
            <script type="text/html" id="trTemplete">
                <tr>
                    <td><a href="javascript:void(1)" class="layui-text-blue">{{d.name}}</a></td>
                    <td>{{d.type}}</td>
                    <td><span class="layui-badge-dot layui-badge-dot-bg {{d.color}}"></span>{{d.status}}</td>
                    <td>{{d.count}}</td>
                    <td>{{d.operator}}</td>
                </tr>
            </script>
            <tr id="noRowTr">
                <td colspan="5" style="text-align: center">没有异常设备</td>
            </tr>
            </tbody>
        </table>
    </div>
</section>

<section id="singleCard" class="layui-card"></section>
</body>
<script src="plugin/echarts-5.0.0/echarts.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/factory/abnormal.js" type="text/javascript" charset="UTF-8"></script>
</html>