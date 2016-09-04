	var setting = {
			async: {
				enable: true, //启用异步加载
				url:pathURL, //调用的后台的方法
				autoParam:["idFaqCategory"],  //向后台传递的参数
				otherParam:{}, //带参数值
				type:'get',
				dataFilter: ajaxDataFilter
			},
				
			data : {
				key:{
					name:"categoryName"//zTree 节点数据保存节点名称的属性名称
				}
			},
			callback: {
				beforeClick:beforeClick,
				onRightClick:OnRightClick//,
//				beforeAsync:zTreeBeforeAsync//,
//				onAsyncSuccess: zTreeOnAsyncSuccess//,
//				beforeExpand: zTreeBeforeExpand,
//				onExpand: zTreeOnExpand
			}
	};
	//对异步加载 Ajax返回数据进行预处理的函数
	function ajaxDataFilter(treeId, parentNode, responseData) {
//		alert("filter");
	    return responseData.data;
	}
	//点击节点触发的事件
	function beforeClick(treeId, treeNode, clickFlag) {
//		alert("beforeClick");
		//点击事件处理方法，可在其他js中调用
		treeNodeClick(treeId, treeNode, clickFlag);
	}
	
//	//右击节点触发的事件
	function OnRightClick(event, treeId, treeNode) {
		rightClkPopMenu(event, treeId, treeNode);
	}

//	//捕获异步加载之前的事件回调函数，zTree 根据返回值确定是否允许进行异步加载
//	function zTreeBeforeAsync(treeId, treeNode) {
//		alert("BeforeAsync");
//	}
//	//捕获异步加载正常结束的事件回调函数
//	function zTreeOnAsyncSuccess(event, treeId, treeNode, msg)  {
//		var treeObj = $.fn.zTree.getZTreeObj(treeId);
//		var nodes = treeObj.getNodes();
//		for(var node in nodes){
//			if(nodes[node].depth == 1){
//				nodes[node].isParent = true;
//				treeObj.updateNode(nodes[node]);//显示父节点的图标
//			}
//		}
//	}
//	
//	var ztree;
//	//解析数据并初始化树
//	function parseDataAndInitTree(treeId,data){
//		$.fn.zTree.init($('#'+treeId), setting, parseData(data.data));
//		ztree = $.fn.zTree.getZTreeObj(treeId);
//	}
//	//解析数据
//	function parseData(data){
//		var treeNodes = [];
//		$.each(data,function(i,items) { 
//			for(var item in items){
//				var childVals = [];
//				for(var tmp in items[item]){
//					childVals.push({name:items[item][tmp].categoryName,id:items[item][tmp].idFaqCategory});
//				}
//				treeNodes.push({name:item.categoryName,value:item.idFaqCategory,children:childVals});
//			}
//	    });
//		return treeNodes;
//	}

	
	function initTreeNodes(treeId){
		//异步加载根节点，第3个参数zNodes可以设置为 null 或 []
		zTreeObj = $.fn.zTree.init($("#"+treeId), setting);
	}

	