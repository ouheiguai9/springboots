;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
}).use(['element', 'util', 'restful', 'table'], function () {
  var form = layui.form;
  var element = layui.element;
  var util = layui.util;
  var $ = layui.$;
  var table = layui.table;
  var feedback = layui.feedback;
  var restful = layui.restful;
  var currentRow;

  /********************************组件渲染*********************************/
  table.render({
    elem: '#tableList'
    , url: 'auth/api/factory/machines'
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
      {type: 'checkbox', fixed: 'left'}
      , {field: 'name', width: 200, title: '设备名称'}
      , {field: 'type', width: 200, title: '设备类型'}
      , {field: 'producer', width: 200, title: '生产商'}
      , {field: 'operator', width: 150, title: '操作员'}
      , {field: 'triColorLEDSerialNumber', width: 150, title: '三色灯串号'}
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
    if (obj.triColorLEDId) {
      obj.triColorLED = {id: obj.triColorLEDId};
    }
    if (currentRow) {
      restful.put('auth/api/factory/machines', obj, function (data) {
        currentRow.update(data);
        feedback.successMsg('修改成功');
      });
    } else {
      restful.post('auth/api/factory/machines', obj, function () {
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
      renderLedSelect();
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
        renderLedSelect();
        break;
      case 'delete':
        deleteRow(row);
        break;
    }
  });

  function renderLedSelect() {
    restful.get('auth/api/factory/machines/triColorLED', {}, function (data) {
      var selectJq = $('#triColorLEDId').empty();
      $.each(data, function (i, item) {
        $('<option value="' + item.id + '">' + item.serialNumber + '</option>').appendTo(selectJq);
      });
      if (currentRow) {
        // noinspection JSUnresolvedVariable
        if(currentRow.data.triColorLEDId) {
          // noinspection JSUnresolvedVariable
          $('<option value="' + currentRow.data.triColorLEDId + '">' + currentRow.data.triColorLEDSerialNumber + '</option>').appendTo(selectJq);
        }
        console.info(currentRow.data);
        form.val('editForm', currentRow.data);
      } else {
        form.val('editForm', {});
      }
      $('section').toggleClass('layui-hide');
    });
  }

  function deleteRow(row) {
    feedback.confirm('确定删除?', function (index) {
      feedback.close(index);
      restful.delete('auth/api/factory/machines/' + row.data.id, null, function () {
        table.reload('tableList');
      });
    });
  }
});