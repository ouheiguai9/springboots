;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
}).use(['restful', 'laydate', 'laytpl'], function () {
  var $ = layui.$;
  var feedback = layui.feedback;
  var restful = layui.restful;
  var laydate = layui.laydate;
  var laytpl = layui.laytpl;
  var renderInterval;
  var barChart, pieChart, rectChart, lineChart;
  var pattern = 'yyyy-MM-dd hh:mm:ss', now, start, end;

  /********************************组件渲染*********************************/
  renderTable();

  /********************************事件绑定*********************************/
  Date.prototype.format = function (fmt) { //author: meizz
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

  function renderTable() {
    $('div.layui-card').toggleClass('layui-hide');
    $('#singleCard').empty();
    barChart = undefined;
    pieChart = undefined;
    rectChart = undefined;
    lineChart = undefined;
    render();
    renderInterval = setInterval(render, 10 * 1000);
  }

  function render() {
    restful.disableLoading();
    restful.get('auth/api/factory/view', {}, function (view) {
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
      $('#dateText').html('自定义');
      jq.removeClass('layui-btn-primary').siblings().addClass('layui-btn-primary');
      doQuery({timeType: jq.data('type'), deviceId: machine['deviceId']});
    }).eq(0).click();

    now = new Date();
    start = new Date(now.getTime() - 3600 * 24 * 1000).format(pattern);
    end = now.format(pattern);
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
            doQuery({timeType: 'X', start: start, end: end, deviceId: machine['deviceId']})
          }
        }
      });
      $('#datePicker').click();
    });
  }

  function doQuery(params) {
    restful.get('auth/api/factory/singleView', params, function (view) {
      renderBarAndPie(view);
      renderRectChart(view);
      renderLineChart(view);
    });
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
        name: '时长(分钟)',
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
    if (view['logs'].length === 0) return;
    var startTime = view['logs'][0]['value'][0];

    var colorList = ['#FF5722', '#FFB800', '#5FB878', '#E2E2E2'];
    var map = {};
    $.each(view['statusArray'], function (i, item) {
      map[item] = i;
    });
    $.each(view['logs'], function (i, item) {
      item.itemStyle = {
        normal: {
          color: colorList[map[item.name]]
        }
      };
    });
    rectChart.setOption({
      tooltip: {
        formatter: function (params) {
          return params.marker + params.name + ': ' + params.value[1] + ' s';
        }
      },
      title: {
        text: '时间轴(' + ((new Date(startTime * 1000)).format(pattern)) + '始)',
        left: 'center'
      },
      dataZoom: [{
        type: 'slider',
        filterMode: 'weakFilter',
        showDataShadow: false,
        top: 245,
        labelFormatter: ''
      }, {
        type: 'inside',
        filterMode: 'weakFilter'
      }],
      grid: {
        height: 140
      },
      xAxis: {
        min: startTime,
        scale: true,
        axisLabel: {
          formatter: function (val) {
            return Math.max(0, val - startTime) + ' s';
          }
        }
      },
      yAxis: {show: false},
      series: [{
        type: 'custom',
        renderItem: function (params, api) {
          var start = api.coord([api.value(0), 0]);
          var end = api.coord([api.value(0) + api.value(1), 0]);
          var height = api.size([0, 1])[1];
          var rectShape = echarts.graphic.clipRectByRect({
            x: start[0],
            y: start[1] - height,
            width: end[0] - start[0],
            height: height
          }, {
            x: params.coordSys.x,
            y: params.coordSys.y,
            width: params.coordSys.width,
            height: params.coordSys.height
          });
          return rectShape && {
            type: 'rect',
            transition: ['shape'],
            shape: rectShape,
            style: api.style()
          };
        },
        itemStyle: {
          opacity: 0.8
        },
        encode: {
          x: [0],
          y: -1
        },
        data: view['logs']
      }]
    });
  }

  function renderLineChart(view) {
    if (!lineChart) {
      var jq = $('#lineChart');
      jq.height(300);
      lineChart = echarts.init(jq[0]);
    } else {
      lineChart.clear();
    }
    if (view['redDurations'].length === 0) return;
    lineChart.setOption({
      color: ['#FF5722', '#FFB800', '#5FB878', '#E2E2E2'],
      title: {
        text: '产品效率',
        left: 'center'
      },
      tooltip: {
        trigger: 'axis'
      },
      dataZoom: [{
        type: 'slider',
        filterMode: 'weakFilter',
        showDataShadow: false,
        top: 250,
        labelFormatter: ''
      }, {
        type: 'inside',
        filterMode: 'weakFilter'
      }],
      grid: {
        left: '3%',
        right: '4%',
        bottom: 50,
        containLabel: true
      },
      xAxis: {
        type: 'category',
        scale: true,
        boundaryGap: false,
        data: $.map(view['redDurations'], function (item, i) {
          return '工件' + (i + 1);
        })
      },
      yAxis: {
        name: '时长(秒)',
        type: 'value'
      },
      series: [
        {
          name: '故障',
          type: 'line',
          data: view['redDurations']
        },
        {
          name: '暂停',
          type: 'line',
          data: view['yellowDurations']
        },
        {
          name: '运行',
          type: 'line',
          data: view['greenDurations']
        }
      ]
    });
  }
});