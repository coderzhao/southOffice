$(document).ready(function(){
    $(".leftbtn_01").click(function(){
        $(window).attr('location','portrait_search.html');
    })

    $("#slider").slider({
        range: "max",
        min: 1,
        max: 100,
        value: 90,
        slide: function( event, ui ) {
            $( "#threshold" ).text( ui.value );
        }
    });
    $( "#threshold" ).text( $("#slider").slider( "value" ) );

    $.ajax({
        url:"/anytec/getCameras",
        type:"post",
        dataType:"json",
        success:function (data) {
            for(var index in data){
                $("#cameraResult").append("<div class='col-lg-4 select_box'>"+
                    "<input type='checkbox' name='checkbox' value='"+data[index]+"'/>"+
                    "<div class='checkbox_text'>"+data[index]+"</div></div>");
            }
        },
        error:function () {
            $("#cameraResult").append("获取camera失败！");
        }
    });

    laydate({
        elem: '#startTime'
    });
    laydate({
        elem: '#endTime'
    });

})

function allCheck() {
    $('input[name="checkbox"]').prop('checked','true');
}

function reverseCheck() {
    $('input[name="checkbox"]').each(function () {
        $(this).prop("checked", !$(this).prop("checked"));
    });
}

function imageSearch() {
    var threshold =$("#threshold").text();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    if(startTime==""){
        $("#msg").text("搜索开始时间不能为空！");
        return false;
    }
    if(endTime==""){
        $("#msg").text("搜索结束时间不能为空！");
        return false;
    }
    var camera = "";
    $.each($('input:checkbox'),function(){
        if(this.checked) {
            //cameraArray.push($(this).val());
            camera+=$(this).val();
            camera+=",";
        }
    });
    camera=camera.substring(0,camera.length-1);
    if(camera==""){
        $("#msg").text("请选择要搜索的设备！");
        return false;
    }
    var formData = new FormData();
    formData.append("threshold",threshold/100);
    formData.append("startTime",startTime);
    formData.append("endTime",endTime);
    formData.append("camera",camera);
    $.ajax({
        url:"/anytec/imageSearch",
        type:"post",
        data:formData,
        processData: false,
        contentType: false,
        success: function (data) {
            if(data.length>0){
                for(var index in data){
                    $("#photoResult").append("<div class='col-md-3 col-sm-4 col-xs-6 screenshots'>"+
                        "<div class='screenshots_box'>"+
                        "<img src='"+data[index]["face"]+"'/>"+
                        "<div class='img_time'>"+timetrans(data[index]['timestamp'])+"</div></div></div>");
                }
            }


            // <div class="time_day">
            //     <div class="container box_time">
            //     <div class="row row_box" id="photoResult">
            //     <div class="col-md-3 col-sm-4 col-xs-6 screenshots">
            //     <div class="screenshots_box">
            //     <img src="/static/img/img1.png" />
            //     <div class="img_time">5时4分58秒</div>
            // </div>
            // </div>
            // </div>
            // </div>
            // </div>

            // var resultList = data.result;
            // var mjo = new Array();
            // var yjo = new Array();
            // var jo = new Array();
            // var array =new Array();
            //  var date = new Date(resultList[0]);
            //  var year =date.getFullYear();
            //  var month = date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1;
            //  var day = date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate();
            // for (var index in resultList){
            //     var indexDate = new Date(resultList[index]);
            //     var indexYear =indexDate.getFullYear();
            //     var indexMonth = indexDate.getMonth()+1 < 10 ? '0'+(indexDate.getMonth()+1) : indexDate.getMonth()+1;
            //     var indexDay = indexDate.getDate() < 10 ? '0' + (indexDate.getDate()) : indexDate.getDate();
            //     var indexH = (indexDate.getHours() < 10 ? '0' + indexDate.getHours() : indexDate.getHours()) + ':';
            //     var indexM = (indexDate.getMinutes() <10 ? '0' + indexDate.getMinutes() : indexDate.getMinutes()) + ':';
            //     var indexS = (indexDate.getSeconds() <10 ? '0' + indexDate.getSeconds() : indexDate.getSeconds());
            //     var indexTime = indexH+indexM+indexS;
            //     if(indexYear==year){
            //        if(indexMonth==month){
            //            if(indexDay==day){
            //                array.push(indexTime);
            //            }else {
            //                mjo.push({indexDay:array});
            //                array.splice(0, array.length);
            //                array.push(indexTime);
            //                day =indexDay;
            //            }
            //        }else {
            //                mjo.push({indexDay:array});
            //                yjo.push({indexMonth:mjo});
            //                array.splice(0, array.length);
            //                mjo.splice(0, mjo.length);
            //                day = indexDay;
            //                month = indexMonth;
            //        }
            //     }else {
            //         mjo.push({indexDay:array});
            //         yjo.push({indexMonth:mjo});
            //         jo.push({indexYear:yjo});
            //         array.splice(0, array.length);
            //         mjo.splice(0, mjo.length);
            //         yjo.splice(0, yjo.length);
            //         day = indexDay;
            //         month = indexMonth;
            //         year = indexYear;
            //     }
            // }
            //  var str = jo.join("-");
            //  alert(str);
        },
        error:function () {
            $("#msg").text("查询发生错误！");
        }
    });
}

function timetrans(time){
    var date = new Date(parseInt(time.$numberLong));//如果date为13位不需要乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    var m = (date.getMinutes() <10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    var s = (date.getSeconds() <10 ? '0' + date.getSeconds() : date.getSeconds());
    return Y+M+D+h+m+s;
}
