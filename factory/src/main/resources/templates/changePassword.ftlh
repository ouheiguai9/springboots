<#import 'lib/security_lib.ftlh' as security>
<!DOCTYPE html>
<html lang="en">
<head>
    <#include "include/common_head_tpt.ftlh">
    <link href="css/login.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray">
<div class="layui-admin-login-main">
    <#if !security.isAuthentication()>
        <h2 class="layui-admin-login-header">智慧机床</h2>
    </#if>
    <form class="layui-form">
        <div class="layui-admin-login-error-info">${error!}</div>
        <input type="hidden" name="authentication" id="authentication"
               value="${security.isAuthentication()?string('yes', 'no')}">
        <input type="hidden" name="username" id="username" value="${username}">
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-password" for="oldPassword"></label>
            <input type="password" name="oldPassword" id="oldPassword" lay-verify="required" placeholder="原始密码"
                   class="layui-input">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-password" for="newPassword"></label>
            <input type="password" name="newPassword" id="newPassword" lay-verify="required|password" placeholder="新密码"
                   class="layui-input">
        </div>
        <div class="layui-form-item">
            <label class="layui-admin-login-icon layui-icon layui-icon-password" for="password"></label>
            <input type="password" name="repassword" id="repassword" lay-verify="required|repassword" placeholder="确认密码"
                   class="layui-input" data-equal="newPassword">
        </div>
        <div class="layui-form-item">
            <button class="layui-btn layui-btn-fluid" lay-submit>确 定</button>
        </div>
    </form>
</div>
</body>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/changePassword.js" type="text/javascript" charset="UTF-8"></script>
</html>