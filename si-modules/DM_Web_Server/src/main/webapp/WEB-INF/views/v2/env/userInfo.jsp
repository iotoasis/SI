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
    <script type="text/javaScript" language="javascript">
		$(document).ready(function() {
			initUI();
		});
		
		function initUI() {
			$("#side-menu_operator").addClass("active");
			$("#side-menu_operator ul").addClass("in");
			$("#side-menu_operator ul li:eq(2)").addClass("active");
		}
		
		function fnDetail(){
			var myForm					= document.getElementById("myForm");
			myForm.actionType.value		= "U";
			myForm.action				= "<c:url value='/env/userInfo/info.do'/>";
		   	myForm.submit();
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
				<h2>내 정보 관리</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>운영 관리</li>
					<li class="active"><strong>내 정보 관리</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
<form id="myForm" name="myForm" method="post">
<input type="hidden" name="actionType" value="" />
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">내 정보</h3>
						</div>
						<div class="ibox-content form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-label">그룹명</label>
								<div class="col-sm-10">
									<p class="control-label" style="text-align:left;"><c:out value="${accountVO.mngAccountGroupNm}" /></p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">로그인 ID</label>
								<div class="col-sm-10">
									<p class="control-label" style="text-align:left;"><c:out value="${accountVO.loginId}" /></p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">이름</label>
								<div class="col-sm-10">
									<p class="control-label" style="text-align:left;"><c:out value="${accountVO.name}" /></p>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">이메일</label>
								<div class="col-sm-10">
									<p class="control-label" style="text-align:left;"><c:out value="${accountVO.email}" /></p>
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="text-center">
								<a class="btn btn-primary" href="javascript:fnDetail();">수정</a>
							</div>
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
