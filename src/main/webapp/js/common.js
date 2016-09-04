//type:有三种型号，succeed、warning、error
function diaAlert(type,text){
	if(type!='succeed'&&type!='warning'&&type!='error'){
		type='warning';
	}
	art.dialog({
		lock:true,
		fixed:true,
		resize:false,
		daration:0,
		time: 3,
		icon:type,
		title:"系统提示",
		content:text
	});
}

//弹框中有提示时，关掉提示时，再关弹框
//type:有三种型号，succeed、warning、error
function diaAlert2(type,text){
	if(type!='succeed'&&type!='warning'&&type!='error'){
		type='warning';
	}
	art.dialog({
		lock:true,
		fixed:true,
		resize:false,
		daration:0,
		icon:type,
		title:false,
		content:text,
		close:function(){
			art.dialog.close();
		}
	});
}

//弹框中有提示时，关掉提示时，再回调fn函数
//type:有三种型号，succeed、warning、error
function diaAlert3(type,text,fn){
	if(type!='succeed'&&type!='warning'&&type!='error'){
		type='warning';
	}
	art.dialog({
		lock:true,
		fixed:true,
		resize:false,
		daration:0,
		icon:type,
		title:false,
		content:text,
		close:function(){
			fn();
		}
	});
}

function diaWin(url, title, width, height){
	if(!width) {
		width = 600;
	}
	if(!height) {
		height = 500;
	}
	art.dialog.open(url,{
		id:'diaWinId',
		title:title,
		lock:true,
		resize:false,
		duration:0,
		width:width,
		height:height,
		close:function () {
			document.getElementById('iframe-'+menu_order).contentWindow.location.reload(true);
		}
		},false);
}
