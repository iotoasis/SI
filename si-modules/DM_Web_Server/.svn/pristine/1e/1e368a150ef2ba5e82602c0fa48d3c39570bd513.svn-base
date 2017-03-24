<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ page session="true" %>
<html>
<head>
	<title>사용자관리</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	
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


<div id="wrapper">
	<!-- Header 시작 -->
	<%@ include file="/WEB-INF/views/herit/common/header/header.jsp" %>
	<!-- Header 끝 -->
	
	
	<div class="position_text">
		<div class="wrap_div">
			<a href="${pageContext.request.contextPath}"><img src="${pageContext.request.contextPath}/images/common/home_icon.gif" alt="" /></a>
			<span>
				< <a href="${pageContext.request.contextPath}/env/userInfo/info.do">환경설정</a> 
				< <a href="${pageContext.request.contextPath}/env/userInfo/info.do">사용자정보</a> 
			</span>
		</div>
	</div>


	<!-- .container_wrap -->
	<div class="container_wrap">
		<!-- .snb -->
		<%@ include file="/WEB-INF/views/herit/common/left/menu05.jsp" %>
		<!-- /.snb -->	
		

			

	
	

		<!-- #container -->
		<div id="container">
		<form id="myForm" name="myForm" action="<c:url value='/env/userInfo/info.do'/>" method="post">
			<input type="hidden" name="actionType" value="${accountVO.actionType}">
			<input type="hidden" name="loginId" value="${accountVO.loginId}">


			<div class="contents_title">
				사용자 정보
			</div>
			<div class="table_type3">
				<div class="tr first">
					<div class="th"><span>그룹명</span></div>
					<div class="td">
						<div class="td_content">
							${accountVO.mngAccountGroupNm}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>사용자ID</span></div>
					<div class="td">
						<div class="td_content">
							${accountVO.loginId}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>사용자명</span></div>
					<div class="td">
						<div class="td_content">
							${accountVO.name}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>비밀번호</span></div>
					<div class="td">
						<div class="td_content">
							********
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>소속부서</span></div>
					<div class="td">
						<div class="td_content">
							${accountVO.departmentNm}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>Email</span></div>
					<div class="td">
						<div class="td_content">
							${accountVO.email}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>전화번호</span></div>
					<div class="td">
						<div class="td_content">
							${accountVO.phone}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>휴대폰번호</span></div>
					<div class="td">
						<div class="td_content">
							${accountVO.mobile}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>사용여부</span></div>
					<div class="td">
						<div class="td_content">
							<c:if test="${accountVO.disabled == '1'}">사용</c:if>
							<c:if test="${accountVO.disabled == '0'}">미사용</c:if>
						</div>
					</div>
				</div>
			</div>
			

			<div class="contents_title top_mar_30">접속 허용 IP</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_type_left bott_mar_30">
				<c:choose>						
					<c:when test="${!empty ipLimitResultList}">
						<c:forEach items="${ipLimitResultList}" var="resultInfo" varStatus="status">
							<tr>
								<th width="20%">IP ${status.count}</th>
								<td>${resultInfo.ip}</td>
							</tr>
						</c:forEach>
						<c:if test="${fn:length(ipLimitResultList) < 5}">
							<c:forEach begin="${fn:length(ipLimitResultList) + 1}" end="5" step="1" var="resultInfo" varStatus="status">
								<tr>
									<th width="20%">IP ${status.index}</th>
									<td></td>
								</tr>
							</c:forEach>	
						</c:if>
					</c:when>
					<c:otherwise>
						<c:forEach begin="1" end="5" step="1" var="resultInfo" varStatus="status">
							<tr>
								<th width="20%">IP ${status.count}</th>
								<td>&nbsp;</td>
							</tr>
						</c:forEach>	
					</c:otherwise> 
				</c:choose>	
			</table>
			
			<div class="write_button_wrap">
				<c:if test="${sessionScope.requestAuth.authorizationDBUpdate == '1'}">
					<div class="board_bott_bt_pink"><a href="javascript:fnDetail();">수정</a></div>
				</c:if>
			</div>		

	
	
		</form>
		</div>
		<!-- /#container -->			

	
	</div>
	<!-- .container_wrap -->



	<!-- Footer 시작 --> 
	<%@ include file="/WEB-INF/views/herit/common/footer/footer.jsp" %>
	<!-- Footer 끝 --> 	
</div>		
<!-- /#wrapper -->	


</body>
</html>
