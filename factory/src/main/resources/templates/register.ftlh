<!DOCTYPE html>
<html lang="en">
<head>
    <#include "include/common_head_tpt.ftlh">
    <link href="css/login.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray">
<div class="layui-admin-login-main" style="padding-top: 80px;">
    <h2 class="layui-admin-login-header">智慧机床</h2>
    <form class="layui-form">
        <div class="layui-admin-login-error-info">${error!}</div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-username" for="username"></label>
            <input type="text" name="username" id="username" lay-verify="required|username" placeholder="用户名(字母和数字)"
                   class="layui-input" lay-verType="tips">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-password" for="password"></label>
            <input type="password" name="password" id="password" lay-verify="required|password" placeholder="密码"
                   class="layui-input" lay-verType="tips">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-password" for="password"></label>
            <input type="password" name="repassword" id="repassword" lay-verify="required|repassword" placeholder="确认密码"
                   class="layui-input" lay-verType="tips" data-equal="password">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-cellphone" for="phone"></label>
            <input type="text" name="phone" id="phone" lay-verify="required|phone|number" placeholder="手机号码"
                   class="layui-input" lay-verType="tips">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-username" for="nickname"></label>
            <input type="text" name="nickname" id="nickname" lay-verify="nickname" placeholder="公司名称(选填)"
                   class="layui-input" lay-verType="tips">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-email" for="email"></label>
            <input type="text" name="email" id="email" lay-verify="email" placeholder="邮箱(选填)"
                   class="layui-input" lay-verType="tips">
        </div>
        <#if allowCaptcha>
            <div class="layui-form-item">
                <div class="layui-row">
                    <div class="layui-col-xs8">
                        <label class="layui-admin-login-icon layui-icon layui-icon-vercode" for="captcha"></label>
                        <input type="text" id="captcha" name="_captcha_" lay-verify="required" placeholder="图形验证码"
                               class="layui-input" lay-verType="tips">
                    </div>
                    <div class="layui-col-xs3 layui-col-md-offset1">
                        <img src="captcha" class="layui-admin-login-captcha" title="单击刷新" alt="验证码"/>
                    </div>
                </div>
            </div>
        </#if>
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