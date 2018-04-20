$(document).ready(function () {
    $.ajax({
        url: "/anytec/getRegisterUrl",
        type: "post",
        dataType: "json",
        success: function (data) {
            if (data != null) {
                $("#register").attr("href", data.url);
            }
        }
    });
})
function showImageDetail(obj) {
    imgShow("#outerdiv", "#innerdiv", "#bigimg", obj);
}
//
//弹出显示图片大图
function imgShow(outerdiv, innerdiv, bigimg, obj) {
    var src = obj.src;//获取当前点击的pimg元素中的src属性
    $(bigimg).attr("src", src);//设置#bigimg元素的src属性
    $(bigimg).css("width", 600);//以最终的宽度对图片缩放
    $(innerdiv).css({"top": 100, "left": 400});//设置#innerdiv的top和left属性
    $(outerdiv).fadeIn("fast");//淡入显示#outerdiv及.pimg
    $(outerdiv).click(function () {//再次点击淡出消失弹出层
        $(this).fadeOut("fast");
    });
}
var websocket;
var repeatTimes=10;
var local_port=window.location.port
$(document).ready(function () {

    var pushContent = [];
    var currentIndex = 0;
    // if ('WebSocket' in window) {
    //
    //     // websocket = new WebSocket("ws://" + window.location.hostname + ":8090/webSocketServer");
    // } else if ('MozWebSocket' in window) {
    //     websocket = new MozWebSocket("ws://" + window.location.hostname + ":13319/webSocketServer");
    //     // websocket = new MozWebSocket("ws://" + window.location.hostname + ":8090/webSocketServer");
    // } else {
    //     // websocket = new SockJS("http://localhost:8090/sockjs/webSocketServer");
    //     websocket = new SockJS("http://localhost:13319/sockjs/webSocketServer");
    // }
    // function keepConnected() {
    //     websocket.send("hello");
    // }
    function timetrans(date){

        var date = new Date(date);//如果date为13位不需要乘1000
        var Y = date.getFullYear() + '-';
        var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
        var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
        var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
        var m = (date.getMinutes() <10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
        var s = (date.getSeconds() <10 ? '0' + date.getSeconds() : date.getSeconds());
        return Y+M+D+h+m+s;
    }
     function openFunc(evnt) {
         repeatTimes=15;
        console.log("WebSocket连接成功");
    };
     function onmessageFunc(evnt) {

        // $(".modal-backdrop").remove();
        var data = evnt.data;

        //results可以是一张图片中的多个人脸
        if (data.indexOf("results") != -1) {
            var rootJson = JSON.parse(data);

            var newContent = '';
            var showHtml = '';
            for (var i = 0; i < rootJson.results.length; i++) {
                var singleFace = rootJson.results[i];
                if (singleFace) {
                    // var face = "data:image;base64," + singleFace["face"];
                    var face = singleFace["face"];
                    var photo = singleFace["photo"];
                    // var matchFaceURL = singleFace.matchFace;
                    // var confidence = singleFace.confidence;
                    // var meta = singleFace.meta;
                    showHtml =
                        '<div class="col-xs-4 col-sm-4 img_height">' +
                            '<div class="col-xs-12">' +
                                '<div class="col-xs-4"><img class="col-xs-12 thumbnail" src="' + face + '"/><p>切出人脸图</p></div>' +
                                '<div class="col-xs-4"><img class="col-xs-12 thumbnail" src="' + singleFace["matchFace"] + '"/><p>库中图</p></div>' +
                                '<div class="col-xs-4"><img class="col-xs-12 thumbnail" onclick="showImageDetail(this)" src="' + photo + '"/><p>原图</p>' +
                                '</div>' +
                            '</div>' +
                            '<div class="col-xs-12">' +
                                '<p>相似度:' + singleFace["confidence"] + '</p>' +
                                '<p>姓名:' + singleFace["meta"] + '</p>' +
                                // '<p>时间:' + timetrans(parseInt(singleFace["timestamp"])) + '</p>' +
                                '<p>时间:' + singleFace["time"]+ '</p>' +
                                '<p>摄像头:' + singleFace["camera"] + '</p>' +
                            '</div>' +
                        '</div>'
                    if (pushContent.length < 9) {
                        pushContent[pushContent.length] = showHtml;
                    }else{
                        pushContent.shift();
                        pushContent.push(showHtml);
                    }

                }
            }
            var tmp=pushContent.slice(0);
            var reverse=tmp.reverse();
            $('#showIdentifyResults').html(reverse.join(''));

        }
    };
    function onerrorFunc() {
        console.log("WebSocket连接出错");
    };
    function oncloseFunc() {
        console.log("WebSocket连接关闭");
        initWebsocket();
    };
    function initWebsocket() {
        websocket = new WebSocket("ws://" + window.location.hostname + ":"+local_port+"/webSocketServer");
        // websocket = new WebSocket("ws://" + window.location.hostname + ":13319/webSocketServer");
        websocket.onopen =openFunc;
        websocket.onmessage =onmessageFunc;
        websocket.onerror=onerrorFunc;
        websocket.onclose=oncloseFunc;
    }
    initWebsocket()
    // setInterval(function () {
    //     websocket.send("hello")
    // },500000);
});
