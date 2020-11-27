<!DOCTYPE html>
<html lang="en">
<head>
    <#include "include/common_head_tpt.ftlh">
    <link href="css/login.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray">
<div class="layui-admin-login-main">
    <h2 class="layui-admin-login-header">智能工厂管理系统</h2>
    <form class="layui-form" action="login" method="post">
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-username" for="username"></label>
            <input type="text" name="username" id="username" lay-verify="required" placeholder="用户名"
                   class="layui-input">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-password" for="password"></label>
            <input type="password" name="password" id="password" lay-verify="required" placeholder="密码"
                   class="layui-input">
        </div>
        <div class="layui-form-item">
            <div class="layui-row">
                <div class="layui-col-xs8">
                    <label class="layui-admin-login-icon layui-icon layui-icon-vercode" for="captcha"></label>
                    <input type="text" id="captcha" name="_captcha_" lay-verify="required" placeholder="图形验证码"
                           class="layui-input">
                </div>
                <div class="layui-col-xs3 layui-col-md-offset1">
                    <img src="captcha" class="layui-admin-login-captcha" title="单击刷新" alt="验证码"/>
                </div>
            </div>
        </div>
        <div class="layui-form-item">
            <div class="layui-col-xs8">
                <input type="checkbox" name="remember" lay-skin="primary" title="记住密码">
            </div>
            <div class="layui-col-xs3 layui-col-md-offset1">
                <a href="javascript:void(1);" class="layui-admin-link" style="display:block; margin-top: 7px; float: right">忘记密码？</a>
            </div>
        </div>
        <div class="layui-form-item">
            <button class="layui-btn layui-btn-fluid" lay-submit>登 入</button>
        </div>
    </form>
</div>
<#include "include/footer_tpt.ftlh">
</body>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/login.js" type="text/javascript" charset="UTF-8"></script>
</html>