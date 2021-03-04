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
  var cascadeSelectObj = {
    'category': {
      'child': 'type'
      , 'parent': ['铣床类', '车床类', '磨床类', '机器人', '其它类', '自定义']
      , 'children': [
        ['立加', '卧加', '龙门', '镗床', '雕铣机'],
        ['平轨车床', '斜轨车床', '车铣复合', '立车', '走心机'],
        ['平面磨床', '外圆磨床', '内圆磨床', '珩磨机', '磨齿机'],
        [],
        ['慢走丝', '注塑机', '冲床', '折弯机', '激光切割机', '深孔钻']
      ]
    }
    , 'country': {
      'child': 'brand'
      , 'parent': ['日本', '台湾', '欧美', '韩国', '中国']
      , 'children': [
        ['牧野', 'mazak', '森精机', '西铁城', '津上', '三菱重工', 'OKUMA', '北村卧加', '北村车床', '丰田', '东芝', '沙迪克Sodick', '兄弟BROTHER', '松浦', '小松', 'NIGATA', 'SNK', 'OKK', '远洲'],
        ['友嘉', '台中', '大乔', '永进', '亚威', '东台', '程泰', '龙泽', '丽伟', '油机', '荣田', '福裕', '丽驰', '协鸿'],
        ['哈挺', 'MAG', 'DMG森精机', 'WFL', 'PUMA', 'EMAG埃马克', '斯兰福林', '巨浪', '舒特schuette', '赫克 Hurco'],
        ['斗山大宇', '起亚', 'UGNIT', 'STAR'],
        ['北京精雕', '宝鸡', '沈阳', '大连', '纽威', '海天', '台群', '润星', '嘉泰', '济南二机', '皖南', '秦川', '武重', '大连科德', '群志', '国盛', '北一', '新瑞']
      ]
    }
  };

  /********************************组件渲染*********************************/
  table.render({
    elem: '#tableList'
    , url: 'auth/api/factory/machines?'
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
      {field: 'ordering', width: 110, title: '本厂编号', sort: true}
      , {field: 'virtualName', title: '设备名称'}
      // , {field: 'producer', width: 200, title: '生产商'}
      , {field: 'operator', width: 150, title: '操作员'}
      , {field: 'triColorLEDSerialNumber', width: 150, title: '三色灯串号'}
      , {field: 'description', title: '零件名称'}
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

  form.on('select(cascade)', function (data) {
    var parent = $(data.elem).attr('id'), obj = cascadeSelectObj[parent], childJq = $('#' + obj.child).empty(),
        parentIndex = obj.parent.indexOf(data.value);
    if (parentIndex > -1 && parentIndex < obj.children.length) {
      $.each(obj.children[parentIndex], function (j, jItem) {
        $('<option value="' + jItem + '">' + jItem + '</option>').appendTo(childJq);
      });
      form.render('select');
    }
    dealUnknown(parent, data.value);
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
      renderSelectDom();
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
        renderSelectDom();
        break;
      case 'delete':
        deleteRow(row);
        break;
    }
  });

  function deleteRow(row) {
    feedback.confirm('确定删除?', function (index) {
      feedback.close(index);
      restful.delete('auth/api/factory/machines/' + row.data.id, null, function () {
        table.reload('tableList');
      });
    });
  }

  function renderSelectDom() {
    restful.get('auth/api/factory/machines/triColorLED', {}, function (data) {
      var selectJq = $('#triColorLEDId').empty();
      $('<option value="">未选择</option>').appendTo(selectJq);
      $.each(data, function (i, item) {
        // noinspection JSUnresolvedVariable
        $('<option value="' + item.id + '">' + item.serialNumber + '</option>').appendTo(selectJq);
      });
      if (currentRow) {
        // noinspection JSUnresolvedVariable
        if (currentRow.data.triColorLEDId) {
          // noinspection JSUnresolvedVariable
          $('<option value="' + currentRow.data.triColorLEDId + '">' + currentRow.data.triColorLEDSerialNumber + '</option>').appendTo(selectJq);
        }
        renderCascadeSelect('category', currentRow.data.category);
        renderCascadeSelect('country', currentRow.data.country);
        form.val('editForm', currentRow.data);
      } else {
        renderCascadeSelect('category');
        renderCascadeSelect('country');
        form.val('editForm', {});
      }
      $('section').toggleClass('layui-hide');
    });
  }

  function renderCascadeSelect(parent, parentValue) {
    var parentJq = $('#' + parent).empty(), obj = cascadeSelectObj[parent], parentValues = obj.parent,
        childValues = obj.children, childJq = $('#' + obj.child).empty();
    $.each(parentValues, function (i, item) {
      $('<option value="' + item + '"' + (i === 0 ? 'selected' : '') + '>' + item + '</option>').appendTo(parentJq);
      if (i < childValues.length && (parentValue === item || (parentValue === undefined && i === 0))) {
        $.each(childValues[i], function (j, jItem) {
          $('<option value="' + jItem + '">' + jItem + '</option>').appendTo(childJq);
        });
      }
    });
    dealUnknown(parent, parentValue);
  }

  function dealUnknown(parent, parentValue) {
    if (parent === 'category') {
      if (parentValue === '自定义') {
        $('.j-known-type').addClass('layui-hide');
        $('#unknownType').removeClass('layui-hide');
      } else {
        $('#unknown').val('');
        $('.j-known-type').removeClass('layui-hide');
        $('#unknownType').addClass('layui-hide');
      }
    }
  }
});