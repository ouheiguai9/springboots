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
            <a class="j-locked-btn layui-btn layui-btn-normal layui-btn-xs{{ d.locked?' layui-hide':''}}"
               lay-event="authorize">授权</a>
            <a class="j-locked-btn layui-btn layui-btn-xs{{ d.locked?' j-row-disable':' layui-hide'}}"
               lay-event="unlock">启用</a>
            <a class="j-locked-btn layui-btn layui-btn-danger layui-btn-xs{{ d.locked?' layui-hide':''}}"
               lay-event="lock">禁用</a>
        </div>
    </script>

    <script type="text/html" id="menuContent">
        <div class="authorize-card">
            <div ></div>
            <div class="layui-card-header">
                <div class="layui-btn-container">
                    <button class="layui-btn layui-btn-sm" lay-event="doAuthorizeSave">保存</button>
                    <button class="layui-btn layui-btn-sm" lay-event="doAuthorizeRest">重置</button>
                    <button class="layui-btn layui-btn-sm" lay-event="doAuthorizeCancel">取消</button>
                </div>
            </div>
            <div class="layui-card-body">
                <div id="menuTree"></div>
            </div>
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
                        <label class="layui-form-label" for="txtName">角色名称：</label>
                        <div class="layui-input-block">
                            <input id="txtName" type="text" name="name" class="layui-input" placeholder="角色名称"
                                   autocomplete="off" lay-verify="required" lay-verType="tips">
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
<script src="js/module/system/role.js" type="text/javascript" charset="UTF-8"></script>
</html>