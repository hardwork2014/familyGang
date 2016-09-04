var menu_order='';
$(function(){
	init();
	$("#top-searchInput").keydown(function(e){
		if(e.keyCode==13){
			$("#top-searchInput").val("");
			diaAlert("warning", "亲，下一版本尽请期待！");
		}
	});
})
window.onload=function(){
	windowSize();
}
function init(){
	leftSlide();
	leftBtn();
	//windowSize();
}

//左侧下拉效果
function leftSlide(){
	$(".menuLev1").click(function(){
		var obj=$(this);
		var nextobj=obj.next();
		var objLi=obj.closest("li");
		var objSiblings=objLi.siblings().find(".menuLev2Wrap");
		
		nextobj.slideToggle("slow");
		if(objSiblings.is(":visible")){
			objSiblings.slideUp("slow");
		}
	})
	
	$(".menuLev2Wrap ul li:last-child").css({"border-bottom":"0"});
}

//左侧伸缩按钮
function leftBtn(){
	var siderBtn=$("#siderBtn");
	
	siderBtn.click(function(){
		var obj=$(this);
		if(!obj.hasClass("siderBtnOn")){
			$("#sider").hide();
			obj.css({"left":"0px"});
			obj.addClass("siderBtnOn");
			$("#mainWrap").css({"margin-left":0});
			$("body").removeClass("image");
		}else{
			$("#sider").show();
			obj.css({"left":"211px"});
			obj.removeClass("siderBtnOn");
			$("#mainWrap").css({"margin-left":"211px"});
			$("body").addClass("image");
		}
	})
	
}

//处理高度和宽度
function layoutSize(){
	var sider=$("#sider");
	
	var size=(function(){
		var winOjb=$(window);
		var topH=109;
		return {w:winOjb.width(),h:winOjb.height()-topH,topH:topH};
	})();
	
	$("#siderBtn").css({"top":(size.h/2 + size.topH)+"px"}).show();
	
	(function(){
		var wrapH=$("#wrap").outerHeight();
		var siderH=sider.outerHeight();
		var h=siderH>wrapH?siderH:wrapH;
		h=h>size.h?h:size.h;
		sider.css({"min-height":h});
	})();
}

//窗口改变
function windowSize(){
	$(window).resize(function(){
		layoutSize();
	}).trigger("resize");
}

//同步iframe高度
function iFrameHeight(ifmID) { 
	var pTar = null;
	if (document.getElementById){
		pTar = document.getElementById(ifmID);
	}
	else{
		eval('pTar = ' + ifmID + ';');
	}
	if (pTar && !window.opera){
		//begin resizing iframe
		pTar.style.display="block"
		if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
			//ns6 syntax
			pTar.height = pTar.contentDocument.body.offsetHeight +20;
			//pTar.width = pTar.contentDocument.body.scrollWidth+20;
		}
		else if (pTar.Document && pTar.Document.body.scrollHeight){
			//ie5+ syntax
			pTar.height = pTar.Document.body.scrollHeight;
			//pTar.width = pTar.Document.body.scrollWidth;
		}
	}
}

