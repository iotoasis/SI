<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="true" %>
<html>
<head>
	<title>그룹권한관리</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	
	
		/* ********************************************************
		 * 조회 처리
		 **********************************************************/	
		function fnSearch(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/admin/accountGroup/list.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 등록 처리
		 **********************************************************/
		function fnInsert(){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "I";
			myForm.action           = "<c:url value='/admin/accountGroup/info.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 상세 처리
		 **********************************************************/
		function fnDetail(id, groupCode){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "V";
			myForm.id.value  = id;
			myForm.groupCode.value  = groupCode;
			myForm.action           = "<c:url value='/admin/accountGroup/info.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 삭제 처리
		 **********************************************************/
		function fnCheckDelete(){
			if(checkOneMoreCheckbox(document.myForm, "checkList")){
				if (confirm("삭제하시겠습니까?")) {
					var myForm				 = document.getElementById("myForm");
					myForm.action           = "<c:url value='/admin/accountGroup/delete.do'/>";
				   	myForm.submit();
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
			$("#04").addClass("gnb_active");
			//서브메뉴 활성화
			$("#04_01").addClass("snb_active");
			$("#04_02").removeClass("snb_active");
			$("#04_03").removeClass("snb_active");
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
				< <a href="${pageContext.request.contextPath}/admin/accountGroup/list.do">권한그룹 관리</a>
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
		<form id="myForm" name="myForm" action="<c:url value='/admin/accountGroup/list.do'/>" method="post">
			<input type="hidden" name="actionType" value="">
			<input type="hidden" name="id" value="">
			<input type="hidden" name="groupCode" value="">
	
	
	
			<div class="search_form_wrap">
				<div class="search_form_div">
					<span>검색조건</span>
					<select id="searchKey" name="searchKey" style="width:auto" class="select_box">
						<option value="GROUP_CODE" <c:if test="${'GROUP_CODE' == accountGroupVO.searchKey}">selected="selected"</c:if> >그룹코드</option>
						<option value="GROUP_NAME" <c:if test="${'GROUP_NAME' == accountGroupVO.searchKey}">selected="selected"</c:if> >그룹명</option>
					</select>
					
					<input type="text" name="searchVal" value="${accountGroupVO.searchVal}">
					<input type="button" class="search_bt_gray" value="조회" onclick="javascript:fnSearch();" />
				</div>
			</div>


			<div class="contents_title">
				권한그룹 목록
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
				<div class="table_type11">
					<div class="table_header">
						<div class="th col1"><div><input type="checkbox" name="allChk" onclick="javascript:checkAll(this);"></div></div>
						<div class="th col2"><div>그룹 ID</div></div>
						<div class="th col3"><div>그룹 명</div></div>
						<div class="th col4 last"><div>설명</div></div>
					</div>
					<ul class="table_body">
						<c:choose>						
							<c:when test="${!empty resultList}">
								<c:forEach items="${resultList}" var="resultInfo" varStatus="status">
									<li>
										<div class='col1'><input type="checkbox" name="checkList" value="${resultInfo.id}"></div>
										<div class='col2'>
											<c:if test="${sessionScope.requestAuth.authorizationDBRead == '1'}">
												<a href="javascript:fnDetail('${resultInfo.id}', '${resultInfo.groupCode}');"><c:out value="${resultInfo.groupCode}" /></a>
											</c:if>
											<c:if test="${sessionScope.requestAuth.authorizationDBRead == '0'}">
												<c:out value="${resultInfo.groupCode}" />
											</c:if>
										</div>
										<div class='col3'><c:out value="${resultInfo.groupName}" /></div>
										<div class='col4'>
											<c:if test="${fn:length(resultInfo.description) < 20}">
											    <c:out value="${resultInfo.description}" />            
											</c:if>
											<c:if test="${fn:length(resultInfo.description) > 20}">
											    <c:out value="${fn:substring(resultInfo.description,0,17)}" />...                    
											</c:if>
										</div>
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
