;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
}).use(['element', 'util', 'restful'], function () {
  var element = layui.element;
  var util = layui.util;
  var $ = layui.$;
  var feedback = layui.feedback;

  /********************************组件渲染*********************************/


  /********************************事件绑定*********************************/
  util.event('lay-click-event', {
    flexible: function (jq) {
      jq.children('i').toggleClass('layui-icon-shrink-right').toggleClass('layui-icon-spread-left');
      $('.layui-admin').toggleClass('layui-admin-shrink');
    }
    , refresh: function () {

    }
    , fullscreen: function (jq) {
      var fullClass = 'layui-icon-screen-full';
      var restoreClass = 'layui-icon-screen-restore';
      var jqIcon = jq.children('i');
      if (jqIcon.hasClass(fullClass)) {
        doFullScreen();
        jqIcon.addClass(restoreClass).removeClass(fullClass);
      } else {
        doExitScreen();
        jqIcon.addClass(fullClass).removeClass(restoreClass);
      }
    }
    , logout: function () {
      feedback.confirm('确定退出系统?', function () {
        location.href = 'logout';
      });
    }
  });

  /**
   * 全屏
   */
  function doFullScreen() {
    // noinspection JSUnresolvedVariable
    var e = document.documentElement
        , f = e.requestFullScreen || e.webkitRequestFullScreen || e.mozRequestFullScreen || e.msRequestFullscreen;
    'undefined' != typeof f && f && f.call(e)
  }

  /**
   * 退出全屏
   */
  function doExitScreen() {
    // noinspection JSUnresolvedVariable
    document.exitFullscreen ? document.exitFullscreen() : document.mozCancelFullScreen ? document.mozCancelFullScreen() : document.webkitCancelFullScreen ? document.webkitCancelFullScreen() : document.msExitFullscreen && document.msExitFullscreen()
  }
});