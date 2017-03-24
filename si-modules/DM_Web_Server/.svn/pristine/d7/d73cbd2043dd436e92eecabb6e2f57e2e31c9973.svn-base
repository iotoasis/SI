<%@page pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javaScript" language="javascript">
	/* ********************************************************
	 * 등록 처리
	 **********************************************************/
	function fnUserInfo(){
		openWindow("${pageContext.request.contextPath}/env/userInfo/login.do", "userInfo_popup", 830, 540);			
		var myForm				 = document.getElementById("myForm");
		myForm.id.value  		= "";
		myForm.actionType.value  = "LOGIN";
		myForm.target 			= "userInfo_popup";
		myForm.action           = "<c:url value='/env/userInfo/login.do'/>";
	   	myForm.submit();
	}
</script>

		<div class="header_wrap">
			<!-- #header -->
			<div id="header">
				<!-- .wrap_div -->
				<div class="wrap_div">
					<h1>
						<a href="${pageContext.request.contextPath}">
							<img src="${pageContext.request.contextPath}/images/hitdm/common/dm_logo.png" alt="" />
						</a>
					</h1>
					<div class="header_top">
						<c:if test="${account.loginSuccessYN != 'Y'}">
							<a href="${pageContext.request.contextPath}/security/authenticate.do" class="header_join_button">로그인</a>
						</c:if>
						<c:if test="${account.loginSuccessYN == 'Y'}">
							<span>${account.loginId}님 반갑습니다.</span><span style="display:none;">${account.groupId}</span>
			                <!--<a href="javascript:fnUserInfo();" id="login_anchor" class="header_join_button">사용자정보</a>-->
			                <a href="${pageContext.request.contextPath}/security/logout.do" class="header_join_button">로그아웃</a>
						</c:if>
					</div>
					<div id="gnb">
						<ul>
							<li class='gnb_first gnb_device'><a href='${pageContext.request.contextPath}/device/list.do'>디바이스 관리</a>
								<ul class='gnb_1 gnb_sub'>
									<li class='first'><a href='${pageContext.request.contextPath}/device/list.do'>목록 조회</a></li>
									<li><a href='${pageContext.request.contextPath}/device/detail.do'>상세 정보</a></li>
									<li><a href='${pageContext.request.contextPath}/device/register.do'>디바이스 등록</a></li>
									<li><a href='${pageContext.request.contextPath}/device/oneM2MRegister.do'>oneM2M 디바이스 등록</a></li>
									<li><a href='${pageContext.request.contextPath}/device/registers.do'>디바이스 일괄 등록</a></li>
								</ul></li>
							<li class='gnb_firmware'><a href='${pageContext.request.contextPath}/firmware/list.do'>펌웨어 관리</a>
								<ul class='gnb_4 gnb_sub'>
									<li class='first'><a href='${pageContext.request.contextPath}/firmware/list.do'>펌웨어 목록</a></li>
									<li><a href='${pageContext.request.contextPath}/firmware/detail.do'>펌웨어 상세정보</a></li>
									<li><a href='${pageContext.request.contextPath}/firmware/device/upgrade.do'>펌웨어 업그레이드</a></li>
									<li><a href='${pageContext.request.contextPath}/firmware/device/schedule.do'>스케줄링</a></li>
									<li><a href='${pageContext.request.contextPath}/firmware/device/status.do'>상태조회</a></li>
									<!-- <li><a href='${pageContext.request.contextPath}/firmware/group/upgrade.do'>배치업그레이드</a></li>  -->
								</ul></li>
							<li class='gnb_history'><a href='${pageContext.request.contextPath}/history/error.do'>이력</a>
								<ul class='gnb_4 gnb_sub'>
									<li class='first'><a href='${pageContext.request.contextPath}/history/error.do'>장애이력</a></li>
									<li><a href='${pageContext.request.contextPath}/history/control.do'>제어이력</a></li>
									<li><a href='${pageContext.request.contextPath}/history/firmware.do'>펌웨어업그레이드이력</a></li>
									<li><a href='${pageContext.request.contextPath}/history/status.do'>상태이력</a></li>
								</ul></li>
							<li class='gnb_stats'><a href='${pageContext.request.contextPath}/stats/register.do'>통계</a>
								<ul class='gnb_3 gnb_sub'>
									<li class='first'><a href='${pageContext.request.contextPath}/stats/register.do'>등록 통계</a></li>
									<li><a href='${pageContext.request.contextPath}/stats/usage.do'>사용 통계</a></li>
								</ul></li>
							<c:if test="${account.groupId < 3}">
							<li class='gnb_information'><a
								href='${pageContext.request.contextPath}/information/deviceModel.do'>정보관리</a>
								<ul class='gnb_5 gnb_sub'>
									<li class='first'><a href='${pageContext.request.contextPath}/information/deviceModel.do'>디바이스모델관리</a></li>
									<li><a href='${pageContext.request.contextPath}/information/user.do'>사용자정보 관리</a></li>
									<li><a href='${pageContext.request.contextPath}/env/userInfo/info.do'>내정보 관리</a></li>
								</ul></li>
							</c:if>
						</ul>
						<div class="gnb_sub_bg"></div>
					</div>
				</div>
				<!-- /.wrap_div -->
			</div>
			<!-- /#header -->
		</div>