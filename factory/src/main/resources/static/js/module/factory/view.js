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
  var renderInterval;
  var currentTime = (new Date()).getTime();
  var barChart, pieChart, rectChart, lineChart;

  /********************************组件渲染*********************************/
  renderTable();

  /********************************事件绑定*********************************/
  function renderTable() {
    $('div.layui-card').toggleClass('layui-hide');
    $('#singleCard').empty();
    render();
    renderInterval = setInterval(render, 10 * 1000);
  }

  function render() {
    restful.disableLoading();
    restful.get('auth/api/factory/view', {initTime: currentTime}, function (view) {
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
      restful.enableLoading();
    });
  }

  function showSingleMachine(event) {
    var machine = event.data;
    clearInterval(renderInterval);

    $('div.layui-card').toggleClass('layui-hide');
    laytpl($('#singleTemplete').html()).render(machine, function (content) {
      var jq = $(content);
      $('#singleCard').append(jq);
    });
    $('#btnBack').on('click', renderTable);

    $('.j-time-type').on('click', function () {
      var jq = $(this);
      if (!jq.hasClass('layui-btn-primary')) {
        return;
      }
      $('.j-time-type').not(this).addClass('layui-btn-primary');
      jq.removeClass('layui-btn-primary');
      var type = jq.data('type');
      if (type !== 'X') {
        restful.get('auth/api/factory/singleView', {timeType: type, deviceId: machine['deviceId']}, function (view) {
          renderBarAndPie(view);
          renderRectChart(view);
          renderLineChart(view);
        });
      } else {

      }
    }).eq(0).click();
  }

  function renderBarAndPie(view) {
    var jq;
    if (!barChart) {
      jq = $('#barChart');
      jq.height(300);
      barChart = echarts.init(jq[0]);
    } else {
      barChart.clear();
    }
    barChart.setOption({
      title: {
        text: '状态时间',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      xAxis: {
        type: 'category',
        data: view['statusArray']
      },
      yAxis: {
        type: 'value'
      },
      series: [{
        name: '时长(分钟)',
        data: $.map(view['durationArray'], function (item) {
          return parseFloat((item / 60.0).toFixed(2));
        }),
        type: 'bar',
        itemStyle: {
          color: function (param) {
            var colorList = ['#FF5722', '#FFB800', '#5FB878', '#E2E2E2'];
            return colorList[param.dataIndex];
          }
        }
      }]
    });


    if (!pieChart) {
      jq = $('#pieChart');
      jq.height(300);
      pieChart = echarts.init(jq[0]);
    } else {
      pieChart.clear();
    }
    var pieData = [];
    $.each(view['statusArray'], function (i, item) {
      pieData.push({value: parseFloat((view['durationArray'][i] / 60.0).toFixed(2)), name: item});
    });
    pieChart.setOption({
      color: ['#FF5722', '#FFB800', '#5FB878', '#E2E2E2'],
      title: {
        text: '状态比例',
        left: 'center'
      },
      tooltip: {
        trigger: 'item',
        formatter: '{a} <br/>{b} : {c} 分钟'
      },
      legend: {
        orient: 'vertical',
        left: 'right'
      },
      series: [
        {
          name: '状态比例',
          type: 'pie',
          radius: '50%',
          data: pieData,
          label: {
            normal: {
              formatter: '{b}: {d}%',
              textStyle: {
                fontWeight: 'normal',
                fontSize: 15
              }
            }
          },
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }
      ]
    });
  }

  function renderRectChart(view) {
    if (!rectChart) {
      var jq = $('#rectChart');
      jq.height(300);
      rectChart = echarts.init(jq[0]);
    } else {
      rectChart.clear();
    }
    rectChart.setOption({});
  }

  function renderLineChart(view) {
    if (!lineChart) {
      var jq = $('#lineChart');
      jq.height(300);
      lineChart = echarts.init(jq[0]);
    } else {
      lineChart.clear();
    }

    lineChart.setOption({
      color: ['#FF5722', '#FFB800', '#5FB878', '#E2E2E2'],
      title: {
        text: '产品效率',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      legend: {
        data: view['statusArray']
      },
      grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
      },
      xAxis: {
        type: 'category',
        boundaryGap: false,
        data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          name: '邮件营销',
          type: 'line',
          stack: '总量',
          data: [120, 132, 101, 134, 90, 230, 210]
        },
        {
          name: '联盟广告',
          type: 'line',
          stack: '总量',
          data: [220, 182, 191, 234, 290, 330, 310]
        },
        {
          name: '视频广告',
          type: 'line',
          stack: '总量',
          data: [150, 232, 201, 154, 190, 330, 410]
        },
        {
          name: '直接访问',
          type: 'line',
          stack: '总量',
          data: [320, 332, 301, 334, 390, 330, 320]
        }
      ]
    });
  }
});