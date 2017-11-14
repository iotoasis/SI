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
	
	<script src="<c:url value="/v2/js/herit/user.js" />"></script>
	<script src="<c:url value="/v2/js/herit/hdb.js" />"></script>

	
    <script type="text/javaScript" language="javascript">
    	var validator;
    
		$(document).ready(function() {
			initUI();
			refreshUserList();
			
			validator = $("#modalForm").validate({
				rules : {
					modal_loginId : {	required : true, minlength: 3, maxlength: 12 },
					modal_name : { required : true, minlength: 3, maxlength: 12 },
					modal_password : { required : true },
					modal_password_confirm : { equalTo : "#modal_password" },
					modal_email : {required : true, email : true },
				},
				messages : {
					modal_loginId : { 
						required : "아이디를 입력하세요.",
						minlength : jQuery.validator.format("아이디는 {0}자 이상")
					},
					modal_name : {
						required : "이름을 입력하세요.",
						minlength : jQuery.validator.format("이름은 {0}자 이상")
					},
					modal_password : {
						required : "암호를 입력하세요."
					},
					modal_password_confirm : {
						equalTo : "암호를 다시 확인하세요."
					},
					modal_email : {
						required : "이메일주소를 입력하세요.",
						email : "올바른 이메일주소를 입력하세요."
					},
				}
			});
		});
		
		function initUI() {
			$("#side-menu_operator").addClass("active");
			$("#side-menu_operator ul").addClass("in");
			$("#side-menu_operator ul li:eq(1)").addClass("active");
		}
    
		function fnModal(options) {
			validator.resetForm()
			$("#modal_id").val("");
			$("#modal_loginId").val("");
			$("#modal_name").val("");
			$("#modal_email").val("");
			$("#modal_password").val("");
			$("#modal_password_confirm").val("");
			$("#modal_accountGroupId").val("2");
			
			switch (options.actionType) {
				case "I":
					$(".modal-title").html("사용자 등록");
					$("#modal_loginId").removeAttr("readonly");
					$(".modal-footer").html("<button type='button' onclick='execInsert(); return false;' class='btn btn-primary'>등록</button><button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>");
					break;
				
				case "V":
					$(".modal-title").html("사용자 정보 수정");
					var user = findUser(options.data.id);
					
					$("#modal_id").val(options.data.id);
					$("#modal_loginId").val(user.loginId).attr("readonly", "readonly");
					$("#modal_name").val(user.name);
					$("#modal_email").val(user.email);
					$("#modal_password").val("*****");
					$("#modal_password_confirm").val("*****");
					$("#modal_accountGroupId").val(user.accountGroupId);
					$(".modal-footer").html("<button type='button' onclick='execUpdate(); return false;' class='btn btn-primary'>수정</button><button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>");
					break;
			}
			$("#myModal").modal('show');
		}
		
		function fnInsert() {
			fnModal({
				actionType : "I"
			});
		}

		function execInsert() {
			if (!$('#modalForm').valid())
				return;
			
			var context = {
				"param" : {
					"name" : $("#modal_name").val(), 
					"loginId" : $("#modal_loginId").val(), 
					"loginPwd" : $("#modal_password").val(),
					"email" : $("#modal_email").val(), 
					"mngAccountGroupId" : $("#modal_accountGroupId").val()
				},
				"handler": ""
			};

			$.ajax({
				type: "POST",
				dataType: "json",
				url: "<c:url value="/api/hdp/account/info/insert.do" />",
				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				data: context.param,
				context: context
			}).done(function(msg) { 
				try {
					var handled = false;
					if (msg.result == 0) {
						alert("사용자를 등록하였습니다");
						$("#myModal").modal('hide');
						
						refreshUserList();
					} else {
						alert("요청 처리중 오류가 발생했습니다. 입력값을 확인 후 다시 시도해주세요.\r\nErrorCode:" + msg.errorCode +"\r\nErrorMsg:"+msg.content);				
					}
					
				} catch (e){
					alert("요청 처리중 오류가 발생했습니다. 관리자에게 문의해주세요.")
				}
			});
		}
		
		function fnUpdate(id) {
			fnModal({
				actionType : "V",
				data : {
					id : id
				}
			});
		}
		
		function execUpdate() {
			if (!$('#modalForm').valid())
				return;

			var context = {
				"param" : {
					"id" : $("#modal_id").val(),
					"name" : $("#modal_name").val(), 
					"loginId" : $("#modal_loginId").val(), 
					"loginPwd" : $("#modal_password").val() == "*****" ? null : $("#modal_password").val(),
					"email" : $("#modal_email").val(), 
					"mngAccountGroupId" : $("#modal_accountGroupId").val()
				},
				"handler": ""
			};

			$.ajax({
				type: "POST",
				dataType: "json",
				url: "<c:url value="/api/hdp/account/info/set.do" />",
				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				data: context.param,
				context: context
			}).done(function(msg) { 
				try {
					var handled = false;
					if (msg.result == 0) {
						alert("사용자 정보를 수정하였습니다");
						$("#myModal").modal('hide');
						
						refreshUserList();
					} else {
						alert("요청 처리중 오류가 발생했습니다. 입력값을 확인 후 다시 시도해주세요.\r\nErrorCode:" + msg.errorCode +"\r\nErrorMsg:"+msg.content);				
					}
					
				} catch (e){
					alert("요청 처리중 오류가 발생했습니다. 관리자에게 문의해주세요.")
				}
			});
		}
		
		
		function fnDelete(id) {
			if (!confirm("사용자를 삭제 하시겠습니까?"))
				return;
			
			execDelete(id);
		}
		
		function execDelete(id) {
			var context = {
				"param" : {
					"id" : id
				},
				"handler": ""
			};

			$.ajax({
				type: "POST",
				dataType: "json",
				url: "<c:url value="/api/hdp/account/info/delete.do" />",
				contentType: "application/x-www-form-urlencoded; charset=UTF-8",
				data: context.param,
				context: context
			}).done(function(msg) { 
				try {
					var handled = false;
					if (msg.result == 0) {
						alert("사용자 정보를 삭제하였습니다");
						$("#myModal").modal('hide');
						
						refreshUserList();
					} else {
						alert("요청 처리중 오류가 발생했습니다. 입력값을 확인 후 다시 시도해주세요.\r\nErrorCode:" + msg.errorCode +"\r\nErrorMsg:"+msg.content);				
					}
					
				} catch (e){
					alert("요청 처리중 오류가 발생했습니다. 관리자에게 문의해주세요.")
				}
			});
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
				<h2>사용자 정보 관리</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>운영 관리</li>
					<li class="active"><strong>사용자 정보 관리</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">사용자 정보</h3>
						</div>
						<div class="ibox-content">
							<div class="panel">
								<div class="panel-heading">
									<div class="panel-options">
										<ul class="nav nav-tabs">
											<li class="active"><a data-toggle="tab" href="#tab-admin">ADMIN</a></li>
											<li class=""><a data-toggle="tab" href="#tab-manager">MANAGER</a></li>
											<li style="float: right;"><button type="button" onclick="fnInsert(); return false;" class="btn btn-sm btn-primary">사용자 등록</button></li>
										</ul>
									</div>
								</div>
								
								<div class="panel-body">
									<div class="tab-content">
										<div id="tab-admin" class="tab-pane active">
											<table class="table table-bordered table-hover" id="admin_table">
												<thead>
													<tr>
														<th id="adm_th_name">이름</th>
														<th id="adm_th_loginId">ID</th>
														<th id="adm_th_email">이메일 주소</th>
														<th id="adm_th_lastAccessTime">최종 로그인시간</th>
														<th id="adm_th_updateTime">수정시간</th>
														<th id="adm_th_modify" class="manual_renderer">수정</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
											</table>											
										</div>
									
										<div id="tab-manager" class="tab-pane">
											<table class="table table-bordered table-hover" id="manager_table">
												<thead>
													<tr>
														<th id="adm_th_name">이름</th>
														<th id="adm_th_loginId">ID</th>
														<th id="adm_th_email">이메일 주소</th>
														<th id="adm_th_lastAccessTime">최종 로그인시간</th>
														<th id="adm_th_updateTime">수정시간</th>
														<th id="adm_th_modify" class="manual_renderer">수정</th>
													</tr>
												</thead>
												<tbody>
												</tbody>
											</table>											
										</div>
									</div>
								</div>
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


<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form id="modalForm">
<input type="hidden" id="modal_id" name="modal_id" />
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>로그인ID</dt>
						<dd><input type="text" id="modal_loginId" name="modal_loginId" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>이름</dt>
						<dd><input type="text" id="modal_name" name="modal_name" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>이메일 주소</dt>
						<dd><input type="text" id="modal_email" name="modal_email" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>패스워드</dt>
						<dd><input type="password" id="modal_password" name="modal_password" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>패스워드 확인</dt>
						<dd><input type="password" id="modal_password_confirm" name="modal_password_confirm" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>그룹</dt>
						<dd>
							<select name="modal_accountGroupId" id="modal_accountGroupId" class="form-control input-sm" >
								<option value="2">ADMIN</option>
								<option value="3">MANAGER</option>
							</select>
						</dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
			</div>
		</div>
</form>
	</div>
</div>


</body>
</html>
