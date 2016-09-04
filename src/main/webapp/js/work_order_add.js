/**
 * 工单主页js
 */

$(document).ready(function() {
	initEditor();
	initCussWorkOrderType();
	initHandler();
});

var editor;
var editor2;
/**
 * 初始化文本编辑器
 */
function initEditor() {
	var editorJosn={
			cssPath: basePath +'/js/plugin/kindeditor/plugins/code/prettify.css',
			uploadJson: basePath +'/js/plug/kindeditor/jsp/upload_json.jsp',
			fileManangetJson: basePath +'/js/plug/kindeditor/jsp/file_manager_json.jsp',
			resizeType : 1,
			allowPreviewEmoticons : false,
			allowImageUpload : false,
			items : [
				'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
				'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
				'insertunorderedlist', '|', 'emoticons', 'link']
	};
	KindEditor.ready(function(K) {
		editor = K.create('textarea[name="problemDescribe"]', editorJosn);
		editor2 = K.create('textarea[name="suggest"]', editorJosn);
	});
}

/**
 * 初始化工单类型
 */
function initCussWorkOrderType() {
	var parentTypeId=$('#parentTypeId').val();
	var params={'parentType':parentTypeId,'adm':new Date().getTime()};
	$.ajax({
	    type: 'get',
	    url: basePath + '/executive/workorder/InitCussWorkOrderType',
	    data:params,
	    dataType: 'json',
	    success: function(data) {
	    	if(data !=''){
	    		var zNodes = eval(data);
	    		$("#parentTypeId option").remove();
	    	    $("#parentTypeId").append("<option value=''>请选择</option>");
	    	    var length=1;
	    		$(zNodes).each(function (key) {
	    			length=length+1;
	    			var id = zNodes[key].id;
	    			var name = zNodes[key].name;	    			
	    			$("#parentTypeId").append("<option value='" + id + "'>"+ name + "</option>");	    								
	    		});
	    	}  
	     },
	     error:function(data,textstatus){} 
	});
}

/**
 * 根据工单类型查询工单类型细分
 */
function InitSubType(parentId){	
	$("#subTypeId option").remove();
    $("#subTypeId").append("<option value=''>请选择</option>");
	var params={'parentType':parentId.split('@')[0],'adm':new Date().getTime()};
	if(parentId !=''){
		$.ajax({
		    type: 'get',
 		    url: basePath + 'executive/workorder/InitCussWorkOrderType',
		    data:params,
		    dataType: 'json',
		    success: function(data) {
		    	if(data !=''){
		    		var zNodes = eval(data);
		    		$("#subTypeId option").remove();
		    	    $("#subTypeId").append("<option value=''>请选择</option>");
		    	    var length=1;
		    		$(zNodes).each(function (key) {
		    			length=length+1;
		    			var id = zNodes[key].id;
		    			var name = zNodes[key].name;	 	   			
		    			$("#subTypeId").append("<option value='" + id + "'>"+ name + "</option>");	    								
		    		});
		    	}  
		     },
		     error:function(data,textstatus){} 
		});		
	}
}

/**
 * 初始化轮询人员
 */
