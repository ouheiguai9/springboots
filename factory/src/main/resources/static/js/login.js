;
layui.use(['form', 'jquery', 'layer'], function () {
  var $ = layui.$;
  var layer = layui.layer;

  /********************************组件渲染*********************************/


  /********************************事件绑定*********************************/
  $('div.u-menu-item').on('click', function () {
    window.location.href = this.dataset.href;
  });
  $('img.layui-admin-login-captcha').on('click', function () {
    $(this).prop('src', 'captcha?r=' + Math.random());
  });
  $('#btnForgetPassword').on('click', function () {
    layer.alert('尊敬的企业客户：<br/>如果密码丢失，请联系人工客服。<br/>联系电话：18114758636/0512-68052881');
  });
});