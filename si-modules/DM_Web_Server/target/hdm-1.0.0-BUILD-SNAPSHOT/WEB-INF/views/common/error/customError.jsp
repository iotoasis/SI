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

				<div class="error_box">
					<div class="error_title">
						페이지 실행중 <span><c:out value="${requestScope['javax.servlet.error.status_code']}"></c:out> 에러</span>가 발생했습니다.
					</div>
					<div class="error_con">
						<span>죄송합니다!</span><br />
						시스템 운용 중 뜻하지 않은 오류가 발생되었습니다.<br /> 
						위 오류 사항을 알려주시면 문제 해결에 많은 도움이 되오니 시스템 담당자에게 연락 주시면 감사하겠습니다.<br /><br />
						시스템 이용에 불편을 드려 대단히 죄송합니다.
					</div>
						<!-- custom 에러 : ${exception} -->
				</div>
			</div>
			<!-- /#container -->
			<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
		</div>
		<!-- /#wrapper -->
</body>
</html>








