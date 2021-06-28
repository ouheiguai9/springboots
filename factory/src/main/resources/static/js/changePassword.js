;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
}).use(['form', 'restful'], function () {
  var form = layui.form;
  var $ = layui.$;
  var restful = layui.restful;
  var feedback = layui.feedback;

  /********************************组件渲染*********************************/


  /********************************事件绑定*********************************/
  form.on('submit()', function (params) {
    restful.postForm('changePassword', params.field, function () {
      var afterClose = undefined, msg = '密码修改成功';
      if (params.field.authentication === 'no') {
        msg += ', 请重新登录';
        afterClose = function () {
          location.href = 'login';
        };
      }
      feedback.successMsg(msg, afterClose);
    });
    return false;
  });


  /********************************函数定义*********************************/
  form.verify({
    password: function (value, item) {
      if (/^.{6,16}$/.test(value)) {
        var contain = 0;
        if (/[A-Z]/.test(value)) {
          contain += 1;
        }
        if (/[a-z]/.test(value)) {
          contain += 1;
        }
        if (/[0-9]/.test(value)) {
          contain += 1;
        }
        if (/[!@#$%^&*? ]/.test(value)) {
          contain += 1;
        }
        if (contain > 1) {
          return;
        }
      }
      return '长度6-16位(必须包含大小写字母、数字、特殊符号中任意两种)';
    }
    , repassword: function (value, item) {
      var equalId = $(item).data('equal');
      if (value === $('#' + equalId).val()) {
        return;
      }
      return '两次输入密码不一致';
    }
  });
});