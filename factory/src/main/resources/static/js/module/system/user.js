;
layui.config({
  base: 'plugin/layui_exts/'
}).extend({
  restful: 'restful/index'
  , feedback: 'feedback/index'
  , formSelects: 'selects/formSelects-v4.min'
}).use(['element', 'util', 'restful', 'table', 'laydate', 'upload', 'formSelects'], function () {
  var form = layui.form;
  var util = layui.util;
  var $ = layui.$;
  var table = layui.table;
  var feedback = layui.feedback;
  var restful = layui.restful;
  var laydate = layui.laydate;
  var upload = layui.upload;
  var formSelects = layui.formSelects;
  var currentRow;

  /********************************组件渲染*********************************/
  $('#avatar').on('mouseenter', function () {
    if (this.value !== '') {
      var jq = $(this);
      var offset = jq.offset();
      var index = feedback.open({
        type: 1
        , title: false
        , closeBtn: 0
        , area: [100, 100]
        , shade: 0
        , offset: [offset.top - 105, offset.left + jq.outerWidth() - 100]
        , content: '<img src="' + this.value + '" style="height: 100px; width: 100px;">'
      });
      jq.one('mouseleave', function () {
        feedback.close(index);
      });
    }
  });
  formSelects.config('selectRole', {
    beforeSuccess: function (id, url, searchVal, result) {
      $.each(result, function (index, item) {
        item.value = item.id;
      });
      return result;
    }
  }).data('selectRole', 'server', {
    url: 'auth/api/roles/all'
  });
  //创建一个上传组件
  upload.render({
    elem: '#btnUpload'
    , url: 'upload'
    , headers: {'avatar': true}
    , done: function (res) { //上传后的回调
      // noinspection JSUnresolvedVariable
      $('#avatar').val(res.fileDownloadUri);
    }
  });
  laydate.render({
    elem: '#validPeriod'
    , type: 'datetime'
    , range: '至'
    , done: function (value) {
      var arr = value.split(' 至 ');
      $('#beginValidPeriod').val(arr[0]);
      $('#endValidPeriod').val(arr[1]);
    }
  });
  table.render({
    elem: '#tableList'
    , url: 'auth/api/users'
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
      {field: 'username', width: 100, title: '用户名', sort: true}
      , {field: 'nickname', title: '名称', sort: true}
      , {field: 'phone', width: 150, title: '手机', align: 'center', sort: true}
      , {field: 'createdDate', width: 200, title: '创建时间', align: 'center', sort: true}
      , {
        field: 'locked', width: 100, title: '状态', align: 'center', sort: true, templet: function (d) {
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
    obj.sex = ('true' === obj.sex);
    if ($('#username').length === 0) {
      restful.put('personalDetail', obj, function () {
        feedback.successMsg('修改成功');
      });
    } else if (currentRow) {
      restful.put('auth/api/users', obj, function () {
        currentRow.update(obj);
        feedback.successMsg('修改成功');
      });
    } else {
      restful.post('auth/api/users', obj, function () {
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
      $('#labelRole').css('height', '38px');
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
        formSelects.value('selectRole', row.data.roleIdStr.split(','));
        $('section').toggleClass('layui-hide');
        $('#labelRole').css('height', '38px');
        $('input[name="sex"]').prop('checked', function () {
          return (this.value === 'true' && row.data.sex) || (this.value === 'false' && !row.data.sex);
        });
        $('#password').val('');
        form.render('radio');
        break;
      case 'lock':
        updateLocked(row);
        break;
      case 'unlock':
        updateLocked(row);
        break;
      case 'authorize':
        feedback.open({
          content: $('#menuContent')
        });
        break;
    }
  });

  /********************************函数定义*********************************/
  form.verify({
    password: function (value, item) {
      if ($('#userId').val() !== '' && value === '') return;
      if (/^.{6,16}$/.test(value)) {
        var contain = 0;
        if (/[A-Z]/.test(value)) {
          contain += 1;
        }
        if (/[a-z]/.test(value)) {
          contain += 1;
        }
        if (/[0-9]/.test(value)) {
          contain += 1;
        }
        if (/[!@#$%^&*? ]/.test(value)) {
          contain += 1;
        }
        if (contain > 2) {
          return;
        }
      }
      return '长度6-16位(必须包含大小写字母、数字、特殊符号中任意三种)';
    }
    , nickname: [
      /^[\u2E80-\u9FFF]{2,10}$/
      , '2-10位汉字'
    ]
    , username: [
      /^[a-zA-Z]([a-zA-Z0-9]|[_]){5,16}$/
      , '以字母开始6-16位(字母/数字/下划线)'
    ]
  });

  function updateLocked(row) {
    var obj = row.data;
    obj.locked = !obj.locked;
    restful.postForm('auth/api/users/locked', obj, function (data) {
      row.update(data);
      row.tr.toggleClass('layui-disabled').find('.j-locked-btn').toggleClass('layui-hide');
    });
  }
});