;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
}).use(['restful', 'laydate', 'laytpl', 'form'], function () {
  var $ = layui.$;
  var feedback = layui.feedback;
  var restful = layui.restful;
  var laydate = layui.laydate;
  var laytpl = layui.laytpl;
  var form = layui.form;

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

  var barChartMap = {}, colorMap = {
        'RED': '#FF5722',
        'YELLOW': '#FFB800',
        'GREEN': '#5FB878',
        'NONE': '#E2E2E2'
      }, now = new Date(), pattern = 'yyyy-MM-dd hh:mm:ss',
      start = new Date(now.getTime() - 3600 * 24 * 1000).format(pattern), end = now.format(pattern);

  /********************************组件渲染*********************************/

  /********************************事件绑定*********************************/
  $('.j-time-type').on('click', function () {
    var jq = $(this);
    if (!jq.hasClass('layui-btn-primary')) {
      return;
    }
    $('#dateText').html('自定义');
    jq.removeClass('layui-btn-primary').siblings().addClass('layui-btn-primary');
    doQuery({timeType: jq.data('type')})
  }).eq(0).click();

  $('#btnTrigger').one('click', function () {
    laydate.render({
      elem: '#datePicker'
      , type: 'datetime'
      , range: '至'
      , value: start + ' 至 ' + end
      , min: '2021-01-01 00:00:00'
      , max: end
      , done: function (value) {
        if (!value) {
          return;
        }
        var arr = value.split(' 至 ');
        start = arr[0];
        end = arr[1];
        //计算两个时间间隔天数
        if (((new Date(end) - new Date(start)) / (1000 * 60 * 60 * 24)) > 31) {
          feedback.layer.msg('最多选择31天');
        } else {
          $('#dateText').html(value);
          $('#btnTrigger').removeClass('layui-btn-primary').siblings().addClass('layui-btn-primary');
          doQuery({timeType: 'X', start: start, end: end})
        }
      }
    });
    $('#datePicker').click();
  });

  $('#btnTriggerConf').on('click', function () {
    restful.get('auth/api/factory/rank/conf', {}, function (list) {
      var panel = $('#confPanel');
      var templete = $('#confRow').html();
      layui.each(list, function (index, group) {
        laytpl(templete).render(group, function (row) {
          panel.append(row);
        });
      })
      $('.layui-card-body').toggleClass('layui-hide');
      form.render();
    });
  });

  $('#btnBackConf').on('click', function () {
    $('.layui-card-body').toggleClass('layui-hide');
    $('#confPanel').empty();
  });

  function doQuery(params) {
    restful.get('auth/api/factory/rank', params, function (rank) {
      renderChart('statusGreen', '运行排行榜', rank['GREEN'], 'greenDuration', colorMap['GREEN']);
      renderChart('statusRed', '故障排行榜', rank['RED'], 'redDuration', colorMap['RED']);
      renderChart('statusYellow', '暂停排行榜', rank['YELLOW'], 'yellowDuration', colorMap['YELLOW']);
      renderChart('statusNone', '离线排行榜', rank['NONE'], 'noneDuration', colorMap['NONE']);
    });
  }

  function renderChart(elementId, title, list, key, color) {
    var barChart = barChartMap[elementId];
    if (!barChart) {
      var jq = $('#' + elementId);
      jq.height(300);
      barChart = echarts.init(jq[0]);
    } else {
      barChart.clear();
    }
    var option = {
      title: {
        text: title
      },
      tooltip: {},
      xAxis: {
        data: $.map(list, function (item) {
          return item['name'];
        })
      },
      yAxis: {
        name: '时长(秒)'
      },
      series: [{
        name: '时长',
        type: 'bar',
        itemStyle: {
          color: color
        },
        data: $.map(list, function (item) {
          return item[key];
        })
      }]
    };
    barChart.setOption(option);
  }
});