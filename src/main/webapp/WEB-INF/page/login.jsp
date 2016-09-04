<%@ page language="java" contentType="text/html; charset=UTF-8"       
    pageEncoding="UTF-8"%>  
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path;
%>         
<!DOCTYPE html>
<html>       
	<head>
		<meta charset="utf-8">
		<title>客服工单管理系统</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<link rel="icon" href="<%=basePath %>/favicon.ico" type="image/x-icon"/>  
		<link rel="shortcut icon" href="<%=basePath %>/favicon.ico" type="image/x-icon" />
		<link href="<%=basePath %>/animated_favicon.gif" rel="icon" type="image/gif" >  
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>/css/login.css"/>
		<script type="text/javascript">
			if (window.top !== window.self) {
				window.top.location.href = window.self.location.href;
			}
			var sessionId = '${sessionId}';
			var outURL = ${outURL};
		</script>
	</head>
    <body>
	 <div id="loginWrap">
	 	<div id="loginLogo"></div>
	 	<div id="loginFormWrap">
	 		<div id="loginAd"></div>
	 		<form id="loginForm" method="POST">
	 			<div id="errorMessage"></div>
	 			<div class="loginLine pr clearfix">
	 				<label>用户名：</label><input id="j_username" name="j_username" type="text" class="loginInput loginName" maxlength="16"/><span class="icon_username"></span>
	 			</div>
	 			<div class="loginLine pr clearfix">
	 				<label>密码：</label><input id="j_password" name="j_password" type="password" class="loginInput loginPwd" maxlength="16"><span class="icon_pwd"></span>
	 			</div>
	 			<div class="loginLine clearfix">
	 				<label>验证码：</label><input type="text" id="veryCode" name="validateCode" class="loginInput loginCode" maxlength="4"/><img id="verifyCodeImgId" class="hand" title="点击刷新验证码" onclick="changeVerifyCode()" src="<%=basePath %>/servlet/VerifyCodeServlet" width="84" height="24"/>
	 			</div>
	 			<div class="loginLine clearfix">
	 				<button type="submit" class="loginBtn">登&nbsp;&nbsp;录</button>
	 			</div>
	 			<div id="info" style="text-indent:145px;line-height:40px;"></div> 
	 		</form>
	 	</div>
	 	<div class="footer">
	 		<div class="copyright">
	            <p class="cp_txt">版权所有 © 中国平安保险（集团）股份有限公司 未经许可不得复制、转载或摘编，违者必究!<br>Copyright © PING AN INSURANCE (GROUP) COMPANY OF CHINA ,LTD. All Rights Reserved</p>
	        </div>
	 	</div>
	 </div>
	</body>	      
	<script type="text/javascript">
		var CONTEXT_PATH = '<%=basePath %>';
		var getFullURL = function(subUrl){
		    var estimatingURL = null;
		    if (!subUrl){
		        estimatingURL = CONTEXT_PATH + "/";
		    }else if (subUrl.substring(0, 1) != "/"){ 
		        estimatingURL = CONTEXT_PATH + "/" + subUrl;
		    }else {
		        estimatingURL = CONTEXT_PATH + subUrl;
		    }
		    return estimatingURL;
		};
	</script>
	<script src="<%=basePath %>/js/jquery.min.js"></script>
	<script src="<%=basePath %>/js/common.js"></script>
	<script src="<%=basePath %>/js/login.js"></script>	
</html>
