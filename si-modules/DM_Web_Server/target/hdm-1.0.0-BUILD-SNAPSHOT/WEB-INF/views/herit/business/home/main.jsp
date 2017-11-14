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
	var gr_id = "home";
	var sub_num = "1";
	var contextPath = "${pageContext.request.contextPath}";
	
	$(document).ready(function() {
		
		initUI();
		
	})
</script>

<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>

<script src="${pageContext.request.contextPath}/js/herit/main.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>

<script src="${pageContext.request.contextPath}/js/herit/map_google.js"></script>

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



				<div class="content_wrap">
					<div class="content_left">

						<div class="content_title_box">
							<p class="content_title">디바이스 현황</p>
							<!-- <a href="#" class="more">상세보기</a> -->
						</div>

						<div class="device_cond_wrap">

							<table>
								<tr>
									<td>
										<div class="device_cond">
											<div class="table_header">
												<div class="th col2">
													<div>분류</div>
												</div>
												<div class="th col3">
													<div>디바이스수</div>
												</div>
											</div>
											<ul class="table_body">
												<li>
													<div class='col2'>전체 등록수</div>
													<div class='col3'>
														<span id="totalRegCount">...</span>개
													</div>
												</li>
												<li>
													<div class='col2'>오늘 등록수</div>
													<div class='col3'>
														<span id="todayRegCount">...</span>개
													</div>
												</li>
												<li>
													<div class='col2'>최근1주일 등록수</div>
													<div class='col3'>
														<span id="weekRegCount">...</span>개
													</div>
												</li>
												<li>
													<div class='col2'>최근1개월 등록수</div>
													<div class='col3'>
														<span id="monthRegCount">...</span>개
													</div>
												</li>
											</ul>
										</div>

									</td>
									<td>

										<div class="device_cond" style="margin-left: 2px">
											<div class="table_header">
												<div class="th col2">
													<div>상태</div>
												</div>
												<div class="th col3">
													<div>디바이스수</div>
												</div>
											</div>
											<ul class="table_body">
												<li>
													<div class='col2'>NORMAL</div>
													<div class='col3'>
														<span id="normalCount">...</span>개
													</div>
												</li>
												<li>
													<div class='col2'>CRITICAL</div>
													<div class='col3'>
														<span id="criticalCount">...</span>개
													</div>
												</li>
												<li>
													<div class='col2'>MAJOR</div>
													<div class='col3'>
														<span id="majorCount">...</span>개
													</div>
												</li>
												<li>
													<div class='col2'>MINOR</div>
													<div class='col3'>
														<span id="minorCount">...</span>개
													</div>
												</li>
											</ul>
										</div>
									</td>
								</tr>
							</table>
						</div>

						<div class="content_title_box obstacle_title">
							<p class="content_title">디바이스모델 등록 현황</p>
							<!-- <a href="#" class="more">상세보기</a> -->
						</div>


						<div class="device_cond_wrap">

							<div class="trafic_graph_bottom">
								등록모델수: <span id="deviceModelCount">5</span>개
							</div>

							<div id="deviceModel_table_div" style="overflow-y:scroll; height:100px">
								<table border="0" cellspacing="0" cellpadding="0"
									class="table_type_nomal_scroll" id="deviceModel_table">
									<thead>
										<tr>
											<th id="hd_dm_manufacturer">제조사</th>
											<th id="hd_dm_modelName">모델번호</th>
											<th id="hd_dm_createTime">등록일자</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td colspan="3">데이터 로딩중...</td>
										</tr>
									</tbody>
								</table>
							</div>

						</div>

						<div class="content_title_box obstacle_title">
							<p class="content_title">펌웨어 등록 현황</p>
							<!-- <a href="#" class="more">상세보기</a> -->
						</div>

						<div class="device_cond_wrap">


							<div class="trafic_graph_bottom">
								등록펌웨어수: <span id="firmwareCount">5</span>개
							</div>

							<div id="package_table_div" style="overflow-y:scroll; height:100px">
								<table border="0" cellspacing="0" cellpadding="0"
									class="table_type_nomal_scroll" id="package_table">
									<thead>
										<tr>
											<th id="hd_fw_manufacturer">제조사</th>
											<th id="hd_fw_modelName">모델번호</th>
											<th id="hd_fw_package">패키지명</th>
											<th id="hd_fw_createTime">등록일자</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td colspan="4">데이터 로딩중...
												</th>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
					<div class="content_right">
						<div class="content_title_box">
							<p class="content_title">장애현황 
							<img src="${pageContext.request.contextPath}/images/hitdm/common/refresh_btn.gif"  class="image_btn" id="refleshErrorBtn">
							</p>
						</div>
						<div class="error_status_wrap">
							<div class="error_status_area">
								<div id="map-canvas" style="height: 250px"></div>
							</div>
							<div class="error_status_bottom">
								<input type="checkbox" name="" id="cb_showCritical" checked class="ch_errorGrade" /> <label for="cb_showCritical">CRITICAL</label> 
								<input type="checkbox" name="" id="cb_showMajor" checked class="ch_errorGrade" /> <label for="cb_showMajor">MAJOR</label> 
								<input type="checkbox" name="" id="cb_showMinor" checked class="ch_errorGrade" /> <label for="cb_showMinor">MINOR</label> 
								<input type="checkbox" name="" id="cb_showNormal" checked class="ch_errorGrade" /> <label for="cb_showNormal">NORMAL</label>
							</div>

							<br>
							<div id="error_table_div" style="overflow-y:scroll; height:200px">
								<table border="0" cellspacing="0" cellpadding="0"
									class="table_type_nomal_scroll" id="error_table">
									<thead>
										<tr>
											<th id="hd_er_createTime">발생시간</th>
											<th id="hd_er_modelName">모델명</th>
											<th id="hd_er_sn" class="manual_renderer">SN</th>
											<th id="hd_er_errorGrade" class="manual_renderer">등급</th>
											<th id="hd_er_errorCode">코드</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td colspan="4">데이터 로딩중...</td>
										</tr>
									</tbody>
								</table>
							</div>
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