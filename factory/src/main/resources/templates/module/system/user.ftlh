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
            <form class="layui-form layui-form-pane" lay-filter="editForm">
                <input type="hidden" id="userId" name="id">
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="username">用户名：</label>
                            <div class="layui-input-block">
                                <input type="text" name="username" id="username" lay-verify="required|username"
                                       placeholder="用户名"
                                       class="layui-input" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="email">邮箱：</label>
                            <div class="layui-input-block">
                                <input type="text" name="email" id="email" lay-verify="required|email" placeholder="邮箱"
                                       class="layui-input" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="phone">手机号：</label>
                            <div class="layui-input-block">
                                <input type="text" name="phone" id="phone" lay-verify="required|phone|number"
                                       placeholder="手机号"
                                       class="layui-input" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="password">密码：</label>
                            <div class="layui-input-block">
                                <input type="password" name="password" id="password" lay-verify="password"
                                       placeholder="密码"
                                       class="layui-input" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="nickname">昵称：</label>
                            <div class="layui-input-block">
                                <input type="text" name="nickname" id="nickname" lay-verify="required|nickname"
                                       placeholder="昵称"
                                       class="layui-input" lay-verType="tips">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item" pane>
                            <label class="layui-form-label" for="sex">性别：</label>
                            <div class="layui-input-block">
                                <input type="radio" name="sex" value="true" title="男">
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
                                       disabled>
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
                                <input type="text" name="address" id="address" placeholder="地址" class="layui-input">
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