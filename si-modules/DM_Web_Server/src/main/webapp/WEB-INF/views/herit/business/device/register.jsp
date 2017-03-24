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
<title>HIT DM Admin</title>
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp"%>
<script type="text/javascript">
var gr_id="device";
var sub_num="3";
$(document).ready(function() {
	initUI();
})
</script>
<script src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/deviceRegister.js"></script>
</head>
<body>
	<!-- #wrapper -->
	<div id="wrapper">
		<!-- .header_wrap -->
		<%@ include file="/WEB-INF/views/common/include/hdm_menu.jsp"%>
		<!-- /.header_wrap -->
		<!-- #container -->
		<div class="sub0103">
			<div id="container">
				<div class="page_title">
					<h2>디바이스 등록</h2>
					<div class="position_text">
						<img src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif" alt="" />
						디바이스 관리 &gt; 디바이스 등록
					</div>
				</div>
				<div class="content_wrap top_pad_30">
					<div class="content_title_box" style="height: 24px">
						<p class="content_title">디바이스 정보 입력</p>
					</div>
					<!-- Search-->
					<%-- <div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<!--<div class="search_title">검색설정</div> -->
								<form action="">
									<div class="search_form_wrap">
										<div class="search_form_div">
											<span>디바이스 모델</span>
											<select name="deviceModel" id="search_deviceModel" value="${param.deviceModel}">
												<c:forEach items="${deviceModelList}" var="deviceModel">
													<option value="${deviceModel.id}|${deviceModel.oui}|${deviceModel.modelName}"														
													<c:if test="${fn:contains(param.deviceModel, deviceModel.modelName)}">selected</c:if>
													>${deviceModel.manufacturer}-${deviceModel.modelName}
													</option>
												</c:forEach>			
											</select>
											<span class="left_pad_30">시리얼번호</span> 
											<input type="text" size="30" id="serialNo" />
											<br /><br />
											<span>ID</span>
											<input type="text" id="authId" />
											<span class="left_pad_200">Password</span>
											<input type="password" id="authPwd" />
											<input name="button" type="button" class="search_bt_green" id="btnRegister" value="저장" />
											
										</div>
									</div>
								</form>
							</div>
						</div> --%>
						<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<form action="">
									<div class="search_form_wrap" style="padding:20px;">
										<table>
										<tr>
											<td height="30" width="150">디바이스 모델</td>
											<td>							
												<select name="deviceModel" id="search_deviceModel" value="${param.deviceModel}" >
													<c:forEach items="${deviceModelList}" var="deviceModel">
														<option value="${deviceModel.id}|${deviceModel.oui}|${deviceModel.modelName}"														
														<c:if test="${fn:contains(param.deviceModel, deviceModel.modelName)}">selected</c:if>
														>${deviceModel.manufacturer}-${deviceModel.modelName}
														</option>
													</c:forEach>			
												</select>											
											</td>
										</tr>
										<tr>
											<td height="30" width="150">시리얼번호</td>
											<td>
												<input type="text" id="serialNo" size="38" />
											</td>
										</tr>
										<tr>
											<td height="30" width="150">ID</td>
											<td>
												<input type="text" id="authId" size="38" />
											</td>
										</tr>
										<tr>
											<td height="30" width="150">Password</td>
											<td>
												<input type="password" id="authPwd" size="38" />
											</td>
										</tr>
						
										<tr>
											<td height="30" width="150">&nbsp;</td>
											<td height="30" colspan="2">
											<input name="button" type="button" class="search_bt_green" id="btnRegister" value="저장" />
											</td>
										</tr>
										</table>
									</div>
								</form>
							</div>
						</div>
						<!-- Search END-->
					</div>
				</div>
			</div>
			<!-- /#container -->
			<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
		</div>
		<!-- /#wrapper -->
</body>
</html>