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

  /********************************组件渲染*********************************/
  renderTable();

  /********************************事件绑定*********************************/

  function renderTable() {
    $('div.layui-card').toggleClass('layui-hide');
    $('#singleCard').empty();
    restful.get('auth/api/factory/abnormal', {
      'status': 'YELLOW',
      'threshold': 20,
      'start': '2020-01-01 00:00:00',
      'end': '2020-03-01 00:00:00'
    }, function (view) {
      // noinspection JSUnresolvedVariable
      $('#listCard').find('h1').html(view.timeInterval);
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
            tbody.append($(tr).find('a').bind('click', item, showSingleMachine).end());
          });
        });
        noRowTr.addClass('layui-hide');
      } else {
        noRowTr.removeClass('layui-hide');
      }
    });
  }

  function showSingleMachine(event) {
    var machine = event.data;

    $('div.layui-card').toggleClass('layui-hide');
    laytpl($('#singleTemplete').html()).render(machine, function (content) {
      var jq = $(content);
      $('#singleCard').append(jq);
    });
    $('#btnBack').on('click', renderTable);
  }
});