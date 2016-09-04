<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>     
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>主页</title>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%=basePath %>css/display.css"/>
		<!--[if lt IE 9]><script src="js/html5.js"></script><![endif]-->
		<script src="<%=basePath %>js/jquery.min.js"></script>
		<script src="<%=basePath %>js/plugin/artDialog/artDialog.source.js?skin=chrome"></script>
		<script src="<%=basePath %>js/plugin/artDialog/plugins/iframeTools.source.js"></script>
		<script src="<%=basePath %>js/common.js"></script>
		
	</head>
	<body style="height:800px;background:#fff;">
		<img src="<%=basePath %>image/homeBG.jpg" style="margin-top:50px;" width="100%">
	</body>
</html>