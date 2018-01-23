$(document).ready(function () {

    var websocket;
    var pushContent = [];

    // $(".modal-backdrop").remove();
    $.ajax({
        url: '/anytec/getFaceList',
        type: 'post',
        success: function (data) {
            if (data.length > 0) {
                var rootJson = JSON.parse(data);

                var newContent = '';
                var showHtml = '';
                // var preHtml=$('#showIdentifyResults').html();
                for (var i = 0; i < rootJson.length; i++) {
                    var singleFace = rootJson[i];
                    if (singleFace) {
                        var face = "data:image;base64," + singleFace["face"];
                        var photo = "data:image;base64," + singleFace["photo"];
                        // var matchFaceURL = singleFace.matchFace;
                        // var confidence = singleFace.confidence;
                        // var meta = singleFace.meta;
                        showHtml =
                            '<div class="col-xs-4 col-sm-4">' +
                            '<div class="col-xs-12">' +
                            '<div class="col-xs-4"><img class="col-xs-12 thumbnail" src="' + face + '"/><p>切出人脸图</p></div>' +
                            '<div class="col-xs-4"><img class="col-xs-12 thumbnail" src="' + singleFace["matchFace"] + '"/><p>库中图</p></div>' +
                            '<div class="col-xs-4"><img class="col-xs-12 thumbnail" data-toggle="modal" data-target="#myModal' + pushContent.length + '" src="' + photo + '"/><p>原图</p>' +
                            '<div class="modal fade" id="myModal' + pushContent.length + '" tabindex="-1">' +
                            '<div class="modal-dialog modal-lg">' +
                            '<div class="modal-content">' +
                            '<img src="' + photo + '" class="col-xs-9 thumbnail " data-toggle="modal"/>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '</div>' +
                            '<div class="col-xs-12">' +
                            '<p>confidence:' + singleFace["confidence"] + '</p>' +
                            '<p>姓名:' + singleFace["meta"] + '</p>' +
                            '<p>时间:' + singleFace["timestamp"] + '</p>' +
                            '<p>摄像头:' + singleFace["camera"] + '</p>' +
                            '</div>' +
                            '</div>'
                        newContent = showHtml + newContent
                        // if (currentIndex <= 17) {
                        //     pushContent[currentIndex] = showHtml;
                        //     currentIndex++;
                        //     continue;
                        // }else{
                        //     currentIndex[17]=
                        // }
                        // if (pushContent.length <= 9) {
                        //     pushContent[pushContent.length] = showHtml;
                        // } else {
                        //     pushContent.shift();
                        //     pushContent.push(showHtml);
                        // }

                    }
                    // var demo =  + showHtml;
                }
                // var tmp = pushContent.slice(0, -1);
                // var reverse = tmp.reverse();
                $('#showIdentifyResults').html(newContent);
                // $('#showIdentifyResults').html(reverse.join(''));

            }
        }
    })

});