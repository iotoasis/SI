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
		
		function fnUpdate(){
			if (!$("#myForm").validate({
				//rules: validationRules
			}).form()) {return;}			
			var myForm					= document.getElementById("myForm");
			myForm.action				= "<c:url value='/env/userInfo/update.do'/>";
		   	myForm.submit();
		}
		
		/* ********************************************************
		 * 비밀번호 변경 체크 처리
		 **********************************************************/
		function changeCheck(){
			var myForm					= document.getElementById("myForm");
			myForm.changeYn.value		= "Y";
		}
		function changeYn2(){
			var myForm					= document.getElementById("myForm");
			if(myForm.changeYn.value != "Y"){
				$("#myForm").find("[name=loginPwd]").val("");
				$("#myForm").find("[name=confirmLoginPwd]").val("");
			}
		}		
		
    
		$(function() {
			jQuery.validator.addMethod("checkPassword", function(value, element) {
				return this.optional(element) || /((?=.*\d)(?=.*[a-zA-z])(?=.*[!@#$%^&*]))/gm.test(value);
			}); 			
			jQuery.validator.addMethod("ipArrayList", function(value, element) {
				return this.optional(element) || /^[0-9.]+$/.test(value);
			}); 
			// validate signup form on keyup and submit
			$("#myForm").validate({
				rules : {
					mngAccountGroupId : "required",
					name : {
						required : true,
						maxlength : 15
					},
					loginPwd : {
						required : true,
						minlength : 8,
						maxlength : 20,
						checkPassword : true
					},
					confirmLoginPwd : {
						required : true,
						minlength : 8,
						maxlength : 20,
						equalTo : "#loginPwd"
					},
					email : {
						required : true,
						email : true
					},
					phone : {
						digits : true,
						maxlength : 20
					},
					mobile : {
						digits : true,
						maxlength : 20
					},
					"ipArrayList" : {
						maxlength : 15,
						ipArrayList : true
					}
				},
				messages : {
					mngAccountGroupId : "그룹명을 입력해 주세요",
					name : {
						required : "이름을 입력해 주세요",
						maxlength : "이름은 {0}자 이하이어야 합니다."
					},
					loginPwd : {
						required : "비밀번호를 입력해 주세요",
						minlength : "비밀번호는 {0}자 이상이어야 합니다.",
						maxlength : "비밀번호는 {0}자 이하이어야 합니다.",
						checkPassword : "알파벳과 숫자, 특수문자를 조합하여 입력하세요."
					},
					confirmLoginPwd : {
						required : "비밀번호를 한 번 더 입력해 주세요",
						minlength : "비밀번호는 {0}자 이상이어야 합니다.",
						maxlength : "비밀번호는 {0}자 이하이어야 합니다.",
						equalTo : "비밀번호가 일치하지 않습니다."
					},
					email : "형식에 맞는 이메일을 입력해 주세요.",
					phone : {
						digits : "숫자만 입력하세요",
						maxlength : "전화번호는 {0}자 이하이어야 합니다."
					},
					mobile : {
						digits : "숫자만 입력하세요",
						maxlength : "휴대폰 번호는 {0}자 이하이어야 합니다."
					},
					"ipArrayList" : {
						maxlength : "",
						ipArrayList : ""
					}
				}
			});		
			
			
			
			//처리 결과 
			if("${result}" != "" && "${retCod}" == "0"){
				//alert("${message}");
				//fnDetail();
				alert("정상적으로 등록 처리되었습니다.\n변경된 정보를 반영하기 위헤 재로그인 하셔야 합니다.");
				location.href = "<c:url value="/security/logout.do" />";				
			}else if("<c:out value="${retCod}" />" == "1"){
				alert("<c:out value="${message}" />");
			}
			
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
<input type="hidden" name="actionType" value="<c:out value="${accountVO.actionType}" />">
<input type="hidden" id="id" name="id" value="<c:out value="${accountVO.id}" />" />
<input type="hidden" id="changeYn" name="changeYn" value="" />
<input type="hidden" id="disabled" name="disabled" value="1" />
<input type="hidden" id="mobile" name="mobile" value="<c:out value="${accountVO.mobile}" />" />
<input type="hidden" id="phone" name="phone" value="<c:out value="${accountVO.phone}" />" />
<input type="hidden" id="department" name="department" value="<c:out value="${accountVO.department}" />" />
<input type="hidden" id="mngAccountGroupId" name="mngAccountGroupId" value="<c:out value="${accountVO.mngAccountGroupId}" />" />

			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">내 정보 수정</h3>
						</div>
						<div class="ibox-content form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-label">로그인 ID</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="loginId" name="loginId" value="${accountVO.loginId}" readonly="readonly" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">이름</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="name" name="name" value="${accountVO.name}" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">이메일</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="email" name="email" value="${accountVO.email}" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">패스워드</label>
								<div class="col-sm-10">
									<input type="password" class="form-control" id="loginPwd" name="loginPwd" value="abcd1234!" onkeydown="changeCheck();" onfocus="changeYn2();" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">패스워드 확인</label>
								<div class="col-sm-10">
									<input type="password" class="form-control" id="confirmLoginPwd" name="confirmLoginPwd" value="abcd1234!" onkeydown="changeCheck();" />
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
