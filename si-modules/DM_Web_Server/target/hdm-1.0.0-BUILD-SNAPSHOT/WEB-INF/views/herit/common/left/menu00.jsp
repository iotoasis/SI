<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="snb">
	<div class="snb_header">가스밸브 서비스</div>
	<ul>
		<li id="00_01"><a href='${pageContext.request.contextPath}/sgvmng/serviceStatus/list.do'>서비스 현황</a></li>
		<li id="00_02"><a href='${pageContext.request.contextPath}/sgvmng/subsInfo/list.do'>청약정보 목록</a></li>
		<li id="00_03"><a href='${pageContext.request.contextPath}/sgvmng/appUserList/list.do'>사용자정보 목록</a></li>			
	</ul>
</div>
