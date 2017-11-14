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
			$("#side-menu_device").addClass("active");
			$("#side-menu_device ul").addClass("in");
			$("#side-menu_device ul li:eq(1)").addClass("active");
		}
		
		function fnSearch() {
			var myForm					= document.getElementById("myForm");
			myForm.action				= "<c:url value='/device/list.do'/>";
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
				<h2>목록 조회</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>디바이스 관리</li>
					<li class="active"><strong>목록 조회</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">목록 조회</h3>
						</div>
						<div class="ibox-content">
<form action="" name="myForm" id="myForm" method="POST">
<input type="hidden" name="page" value="1" />
<input type="hidden" name="currPage" value="1" />

							<div class="row">
								<div class="col-sm-12">
									<label class="form-inline">
										디바이스 모델&nbsp;
										<select id="search_deviceModelId" name="deviceModel" class="form-control input-sm">
											<option value="<c:out value="${deviceModel.oui}" />">ALL</option>
											<c:forEach items="${deviceModelList}" var="deviceModel">
												<option value="${deviceModel.oui}|${deviceModel.modelName}" <c:if test="${fn:contains(param.deviceModel, deviceModel.modelName)}">selected="selected"</c:if> >${deviceModel.manufacturer}-${deviceModel.modelName}
												</option>
											</c:forEach>			
										</select>
										&nbsp; &nbsp; &nbsp; &nbsp; 
										시리얼번호&nbsp;
										<div class="input-group">
											<input type="text" name="sn" value="<c:out value="${param.sn}" />" class="input-sm form-control" placeholder="시리얼번호" />
											<span class="input-group-btn">
												<button type="button" onclick="fnSearch(); return false;" class="btn btn-sm btn-primary"> 조회</button>
											</span>
										</div>
									</label>
								</div>
							</div>
</form>
							<div>&nbsp;</div>

							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>제조사</th>
										<th>디바이스ID</th>
										<th>모델번호</th>
										<th>SN</th>
										<th>펌웨어버전</th>
										<th>생성일시</th>
										<th>등록갱신일시</th>
										<th>등록</th>
										<th>연결</th>
										<th>오류</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>						
										<c:when test="${!empty resultPagingUtil.currList}">
											<c:forEach items="${resultPagingUtil.currList}" var="resultInfo" varStatus="status">
												<tr>
													<td><c:out value="${resultInfo['manufacturer']}"/></td>
													<td>
														<a href="<c:url value="/v2/monitor/device.do?deviceId=${resultInfo['deviceId']}" />">
														<c:set var="length" value="${fn:length(resultInfo['deviceId'])}"/>
														<c:choose>
															<c:when test="${length > 30}">
																${fn:substring(resultInfo['deviceId'], 0, 25)}...
															</c:when>
															<c:otherwise>
																${resultInfo['deviceId']}
															</c:otherwise>
														</c:choose>
														</a>
													</td>
													<td>${resultInfo['modelName']}</td>
													<td>${resultInfo['sn']}</td>
													<td>${resultInfo['firmwareVersion']}</td>
													<td>${resultInfo['createTime']}</td>
													<td>${resultInfo['updateTime']}</td>
													<td>
														<c:if test="${fn:contains(resultInfo['bsStatus'], '0')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_gray.gif'></c:if>
														<c:if test="${fn:contains(resultInfo['bsStatus'], '1')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_gray.gif'></c:if>
														<c:if test="${fn:contains(resultInfo['bsStatus'], '2')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_gray.gif'></c:if>
														<c:if test="${fn:contains(resultInfo['bsStatus'], '3')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_gray.gif'></c:if>
														<c:if test="${fn:contains(resultInfo['bsStatus'], '4')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_green.gif'></c:if>
													</td>
													<td>
														<c:if test="${fn:contains(resultInfo['connStatus'], '0')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_gray.gif'></c:if>
														<c:if test="${fn:contains(resultInfo['connStatus'], '1')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_green.gif'></c:if>
													</td>
													<td>
														<c:choose>
															<c:when test="${fn:contains(resultInfo['connStatus'], '0')}">
																<img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_gray.gif' alt="minor">
															</c:when>
															<c:when test="${fn:contains(resultInfo['connStatus'], '1')}">
																<c:if test="${fn:contains(resultInfo['connStatus'], '0')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_gray.gif'></c:if>
																<c:if test="${fn:contains(resultInfo['errGrade'], '0')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_green.gif' alt="normal"></c:if>
																<c:if test="${fn:contains(resultInfo['errGrade'], '1')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_gray.gif' alt="minor"></c:if>
																<c:if test="${fn:contains(resultInfo['errGrade'], '2')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_yellow.gif' alt="major"></c:if>
																<c:if test="${fn:contains(resultInfo['errGrade'], '3')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon.gif' alt="critical"></c:if>
																<c:if test="${fn:contains(resultInfo['errGrade'], '4')}"><img src='${pageContext.request.contextPath}/images/hitdm/common/box_icon_red.gif' alt="fatqal"></c:if>
															</c:when>
														</c:choose>
													</td>
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
						            		url="${pageContext.request.contextPath}/device/list.do" method="movePage1" beforeMethod="" />       		
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


<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form id="modalForm">
<input type="hidden" id="modal_id" value="" />
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="form-group">
						<label>그룹코드</label> <input type="text" id="modal_groupCode" validateMsg="그룹코드" class="form-control" />
					</div>
					<div class="form-group">
						<label>그룹명</label> <input type="text" id="modal_groupName" validateMsg="그룹명" class="form-control" />
					</div>
					<div class="form-group">
						<label>설명</label> <input type="text" id="modal_description" validateMsg="설명" class="form-control" />
					</div>
				</div>
			</div>
			<div class="modal-footer">
			</div>
		</div>
</form>
	</div>
</div>


</body>
</html>
