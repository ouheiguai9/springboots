<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray layui-admin-crud" style="overflow:auto;">
<section id="listCard" class="layui-card">
    <div class="layui-card-header">
        <div class="layui-row layui-form">
            <div class="layui-col-md5">
                <div class="layui-form-item">
                    <label class="layui-form-label" for="period">时间段：</label>
                    <div class="layui-input-block">
                        <input id="period" type="text" name="period" class="layui-input">
                    </div>
                </div>
            </div>
            <div class="layui-col-md3">
                <div class="layui-form-item">
                    <label class="layui-form-label" for="status">状态：</label>
                    <div class="layui-input-block">
                        <select id="status" name="status">
                            <option value="RED">故障</option>
                            <option value="YELLOW" selected>暂停</option>
                            <option value="GREEN">运行</option>
                            <option value="NONE">离线</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="layui-col-md3">
                <div class="layui-form-item">
                    <label class="layui-form-label" for="threshold">阈值(秒)：</label>
                    <div class="layui-input-block">
                        <input id="threshold" type="number" name="threshold" class="layui-input" min="0" value="180">
                    </div>
                </div>
            </div>
            <div class="layui-col-md1" style="text-align: right;">
                <button id="btnQuery" type="button" class="layui-btn" style="vertical-align: top;">查询</button>
            </div>
        </div>
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
                <th>详细信息</th>
            </tr>
            </thead>
            <tbody>
            <script type="text/html" id="trTemplete">
                <tr>
                    <td>{{d.ordering}}</td>
                    <td>{{d.name}}</td>
<#--                    <td><span class="layui-badge-dot layui-badge-dot-bg {{d.color}}"></span>{{d.status}}</td>-->
                    <td>{{d.status}}</td>
                    <td><a href="javascript:void(1)" class="layui-text-blue">{{d.count}}</a></td>
                    <td>{{d.operator}}</td>
                    <td>{{d.detail}}</td>
                </tr>
            </script>
            <tr id="noRowTr">
                <td colspan="6" style="text-align: center">没有异常设备</td>
            </tr>
            </tbody>
        </table>
    </div>
</section>

<section id="singleCard" class="layui-card layui-hide">
    <div class="layui-card-header">
        <h1 class="local-time-title"></h1>
        <a id="btnBack" href="javascript:void(1);" title="返回列表" style="position: absolute; top: 0; right: 15px;">
            <i class="layui-icon layui-icon-table"></i>
        </a>
    </div>
    <div class="layui-card-body">
        <table id="tableList" class="layui-hide" lay-filter="tableList"></table>
    </div>
</section>
</body>
<script src="plugin/echarts-5.0.0/echarts.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/factory/abnormal.js" type="text/javascript" charset="UTF-8"></script>
</html>