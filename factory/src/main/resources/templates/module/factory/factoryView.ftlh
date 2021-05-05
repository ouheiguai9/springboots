<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray layui-admin-crud" style="overflow:auto;">
<div id="listCard" class="layui-card layui-hide">
    <div class="layui-card-header">
        <h1 class="local-time-title"></h1>
    </div>
    <div class="layui-card-body">
        <div class="layui-row layui-form">
            <div class="layui-col-md3">
                <div class="layui-form-item">
                    <label class="layui-form-label" for="workshopId">归属车间：</label>
                    <div class="layui-input-block">
                        <select id="workshopId" name="workshopId"></select>
                    </div>
                </div>
            </div>
            <div class="layui-col-md6">
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
                <th>本厂编号</th>
                <th>设备名称</th>
                <th>状态</th>
                <th>持续时间</th>
                <th>当前数量</th>
                <th>操作员</th>
                <th>详细信息</th>
            </tr>
            </thead>
            <tbody>
            <script type="text/html" id="trTemplete">
                <tr>
                    <td><a href="javascript:void(1)" class="layui-text-blue">{{d.ordering}}</a></td>
                    <td>{{d.name}}</td>
                    <td><span class="layui-badge-dot layui-badge-dot-bg {{d.color}}"></span>{{d.status}}</td>
                    <td>{{d.duration}}</td>
                    <td>{{d.count}}</td>
                    <td>{{d.operator}}</td>
                    <td>{{d.detail}}</td>
                </tr>
            </script>
            <tr id="noRowTr">
                <td colspan="7" style="text-align: center">未添加设备</td>
            </tr>
            </tbody>
        </table>

        <div id="pager" style="text-align: right;"></div>
    </div>
</div>

<div id="singleCard" class="layui-card"></div>
<script type="text/html" id="singleTemplete">
    <div class="layui-card-header">
        <h1 class="local-time-title">{{d.ordering}} {{d.name}} 当前状态：{{d.status}}</h1>
        <a id="btnBack" href="javascript:void(1);" title="返回列表" style="position: absolute; top: 0; right: 15px;">
            <i class="layui-icon layui-icon-table"></i>
        </a>
    </div>
    <div class="layui-card-body">
        <div class="layui-row">
            <div class="layui-col-md8 layui-col-md-offset2">
                <div class="layui-row">
                    <div class="layui-col-xs12" style="text-align: center;">
                        <button type="button" data-type="S"
                                class="layui-btn layui-btn-primary layui-btn-sm j-time-type">当前班次
                        </button>
                        <button type="button" data-type="D"
                                class="layui-btn layui-btn-primary layui-btn-sm j-time-type">今天
                        </button>
                        <button type="button" data-type="D3"
                                class="layui-btn layui-btn-primary layui-btn-sm j-time-type">近三天
                        </button>
                        <button type="button" data-type="W"
                                class="layui-btn layui-btn-primary layui-btn-sm j-time-type">近七天
                        </button>
                        <button type="button" data-type="M"
                                class="layui-btn layui-btn-primary layui-btn-sm j-time-type">近一月
                        </button>
                        <button id="btnTrigger" type="button" class="layui-btn layui-btn-primary layui-btn-sm"
                                style="position: relative">
                            <div id="dateText">自定义</div>
                            <div id="datePicker"
                                 style="position: absolute; top: 0; left: 0; width:100%; height: 100%; font-size: 0"></div>
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="layui-row">
            <div class="layui-col-xs12">
                <div class="layui-row">
                    <div class="layui-col-xs12 layui-col-md6">
                        <div id="barChart"></div>
                    </div>
                    <div class="layui-col-xs12 layui-col-md6">
                        <div id="pieChart"></div>
                    </div>
                </div>
            </div>
        </div>

        <div class="layui-row">
            <div class="layui-col-xs12">
                <div class="layui-row">
                    <div class="layui-col-xs12 layui-col-md6">
                        <div id="rectChart"></div>
                    </div>
                    <div class="layui-col-xs12 layui-col-md6">
                        <div id="lineChart"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</script>
</body>
<script src="plugin/echarts-5.0.0/echarts.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/factory/view.js" type="text/javascript" charset="UTF-8"></script>
</html>