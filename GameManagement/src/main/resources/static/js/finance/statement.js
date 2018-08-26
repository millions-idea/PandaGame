/*!财务模块-交易清单 韦德 2018年8月5日22:50:57*/
var route = "./statement";
var service;
var tableIndex;
(function () {
    service = initService(route);

    // 获取系统账户信息
    service.getSystemAccount(function (data) {
        console.log(data)
        if(!isNaN(data.error) || (!isNaN(data.code) && data.code != 0)) return;
        $("#sys_username").text(data.username);
        $("#sys_balance").text(data.balance);
        $("#income-amount").text(data.incomeAmount);
        $("#expend-amount").text(data.expendAmount);
    });

    // 加载数据表
    initDataTable(route + "/getLimit", function (form, table, layer, vipTable, tableIns) {
        // 动态注册事件
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

            if(layEvent === 'detail'){ //查看
                service.view(data,function (html) {
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-rim',
                        title: '预览',
                        area: ['420px', 'auto'],
                        shadeClose:true,
                        content: html
                    });
                })
            } else if(layEvent === 'del'){ //删除
                layer.confirm('确定要删除此项吗？', function(index){
                    var param = {
                        id: obj.data.goods_id.toString()
                    };
                    service.deleteBy(param, function (data) {
                        if(!isNaN(data.error) || data.code == 1){
                            layer.msg("删除失败");
                            return
                        }
                        layer.msg("删除成功");
                        obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                        layer.close(index);
                    })
                });
            } else if(layEvent === 'edit'){ //编辑
                service.getEditView(data, function (html) {
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-rim',
                        title: '编辑',
                        area: ['420px', 'auto'],
                        shadeClose:true,
                        content: html
                    });
                });
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
         * 获取系统账户信息 2018年8月7日00:23:26
         * @param callback
         */
        getSystemAccount: function (callback) {
            $.get("/finance/getSystemAccount",function (data) {
                callback(data);
            })
        },
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

        var param =  "?condition=" + $queryCondition.val();
        param += "&trade_type=" + $tradeTypeInput.val();
        param += "&trade_date_begin=" + $tradeDateBeginInput.val();
        param += "&trade_date_end=" + $tradeDateEndInput.val();

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
            , height: 480    //容器高度
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
        {type: "numbers"}
        , {field: 'transaction_id', title: 'ID', width: 80, sort: true}
        , {field: 'record_id', title: '流水号', width: 300}
        , {field: 'record_no', title: '交易号', width: 200, templet: function (d) {
                return d.record_no == null ? "站内交易" : d.record_no;
            }}
        , {field: 'from_username', title: '甲方', width: 150}
        , {field: 'to_username', title: '乙方', width: 150}
        , {field: 'trade_date', title: '交易日', width: 240}
        , {field: 'trade_type', title: '交易类型', width: 120, templet: function (d) {
                return d.trade_type == 1 ? "收入" : "支出";
            }}
        , {field: 'trade_amount', title: '金额', width: 120, align: "center", templet: function (d) {
                if(d.trade_type == 1){
                    return "<span style='color: #2fc253;font-size: 15px;'>+" + d.trade_amount + "</span>";
                }else{
                    return "<span style='color: #c2330f;font-size: 15px;'>-" + d.trade_amount + "</span>";
                }
                return "<span>" + d.trade_amount + "</span>";
            }}
        , {field: 'remark', title: '摘要', width: 240}
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
        , height: 480    //容器高度
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
        $(o).height(395);
    });
}
