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
<title>HIT DM Admin - Firmware Detail</title>
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp"%>
<script type="text/javascript">
var gr_id="stats";
var sub_num="2";


$(document).ready(function() {
	
	initUI();
})
</script>

<script language="javascript" src="${pageContext.request.contextPath}/js/herit/statsUsage.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
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
					<h2>메시지 통계</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 통계 &gt; 사용 통계
					</div>
				</div>

				<div class="content_wrap top_pad_30">
					<!-- /-->
					<div class="search_form_wrap">
						<div class="search_form">
								<div class="search_title">조회</div>
								<!--3단-->
								<div class="search_table">
									<div class="th">기간</div>
									<div class="td10">
										<select id="selSearchType">
											<option value="day">일별</option>
											<option value="month">월별</option>
										</select>
									</div>

									<div class="th">시작</div>
									<div class="td4">
										<input type="text" id="dpStart" class="date_picker"></input> 
									</div>
									<div class="th">종료</div>
									<div class="td4">
										<input type="text" id="dpEnd" class="date_picker"></input> 
									</div>
								</div>
								<!--3단 end-->
						</div>
						<div class="search_bottom">
							<input id="btnSearch" name="Submit" type="submit" class="search_bt_green" value="검색" /> 
							<!-- <input name="Submit" type="submit" class="search_bt_gray" value="엑셀 다운로드" /> -->
						</div>
					</div>

					<!-- /-->
					<table width="100%" border="0" cellspacing="0" cellpadding="0"
						class="table_type_nomal_scroll3" id="stat_table">
						<thead>
							<tr>
								<th>기간</th>
								<th>에러메시지수</th>
								<th>제어메시지수</th>
								<th>펌웨어업데이트메시지수</th>
							</tr>
						</thead>
						<tbody>
						<tr>
							<td colspan="4">-</td>
						</tr>
						</tbody>
					</table>
					
				</div>
			</div>
		</div>
		
		<!-- /#container -->
		<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
	</div>
	<!-- /#wrapper -->

</body>
</html>