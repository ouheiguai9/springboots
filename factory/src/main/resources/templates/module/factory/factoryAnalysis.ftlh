<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray layui-admin-crud" style="overflow:auto;">
<div class="layui-card">
    <div class="layui-card-header">
        <h1 class="local-time-title">设备效率排名</h1>
    </div>
    <div class="layui-card-body">
        <div class="layui-row">
            <div class="layui-col-md8 layui-col-md-offset2">
                <div class="layui-row">
                    <div class="layui-col-xs12" style="text-align: center;">
                        <button type="button" data-type="S" class="layui-btn layui-btn-primary layui-btn-sm j-time-type">当前班次</button>
                        <button type="button" data-type="D" class="layui-btn layui-btn-primary layui-btn-sm j-time-type">今天</button>
                        <button type="button" data-type="D3" class="layui-btn layui-btn-primary layui-btn-sm j-time-type">近三天</button>
                        <button type="button" data-type="W" class="layui-btn layui-btn-primary layui-btn-sm j-time-type">近七天</button>
                        <button type="button" data-type="M" class="layui-btn layui-btn-primary layui-btn-sm j-time-type">近一月</button>
<#--                        <button type="button" data-type="X" class="layui-btn layui-btn-primary layui-btn-sm j-time-type">自定义</button>-->
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs12">
                <div class="layui-row">
                    <div class="layui-col-xs12 layui-col-md6">
                        <div id="statusGreen"></div>
                    </div>
                    <div class="layui-col-xs12 layui-col-md6">
                        <div id="statusRed"></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-row">
            <div class="layui-col-xs12">
                <div class="layui-row">
                    <div class="layui-col-xs12 layui-col-md6">
                        <div id="statusYellow"></div>
                    </div>
                    <div class="layui-col-xs12 layui-col-md6">
                        <div id="statusNone"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
<script src="plugin/echarts-5.0.0/echarts.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/factory/analysis.js" type="text/javascript" charset="UTF-8"></script>
</html>