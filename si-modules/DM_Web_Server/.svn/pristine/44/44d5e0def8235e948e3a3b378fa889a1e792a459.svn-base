<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>사용자 로그인</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">

		/* ********************************************************
		 * 등록 처리
		 **********************************************************/

		function fnLogin(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/security/authenticate.do'/>";
		   	myForm.submit();
		}
		
		function fnInsert(){
			if (!$("#myForm").validate({
				//rules: validationRules
			}).form()) {return;}
			var myForm				 = document.getElementById("myForm");
			if(myForm.actionType.value == "U"){
				myForm.action           = "<c:url value='/devicemng/service/update.do'/>";
			}else{
				myForm.action           = "<c:url value='/devicemng/service/insert.do'/>";
			}			
		   	myForm.submit();
		}

		function fnClose(){
		   	opener.myForm.target = "_self";
		   	self.close();
		}
		/* ********************************************************
		 * 최초 실행
		 ******************************************************** */
		$(function() {

			
			//처리 결과 
			if ("${result}" != "" && "${retCod}" == "0") {
				alert("정상적으로 인증되었습니다.");
				opener.location.href="${pageContext.request.contextPath}/env/userInfo/info.do";
				self.close();
			}

		});
	</script>
</head>
<body>



	<!-- #wrapper -->
	<div id="wrapper">
	
		<!-- .header_wrap -->
		<div class="header_wrap">
		<form id="myForm" name="myForm" action="${pageContext.request.contextPath}/security/authenticate.do" method="post">
			<input type="hidden" name="actionType" value="LOGIN_POP">
				

			<div class="servicelist_wrap">
				<p class="servicelist_title1">
					사용자 로그인
				</p>
				<p class="servicelist_title3">로그인 정보를 입력 하세요.</p>


			<div class="member_wrap">
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
				
				<c:if test="${error_txt != ''}">     
					<div class="member_footer padding10">
						<p class="member_title2">로그인 실패</p>
						<p class="member_title3">${error_txt}</p>
					</div>
				</c:if>  				
			</div>	
				
				<div class="ac top_pad_20">
					<input type="button" class="board_bott_bt_pink" value="닫기" onclick="javascript:fnClose();" />
				</div>
			</div>
			<div class="servicelist_footer"></div>
		 
		 
		</form>		
		</div>		
		<!-- .header_wrap -->
	</div>		
	


		
	
	

	
	
</body>
</html>
