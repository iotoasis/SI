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
	 * 재조회 처리
	 **********************************************************/
	function reload(){
		var myForm				 = document.getElementById("myForm");
		myForm.actionType.value  = "V";
		myForm.action           = "<c:url value='/env/userInfo/info.do'/>";
	   	myForm.submit();
	}		

	/* ********************************************************
	 * 상세 처리
	 **********************************************************/
	function fnDetail(){
		var myForm				 = document.getElementById("myForm");
		myForm.actionType.value  = "U";
		myForm.action           = "<c:url value='/env/userInfo/info.do'/>";
	   	myForm.submit();
	}
	
	/* ********************************************************
	 * 등록 처리
	 **********************************************************/
	function fnIpLimitInsert(){
		openWindow("", "ipLimit_popup", 830, 410);			
		var myForm				 = document.getElementById("myForm2");
		myForm.id.value  		= "";
		myForm.actionType.value  = "IP_LIMIT_I";
		myForm.target 			= "ipLimit_popup";
		myForm.action           = "<c:url value='/env/userInfo/info.do'/>";
	   	myForm.submit();
	}
	/* ********************************************************
	 * 삭제 처리
	 **********************************************************/
	function fnIpLimitCheckDelete(){
		var myForm				 = document.getElementById("myForm2");			
		if("${fn:length(ipLimitResultList)}" == "0"){
			alert("조회된 데이터가 없습니다.");
			return false;
		}else{
			var idx = getRadioValue(myForm.num);				
			if(idx != ""){
				if (confirm("선택된 항목을 삭제하시겠습니까?")) {
					myForm.id.value = idx;
					myForm.action           = "<c:url value='/env/userInfo/ipLimit/delete.do'/>";
				   	myForm.submit();
				}						
			}else{
				alert("선택된 항목이 없습니다.");
				return false;
			}
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
		
		//처리 결과 
		if("${result}" != "" && "${retCod}" == "0"){
			alert("${message}");
			reload();
		}else if("${retCod}" == "1"){
			alert("${message}");
			reload();
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
									<input type="hidden" name="loginId" value="${accountVO.loginId}">
									<div class="search_form_wrap" style="padding:20px;">
										<table>
										<tr>
											<td height="30" width="150">그룹명</td>
											<td>${accountVO.mngAccountGroupNm}</td>
										</tr>
										<tr>
											<td height="30" width="150">로그인 ID</td>
											<td>${accountVO.loginId}</td>
										</tr>
										<tr>
											<td height="30" width="150">이름</td>
											<td>${accountVO.name}</td>
										</tr>
										<tr>
											<td height="30" width="150">이메일</td>
											<td>${accountVO.email}</td>
										</tr>
										<tr>
											<td height="30" width="150">&nbsp;</td>
											<td height="30" colspan="2">
											<input name="Submit" type="submit" class="search_bt_green" value="수정" onclick="javascript:fnDetail();"/>
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