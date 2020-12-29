;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
}).use(['element', 'util', 'restful', 'table', 'laydate'], function () {
  var form = layui.form;
  var element = layui.element;
  var util = layui.util;
  var $ = layui.$;
  var table = layui.table;
  var feedback = layui.feedback;
  var restful = layui.restful;
  var laydate = layui.laydate;
  var currentRow, tableRows, tableUrl = 'auth/api/factory/schedules';

  /********************************组件渲染*********************************/
  table.render({
    elem: '#tableList'
    , id: 'tableList'
    , url: tableUrl
    , parseData: function (rows) {
      $.each(rows, function (i, row) {
        row.rowNumber = (i + 1);
      });
      tableRows = rows;
      return {
        'code': 0
        , 'msg': '加载成功'
        , 'count': rows.length
        , 'data': tableRows
      };
    }
    , toolbar: '#tableListToolBar'
    , cols: [[
      {field: 'rowNumber', width: 50, title: '序号'}
      , {field: 'name', width: 200, title: '班次'}
      , {field: 'startTime', width: 200, title: '开始时间'}
      , {
        field: 'endTime', width: 200, title: '结束时间', templet: function (d) {
          return d.startTime > d.endTime ? ('次日' + d.endTime) : d.endTime;
        }
      }
      , {width: 250, title: '操作', fixed: 'right', align: 'center', toolbar: '#tableListRowToolBar'}
    ]]
  });

  /********************************事件绑定*********************************/
  form.on('submit(editForm)', function (params) {
    var obj = params.field;
    if (currentRow) {
      if (currentRow.data.id) {
        var next = $.extend(true, {}, currentRow.data, obj);
        restful.put('auth/api/factory/schedules', next, function (data) {
          currentRow.update(next);
          cancel(currentRow);
        });
      } else {
        restful.post('auth/api/factory/schedules', $.extend(true, {}, currentRow.data, obj), function () {
          table.reload('tableList', {url: tableUrl});
          currentRow = undefined;
        });
      }
    }
    return false;
  });

  //监听头部工具条
  table.on('toolbar(tableList)', function (obj) {
    if (obj.event === 'create') {
      addRow();
    }
  });

  //监听行工具条
  table.on('tool(tableList)', function (row) {
    switch (row.event) {
      case 'edit':
        edit(row);
        break;
      case 'save':
        $('#virtualBtn').click();
        break;
      case 'cancel':
        cancel(row);
        break;
      case 'delete':
        deleteRow(row);
        break;
    }
  });

  function addRow() {
    var rank = tableRows.length + 1;
    tableRows.push({
      rowNumber: rank
      , name: '第' + rank + '班'
      , startTime: ''
      , endTime: ''
    });
    table.reload('tableList', {data: tableRows, url: ''});
    $('.j-locked-btn').not('.layui-hide').last().click();
  }

  function edit(row) {
    if (currentRow) {
      feedback.layer.msg('请先保存正在修改的数据');
      return;
    }
    currentRow = row;
    row.tr.find('.j-locked-btn').toggleClass('layui-hide');
    var jqName = row.tr.find('td[data-field="name"]');
    var jqStartTime = row.tr.find('td[data-field="startTime"]');
    var jqEndTime = row.tr.find('td[data-field="endTime"]');
    jqName.find('div.layui-table-cell').toggleClass('layui-hide');
    jqStartTime.find('div.layui-table-cell').toggleClass('layui-hide');
    jqEndTime.find('div.layui-table-cell').toggleClass('layui-hide');
    jqName.append('<div class="row-edit-item"><input type="text" name="name" lay-verify="required" lay-verType="tips"></div>');
    jqStartTime.append('<div class="row-edit-item"><input type="text" id="startTime" name="startTime" lay-verify="required|scheduleTime" lay-verType="tips"></div>');
    jqEndTime.append('<div class="row-edit-item"><input type="text" id="endTime" name="endTime" lay-verify="required|scheduleTime" lay-verType="tips"></div>');
    form.val('editForm', row.data);
    laydate.render({
      elem: '#startTime'
      , type: 'time'
    });
    laydate.render({
      elem: '#endTime'
      , type: 'time'
    });
  }

  function cancel(row) {
    currentRow = undefined;
    if (!row.data.id) {
      tableRows.pop();
      row.del();
      return;
    }
    row.tr.find('.j-locked-btn').toggleClass('layui-hide');
    var jqName = row.tr.find('.row-edit-item').remove().end().find('div.layui-table-cell').removeClass('layui-hide');
  }

  function deleteRow(row) {
    feedback.confirm('确定删除?', function (index) {
      feedback.close(index);
      restful.delete('auth/api/factory/schedules/' + row.data.id, null, function () {
        table.reload('tableList', {url: tableUrl});
      });
    });
  }

  form.verify({
    scheduleTime: function (value, item) {
      if (item.name === 'endTime' && $('input[name="startTime"]').val() === value) {
        return '开始时间不能等于结束时间';
      }
      var current = currentRow.data;
      for (var i = 0; i < tableRows.length; i++) {
        var tmp = tableRows[i];
        if (current.id === tmp.id) continue;
        if (tmp.startTime < tmp.endTime) {
          if (value > tmp.startTime && value < tmp.endTime) return '与' + tmp.name + '时间存在交叉';
        } else {
          if (!(value >= tmp.endTime && value <= tmp.startTime)) return '与' + tmp.name + '时间存在交叉';
        }
      }
    }
  });
});