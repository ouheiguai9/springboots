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
        <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
        {{# if(d.locked) { }}
        <a class="j-locked-btn j-row-disable layui-btn layui-btn-danger layui-btn-xs" lay-event="unlock">启用</a>
        <a class="j-locked-btn layui-btn layui-btn-danger layui-btn-xs layui-hide" lay-event="lock">禁用</a>
        {{# } else { }}
        <a class="j-locked-btn layui-btn layui-btn-danger layui-btn-xs layui-hide" lay-event="unlock">启用</a>
        <a class="j-locked-btn layui-btn layui-btn-danger layui-btn-xs" lay-event="lock">禁用</a>
        {{# } }}
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
            <form class="layui-form" lay-filter="editForm">
                <input type="hidden" name="id">
                <div class="layui-row layui-col-space10 layui-form-item">
                    <div class="layui-col-lg6">
                        <label class="layui-form-label" for="txtName">角色名称：</label>
                        <div class="layui-input-block">
                            <input id="txtName" type="text" name="name" class="layui-input" placeholder="角色名称"
                                   autocomplete="off" lay-verify="required" lay-verType="tips">
                        </div>
                    </div>
                    <div class="layui-form-item">
                        <label class="layui-form-label" for="txtDesc">描述信息：</label>
                        <div class="layui-input-block">
                            <textarea id="txtDesc" name="description" class="layui-textarea"></textarea>
                        </div>
                    </div>
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