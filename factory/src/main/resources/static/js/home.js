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
  var pageTab = new PageTab();

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
      key: jq.data('key') + pageTab.pages.length
      , url: jq.data('href')
      , target: jq.data('target')
      , name: jq.find('a').html() + pageTab.pages.length
    };
    if (param.target !== 'inner') {
      window.open(param.url, '_blank');
      return;
    }
    pageTab.addPageTab(param);
  });

  util.event('lay-click-event', {
    flexible: function (jq) {
      jq.children('i').toggleClass('layui-icon-shrink-right').toggleClass('layui-icon-spread-left');
      $('.layui-admin').toggleClass('layui-admin-shrink');
    }
    , refresh: function () {
      var thisUrl = pageTab.pages[pageTab.selectedIndex].url;
      pageTab.jqBody.find('div.page-tabs-body-item').eq(pageTab.selectedIndex).find('iframe').attr('src', thisUrl);
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
      pageTab.rollPageTabTitle(true);
    }
    , rightPage: function () {
      pageTab.rollPageTabTitle(false);
    }
    , closeThisTabs: function () {
      if (pageTab.selectedIndex === 0) return;
      element.tabDelete(pageTab.layTabFilter, pageTab.pages[pageTab.selectedIndex].key);
    }
    , closeOtherTabs: function () {
      $.each(pageTab.pages.slice(1, pageTab.selectedIndex).concat(pageTab.pages.slice(pageTab.selectedIndex + 1, pageTab.pages.length)), function (index, item) {
        element.tabDelete(pageTab.layTabFilter, item.key);
      });
    }
    , closeAllTabs: function () {
      $.each(pageTab.pages.slice(1, pageTab.pages.length), function (index, item) {
        element.tabDelete(pageTab.layTabFilter, item.key);
      });
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

  function PageTab() {
    this.layTabFilter = 'lay-admin-tabs';
    this.jqTitle = $('.page-tabs .layui-tab-title');
    this.jqBody = $('.layui-admin section.layui-body');
    this.maxJqTitileWidth = this.jqTitle.innerWidth();
    this.selectedIndex = 0;
    this.pages = [{}];
    this.pageMap = {};
    this.showTitleStartIndex = 0;
    this.showTitleEndIndex = 0;

    var that = this;
    this.jqTitle.resize(function () {
      that.resize($(this).innerWidth());
    });
    element.on('tab(' + this.layTabFilter + ')', function () {
      that.selectPageTab(that.jqTitle.find('li').index(this));
    });
    element.on('tabDelete(' + this.layTabFilter + ')', function (data) {
      that.deletePageTab(data.index);
    });
  }

  $.extend(PageTab.prototype, {
    resize: function (size) {
      this.maxJqTitileWidth = size;
      this.selfAdaptionTitle(this.selectedIndex);
    }
    , addPageTab: function (param) {
      var exists = this.pageMap[param.key];
      if (!exists) {
        exists = this.pages.length;
        this.pageMap[param.key] = exists;
        this.pages.push(param);
        element.tabAdd(this.layTabFilter, {
          title: param.name
          , content: param.url
          , id: param.key
        });
        var bodyItem = $('<div class="page-tabs-body-item layui-hide"/>').appendTo(this.jqBody);
        $('<iframe frameborder="0" class="iframe-item"/>').appendTo(bodyItem).attr('src', param.url);
      }
      this.selfAdaptionTitle(exists);
      element.tabChange(this.layTabFilter, param.key);
    }
    , deletePageTab: function (index) {
      console.info(this);
      delete this.pageMap[this.pages[index].key];
      this.pages.splice(index, 1);
      console.info(this);
      if (this.selectedIndex === index) {
        this.selectedIndex = -1;
      } else if (this.selectedIndex > index) {
        this.selectedIndex--;
      }
      this.jqBody.find('div.page-tabs-body-item').eq(index).remove();
    }
    , selectPageTab: function (index) {
      if (this.selectedIndex === index) return;
      this.jqBody.find('.page-tabs-body-item').addClass('layui-hide').eq(index).removeClass('layui-hide');
      this.selectedIndex = index;
    }
    , selfAdaptionTitle: function (forceIndex) {
      var items = this.jqTitle.find('li').addClass('layui-hide');
      var invalidWidth = this.renderTitleDom(items[forceIndex], true).outerWidth();
      var left = forceIndex - 1, right = forceIndex + 1;
      this.showTitleStartIndex = this.showTitleEndIndex = forceIndex;
      while (invalidWidth < this.maxJqTitileWidth) {
        var additional = 0;
        if (left > -1) {
          additional += this.renderTitleDom(items[left], true).outerWidth();
          this.showTitleStartIndex = left;
          left -= 1;
        }
        if (right < items.length) {
          additional += this.renderTitleDom(items[right], true).outerWidth();
          this.showTitleEndIndex = right;
          right += 1;
        }
        if (additional === 0) break;
        invalidWidth += additional;
      }
      if (invalidWidth > this.maxJqTitileWidth && this.showTitleStartIndex < this.showTitleEndIndex) {
        var hideIndex;
        if (this.showTitleEndIndex === (items.length - 1)) {
          hideIndex = this.showTitleStartIndex;
          this.showTitleStartIndex++;
        } else {
          hideIndex = this.showTitleEndIndex;
          this.showTitleEndIndex--;
        }
        this.renderTitleDom(items[hideIndex], false);
      }
    }
    , rollPageTabTitle: function (isLeft) {
      var begin = this.showTitleStartIndex, step = -1, invalidWidth = 0;
      if (isLeft) {
        if (this.showTitleStartIndex === 0) return false;
      } else {
        if (this.showTitleEndIndex === (this.pages.length - 1)) return false;
        step = 1;
        begin = this.showTitleEndIndex;
      }
      begin += step;
      var items = this.jqTitle.find('li');
      var tmpLen = 0, showLen = this.showTitleEndIndex - this.showTitleStartIndex + 1;
      while (begin > -1 && begin < items.length && invalidWidth < this.maxJqTitileWidth) {
        if (tmpLen < showLen) {
          this.renderTitleDom(items[begin - step * showLen], false);
        }
        invalidWidth += this.renderTitleDom(items[begin], true).outerWidth();
        tmpLen++;
        begin += step;
      }
      if (isLeft) {
        this.showTitleStartIndex -= tmpLen;
        this.showTitleEndIndex -= Math.min(tmpLen, showLen);
      } else {
        this.showTitleStartIndex += Math.min(tmpLen, showLen);
        this.showTitleEndIndex += tmpLen;
      }
      if (invalidWidth > this.maxJqTitileWidth && this.showTitleStartIndex < this.showTitleEndIndex) {
        var hideIndex;
        if (isLeft) {
          hideIndex = this.showTitleStartIndex++;
        } else {
          hideIndex = this.showTitleEndIndex--;
        }
        this.renderTitleDom(items[hideIndex], false);
      }
      return true;
    }
    , renderTitleDom: function (dom, show) {
      return show ? $(dom).removeClass('layui-hide') : $(dom).addClass('layui-hide');
    }
  }, true);
});