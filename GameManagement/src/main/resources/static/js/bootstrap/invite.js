$(function () {
    // 注册
    $("#registerButton").click(function(){
        var $phone = $("#phone"),
            $password = $("#password"),
            $password2 = $("#password2"),
            $code = $("#code");

        // 校验手机号和密码
        if($phone.val().length < 11) return layer.open({
            content: '手机号最短需要11位数字'
            ,btn: '我知道了'
        });

        if($phone.val().length > 11) return layer.open({
            content: '手机号最长需要11位数字'
            ,btn: '我知道了'
        });

        if($password.val().length < 6) return layer.open({
            content: '密码最短需要6位字符'
            ,btn: '我知道了'
        });

        if($password.val().length > 32) return layer.open({
            content: '密码最长需要32位字符'
            ,btn: '我知道了'
        });

        if($password.val() != $password2.val()) return layer.open({
            content: '两次密码不一致'
            ,btn: '我知道了'
        });

        $.post("/api/bootstrap/register",{
            phone: $phone.val(),
            password: $password.val(),
            password2: $password2.val(),
            parentId: $code.val()
        }, function (data) {
            if(data === null || data.code != 200) return layer.open({
                content: data.msg
                ,skin: 'msg'
                ,time: 2 //2秒后自动关闭
            });
            layer.open({
                content: '恭喜您注册成功，现在去下载APP使用吧~'
                ,btn: '去下载'
                ,yes: function () {
                    location.href = "http://ad.kdxny74.cn/";
                }
            });
        });


    })
})