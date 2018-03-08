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
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp" %>
<script type="text/javascript">
var gr_id="history";
var sub_num="1";

var param = ${paramJson};
var deviceModelList = ${deviceModelListJson};

var contextPath = "${pageContext.request.contextPath}";
var deviceModel = "";
var deviceId = "";
var sn = "";
var page = "";
$(document).ready(function() {
	if (param['deviceModel'] != null) {
		deviceModel = param['deviceModel'][0];
	}
	if (param['deviceId'] != null) {
		deviceId = param['deviceId'][0];
	}
	if (param['sn'] != null) {
		page = param['sn'][0];
	}
	if (param['page'] != null) {
		page = param['page'][0];
	}
	
	initUI();
})


</script>
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/history.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/historyError.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
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
					<h2>장애 이력 조회</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 이력 &gt; 장애이력
					</div>
				</div>

				<div class="content_wrap top_pad_30">

					<!-- Search-->
					<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<!--<div class="search_title">검색설정</div> -->
								<div class="search_form_wrap">
									<div class="search_form_div">
										디바이스 모델
										<select name="deviceModel" id="search_deviceModelId" value="${param.deviceModel}">
											<option value="">선택</option>
											<c:forEach items="${deviceModelList}" var="deviceModel">
												<option value="${deviceModel.oui}|${deviceModel.modelName}"														
												<c:if test="${fn:contains(param.deviceModel, deviceModel.modelName)}">selected</c:if>
												>${deviceModel.manufacturer}-${deviceModel.modelName}
												</option>
											</c:forEach>			
										</select>
										시리얼번호 <input type="text" value="${param.sn}" 
											name="sn" id="search_sn" class="i_text" /> 
										<input type="submit" class="search_bt_green" id="btnSearchDevice" value="검색" />
									</div>
									<div class="search_form_div">
										<span>디바이스</span>
										<select name="device" id="search_deviceId">
											<option value="">선택</option>
										</select>
										<input name="Submit"
											type="submit" class="search_bt_green" id="btnSearchHistory" value="장애이력 검색" />
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
							class="table_type_nomal_scroll_height" id="history_table">
							<thead>
								<tr>
									<th id="hd_modelName">모델번호</th>
									<th id="hd_deviceId">디바이스ID</th>
									<th id="hd_sn">SN</th>
									<th id="hd_errorCode">에러코드</th>
									<th id="hd_errorGrade" class="history_manual_renderer">장애등급</th>
									<th id="hd_errorData">에러데이타</th>
									<th id="hd_errorTime">에러발생시간</th>
									<th id="hd_resourceUri">URI</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td colspan="8">디바이스를 선택한 후 장애이력을 검색하세요.</td>
								</tr>
							</tbody>
						</table>
						<div class="paging_wrap">

						</div>
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