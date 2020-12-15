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
  $('.j-folder-menu').mouseenter(function () {
    if (!$('body').hasClass('layui-admin-shrink')) return;
    var jq = $(this);
    var cite = jq.find('cite');
    if (cite.length === 0) return;
    var msg = cite.html();
    var index = feedback.tips(msg, this, {
      tips: [2, '#333'],
      time: -1
    });
    jq.data('index', index);
  }).mouseleave(function () {
    if (!$('body').hasClass('layui-admin-shrink')) return;
    feedback.close($(this).data('index'));
  }).click(function () {
    if ($('body').hasClass('layui-admin-shrink')) {
      $('#menuFlexible').trigger('click');
      feedback.close($(this).data('index'));
    }
  });

  $('.j-href-menu').on('click', function () {
    var jq = $(this);
    var jqTitle = $('.layui-tab-title');
    var param = {
      key: jq.data('key')
      , url: jq.data('href')
      , target: jq.data('target')
      , name: jq.find('a').html()
    };
    while (rollPageTabsTitle(false)) {
    }
    var maxWidth = jqTitle.innerWidth();
    var items = jqTitle.find('li').not('.layui-hide');
    var invalidWidth = 130;
    items.each(function (index, item) {
      invalidWidth += $(item).outerWidth();
    });
    if (invalidWidth > maxWidth) {
      $(items[0]).addClass('layui-hide');
    }

    element.tabAdd('lay-admin-tabs', {
      title: param.name
      , content: param.url
      , id: param.key
    });
  });

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
    , leftPage: function () {
      rollPageTabsTitle(true);
    }
    , rightPage: function (jq) {
      rollPageTabsTitle(false);
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

  /**
   * 移动标题
   * @param isLeft 是否左移
   * @return {boolean}
   */
  function rollPageTabsTitle(isLeft) {
    var jqTitle = $('.layui-tab-title');
    var items = jqTitle.find('li');
    var showItems = items.not('.layui-hide');
    var begin = items.index(showItems[0]);
    var step = -1;
    if (isLeft) {
      if (begin === 0) return false;
      begin += step;
    } else {
      step = 1;
      begin += showItems.length;
      if (begin === items.length) return false;
    }
    var maxWidth = jqTitle.innerWidth();
    var invalidWidth = 130;
    var tmpLen = 0;
    while (begin > -1 && begin < items.length && invalidWidth < maxWidth) {
      if (tmpLen < showItems.length) {
        var showItemIndex = begin - step * showItems.length;
        $(items[begin - step * showItems.length]).addClass('layui-hide');
      }
      invalidWidth += $(items[begin]).removeClass('layui-hide').outerWidth();
      tmpLen++;
      begin += step;
    }
    return true;
  }
});