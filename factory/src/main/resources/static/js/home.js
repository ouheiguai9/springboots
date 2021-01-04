;
layui.use(['util'], function () {
  var $ = layui.$;
  /********************************组件渲染*********************************/


  /********************************事件绑定*********************************/
  $('#setHomePage').on('click', function () {
    if (window.parent) {
      window.parent.openSetHomePage()
    }
  });
});