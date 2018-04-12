$(document).ready(function () {
    $(".leftbtn_01").click(function () {
        $(window).attr('location', 'portraitSearch');
    })

    $('label').click(function(){
        $('.event_year>li').removeClass('current');
        $(this).parent('li').addClass('current');
        var year = $(this).attr('for');
        $('#'+year).parent().prevAll('div').slideUp(800);
        $('#'+year).parent().slideDown(800).nextAll('div').slideDown(800);
    });

    $("#slider").slider({
        range: "max",
        min: 1,
        max: 100,
        value: 90,
        slide: function (event, ui) {
            $("#threshold").text(ui.value);
        }
    });
    $("#threshold").text($("#slider").slider("value"));

    $.ajax({
        url: "/anytec/getCameras",
        type: "post",
        dataType: "json",
        success: function (data) {
            if (data.length > 0) {
                for (var index in data) {
                    $("#cameraResult").append("<div class='col-lg-4 select_box'>" +
                        "<input type='checkbox' name='checkbox' value='" + data[index] + "'/>" +
                        "<div class='checkbox_text'>" + data[index] + "</div></div>");
                }
            }
        },
        error: function () {
            $("#cameraResult").append("获取camera失败！");
        }
    });

})


function allCheck() {
    $('input[name="checkbox"]').prop('checked', 'true');
}

function reverseCheck() {
    $('input[name="checkbox"]').each(function () {
        $(this).prop("checked", !$(this).prop("checked"));
    });
}

function labelClick(obj) {
    $('.event_year>li').removeClass('current');
    $(obj).parent('li').addClass('current');
    var year = $(obj).attr('for');
    $('#'+year).parent().prevAll('div').slideUp(800);
    $('#'+year).parent().slideDown(800).nextAll('div').slideDown(800);
}

