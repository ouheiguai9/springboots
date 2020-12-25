;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
}).use(['element', 'util', 'restful', 'table', 'tree', 'laytpl'], function () {
  var form = layui.form;
  var element = layui.element;
  var util = layui.util;
  var $ = layui.$;
  var table = layui.table;
  var feedback = layui.feedback;
  var restful = layui.restful;
  var tree = layui.tree;
  var laytpl = layui.laytpl;
  var currentRow, menuData;

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
    , 'doAuthorizeSave': function () {
      var checkData = tree.getChecked('menuTree');
      restful.postForm('auth/api/roles/authorize', {
        id: currentRow.data.id
        , menuIdStr: getCheckedMenuId()
      }, function () {
        currentRow = undefined;
        feedback.closeAll();
        feedback.successMsg('修改成功');
      });
    }
    , 'doAuthorizeRest': function () {
      tree.reload('menuTree');
      tree.setChecked('menuTree', currentRow.data.idArr);
    }
    , 'doAuthorizeCancel': function () {
      currentRow = undefined;
      feedback.closeAll();
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
        updateLocked(row);
        break;
      case 'unlock':
        updateLocked(row);
        break;
      case 'authorize':
        currentRow = row;
        authorize(row);
        break;
    }
  });

  function updateLocked(row) {
    var obj = row.data;
    obj.locked = !obj.locked;
    restful.postForm('auth/api/roles/locked', obj, function (data) {
      row.update(data);
      row.tr.toggleClass('layui-disabled').find('.j-locked-btn').toggleClass('layui-hide');
    });
  }

  function authorize(row) {
    restful.get('auth/api/roles/menu/' + row.data.id, {}, function (idArr) {
      currentRow.data.idArr = idArr;
      if (!menuData) {
        restful.get('auth/api/menus', {}, function (data) {
          data.sort(function (a, b) {
            // noinspection JSUnresolvedVariable
            return b.ordering - a.ordering;
          });
          var treeData = new Array(data.length);
          $.each(data, function (i, item) {
            treeData[i] = menu2tree(item);
          });
          menuData = treeData;
          openAuthorizePanel(menuData, idArr);
        });
      } else {
        openAuthorizePanel(menuData, idArr);
      }
    });
  }

  function openAuthorizePanel(data, checkedMenuIdArr) {
    laytpl($('#menuContent').html()).render({}, function (html) {
      feedback.open({
        type: 1
        , title: false
        , area: ['300px', '100%']
        , offset: 'rt'
        , closeBtn: 0
        , anim: 1
        , content: html
        , success: function () {
          renderMenu('menuTree', checkedMenuIdArr, data);
        }
      });
    });
  }

  function renderMenu(id, checkedMenuIdArr, menuList) {
    tree.render({
      elem: '#' + id
      , data: menuList
      , showCheckbox: true
      , id: id
    });
    tree.setChecked(id, checkedMenuIdArr);
  }

  function menu2tree(menu) {
    var treeObj = {
      id: menu.id
      , title: menu.name
      , disabled: menu.locked
      , spread: !menu.locked
    };
    var children = menu.children;
    if (children.length > 0) {
      children.sort(function (a, b) {
        // noinspection JSUnresolvedVariable
        return b.ordering - a.ordering;
      });
      $.each(children, function (i, item) {
        children[i] = menu2tree(item);
      });
      treeObj.children = children;
    }
    return treeObj;
  }

  function getCheckedMenuId() {
    var checkedArray = tree.getChecked('menuTree');
    var idArray = [];
    while (checkedArray.length > 0) {
      var menu = checkedArray.pop();
      if (menu.children && menu.children.length > 0) {
        $.each(menu.children, function (i, item) {
          checkedArray.push(item);
        })
      } else {
        idArray.push(menu.id);
      }
    }
    return idArray.join(',');
  }
});