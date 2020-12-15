<#import 'lib/security_lib.ftlh' as security>
<!DOCTYPE html>
<html lang="en">
<head>
    <#include "include/common_head_tpt.ftlh">
    <link href="css/home.css" rel="stylesheet" type="text/css">
</head>
<body class="layui-admin layui-layout-body layui-bg-gray layui-unselect">
<header>
    <ul class="layui-nav layui-layout-left">
        <li class="layui-nav-item" lay-unselect>
            <a id="menuFlexible" href="javascript:void(1);" lay-click-event="flexible" title="侧边伸缩">
                <i class="layui-icon layui-icon-shrink-right"></i>
            </a>
        </li>
        <li class="layui-nav-item" lay-unselect>
            <a href="javascript:void(1);" lay-click-event="refresh" title="刷新">
                <i class="layui-icon layui-icon-refresh-3"></i>
            </a>
        </li>
    </ul>
    <ul class="layui-nav layui-layout-right" lay-unselect>
        <li class="layui-nav-item">
            <a lay-href="app/message/index.html" lay-click-event="message" lay-text="消息中心">
                <i class="layui-icon layui-icon-notice"></i>
                <!-- 如果有新消息，则显示小圆点 -->
                <span class="layui-badge-dot"></span>
            </a>
        </li>
        <li class="layui-nav-item" lay-unselect>
            <a href="javascript:void(1);" lay-click-event="fullscreen">
                <i class="layui-icon layui-icon-screen-full"></i>
            </a>
        </li>
        <li class="layui-nav-item" lay-unselect>
            <a href="javascript:void(1);" class="layui-elip" style="max-width: 80px;">
                ${security.getCurrentUser().getNickname()}
            </a>
            <dl class="layui-nav-child">
                <#if !security.isAdmin()>
                    <dd class="j-href-menu" data-key="test" data-href="safeChangePassword" data-target="inner" lay-unselect>
                        <a href="javascript:void(1);">基本资料</a>
                    </dd>
                    <dd class="j-href-menu" data-key="test" data-href="safeChangePassword" data-target="inner" lay-unselect>
                        <a href="javascript:void(1);">修改密码</a>
                    </dd>
                    <hr>
                </#if>
                <dd lay-unselect lay-click-event="logout">
                    <a href="javascript:void(1);" style="color: #ff5722; text-align: center;">退出</a>
                </dd>
            </dl>
        </li>
    </ul>
</header>
<section class="layui-side">
    <div class="layui-side-scroll">
        <div class="layui-logo"></div>
        <ul class="layui-nav layui-nav-tree">
            <li data-name="home" class="layui-nav-item layui-nav-itemed">
                <a href="javascript:void(1);" class="j-folder-menu">
                    <i class="layui-icon layui-icon-home"></i>
                    <cite>主页</cite>
                </a>
                <dl class="layui-nav-child">
                    <dd class="j-href-menu" data-key="test" data-href="safeChangePassword" data-target="inner">
                        <a href="javascript:void(1);">控制台</a>
                    </dd>
                    <dd class="j-href-menu" data-key="test" data-href="safeChangePassword" data-target="inner">
                        <a href="javascript:void(1);">主页一</a>
                    </dd>
                    <dd class="j-href-menu" data-key="test" data-href="safeChangePassword" data-target="inner">
                        <a href="javascript:void(1);">主页二</a>
                    </dd>
                </dl>
            </li>
        </ul>
    </div>
</section>
<section class="page-tabs">
    <div class="layui-icon page-tabs-control layui-icon-prev" lay-click-event="leftPage"></div>
    <div class="layui-icon page-tabs-control layui-icon-next" lay-click-event="rightPage"></div>
    <div class="layui-icon page-tabs-control layui-icon-down">
        <ul class="layui-nav page-tabs-select">
            <li class="layui-nav-item">
                <a href="javascript:void(1);"></a>
                <dl class="layui-nav-child layui-anim-fadein">
                    <dd lay-unselect lay-click-event="closeThisTabs"><a href="javascript:void(1);">关闭当前标签页</a></dd>
                    <dd lay-unselect lay-click-event="closeOtherTabs"><a href="javascript:void(1);">关闭其它标签页</a></dd>
                    <dd lay-unselect lay-click-event="closeAllTabs"><a href="javascript:void(1);">关闭全部标签页</a></dd>
                </dl>
            </li>
        </ul>
    </div>
    <div class="layui-tab layui-tab-brief" lay-unauto lay-allowClose="true" lay-filter="lay-admin-tabs">
        <ul class="layui-tab-title">
            <li class="layui-this"><i class="layui-icon layui-icon-home"></i></li>
        </ul>
    </div>
</section>
<section class="layui-body">
    <div class="page-tabs-body-item">
        <iframe src="safeChangePassword" frameborder="0" class="iframe-item"></iframe>
    </div>
</section>
</body>
<script src="plugin/layui/layui.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/home.js" type="text/javascript" charset="UTF-8"></script>
</html>