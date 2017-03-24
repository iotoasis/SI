<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" 
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="true"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>Device</title>
	<%@ include file="/WEB-INF/views/mobile/common/common_head.jsp"%>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

<script type="text/javascript">
var contextPath = "${pageContext.request.contextPath}";
$(document).ready(function() {
	initUI();
})
</script>

<script src="${pageContext.request.contextPath}/mobile/js/herit/hdb.js"></script>
<script src="${pageContext.request.contextPath}/mobile/js/herit/mobileDeviceDetail.js"></script>
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
							<table class="table" id="infoTable" style="table-layout:fixed;">
							
							</table>
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
<script src="${pageContext.request.contextPath}/v2/js/jquery-2.1.1.js"></script>
<%@ include file="/WEB-INF/views/mobile/common/common_js.jsp"%>

<!-- Custom and plugin javascript -->
<script src="/hdm/v2/js/inspinia.js"></script>
<script src="/hdm/v2/js/plugins/pace/pace.min.js"></script>

</body>

</html>
		