<!DOCTYPE html>
<html lang="en">
<head>
    <#include "include/common_head_tpt.ftlh">
    <link href="css/login.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray">
<div class="layui-admin-login-main" style="padding-top: 80px;">
    <h2 class="layui-admin-login-header">智能工厂管理系统</h2>
    <form class="layui-form" action="register" method="post">
        <div class="layui-admin-login-error-info">${error!}</div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-username" for="username"></label>
            <input type="text" name="username" id="username" lay-verify="required" minlength="6" placeholder="用户名"
                   class="layui-input">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-password" for="password"></label>
            <input type="password" name="password" id="password" lay-verify="required" minlength="6" placeholder="密码"
                   class="layui-input">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-password" for="password"></label>
            <input type="password" name="repassword" id="repassword" lay-verify="required" minlength="6" placeholder="确认密码"
                   class="layui-input">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-cellphone" for="phone"></label>
            <input type="text" name="phone" id="phone" lay-verify="required|phone|number" placeholder="手机号码"
                   class="layui-input">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-email" for="email"></label>
            <input type="text" name="email" id="email" lay-verify="required|email" placeholder="邮箱"
                   class="layui-input">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-username" for="nickname"></label>
            <input type="text" name="nickname" id="nickname" lay-verify="required" placeholder="昵称"
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
            <button class="layui-btn layui-btn-fluid" lay-submit>注 册</button>
        </div>
    </form>
</div>
<#include "include/footer_tpt.ftlh">
</body>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/register.js" type="text/javascript" charset="UTF-8"></script>
</html>