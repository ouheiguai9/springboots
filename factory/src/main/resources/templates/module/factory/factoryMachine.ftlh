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
        <div class="layui-btn-group">
            <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
            <a class="layui-btn layui-btn-xs layui-btn-danger" lay-event="delete">删除</a>
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
                <input type="hidden" name="id">
                <div class="layui-row">
                    <div class="layui-col-md8">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="ordering">本厂编号：</label>
                            <div class="layui-input-block">
                                <input id="ordering" type="text" name="ordering" class="layui-input"
                                       placeholder="不超过8个字符(如:YG-0001)"
                                       lay-verify="required" lay-verType="tips" maxlength="8">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md4">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="category">设备类型：</label>
                            <div class="layui-input-block">
                                <select id="category" name="category" lay-filter="cascade"></select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md4">
                        <div class="layui-form-item j-known-type">
                            <label class="layui-form-label" for="type">具体类型：</label>
                            <div class="layui-input-block">
                                <select id="type" name="type" lay-filter="type"></select>
                            </div>
                        </div>
                        <div id="unknownType" class="layui-form-item layui-hide">
                            <label class="layui-form-label" for="unknown">具体类型：</label>
                            <div class="layui-input-block">
                                <input id="unknown" type="text" name="unknown" class="layui-input"
                                       placeholder="请输入自定义类型"
                                       maxlength="16">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md8">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="type">设备型号：</label>
                            <div class="layui-input-block">
                                <input id="name" type="text" name="name" class="layui-input"
                                       placeholder="不超过8个字符(如:VMC850)"
                                       maxlength="8">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row j-known-type">
                    <div class="layui-col-md4">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="country">产地：</label>
                            <div class="layui-input-block">
                                <select id="country" name="country" lay-filter="cascade"></select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md4">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="brand">品牌：</label>
                            <div class="layui-input-block">
                                <select id="brand" name="brand" lay-filter="brand"></select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md8">
                        <div class="layui-form-item j-known-type">
                            <label class="layui-form-label" for="software">控制系统：</label>
                            <div class="layui-input-block">
                                <select id="software" name="software">
                                    <option value="FANUC" selected>FANUC</option>
                                    <option value="三菱">三菱</option>
                                    <option value="西门子">西门子</option>
                                    <option value="新代"> 新代</option>
                                    <option value="宝元">宝元</option>
                                    <option value="广数GSK">广数GSK</option>
                                    <option value="华中">华中</option>
                                    <option value="凯恩帝KND">凯恩帝KND</option>
                                    <option value="莱纳克LYNUC">莱纳克LYNUC</option>
                                    <option value="华兴">华兴</option>
                                    <option value="广泰">广泰</option>
                                    <option value="OKUMA">OKUMA</option>
                                    <option value="海德汉">海德汉</option>
                                    <option value="NUM">NUM</option>
                                    <option value="HASS">HASS</option>
                                    <option value="PLC/工控机">PLC/工控机</option>
                                    <option value="其它">其它</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md4">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="operator">操作人员：</label>
                            <div class="layui-input-block">
                                <input id="operator" type="text" name="operator" class="layui-input"
                                       placeholder="不超过8个字符(如:李大宝)"
                                       lay-verify="required" lay-verType="tips" maxlength="8">
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md4">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="description">零件名称：</label>
                            <div class="layui-input-block">
                                <input id="description" type="text" name="description" class="layui-input"
                                       placeholder="不超过16个字符" maxlength="16">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="layui-row">
                    <div class="layui-col-md4">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="triColorLEDId">三色灯：</label>
                            <div class="layui-input-block">
                                <select id="triColorLEDId" name="triColorLEDId"></select>
                            </div>
                        </div>
                    </div>
                    <div class="layui-col-md4">
                        <div class="layui-form-item">
                            <label class="layui-form-label" for="workshopId">归属车间：</label>
                            <div class="layui-input-block">
                                <select id="workshopId" name="workshopId"></select>
                            </div>
                        </div>
                    </div>
                </div>
                <#--                <div class="layui-row">-->
                <#--                    <div class="layui-col-md12">-->
                <#--                        <div class="layui-form-item layui-form-text">-->
                <#--                            <label class="layui-form-label" for="description">详细信息：</label>-->
                <#--                            <div class="layui-input-block">-->
                <#--                                <textarea id="description" name="description" class="layui-textarea"-->
                <#--                                          placeholder="不超过16个字符" maxlength="16"></textarea>-->
                <#--                            </div>-->
                <#--                        </div>-->
                <#--                    </div>-->
                <#--                </div>-->
                <div class="layui-row">
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
<script src="js/module/factory/machine.js" type="text/javascript" charset="UTF-8"></script>
</html>