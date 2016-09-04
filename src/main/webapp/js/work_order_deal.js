/**
 * 工单处理页js
 */

function initHanler(){
	var params={'appoint':'appoint','adm':new Date().getTime()};
	$.ajax({
	    type: 'get',
	    url: basePath+'executive/workorder/InitHandler',
	    data:params,
	    dataType: 'json',
	    success: function(data) {
	    	var result=eval(data);
	    	$("#assign_panel_ul li").remove();
    		$(result).each(function (key) {
    			var id = result[key].userUMID;
    			var name = result[key].userName;	    			
    			$("#assign_panel_ul").append("<li onclick=\"appointHandlerUm('"+id+"')\" value='" + id + "'>"+ id + "(" + name + ")" + "</li>");	    								
    		});
	    },
	     error:function(data,textstatus){} 
	});	
}

function appointHandlerUm(userUMID){
	$('#workOrderHandlerUm').val(userUMID);
	var params =getParam('appoint');		
	   $.ajax({
			    type: 'post',
			    url: basePath+'executive/workorder/deal',
			    data: params,
			    dataType: 'text',
			    success: function(data) {
			    	if($.trim(data)=='000'){
			    	//	location.reload(true);
			    		//指派成功，跳转到指派成功页面
			    		var url=basePath+'executive/workorder/intoAppoint.do';
			    		//$(parent.document.getElementById("tabItem-"+parent.menu_order)).find(".tab-close")[0].click();
			    		$(parent.document.getElementById("iframe-" + parent.menu_order)).attr("src", url);
			    	}
			    	else{
			    		diaAlert('error','操作失败！');
			    	};		    	
			     },
			     error:function(data,textstatus){
			    	 alert("error:"+data);
			     }
			});	
}

function queryDealRecords(){
	var params = {'idWorkOrder':$("#idWorkOrder").val(),'adm':new Date().getTime()};
	$.ajax({
	    type: 'get',
	    url: basePath+'executive/workorder/queryDealRecords',
	    data:params,
	    dataType: 'json',
	    success: function(data) {
	    	var myTemplate = Handlebars.compile($("#table-template").html());
	    	
	        //注册一个比较数字大小的方法compare
	        Handlebars.registerHelper("compare",function(v1,v2,options){
	          if(v1>v2){
	            return options.fn(this);
	          }else{
	            return options.inverse(this);
	          }
	        });	
	        	        
	        //注册一个比较数字是否相等的方法compare2
	        Handlebars.registerHelper("compare2",function(v1,v2,options){
	          if(v1 != v2){
	            return options.fn(this);
	          }else{
	            return options.inverse(this);
	          }
	        });		        
	        
	        //注册一个遍历方法list
	        Handlebars.registerHelper('list', function(v1,v2, v3,options) {   
	        	var out = ""; 
	        	for(var i=v1; i<=v2; i++) {
	        		if(v3 != i ){
	        		    out=out+"&nbsp;<span   onclick=\"queryDealRecordsNum("+i+")\">"+i+"</span>";
	        		}
	        		else out=out+"  "+"<span >"+i+"</span>";
	        	}
	        	return out ; 
	        }); 		              	    	       
		    //角色显示名称  
	        Handlebars.registerHelper("showRoleName", function(creatorRole) {
	            if(creatorRole=='EXE_CUSTOMER')return "客服专员";
	            if(creatorRole=='LEADER_CUSTOMER')return "客服管理员";
	            if(creatorRole=='TEL_CUSTOMER')return "电话接听员";
	            return "客户";
	        });	        
		    //日期格式化   
	        Handlebars.registerHelper("showDateFormat", function(timestamp) {
	            return new Date(timestamp).Format("yyyy-MM-dd hh:mm:ss");
	       });	        
	        
	        $('#replyList').html(myTemplate(data));
	        var max=$("html").height()>$("body").height()?$("html").height():$("body").height();
	        $(parent.document.getElementById("iframe-" + parent.menu_order)).height(max+200);
	        //parent.document.getElementById("iframe-01-01").style.height=document.body.scrollHeight; 
	     },
	     error:function(data,textstatus){} 
	});
}

