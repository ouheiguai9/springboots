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
  var currentRow, tableRows, tableUrl = 'auth/api/factory/workshops';

  /********************************组件渲染*********************************/
  table.render({
    elem: '#tableList'
    , id: 'tableList'
    , url: tableUrl
    , parseData: function (rows) {
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
      {type: 'numbers'}
      , {field: 'name', width: 250, title: '名称'}
      , {field: 'description', title: '描述'}
      , {width: 250, title: '操作', fixed: 'right', align: 'center', toolbar: '#tableListRowToolBar'}
    ]]
  });

  /********************************事件绑定*********************************/
  form.on('submit(editForm)', function (params) {
    var obj = params.field;
    if (currentRow) {
      if (currentRow.data.id) {
        var next = $.extend(true, {}, currentRow.data, obj);
        restful.put('auth/api/factory/workshops', next, function (data) {
          currentRow.update(next);
          cancel(currentRow);
        });
      } else {
        restful.post('auth/api/factory/workshops', $.extend(true, {}, currentRow.data, obj), function () {
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
    tableRows.push({
      name: '第' + (tableRows.length + 1) + '车间'
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
    var jqDescription = row.tr.find('td[data-field="description"]');
    var jqNameInput = $('<div class="row-edit-item"></div>').width(jqName.innerWidth());
    var jqDescriptionInput = $('<div class="row-edit-item"></div>').width(jqDescription.innerWidth());
    jqName.find('div.layui-table-cell').toggleClass('layui-hide');
    jqDescription.find('div.layui-table-cell').toggleClass('layui-hide');
    jqNameInput.append('<input type="text" name="name" lay-verify="required" lay-verType="tips">');
    jqDescriptionInput.append('<input type="text" name="description">');
    jqName.append(jqNameInput);
    jqDescription.append(jqDescriptionInput);
    form.val('editForm', row.data);
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
      restful.delete('auth/api/factory/workshops/' + row.data.id, null, function () {
        table.reload('tableList', {url: tableUrl});
      });
    });
  }
});