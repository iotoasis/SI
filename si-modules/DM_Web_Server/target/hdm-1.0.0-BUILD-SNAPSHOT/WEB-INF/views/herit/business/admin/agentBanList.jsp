<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/tld/herit.tld" prefix="herit" %>
<%@ page session="true" %>
<html>

<head>
	<title>에이전트 정책관리</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	
	
		/* ********************************************************
		 * 조회 처리
		 **********************************************************/	
		function fnSearch(){
			var myForm				 = document.getElementById("myForm");
			myForm.currPage.value  	= "1";
			myForm.action           = "<c:url value='/admin/agentBan/list.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 상세 처리
		 **********************************************************/
		function fnDetail(id){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "U";
			myForm.id.value  = id;
			myForm.action           = "<c:url value='/admin/agentBan/info.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 등록 처리
		 **********************************************************/
		function fnInsert(){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "I";
			myForm.action           = "<c:url value='/admin/agentBan/info.do'/>";
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
			$("#04_03").removeClass("snb_active");
			$("#04_04").addClass("snb_active");
			
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
				< <a href="${pageContext.request.contextPath}/admin/agentBan/list.do">에이전트 정책 관리</a>
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
		<form id="myForm" name="myForm" action="<c:url value='/admin/agentBan/list.do'/>" method="post">
			<input type="hidden" name="currPage" value="${currPage}">
			<input type="hidden" name="actionType" value="">
			<input type="hidden" name="id" value="">
			
			
			
			<div class="contents_title">
				에이전트 정책 목록
				<div class="title_button_list">
					<c:if test="${sessionScope.requestAuth.authorizationDBCreate == '1'}">
						<div class="board_top_bt"><a href="javascript:fnInsert();">등록</a></div> 
					</c:if>
				</div>
			</div>	
			
						
			

			<div class="table_scroll">
				<div class="table_type13">
					<div class="table_header">
						<div class="th col1"><div>No</div></div>
						<div class="th col2"><div>실행금지 버전</div></div>
						<div class="th col3"><div>적용여부</div></div>
						<div class="th col4"><div>작성자</div></div>
						<div class="th col5 last"><div>등록일</div></div>
					</div>
					<ul class="table_body">
						<c:choose>						
							<c:when test="${!empty resultPagingUtil.currList}">
								<c:forEach items="${resultPagingUtil.currList}" var="resultInfo" varStatus="status">
									<li>
										<div class='col1'>
											<c:out value="${(resultPagingUtil.totalSize-((currPage-1)*resultPagingUtil.pageSize)) - status.index}" />
										</div>
										<div class='col2'>
											<c:if test="${sessionScope.requestAuth.authorizationDBRead == '1'}">
												<a href="javascript:fnDetail('${resultInfo.id}');"><c:out value="${resultInfo.stopVersion}" /></a>
											</c:if>
											<c:if test="${sessionScope.requestAuth.authorizationDBRead == '0'}">
												<c:out value="${resultInfo.stopVersion}" />
											</c:if>
										</div>
										<div class='col3'>
											<c:if test="${resultInfo.expired == '1'}">예 </c:if>
											<c:if test="${resultInfo.expired == '0'}">아니오</c:if>
										</div>
										<div class='col4'><c:out value="${resultInfo.mngAccountLoginNm}" /></div>
										<div class='col5'><c:out value="${resultInfo.createTime}" /></div>
									</li>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<li>
									<div class='nodata'>조회된 데이터가 없습니다.</div>
								</li>
							</c:otherwise> 
						</c:choose>
					</ul>
				</div>
			</div>
	
		
	        <%--페이징처리 START--%>
	        <div class="paging_wrap">
            	<herit:table table="${resultPagingUtil}" form="document.myForm" contextPath="${pageContext.request.contextPath}"
            		url="${pageContext.request.contextPath}/admin/agentBan/list.do" method="movePage1" beforeMethod="" />  		
	        </div>	
	        <%--페이징처리 END--%>



	
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
