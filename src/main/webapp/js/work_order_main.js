/**
 * 工单主页js
 */
$(document).ready(function() {
	if(userRole == "TEL_CUSTOMER") {
		requestUrl = basePath + "answer/workorder/" + optype + "/query.do";
	}else if(userRole == "LEADER_CUSTOMER") {
		requestUrl = basePath + "manage/workorder/" + optype + "/query.do";
	}else if(userRole == "EXE_CUSTOMER") {
		requestUrl = basePath + "executive/workorder/" + optype + "/query.do";
	}
	if(("undefined" != typeof specialOpt) && specialOpt == 'myAddOrder')requestUrl = basePath + "answer/workorder/" + optype + "/query.do";
	$.ajax({
	    type: 'post',
	    url: requestUrl,
	    data: {},
	    dataType: 'json',
	    success: function(data) {
	    	var myTemplate = Handlebars.compile($("#table-template").html());
	    	
	        //注册一个比较数字大小的方法compare
	        Handlebars.registerHelper("compare",function(v1,v2,options){
	          //判断v1是否比v2大
	          if(v1>v2){
	            //继续执行
	            return options.fn(this);
	          }else{
	            //执行else部分
	            return options.inverse(this);
	          }
	        });	
	        	        
	        //注册一个比较数字是否相等的方法compare2
	        Handlebars.registerHelper("compare2",function(v1,v2,options){
	          //判断v1是否等于v2
	          if(v1 != v2){
	            //继续执行
	            return options.fn(this);
	          }else{
	            //执行else部分
	            return options.inverse(this);
	          }
	        });		        
	        
	        //注册一个遍历方法list
	        Handlebars.registerHelper('list', function(v1,v2, v3,options) {   
	        	var out = ""; 
	        	var margin_left = "";
	        	for(var i=v1; i<=v2; i++) {
	        		if(i != v1) {
	        			margin_left = " style='margin-left:5px'";
	        		}
	        		if(v3 != i ){
	        		    out = out+"<span class='pageNum'"+margin_left+" onclick=\"queryCussWorkOrderNum("+i+")\">"+i+"</span>";
	        		}
	        		else out = out+"<span class='pageNum pageOn'"+margin_left+">"+i+"</span>";
	        	}
	        	return out ; 
	        }); 		              
	       //日期格式化   
	        Handlebars.registerHelper("showDateFormat", function(timestamp) {
	            return new Date(timestamp).Format("yyyy-MM-dd hh:mm:ss");
	       });
	        
	       //将json对象用刚刚注册的Handlebars模版封装，得到最终的html，插入到基础table中。
	       $('#tableList').html(myTemplate(data.data));
	       tipCss();
	     },
	     error:function(data,textstatus){} 
	});		
	initOrderType();
  });
var requestUrl;
var setting = {
	view: {
		showIcon: true,
		dblClickExpand: false,
		selectedMulti: false
	},
	data: {
		key:{
			name:"name"
		},
		simpleData: {
			enable: true,
			idKey: "id",
			pIdKey: "pId",
			rootPId: 0
		}
	},
	callback: {
		onClick: onClick
	}
};

var zNodes =[];
function onClick(e, treeId, treeNode) {
	$("#typeId").attr("value", treeNode.name);
	$("#typeId").attr("data-value", treeNode.id);
	//点击后隐藏
	hideMenu();
}

function showMenu() {
	var categoryObj = $("#typeId");
	var categoryOffset = $("#typeId").offset();
	$("#menuContent").css({left:categoryOffset.left + "px", "z-index":555,top:categoryOffset.top+1 + categoryObj.outerHeight() + "px"}).slideDown("fast");

	$("body").bind("mousedown", onBodyDown);
}

function hideMenu() {
	$("#menuContent").fadeOut("fast");
	$("body").unbind("mousedown", onBodyDown);
}

function onBodyDown(event) {
	if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(event.target).parents("#menuContent").length>0)) {
		hideMenu();
	}
}

//处理工单类型树形数据
function handCategory(result){
	//工单类型下拉选项
	var zNodes_data =[];
	if(result !=null && result.length >0){
   		var data1={};
		data1.name="全部";
		data1.id=0;
		data1.pId=0;
		data1.isLeaf =0;
		data1.iconOpen= basePath + "image/folder_open.png";
		data1.iconClose= basePath + "image/folder.png";
		zNodes_data[0]=data1;
		
		for(var i = 0;i < result.length;i++) {
     	var data = result[i];
		if(data.pId ==-1){
			data.iconOpen= basePath + "image/folder_open.png";
			data.iconClose= basePath + "image/folder.png";
			data.open=true;
		}else{
			data.icon= basePath + "image/folder_leaf.png";
		}
		zNodes_data[i+1] = data;
		}
	}
	zNodes = zNodes_data;		
}


