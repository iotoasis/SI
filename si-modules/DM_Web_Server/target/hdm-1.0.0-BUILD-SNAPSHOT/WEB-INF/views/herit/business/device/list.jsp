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
var gr_id="device";
var sub_num="1";

function movePage1(cPage, moveForm, actionUrl){
    moveForm.page.value = cPage;
    moveForm.action = actionUrl;
    moveForm.submit();
}
</script>
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
					<h2>목록 조회</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 디바이스 관리 &gt; 목록 조회
					</div>
				</div>

				<div class="content_wrap top_pad_30">

					<!-- Search-->
					<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<!--<div class="search_title">검색설정</div> -->
								<form action="" name="searchForm" id="searchForm">
									<div class="search_form_wrap">
										<div class="search_form_div">
												디바이스 모델
												<select name="deviceModel" id="search_deviceModelId" value="${param.deviceModel}">
													<option value="${deviceModel.oui}">ALL</option>
													<c:forEach items="${deviceModelList}" var="deviceModel">
														<option value="${deviceModel.oui}|${deviceModel.modelName}"														
														<c:if test="${fn:contains(param.deviceModel, deviceModel.modelName)}">selected</c:if>
														>${deviceModel.manufacturer}-${deviceModel.modelName}
														</option>
													</c:forEach>			
												</select>
												시리얼번호 <input type="text" value="${param.sn}" 
													name="sn" id="search_sn" class="i_text" /> 
												<input type="submit" class="search_bt_green" value="검색" />
										</div>
									</div>
									<input type="hidden" name="page" value="1" />
									<input type="hidden" name="currPage" value="1" />
								</form>
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
							
						<c:choose>						
							<c:when test="${!empty resultPagingUtil.currList}">
								<c:forEach items="${resultPagingUtil.currList}" var="resultInfo" varStatus="status">
								
							<tr>
								<td><c:out value="${resultInfo['manufacturer']}"/></td>
					
								<td>
									<a href="${pageContext.request.contextPath}/device/detail.do?deviceId=${resultInfo['deviceId']}">
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
					</table>
						
			        <%--페이징처리 START--%>
			        <div class="paging_wrap">
		            	<herit:table table="${resultPagingUtil}" form="document.searchForm" contextPath="${pageContext.request.contextPath}"
		            		url="${pageContext.request.contextPath}/device/list.do" method="movePage1" beforeMethod="" />       		
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