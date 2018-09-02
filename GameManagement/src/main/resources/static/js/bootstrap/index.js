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
        localStorage.setItem("username", data.field.username);
        $.post("./api/login", {
            "username": data.field.username,
            "password": data.field.password,
           /* "_csrf": $("meta[name='_csrf']").prop("content"),
            "_csrf_header": $("meta[name='_csrf_header']").prop("content")*/
        }, function (data) {
            if(utils.response.isError(data)) {
                layer.alert("您无权访问系统，请联系相关部门索取工号！");
                return;
            }
            if(data.role == "ADMIN") localStorage.setItem("dynamicNavigationUrl","/json/nav_main.json");
            if(data.role == "STAFF") localStorage.setItem("dynamicNavigationUrl","/json/nav_main_service.json");
            location.href = "/management/index";
        });
        return false;
    });

    // you code ...
})

$(function () {
    var username = localStorage.getItem("username");
    if(username != null){
        $("input[name='username']").val(username);
    }
})