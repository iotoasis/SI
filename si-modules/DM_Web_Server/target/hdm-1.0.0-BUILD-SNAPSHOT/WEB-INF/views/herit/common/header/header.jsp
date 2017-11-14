<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javaScript" language="javascript">
	/* ********************************************************
	 * 등록 처리
	 **********************************************************/
	function fnUserInfo(){
		openWindow("", "userInfo_popup", 830, 540);			
		var myForm				 = document.getElementById("myForm");
		myForm.id.value  		= "";
		myForm.actionType.value  = "LOGIN";
		myForm.target 			= "userInfo_popup";
		myForm.action           = "<c:url value='/env/userInfo/login.do'/>";
	   	myForm.submit();
	}
</script>

<!-- .header_wrap -->
<div class="header_wrap">
	<!-- #header -->
	<div id="header">
		<!-- .wrap_div -->
		<div class="wrap_div">
			<h1><a href="${pageContext.request.contextPath}"><img src="${pageContext.request.contextPath}/images/common/logo.gif" alt="" /></a></h1>
			<div class="header_top">
				<c:if test="${account.loginSuccessYN != 'Y'}">
					<a href="${pageContext.request.contextPath}/security/authenticate.do" class="header_join_button">로그인</a>
				</c:if>
				<c:if test="${account.loginSuccessYN == 'Y'}">
					<span>${account.loginId}님 반갑습니다.</span>
	                <a href="javascript:fnUserInfo();" id="login_anchor" class="header_join_button">사용자정보</a>
	                <a href="${pageContext.request.contextPath}/security/logout.do" class="header_join_button">로그아웃</a>
				</c:if>
			</div>
			<div id="gnb">
				<ul>
					<li id="00" class="gnb_sgv">
						<a href='${pageContext.request.contextPath}/sgvmng/serviceStatus/list.do' target='_self'>가스밸브 서비스</a>
					</li>
					<li id="01" class="gnb_network gnb_active">
						<a href='${pageContext.request.contextPath}/homemng/platformStatus/list.do' target='_self'>홈 네트워크 관리</a>
					</li>
					<li id="02" class="gnb_device">
						<a href='${pageContext.request.contextPath}/devicemng/deviceType/list.do' target='_self'>디바이스 관리</a>
					</li>
					<li id="03" class="gnb_api">
						<a href='${pageContext.request.contextPath}/openapi/openapiClient/list.do' target='_self'>Open API 접속 관리</a>
					</li>
					<li id="04" class="gnb_manage">
						<a href='${pageContext.request.contextPath}/admin/accountGroup/list.do' target='_self'>운영 관리</a>
					</li>
				</ul>
			</div>
		</div>
	</div>
	<!-- /#header -->
</div>