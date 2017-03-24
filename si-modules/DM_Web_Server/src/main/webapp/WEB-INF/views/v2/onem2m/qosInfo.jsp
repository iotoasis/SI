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
			$("#side-menu_oneM2M").addClass("active");
			$("#side-menu_oneM2M ul").addClass("in");
			$("#side-menu_oneM2M ul li:eq(2)").addClass("active");
		}

		function fnUpdate(){
			if (!$("#myForm").validate({
				//rules: validationRules
			}).form()) {return;}
			
			if (!confirm("Qos를 수정하시겠습니까?"))
				return
			
			var myForm					= document.getElementById("myForm");
			myForm.action				= "<c:url value='/onem2m/qos/update.do'/>";
		   	myForm.submit();
		}

		
		$(function() {
			$("#myForm").validate({
				rules : {
					maxPollingSessionNo : {
						required : true,
						number: true
					},
					maxAENo : {
						required : true,
						number: true
					},
					maxCSENo : {
						required : true,
						number: true
					},
					maxTps : {
						required : true,
						number: true
					}
				},
				messages : {
					maxPollingSessionNo : {
						required : "최대 폴링 세션수를 입력해 주세요",
						digits : "숫자만 입력하세요"
					},
					maxAENo : {
						required : "최대 AE 등록수를 입력해 주세요",
						digits : "숫자만 입력하세요"
					},
					maxCSENo : {
						required : "최대 CSE 등록수를 입력해 주세요",
						digits : "숫자만 입력하세요"
					},
					maxTps : {
						required : "최대 TPS를 입력해 주세요",
						digits : "숫자만 입력하세요"
					}
				}
			});
		});
		
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
				<h2>QoS 관리</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>oneM2M 서버 관리</li>
					<li class="active"><strong>QoS 관리</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
<form id="myForm" name="myForm" method="post">
<input type="hidden" name="id" value="<c:out value="${configurationVO.id}" />" />
<input type="hidden" name="CONFIGURATION_NAME" value="<c:out value="${configurationVO.CONFIGURATION_NAME}" />" />
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">Qos 관리</h3>
						</div>
						<div class="ibox-content form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-label">최대 폴링 세션수</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="maxPollingSessionNo" name="maxPollingSessionNo" value="<c:out value="${configurationVO.maxPollingSessionNo}" />" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">최대 AE 등록수</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="maxAENo" name="maxAENo" value="<c:out value="${configurationVO.maxAENo}" />" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">최대 CSE 등록수</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="maxCSENo" name="maxCSENo" value="<c:out value="${configurationVO.maxCSENo}" />" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">최대 TPS</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="maxTps" name="maxTps" value="<c:out value="${configurationVO.maxTps}" />" />
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="text-center">
								<a class="btn btn-primary" href="javascript:fnUpdate();">저장</a>
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
