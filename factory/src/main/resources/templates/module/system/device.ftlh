<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray layui-admin-crud">
<section id="listSection" class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <table id="tableList" class="layui-hide" lay-filter="tableList"></table>
        </div>
    </div>

    <script type="text/html" id="tableListToolBar">
        <div class="layui-btn-container">
            <div class="tool-bar-search-group">
                <input type="text" id="search" placeholder="生产商/收货方/主串号">
                <button class="layui-btn layui-btn-sm" lay-event="search">查询</button>
            </div>
            <button class="layui-btn layui-btn-sm" lay-event="create">添加</button>
        </div>
    </script>

    <script type="text/html" id="tableListRowToolBar">
        <div class="layui-btn-group">
            <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
            <a class="j-locked-btn layui-btn layui-btn-xs{{ d.locked?' j-row-disable':' layui-hide'}}"
               lay-event="unlock">启用</a>
            <a class="j-locked-btn layui-btn layui-btn-danger layui-btn-xs{{ d.locked?' layui-hide':''}}"
               lay-event="lock">禁用</a>
        </div>
    </script>
</section>

<section id="editSection" class="layui-fluid layui-hide">
    <div class="layui-card">
        <div class="layui-card-header">
            <div class="layui-btn-container layui-form-tool">
                <button class="layui-btn layui-btn-sm" lay-event="back">返回</button>
            </div>
        </div>
        <div class="layui-card-body">
            <form class="layui-form layui-form-pane" lay-filter="editForm">
                <input type="text" name="id" class="layui-hide">
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="serialNumber">主串号：</label>
                            <div class="layui-input-block">
                                <input id="serialNumber" type="text" name="serialNumber" class="layui-input"
                                       placeholder="主串号"
                                       lay-verify="required" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="serialNumber1">辅助串号1：</label>
                            <div class="layui-input-block">
                                <input id="serialNumber1" type="text" name="serialNumber1" class="layui-input"
                                       placeholder="辅助串号1">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="serialNumber1">辅助串号2：</label>
                            <div class="layui-input-block">
                                <input id="serialNumber2" type="text" name="serialNumber2" class="layui-input"
                                       placeholder="辅助串号2">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="type">设备类型：</label>
                            <div class="layui-input-block">
                                <select id="type" name="type">
                                    <option value="TriColorLed">三色灯</option>
                                    <option value="RemoteUSB">远程硬盘</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="producer">生产商：</label>
                            <div class="layui-input-block">
                                <input id="producer" type="text" name="producer" class="layui-input"
                                       placeholder="生产商">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="consumerId">收货方：</label>
                            <div class="layui-input-block">
                                <input id="consumerId" type="text" name="consumerId" class="layui-input"
                                       placeholder="收货方">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        <div class="layui-form-item layui-form-text">
                            <label class="layui-form-label" for="description">描述信息：</label>
                            <div class="layui-input-block">
                                <textarea id="description" name="description" class="layui-textarea"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button class="layui-btn" lay-submit lay-filter="editForm">立即提交</button>
                            <button id="btnRest" type="reset" class="layui-btn layui-btn-primary">重置</button>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </div>
</section>
</body>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/module/system/device.js" type="text/javascript" charset="UTF-8"></script>
</html>