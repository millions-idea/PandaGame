layui.use(['form', 'layer'], function () {

    // 操作对象
    var form = layui.form
        , layer = layui.layer
        , $ = layui.jquery;

    // 验证
    form.verify({
        username: function (value) {
            if (value == "") {
                return "请输入用户名";
            }
        },
        password: function (value) {
            if (value == "") {
                return "请输入密码";
            }
        }
    });

    // 提交监听
    form.on('submit(sub)', function (data) {
        $.post("./api/login", data.field, function (data) {
            if(utils.response.isError(data)) {
                layer.alert("您无权访问系统，请联系相关部门索取工号！");
                return;
            }
            location.href = "/management/index";
        });
        return false;
    });

    // you code ...
})