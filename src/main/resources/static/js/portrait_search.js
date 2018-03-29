$(document).ready(function(){
    var p_bgWidth=$(".percentage_bg").width();
    $(".percentage_bg").css("height",p_bgWidth);
    
    $("#gundiv ul li").click(function(){
    	$("#gundiv ul li").css("color","#000000");
    	$("#gundiv ul li img").removeClass("addimg_border");
    	$(this).children("img").addClass("addimg_border");
    	$(this).css("color","red");
    })
})
