/*!财务模块-会计账簿 韦德 2018年8月27日01:05:05*/
var route = "/management/area";
var service;
var tableIndex;
(function () {
    service = initService(route);

    // 加载数据表
    initDataTable(route + "/getAreaLimit", function (form, table, layer, vipTable, tableIns) {

    },function (table, res, curr, count) {
        // 监听工具条
        table.on('tool(my-data-table)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if(layEvent === 'open'){ //开放
                layer.confirm('您确定开放此分区？', {
                    btn: ['继续','取消'] //按钮
                }, function(){
                    data.isEnable = 1;
                    service.update(data, function (data) {
                        if(utils.response.isErrorByCode(data)) return layer.msg("操作失败");
                        if(utils.response.isException(data)) return layer.msg(data.msg);
                        tableIndex.reload();
                        layer.msg("操作成功");
                    })
                });
            }else if(layEvent === 'close'){ //关闭
                layer.confirm('您确定关闭此分区？', {
                    btn: ['继续','取消'] //按钮
                }, function(){
                    data.isEnable = 0;
                    service.update(data, function (data) {
                        if(utils.response.isErrorByCode(data)) return layer.msg("操作失败");
                        if(utils.response.isException(data)) return layer.msg(data.msg);
                        tableIndex.reload();
                        layer.msg("操作成功");
                    })
                });
            }else if(layEvent === 'save'){ //保存
                service.update(data, function (data) {
                    if(utils.response.isErrorByCode(data)) return layer.msg("操作失败");
                    if(utils.response.isException(data)) return layer.msg(data.msg);
                    tableIndex.reload();
                    layer.msg("操作成功");
                })
            }
        });
    });
})()

/**
 * 加载模块
 * @param r
 * @returns
 */
function initService(r) {
    return {
        update: function (param, callback) {
            param.addTime = utils.date.timestampConvert(param.addTime);
            $.post(r + "/update", param, function (data) {
                callback(data);
            });
        }
    }
}

/**
 * 加载数据表
 * @param url
 * @param callback
 * @param loadDone
 */
function initDataTable(url, callback, loadDone) {
    var $queryButton = $("#my-data-table-query"),
        $queryCondition = $("#my-data-table-condition"),
        $tradeTypeInput = $("select[name='trade_type']"),
        $tradeDateBeginInput = $("input[name='trade_date_begin']"),
        $tradeDateEndInput = $("input[name='trade_date_end']");

    var cols = getTableColumns();

    // 注册查询事件
    $queryButton.click(function () {
        $queryButton.attr("disabled",true);
        var condition = $queryCondition.val();
        if(condition.indexOf("+") != -1) condition = condition.replace("+", "[add]");
        if(condition.indexOf("-") != -1) condition = condition.replace("-", "[reduce]");
        var param =  "?condition=" + encodeURI(condition);
        param += "&state=" + $tradeTypeInput.val();
        param += "&beginTime=" + $tradeDateBeginInput.val();
        param += "&endTime=" + $tradeDateEndInput.val();

        loadTable(tableIndex,"my-data-table", "#my-data-table", cols, url + param, function (res, curr, count) {
            $queryButton.removeAttr("disabled");
        });
    })

    layui.use(['table', 'form', 'layer', 'vip_table', 'layedit', 'tree','element'], function () {
        // 操作对象
        var form = layui.form
            , table = layui.table
            , layer = layui.layer
            , vipTable = layui.vip_table
            , $ = layui.jquery
            , layedit = layui.layedit
            , element = layui.element;

        // 表格渲染
        tableIndex = table.render({
            elem: '#my-data-table'                  //指定原始表格元素选择器（推荐id选择器）
            , height: 720    //容器高度
            , cols: cols
            , id: 'my-data-table'
            , url: url
            , method: 'get'
            , page: true
            , limits: [30, 60, 90, 150, 300]
            , limit: 30 //默认采用30
            , loading: true
            , even: true
            , done: function (res, curr, count) {
                loadDone(table, res, curr, count);
            }
        });

        // 刷新
        $('#btn-refresh-my-data-table').on('click', function () {
            tableIndex.reload();
        });

        // you code ...
        callback(form, table, layer, vipTable, tableIndex);
    });
}

/**
 * 获取表格列属性
 * @returns {*[]}
 */
function getTableColumns() {
    return [[
        {type: "numbers", fixed: 'left'}
        , {field: 'subareaId', title: 'ID', width: 80, sort: true}
        , {field: 'name', title: '分区名称', width: 240, sort: true}
        , {field: 'isEnable', title: '状态', width: 80, sort: true, templet: function (d) {
                if(d.isEnable == 1){
                    return "<span style='color: #33CC33'>开放中</span>";
                }else{
                    return "<span style='color: #FF3300'>已关闭</span>";
                }
            }}
        , {field: 'isRelation', title: '分区类型', width: 150, templet: function (d) {
                if(d.isRelation == 0){
                    return "分区";
                }else{
                    return "大区";
                }
            }}
        , {field: 'backgroundImage', title: '广告背景', edit:"text", width: 240}
        , {field: 'price', title: '房间定价', edit:"numbers", width: 240}
        , {field: 'maxPersonCount', title: '人数限制', edit:"numbers", width: 240}
        , {field: 'limitPrice', title: '收费标准', edit:"numbers", width: 240}
        , {field: 'reducePrice', title: '实际扣费', edit:"numbers", width: 240}
        , {fixed: 'right',title: '操作', width: 180, align: 'center', templet: function (d) {
                if(d.isEnable == 0){
                    var html  = " <a name=\"item-view\" class=\"layui-btn layui-btn layui-btn-sm\" lay-event=\"open\">开放分区</a>";
                    html += " <a name=\"item-view\" class=\"layui-btn layui-btn layui-btn-sm layui-btn-primary\" lay-event=\"save\">保存修改</a>";
                    return html;
                }else {
                    var html = " <a name=\"item-view\" class=\"layui-btn layui-btn layui-btn-sm\" lay-event=\"close\">关闭分区</a>";;
                    html += " <a name=\"item-view\" class=\"layui-btn layui-btn layui-btn-sm layui-btn-primary\" lay-event=\"save\">保存修改</a>";
                    return html;
                }
            }}
    ]];
}

/**
 * 加载表格数据
 * @param tableIns
 * @param id
 * @param elem
 * @param cols
 * @param url
 * @param loadDone
 */
function loadTable(index,id,elem,cols,url,loadDone) {
    index.reload({
        elem: elem
        , height: 720    //容器高度
        , cols: cols
        , id: id
        , url: url
        , method: 'get'
        , page: true
        , limits: [30, 60, 90, 150, 300]
        , limit: 30 //默认采用30
        , loading: true
        , even: true
        , done: function (res, curr, count) {
            resetPager();
            loadDone(res, curr, count);
        }
    });
}

function resetPager() {
    $(".layui-table-body.layui-table-main").each(function (i, o) {
        $(o).height(640);
    });
}
