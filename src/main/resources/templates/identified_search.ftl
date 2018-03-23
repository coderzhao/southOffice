<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>人像检索</title>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css" />
    <link rel="stylesheet" href="/static/css/identified_search.css" />
    <link rel="stylesheet" href="/static/css/laydate.css" />
    <link rel="stylesheet" href="/static/css/jquery-ui.min.css" />
    <script type="text/javascript" src="/static/js/jquery.min.js" ></script>
    <script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/static/js/identified_search.js" ></script>
    <script type="text/javascript" src="/static/js/laydate.dev.js" ></script>
    <script type="text/javascript" src="/static/js/jquery-ui.min.js"></script>
</head>
<body>
<nav class="navbar navbar-inverse navbar-fixed-top">
    <div class="container">
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
                <li><a href="#">预警平台</a></li>
                <li><a href="protection.html"><span>|</span>布防管理</a></li>
                <li><a href="#"><span>|</span>查询历史</a></li>
                <li  class="nav_active"><a href="identified_search.html"><span>|</span>人像检索</a></li>
            </ul>
        </div>
    </div>
</nav>
<div class="left_container">
    <div class="leftbtn_01">
        <a href="#">人<br>像<br>检<br>索</a>
    </div>
    <div class="leftbtn_02">
        <a href="#">通<br>过<br>设<br>备<br>搜<br>索</a>
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
                        <div class="left_percentage" id="threshold">87%</div>
                        <#--<div class="left_progress">-->
                            <#--<div class="progress">-->
                                <#--<div class="progress-bar" aria-valuenow="40" aria-valuemax="100" aria-valuemin="0" style="width:87%;">-->
                                <#--</div>-->
                            <#--</div>-->
                        <#--</div>-->
                        <div class="left_progress">
                            <div class="progress">
                                <div class="progress-bar" aria-valuenow="40" aria-valuemax="100" aria-valuemin="0" style="width:100%;">
                                    <div id="slider"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="right_date">
                        <div class="right_title">时间段</div>
                        <div class="start_date">
                            <div class="sart_text">起始</div>
                            <div class="demo1">
                                <input type="text" id="startTime" readonly="readonly" value="2018-03-03" >
                            </div>
                        </div>
                        <div class="end_date">
                            <div class="sart_text">结束</div>
                            <div class="demo1">
                                <input type="text" id="endTime" readonly="readonly" value="2018-03-07">
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
                    <div id="msg"></div>
                    <div class="bottom_btn">
                        <div class="btn_box">
                            <div class="btn_01" onclick="allCheck()">
                               全选
                            </div>
                            <div class="btn_01" onclick="reverseCheck()">
                                反选
                            </div>
                            <div class="btn_01">
                                <img src="/static/img/icon_01.png" onclick="imageSearch()"/>
                            </div>
                            <div class="btn_02">
                                <img src="/static/img/icon_02.png"/>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
        <div class="col-lg-6 col_01">
            <div class="time_container">
                <div class="bottom_time" id="photoResult">
                    <div class="top_date">
                        <div class="left_month" id="month">3月</div>
                        <div class="right_year" id="year">2018年</div>
                    </div>
                    <div class="time_day day01">
                        <div class="day_digital" id="day">7</div>
                        <div class="container box_time">
                            <div class="row">
                                <div class="col-md-3 col-sm-4 col-xs-6 screenshots">
                                    <div class="screenshots_box">
                                        <img src="/static/img/img1.png" />
                                        <div class="img_time">5时4分58秒</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="time_day">
                        <div class="container box_time">
                            <div class="row row_box">
                                <div class="day_digital">5</div>
                                <div class="col-md-3 col-sm-4 col-xs-6 screenshots">
                                    <div class="screenshots_box">
                                        <img src="/static/img/img1.png" />
                                        <div class="img_time">5时4分58秒</div>
                                    </div>
                                </div>
                                <div class="col-md-3 col-sm-4 col-xs-6 screenshots">
                                    <div class="screenshots_box">
                                        <img src="/static/img/img1.png" />
                                        <div class="img_time">5时4分58秒</div>
                                    </div>
                                </div>
                                <div class="col-md-3 col-sm-4 col-xs-6 screenshots">
                                    <div class="screenshots_box">
                                        <img src="/static/img/img1.png" />
                                        <div class="img_time">5时4分58秒</div>
                                    </div>
                                </div>

                            </div>
                        </div>
                    </div>
                    <div class="time_day">
                        <div class="container box_time">
                            <div class="row row_box">
                                <div class="day_digital">4</div>
                                <div class="col-md-3 col-sm-4 col-xs-6 screenshots">
                                    <div class="screenshots_box">
                                        <img src="/static/img/img1.png" />
                                        <div class="img_time">5时4分58秒</div>
                                    </div>
                                </div>
                                <div class="col-md-3 col-sm-4 col-xs-6 screenshots">
                                    <div class="screenshots_box">
                                        <img src="/static/img/img1.png" />
                                        <div class="img_time">5时4分58秒</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</div>
<div class="bottom_01"></div>
<div class="bottom_02"></div>
</body>
</html>