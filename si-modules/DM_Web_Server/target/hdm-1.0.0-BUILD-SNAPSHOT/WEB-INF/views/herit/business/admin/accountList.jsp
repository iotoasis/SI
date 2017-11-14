<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/tld/herit.tld" prefix="herit" %>
<%@ page session="true" %>
<html>

<head>
	<title>사용자관리</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	
	
		/* ********************************************************
		 * 조회 처리
		 **********************************************************/	
		function fnSearch(){
			var myForm				 = document.getElementById("myForm");
			myForm.currPage.value  	= "1";
			myForm.action           = "<c:url value='/admin/account/list.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 등록 처리
		 **********************************************************/
		function fnInsert(){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "I";
			myForm.action           = "<c:url value='/admin/account/info.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 상세 처리
		 **********************************************************/
		function fnDetail(loginId){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "V";
			myForm.loginId.value  = loginId;
			myForm.action           = "<c:url value='/admin/account/info.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 삭제 처리
		 **********************************************************/
		function fnCheckDelete(){
			if(checkOneMoreCheckbox(document.myForm, "checkList")){
				if (confirm("삭제하시겠습니까?")) {
					var myForm				 = document.getElementById("myForm");
					myForm.action           = "<c:url value='/admin/account/delete.do'/>";
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
			$("#04_02").addClass("snb_active");
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
				< <a href="${pageContext.request.contextPath}/admin/account/list.do">사용자 관리</a>
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
		<form id="myForm" name="myForm" action="<c:url value='/admin/account/list.do'/>" method="post">
			<input type="hidden" name="currPage" value="${currPage}">
			<input type="hidden" name="actionType" value="">
			<input type="hidden" name="loginId" value="">
	
	
			<div class="search_form_wrap">
				<div class="search_form_div">
					<span>그룹명</span>
					<select id="mngAccountGroupId" name="mngAccountGroupId" style="width:auto" class="select_box">
						<option value="" <c:if test="${'' == accountVO.mngAccountGroupId}">selected="selected"</c:if> >== 전체 ==</option>	
						<c:forEach var="result" items="${mngAccountGroupIdList}" varStatus="status">						
							<option value='<c:out value="${result.code}"/>' <c:if test="${result.code == accountVO.mngAccountGroupId}">selected="selected"</c:if>><c:out value="${result.name}"/></option>
						</c:forEach>
					</select>
							
					<span>검색조건</span>
					<select id="searchKey" name="searchKey" style="width:auto" class="select_box">
						<option value="LOGIN_ID" <c:if test="${'LOGIN_ID' == accountVO.searchKey}">selected="selected"</c:if> >사용자ID</option>
						<option value="NAME" <c:if test="${'NAME' == accountVO.searchKey}">selected="selected"</c:if> >사용자명</option>
					</select>
					
					<input type="text" name="searchVal" value="${accountVO.searchVal}">
					<input type="button" class="search_bt_gray" value="조회" onclick="javascript:fnSearch();" />
				</div>
			</div>

	
	
			<div class="contents_title">
				사용자 목록
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
				<div class="table_type12">
					<div class="table_header">
						<div class="th col1"><div><input type="checkbox" name="allChk" onclick="javascript:checkAll(this);"></div></div>
						<div class="th col2"><div>그룹 명</div></div>
						<div class="th col3"><div>사용자 명</div></div>
						<div class="th col4"><div>사용자 ID</div></div>
						<div class="th col5"><div>이메일</div></div>
						<div class="th col6"><div>사용여부</div></div>
						<div class="th col7 last"><div>등록일</div></div>
					</div>
					<ul class="table_body">
						<c:choose>						
							<c:when test="${!empty resultPagingUtil.currList}">
								<c:forEach items="${resultPagingUtil.currList}" var="resultInfo" varStatus="status">
									<li>
										<div class='col1'><input type="checkbox" name="checkList" value="${resultInfo.loginId}"></div>
										<div class='col2'><c:out value="${resultInfo.mngAccountGroupNm}" /></div>
										<div class='col3'>
											<c:if test="${sessionScope.requestAuth.authorizationDBRead == '1'}">
												<a href="javascript:fnDetail('${resultInfo.loginId}');"><c:out value="${resultInfo.name}" /></a>
											</c:if>
											<c:if test="${sessionScope.requestAuth.authorizationDBRead == '0'}">
												<c:out value="${resultInfo.name}" />
											</c:if>		
										</div>
										<div class='col4'>
											<c:if test="${sessionScope.requestAuth.authorizationDBRead == '1'}">
												<a href="javascript:fnDetail('${resultInfo.loginId}');"><c:out value="${resultInfo.loginId}" /></a>
											</c:if>
											<c:if test="${sessionScope.requestAuth.authorizationDBRead == '0'}">
												<c:out value="${resultInfo.loginId}" />
											</c:if>									
										</div>
										<div class='col5'><c:out value="${resultInfo.email}" /></div>
										<div class='col6'>
											<c:if test="${resultInfo.disabled == '1'}">사용</c:if>
											<c:if test="${resultInfo.disabled == '0'}">미사용</c:if>
										</div>
										<div class='col7'><c:out value="${resultInfo.updateTime}" /></div>
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
            		url="${pageContext.request.contextPath}/admin/account/list.do" method="movePage1" beforeMethod="" />       		
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
