<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/tld/herit.tld" prefix="herit" %>
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
			myForm.currPage.value  	= "1";
			myForm.action           = "<c:url value='/admin/agentNotice/list.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 등록 처리
		 **********************************************************/
		function fnInsert(){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "I";
			myForm.action           = "<c:url value='/admin/agentNotice/info.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 상세 처리
		 **********************************************************/
		function fnDetail(id){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "V";
			myForm.id.value  = id;
			myForm.action           = "<c:url value='/admin/agentNotice/info.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 삭제 처리
		 **********************************************************/
		function fnCheckDelete(){
			if(checkOneMoreCheckbox(document.myForm, "checkList")){
				if (confirm("삭제하시겠습니까?")) {
					var myForm				 = document.getElementById("myForm");
					myForm.action           = "<c:url value='/admin/agentNotice/delete.do'/>";
				   	myForm.submit();
				}
			}			
		}
		
		function test(){
			alert("test");
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
		<form id="myForm" name="myForm" action="<c:url value='/admin/agentNotice/list.do'/>" method="post">
			<input type="hidden" name="currPage" value="${currPage}">
			<input type="hidden" name="actionType" value="">
			<input type="hidden" name="id" value="">
	

			<div class="search_form_wrap">
				<div class="search_form_div">
					<span>에이전트 타입</span>
					<select id="agentType" name="agentType" style="width:auto" class="select_box">
						<option value="" <c:if test="${'' == agentNoticeVO.agentType}">selected="selected"</c:if> >== 전체 ==</option>
						<option value="01" <c:if test="${'01' == agentNoticeVO.agentType}">selected="selected"</c:if> >홈에이전트</option>
						<option value="02" <c:if test="${'02' == agentNoticeVO.agentType}">selected="selected"</c:if> >가스밸브</option>
					</select>
					
					<span>종류</span>
					<select id="noticeType" name="noticeType" style="width:auto" class="select_box">
						<option value="" <c:if test="${'' == agentNoticeVO.noticeType}">selected="selected"</c:if> >== 전체 ==</option>
						<option value="1" <c:if test="${'1' == agentNoticeVO.noticeType}">selected="selected"</c:if> >긴급</option>
						<option value="0" <c:if test="${'0' == agentNoticeVO.noticeType}">selected="selected"</c:if> >일반</option>
					</select>
					
					<span>검색조건</span>
					<select id="searchKey" name="searchKey" style="width:auto" class="select_box">
						<option value="TITLE" <c:if test="${'TITLE' == agentNoticeVO.searchKey}">selected="selected"</c:if> >제목</option>
					</select>
					
					<input type="text" name="searchVal" value="${agentNoticeVO.searchVal}">
					<input type="button" class="search_bt_gray" value="조회" onclick="javascript:fnSearch();" />
				</div>
			</div>
			
				

	
			<div class="contents_title">
				공지사항
				<div class="title_button_list">
					<c:if test="${sessionScope.requestAuth.authorizationDBCreate == '1'}">
						<div class="board_top_bt"><a href="javascript:fnInsert();">등록</a></div> 
					</c:if>
					<c:if test="${sessionScope.requestAuth.authorizationDBDelete == '1'}">
						<div class="board_top_bt"><a href="javascript:fnCheckDelete();">선택삭제</a></div>
					</c:if>
				</div>
			</div>	


	
	
			<div class="table_scroll">
				<div class="table_type14">
					<div class="table_header">
						<div class="th col1"><div><input type="checkbox" name="allChk" onclick="javascript:checkAll(this);"></div></div>
						<div class="th col2"><div>에이전트 타입</div></div>
						<div class="th col3"><div>종류</div></div>
						<div class="th col4"><div>제목</div></div>
						<div class="th col5"><div>공지 시작시간</div></div>
						<div class="th col6"><div>공지 종료시간</div></div>
						<div class="th col7"><div>작업 시작시간</div></div>
						<div class="th col8"><div>작업 종료시간</div></div>
						<div class="th col9 last"><div>등록일</div></div>
					</div>
					<ul class="table_body">
						<c:choose>						
							<c:when test="${!empty resultPagingUtil.currList}">
								<c:forEach items="${resultPagingUtil.currList}" var="resultInfo" varStatus="status">
									<li>
										<div class='col1'><input type="checkbox" name="checkList" value="${resultInfo.id}"></div>
										<div class='col2'>
											<c:if test="${resultInfo.agentType == '01'}">홈에이전트</c:if>
											<c:if test="${resultInfo.agentType == '02'}">가스밸브</c:if>
										</div>
										<div class='col3'>
											<c:if test="${resultInfo.noticeType == '1'}">긴급</c:if>
											<c:if test="${resultInfo.noticeType == '0'}">일반</c:if>
										</div>
										<div class='col4'>
											<c:if test="${fn:length(resultInfo.title) < 20}">
											    <a href="javascript:fnDetail('${resultInfo.id}');"><c:out value="${resultInfo.title}" /></a>     
											</c:if>
											<c:if test="${fn:length(resultInfo.title) > 20}">
											    <a href="javascript:fnDetail('${resultInfo.id}');"><c:out value="${fn:substring(resultInfo.title,0,17)}" />...</a>                
											</c:if>	
										</div>
										<div class='col5'><c:out value="${resultInfo.startTime}" /></div>
										<div class='col6'><c:out value="${resultInfo.expireTime}" /></div>
										<div class='col7'><c:out value="${resultInfo.workStartTime}" /></div>
										<div class='col8'><c:out value="${resultInfo.workEndTime}" /></div>
										<div class='col9'><c:out value="${resultInfo.updateTime}" /></div>
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
            		url="${pageContext.request.contextPath}/admin/agentNotice/list.do" method="movePage1" beforeMethod="" />    		
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
