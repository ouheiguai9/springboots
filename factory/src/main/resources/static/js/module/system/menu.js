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
    , url: 'auth/api/menus'
    , parseData: function (menus) {
      var rows = [];
      var select = $('#parentId');
      $.each(menus, function (i, menu) {
        $('<option/>').attr('value', menu.id).html(menu.name).appendTo(select);
        rows = rows.concat(menu2rows(menu));
      });
      return {
        'code': 0, //解析接口状态
        'msg': '加载成功', //解析提示文本
        'count': menus.length, //解析数据长度
        'data': rows
      };
    }
    , cols: [[
      {field: 'name', title: '菜单名', templet: '#tableName'}
      , {field: 'code', width: 150, title: '编码'}
      , {field: 'createdDate', width: 180, title: '创建时间', align: 'center'}
      , {width: 80, title: '操作', fixed: 'right', align: 'center', toolbar: '#tableListRowToolBar'}
    ]]
    , toolbar: true
  });

  /********************************事件绑定*********************************/
  form.on('submit(editForm)', function (params) {
    var obj = params.field;
    if (currentRow) {
      restful.put('auth/api/menus', obj, function () {
        currentRow.update(obj);
        feedback.successMsg('修改成功');
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

  //监听行工具条
  table.on('tool(tableList)', function (row) {
    if (row.event === 'edit') {
      currentRow = row;
      form.val('editForm', row.data);
      $('#code').html(row.data.code);
      $('section').toggleClass('layui-hide');
    }
  });

  function menu2rows(menu, level) {
    var menus = [menu];
    level = !!level ? level : 0;
    menu.level = level;
    // noinspection JSUnresolvedVariable
    var children = menu.orderChildren;
    if (children.length > 0) {
      $.each(children, function (i, subMenu) {
        menus = menus.concat(menu2rows(subMenu, level + 1));
      })
    }
    return menus;
  }
});