$(document).ready(function() {	
	initHanler();	
	queryDealRecords();
	$("#operateFlowLogDiv").hide();
	$("#add_record_btn").bind('click',function(event){
		hidePanel();
		event.stopPropagation();
		$("#operateFlowLogDiv").show();
		editor = KindEditor.create('textarea[name="suggest"]', editorJosn);
	});
	
	$('#operateFlowLogDiv').delegate('#cancle_btn', 'click', function() { 
		KindEditor.remove('textarea[name="suggest"]');
		$("#operateFlowLog").val('');
		$("#operateFlowLogDiv").hide();
	});
	
	$("#assign_btn").bind('click',function(event){
		hidePanel();
		event.stopPropagation();
		$("#assign_panel").show();
	});
	$("#urgent_level_btn").bind('click',function(event){
		hidePanel();
		event.stopPropagation();
		$("#urgent_level_panel").show();
	});
	$("body").bind('click',function(){
		hidePanel();
	});
});

function hidePanel() {
	$("#assign_panel").hide();
	$("#urgent_level_panel").hide();
}


//分页
function queryDealRecordsNum(currentPage){
	queryDealRecordsPageOp(currentPage);	
}

/**
 * 点按钮查询或分页事件	  
 */  
function queryDealRecordsPageOp(currentPage){
	 var params ={'currentPage':currentPage,
			 'adm':new Date().getTime(),
			 'idWorkOrder':$("#idWorkOrder").val()
	 };	
    $.ajax({
		    type: 'post',
		    url: basePath+'executive/workorder/queryDealRecords',
		    data: params,
		    dataType: 'json',
		    success: function(data) {
		    	var myTemplate = Handlebars.compile($("#table-template").html());
		        $('#replyList').html(myTemplate(data));
		     },
		     error:function(data,textstatus){
		    	 
			 } 
		});	
}

/***** 获取web工程根路径 ********/
function getBasePath(){
	var urlAll=window.location.href;
	var urlHost="http://"+window.location.host+"/";
	var temp=urlAll.substr(urlHost.length); //获取域名后面的位置
	temp=temp.substr(0,temp.indexOf("/")); //截取工程名
	urlHost=urlHost+temp+"/";
	return urlHost;
}

function getParam(op,value){
	var params ={
			'adm':new Date().getTime(),
			'workOrderTypeId':$('#typeIdHidden').val(),
			'custName':$("#custName").val(),'cussOrderCode':$("#cussOrderCode").val(),
			'custTel':$("#custTel").val(),'issuDesc':$("#problemDescribe").val(),'workOrderHandlerUm':$('#workOrderHandlerUm').val(),
			'feedbackToCustomer':$("#feedbackToCustomer").val(),'channelSource':$("#channelSourceHidden").val(),
			'probPicAccessPath':$("#probPicAccessPath").val(),'workOrderCode':$("#cussOrderCode").val(),
			'idWorkOrder':$("#idWorkOrder").val(),'operateFlowLog':op=='submit'?editor.html():$('#operateFlowLog').val(),'urgentPriority':value,
			'dealType':op,'workOrderStatus':$("#workOrderStatus").val()
	};
	return params;
}

/**
 * 工单处理
 * @param op
 */
function cussWorkOrderDeal(op,value){
	$('#dealType').val(op);
	if(op=='submit'){
		editor.sync();
		if(editor.isEmpty()){
			parent.diaAlert('warning','请填写处理日志');
			return;			
		}else{
			if($.trim(editor.html()).length <10){
				parent.diaAlert('warning','处理日志不少于10个汉字');
				return false;
		    }		
			if($.trim(editor.html()).length > 150){
				parent.diaAlert('warning','处理日志不超过150个汉字');
				return false;
		    }
		}		
	}
	if(op=='appoint'){
		return;	
	}	
	if(op=='closed' || op=='applyBack' || (op=='completed' && $('#workOrderStatus').val()=='6') || op=='unapproved'||op=='reOpen'){
		parent.diaWin(basePath+'executive/workorder/intofeedback',"反馈信息",800,250);
		return;
	}
	
	var params =getParam(op,value);	
	
	var url=basePath+"executive/workorder/deal";
	if(op=='approved'){
		url=basePath+"manage/workorder/deal";
	}
	
   $.ajax({
		    type: 'post',
		    url: url,
		    data: params,
		    dataType: 'text',
		    success: function(data) {
		    	if($.trim(data) =='000'){
		    		location.reload();
		    	}else{
		    		diaAlert('error','操作失败！');
		    	};		    	
		     },
		     error:function(data,textstatus){
		    	 alert("error:"+data);
		     }
		});
}
