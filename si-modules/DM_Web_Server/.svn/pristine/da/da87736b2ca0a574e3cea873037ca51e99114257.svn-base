<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/tld/herit.tld" prefix="herit" %>
<%@ page session="true"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>HIT DM Admin</title>
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp" %>
<script type="text/javascript">
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
})

function movePage(cPage){
	window.location.href = "${pageContext.request.contextPath}/firmware/device/upgrade.do?deviceModel="+deviceModel+
																	"&package="+package+
																	"&sn="+sn+
																	"&page="+cPage;
}

</script>
<script src="${pageContext.request.contextPath}/js/herit/firmwareUpgrade.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/dm.js"></script>

</head>
<body>
	<!-- #wrapper -->
	<div id="wrapper">
	
		<!-- .header_wrap -->
		<%@ include file="/WEB-INF/views/common/include/hdm_menu.jsp" %>
		<!-- /.header_wrap -->


		<!-- #container -->
		<div class="sub0101">
			<div id="container">
				<div class="page_title">
					<h2>펌웨어 업그레이드</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 펌웨어 관리 &gt; 펌웨어 업그레이드
					</div>
				</div>

				<div class="content_wrap top_pad_30">

					<!-- Search-->
					<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<!--<div class="search_title">검색설정</div> -->
								<form action="">
									<div class="search_form_wrap">
										<div class="search_form_div">
												디바이스 모델
												<select name="deviceModelId" id="search_deviceModelId">
													<option value="">선택</option>
													<c:forEach items="${deviceModelList}" var="deviceModel">
														<option value="${deviceModel.id}"														
														<c:if test="${fn:contains(param.deviceModelId, deviceModel.id)}">selected</c:if>
														>${deviceModel.manufacturer}-${deviceModel.modelName}
														</option>
													</c:forEach>			
												</select>
												패키지
												<select name="firmwareId" id="search_package">
													<option value="">선택</option>
													<c:forEach items="${firmwareList}" var="firmwareInfo">
														<option value="${firmwareInfo.id}"														
														<c:if test="${fn:contains(param.firmwareId, firmwareInfo.id)}">selected</c:if>
														>${firmwareInfo['package']}
														</option>
													</c:forEach>			
												</select>
												시리얼번호 <input type="text" value="${param.sn}" 
													name="sn" id="search_sn" class="i_text" /> 
												<input type="submit" class="search_bt_green" value="검색" />
										</div>
									</div>
								</form>
							</div>
						</div>

					</div>
					<!-- Search END-->

					<!-- Search-->
					<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<!--<div class="search_title">검색설정</div> -->
								<div class="search_form_wrap">
									<div class="search_form_div">
											펌웨어버전
											<select name="firmwareVersion" id="search_firmwareVersion">
												<option value="">선택</option>
												<c:forEach items="${versionList}" var="firmwareVer">
													<option value="${firmwareVer.version}">${firmwareVer.version}</option>
												</c:forEach>			
											</select>
											<input type="submit" id="btnUpgrade" class="search_bt_gray" value="업그레이드 실행" />
									</div>
								</div>
							</div>
						</div>

					</div>
					<!-- Search END-->

					<div class="content_title_box obstacle_title">
						<p class="content_title">목록 조회</p>
					</div>
					<div class="device_list_wrap_100py2">

						<table border="0" cellspacing="0" cellpadding="0"
							class="table_type_nomal_scroll_height">
							<tr>
								<th><input type="checkbox" name="input" id="input"
									class="top_pad_5" /></th>
								<th>모델번호</th>
								<!-- <th>디바이스ID</th> -->
								<th>시리얼번호</th>
								<th>패키지명</th>
								<th>펌웨어버전</th>
								<th>업데이트일시</th>
								<th>업그레이드상태</th>
								<th>업그레이드버전</th>
								<th>업그레이드</th>
							</tr>
							
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
					</table>
						
			        <%--페이징처리 START--%>
			        <div class="paging_wrap">
		            	<herit:table table="${resultPagingUtil}" form="document.myForm" contextPath="${pageContext.request.contextPath}"
		            		url="${pageContext.request.contextPath}/firmware/device/upgrade.do" method="movePage" beforeMethod="" />       		
			        </div>	
			        <%--페이징처리 END--%>
	        
						
					</div>
				</div>



			</div>
		</div>



		<!-- /#container -->
		<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp" %>
	</div>
	<!-- /#wrapper -->

</body>
</html>