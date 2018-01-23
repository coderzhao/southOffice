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
    function keepConnected() {
        websocket.send("hello");
    }

     function openFunc(evnt) {
         repeatTimes=15;
        console.log("WebSocket连接成功");
    };
     function onmessageFunc(evnt) {

        // $(".modal-backdrop").remove();
        var data = evnt.data;

        if (data.indexOf("results") != -1) {
            var rootJson = JSON.parse(data);

            // $("#photo").attr("src",photo);
            // var prehtml = $('#showIdentifyResults').html()
            // for (var j=0;i<pushContent.length;j++){
            //     if($('#myModal'+j)){
            //         $('#myModal'+j).modal("hide")
            //     }
            // }
            var newContent = '';
            var showHtml = '';
            for (var i = 0; i < rootJson.results.length; i++) {
                var singleFace = rootJson.results[i];
                if (singleFace) {
                    var face = "data:image;base64," + singleFace["face"];
                    // var photo = "data:image;base64," + singleFace["photo"];
                    var photo =singleFace["photo"];
                    // var matchFaceURL = singleFace.matchFace;
                    // var confidence = singleFace.confidence;
                    // var meta = singleFace.meta;
                    showHtml =
                        '<div class="col-xs-4 col-sm-4">' +
                            '<div class="col-xs-12">' +
                                '<div class="col-xs-4"><img class="col-xs-12 thumbnail" src="' + face + '"/><p>切出人脸图</p></div>' +
                                '<div class="col-xs-4"><img class="col-xs-12 thumbnail" src="' + singleFace["matchFace"] + '"/><p>库中图</p></div>' +
                                '<div class="col-xs-4"><img class="col-xs-12 thumbnail" onclick="showImageDetail(this)" src="' + photo + '"/><p>原图</p>' +
                                '</div>' +
                            '</div>' +
                            '<div class="col-xs-12">' +
                                '<p>confidence:' + singleFace["confidence"] + '</p>' +
                                '<p>姓名:' + singleFace["meta"] + '</p>' +
                                '<p>时间:' + singleFace["timestamp"] + '</p>' +
                                '<p>摄像头:' + singleFace["camera"] + '</p>' +
                            '</div>' +
                        '</div>'
                    // newContent = showHtml + newContent
                    // if (currentIndex <= 17) {
                    //     pushContent[currentIndex] = showHtml;
                    //     currentIndex++;
                    //     continue;
                    // }else{
                    //     currentIndex[17]=
                    // }
                    if (pushContent.length <= 9) {
                        pushContent[pushContent.length] = showHtml;
                    }else{
                        pushContent.shift();
                        pushContent.push(showHtml);
                    }

                }
                // var demo = $('#showIdentifyResults').html()+showHtml;
            }
            var tmp=pushContent.slice(0,-1);
            var reverse=tmp.reverse();
            $('#showIdentifyResults').html(reverse.join(''));

        }
    };
    function onerrorFunc(evnt) {
        console.log("WebSocket连接出错");
    };
    function oncloseFunc(evnt) {
        console.log("WebSocket连接关闭");
        if(repeatTimes==0){
            console.log("repeat times over")
            return false;
        }
        repeatTimes=repeatTimes-1;
        initWebsocket();
    };
    function initWebsocket() {
        // websocket = new WebSocket("ws://" + window.location.hostname + ":8090/webSocketServer");
        websocket = new WebSocket("ws://" + window.location.hostname + ":13319/webSocketServer");
        websocket.onopen =openFunc;
        websocket.onmessage =onmessageFunc;
        websocket.onerror=onerrorFunc;
        websocket.onclose=oncloseFunc;
    }
    initWebsocket()
    setInterval(function () {
        websocket.send("hello")
    },50000);
});
