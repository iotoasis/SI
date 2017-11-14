<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="snb">
	<div class="snb_header">디바이스 관리</div>
	<ul>
		<li id="02_01"><a href="${pageContext.request.contextPath}/devicemng/deviceType/list.do">디바이스 타입 목록</a></li>
		<li id="02_02"><a href="${pageContext.request.contextPath}/devicemng/deviceModel/list.do">디바이스 모델 목록</a></li>
		<li id="02_03"><a href="${pageContext.request.contextPath}/devicemng/service/list.do">서비스 목록</a></li>
		<li id="02_04"><a href="${pageContext.request.contextPath}/devicemng/app/list.do">앱 목록</a></li>
		<li id="02_05"><a href="${pageContext.request.contextPath}/devicemng/activity/list.do">액티비티 목록</a></li>
	</ul>
</div>



