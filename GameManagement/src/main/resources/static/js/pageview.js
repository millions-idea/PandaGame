Date.prototype.Format = function(fmt){
    var o = {
        "M+" : this.getMonth()+1,
        "d+" : this.getDate(),
        "h+" : this.getHours(),
        "m+" : this.getMinutes(),
        "s+" : this.getSeconds(),
        "q+" : Math.floor((this.getMonth()+3)/3),
        "S"  : this.getMilliseconds()
    };  
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

function getCookie(c_name){
    if (document.cookie.length>0){
        c_start=document.cookie.indexOf(c_name + "=");
        if (c_start!=-1){
            c_start=c_start + c_name.length+1;
            c_end=document.cookie.indexOf(";",c_start);
            if (c_end==-1) c_end=document.cookie.length;
            return unescape(document.cookie.substring(c_start,c_end));
        }   
    }   
    return ""; 
}

var xymj_uvid = getCookie('xymj_uvid');
if (!xymj_uvid) {
    var liveDate = new Date(); 
    liveDate.setTime(liveDate.getTime() + 365*24*60*60*1000); 
    xymj_uvid = new Date().Format("yyyyMMddhhmmssS") + Math.round(Math.random() * 1000);
    document.cookie = 'xymj_uvid=' + xymj_uvid + "; expires=" + liveDate.toGMTString();
}

var script = document.createElement("script");
script.type = 'text/javascript';
script.src = 'http://q.kkfun.com/uwsgi/pageview?url=' + escape(window.location.href) + '&xymj_uvid=' + xymj_uvid;
document.body.appendChild(script);

var index = window.location.href.indexOf('?') + 1;
var cid = 'no90000';
if (index > 0) {
    cid = window.location.href.slice(index);
}

var apkurl = document.getElementById("app11").href;
apkurl = apkurl.replace('http://zjhweb.kkfun.com.cn/static/', 'http://q.kkfun.com/uwsgi/');
apkurl = apkurl.replace('http://cdn.zjh.tisgame.com/', 'http://q.kkfun.com/uwsgi/');
document.getElementById("app11").href = apkurl + "?" + cid + "&xymj_uvid=" + xymj_uvid;
