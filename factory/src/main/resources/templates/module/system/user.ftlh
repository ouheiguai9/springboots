<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="plugin/layui_exts/selects/formSelects-v4.css" rel="stylesheet" type="text/css">
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
                <input type="text" id="search" placeholder="用户名/手机号码">
                <button class="layui-btn layui-btn-sm" lay-event="search">查询</button>
            </div>
            <button class="layui-btn layui-btn-sm" lay-event="create">添加</button>
        </div>
    </script>

    <script type="text/html" id="tableListRowToolBar">
        <div class="layui-btn-group">
            <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
            <a class="j-locked-btn layui-btn layui-btn-normal layui-btn-xs{{ d.locked?' layui-hide':''}}"
               lay-event="authorize">授权</a>
            <a class="j-locked-btn layui-btn layui-btn-danger layui-btn-xs{{ d.locked?' j-row-disable':' layui-hide'}}"
               lay-event="unlock">启用</a>
            <a class="j-locked-btn layui-btn layui-btn-danger layui-btn-xs{{ d.locked?' layui-hide':''}}"
               lay-event="lock">禁用</a>
        </div>
    </script>

    <script type="text/html" id="menuContent">
        <div class="authorize-card">
            <div class="authorize-card-header">
                <div class="layui-btn-container">
                    <button class="layui-btn layui-btn-sm" lay-event="doAuthorizeSave">保存</button>
                    <button class="layui-btn layui-btn-sm" lay-event="doAuthorizeRest">重置</button>
                    <button class="layui-btn layui-btn-sm" lay-event="doAuthorizeCancel">取消</button>
                </div>
            </div>
            <div class="authorize-card-body">
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
                <input type="text" id="userId" name="id" class="layui-hide">
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="username">用户名：</label>
                            <div class="layui-input-block">
                                <input type="text" name="username" id="username" lay-verify="required|username"
                                       placeholder="用户名" class="layui-input" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="phone">手机号：</label>
                            <div class="layui-input-block">
                                <input type="text" name="phone" id="phone" lay-verify="required|phone|number"
                                       placeholder="手机号" class="layui-input" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="password">密码：</label>
                            <div class="layui-input-block">
                                <input type="password" name="password" id="password" lay-verify="password"
                                       placeholder="密码(6-16位字母和数字)" class="layui-input" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="email">邮箱：</label>
                            <div class="layui-input-block">
                                <input type="text" name="email" id="email" placeholder="邮箱(选填)" class="layui-input"
                                       lay-verType="tips">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="nickname">公司名称：</label>
                            <div class="layui-input-block">
                                <input type="text" name="nickname" id="nickname" lay-verify="nickname"
                                       placeholder="公司名称(选填)" class="layui-input" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item" pane>
                            <label class="layui-form-label" for="sex">性别：</label>
                            <div class="layui-input-block">
                                <input type="radio" name="sex" value="true" title="男" checked>
                                <input type="radio" name="sex" value="false" title="女">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        <div class="layui-form-item">
                            <label id="labelRole" class="layui-form-label">角色：</label>
                            <div class="layui-input-block">
                                <select name="roleIdStr" xm-select="selectRole"></select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md10">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="avatar">图标：</label>
                            <div class="layui-input-block">
                                <input type="text" name="avatar" id="avatar" placeholder="图标" class="layui-input"
                                       readonly>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md2">
                        <button type="button" class="layui-btn layui-btn-normal" id="btnUpload">选择图片</button>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="address">地址：</label>
                            <div class="layui-input-block">
                                <input type="text" name="address" id="address" placeholder="地址(选填)" class="layui-input">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="validPeriod">有效期：</label>
                            <div class="layui-input-block">
                                <input type="hidden" name="beginValidPeriod" id="beginValidPeriod">
                                <input type="hidden" name="endValidPeriod" id="endValidPeriod">
                                <input type="text" name="validPeriod" id="validPeriod" placeholder="有效期"
                                       class="layui-input">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-form-item">
                        <div class="layui-input-block">
                            <button id="btnSubmit" class="layui-btn" lay-submit lay-filter="editForm">立即提交</button>
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
<script src="js/module/system/user.js" type="text/javascript" charset="UTF-8"></script>
</html>