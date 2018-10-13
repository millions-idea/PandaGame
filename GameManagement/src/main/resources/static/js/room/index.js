/*!财务模块-会计账簿 韦德 2018年8月27日01:05:05*/
var route = "/management/room";
var service;
var tableIndex;
(function () {
    service = initService(route);

    // 加载数据表
    initDataTable(route + "/getGameRoomLimit", function (form, table, layer, vipTable, tableIns) {

    },function (table, res, curr, count) {
        // 监听工具条
        table.on('tool(my-data-table)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if(layEvent === 'del'){ //解散
                layer.confirm('您确定要解散此房间？此操作不会结算佣金！', {
                    btn: ['确定','取消'] //按钮
                }, function () {
                    service.disbandRoom({
                        roomCode: data.roomCode
                    }, function (data) {
                        if(utils.response.isErrorByCode(data)) return layer.msg("解散失败");
                        if(utils.response.isException(data)) return layer.msg(data.msg);
                        tableIndex.reload();
                        layer.msg("解散成功");
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
        disbandRoom: function (param, callback) {
            $.post(r + "/disbandRoom", param, function (data) {
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
        /*param += "&state=" + $tradeTypeInput.val();*/
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
        , {field: 'roomId', title: 'ID', width: 80, sort: true}
        , {field: 'roomCode', title: '频道号', width: 150}
        , {field: 'externalRoomId', title: '房间号', width: 150}
        , {field: 'parentAreaName', title: '游戏大区', width: 150}
        , {field: 'subareaName', title: '游戏分区', width: 150}
        , {field: 'status', title: '游戏状态', width: 120, sort:true,  templet: function (d) {
                var status = "未开始";
                switch (d.status){
                    case 1:
                        status = "已就绪";
                        break;
                    case 2:
                        status = "游戏中";
                        break;
                    case 3:
                        status = "已暂停";
                        break;
                    case 4:
                        status = "已结束";
                        break;
                    case 5:
                        status = "已结算";
                        break;
                    case 6:
                        status = "已失效";
                        break;
                }
                return status;
            }}
        , {field: 'isEnable', title: '房间状态', width: 150, templet: function (d) {
                return d.isEnable == 0 ? "已禁用" : "正常";
            }}
        , {field: 'addTime', title: '创建时间', width: 180, sort: true, templet: function (d) {
                return d.addTime == null ? '' : utils.date.timestampConvert(d.addTime);
            }}
        , {fixed: 'right',title: '操作', width: 120, align: 'center', templet: function (d) {
                return "<a name=\"item-edit\" class=\"layui-btn layui-btn layui-btn-xs layui-btn-danger\" lay-event=\"del\">强制解散</a>";
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