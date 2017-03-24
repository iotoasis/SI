<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="true"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Setting</title>
	<%@ include file="/WEB-INF/views/mobile/common/common_head.jsp"%>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script type="text/javascript" src="https://www.google.com/jsapi"></script>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
<!-- <link href="/hdm/v2/css/noUiSlider/nouislider.css" rel="stylesheet">
<link href="/hdm/v2/css/noUiSlider/nouislider.min.css" rel="stylesheet"> -->

<script type="text/javascript">
var contextPath = "${pageContext.request.contextPath}";
$(document).ready(function() {
	initUI();
})

</script>

<script src="${pageContext.request.contextPath}/mobile/js/herit/hdb.js"></script>
<script src="${pageContext.request.contextPath}/mobile/js/herit/dm.js"></script>
<script src="${pageContext.request.contextPath}/mobile/js/herit/mobileDeviceSetting.js"></script>
<script src="${pageContext.request.contextPath}/mobile/js/herit/updateSetting.js"></script>
<script src="${pageContext.request.contextPath}/mobile/js/herit/settingMap.js"></script>
<%-- <script src="${pageContext.request.contextPath}/mobile/js/herit/nouislider.js"></script>
<script src="${pageContext.request.contextPath}/mobile/js/herit/nouislider.min.js"></script> --%>
</head>

<body>
<div id="wrapper">

	<nav class="navbar-default navbar-static-side" role="navigation">
        <div class="sidebar-collapse">
        	
        	<%@ include file="/WEB-INF/views/mobile/common/common_sidemenu.jsp"%>
        	
        </div>
    </nav>
    
    <div id="page-wrapper" class="gray-bg">
    	<div class="row border-bottom">
 
        	<%@ include file="/WEB-INF/views/mobile/common/common_topbar.jsp"%>
   	
        </div>
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<select name="deviceModel" id="search_deviceModel" value="${param.deviceModel}">
							<option value="choose">등록 디바이스</option>
							<c:forEach items="${deviceModelList}" var="deviceModel">
								<option value="${deviceModel.id}|${deviceModel.oui}|${deviceModel.modelName}"														
								<c:if test="${fn:contains(param.deviceModel, deviceModel.modelName)}">selected</c:if>
								>${deviceModel.manufacturer}-${deviceModel.modelName}
								</option>
							</c:forEach>			
						</select>
					</div>
				</div>
			</div>
		
		
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>자율적 상황판단 제어 설정</h5>
						</div>
						<div class="ibox-content">
							<input type="checkbox" class="js-switch" /><span style="font-weight: bold;"> 자율적 상황판단 제어</span><br /><br />
							<input type="checkbox" class="js-switch2" /><span style="font-weight: bold;"> 이벤트 Push 알림</span>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5>센서 데이터 업데이트 설정</h5>
						</div>
						<div class="ibox-content">
							<input type="checkbox" class="js-switch3" /><span style="font-weight: bold;"> 센서 데이터 업데이트</span><br /><br />
							<input type="checkbox" class="js-switch4" /><span style="font-weight: bold;"> 업데이트 주기 설정 (default : 10초)</span><br /><br />
							
							
							<div id="slider"></div>
							<p id="sliderInfo">
							  <label for="amount">(1sec increments) :</label>
							  <input type="text" id="amount" readonly />
							  <input type="hidden" id="changevalue" />
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
		
	    <div class="footer">
	        
	        <%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
	        
	    </div>
	    
    </div>
</div>

<%@ include file="/WEB-INF/views/mobile/common/common_js.jsp"%>

<!-- Custom and plugin javascript -->
<script src="/hdm/v2/js/inspinia.js"></script>
<script src="/hdm/v2/js/plugins/pace/pace.min.js"></script>

</body>

</html>
		