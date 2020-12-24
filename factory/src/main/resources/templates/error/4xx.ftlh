<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../include/common_head_tpt.ftlh">
    <link href="css/error.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-layout-body layui-bg-gray">
<div class="layui-fluid">
    <div class="layui-admin-error-tips">
        <i class="layui-icon" face>&#xe664;</i>
        <div class="layui-text">
            <h1>
                <#--noinspection FtlReferencesInspection-->
                <span class="layui-anim layui-anim-loop layui-anim-">${status[0]}</span>
                <#--noinspection FtlReferencesInspection-->
                <span class="layui-anim layui-anim-loop layui-anim-rotate">${status[1]}</span>
                <#--noinspection FtlReferencesInspection-->
                <span class="layui-anim layui-anim-loop layui-anim-">${status[2]}</span>
            </h1>
        </div>
    </div>
</div>
</body>
</html>