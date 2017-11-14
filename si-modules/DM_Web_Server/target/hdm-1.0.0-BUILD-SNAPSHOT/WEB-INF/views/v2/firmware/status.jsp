<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/tld/herit.tld" prefix="herit" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
    <title>OASIS SI Admin</title>
	<%@ include file="/WEB-INF/views/v2/common/common_js.jsp"%>
	<script src="<c:url value="/v2/js/herit/hdb.js" />"></script>
	<script src="<c:url value="/v2/js/herit/firmware.js" />"></script>
	<script src="<c:url value="/v2/js/herit/firmwareStatus.js" />"></script>
	
    <script type="text/javaScript" language="javascript">
	    var param = ${paramJson};
	    var oui = "";
	    var modelName = "";
	    var sn = "";
	    var page = "";
	
	    var contextPath = "${pageContext.request.contextPath}";
    
		$(document).ready(function() {
	    	if (param['oui'] != null) {
	    		page = param['oui'][0];
	    	}
	    	if (param['modelName'] != null) {
	    		page = param['modelName'][0];
	    	}
	    	if (param['sn'] != null) {
	    		page = param['sn'][0];
	    	}
	    	if (param['page'] != null) {
	    		page = param['page'][0];
	    	}
			
			initUI();
			initUI2();
		});
		
		function initUI() {
			$("#side-menu_firmware").addClass("active");
			$("#side-menu_firmware ul").addClass("in");
			$("#side-menu_firmware ul li:eq(4)").addClass("active");
		}
    </script>
</head>
<body>

<div id="wrapper">

	<nav class="navbar-default navbar-static-side" role="navigation">
		<div class="sidebar-collapse">
			<%@ include file="/WEB-INF/views/v2/common/common_sidemenu.jsp"%>
		</div>
	</nav>


	<div id="page-wrapper" class="gray-bg">
		<div class="row border-bottom">
			<%@ include file="/WEB-INF/views/v2/common/common_topbar.jsp"%>
		</div>
		

		<div class="row wrapper border-bottom white-bg page-heading">
			<div class="col-sm-4">
				<h2>상태 조회</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>펌웨어 관리</li>
					<li class="active"><strong>상태 조회</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
<form action="" name="myForm" id="myForm" method="POST">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">상태 조회</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-12">
									<label class="form-inline">
										디바이스 모델&nbsp;
										<select name="deviceModelId" id="search_deviceModelId" class="form-control input-sm">
											<option value="">선택</option>
											<c:forEach items="${deviceModelList}" var="deviceModel">
												<option value="${deviceModel.oui}|${deviceModel.modelName}"														
												<c:if test="${fn:contains(param.deviceModelId, deviceModel.id)}">selected</c:if>
												>${deviceModel.manufacturer}-${deviceModel.modelName}
												</option>
											</c:forEach>			
										</select>
										&nbsp; &nbsp; &nbsp; &nbsp; 
										시리얼번호&nbsp;
										<div class="input-group">
											<input type="text" name="sn" id="search_sn" value="<c:out value="${param.sn}" />" class="input-sm form-control" placeholder="시리얼번호" />
											<span class="input-group-btn">
												<button type="button" id="btnSearchfirmware" class="btn btn-sm btn-primary" > 조회</button>
											</span>
										</div>
									</label>
								</div>
							</div>
						
							<div>&nbsp;</div>

							<table id="firmware_table" class="table table-bordered table-hover">
								<thead>
									<tr>
										<th id="hd_modelName">모델번호</th>
										<th id="hd_sn">시리얼번호</th>
										<th id="hd_package">패키지명</th>
										<th id="hd_version">펌웨어버전</th>
										<th id="hd_upStatus" class="firmware_manual_renderer">업그레이드상태</th>
										<th id="hd_upStatusTime">상태갱신시간</th>
										<th id="hd_upVersion">업그레이드버전</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td colspan="7">디바이스를 선택한 후 펌웨어 업그레이드 상태를 조회하세요.</td>
									</tr>
								</tbody>
							</table>
							
							<div class="row">
								<div class="col-sm-12 text-center">
									<div class="dataTables_paginate paging_simple_numbers">
										<ul class="pagination paging_wrap"></ul>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
</form>
		</div>
		
		
		<div class="footer">
			<%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
		</div>
	</div>
</div>

</body>

</html>
