<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>공지사항</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	
	
		/* ********************************************************
		 * 조회 처리
		 **********************************************************/	
		function fnSearch(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/admin/agentNotice/list.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 상세 처리
		 **********************************************************/
		function fnDetail(){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "U";
			myForm.action           = "<c:url value='/admin/agentNotice/info.do'/>";
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
			$("#04").addClass("gnb_active");
			//서브메뉴 활성화
			$("#04_01").removeClass("snb_active");
			$("#04_02").removeClass("snb_active");
			$("#04_03").addClass("snb_active");
			$("#04_04").removeClass("snb_active");
			
			//처리 결과 
			if("${result}" != "" && "${retCod}" == "0"){
				alert("${message}");
				fnSearch();
			}else if("${retCod}" == "1"){
				alert("${message}");
				fnSearch();
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
				< <a href="${pageContext.request.contextPath}/admin/accountGroup/list.do">운영 관리</a>
				< <a href="${pageContext.request.contextPath}/admin/agentNotice/list.do">공지사항</a>
				< 공지사항 정보
			</span>
		</div>
	</div>


	<!-- .container_wrap -->
	<div class="container_wrap">
		<!-- .snb -->
		<%@ include file="/WEB-INF/views/herit/common/left/menu04.jsp" %>
		<!-- /.snb -->	
		

			

	
	

		<!-- #container -->
		<div id="container">
		<form id="myForm" name="myForm" action="<c:url value='/admin/agentNotice/info.do'/>" method="post">
			<input type="hidden" name="actionType" value="${agentNoticeVO.actionType}">
			<input type="hidden" name="id" value="${agentNoticeVO.id}">
	


			<div class="contents_title">
				공지사항 정보
			</div>
			<div class="table_type3">
				<div class="tr first">
					<div class="th"><span>에이전트 타입</span></div>
					<div class="td">
						<div class="td_content">
							<c:if test="${agentNoticeVO.agentType == '01'}">홈에이전트</c:if>
							<c:if test="${agentNoticeVO.agentType == '02'}">가스밸브</c:if>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>종류</span></div>
					<div class="td">
						<div class="td_content">
							<c:if test="${agentNoticeVO.noticeType == '1'}">긴급</c:if>
							<c:if test="${agentNoticeVO.noticeType == '0'}">일반</c:if>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>공지 시작시간</span></div>
					<div class="td">
						<div class="td_content">
							${agentNoticeVO.startTime}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>공지 종료시간</span></div>
					<div class="td">
						<div class="td_content">
							${agentNoticeVO.expireTime}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>작업 시작시간</span></div>
					<div class="td">
						<div class="td_content">
							${agentNoticeVO.workStartTime}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>작업 종료시간</span></div>
					<div class="td">
						<div class="td_content">
							${agentNoticeVO.workEndTime}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>제목</span></div>
					<div class="td">
						<div class="td_content">
							${agentNoticeVO.title}
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>링크</span></div>
					<div class="td">
						<div class="td_content">
							<a href="http://${agentNoticeVO.link}" target="_new">http://${agentNoticeVO.link}</a>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>내용</span></div>
					<div class="td">
						<div class="td_content">
							<textarea id="content" name="content" style="width:568px;" cols="80" rows="18" readonly>${agentNoticeVO.content}</textarea>
						</div>
					</div>
				</div>
			</div>
	
			<div class="write_button_wrap">
				<div class="board_bott_bt_gray right_mar_270"><a href="javascript:fnSearch()">목록</a></div>
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
