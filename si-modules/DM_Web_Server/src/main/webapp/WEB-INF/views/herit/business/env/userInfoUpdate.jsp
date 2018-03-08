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
<title>HIT DM Admin - Login</title>
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp"%>

	<script type="text/javaScript" language="javascript">
	var gr_id="information";
	var sub_num="3";


	

	/* ********************************************************
	 * 상세 처리
	 **********************************************************/
	function fnDetail(){
		var myForm				 = document.getElementById("myForm");
		myForm.actionType.value  = "V";
		myForm.action           = "<c:url value='/env/userInfo/info.do'/>";
	   	myForm.submit();
	}		
	/* ********************************************************
	 * 수정 처리
	 **********************************************************/
	function fnUpdate(){
		if (!$("#myForm").validate({
			//rules: validationRules
		}).form()) {return;}			
		var myForm				 = document.getElementById("myForm");
		myForm.action           = "<c:url value='/env/userInfo/update.do'/>";
	   	myForm.submit();
	}
	/* ********************************************************
	 * 비밀번호 변경 체크 처리
	 **********************************************************/
	function changeCheck(){
		var myForm				 = document.getElementById("myForm");
		myForm.changeYn.value = "Y";
	}
	function changeYn2(){
		var myForm				 = document.getElementById("myForm");
		if(myForm.changeYn.value != "Y"){
			$("#myForm").find("[name=loginPwd]").val("");
			$("#myForm").find("[name=confirmLoginPwd]").val("");
		}
	}		
	/* ********************************************************
	 * 최초 실행
	 ******************************************************** */
	$(function() {
		
		//대메뉴 활성화 
		$("#01").removeClass("gnb_active");
		$("#02").removeClass("gnb_active");
		$("#03").removeClass("gnb_active");
		$("#04").removeClass("gnb_active");
		//서브메뉴 활성화
		$("#05_01").addClass("snb_active");
		
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
			location.href="${pageContext.request.contextPath}/security/authenticate.do";				
		}else if("${retCod}" == "1"){
			alert("${message}");
		}
		
	});
	</script>
	
	<script language="javascript" src="${pageContext.request.contextPath}/jquery/js/jquery.validate.js"></script>
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
					<h2>내정보 관리</h2>
					<div class="position_text">
						
					</div>
				</div>

				<div class="content_wrap top_pad_30">

					<div class="content_title_box" style="height: 24px">
						<p class="content_title">정보</p>
					</div>

					<!-- Search-->
					<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<form id="myForm" name="myForm" action="${pageContext.request.contextPath}/env/userInfo/info.do" method="post">
									<input type="hidden" name="actionType" value="${accountVO.actionType}">
									<input type="hidden" id="id" name="id" value="${accountVO.id}" />
									<input type="hidden" id="changeYn" name="changeYn" value="" />
									<input type="hidden" id="disabled" name="disabled" value="1" />
									<input type="hidden" id="mobile" name="mobile" value="${accountVO.mobile}" />
									<input type="hidden" id="phone" name="phone" value="${accountVO.phone}" />
									<input type="hidden" id="department" name="department" value="${accountVO.department}" />
									<input type="hidden" id="mngAccountGroupId" name="mngAccountGroupId" value="${accountVO.mngAccountGroupId}" />
																		
									<div class="search_form_wrap" style="padding:20px;">
										<table>
										<tr>
											<td height="30" width="150">로그인 ID</td>
											<td>							
												<input type="text" id="loginId" name="loginId" value="${accountVO.loginId}" class="i_text focus_input al readonly" size="40" readonly />
											</td>
										</tr>
										<tr>
											<td height="30" width="150">이름</td>
											<td>
												<input type="text" id="name" name="name" value="${accountVO.name}" class="i_text focus_input al" size="40"/>
											</td>
										</tr>
										<tr>
											<td height="30" width="150">이메일</td>
											<td>
												<input type="text" id="email" name="email" value="${accountVO.email}" class="i_text focus_input al" size="40"/>
											</td>
										</tr>
										<tr>
											<td height="30" width="150">패스워드</td>
											<td>
												<input type="password" id="loginPwd" name="loginPwd" value="abcd1234!" onkeydown="changeCheck();" onfocus="changeYn2();" class="i_text focus_input al" size="40"/>
											</td>
										</tr>
										<tr>
											<td height="30" width="150">패스워드 확인</td>
											<td>
												<input type="password" id="confirmLoginPwd" name="confirmLoginPwd" value="abcd1234!" onkeydown="changeCheck();" class="i_text focus_input al" size="40"/>
											</td>
										</tr>
						
							
						
										<tr>
											<td height="30" width="150">&nbsp;</td>
											<td height="30" colspan="2">
											<input name="Submit" type="submit" class="search_bt_green" value="저장" onclick="javascript:fnUpdate();"/>
											</td>
										</tr>
										</table>
									</div>
								</form>
							</div>
						</div>    
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