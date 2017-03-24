<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/tld/herit.tld" prefix="herit"%>
<%@ page session="true"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>HIT DM Admin</title>
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp"%>
<script type="text/javascript">
	var gr_id = "firmware";
	var sub_num = "1";
</script>
</head>
<body>
	<!-- #wrapper -->
	<div id="wrapper">

		<!-- .header_wrap -->
		<%@ include file="/WEB-INF/views/common/include/hdm_menu.jsp"%>
		<!-- /.header_wrap -->


		<!-- #container -->
		<div class="sub0101">
			<div id="container">
				<div class="page_title">
					<h2>목록 조회</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 펌웨어 관리 &gt; 목록 조회
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
											디바이스 모델 <select name="deviceModel" id="search_deviceModelId">
												<option value="">ALL</option>
												<c:forEach items="${deviceModelList}" var="deviceModel">
													<option value="${deviceModel.id}"
														<c:if test="${fn:contains(param.deviceModel, deviceModel.modelName)}">selected</c:if>>${deviceModel.manufacturer}-${deviceModel.modelName}
													</option>
												</c:forEach>
											</select> <input type="submit" class="search_bt_green" value="검색" />
										</div>
									</div>
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
								<th>디바이스모델</th>
								<th>패키지명</th>
								<th>최신버전</th>
								<th>버전수</th>
								<th>생성일시</th>
								<th>등록갱신일시</th>
							</tr>

							<c:choose>
								<c:when test="${!empty resultPagingUtil.currList}">
									<c:forEach items="${resultPagingUtil.currList}"
										var="resultInfo" varStatus="status">

										<tr>
											<td><c:out value="${resultInfo['manufacturer']}" /></td>
											<td>${resultInfo['modelName']}</td>
											<td><a
												href="${pageContext.request.contextPath}/firmware/detail.do?id=${resultInfo['id']}">${resultInfo['package']}</a></td>
											<td>${resultInfo['latestVersion']}</td>
											<td>${resultInfo['versionCount']}</td>
											<td>${resultInfo['createTime']}</td>
											<td>${resultInfo['updateTime']}</td>
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
							<herit:table table="${resultPagingUtil}" form="document.myForm"
								contextPath="${pageContext.request.contextPath}"
								url="${pageContext.request.contextPath}/device/list.do"
								method="movePage1" beforeMethod="" />
						</div>
						<%--페이징처리 END--%>


					</div>
				</div>



			</div>
		</div>



		<!-- /#container -->
		<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
	</div>
	<!-- /#wrapper -->

</body>
</html>