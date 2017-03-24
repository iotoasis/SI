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
	<script src="<c:url value="/v2/js/herit/firmwareUpgrade.js" />"></script>
	<script src="<c:url value="/v2/js/herit/dm.js" />"></script>
	
    <script type="text/javaScript" language="javascript">
		var gr_id="firmware";
		var sub_num="3";

		var resultPagingUtilJson = ${resultPagingUtilJson};
		var paramJson = ${paramJson};
		var firmwareListJson = ${firmwareListJson};
		var versionListJson = ${versionListJson};
		var deviceModelListJson = ${deviceModelListJson};


		var deviceModel = "";
		var package = "";
		var sn = "";
		var page = "";
		$(document).ready(function() {
			if (paramJson['deviceModel'] != null) {
				deviceModel = paramJson['deviceModel'][0];
			}
			if (paramJson['package'] != null) {
				package = paramJson['package'][0];
			}
			if (paramJson['sn'] != null) {
				page = paramJson['sn'][0];
			}
			if (paramJson['page'] != null) {
				page = paramJson['page'][0];
			}
			
			initUI();
			initUI2();
			
			$('.upgradeCheckbox').iCheck({
				checkboxClass: 'icheckbox_square-green',
				radioClass: 'iradio_square-green',
			});
			
		})

		function movePage(cPage){
			window.location.href = "<c:url value="/firmware/device/upgrade.do" />?deviceModel="+deviceModel+
																			"&package="+package+
																			"&sn="+sn+
																			"&page="+cPage;
		}
		
		function initUI() {
			$("#side-menu_firmware").addClass("active");
			$("#side-menu_firmware ul").addClass("in");
			$("#side-menu_firmware ul li:eq(1)").addClass("active");
		}
		
		function fnSearch() {
			document.myForm.submit();
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
				<h2>펌웨어 업그레이드</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>펌웨어 관리</li>
					<li class="active"><strong>펌웨어 업그레이드</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
<form action="" name="myForm" id="myForm" method="POST">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">펌웨어업그레이드</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-12">
									<label class="form-inline">
										디바이스 모델&nbsp;
										<select name="deviceModelId" id="search_deviceModelId" class="form-control input-sm">
											<option value="">선택</option>
											<c:forEach items="${deviceModelList}" var="deviceModel">
												<option value="${deviceModel.id}"														
												<c:if test="${fn:contains(param.deviceModelId, deviceModel.id)}">selected</c:if>
												>${deviceModel.manufacturer}-${deviceModel.modelName}
												</option>
											</c:forEach>			
										</select>
										&nbsp; &nbsp; &nbsp; &nbsp; 
										패키지&nbsp;
										<select name="firmwareId" id="search_package" class="form-control input-sm">
											<option value="">선택</option>
											<c:forEach items="${firmwareList}" var="firmwareInfo">
												<option value="${firmwareInfo.id}"														
												<c:if test="${fn:contains(param.firmwareId, firmwareInfo.id)}">selected</c:if>
												>${firmwareInfo['package']}
												</option>
											</c:forEach>			
										</select>
										&nbsp; &nbsp; &nbsp; &nbsp; 
										시리얼번호&nbsp;
										<div class="input-group">
											<input type="text" name="sn" id="search_sn" value="<c:out value="${param.sn}" />" class="input-sm form-control" placeholder="시리얼번호" />
											<span class="input-group-btn">
												<button type="button" onclick="fnSearch(); return false;" class="btn btn-sm btn-primary" > 검색</button>
											</span>
										</div>
									</label>
								</div>
							</div>
							<div class="row">
								<div class="col-sm-12">
									<label class="form-inline">
										펌웨어버전&nbsp;
										<div class="input-group">
											<select name="firmwareVersion" id="search_firmwareVersion" class="form-control input-sm">
												<option value="">선택</option>
												<c:forEach items="${versionList}" var="firmwareVer">
													<option value="${firmwareVer.version}">${firmwareVer.version}</option>
												</c:forEach>			
											</select>
											<span class="input-group-btn">
												<button type="button" id="btnUpgrade" class="btn btn-sm btn-primary" disabled="disabled" > 업그레이드 실행</button>
											</span>
										</div>
									</label>
								</div>
							</div>

							<div>&nbsp;</div>

							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th><!-- <input type="checkbox" name="input" id="input" class="upgradeCheckbox" /> --></th>
										<th>모델번호</th>
										<th>시리얼번호</th>
										<th>패키지명</th>
										<th>펌웨어버전</th>
										<th>업데이트일시</th>
										<th>업그레이드상태</th>
										<th>업그레이드버전</th>
										<th>업그레이드</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>						
										<c:when test="${!empty resultPagingUtil.currList}">
											<c:forEach items="${resultPagingUtil.currList}" var="resultInfo" varStatus="status">
											
										<tr>
											<td><input type="checkbox" id="cb_${resultInfo['deviceId']}" curVersion="${resultInfo['version']}" class="upgradeCheckbox" /></td>
											<td><c:out value="${resultInfo['modelName']}"/></td>
											<!-- <td>${resultInfo['deviceId']}</td> -->
											<td>${resultInfo['sn']}</td>
											<td>${resultInfo['package']}</td>
											<td>${resultInfo['version']}</td>
											<td>${resultInfo['updateTime']}</td>
											<td>
											<c:if test="${resultInfo['upStatus'] eq null}">-</c:if>
											<c:if test="${resultInfo['upStatus'] eq '100'}">명령전송</c:if>
											<c:if test="${resultInfo['upStatus'] eq '1'}">초기화</c:if>
											<c:if test="${resultInfo['upStatus'] eq '2'}">다운로드중</c:if>
											<c:if test="${resultInfo['upStatus'] eq '3'}">다운로드종료</c:if>
											<c:if test="${resultInfo['upStatus'] eq '10'}">설치시작</c:if>
											<c:if test="${resultInfo['upStatus'] eq '101'}">					
												<c:if test="${resultInfo['upResult'] eq '1'}">실패:Storage</c:if>
												<c:if test="${resultInfo['upResult'] eq '2'}">실패:Memory</c:if>
												<c:if test="${resultInfo['upResult'] eq '3'}">실패:Connection</c:if>
												<c:if test="${resultInfo['upResult'] eq '4'}">실패:CRC</c:if>
												<c:if test="${resultInfo['upResult'] eq '5'}">실패:Package</c:if>
												<c:if test="${resultInfo['upResult'] eq '6'}">실패:URI</c:if>
												<c:if test="${resultInfo['upResult'] eq '10'}">업그레이드성공</c:if>
												<c:if test="${resultInfo['upResult'] eq '11'}">실패:앱오류</c:if>
											</c:if>
											(${resultInfo['upStatus']},${resultInfo['upResult']})</td>
											<td>${resultInfo['upVersion']}</td>
											<td> </td>
										</tr>
										
											</c:forEach>
										</c:when>
										<c:otherwise>
										
										<tr>
											<td colspan="9">조회된 데이터가 없습니다.</td>
										</tr>
						
										</c:otherwise> 
									</c:choose>
								
								</tbody>
							</table>
							
							<div class="row">
								<div class="col-sm-12 text-center">
									<div class="dataTables_paginate paging_simple_numbers">
										<herit:table table="${resultPagingUtil}" form="document.myForm" contextPath="${pageContext.request.contextPath}"
											url="${pageContext.request.contextPath}/firmware/device/upgrade.do" method="movePage1" beforeMethod="" />
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