function cameraSearch() {
    var threshold = $("#threshold").text();
    var startTime = $("#startTime").val();
    var endTime = $("#endTime").val();
    if (startTime == "") {
        $("#msg").text("搜索开始时间不能为空！");
        return false;
    }
    if (endTime == "") {
        $("#msg").text("搜索结束时间不能为空！");
        return false;
    }

    var start = Date.parse(new Date(startTime));
    var end = Date.parse(new Date(endTime));
    if (start > end) {
        $("#msg").text("开始时间不能大于结束时间！");
        return false;
    }

    var camera = "";
    $.each($('input:checkbox'), function () {
        if (this.checked) {
            //cameraArray.push($(this).val());
            camera += $(this).val();
            camera += ",";
        }
    });
    camera = camera.substring(0, camera.length - 1);
    if (camera == "") {
        $("#msg").text("请选择要搜索的设备！");
        return false;
    }
    var formData = new FormData();
    formData.append("threshold", threshold / 100);
    formData.append("startTime", startTime);
    formData.append("endTime", endTime);
    formData.append("camera", camera);
    $.ajax({
        url: "/anytec/imageSearchByCamera",
        type: "post",
        data: formData,
        processData: false,
        contentType: false,
        success: function (data) {
            // if(data.length>0){
            //     $("#msg").text("查询结果如下：");
            //     for(var index in data){
            //         // $("#photoResult").append("<div class='col-md-3 col-sm-4 col-xs-6 screenshots'>"+
            //         //     "<div class='screenshots_box'>"+
            //         //     "<img src='"+data[index]["face"]+"'/>"+
            //         //     "<div class='img_time'>"+timetrans(data[index]['timestamp'])+"</div></div></div>");
            //
            //     // <li role="presentation" class="active"><a href="#one">one</a></li>
            //         $("#timeList").append("<li role='presentation'><a href='#number"+index+"'>xxx</a></li>");
            //         $("#photoResult").append("<div class='time_day01'><div class='container box_time'>" +
            //             " <div class='row' id='number"+index+"'><div class='col-md-3 col-sm-4 col-xs-6 screenshots'>"+
            //              "<div class='screenshots_box'>"+
            //              "<img src='"+data[index]["face"]+"'/>"+
            //              "<div class='img_time'>"+timetrans(data[index]['timestamp'])+"</div></div></div>");
            //     }
            // }else{
            //     $("#msg").text("没有查到符合条件的结果！");
            // }
            //
            $("#timeList").html("");
            $("#photoResult").html("");
            if (data.length > 0) {
                $("#msg").text("查询结果如下：");
                var i = 0;
                var indexMonth;
                var indexYear;
                var time;
                var content;
                for (var index in data) {
                    var jsonobj = eval(data[index]);
                    if(index==0){
                        for(var prop in jsonobj){
                            indexYear = prop.split("年")[0];
                            var str = prop.split("年")[1];
                            indexMonth = str.split("月")[0];
                            time = indexYear + "年" + indexMonth + "月";
                            $("#timeList").append("<li class='current'><label for='" + indexYear + indexMonth + "' onclick='labelClick(this)'>" + time + "</label></li>");
                            content=" <div><h3 id='" + indexYear + indexMonth+ "'>"+indexYear+"年"+indexMonth +"月"+"</h3>";
                            content+="<li><span>" +jsonobj[prop][0]["day"]+"日</span><div class='imgbox'>";
                            for (var key in jsonobj[prop]) {
                                content+="<div class='imgminbox'><div class='event_img'>"+
                                    "<img src='" + jsonobj[prop][key]["face"] + "'/></div>" +
                                    "<div class='event_text'>" + jsonobj[prop][key]["time"] + "</div></div>";
                            }
                            content+="</div></li>";
                        }
                    }else{
                        for(var prop in jsonobj){
                            year = prop.split("年")[0];
                            month = prop.split("年")[1].split("月")[0];
                            time = year + "年" + month + "月";
                            if (year == indexYear && month == indexMonth) {
                                content+="<li><span>"+jsonobj[prop][0]["day"]+"日</span><div class='imgbox'>";
                            }else {
                                $("#timeList").append("<li><label for='" + year + month + "' onclick='labelClick(this)'>" + time + "</label></li>");
                                content+=" </div><div><h3 id='" + year + month + "'>"+year+"年"+month+"月"+"</h3>";
                                content+="<li><span>" +jsonobj[prop][0]["day"]+"日</span><div class='imgbox'>";
                            }
                            for (var key in jsonobj[prop]) {
                                content+="<div class='imgminbox'><div class='event_img'>"+
                                    "<img src='" + jsonobj[prop][key]["face"] + "'/></div>" +
                                    "<div class='event_text'>" + jsonobj[prop][key]["time"] + "</div></div>";
                            }
                            content+="</div></li>";
                        }
                        indexYear=year;
                        indexMonth=month;
                    }
                    if (data.length == 1) {
                        content+="</div>";
                    }

                    if (i == data.length-1&&data.length!=1) {
                        content+="</div>";
                    }
                    i = i + 1;
                    }
                    $("#photoResult").append(content);
                }
            },
            // var date = new Date(parseInt(data[0]['timestamp'].$numberLong));
            // var year = date.getFullYear();
            // var month = date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1;
            // var monthArray = new Array();
            // var yearArray = new Array();
            // for (var index in data) {
            //     if(index==0){
            //         monthArray.push(data[index]);
            //     }
            //     if (index != 0) {
            //         var indexDate = new Date(parseInt(data[index]['timestamp'].$numberLong));
            //         var indexYear = indexDate.getFullYear();
            //         var indexMonth = indexDate.getMonth() + 1 < 10 ? '0' + (indexDate.getMonth() + 1) : indexDate.getMonth() + 1;
            //         if (indexYear == year) {
            //             if (indexMonth == month) {
            //                 monthArray.push(data[index]);
            //             } else {
            //                 var array = new Array();
            //                 for(var i=0;i<monthArray.length;i++){
            //                     array[i] = monthArray[i];
            //                 }
            //                 yearArray.push(month, array);
            //                 monthArray.splice(0, monthArray.length);
            //                 monthArray.push(indexMonth);
            //                 month = indexMonth;
            //             }
            //         }
            //         if (index == data.length-1) {
            //             if (monthArray.length != 0) {
            //                 var array = new Array();
            //                 for(var i=0;i<monthArray.length;i++){
            //                     array[i] = monthArray[i];
            //                 }
            //                 yearArray.push(month, array);
            //                 monthArray.splice(0, monthArray.length);
            //             }
            //         }
            //     }
            // }
            //
            // if (yearArray.length > 0) {
            //     $("#timeList").html("");
            //     $("#photoResult").html("");
            //     for (var key in yearArray) {
            //             // <li role="presentation" class="active"><a href="#one">one</a></li>
            //         $("#timeList").append("<li role='presentation'><a href='#number" + key + "'>"+key+"月</a></li>");
            //         $("#photoResult").append("<div class='time_day01'><div class='container box_time'>" +
            //         " <div class='row' id='number" + key + "'>");
            //         for(var okey in yearArray[key]){
            //             $("#photoResult").append("<div class='col-md-3 col-sm-4 col-xs-6 screenshots'>" +
            //                 "<div class='screenshots_box'>" +
            //                 "<img src='" + yearArray[key][okey]["face"] + "'/></div></div>");
            //         }
            //    }
            // }
        error: function () {
            $("#msg").text("查询发生错误！");
        }
    });
}

function timetrans(time) {
    var date = new Date(parseInt(time.$numberLong));//如果date为13位不需要乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
    return Y + M + D + h + m + s;
}


function timetransYMD(time) {
    var date = new Date(parseInt(time.$numberLong));//如果date为13位不需要乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
    var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
    return Y + M + D + h + m + s;
}

function timetransHMS(time) {
    var date = new Date(parseInt(time.$numberLong));//如果date为13位不需要乘1000
    var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
    var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes()) + ':';
    var s = (date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds());
    return h + m + s;
}
