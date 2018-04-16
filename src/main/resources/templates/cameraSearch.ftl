<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>通过设备检索</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/cameraSearch.css"/>
    <link rel="stylesheet" href="/static/css/jedate.css"/>
    <link rel="stylesheet" href="/static/css/about.css"/>
    <link rel="stylesheet" href="/static/css/jquery-ui.min.css"/>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <#--<div class="container">-->
        <div>
        <div class="navbar-header">
            <button class="navbar-toggle" type="button" data-toggle="collapse" data-target="#navbar">
                <span class="sr-only"></span>
                <span class="icon-bar "></span>
                <span class="icon-bar "></span>
                <span class="icon-bar "></span>
            </button>
        </div>
        <div id="navbar" class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li><a href="/">预警平台</a></li>
                <li><a id="register"><span style="margin-left: -15px;">|</span>布防管理</a></li>
                <li><a href="/anytec/historyManager"><span>|</span>查询历史</a></li>
                <li class="nav_active"><a href="/anytec/portraitSearch"><span>|</span>人像检索</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="left_container">
    <div class="leftbtn_01" onclick="window.location.href='/anytec/portraitSearch'">
        <a href="/anytec/portraitSearch">人<br>像<br>检<br>索</a>
    </div>
    <div class="leftbtn_02" onclick="window.location.href='/anytec/cameraSearch'">
        <a href="/anytec/cameraSearch">通<br>过<br>设<br>备<br>搜<br>索</a>
    </div>
</div>
<div class="container max_container">
    <div class="row">
        <div class="col-lg-6 col_01">
            <div class="equipment_container">
                <div class="top_box">
                    <div class="top_btn">搜索指定设备</div>
                </div>
                <div class="middle_box">
                    <div class="left_similarity">
                        <div class="left_title">相似度</div>
                        <div class="left_percentage" id="threshold">75</div>
                        <div class="left_progress">
                            <div class="progress">
                                <div class="progress-bar" aria-valuenow="40" aria-valuemax="100" aria-valuemin="0"
                                     style="width:100%;">
                                    <div id="slider" class="slidercolor"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="right_date">
                        <div class="right_title">时间段</div>
                        <div class="start_date">
                            <div class="sart_text">起始</div>
                            <div class="demo1">
                                <input type="text" id="startTime" readonly="readonly" value="2018-03-03 0:0:0" >
                            </div>
                        </div>
                        <div class="end_date">
                            <div class="sart_text">结束</div>
                            <div class="demo1">
                                <input type="text" id="endTime" readonly="readonly" value="2018-04-07 23:59:59">
                            </div>
                        </div>
                    </div>
                </div>
                <div class="bottom_box">
                    <div class="bottom_choose">
                        <div class="choose_title">
                            <div class="title_text">选<br/>择<br/>设<br/>备</div>
                        </div>
                        <div class="choose_icon"><img src="/static/img/triangle.png"/></div>
                        <div class="choose_content">
                            <div class="container content_box">
                                <div class="row" id="cameraResult">
                                <#--<div class="col-lg-4 select_box">-->
                                        <#--<input type="checkbox" name="checkbox"></input>-->
                                        <#--<div class="checkbox_text">Camera01</div>-->
                                    <#--</div>-->
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="bottom_btn">
                        <div id="msg" class="msg_font"></div>
                        <div class="btn_box">
                            <div class="btn_01" onclick="allCheck()" style="margin-right:20px;">
                                <div class="button_font">全选</div>
                            </div>
                            <div class="btn_01" onclick="reverseCheck()" style="margin-right:20px;">
                                <div class="button_font">反选</div>
                            </div>
                            <div class="btn_01">
                                <img src="/static/img/icon_01.png" onclick="cameraSearch()"/>
                            </div>
                            <#--<div class="btn_02">-->
                                <#--<img src="/static/img/icon_02.png"/>-->
                            <#--</div>-->
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <div class="col-lg-6 col_01">
            <div class="time_container">
                <div class="page">
                    <div class="box">
                        <ul class="event_year" id="timeList">
                        </ul>
                        <ul class="event_list" id="photoResult">

                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<#--<div class="bottom_01"></div>-->
<div class="bottom_02"></div>
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/static/js/cameraSearch.js"></script>
<script type="text/javascript" src="/static/js/jedate.js"></script>
<script type="text/javascript" src="/static/js/jquery-ui.min.js"></script>
</body>
</html>