layui.define(['layer'], function (exports) {
  var layer = layui.layer;
  var loadingCounter = 0, loadingIndex;

  function Feedback() {
  }

  Feedback.prototype = {
    successMsg: function (content, onAfterClose) {
      var config = {
        skin: 'layui-admin-layer'
        , closeBtn: 0
        , title: false
        , move: false
        , btn: false
        , shadeClose: true
        , time: 1000000
        , content: '<div class="success-box">'
            + '<i class="layui-icon layui-icon-ok-circle"></i>'
            + '<span class="layui-text layui-elip">' + (content || '操作成功') + '</span>'
            + '</div>'

      };
      if (typeof onAfterClose === 'function') {
        config.end = onAfterClose;
      }
      layer.open(config);
    }
    , failMsg: function (detail, onAfterClose) {
      var showDetail = this.constraintLength(detail, 250);
      var config = {
        skin: 'layui-admin-layer'
        , closeBtn: 0
        , title: false
        , move: false
        , btn: ['<i class="layui-icon layui-icon-close"></i>']
        , time: 0
        , content: '<div class="failure-box">'
            + '<i class="layui-icon layui-icon-about"></i>'
            + '<span class="layui-text">操作失败</span>'
            + '<p class="error-detail">' + showDetail + '</p>'
            + '</div>'
      };
      if (typeof onAfterClose === 'function') {
        config.end = onAfterClose;
      }
      layer.open(config);
    }
    , loading: function () {
      if (loadingCounter < 1) {
        var icon = '<i class="layui-icon layui-icon-loading layui-anim layui-anim-rotate layui-anim-loop f-txt-white"/>';
        loadingIndex = layer.open({
          skin: 's-layer-feedback'
          , closeBtn: 0
          , title: false
          , move: false
          , btn: false
          , time: 0
          , content: icon + '<span class="layui-text layui-elip f-txt-white">操作进行中</span>'
        });
      }
      loadingCounter++;
    }
    , closeLoading: function () {
      if (loadingCounter < 1) return;
      if (loadingCounter > 1) {
        loadingCounter--;
        return;
      }
      layer.close(loadingIndex);
      loadingCounter = 0;
    }
    , constraintLength: function (str, maxLength) {
      if (maxLength < 1) return str;
      //获得字符串实际长度，中文2，英文1
      var realLength = 0, len = str.length, charCode = -1;
      for (var i = 0; i < len; i++) {
        charCode = str.charCodeAt(i);
        if (charCode >= 0 && charCode <= 128)
          realLength += 1;
        else
          realLength += 2;
        if (realLength > maxLength) {
          return str.slice(0, i) + '...';
        }
      }
      return str;
    }
    , prompt: layer.prompt
    , confirm: layer.confirm
    , open: layer.open
    , tips: layer.tips
    , close: layer.close
    , closeAll: layer.closeAll
    , layer: layer
  };

  exports('feedback', new Feedback());
});