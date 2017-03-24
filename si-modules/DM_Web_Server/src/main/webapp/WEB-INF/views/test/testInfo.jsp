<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Test Page</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	
	
		/* ********************************************************
		 * 조회 처리
		 **********************************************************/	
		function fnSearch(){
		   	document.myForm.submit();
		}
		/* ********************************************************
		 * 등록 처리
		 **********************************************************/
		function fnInsert(){
			
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/test/insertTest.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 수정 처리
		 **********************************************************/
		function fnUpdate(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/test/updateTest.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 삭제 처리
		 **********************************************************/
		function fnDelete(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/test/deleteTest.do'/>";
		   	myForm.submit();
		}		
		
		/* ********************************************************
		 * 최초 실행
		 ******************************************************** */
		$(function() {
			
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

<h1>
	Hello Test Page!   
</h1>
<P>  The time on the server is ${serverTime}. </P>


<form id="myForm" name="myForm" action="<c:url value='/test/testInfo.do'/>" method="post">
	
	<h2>Test List</h2>
	<div id = "testList">
		<table cellpadding="0" cellspacing="0" border="1">
			<tr>
				<th width="10%" scope="col" nowrap>아이디</th>
				<th width="20%" scope="col" nowrap>이름</th>
				<th width="20%" scope="col" nowrap>이메일</th>
			</tr>
				<c:choose>						
					<c:when test="${!empty resultList}">
						<c:forEach items="${resultList}" var="resultInfo" varStatus="status">
							<tr>
								<td><c:out value="${resultInfo.loginid}" /></td>
								<td><c:out value="${resultInfo.name}" /></td>
								<td><c:out value="${resultInfo.email}" /></td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="3">조회된 데이터가 없습니다.</td>
						</tr>						
					</c:otherwise> 
				</c:choose>
		</table>
	</div>
	
	
	<div id = "testInfo">
		<h2>Test info</h2>
		<dl>
			<dt>ID</dt>
			<dt><input type="text" id="loginid" name="loginid" value="${resultVO.loginid}"/></dt>
		</dl>
		<dl>
			<dt>이름</dt>
			<dt><input type="text" id="name" name="name" value="${resultVO.name}"/></dt>
		</dl>
		<dl>
			<dt>이메일</dt>
			<dt><input type="text" id="email" name="email" value="${resultVO.email}"/></dt>
		</dl>
	</div>
	
	<input type="button" class="btn" value="조회" onclick="javascript:fnSearch();return false;" /> 
	<input type="button" class="btn" value="등록" onclick="javascript:fnInsert();return false;" /> 
	<input type="button" class="btn" value="수정" onclick="javascript:fnUpdate();return false;" /> 
	<input type="button" class="btn" value="삭제" onclick="javascript:fnDelete();return false;" />
	
</form>

</body>
</html>
