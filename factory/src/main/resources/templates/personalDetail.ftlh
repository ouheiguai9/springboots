<!DOCTYPE html>
<html lang="en">
<head>
    <#include "include/common_head_tpt.ftlh">
    <link href="plugin/layui_exts/selects/formSelects-v4.css" rel="stylesheet" type="text/css">
    <link href="css/crud.css" rel="stylesheet" type="text/css">
</head>
<body>
<section class="layui-fluid">
    <div class="layui-card">
        <div class="layui-card-body">
            <form class="layui-form layui-form-pane" lay-filter="editForm">
                <input type="hidden" id="userId" name="id" value="${me.getId()}">
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label">用户名：</label>
                            <div class="layui-input-block">
                                <div class="layui-fake-input layui-disabled">${me.getUsername()}</div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label">手机号：</label>
                            <div class="layui-input-block">
                                <div class="layui-fake-input layui-disabled">${me.getPhone()}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label">密码：</label>
                            <div class="layui-input-block">
                                <div class="layui-fake-input layui-disabled">******</div>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item">
                            <label class="layui-form-label">邮箱：</label>
                            <div class="layui-input-block">
                                <input type="text" name="email" id="email" placeholder="邮箱(选填)"
                                       value="${me.getEmail()!}" class="layui-input" lay-verType="tips">
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
                                       placeholder="公司名称(选填)" value="${me.getNickname()!}" class="layui-input"
                                       lay-verType="tips">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md6">
                        <div class="layui-form-item" pane>
                            <label class="layui-form-label">性别：</label>
                            <div class="layui-input-block">
                                <input type="radio" name="sex" value="true"
                                       title="男" ${me.isSex()?string("checked","")}>
                                <input type="radio" name="sex" value="false"
                                       title="女" ${me.isSex()?string("","checked")}>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        <div class="layui-form-item">
                            <label id="labelRole" class="layui-form-label">角色：</label>
                            <div class="layui-input-block">
                                <div class="layui-fake-input layui-disabled">
                                    <#list me.getRoleSet() as role>
                                        <span class="layui-badge layui-bg-green">${role.getName()}</span>
                                    </#list>
                                </div>
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
                                       value="${me.getAvatar()!}" disabled>
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
                                <input type="text" name="address" id="address" placeholder="地址(选填)" class="layui-input"
                                       value="${me.getAddress()!}">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md12">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="validPeriod">有效期：</label>
                            <div class="layui-input-block">
                                <div class="layui-fake-input layui-disabled">${me.getValidPeriod()}</div>
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