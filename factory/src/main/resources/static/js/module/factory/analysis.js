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
  var barChartMap = {};

  /********************************组件渲染*********************************/

  /********************************事件绑定*********************************/
  $('.j-time-type').on('click', function () {
    var jq = $(this);
    if (!jq.hasClass('layui-btn-primary')) {
      return;
    }
    $('.j-time-type').not(this).addClass('layui-btn-primary');
    jq.removeClass('layui-btn-primary');
    var type = jq.data('type');
    if (type !== 'X') {
      restful.get('auth/api/factory/rank', {timeType: type}, function (rank) {
        renderChart('statusGreen', '运行排行榜', rank['GREEN'], 'greenDuration');
        renderChart('statusRed', '故障排行榜', rank['RED'], 'redDuration');
        renderChart('statusYellow', '暂停排行榜', rank['YELLOW'], 'yellowDuration');
        renderChart('statusNone', '离线排行榜', rank['NONE'], 'noneDuration');
      });
    } else {

    }
  });

  function renderChart(elementId, title, list, key) {
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
      yAxis: {},
      series: [{
        name: '时长(秒)',
        type: 'bar',
        data: $.map(list, function (item) {
          return item[key];
        })
      }]
    };
    barChart.setOption(option);
  }
});