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

    <script type="text/html" id="tableName">
        <span class="layui-text" style="padding-left: {{d.level*24}}px">
            {{ d.name }}
        </span>
    </script>

    <script type="text/html" id="tableListRowToolBar">
        <div class="layui-btn-group">
            <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
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
                    <div class="layui-form-item layui-col-md6">
                        <label class="layui-form-label">菜单编码：</label>
                        <div class="layui-input-block">
                            <input type="hidden" name="code">
                            <div id="code" class="layui-fake-input layui-disabled"></div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-form-item layui-col-md6">
                        <label class="layui-form-label" for="name">菜单名称：</label>
                        <div class="layui-input-block">
                            <input id="name" type="text" name="name" class="layui-input" placeholder="菜单名称"
                                   lay-verify="required" lay-verType="tips">
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-form-item layui-col-md6">
                        <label class="layui-form-label" for="parentId">上级菜单：</label>
                        <div class="layui-input-block">
                            <select id="parentId" name="parentId"></select>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-form-item layui-col-md6">
                        <label class="layui-form-label" for="icon">图标：</label>
                        <div class="layui-input-block">
                            <input id="icon" type="text" name="icon" class="layui-input" placeholder="图标"
                                   lay-verType="tips">
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-form-item layui-col-md6">
                        <label class="layui-form-label" for="ordering">排序值：</label>
                        <div class="layui-input-block">
                            <input id="ordering" type="text" name="ordering" class="layui-input" placeholder="排序值"
                                   lay-verify="required|number" lay-verType="tips">
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-form-item layui-form-text layui-col-md6">
                        <label class="layui-form-label" for="txtDesc">描述信息：</label>
                        <div class="layui-input-block">
                            <textarea id="txtDesc" name="description" class="layui-textarea"></textarea>
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
<script src="js/module/system/menu.js" type="text/javascript" charset="UTF-8"></script>
</html>