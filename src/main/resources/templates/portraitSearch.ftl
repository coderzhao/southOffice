<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<title>人像检索</title>
		<link rel="stylesheet" href="/static/css/bootstrap.min.css" />
		<link rel="stylesheet" href="/static/css/cameraSearch.css" />
		<link rel="stylesheet" href="/static/css/jedate.css" />
		<link rel="stylesheet" href="/static/css/portraitSearch.css" />
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
		   				<li><a id="register"><span>|</span>布防管理</a></li>
		   				<li><a href="/anytec/historyManager"><span>|</span>查询历史</a></li>
		   				<li  class="nav_active"><a href="/anytec/portraitSearch"><span>|</span>人像检索</a></li>
		   			</ul>
		   		</div>
		   	</div>
		</nav>
		<div class="left_container">
			<div class="leftbtn_01_1" onclick="window.location.href='/anytec/portraitSearch'">
				<a href="/anytec/portraitSearch">人<br>像<br>检<br>索</a>
			</div>
			<div class="leftbtn_02_1" onclick="window.location.href='/anytec/cameraSearch'">
				<a href="/anytec/cameraSearch">通<br>过<br>设<br>备<br>搜<br>索</a>
			</div>
		</div>
		<div class="container max_container">
			<div class="row">
				<div class="col-lg-6 col_01">
					<div class="equipment_container">
						<div class="top_box">
							<div class="top_btn">人像检索</div>
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
								        <input type="text" id="endTime" readonly="readonly" value="2018-03-07 23:59:59">
								    </div>
								</div>
							</div>
						</div>
						<div class="bottom_box">
							<div class="bottom_btn01">
								<#--<div class="date_videoBox">-->
									<#--<div class="date_box01">-->
										<#--<div class="date_cell">-->
											<#--2014年5月8日-->
										<#--</div>-->
									<#--</div>-->
								<#--</div>-->
								<div id="msg" class="msg_font"></div>
								<div class="btn_box01">
									<div class="btn_01_1">
										<img src="/static/img/icon_01.png" onclick="portraitSearch()">
									</div>
									<#--<div class="btn_02_1">-->
										<#--<img src="/static/img//icon_02.png"/>-->
									<#--</div>-->
								</div>
							</div>
							<!--滚动插件-->
							<div id="zsgun">
								<a href="#" class="prenext zspre"></a>
								<a href="#" class="prenext zsnext"></a>
								<div id="gundiv" class="container2">
									<ul id="photoResult">
										<#--<li><img src="/static/img/img01.jpg" alt="大头网网页特效" width="153" height="153"/><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img02.jpg" alt="" width="153" height="153"/><a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img03.jpg" alt="" width="153" height="153"/><a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img04.jpg" alt="大头网网页特效" width="153" height="153"/></a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img05.jpg" alt="" width="153" height="153"/><a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img01.jpg" alt="大头网网页特效" width="153" height="153"/></a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img02.jpg" alt="" width="153" height="153"/><a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img03.jpg" alt="" width="153" height="153"/><a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img04.jpg" alt="大头网网页特效" width="153" height="153"/></a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img05.jpg" alt="" width="153" height="153"/><a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img05.jpg" alt="" width="153" height="153"/><a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img05.jpg" alt="" width="153" height="153"/><a><p>13时4分58秒</a></p></li>-->
										<#--<li><img src="/static/img/img05.jpg" alt="" width="153" height="153"/><a><p>13时4分58秒</a></p></li>-->
									</ul>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6 col_01">
					<div class="time_container" id="container">
						
					</div>
				</div>
                <div class ='panel'>
                    <input id = 'input' value = '点击地图显示地址' disabled></input>
                </div>
			</div>
		</div>
		<div class="bottom_01"></div>
		<div class="bottom_02"></div>

        <script type="text/javascript" src="https://webapi.amap.com/maps?v=1.4.2&key=02f07c6c50144a86df5051213ef9cbeb"></script>
        <script type="text/javascript" src="https://webapi.amap.com/demos/js/liteToolbar.js"></script>
        <script type="text/javascript" src="/static/js/jquery.min.js"></script>
        <script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
        <script type="text/javascript" src="/static/js/jedate.js" ></script>
        <script type="text/javascript" src="/static/js/hScrollPane.js" ></script>
        <script type="text/javascript" src="/static/js/portraitSearch.js" ></script>
        <script type="text/javascript" src="/static/js/jquery-ui.min.js"></script>
	</body>

</html>