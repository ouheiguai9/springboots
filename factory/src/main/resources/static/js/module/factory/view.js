;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
}).use(['restful', 'laytpl'], function () {
  var $ = layui.$;
  var restful = layui.restful;
  var laytpl = layui.laytpl;
  var initTime = (new Date()).getTime();

  /********************************组件渲染*********************************/
  $(document).off('ajaxStart').off('ajaxStop');
  console.info($);
  render();
  // setInterval(render, 2 * 60 * 1000);
  setInterval(render, 10 * 1000);

  /********************************事件绑定*********************************/
  function render() {
    restful.get('auth/api/factory/view', {initTime: initTime}, function (view) {
      // noinspection JSUnresolvedVariable
      $('h1').html(view.timeInterval);
      $('div.total-title-card').find('label').each(function (index) {
        // noinspection JSUnresolvedVariable
        $(this).html(view.totalArr[index]);
      });
      var noRowTr = $('#noRowTr');
      if (view.list && view.list.length > 0) {
        var tbody = $('tbody');
        tbody.find('tr').not(noRowTr).remove();
        $.each(view.list, function (i, item) {
          switch (item.status) {
            case '暂停':
              item.color = 'layui-bg-orange';
              break;
            case '运行':
              item.color = 'layui-bg-green';
              break;
            case '离线':
              item.color = 'layui-bg-gray';
              break;
            default:
              item.color = '';
          }
          laytpl($('#trTemplete').html()).render(item, function (tr) {
            tbody.append(tr);
          });
        });
        noRowTr.addClass('layui-hide');
      } else {
        noRowTr.removeClass('layui-hide');
      }
    });
  }
});