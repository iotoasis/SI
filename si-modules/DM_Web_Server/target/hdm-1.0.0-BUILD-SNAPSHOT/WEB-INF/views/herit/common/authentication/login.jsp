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

		/* ********************************************************
		 * 로그인 처리
		 **********************************************************/	
		function fnLogin(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "${pageContext.request.contextPath}/security/authenticate.do";
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
	<!-- #wrapper -->
	<div id="wrapper">

		<!-- .header_wrap -->
		<%@ include file="/WEB-INF/views/common/include/hdm_menu.jsp"%>
		<!-- /.header_wrap -->


		<!-- #container -->
		<div class="sub0103">
			<div id="container">
				<div class="page_title">
					<h2>로그인</h2>
					<div class="position_text">
						
					</div>
				</div>

				<div class="content_wrap top_pad_30">

					<div class="content_title_box" style="height: 24px">
						<p class="content_title">로그인 정보</p>
					</div>

					<!-- Search-->
					<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<form id="myForm" name="myForm" action="${pageContext.request.contextPath}/security/authenticate.do" method="post">
									<div class="search_form_wrap" style="padding:20px;">
										<table>
										<tr>
											<td height="30" width="150">ID</td>
											<td><input type="text" size="30" name="loginId" id="loginId" /> </td>
										</tr>
										<tr>
											<td height="30" width="150">비밀번호</td>
											<td><input type="password" size="30" name="loginPassword" id="loginPassword" /></td>
										</tr>
										<tr>
											<td height="30" width="150">&nbsp;</td>
											<td height="30" colspan="2">
											<input name="Submit" type="submit" class="search_bt_green" value="로그인" onclick="javascript:fnLogin();"/>
											</td>
										</tr>
										</table>
									</div>
								</form>
							</div>
						</div>
						<c:if test="${error_txt != ''}">     
							<div class="member_footer padding10">
								<p class="member_title2">로그인 실패</p>
								<p class="member_title3">${error_txt}</p>
							</div>
						</c:if>         
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