<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>관리자 로그인</title>
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>	
	<script type="text/javaScript" language="javascript">

		/* ********************************************************
		 * 로그인 처리
		 **********************************************************/	
		function fnLogin(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/security/authenticate.do'/>";
		   	myForm.submit();
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
			$("#00_01").addClass("snb_active");

			// validate signup form on keyup and submit
			$("#myForm").validate({
				rules : {
					loginId : {
						required : true
					},
					loginPassword : {
						required : true
					}
				},
				messages : {
					loginId : {
						required : ""
					},
					loginPassword : {
						required : ""
					}
				}
			});

			
			//처리 결과 
			if("${result}" != "" && "${retCod}" == "0"){
				alert("${message}");
				fnDetail();
			}else if("${retCod}" == "1"){
				alert("${message}");
				fnDetail();
			}			
			
		});
	</script>
		
</head>
<body>



<div id="wrapper">
	<!-- Header 시작 -->
	<%@ include file="/WEB-INF/views/herit/common/header/header.jsp" %>
	<!-- Header 끝 -->
					
	

	<div class="position_text">
		<div class="wrap_div">
			<a href="${pageContext.request.contextPath}"><img src="${pageContext.request.contextPath}/images/common/home_icon.gif" alt="" /></a>
			<span><a href="${pageContext.request.contextPath}/security/authenticate.do">로그인</a></span>
		</div>
	</div>

	
	<!-- .container_wrap -->
	<div class="container_wrap">
		<!-- .snb -->
		<%@ include file="/WEB-INF/views/herit/common/left/menu.jsp" %>
		<!-- /.snb -->
           
           

		<!-- #container -->
		<div id="container">    
		<form id="myForm" name="myForm" action="${pageContext.request.contextPath}/security/authenticate.do" method="post">
				
				
			<div class="contents_title">관리자 로그인</div>
			
			<!--로그인 레이어 -->
			<div class="member_wrap">			
				<p class="member_title1">관리자 로그인</p>
				<!--p class="member_title2">홈페이지 회원이란?</p>
				<p class="member_title3">LG전자 사이트에 가입하신 분으로 LG전자 통합 아이디 회원입니다.</p-->
			
				<div class="member_form">				                  
					<div class="member_input_wrap">
						<div>
							<span><img src="${pageContext.request.contextPath}/images/common/loginbox_id.gif" alt="아이디" /></span>
							<input type="text" name="loginId" id="loginId" />
						</div>
						<div>
							<span><img src="${pageContext.request.contextPath}/images/common/loginbox_password.gif" alt="패스워드" /></span>
							<input type="password" name="loginPassword" id="loginPassword" />
						</div>
					</div>					
					<div class="member_submit_button">
						<a href="javascript:fnLogin();"><input type="image" src="${pageContext.request.contextPath}/images/common/loginbox_loginbt.gif" alt="로그인" /></a>
					</div>
				</div>
				<!--<span class="top_pad_5"><input type="checkbox" name="input">value="Y" checked<span class="left_pad_5">ID 저장하기</span></span>-->	
				
				<c:if test="${error_txt != ''}">     
					<div class="member_footer padding10">
						<p class="member_title2">로그인 실패</p>
						<p class="member_title3">${error_txt}</p>
					</div>
				</c:if>            
				 	                        
			</div>
			             
		</form>
		</div>
		<!-- #container -->
	</div>
	<!-- .container_wrap -->

	 	
	<!-- Footer 시작 --> 
	<%@ include file="/WEB-INF/views/herit/common/footer/footer.jsp" %>
	<!-- Footer 끝 --> 	
</div>		
<!-- /#wrapper -->		

</body>
</html>