//点击菜单，中间内容切换
function mainShow(title,src,order,height){
	if(src.indexOf("http") == -1) {
		src = basePath + src;
	}
	var tabBar=$("#tabBar");
	var tabBarUl=tabBar.find("ul");
	var main=$("#main");
	var tabItem=$("#tabItem-"+order);
	var mainItem=$("#main-"+order);
	
	$("nav li[data-order="+order+"]").addClass("navOn").siblings().removeClass("navOn");
	if(tabItem.length==0){
		//处理tabBar
		(function(tabBarUl,title,order){
			tabBarUl.find(".tabItem").removeClass("tabOn");
			tabBarUl.append(getTabLi(title,order));
		})(tabBarUl,title,order);
		
		//处理main
		(function(main){
			main.find(".webContain").addClass("none");
			main.append(getIframe(src,order,height));
		})(main);
	}else{
		tabItem.addClass("tabOn").siblings().removeClass("tabOn");
		mainItem.removeClass("none").siblings().addClass("none");
	}
	$("nav").find("li").removeClass("navOn").filter("[data-order="+order+"]").addClass("navOn");
	menu_order=order;
}
function tabItem(order,obj){
	var obj=$(obj);
	var mainThis=$("#main-"+order);
	
	$("nav").find("li").removeClass("navOn").filter("[data-order="+order+"]").addClass("navOn");
	obj.addClass("tabOn").siblings().removeClass("tabOn");
	mainThis.removeClass("none").siblings().addClass("none");
}
function tabItemClose(order,obj){
	var e = arguments.callee.caller.arguments[0] || window.event; 
	if(window.event){
		window.event.cancelBubble = true;
	}else{
		e.stopPropagation();
	}
	
	var obj=$(obj).closest(".tabItem");
	var mainThis=$("#main-"+order);
	//var navObj=$("nav li[data-order="+order+"]");
	
	var objPrev=obj.prev();
	var mainThisPrev=mainThis.prev();
	
	if(obj.hasClass("tabOn")){
		objPrev.addClass("tabOn");
		mainThisPrev.removeClass("none");
		
		$("nav li[data-order="+objPrev.attr("data-order")+"]").addClass("navOn").siblings().removeClass("navOn");
	}
	obj.remove();
	mainThis.remove();
	return false;
	
}
function getTabLi(title,order){
	return '<li class="tabItem tabOn" id="tabItem-'+order+'" data-order="'+order+'" onclick="tabItem(\''+order+'\',this)"><div class="tabCenter"><div class="tabRight"><div class="tabLeft"><span class="tab-close" onclick="tabItemClose(\''+order+'\',this)">x</span><span class="tab-ico"></span> <span class="tab-text">' + title + '</span></div></div></div></li>';
}
function getIframe(src,order,height){
	var arr=['intoCussWorkOrderAdd.do'];
	var leng=arr.length;
	var s="";
	for(var i=0;i<leng;i++){
		if(src.indexOf(arr[i])>-1){
			s='onload="iFrameHeight(\'iframe-'+order+'\')"';
			break;
		}
	}
	var iframeObj='<div class="webContain main-'+order+'" id="main-'+order+'"><iframe id="iframe-'+order+'" '+s+' src="'+src+'" width="100%" height="'+height+'" frameborder="0" scrolling="no"></iframe></div>';
	return iframeObj;
}
function navShow(title,src,order,obj,height){
	$(obj).addClass("navOn").siblings().removeClass("navOn");
	mainShow(title,src,order,height);
}

function addFavorite() {
    try {
        window.external.addFavorite(window.location.href, document.title);
    } catch (e) {
    	try {
    		window.sidebar.addPanel(document.title, window.location.href, "");
    	} catch (e) {
    		alert("抱歉，由于chrome,safari,opara浏览器还未支持自动收藏,请使用Ctrl+D进行添加!");
    	}
    }
}

function otherSysLogout(){
	var i=0;
	function ajaxFun(){
		$.ajax({
			async: true,
	        url:outURL[i] + "/init/destorySession.do",
	        timeout: 2000,
	        dataType:"jsonp",
	        jsonp:"jsonpcallback",
	        data: {},
	        success:function(data){
	        },
	        error:function(data,textstatus){
	        	if("timeout" == textstatus){
	        		i = 5000;
	        	}
		     },
		     complete:function(XMLHttpRequest, textStatus){
		        	if(i<outURL.length){
			        	i++;
			        	ajaxFun();
		        	}else{
		        		if(i == 5000){
		        			diaAlert2("error","抱歉，关联系统退出异常，请以关闭游览器的方式退出！");
		        			$(".aui_close").hide();
		        		} else {
			        		var logoutUrl = basePath + '/common/loginout.do';	
			        		window.location.href = logoutUrl;
		        		}
		        	}
		     }
		});
	}
	ajaxFun();
}

function logout() {
	otherSysLogout();
}































