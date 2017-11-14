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
			$("#side-menu_oneM2M ul li:eq(1)").addClass("active");
		}
		
		function fnInsert() {
			if (!$("#myForm").validate({
			}).form()) {return;}
			
			$("#btn").hide();
			
			$.ajax({
				type: "POST",
				url: "<c:url value="/onem2m/platform/insert.do" />",
				data: {
					spId : $("#spId").val(),
					serverName : $("#serverName").val(),
					serverHost : $("#serverHost").val(),
					serverPort : $("#serverPort").val(),
					protocol : $("#protocol").val(),
					cseId : $("#cseId").val(),
					cseName : $("#cseName").val(),
					maxTps : $("#maxTps").val()
				},
				dataType: "json",
				success: function(response){
					if (response.result) {
						alert("플랫폼 연동을 등록하였습니다!");
						window.location.href = "<c:url value="/onem2m/platform/list.do" />";
					}
					else {
						alert(response.message);
						$("#btn").show();
					}
				},
				error: function(e){
					alert("플랫폼 연동을 등록중 에러가 발생하였습니다!");
					$("#btn").show();
				}
			});
		}

		$(function() {
			$("#myForm").validate({
				rules : {
					spId : {required : true},
					serverName : {required : true},
					serverHost : {required : true},
					serverPort : {required : true, number: true},
					cseId : {required : true},
					cseName : {required : true},
					maxTps : {required : true, number: true}
				},
				messages : {
					spId : {required : "SP ID를 입력해 주세요"},
					serverName : {required : "서버명을 입력해 주세요"},
					serverHost : {required : "서버 호스트를 입력해 주세요"},
					serverPort : {required : "서버 포트를 입력해 주세요", number: "숫자만 입력하세요"},
					cseId : {required : "CSE ID를 입력해 주세요"},
					cseName : {required : "CSEbase 이름을 입력해 주세요"},
					maxTps : {required : "최대 TPS를 입력해 주세요", number: "숫자만 입력하세요"}
					
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
			<div class="col-sm-12">
				<h2>플랫폼 연동 관리</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>oneM2M 서버 관리</li>
					<li class="active"><strong>플랫폼 연동 관리</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
<form id="myForm" name="myForm" method="post">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">플랫폼 연동 등록</h3>
						</div>
						<div class="ibox-content form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-label">SP ID</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="spId" name="spId" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">서버명</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="serverName" name="serverName" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">서버 호스트</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="serverHost" name="serverHost" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">서버 포트</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="serverPort" name="serverPort" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">연결 프로토콜</label>
								<div class="col-sm-10">
									<select class="form-control" id="protocol" name="protocol">
										<option value="HTTP" >HTTP</option>
										<option value="MqTT" >MqTT</option>
										<option value="CoAP" >CoAP</option>
									</select>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">CSE ID</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="cseId" name="cseId" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">CSEbase 이름</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="cseName" name="cseName" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">최대 TPS</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="maxTps" name="maxTps" />
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div id="btn" class="text-center">
								<a class="btn btn-primary" href="javascript:fnInsert();">저장</a>
								<a class='btn btn-danger' href="<c:url value="/onem2m/platform/list.do" />">취소</a>								
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
