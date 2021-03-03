;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
  , tableSelect: 'tableSelect/index'
}).use(['element', 'util', 'restful', 'tableSelect'], function () {
  var form = layui.form;
  var element = layui.element;
  var util = layui.util;
  var $ = layui.$;
  var table = layui.table;
  var feedback = layui.feedback;
  var restful = layui.restful;
  var tableSelect = layui.tableSelect;
  var currentRow;

  /********************************组件渲染*********************************/
  tableSelect.renderUser('consumerId');
  table.render({
    elem: '#tableList'
    , url: 'auth/api/devices'
    , request: {
      pageName: 'page' //页码的参数名称，默认：page
      , limitName: 'size' //每页数据量的参数名，默认：limit
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
      , {
        field: 'type', width: 110, title: '设备类型', sort: true, templet: function (d) {
          switch (d.type) {
            case 'TriColorLed':
              return '三色灯';
            case 'RemoteUSB':
              return '远程硬盘';
          }
          return d.type;
        }
      }
      , {field: 'serialNumber', width: 150, title: '主串号'}
      // , {field: 'serialNumber1', width: 200, title: '辅助串号1'}
      // , {field: 'serialNumber2', width: 200, title: '辅助串号2'}
      , {field: 'producer', title: '生产商'}
      , {field: 'consumerName', title: '收货方'}
      , {field: 'createdDate', width: 160, title: '创建时间', align: 'center', sort: true}
      , {
        field: 'locked', width: 90, title: '状态', align: 'center', sort: true, templet: function (d) {
          return d.locked ? '不可用' : '可用';
        }
      }
      , {width: 150, title: '操作', fixed: 'right', align: 'center', toolbar: '#tableListRowToolBar'}
    ]]
    , autoSort: false
    , page: {
      curr: 0
      , first: 0
      , limit: 30
    }
    , done: function () {
      $('.j-row-disable').parents('tr').addClass('layui-disabled');
    }
  });

  /********************************事件绑定*********************************/
  form.on('submit(editForm)', function (params) {
    var obj = params.field;
    // noinspection JSUnresolvedVariable
    if (obj.consumerId) {
      obj.consumer = {id: obj.consumerId};
    }
    if (currentRow) {
      // noinspection JSUnresolvedVariable
      if (currentRow.data.consumerId !== '' && currentRow.data.consumerId !== obj.consumerId) {
        feedback.confirm('确定变更收货方?', function (index) {
          restful.put('auth/api/devices', obj, function (data) {
            currentRow.update(data);
            feedback.successMsg('修改成功');
          });
          feedback.close(index);
        });
      } else {
        restful.put('auth/api/devices', obj, function (data) {
          currentRow.update(data);
          feedback.successMsg('修改成功');
        });
      }
    } else {
      restful.post('auth/api/devices', obj, function () {
        feedback.successMsg('添加成功', function () {
          location.reload();
        });
      });
    }
    return false;
  });

  util.event('lay-event', {
    'back': function () {
      currentRow = undefined;
      $('#btnRest').click();
      $('section').toggleClass('layui-hide');
    }
  });

  table.on('sort(tableList)', function (obj) {
    table.reload('tableList', {
      initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。
      , where: { //请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
        sort: obj.field + ',' + obj.type //排序字段
      }
    });
  });

  //监听头部工具条
  table.on('toolbar(tableList)', function (obj) {
    if (obj.event === 'create') {
      $('section').toggleClass('layui-hide');
    } else if (obj.event === 'search') {
      table.reload('tableList', {
        where: {
          search: $('#search').val()
        }
      });
    }
  });

  //监听行工具条
  table.on('tool(tableList)', function (row) {
    switch (row.event) {
      case 'edit':
        currentRow = row;
        form.val('editForm', row.data);
        // noinspection JSUnresolvedVariable
        if (row.data.consumerId) {
          // noinspection JSUnresolvedVariable
          tableSelect.resetValue('consumerId', row.data.consumerId, row.data.consumerName);
        }
        $('section').toggleClass('layui-hide');
        break;
      case 'lock':
        updateLocked(row);
        break;
      case 'unlock':
        updateLocked(row);
        break;
    }
  });

  function updateLocked(row) {
    var obj = row.data;
    obj.locked = !obj.locked;
    restful.postForm('auth/api/devices/locked', obj, function (data) {
      row.update(data);
      row.tr.toggleClass('layui-disabled').find('.j-locked-btn').toggleClass('layui-hide');
    });
  }
});