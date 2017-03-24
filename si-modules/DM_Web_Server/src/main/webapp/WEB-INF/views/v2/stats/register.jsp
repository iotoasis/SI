<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
    <title>OASIS SI Admin</title>
	<%@ include file="/WEB-INF/views/v2/common/common_js.jsp"%>

	<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">
	<script src="http://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
	<script src="<c:url value="/v2/js/herit/statsRegister.js" />"></script>
	
    <script type="text/javaScript" language="javascript">
		$(document).ready(function() {
			initUI();
			initUI2();
		});
		
		function initUI() {
			$("#side-menu_statistics").addClass("active");
			$("#side-menu_statistics ul").addClass("in");
			$("#side-menu_statistics ul li:eq(0)").addClass("active");
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
				<h2>등록 통계</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>통계</li>
					<li class="active"><strong>등록 통계</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
<form id="myForm" name="myForm" method="post">

			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">등록 통계</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-12">
									<label class="form-inline">
										기간&nbsp;
										<select id="selSearchType" class="form-control input-sm">
											<option value="day">일별</option>
											<option value="month">월별</option>
										</select>
										&nbsp; &nbsp; &nbsp; &nbsp; 
										시작&nbsp;
										<input type="text" id="dpStart" class="date_picker input-sm form-control" style="width:100px;"></input>
										&nbsp; ~ &nbsp; 
										종료&nbsp;
										<div class="input-group">
											<input type="text" id="dpEnd" class="date_picker input-sm form-control" style="width:100px;"></input>
											<span class="input-group-btn">
												<button type="button" id="btnSearch" class="btn btn-sm btn-primary"> 조회</button>
											</span>
										</div>
									</label>
								</div>
							</div>

							<div>&nbsp;</div>

							<table id="stat_table" class="table table-bordered table-hover">
								<thead>
									<tr>
										<th id="hd_statDate" width="50%">기간</th>
										<th id="hd_statCount" width="50%">등록수</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td colspan="2">-</td>
									</tr>
								</tbody>
							</table>						
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

</body>

</html>
