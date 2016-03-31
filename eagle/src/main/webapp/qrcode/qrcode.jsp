<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  
  	<!--防止html缓存-->
	<meta http-equiv="cache-control" content="max-age=0" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />
	<meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
	<meta http-equiv="pragma" content="no-cache" />
	<!--防止html缓存 end-->
	
    <base href="<%=basePath%>">
    
    <title>毒药下载地址</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
  <body>
  <div id="J-c-content" align=center style="margin-top:30px;">
  </div>
  <div id="J-tip" style="text-align:right;"></div>

    <script type="text/javascript">
	var browser = {
	versions: function () {
	var u = navigator.userAgent, app = navigator.appVersion;
	return { //移动终端浏览器版本信息
	ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
	android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
	iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
	iPad: u.indexOf('iPad') > -1, //是否iPad
	};
	}(),
	}
	if (browser.versions.iPhone || browser.versions.iPad || browser.versions.ios) {
		if(is_weixin()){
			getEle('J-tip').innerHTML = '<img src="/images/tip.png" />';
		}else{
			window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.mfma.poison&g_f=991653";
			//"https://itunes.apple.com/us/app/du-yao/id917583287?l=zh";
		}
	}else if (browser.versions.android) {
	window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=com.mfma.poison&g_f=991653";
	//"http://mobile.baidu.com/#/item?docid=7753622";
	}else{
		getEle('J-c-content').innerHTML = '<img src="/images/web1.png"  style="width: 580px;height: 580px;top: 200px;margin:0 auto;" />';
	}

	function getEle(id){
		return document.getElementById(id);
	}

	function is_weixin(){
        var ua = navigator.userAgent.toLowerCase();
        if(ua.match(/MicroMessenger/i)=="micromessenger") {
            return true;
        } else {
            return false;
        }
    }

	</script>
  </body>
</html>
