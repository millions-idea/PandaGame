/*!财务模块-会计账簿 韦德 2018年8月27日01:05:05*/
var route = "./";
var service;
var tableIndex;
(function () {
    service = initService(route);

    // 加载数据表
    initDataTable(route + "getSettlementLimit", function (form, table, layer, vipTable, tableIns) {
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
            if(layEvent === 'edit'){ //编辑
                service.getEditView(data, function (html) {
                    layer.open({
                        type: 1,
                        skin: 'layui-layer-rim',
                        title: '编辑',
                        area: ['auto', 'auto'],
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
         * 加载编辑页面 韦德 2018年8月29日16:11:37
         * @param param
         * @param callback
         */
        getEditView: function (param, callback) {
            param.addTime = utils.date.timestampConvert(param.addTime);
            param.updateTime = utils.date.timestampConvert(param.updateTime);
            $.get(r + "edit-settlement", param, function (data) {
                callback(data);
            });
        },
        getRoomMemberList: function (param, callback) {
            $.get(r + "getRoomMemberList", param, function (data) {
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
        {type: "numbers"}
        , {field: 'settlementId', title: 'ID', width: 80, sort: true}
        , {field: 'roomCode', title: '房间号', width: 150}
        , {field: 'roomName', title: '房间名称', width: 150}
        , {field: 'externalRoomId', title: '外部房间号', width: 150}
        , {field: 'status', title: '游戏状态', width: 150, templet: function (d) {
            //状态(0=未开始,1=已就绪,2=已开始,3=已暂停,4=已结束,5=已结算,6=已失效)
                var state = "未开始";
                if(d.status == 1){
                    state = "已就绪";
                }else if(d.status == 2){
                    state = "已开始";
                }else if(d.status == 3){
                    state = "已暂停";
                }else if(d.status == 4){
                    state = "已结束";
                }else if(d.status == 5){
                    state = "已结算";
                }else if(d.status == 6){
                    state = "已失效";
                }
                return state;
            }}
        , {field: 'countGrade', title: '总成绩', width: 150, templet: function (d) {
                var str = "";
                if(d.countGrade == 0){
                    str += "<span class='layui-badge layui-bg-green'>正常</span>";
                }else {
                    str += "<span class='layui-badge'>有误差</span>";
                }
                return str;
            }}
        , {field: 'state', title: '状态', width: 120, templet: function (d) {
               var state = "待结算";
                if(d.state == 1){
                    state = "已结算";
               }else if(d.state == 2){
                    state = "已拒绝";
                }
                return state;
            }}
        , {field: 'add_time', title: '时间', width: 240, templet: function (d) {
                return utils.date.timestampConvert(d.addTime);
            }}
        , {fixed: 'right', title: '操作', width: 80, align: 'center', toolbar: '#barOption'}
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
