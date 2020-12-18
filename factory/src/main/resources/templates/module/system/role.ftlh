<!DOCTYPE html>
<html lang="en">
<head>
    <#include "../../include/common_head_tpt.ftlh">
    <link href="css/home.css" rel="stylesheet" type="text/css">
</head>
<body>
<section class="layui-fluid">
    <div class="layui-card">
        <div class="layui-form layui-card-header layuiadmin-card-header-auto">
            <div class="layui-form-item">
                <div class="layui-inline">
                    角色筛选
                </div>
                <div class="layui-inline">
                    <select name="rolename" lay-filter="LAY-user-adminrole-type">
                        <option value="-1">全部角色</option>
                        <option value="0">管理员</option>
                        <option value="1">超级管理员</option>
                        <option value="2">纠错员</option>
                        <option value="3">采购员</option>
                        <option value="4">推销员</option>
                        <option value="5">运营人员</option>
                        <option value="6">编辑</option>
                    </select>
                </div>
            </div>
        </div>
        <div class="layui-card-body">
            <div style="padding-bottom: 10px;">
                <button class="layui-btn layuiadmin-btn-role" data-type="batchdel">删除</button>
                <button class="layui-btn layuiadmin-btn-role" data-type="add">添加</button>
            </div>

            <table id="LAY-user-back-role" lay-filter="LAY-user-back-role"></table>
            <script type="text/html" id="buttonTpl">
                {{#  if(d.check == true){ }}
                <button class="layui-btn layui-btn-xs">已审核</button>
                {{#  } else { }}
                <button class="layui-btn layui-btn-primary layui-btn-xs">未审核</button>
                {{#  } }}
            </script>
            <script type="text/html" id="table-useradmin-admin">
                <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i
                            class="layui-icon layui-icon-edit"></i>编辑</a>
                <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i
                            class="layui-icon layui-icon-delete"></i>删除</a>
            </script>
        </div>
    </div>
</section>
</body>
</html>