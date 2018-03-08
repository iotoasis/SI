<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/tld/herit.tld" prefix="herit" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
    <title>OASIS SI Admin</title>
	<%@ include file="/WEB-INF/views/v2/common/common_js.jsp"%>

	<script language="javascript" src="<c:url value="/v2/js/herit/hdb.js" />"></script>
	<script language="javascript" src="<c:url value="/v2/js/herit/deviceRegister.js" />"></script>
	
	
    <script type="text/javaScript" language="javascript">
	    var gr_id="device";
	    
		$(document).ready(function() {
			initUI();
			initUI2();
		});
		
		function initUI() {
			$("#side-menu_device").addClass("active");
			$("#side-menu_device ul").addClass("in");
			$("#side-menu_device ul li:eq(4)").addClass("active");
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
				<h2>디바이스 일괄 등록</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>디바이스 관리</li>
					<li class="active"><strong>디바이스 일괄 등록</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">디바이스 정보 입력</h3>
						</div>
						<div class="ibox-content form-horizontal">
							<div class="form-group">
								<label class="form-inline" style="width:100%;">
									<input type="text" id="fileName" class="form-control" placeholder="파일을 첨부해주세요" readonly="readonly" style="width:50%;" />
									<a class="btn btn-sm btn-primary" href="javascript:document.getElementById('btnFileName').click();">파일선택</a>
									<input type="file" id="btnFileName" style="display:none;" onchange="javascript: document.getElementById('fileName').value = this.value" />
								<a class="btn btn-sm btn-primary" href="javascript:registersDevice();">저장</a>
								</label>
							</div>
						</div>
					</div>
					
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">디바이스 목록</h3>
						</div>
							
						<div id="dvCSV" class="ibox-content form-horizontal">
							<table id="csvTable" class="table table-bordered table-hover">
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
		<div class="footer">
			<%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
		</div>
	</div>
</div>


</body>
</html>
