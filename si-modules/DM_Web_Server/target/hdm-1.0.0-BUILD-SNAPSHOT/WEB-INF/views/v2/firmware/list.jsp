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
    <script type="text/javaScript" language="javascript">
		$(document).ready(function() {
			initUI();
		});
		
		function initUI() {
			$("#side-menu_firmware").addClass("active");
			$("#side-menu_firmware ul").addClass("in");
			$("#side-menu_firmware ul li:eq(0)").addClass("active");
		}
		
		function fnSearch() {
			var myForm					= document.getElementById("myForm");
			myForm.action				= "<c:url value='/firmware/list.do'/>";
			myForm.submit();
		}

		function movePage1(cPage, moveForm, actionUrl){
		    moveForm.page.value = cPage;
		    moveForm.action = actionUrl;
		    moveForm.submit();
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
				<h2>펌웨어 목록 조회</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>펌웨어 관리</li>
					<li class="active"><strong>펌웨어 목록 조회</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
<form action="" name="myForm" id="myForm" method="POST">
<input type="hidden" name="page" value="1" />
<input type="hidden" name="currPage" value="1" />
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">펌웨어 목록 조회</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-6">
									<div class="input-group">
										<select name="deviceModel" id="search_deviceModelId" class="input-sm form-control input-s-sm inline">
											<option value="">ALL</option>
											<c:forEach items="${deviceModelList}" var="deviceModel">
												<option value="${deviceModel.id}" <c:if test="${deviceModel.id == param.deviceModel}">selected="selected"</c:if> >${deviceModel.manufacturer}-${deviceModel.modelName}</option>
											</c:forEach>
										</select>
										<span class="input-group-btn">
											<button type="button" onclick="fnSearch(); return false;" class="btn btn-sm btn-primary"> 조회</button>
										</span>
									</div>
								</div>
							</div>
						
							<div>&nbsp;</div>

							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>제조사</th>
										<th>디바이스모델</th>
										<th>패키지명</th>
										<th>최신버전</th>
										<th>버전수</th>
										<th>생성일시</th>
										<th>등록갱신일시</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>
										<c:when test="${!empty resultPagingUtil.currList}">
											<c:forEach items="${resultPagingUtil.currList}" var="resultInfo" varStatus="status">
												<tr>
													<td><c:out value="${resultInfo['manufacturer']}" /></td>
													<td><c:out value="${resultInfo['modelName']}" /></td>
													<td><a href="<c:url value="/firmware/detail.do?id=${resultInfo['id']}" />"><c:out value="${resultInfo['package']}" /></a></td>
													<td><c:out value="${resultInfo['latestVersion']}" /></td>
													<td><c:out value="${resultInfo['versionCount']}" /></td>
													<td><c:out value="${resultInfo['createTime']}" /></td>
													<td><c:out value="${resultInfo['updateTime']}" /></td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
											<tr>
												<td colspan="7">조회된 데이터가 없습니다.</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</tbody>
							</table>
							
							<div class="row">
								<div class="col-sm-12 text-center">
									<div class="dataTables_paginate paging_simple_numbers">
										<herit:table table="${resultPagingUtil}" form="document.myForm" contextPath="${pageContext.request.contextPath}"
											url="${pageContext.request.contextPath}/firmware/list.do" method="movePage1" beforeMethod="" />
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
