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
            <button class="layui-btn layui-btn-sm" lay-event="create">添加</button>
        </div>
    </script>

    <script type="text/html" id="tableListRowToolBar">
        <div class="layui-btn-group">
            <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="delete">删除</a>
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
                <input type="hidden" name="id">
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="name">设备名称：</label>
                            <div class="layui-input-block">
                                <input id="name" type="text" name="name" class="layui-input" placeholder="不超过8个字符"
                                       lay-verify="required" lay-verType="tips" maxlength="8">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="operator">操作人员：</label>
                            <div class="layui-input-block">
                                <input id="operator" type="text" name="operator" class="layui-input" placeholder="操作人员"
                                       lay-verify="required" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="type">设备类型：</label>
                            <div class="layui-input-block">
                                <input id="type" type="text" name="type" class="layui-input" placeholder="不超过10个字符"
                                       maxlength="10">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="producer">生产商：</label>
                            <div class="layui-input-block">
                                <input id="producer" type="text" name="producer" class="layui-input"
                                       placeholder="生产商">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="triColorLEDId">三色灯：</label>
                            <div class="layui-input-block">
                                <select id="triColorLEDId" name="triColorLEDId"></select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        <div class="layui-form-item layui-form-text">
                            <label class="layui-form-label" for="description">详细信息：</label>
                            <div class="layui-input-block">
                                <textarea id="description" name="description" class="layui-textarea"
                                          placeholder="不超过16个字符" maxlength="16"></textarea>
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
<script src="js/module/factory/machine.js" type="text/javascript" charset="UTF-8"></script>
</html>