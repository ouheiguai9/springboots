layui.define(['layer', 'table'], function (exports) {
  var layer = layui.layer;
  var table = layui.table;
  var $ = layui.$;
  var defaultOption = {
    width: '100%'
    , height: '100%'
    , multiple: false
    , title: '列表选择'
    , valueField: 'id'
    , showField: 'name'
    , limit: 30
    , fields: undefined
    , url: undefined
    , initShowValue: undefined
    , initRealValue: undefined
  };

  function TableSelect() {
  }

  TableSelect.prototype = {
    render: function (id, option) {
      option = $.extend(true, {}, defaultOption, option);
      var orgInput = $('#' + id).addClass('layui-hide')
          , tableId = id + '_select_table'
          , showJq = $('<div class="layui-fake-select" title="点击选择"/>')
          , that = this;
      this.resetValue(id, option.initRealValue, option.initShowValue);
      showJq.on('click', function () {
        layer.open({
          type: 1,
          title: option.title,
          anim: 1,
          area: [option.width, option.height],
          btn: ['确定', '取消'],
          content: '<div class="table-select-content"/>',
          success: function (layero, index) {
            var tableSelectContentJq = layero.find('div.table-select-content');
            $('<table />').attr('id', tableId).attr('lay-filter', tableId).appendTo(tableSelectContentJq);
            // noinspection JSValidateTypes
            table.render({
              elem: '#' + tableId
              , id: tableId
              , url: option.url
              , height: tableSelectContentJq.innerHeight() - 10
              , size: 'sm'
              , request: {
                pageName: 'page'
                , limitName: 'size'
              }
              , parseData: function (res) {
                // noinspection JSUnresolvedVariable
                return {
                  'code': 0,
                  'msg': '加载成功',
                  'count': res.totalElements,
                  'data': res.content
                };
              }
              , cols: [[
                {type: (option.multiple ? 'checkbox' : 'radio'), fixed: 'left'}
              ].concat(option.fields)]
              , autoSort: false
              , page: {
                curr: 0
                , first: 0
                , limit: option.limit
              }
            });
          },
          yes: function (index) {
            var checkRows = table.checkStatus(tableId).data;
            if (checkRows.length > 0) {
              that.resetValue(id, $.map(checkRows, function (item) {
                return item[option.valueField]
              }).join(','), $.map(checkRows, function (item) {
                return item[option.showField]
              }).join(','));
            }
            layer.close(index);
          }
        });
      }).insertAfter(orgInput);
    }
    , renderUser: function (id, option) {
      this.render(id, $.extend(true, {}, {
        title: '用户列表'
        , width: '600px'
        , height: '400px'
        , showField: 'nickname'
        , url: 'auth/api/users'
        , limit: 10
        , fields: [
          {field: 'username', width: 100, title: '用户名'}
          , {field: 'nickname', title: '名称'}
          , {field: 'phone', width: 150, title: '手机', align: 'center'}
        ]
      }, option));
    }
    , resetValue: function (id, realValue, showValue) {
      if (!realValue || !showValue) return;
      var values = realValue.split(','), names = showValue.split(',');
      var orgInput = $('#' + id), showJq = orgInput.siblings('div.layui-fake-select');
      var that = this;
      orgInput.val(realValue);
      showJq.empty();
      $.each(names, function (i, name) {
        var jq = $('<div class="select-item"/>').attr('value', values[i]);
        $('<span class="select-item-desc">').html(name).appendTo(jq);
        $('<span class="layui-icon layui-icon-close"/>').on('click', function (event) {
          event.stopPropagation();
          that.resetValue(id, $.map(jq.siblings(), function (item) {
            return $(item).attr('value');
          }).join(','), $.map(jq.siblings(), function (item) {
            return $(item).html();
          }).join(','));
          jq.remove();
        }).appendTo(jq);
        showJq.append(jq);
      });
    }
  };

  exports('tableSelect', new TableSelect());
});