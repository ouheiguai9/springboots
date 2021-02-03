;
layui.define(['feedback', 'jquery'], function (exports) {
  var feedback = layui.feedback, $ = layui.$;
  var forbidLoading = false;

  function Restful() {
    // this.loadingIndex = undefined;
    this.init();
  }

  Restful.prototype = {
    init: function () {
      var self = this;
      $(document).ajaxStart(function () {
        if (forbidLoading) return;
        feedback.loading();
      }).ajaxStop(function () {
        feedback.closeLoading();
      }).ajaxError(function (event, jqXHR) {
        if (jqXHR.responseJSON) {
          feedback.failMsg(jqXHR.responseJSON.message);
        } else {
          if (jqXHR.status === 401) {
            feedback.failMsg('未登陆或者未授权该操作', function () {
              location.reload();
            });
          } else {
            feedback.failMsg('未知错误，请联系管理员');
          }
        }
      });
    }
    , disableLoading: function () {
      forbidLoading = true;
    }
    , enableLoading: function () {
      forbidLoading = false;
    }
    , get: function (url, params, callback) {
      $.get(url, params, function (data) {
        callback(data);
      }, 'json');
    }
    , post: function (url, params, callback) {
      $.ajax(url, {
        type: 'POST'
        , contentType: 'application/json;charset=UTF-8'
        , dataType: 'json'
        , data: params ? JSON.stringify(params) : null
        , success: function (data) {
          callback(data);
        }
      });
    }
    , postForm: function (url, params, callback) {
      $.post(url, params, function (data) {
        callback(data);
      }, 'json');
    }
    , put: function (url, params, callback) {
      $.ajax(url, {
        type: 'PUT'
        , contentType: 'application/json;charset=UTF-8'
        , dataType: 'json'
        , data: params ? JSON.stringify(params) : null
        , success: function (data) {
          callback(data);
        }
      });
    }
    , delete: function (url, params, callback) {
      $.ajax(url, {
        type: 'DELETE'
        , contentType: 'application/json;charset=UTF-8'
        , dataType: 'json'
        , data: params ? JSON.stringify(params) : null
        , success: function (data) {
          callback(data);
        }
      });
    }
  };

  exports('restful', new Restful());
});