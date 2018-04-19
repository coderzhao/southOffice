$(document).ready(function(){
    var p_bgWidth=$(".percentage_bg").width();
    $(".percentage_bg").css("height",p_bgWidth);
    
    $("#gundiv ul li").click(function(){
    	$("#gundiv ul li").css("color","#000000");
    	$("#gundiv ul li img").removeClass("addimg_border");
    	$(this).children("img").addClass("addimg_border");
    	$(this).css("color","red");
    })

    $.ajax({
        url: "/anytec/getRegisterUrl",
        type: "post",
        dataType: "json",
        success: function (data) {
            if(data!=null){
                $("#register").attr("href",data.url);
            }
        }
    });

    $("#slider").slider({
        range: "max",
        min: 1,
        max: 100,
        value: 75,
        slide: function( event, ui ) {
            $( "#threshold" ).text( ui.value );
        }
    });
    $( "#threshold" ).text( $("#slider").slider( "value" ) );

    //跳转页面
    //	$(".leftbtn_02_1").click(function(){
    //		$(window).attr('location','cameraSearch');
    //	})
    //图片滚动插件代码
    //  var glen = $("#gundiv ul li").length;
    // //  var glen = 6;
    //  $("#gundiv ul").css("width",172 * (glen));
})

$("#zsgun").hScrollPane({
    mover:"ul",
    moverW:function(){return $("#zsgun li").length*430;}(),
    showArrow:true,
    handleCssAlter:"draghandlealter"
});


longitude=114.06667
latitude=22.6166
function locationMap(longitude,latitude) {
    var map = new AMap.Map('container', {
        resizeEnable: true,
        zoom: 13,
        center: [longitude,latitude]
        //center: [116.39,39.9]
    });
    var lnglat =[longitude,latitude];
    AMap.plugin('AMap.Geocoder', function () {
        var geocoder = new AMap.Geocoder({
            //city: "010"//城市，默认：“全国”
            city: "shenzhen"//城市，默认：“全国”
        });
        var marker = new AMap.Marker({
            map: map,
            bubble: true
        })
        marker.setPosition(lnglat);
        geocoder.getAddress(lnglat, function (status, result) {
            if (status == 'complete') {
                document.getElementById('input').value = result.regeocode.formattedAddress
            }
        });
        map.on('click', function (e) {
            marker.setPosition(e.lnglat);
            geocoder.getAddress(e.lnglat, function (status, result) {
                if (status == 'complete') {
                    document.getElementById('input').value = result.regeocode.formattedAddress
                }
            })
        })

    });
}

locationMap(longitude,latitude)


function portraitSearch() {
    var threshold =$("#threshold").text();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    $("#photoResult").html("");
    $("#msg").text("");
    if(startTime==""){
        $("#msg").text("搜索开始时间不能为空！");
        return false;
    }
    if(endTime==""){
        $("#msg").text("搜索结束时间不能为空！");
        return false;
    }
    var start = Date.parse(new Date(startTime));
    var end = Date.parse(new Date(endTime));
    if(start>end){
        $("#msg").text("开始时间不能大于结束时间！");
        return false;
    }
    var formData = new FormData();
    formData.append("threshold",threshold/100);
    formData.append("startTime",startTime);
    formData.append("endTime",endTime);
    formData.append("camera","");
    $.ajax({
        url:"/anytec/imageSearch",
        type:"post",
        data:formData,
        processData: false,
        contentType: false,
        success: function (data) {
            var content;
            if(data.length>0){
                $("#msg").text("查询结果如下：");
                for(var index in data){
                    content += "<li><div class='li_maxbox' onclick='locationMap("+data[index]['longitude']+","+data[index]['latitude']+")'>" +
                        "<div class='li_box'><img alt='' src='"+data[index]["face"]+"' width='153' height='153'/>"
                        +"<p style='width:95px;'>切出人脸图</p></div>";
                    content += "<div class='li_box'><img alt='' src='"+data[index]["matchFace"]+"' width='153' height='153'/>"
                        +"<p style='width:95px;'>库中图</p></div>";
                    content += "<div class='li_box li_box3'><img alt='' src='"+data[index]["photo"]+"' width='153' height='153'/>"
                        +"<p style='width:95px;'>原图</p></div>";
                    content += "<div class='li_textBox'><p style='text-align: left;margin-left: 30px;margin-top: 8px;'>姓名:"+data[index]["meta"]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;相似度:"+data[index]["confidence"]+"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;摄像头:"+data[index]["camera"]+"</p>"
                        +"<p>时间："+timetrans(data[index]['timestamp'].$numberLong)+"</p></div></li>";
                }
                $("#photoResult").append(content);
                $("#gundiv ul").css("width",417 * (data.length));
                $("#zsgun").hScrollPane({
                    mover:"ul",
                    moverW:function(){return $("#zsgun li").length*430;}(),
                    showArrow:true,
                    handleCssAlter:"draghandlealter"
                });
            }else{
                $("#msg").text("没有查到符合条件的结果！");
            }
        },
        error:function () {
            $("#msg").text("查询发生错误！");
        }
    });
}

function timetrans(time){
    var date = new Date(parseInt(time));//如果date为13位不需要乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    var m = (date.getMinutes() <10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    var s = (date.getSeconds() <10 ? '0' + date.getSeconds() : date.getSeconds());
    return Y+M+D+h+m+s;
}