function redirectAddPage(){
	parent.navShow('工单录入', basePath + 'answer/workorder/entering.do','01-01',this,780);
}

function initOrderType() {
	$.ajax({
        type: 'get',
        cache: false,
        dataType: 'json',
        url:  basePath + "executive/workorder/InitOrderTypeTree",
        success:function(result) {
         	handCategory(result);
 		    //类目数据
 		    if(zNodes && zNodes.length >0){
 			    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
 			    var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
 			    if(treeObj){
 			    	treeObj.expandAll(true);
 			    }
 		    }
		}
	});
}

//分页
function queryCussWorkOrderNum(currentPage){
	cussWorkOrderPageOp(currentPage);	
}

/**
 * 点按钮查询或分页事件	  
 */  
function cussWorkOrderPageOp(currentPage){
	var params ={
		 'currentPage':currentPage,
		 'adm':new Date().getTime(),
		 'typeId': $('#typeId').attr("data-value"),
		 'serialNumber': $('#serialNumber').val(),
		 'orderNum': $('#orderNum').val(),
		 'customerName': $('#customerName').val(),
		 'urgentPriority': $('#urgentPriority').val(),
		 'creator': $('#creator') ? $('#creator').val() : null,
		 'workOrderStatus': $('#workOrderStatus') ? $("#workOrderStatus").val() : null,
		 'lastUpdatorUm': $('#lastUpdatorUm') ? $('#lastUpdatorUm').val() : null,
		 'channelSource': $('#channelSource') ? $("#channelSource").val() : null,
		 'createTimeTo': $('#createTimeTo').val(),
		 'createTimeFrom': $('#createTimeFrom').val(),
		 'flowTimeFrom': $('#flowTimeFrom') ? $('#flowTimeFrom').val() : null,
		 'flowTimeTo': $('#flowTimeTo') ? $('#flowTimeTo').val() : null
	};
    $.ajax({
	    type: 'post',
	    url: requestUrl,
	    data: params,
	    dataType: 'json',
	    success: function(data) {
	    	var myTemplate = Handlebars.compile($("#table-template").html());
	        //将json对象用刚刚注册的Handlebars模版封装，得到最终的html，插入到基础table中。
	        $('#tableList').html(myTemplate(data.data));
	        tipCss();
	     },
	     error:function(data,textstatus){
	    	 
		 } 
	});	
}

/**
 * 删除事件
 * @param idWorkOrder	  
 */  
function delCussWorkOrder(idWorkOrder){
	var params ={'workOrderId':idWorkOrder};
	parent.art.dialog({
	    id: 'confirm_id',
	    content: "确定删除该记录吗？",
	    lock:true,
	    close:function(){
	    	parent.art.dialog.close();
	    },
	    button: [
	        {
	            name: '确定',
	            callback: function () {
	        	    $.ajax({
	    			    type: 'post',
	    			    url: basePath+'/answer/workorder/cussWorkOrderDelt.do',
	    			    data: params,
	    			    dataType: 'text',
	    			    success: function(data) {			    			    				    	
	    			    	if($.trim(data)=='000')cussWorkOrderPageOp(1);
	    			    	else parent.diaAlert('error','操作失败！');		    	
	    			     },
	    			     error:function(data,textstatus){
	    			    	 parent.diaAlert('error','操作失败！');
	    				 } 
	    			});	
	            },
	            focus: true
	        },
	        {
	            name: '取消',
	            callback: function () {
	            	parent.art.dialog.close(); 	            	
	            }
	        }
	    ]
	});	
}


function tipCss(){
    $('.tipss').simpletooltip({
        themes: {
            pink: {
                color: 'red',
                border_color: 'red',
                background_color: 'pink',
                border_width: 4
            },
            brazil: {
                color: 'yellow',
                background_color: 'green',
                border_color: 'yellow',
                border_width: 4
            }
        }
		});	
}

/**
 * 进入处理页
 * @param idWorkOrder
 */
function toDealPage(idWorkOrder,opType){
	parent.mainShow('工单处理',basePath + 'executive/workorder/intodeal.do?workOrderId='+idWorkOrder+"&opType="+optype,'10-1000',780);	  
}

/**
 * 进入客户经理处理页
 * @param idWorkOrder,status
 */
function toManagerDealPage(idWorkOrder,status){	
	parent.mainShow(status=='8'?"工单查看":"工单处理",basePath + 'manage/workorder/intodeal.do?workOrderId='+idWorkOrder,'10-1010',780);	  
}

/**
 * 进入查看页
 * @param idWorkOrder
 */
function toShowPage(idWorkOrder,opType){
	parent.mainShow('工单查看',basePath + 'executive/workorder/intoShow.do?workOrderId='+idWorkOrder,'10-1100',780);	  
}
