<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="/tld/herit.tld" prefix="herit" %>
<%@ page session="true"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>HIT DM Admin</title>
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp" %>
<script type="text/javascript">
	var gr_id="firmware";
	var sub_num="4";
	var contextPath = "${pageContext.request.contextPath}";
	
	$(document).ready(function() {
		
		initUI();
	})


</script>
<script src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/firmwareSchedule.js"></script>

</head>
<body>
	<!-- #wrapper -->
	<div id="wrapper">
	
		<!-- .header_wrap -->
		<%@ include file="/WEB-INF/views/common/include/hdm_menu.jsp" %>
		<!-- /.header_wrap -->


		<!-- #container -->
		<div class="sub0101">
			<div id="container">
				<div class="page_title">
					<h2>펌웨어 업그레이드 스케줄링</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 펌웨어 관리 &gt; 스케줄링
					</div>
				</div>

				<div class="content_wrap top_pad_30">

					<!-- Search-->
					<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<!--<div class="search_title">검색설정</div> -->
									<div class="search_form_wrap">
										<div class="search_form_div">
											<table>
												<tr>
													<td height="30" width="150">디바이스 모델</td>
													<td>
														<select name="deviceModelId" id="search_deviceModelId" style="height: 21px;">
															<option value="choose">선택</option>
															<c:forEach items="${deviceModelList}" var="deviceModel">
																<option value="${deviceModel.id}"														
																<c:if test="${fn:contains(param.deviceModelId, deviceModel.id)}">selected</c:if>
																>${deviceModel.manufacturer}-${deviceModel.modelName}
																</option>
															</c:forEach>			
														</select>
													</td>
												</tr>
												<tr>
													<td height="30" width="150">펌웨어 패키지</td>
													<td>
														<select name="firmwareId" id="search_package" class="schedule_select">
															<option value="">선택</option>
															<c:forEach items="${firmwareList}" var="firmwareInfo">
																<option value="${firmwareInfo.id}|${firmwareInfo['package']}"														
																<c:if test="${fn:contains(param.firmwareId, firmwareInfo.id)}">selected</c:if>
																>${firmwareInfo['package']}
																</option>
															</c:forEach>			
														</select>
													</td>
												</tr>
												<tr>
													<td height="30" width="150">펌웨어 버전</td>
													<td>
														<select name="firmwareVersion" id="search_firmwareVersion" class="schedule_select">
															<option value="">선택</option>
															<c:forEach items="${versionList}" var="firmwareVer">
																<option value="${firmwareVer.version}">${firmwareVer.version}</option>
															</c:forEach>			
														</select>
													</td>
												</tr>
												<tr>
													<td height="30" width="150">업그레이드 일시</td>
													<td>
														<input type="text" id="dpStart" class="date_picker" />
														<select class="schedule_select" id="dpTime">
															<option value="00:00">00:00</option>
															<option value="01:00">01:00</option>
															<option value="02:00">02:00</option>
															<option value="03:00">03:00</option>
															<option value="04:00">04:00</option>
															<option value="05:00">05:00</option>
															<option value="06:00">06:00</option>
															<option value="07:00">07:00</option>
															<option value="08:00">08:00</option>
															<option value="09:00">09:00</option>
															<option value="10:00">10:00</option>
															<option value="11:00">11:00</option>
															<option value="12:00">12:00</option>
															<option value="13:00">13:00</option>
															<option value="14:00">14:00</option>
															<option value="15:00">15:00</option>
															<option value="16:00">16:00</option>
															<option value="17:00">17:00</option>
															<option value="18:00">18:00</option>
															<option value="19:00">19:00</option>
															<option value="20:00">20:00</option>
															<option value="21:00">21:00</option>
															<option value="22:00">22:00</option>
															<option value="23:00">23:00</option>
														</select>
													</td>
												</tr>
												<tr>
													<td height="30" width="150">&nbsp;</td>
													<td height="30" colspan="2">
														<input type="submit" id="btnUpgradeSchedule" class="search_bt_green" value="업그레이드 실행" />
													</td>
												</tr>
											</table>
											
										</div>
									</div>
							</div>
						</div>

					</div>
					<!-- Search END-->
				</div>

				
				<div class="content_title_box obstacle_title">
						<p class="content_title">스케줄링 목록 조회</p>
				</div>
					<div class="device_list_wrap_100py2">

						<table border="0" cellspacing="0" cellpadding="0"
							class="table_type_nomal_scroll_height">
							<tr>
								<th>디바이스모델명</th>
								<th>버전</th>
								<th>패키지명</th>
								<th>업그레이드일시</th>
								<th>등록갱신일시</th>
							</tr>
							<c:choose>
								<c:when test="${!empty firmwareUpdateList}">
									<c:forEach items="${firmwareUpdateList}" var="firmwareUpdate" varStatus="status">
										<tr>
											<td>${firmwareUpdate['modelName']}</td>
											<td>${firmwareUpdate['version']}</td>
											<td>${firmwareUpdate['package']}</td>
											<td>${firmwareUpdate['scheduleTime']}</td>
											<td>${firmwareUpdate['updateTime']}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>

									<tr>
										<td colspan="5">조회된 데이터가 없습니다.</td>
									</tr>

								</c:otherwise>
							</c:choose>
						</table>
					</div>
				</div>



		<!-- /#container -->
		<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp" %>
	</div>
	<!-- /#wrapper -->

</body>
</html>