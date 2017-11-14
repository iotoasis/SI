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
var sub_num="5";

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
})


</script>
<script src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/firmware.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/firmwareStatus.js"></script>

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
					<h2>업그레이드 상태조회</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 펌웨어 관리 &gt; 상태조회
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
												<select name="deviceModelId" id="search_deviceModelId">
													<option value="">선택</option>
													<c:forEach items="${deviceModelList}" var="deviceModel">
														<option value="${deviceModel.oui}|${deviceModel.modelName}"														
														<c:if test="${fn:contains(param.deviceModelId, deviceModel.id)}">selected</c:if>
														>${deviceModel.manufacturer}-${deviceModel.modelName}
														</option>
													</c:forEach>			
												</select>
												시리얼번호 <input type="text" value="${param.sn}" 
													name="sn" id="search_sn" class="i_text" /> 
												<input type="submit" class="search_bt_green" id="btnSearchfirmware" value="검색" />
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
							class="table_type_nomal_scroll_height" id="firmware_table">
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
						
			        <%--페이징처리 START--%>
			        <div class="paging_wrap">
		            	       		
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