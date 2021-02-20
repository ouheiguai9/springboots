;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
}).use(['element', 'form', 'laydate', 'table', 'restful', 'laytpl'], function () {
  var $ = layui.$;
  var form = layui.form;
  var laydate = layui.laydate;
  var table = layui.table;
  var feedback = layui.feedback;
  var restful = layui.restful;
  var laytpl = layui.laytpl;

  Date.prototype.format = function (fmt) {
    var o = {
      'M+': this.getMonth() + 1, //月份
      'd+': this.getDate(), //日
      'h+': this.getHours(), //小时
      'm+': this.getMinutes(), //分
      's+': this.getSeconds(), //秒
      'q+': Math.floor((this.getMonth() + 3) / 3), //季度
      'S': this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
    for (var k in o)
      if (new RegExp('(' + k + ')').test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (('00' + o[k]).substr(('' + o[k]).length)));
    return fmt;
  };

  var now = new Date(), pattern = 'yyyy-MM-dd hh:mm:ss', status = 'YELLOW', threshold = 180,
    start = new Date(now.getTime() - 3600 * 24 * 1000).format(pattern), end = now.format(pattern), isValidPeriod = true;

  /********************************组件渲染*********************************/
  var periodObj = laydate.render({
    elem: '#period'
    , type: 'datetime'
    , range: '至'
    , value: start + ' 至 ' + end
    , min: '2021-01-01 00:00:00'
    , max: end
    , done: function (value) {
      var arr = value.split(' 至 ');
      start = arr[0];
      end = arr[1];
      //计算两个时间间隔天数
      if (((new Date(end) - new Date(start)) / (1000 * 60 * 60 * 24)) > 31) {
        feedback.layer.msg('最多选择31天');
        isValidPeriod = false;
      } else {
        isValidPeriod = true;
      }
    }
  });

  var tableIns = table.render({
    elem: '#tableList'
    , url: 'auth/api/factory/abnormal/detail?'
    , request: {
      pageName: 'page' //页码的参数名称，默认：page
      , limitName: 'size' //每页数据量的参数名，默认：limit
    }
    , where: {
      'deviceId': '',
      'status': status,
      'threshold': threshold,
      'start': start,
      'end': end
    }
    , parseData: function (res) { //res 即为原始返回的数据
      return {
        'code': 0, //解析接口状态
        'msg': '加载成功', //解析提示文本
        'count': res.totalElements, //解析数据长度
        'data': res.content //解析数据列表
      };
    }
    , toolbar: '#tableListToolBar'
    , cols: [[
      {type: 'numbers'}
      , {field: 'time', title: '开始时间', sort: true}
      , {
        field: 'time', title: '结束时间', templet: function (d) {
          return new Date(new Date(d.time).getTime() + d.duration * 1000).format(pattern);
        }
      }
      , {field: 'duration', title: '持续时间(秒)'}
    ]]
    , autoSort: false
    , page: {
      curr: 0
      , first: 0
      , limit: 30
    }
  });

  /********************************事件绑定*********************************/
  $('#btnQuery').on('click', function () {
    if (!isValidPeriod) {
      feedback.layer.msg('最多选择31天');
      return;
    }
    status = $('#status').val();
    threshold = $('#threshold').val();
    restful.get('auth/api/factory/abnormal', {
      'status': status,
      'threshold': threshold,
      'start': start,
      'end': end
    }, function (list) {
      var noRowTr = $('#noRowTr');
      if (list && list.length > 0) {
        var tbody = $('tbody');
        tbody.find('tr').not(noRowTr).remove();
        $.each(list, function (i, item) {
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
  }).click();

  $('#btnBack').on('click', function () {
    $('section.layui-card').toggleClass('layui-hide');
  });

  table.on('sort(tableList)', function (obj) {
    table.reload('tableList', {
      initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。
      , where: { //请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
        sort: obj.field + ',' + obj.type //排序字段
      }
    });
  });

  function showSingleMachine(event) {
    var machine = event.data;
    if (machine.count === 0) return;
    $('section.layui-card').toggleClass('layui-hide');
    $('h1.local-time-title').html(laytpl('{{d.start}} 至 {{d.end}} {{d.machine.type}} {{d.machine.name}} {{d.machine.status}}超过{{d.threshold}}秒明细').render({
      'start': start
      , 'end': end
      , 'threshold': threshold
      , 'machine': machine
    }));
    tableIns.reload({
      where: {
        'deviceId': machine.deviceId,
        'status': status,
        'threshold': threshold,
        'start': start,
        'end': end
      }
    });
  }
});