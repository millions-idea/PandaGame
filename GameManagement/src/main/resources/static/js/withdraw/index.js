/*!财务模块-会计账簿 韦德 2018年8月27日01:05:05*/
var route = "/management/withdraw";
var service;
var tableIndex;
(function () {
    service = initService(route);

    // 加载数据表
    initDataTable(route + "/getWithdrawLimit", function (form, table, layer, vipTable, tableIns) {
        // 动态注册事件getSign
        var $tableDelete = $("#my-data-table-delete"),
            $tableAdd = $("#my-data-table-add");
        $tableDelete.click(function () {
            layer.confirm('您确定要删除这些数据？', {
                title: "敏感操作提示",
                btn: ['确定','取消'],
                shade: 0.3,
                shadeClose: true
            },function () {
                var data = table.checkStatus('my-data-table').data;
                var idArr = new Array();
                data.forEach(function (value) {
                    idArr.push(value.business_id);
                });
                var param = {
                    id: idArr.join(",")
                };
                service.deleteBy(param, function (data) {
                    if(!isNaN(data.error) || data.code == 1){
                        layer.msg("删除失败");
                        return
                    }
                    layer.msg("删除成功");
                    tableIndex.reload();
                })
            })
        })
        $tableAdd.click(function () {
            service.getAddView(function (data) {
                layer.open({
                    type: 1,
                    skin: 'layui-layer-rim',
                    title: '添加',
                    area: ['420px', 'auto'],
                    shadeClose: true,
                    content: data
                });
            })
        })
    },function (table, res, curr, count) {
        // 监听工具条
        table.on('tool(my-data-table)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if(layEvent === 'ok'){ //审批通过
                var payId = $( obj.tr[2]).find("td:eq(1)").text();
                if(payId == null || payId.length == 0) return layer.msg("请先输入交易号!");
                service.confirm(data, function (data) {
                    if(utils.response.isErrorByCode(data)) return layer.msg("操作失败");
                    if(utils.response.isException(data)) return layer.msg(data.msg);
                    tableIndex.reload();
                    layer.msg("操作成功");
                })
            }else if(layEvent === 'pass'){ //拒绝
                var cause = $( obj.tr[2]).find("td:eq(0)").text();
                if(cause == null || cause.length == 0) {
                    layer.confirm('您还没有填写拒绝原因，是否取消此次操作？', {
                        btn: ['继续','取消'] //按钮
                    }, function(){
                        service.pass(data, function (data) {
                            if(utils.response.isErrorByCode(data)) return layer.msg("操作失败");
                            if(utils.response.isException(data)) return layer.msg(data.msg);
                            tableIndex.reload();
                            layer.msg("操作成功");
                        })
                    });
                }else{
                    service.pass(data, function (data) {
                        if(utils.response.isErrorByCode(data)) return layer.msg("操作失败");
                        if(utils.response.isException(data)) return layer.msg(data.msg);
                        tableIndex.reload();
                        layer.msg("操作成功");
                    })
                }
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
        /**
         * 审批通过 韦德 2018年8月30日14:58:13
         * @param param
         * @param callback
         */
        confirm: function (param, callback) {
            param.addTime = utils.date.timestampConvert(param.addTime);
            $.post(r + "/confirm", param, function (data) {
                callback(data);
            });
        },
        /**
         * 拒绝审批 韦德 2018年8月30日14:58:13
         * @param param
         * @param callback
         */
        pass: function (param, callback) {
            param.addTime = utils.date.timestampConvert(param.addTime);
            if(param.updateTime != null) param.updateTime = utils.date.timestampConvert(param.updateTime);
            $.post(r + "/pass", param, function (data) {
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
        , {field: 'withdrawId', title: '审批ID', width: 80, sort: true}
        , {field: 'systemRecordId', title: '流水号', width: 240, sort: true}
        , {field: 'phone', title: '手机号', width: 150}
        , {field: 'amount', title: '金额', width: 150, templet: function (d) {
                return "<span style='color: #c2330f;'>" + d.amount + "</span>";
            }}
        , {field: 'state', title: '状态', width: 120, sort:true,  templet: function (d) {
               var state = "待审核";
                if(d.state == 1){
                    state = "已通过";
               }else if(d.state == 2){
                    state = "已拒绝";
                }
                return state;
            }}
        , {field: 'addTime', title: '时间', width: 180, sort: true, templet: function (d) {
                return utils.date.timestampConvert(d.addTime);
            }}
        , {field: 'updateTime', title: '最后编辑时间', width: 180, sort: true, templet: function (d) {
                return d.updateTime == null ? '' : utils.date.timestampConvert(d.updateTime);
            }}
        , {fixed: 'right', field: 'remark', title: '摘要(可编辑)', edit:"text", width: 240}
        , {fixed: 'right', field: 'channelRecordId', title: '交易号(可编辑)', edit:"text", width: 240}
        , {fixed: 'right',title: '操作', width: 180, align: 'center', toolbar: '#barOption'}
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
