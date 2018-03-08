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
var sub_num="5";
$(document).ready(function() {
	initUI();
})
</script>
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/deviceRegister.js"></script>
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
					<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<!--<div class="search_title">검색설정</div> -->
								<form action="">
									<div class="search_form_wrap">
										<div class="search_form_div">
											<input type="text" id="fileName" class="file_text" placeholder="파일을 첨부해주세요" readonly="readonly" />
											<div class="file_button">
												<input type="button" id="btnFileSelect" class="search_bt_green" value="파일선택" />
												<input type="file" id="btnFileName" class="file_input_hidden" onchange="javascript: document.getElementById('fileName').value = this.value" />
											</div>
											<input name="button" type="button" class="search_bt_green" id="btnRegisters" value="저장" />
											
											<!-- <div id="dvCSV"></div> -->
										</div>
									</div>
								</form>
							</div>
						</div>
					<!-- csv 파일 읽은 목록 -->	
					<div class="content_title_box obstacle_title">
						<p class="content_title">디바이스 목록</p>
					</div>
					<div id="dvCSV" class="device_list_wrap_100py2">
						<table id="csvTable" border="0" cellspacing="0" cellpadding="0" class="table_type_nomal_scroll_height">
							<!-- <tr>
								<th>OUI</th>
								<th>MODELNAME</th>
								<th>SN</th>
								<th>AUTH_ID</th>
								<th>AUTH_PWD</th>
							</tr> -->
						</table>
					</div>
						<!-- <div class="content_title_box" style="height: 24px">
							<p class="content_title">파일 입력</p>
						</div>
						<div class="bott_mar_30">
							<div>
								<div class="search_form_only">
									<form action="">
										<div class="search_form_wrap">
											<div class="search_form_div">
												<input type="file" size="100"> 
													<input name="Submit" type="submit" class="search_bt_green"
													value="저장" />
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
						 -->
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