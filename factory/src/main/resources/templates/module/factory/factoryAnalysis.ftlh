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
    <div class="layui-card-body j-switch-panel">
        <div class="layui-row">
            <div class="layui-col-md10 layui-col-md-offset1">
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
                        <button id="btnTriggerConf" type="button" class="layui-btn layui-btn-primary layui-btn-sm">配置
                        </button>
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
    <div class="layui-card-body j-switch-panel layui-hide">
        <form class="layui-form layui-form-pane" lay-filter="formConf">
            <div id="confPanel"></div>
            <div class="layui-row">
                <div class="layui-form-item">
                    <div class="layui-input-block" style="text-align: right;">
                        <button id="btnSaveConf" class="layui-btn layui-btn-sm">保存</button>
                        <button id="btnBackConf" class="layui-btn layui-btn-primary layui-btn-sm">返回</button>
                    </div>
                </div>
            </div>
        </form>
    </div>
    <script type="text/html" id="confRow">
        <div class="layui-form-item" pane>
            <label class="layui-form-label">{{ d.name||'未指定' }}</label>
            <div class="layui-input-block">
                {{# layui.each(d.items, function(index, item){ }}
                <input type="checkbox" name="itemConf" title="{{ item.name }}" value="{{ item.id }}" {{ item.checked ?
                'checked' : '' }}>
                {{# }); }}
            </div>
        </div>
    </script>
</div>
</body>
<script src="plugin/echarts-5.0.0/echarts.min.js" type="text/javascript" charset="UTF-8"></script>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/factory/analysis.js" type="text/javascript" charset="UTF-8"></script>
</html>