function initHandler() {
	var params = {'adm':new Date().getTime()};
	$.ajax({
	    type: 'get',
	    url: basePath + 'executive/workorder/InitHandler',
	    data:params,
	    dataType: 'json',
	    success: function(data) {
	    	var result=eval(data);
	    	$("#workOrderHandlerUm option").remove();
	    	if(curOprRole == 'TEL_CUSTOMER'){
	    		$("#workOrderHandlerUm").append("<option value=''>默认轮询分配</option>");
	    	    var length=1;
	    		$(result).each(function (key) {
	    			length=length+1;
	    			var id = result[key].userUMID;
	    			var name = result[key].userName;	    			
	    			$("#workOrderHandlerUm").append("<option value='" + id + "'>"+ id + "(" + name + ")" + "</option>");	    								
	    		});	    		
	    	}else{
	    	    var length=0;
	    		$(result).each(function (key) {
	    			length=length+1;
	    			var id = result[key].userUMID;
	    			var name = result[key].userName;
	    			if(curOpr == id)$("#workOrderHandlerUm").append("<option selected value='" + id + "'>"+ id + "(" + name + ")" + "</option>");
	    			else $("#workOrderHandlerUm").append("<option value='" + id + "'>"+ id + "(" + name + ")" + "</option>");	    								
	    		});	    		
	    	}

	    },
	     error:function(data,textstatus){} 
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

function cussWorkOrderAdd(currentPage){
	editor.sync();
//	editor2.sync();
	if($("#parentTypeId").val()==''){
		parent.diaAlert('warning','请选择工单类型');
		return false;
	}
	if($("#subTypeId").val()==''){
		parent.diaAlert('warning','请选择工单细分');
		return false;
	}	
	if($('#urgentPriority').val()==''){
		parent.diaAlert('warning','请选择优先级');
		return false;
	}
	if($('#cussOrderCode').val()==''){
		parent.diaAlert('warning','请输入订单号');
		return false;
	}	
	if($('#custName').val()==''){
		parent.diaAlert('warning','请输入订单号查询');
		return false;
	}
	if($('#custTel').val()==''){
		parent.diaAlert('warning','请输入订单号查询');
		return false;
	}
	if($('#problemDescribe').val()==''){
		parent.diaAlert('warning','请录入问题描述');
		return false;
	}else{
		if($.trim($('#problemDescribe').val()).length <10){
			parent.diaAlert('warning','问题描述不少于10个汉字');
			return false;
	    }		
		if($.trim($('#problemDescribe').val()).length > 300){
			parent.diaAlert('warning','问题描述不超过300个汉字');
			return false;
	    }
	} 	
	var params ={
			'adm':new Date().getTime(),
			'parentTypeCode':$("#parentTypeId").val().split('@')[1],
			'typeCode':$('#subTypeId').val().split('@')[1],
			'typeId':$('#subTypeId').val().split('@')[0],					
			'customerName':$("#custName").val(),
			'orderNum':$("#cussOrderCode").val(),
			'customerPhone':$("#custTel").val(),
			'issuDesc':$("#problemDescribe").val(),'currOperatorUm':$('#workOrderHandlerUm').val(),
			'feedbackToCustomer':$("#feedbackToCustomer").val(),'channelSource':$("#channelSource").val(),
			'probPicAccessPath':$("#probPicAccessPath").val(),'urgentPriority':$('#urgentPriority').val(),
			'serialNumber':$("#serialNumber").val()
	};
   $.ajax({
		    type: 'post',
		    url: basePath + 'answer/workorder/entering/add',
		    data: params,
		    dataType: 'json',
		    success: function(data) {
		    	if($.trim(data.code)=='000'){	
		    		parent.art.dialog({
		    		    id: 'confirm_id',
		    		    content: "添加工单已成功！",
		    		    lock:true,
		    		    close:function(){
		    		    	location.reload(true); 
		    		    },
		    		    button: [
		    		        {
		    		            name: '继续添加',
		    		            callback: function () {
		    		            	location.reload(true); 
		    		            },
		    		            focus: true
		    		        },
		    		        {
		    		            name: '进入查看',
		    		            callback: function () {
		    		            	location.reload(true); 
		    		            	parent.mainShow('我的录入工单',basePath+'answer/workorder/entered.do','10-110',780);
		    		            }
		    		        }
		    		    ]
		    		});
		    	}
		    	if($.trim(data.code)=='999'){
		    		parent.diaAlert('error',data.data);
		    	}		    	
		     },
		     error:function(data,textstatus){
		    	 parent.diaAlert('error',data);
			 } 
		});
}

/**根据订单号查询客户信息*/
function selectworkOrderCuss(){
	var cussOrderCode =$.trim($("#cussOrderCode").val());
	if(cussOrderCode==''){
		parent.diaAlert('warning','请输入订单号');
		return false;
	}
	var regex = /^[1-9][0-9]*$/;
	if(!regex.test(cussOrderCode)){
		parent.diaAlert('warning','请输入合法的订单号');
		return false;
	}	
	var params = {'adm':new Date().getTime(),'orderNo':cussOrderCode};
	$.ajax({
	    type: 'get',
	    url: basePath + 'answer/workorder/selectworkOrderCuss',
	    data:params,
	    dataType: 'json',
	    success: function(data) {
	    	if($.trim(data.code)=='000'){
	    		var result=$.parseJSON(data.data);
	    		$("#custName").val(result.mobileNum);
	    		$("#custTel").val(result.mobileNum);
	    	}
	    	if($.trim(data.code)=='999'){
	    		parent.diaAlert('warning',data.data);
	    	}
	    },
	    error:function(data,textstatus){} 
	});		
}
