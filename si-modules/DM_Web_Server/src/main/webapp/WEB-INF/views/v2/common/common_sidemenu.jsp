<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<ul class="nav" id="side-menu">
    <li class="nav-header">
        <div class="dropdown profile-element">
        	<img src="${pageContext.request.contextPath}/images/hitdm/common/logo_oasis_2.png" width="20px" style="margin-top: -2px;"/><strong class="font-bold" style="color:#DFE4ED;">OASIS SI Administrator</strong>
        </div>
        <div class="logo-element" style="padding: 13px 0;">
            <img src="${pageContext.request.contextPath}/images/hitdm/common/logo_oasis_2.png" width="34px"/>
        </div>
    </li>
    <li id="side-menu_oneM2M">
        <a href="#"><i class="fa fa-tachometer"></i> <span class="nav-label">oneM2M 서버 관리</span><span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li><a href="<c:url value="/onem2m/manager.do" />">통합 리소스 관리</a></li>
            <li><a href="<c:url value="/onem2m/resource.do" />">리소스 트리 조회</a></li>
            <li><a href="<c:url value="/onem2m/platform/list.do" />">플랫폼 연동 관리</a></li>
            <li><a href="<c:url value="/onem2m/qos/info.do" />">QoS 관리</a></li>
        </ul>
    </li>
    <li id="side-menu_dm">
        <a href="#"><i class="fa fa-gears"></i> <span class="nav-label">oneM2M 디바이스 관리</span><span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li><a href="<c:url value="/dm/onem2m.do" />">oneM2M 리소스 기반 디바이스 관리</a></li>
            <li><a href="<c:url value="/dm/tr069.do" />">oneM2M 리소스 기반 TR-069 디바이스 관리</a></li>
            <li><a href="<c:url value="/dm/mobius.do" />">모비우스 기반 디바이스 관리</a></li>
        </ul>
    </li>
    <li id="side-menu_device">
        <a href="#"><i class="fa fa-desktop"></i> <span class="nav-label">디바이스 관리</span><span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li><a href="<c:url value="/v2/monitor/index.do" />">통합 모니터링</a></li>
            <li><a href="<c:url value="/device/list.do" />">목록 조회</a></li>
            <li><a href="<c:url value="/device/register.do" />">디바이스 등록</a></li>
            <li><a href="<c:url value="/device/oneM2MRegister.do" />">oneM2M 디바이스 등록</a></li>
            <li><a href="<c:url value="/device/registers.do" />">디바이스 일괄 등록</a></li>
        </ul>
    </li>
	
    <li id="side-menu_firmware">
        <a href="#"><i class="fa fa-download"></i> <span class="nav-label">펌웨어 관리</span><span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li><a href="<c:url value="/firmware/list.do" />">펌웨어 목록 조회</a></li>
			<!--
        	<li><a href="<c:url value="/firmware/upload.do" />">펌웨어 업로드</a></li>
			-->
            <li><a href="<c:url value="/firmware/device/upgrade.do" />">펌웨어 업그레이드</a></li>
            <li><a href="<c:url value="/firmware/device/schedule.do" />">스케줄링</a></li>
            <li><a href="<c:url value="/firmware/device/status.do" />">상태조회</a></li>
        </ul>
    </li>
    <li id="side-menu_history">
        <a href="#"><i class="fa fa-history"></i> <span class="nav-label">이력</span><span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li><a href="<c:url value="/history/error.do" />">장애 이력</a></li>
            <li><a href="<c:url value="/history/control.do" />">제어 이력</a></li>
            <li><a href="<c:url value="/history/firmware.do" />">펌웨어 업그레이드 이력</a></li>
            <li><a href="<c:url value="/history/status.do" />">상태 이력</a></li>
        </ul>
    </li>
    <li id="side-menu_statistics">
        <a href="#"><i class="fa fa-bar-chart-o"></i> <span class="nav-label">통계</span><span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li><a href="<c:url value="/v2/stats/register.do" />">등록 통계</a></li>
            <li><a href="<c:url value="/v2/stats/usage.do" />">사용 통계</a></li>
        </ul>
    </li>
    <li id="side-menu_operator">
        <a href="#"><i class="fa fa-info-circle"></i> <span class="nav-label">운영 관리</span><span class="fa arrow"></span></a>
        <ul class="nav nav-second-level">
            <li><a href="<c:url value="/admin/accountGroup/list.do" />">권한 그룹 관리</a></li>
            <li><a href="<c:url value="/information/user.do" />">사용자 정보 관리</a></li>
            <li><a href="<c:url value="/env/userInfo/info.do" />">내 정보 관리</a></li>
            <li><a href="<c:url value="/information/deviceModel.do" />">디바이스 모델 관리</a></li>
        </ul>
    </li>
    <li id="side-menu_fiware">
        <a href="<c:url value="/dm/fiware.do" />"><i class="fa fa-globe"></i> <span class="nav-label">Fi-Ware 연동 관리</span><span class="fa arrow"></span></a>
    </li>
</ul>
