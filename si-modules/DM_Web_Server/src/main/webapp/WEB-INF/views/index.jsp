<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/js/herit/common/common.js"></script>
</head>
<body>

<!-- redirect page -->
<c:redirect url="/home/main.do" />
 
	<table width="1280px" cellpadding="0" cellspacing="0" border="1">
		<tr>
			<td colspan="2" height="100px">
			
				<div id="headerWrapper">
					<!-- Header 시작 -->
					<%@ include file="/WEB-INF/views/herit/common/header/header.jsp" %>
					<!-- Header 끝 -->
				</div>
				
			</td>
		</tr>
		<tr>
			<td width="150px" height="700px">
				<div id="headerWrapper">
					<!-- left 시작 -->
					<%@ include file="/WEB-INF/views/herit/common/left/menu02.jsp" %>
					<!-- Header 끝 -->
				</div>
			</td>
			<td>
					이곳은 인덱스 페이지 입니다.
			</td>
		</tr>
		<tr>
			<td colspan="2" height="100px">
			
				<div id="footerWrapper">
					<!-- Footer 시작 -->
					<%@ include file="/WEB-INF/views/herit/common/footer/footer.jsp" %>
					<!-- Footer 끝 -->
				</div>	
				
			</td>
		</tr>
	</table>	
	
</body>
</html>