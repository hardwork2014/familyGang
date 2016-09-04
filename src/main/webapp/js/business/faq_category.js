	var ztree;
	var zTreeObj;
	var pathURL = "../../manager/faq/category/query";
	$(document).ready(function(){
		
		initTreeNodes("treeDemo");
		
	});		
	
	function treeNodeClick(treeId, treeNode, clickFlag){
		alert("click");
		$.ajax({
			  type:'get',
			  url:"../../manager/faq/show",
			  data:{"levels":treeNode.levels},
			  success:function(data){
				childNodes = data.data;
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				treeObj.addNodes(treeNode, childNodes);
			  },
			  complete:function(){
//				  siderTree();
			  }
		   });
	}
	
	//右击节点弹出操作菜单
	function rightClkPopMenu(event, treeId, treeNode){
		var treeObj = $.fn.zTree.getZTreeObj(treeId);
		treeObj.selectNode(treeNode);
		if(treeNode.depth == 3){//3级类目需要可以添加文章
			$("#addLeaf").css({"display":"none"});
			$("#addFAQ").css({"display":"block"});
		} else {//1,2级类目需要可以子类目
			$("#addLeaf").css({"display":"block"});
			$("#addFAQ").css({"display":"none"});
		}
		if(treeNode) {
			$("#menu").popupSmallMenu({
				event : event,
				onClickItem  : function(item) {//点击菜单触发的事件
					clickItem(treeNode,item);
				}
			});
		}	 
	}
	function clickItem(treeNode,item){
		alert(item);
		
	}
	
	
	//捕获父节点展开之前的事件回调函数，并且根据返回值确定是否允许展开操作
	function zTreeBeforeExpand(treeId, treeNode){
		alert("BeforeExpand"+treeId+treeNode);
	}
	
	function zTreeOnExpand(event, treeId, treeNode) {
	    alert("onExpand"+treeId+treeNode);
		var id = treeNode.idFaqCategory;
		$.ajax({
			  type:'get',
			  url:pathURL,
			  data:{"id":id},
			  success:function(data){
				childNodes = data.data;
				var treeObj = $.fn.zTree.getZTreeObj(treeId);
				treeObj.addNodes(treeNode, childNodes);
			  },
			  complete:function(){
//				  siderTree();
			  }
		   });
	};

		
		
