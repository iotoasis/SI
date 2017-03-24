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
	
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
	<script src="http://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
	<script src="<c:url value="/v2/js/herit/hdb.js" />"></script>
	<script src="<c:url value="/v2/js/herit/firmwareSchedule.js" />"></script>
	
    <script type="text/javaScript" language="javascript">
		var contextPath = "${pageContext.request.contextPath}";

		$(document).ready(function() {
			initUI();
			initUI2();
		})
		
		function initUI() {
			$("#side-menu_firmware").addClass("active");
			$("#side-menu_firmware ul").addClass("in");
			$("#side-menu_firmware ul li:eq(2)").addClass("active");
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
				<h2>스케줄링</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>펌웨어 관리</li>
					<li class="active"><strong>스케줄링</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">스케줄링</h3>
						</div>
						<div class="ibox-content form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-label">디바이스 모델</label>
								<div class="col-sm-10">
									<select class="input-sm form-control" name="deviceModelId" id="search_deviceModelId" >
										<option value="choose">선택</option>
										<c:forEach items="${deviceModelList}" var="deviceModel">
											<option value="${deviceModel.id}"														
											<c:if test="${fn:contains(param.deviceModelId, deviceModel.id)}">selected</c:if>
											>${deviceModel.manufacturer}-${deviceModel.modelName}
											</option>
										</c:forEach>			
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">펌웨어 패키지</label>
								<div class="col-sm-10">
									<select name="firmwareId" id="search_package" class="input-sm form-control">
										<option value="">선택</option>
										<c:forEach items="${firmwareList}" var="firmwareInfo">
											<option value="${firmwareInfo.id}|${firmwareInfo['package']}"														
											<c:if test="${fn:contains(param.firmwareId, firmwareInfo.id)}">selected</c:if>
											>${firmwareInfo['package']}
											</option>
										</c:forEach>			
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">펌웨어 버전</label>
								<div class="col-sm-10">
									<select name="firmwareVersion" id="search_firmwareVersion" class="input-sm form-control">
										<option value="">선택</option>
										<c:forEach items="${versionList}" var="firmwareVer">
											<option value="${firmwareVer.version}">${firmwareVer.version}</option>
										</c:forEach>			
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">업그레이드 일시</label>
								<div class="col-sm-10 input-group">
									<input type="text" id="dpStart" class="date_picker input-sm form-control" style="width:100px; margin-left: 15px;">&nbsp;
									<select class="input-sm form-control" id="dpTime" style="width:100px;">
										<option value="00:00">00:00</option>
										<option value="01:00">01:00</option>
										<option value="02:00">02:00</option>
										<option value="03:00">03:00</option>
										<option value="04:00">04:00</option>
										<option value="05:00">05:00</option>
										<option value="06:00">06:00</option>
										<option value="07:00">07:00</option>
										<option value="08:00">08:00</option>
										<option value="09:00">09:00</option>
										<option value="10:00">10:00</option>
										<option value="11:00">11:00</option>
										<option value="12:00">12:00</option>
										<option value="13:00">13:00</option>
										<option value="14:00">14:00</option>
										<option value="15:00">15:00</option>
										<option value="16:00">16:00</option>
										<option value="17:00">17:00</option>
										<option value="18:00">18:00</option>
										<option value="19:00">19:00</option>
										<option value="20:00">20:00</option>
										<option value="21:00">21:00</option>
										<option value="22:00">22:00</option>
										<option value="23:00">23:00</option>
									</select>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="text-center">
								<a class="btn btn-primary" href="javascript:executeUpgrade();">업그레이드 실행</a>
							</div>
						</div>
					</div>
					
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">목록조회</h3>
						</div>
						<div class="ibox-content">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>디바이스모델명</th>
										<th>버전</th>
										<th>패키지명</th>
										<th>업그레이드일시</th>
										<th>등록갱신일시</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${!empty firmwareUpdateList}">
											<c:forEach items="${firmwareUpdateList}" var="firmwareUpdate" varStatus="status">
												<tr>
													<td>${firmwareUpdate['modelName']}</td>
													<td>${firmwareUpdate['version']}</td>
													<td>${firmwareUpdate['package']}</td>
													<td>${firmwareUpdate['scheduleTime']}</td>
													<td>${firmwareUpdate['updateTime']}</td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
		
											<tr>
												<td colspan="5">조회된 데이터가 없습니다.</td>
											</tr>
		
										</c:otherwise>
									</c:choose>
								</tbody>
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

</body>

</html>
