<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>查询历史</title>
    <link href="/static/css/bootstrap-table.css" rel="stylesheet"/>
    <link rel="stylesheet" href="/static/css/bootstrap.min.css">


    <script type="text/javascript" src="/static/js/jquery.min.js"></script>
    <script src="/static/js/bootstrap.min.js"></script>
    <script src="/static/js/bootstrap-table.js"></script>
    <script src="/static/js/bootstrap-table-zh-CN.js"></script>
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
                <li><a href="http://u1961b1648.51mypc.cn:24850" target="_blank">布防管理</a></li>
                <li><a href="../../" target="_blank">预警平台</a></li>
                <!--<li><a href="#">查询历史</a></li>-->
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
            <form id="formSearch" class="form-horizontal">
                <div class="form-group" style="margin-top:15px">
                    <div class="col-sm-2 text-primary">
                        <label class="control-label " for="meta">姓名</label>
                        <input type="text" class="form-control" id="meta">
                    </div>
                <#---->
                    <div class="col-sm-2 text-primary">
                        <label class="control-label " for="camera">摄像头</label>
                        <input type="text" class="form-control" id="camera">
                    </div>
                    <!--<div class="col-sm-2 text-primary">-->
                    <!--<label class="control-label " for="start" >起始时间</label>-->
                    <!--<input type="date" class="form-control" id="start">-->
                    <!--</div>-->
                <#---->
                    <!--<div class="col-sm-2 text-primary">-->
                    <!--<label class="control-label" for="end" >结束时间</label>-->
                    <!--<input type="date" class="form-control" id="end" >-->
                    <!--</div>-->
                    <div class="col-sm-4">
                        <button type="button" style="margin-left:50px ;margin-top: 25px;" id="btn_query"
                                class="btn btn-info">查询
                        </button>
                    </div>
                </div>
            </form>
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
<#--</div>-->
<#---->
    <script>
        <#---->
        $("#btn_query").click(function () {
//
            $("#tb_history").bootstrapTable("refresh");
//
        });
        $(function () {
//
//1.初始化Table
            var oTable = new TableInit();
            oTable.Init();
//
//2.初始化Button的点击事件
            var oButtonInit = new ButtonInit();
            oButtonInit.Init();
//
        });
        //
        var TableInit = function () {
            var oTableInit = new Object();
//初始化Table
            oTableInit.Init = function () {
                $('#tb_history').bootstrapTable({
                    url: '/anytec/getFaceList',         //请求后台的URL（*）
                    method: 'get',                      //请求方式（*）
                    toolbar: '#toolbar',                //工具按钮用哪个容器
                    striped: true,                      //是否显示行间隔色
                    cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
                    pagination: true,                   //是否显示分页（*）
                    sortable: false,                     //是否启用排序
// sortOrder: "asc",                   //排序方式
                    queryParams: oTableInit.queryParams,//传递参数（*）
                    sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
                    pageNumber: 1,                       //初始化加载第一页，默认第一页
                    pageSize: 10,                       //每页的记录行数（*）
                    pageList: [10,20,30],           //可供选择的每页的行数（*）
                    search: false,                      //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
//strictSearch: true,
//showColumns: true,                  //是否显示所有的列
// showRefresh: true,                  //是否显示刷新按钮
                    minimumCountColumns: 2,             //最少允许的列数
                    clickToSelect: true,                //是否启用点击选中行
                    height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
//                    uniqueId: "name",                     //每一行的唯一标识，一般为主键列
//showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
                    cardView: false,                    //是否显示详细视图
                    detailView: false,                   //是否显示父子表
                    columns: [
//                        {
//                            field: 'id',
//                            title: 'id'
//                        },
                        {
                            field: 'meta',
                            title: '姓名'
                        }
                        , {
                            field: 'confidence',
                            title: '相似度'
                        }
                        , {
                            field: 'camera',
                            title: '摄像头'
                        }
                        , {
                            field: 'face',
                            title: '切出人脸',
                            formatter: function (value, row, index) {
                                if ('' != value && null != value) {
                                    value = '<img src="data:image;base64,' + value + '" style="height: 100px;width:100px;" onclick="showImageDetail(this)"/>';
//                                    value = '<img src="' + value + '" style="height: 100px;width:100px;" onclick="showImageDetail(this)"/>';
                                    return value;
                                }
                            }
                        }, {
                            field: 'matchFace',
                            title: '匹配的人脸',
                            formatter: function (value, row, index) {
                                if ('' != value && null != value) {
                                    value = '<img src="' + value + '" style="height: 100px;width:100px;"  onclick="showImageDetail(this)"/>';
                                    return value;
                                }
                            }
                        }, {
                            field: 'photo',
                            title: '原图',
                            formatter: function (value, row, index) {
                                if ('' != value && null != value) {
                                    value = '<img src="' + value + '" style="height: 100px;width:100px;"  onclick="showImageDetail(this)"/>';
                                    return value;
                                }
                            }
                        },{
                            field: 'timestamp',
                            title: '时间',
                            formatter: function (value, row, index) {
                                var t =parseInt(row.timestamp);
//                                var d =new Date(t);
//                                d.setTime(t);
//                                var s =d.format('yyyy-MM-dd HH-mm-ss');
                                return timetrans(t);
//                                return row.timestamp;
                            }
                        }
                    ],
                });
            };
//得到查询的参数
            oTableInit.queryParams = function (params) {
                var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
                    limit: params.limit,   //页面大小
                    offset: params.offset/params.limit,  //页码
                    meta: $("#meta").val(),
                    camera:$("#camera").val()
                };
                return temp;
            };
            return oTableInit;
        };
        //
        //
        var ButtonInit = function () {
            var oInit = new Object();
//
//
            oInit.Init = function () {
//
            };
//
            return oInit;
        };
        //
        //处理时间戳
        function changeDateFormat(val) {
            if (val != null) {
                var date = new Date(val);
                var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
                var currentDate = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
                return date.getFullYear() + "-" + month + "-" + currentDate;
            }
        }


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

        //
        //显示图片大图
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
        //
    </script>
</body>
</html>
<#---->