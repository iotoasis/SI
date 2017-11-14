<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
HashMap<String, String> menuClassMap = new HashMap<String, String>();
HashMap<String, String> pageClassMap = new HashMap<String, String>();
menuClassMap.put((String)request.getAttribute("menu"), "active");
pageClassMap.put(request.getAttribute("menu")+"_"+request.getAttribute("page"), "active");
request.setAttribute("menuClassMap", menuClassMap);
request.setAttribute("pageClassMap", pageClassMap);
 %>
            <ul class="nav" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element"> <span>
                            <%-- <img alt="image" class="img-circle" src="${pageContext.request.contextPath}/v2/img/profile_small.jpg" /> --%>
                             </span>

                            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                            <span class="clear"> <span class="block m-t-xs"> <!-- <strong class="font-bold">David Williams</strong> -->
                             </span> <span class="text-muted text-xs block">Administrator <b class="caret"></b></span> </span> </a>
                            <ul class="dropdown-menu animated fadeInRight m-t-xs">
	                            <li><a href="profile.html">Profile</a></li>
	                            <li class="divider"></li>	                            
                                <li><a href="login.html">Logout</a></li>
                            </ul>
                    </div>
                    <div class="logo-element">
                        KEMCTI
                    </div>
                </li>
                <li class="${menuClassMap.monitor}">
                    <a href="#"><i class="fa fa-desktop"></i> <span class="nav-label">KEMCTI</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="${pageClassMap.monitor_index}"><a href="${pageContext.request.contextPath}/mdevice/detail.do">디바이스</a></li>
                        <li class="${pageClassMap.monitor_device}"><a href="${pageContext.request.contextPath}/mdevice/status.do">통계</a></li>
                        <li class="${pageClassMap.monitor_device}"><a href="${pageContext.request.contextPath}/mdevice/list.do">목록</a></li>
                        <li class="${pageClassMap.monitor_device}"><a href="${pageContext.request.contextPath}/mdevice/setting.do">설정</a></li>
                    </ul>
                </li>
                <%-- <li class="${menuClassMap.monitor}">
                    <a href="#"><i class="fa fa-desktop"></i> <span class="nav-label">모니터링</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="${pageClassMap.monitor_index}"><a href="${pageContext.request.contextPath}/v2/monitor/index.do">통합 모니터링</a></li>
                        <li class="${pageClassMap.monitor_device}"><a href="${pageContext.request.contextPath}/v2/monitor/device.do">디바이스모니터링</a></li>
                        <li class="${pageClassMap.monitor_device}"><a href="${pageContext.request.contextPath}/device/list.do">디바이스목록</a></li>
                    </ul>
                </li>
                <li class="${menuClassMap['firmware']}">
                    <a href="#"><i class="fa fa-download"></i> <span class="nav-label">펌웨어 관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="${pageClassMap.firmware_index}"><a href="${pageContext.request.contextPath}/firmware/list.do">펌웨어 목록</a></li>
                        <li class="${pageClassMap.firmware_detail}"><a href="${pageContext.request.contextPath}/firmware/list.do">펌웨어 상세정보</a></li>
                        <li class="${pageClassMap.firmware_upgrade}"><a href="${pageContext.request.contextPath}/firmware/device/upgrade.do">펌웨어 업그레이드</a></li>
                        <li class="${pageClassMap.firmware_upgrade}"><a href="${pageContext.request.contextPath}/firmware/device/schedule.do">스케줄링</a></li>
                        <li class="${pageClassMap.firmware_upgrade}"><a href="${pageContext.request.contextPath}/firmware/device/status.do">상태조회</a></li>
                    </ul>
                </li>
                <li class="${menuClassMap['history']}">
                    <a href="#"><i class="fa fa-history"></i> <span class="nav-label">이력</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="${pageClassMap.history_error}"><a href="${pageContext.request.contextPath}/history/error.do">장애 이력</a></li>
                        <li class="${pageClassMap.history_control}"><a href="${pageContext.request.contextPath}/history/control.do">제어 이력</a></li>
                        <li class="${pageClassMap.history_firmware}"><a href="${pageContext.request.contextPath}/history/firmware.do">펌웨어 업그레이드 이력</a></li>
                        <li class="${pageClassMap.history_status}"><a href="${pageContext.request.contextPath}/history/status.do">상태 이력</a></li>
                    </ul>
                </li>
                <li class="${menuClassMap['stats']}">
                    <a href="#"><i class="fa fa-bar-chart-o"></i> <span class="nav-label">통계</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="${pageClassMap.stats_register}"><a href="${pageContext.request.contextPath}/stats/register.do">등록 통계</a></li>
                        <li class="${pageClassMap.stats_usage}"><a href="${pageContext.request.contextPath}/stats/usage.do">사용 통계</a></li>
                    </ul>
                </li>
                <li class="${menuClassMap['information']}">
                    <a href="#"><i class="fa fa-info-circle"></i> <span class="nav-label">정보 관리</span><span class="fa arrow"></span></a>
                    <ul class="nav nav-second-level">
                        <li class="${pageClassMap.information_deviceModel}"><a href="${pageContext.request.contextPath}/information/deviceModel.do">디바이스 모델 정보</a></li>
                        <li class="${pageClassMap.information_user}"><a href="${pageContext.request.contextPath}/information/user.do">사용자 정보</a></li>
                        <li class="${pageClassMap.information_device}"><a href="${pageContext.request.contextPath}/env/userinfo/info.do">디바이스</a></li>
                    </ul>
                </li> --%>
            </ul>