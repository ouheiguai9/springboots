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
    , url: 'auth/api/roles'
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
      , {field: 'name', width: 200, title: '角色名', sort: true}
      , {field: 'description', title: '描述'}
      , {field: 'createdDate', width: 200, title: '创建时间', align: 'center', sort: true}
      , {
        field: 'locked', width: 120, title: '状态', align: 'center', sort: true, templet: function (d) {
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
    if (currentRow) {
      restful.put('auth/api/roles', obj, function () {
        currentRow.update(obj);
        feedback.successMsg('修改成功');
      });
    } else {
      restful.post('auth/api/roles', obj, function () {
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
    }
  });

  //监听行工具条
  table.on('tool(tableList)', function (row) {
    switch (row.event) {
      case 'edit':
        currentRow = row;
        form.val('editForm', row.data);
        $('section').toggleClass('layui-hide');
        break;
      case 'lock':
        updateRoleLocked(row);
        break;
      case 'unlock':
        updateRoleLocked(row);
        break;
    }
  });

  function updateRoleLocked(row) {
    var obj = row.data;
    obj.locked = !obj.locked;
    restful.postForm('auth/api/roles/locked', obj, function (data) {
      row.update(data);
      row.tr.toggleClass('layui-disabled').find('.j-locked-btn').toggleClass('layui-hide');
    });
  }
});