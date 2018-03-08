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
<title>List</title>
	<%@ include file="/WEB-INF/views/mobile/common/common_head.jsp"%>
<script language="javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="https://www.google.com/jsapi"></script>

<script type="text/javascript">
var contextPath = "${pageContext.request.contextPath}";
$(document).ready(function() {
	initUI();
})
</script>

<script language="javascript" src="${pageContext.request.contextPath}/mobile/js/herit/hdb.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/mobile/js/herit/dm.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/mobile/js/herit/mobileDeviceList.js"></script>
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
					<div class="ibox float-e-margins mobile_box">
						<div class="ibox-title">
							<h5>센서 데이터 정보</h5>
						</div>
						<div class="ibox-content">
							<table class="table" id="infoTable">
							
							</table>
						</div>
					</div>
				</div>
			</div>
		
			<div class="row control_box">
				<div class="col-lg-4">
					<div class="ibox float-e-margins mobile_control_box">
						<div class="ibox-title">
							<h5>액추에이터 제어 - 밸브 ON/OFF</h5>
						</div>
						<div class="ibox-content">
							<input type="checkbox" class="js-switch2" />
						</div>
					</div>
				</div>
				
				
				<div class="col-lg-4">
					<div class="ibox float-e-margins mobile_control_box">
						<div class="ibox-title">
							<h5>액추에이터 제어 - 밸브</h5>
						</div>
						<div class="ibox-content">
							<div class="m-r-md inline">
	                        	<!-- <input type="text" value="0" class="dial m-r-sm" data-fgColor="#1AB394" data-width="100" data-height="100" /> -->
	                        </div>
							<!-- <input type="checkbox" class="js-switch" /> -->
							<!-- <div id="donut_single" style="width: 265px; height: 200px;"></div> -->
						</div>
					</div>
				</div>
				
				
				<div class="col-lg-4">
					<div class="ibox float-e-margins mobile_control_box">
						<div class="ibox-title">
							<h5>액추에이터 제어 - 펌프</h5>
						</div>
						<div class="ibox-content">
							<input type="checkbox" class="js-switch" />
						</div>
					</div>
				</div>
			</div>
			<!-- test test test test test test test test -->
			<!-- <div class="row">
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
							 <div class="text-center">
								<div class="m-r-md inline">
	                            	<input type="text" value="75" class="dial m-r-sm" data-fgColor="#1AB394" data-width="85" data-height="85" />
	                            </div>
							</div>
						</div>
					</div>
				</div>
			</div> -->
	
		
		<!-- <div class="ibox-mobileContent2">
			<div id="donut_single" style="width: 400px; height: 300px; float: left;"></div>
			
			<input type="checkbox" class="js-switch" />
		</div> -->
	
		</div>
	    <div class="footer">
	        
	        <%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
	        
	    </div>
	    
    </div>
</div>
<script language="javascript" src="${pageContext.request.contextPath}/v2/js/jquery-2.1.1.js"></script>
<%@ include file="/WEB-INF/views/mobile/common/common_js.jsp"%>

<!-- Custom and plugin javascript -->
<script language="javascript" src="/hdm/v2/js/inspinia.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/pace/pace.min.js"></script>

</body>

</html>
		