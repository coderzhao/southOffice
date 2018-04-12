<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>查询历史</title>
    <link href="/static/css/bootstrap-table.css" rel="stylesheet"/>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="/static/css/jedate.css"/>
    <style>
        /* Remove the navbar's default margin-bottom and rounded borders */
        .navbar {
            margin-bottom: 0;
            border-radius: 0;
        }

        /* Add a gray background color and some padding to the footer */
        footer {
            background-color: #f2f2f2;
            padding: 25px;
        }
        .titlefont{
            float: left;
            margin-left: 5%;
            margin-top: 12px;
            font-size: 2.5vmin;
            font-family: 微软雅黑;
        }
    </style>
</head>
<body>

<!--导航栏-->

<nav class="navbar navbar-inverse">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <!--<a class="navbar-brand" href="#">预警平台</a>-->
            <a class="navbar-brand" href="#historyManager">查询历史</a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
            <ul class="nav navbar-nav">
                <li><a href="../../" target="_blank">预警平台</a></li>
                <li><a href="http://u1961b1648.51mypc.cn:24850" target="_blank">布防管理</a></li>
                <li><a href="/anytec/portraitSearch" target="_blank">人像检索</a></li>
            </ul>
        </div>
    </div>
</nav>

<div class="jumbotron">
    <div class="container text-center">
        <h1>查询历史</h1>
    </div>
</div>

<!--页面主题-->
<div class="panel-body" style="padding-bottom:0px;">
    <div class="panel panel-default">
        <div class="panel-heading">
            <div class="text-primary">查询条件</div>
        </div>
        <div class="panel-body">
                <div class="form-group" style="margin-top:15px">
                    <div class="col-sm-2 text-primary">
                        <label class="titlefont ">姓名</label>
                        <input type="text" class="form-control" id="meta">
                    </div>
                <#---->
                    <div class="col-sm-2 text-primary">
                        <label class="titlefont ">摄像头</label>
                        <select class="form-control" id="camera"></select>
                    </div>
                    <div class="col-sm-2 text-primary">
                        <label class="titlefont">开始时间</label>
                        <input type="text" id="startTime" readonly="readonly" value="2018-03-07 23:59:59">
                    </div>
                    <div class="col-sm-2 text-primary">
                        <label class="titlefont">结束时间</label>
                        <input type="text" id="endTime" readonly="readonly" value="2018-04-07 23:59:59">
                    </div>
                    <div class="col-sm-2">
                        <button type="button" style="margin-left:50px ;margin-top: 16%;" id="btn_query"
                                class="btn btn-info">查询
                        </button>
                    </div>
                </div>
        </div>
    </div>
<#---->
    <table id="tb_history"></table>
    <div id="outerdiv"
         style="position:fixed;top:0;left:0;background:rgba(0,0,0,0.7);z-index:2;width:100%;height:100%;display:none;">
        <div id="innerdiv" style="position:absolute;">
            <img id="bigimg" style="border:5px solid #fff;" src=""/>
        </div>
    </div>
    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="/static/js/bootstrap-table.js"></script>
    <script type="text/javascript" src="/static/js/bootstrap-table-zh-CN.js"></script>
    <script type="text/javascript" src="/static/js/historyManager.js"></script>
    <script type="text/javascript" src="/static/js/jedate.js"></script>
</body>
</html>