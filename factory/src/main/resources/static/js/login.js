;
layui.use(['form', 'jquery'], function () {
  var $ = layui.$;

  /********************************组件渲染*********************************/


  /********************************事件绑定*********************************/
  $('div.u-menu-item').on('click', function () {
    window.location.href = this.dataset.href;
  });
  $('img.layui-admin-login-captcha').on('click', function () {
    $(this).prop('src', 'captcha?r=' + Math.random());
  });
});