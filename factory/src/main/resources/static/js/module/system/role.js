;
layui.config({
    base: 'plugin/layui_exts/'
}).extend({
    restful: 'restful/index'
    , feedback: 'feedback/index'
}).use(['element', 'util', 'restful', 'table'], function () {
    var element = layui.element;
    var util = layui.util;
    var $ = layui.$;
    var table = layui.table;
    var feedback = layui.feedback;

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
            , {field: 'row', width: 80, title: '序号'}
            , {field: 'name', width: 200, title: '角色名', sort: true}
            , {field: 'description', title: '描述'}
            , {field: 'createdDate', width: 200, title: '创建时间', align: 'center', sort: true}
            , {width: 150, align: 'center', fixed: 'right', toolbar: '#tableListRowToolBar'}
        ]]
        , autoSort: false
        , page: {
            curr: 0
            , limit: 30
        }
    });

    /********************************事件绑定*********************************/
    table.on('sort(tableList)', function (obj) {
        //尽管我们的 table 自带排序功能，但并没有请求服务端。
        //有些时候，你可能需要根据当前排序的字段，重新向服务端发送请求，从而实现服务端排序，如：
        table.reload('idTest', {
            initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。
            , where: { //请求参数（注意：这里面的参数可任意定义，并非下面固定的格式）
                sort: obj.field + ',' + obj.type //排序字段
            }
        });
    });
});