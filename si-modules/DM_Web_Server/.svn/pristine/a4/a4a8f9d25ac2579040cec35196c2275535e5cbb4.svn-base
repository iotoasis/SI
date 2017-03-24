<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="true"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>HIT DM Admin</title>
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp"%>
<script type="text/javascript">
	$(document).ready(function() {
		initUI();
	})

	var gr_id = "device";
	var sub_num = "2";
	
	var logSearchStartTime = "";
	var logSearchEndTime = "";
	var deviceId = "";

	var fileServerHost = "${fileServerHost}";
	var fileServerPort = ${fileServerPort};
	
	var contextPath = "${pageContext.request.contextPath}";
	var deviceInfo = ${deviceInfoJson};
	var moMap = ${moMapJson};
	var profileList = ${profileListJson};
</script>
<script src="${pageContext.request.contextPath}/js/jquery/jquery.flot.js"></script>
<script src="https://maps.googleapis.com/maps/api/js?v=3.exp"></script>
<script src="${pageContext.request.contextPath}/js/herit/deviceDetail.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/dm.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/map_google.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/traffic_graph.js"></script>

<script src="${pageContext.request.contextPath}/js/herit/history.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>

</head>
<body>
	<!-- #wrapper -->
	<div id="wrapper">

		<!-- .header_wrap -->
		<%@ include file="/WEB-INF/views/common/include/hdm_menu.jsp"%>
		<!-- /.header_wrap -->


		<!-- #container -->

		<div class="sub0102">
			<div id="container">
				<div class="page_title">
					<h2>상세정보</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 디바이스 관리 &gt; 상세정보
					</div>
				</div>

				<div class="content_wrap">

					<div class="content_wrap top_pad_30">
						<!-- /-->
						<div class="content_title_box obstacle_title">
							<p class="content_title">
							기본정보 
							<img src="${pageContext.request.contextPath}/images/hitdm/common/more_up_btn.gif" class="image_btn view_toggle_btn" id="basicViewToggleBtn">
							<img src="${pageContext.request.contextPath}/images/hitdm/common/refresh_btn.gif"  class="image_btn" id="refleshBtn">
						</div>
						
						<div class="device_detail_content_wrap">
							<div class="device_info" id="basicView">
								<table width="100%">
									<tr>
										<td width="35%">
											<div class="search_title">모델정보</div>
										</td>
										<td width="35%">
											<div class="search_title">기본정보</div>
										</td>
										<td width="30%">
											<div class="search_title">위치정보</div>
										</td>
									</tr>
									<tr>
										<td><img
											src="${pageContext.request.contextPath}${deviceModelInfo.iconUrl}"
											width="150" style="margin-bottom:10px;">
												<table>
													<tr>
														<td><span class="dinfo_title">제조사</span></td>
														<td><span class="dinfo_value" id="device_MANUFACTURER">${deviceInfo.manufacturer}</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">제조사 OUI</span></td>
														<td><span class="dinfo_value" id="device_OUI">${deviceInfo.oui}</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">모델번호</span></td>
														<td><span class="dinfo_value" id="device_MODEL_NAME">${deviceInfo.modelName}</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">디바이스ID</span></td>
														<c:set var="length" value="${fn:length(deviceInfo.deviceId)}"/>
														<c:choose>
															<c:when test="${length > 25}">
																<td><span class="dinfo_value" id="device_DEVICE_ID" alt="${deviceInfo.deviceId}">${fn:substring(deviceInfo.deviceId, 0, 25)}...</span></td>
															</c:when>
															<c:otherwise>
																<td><span class="dinfo_value" id="device_DEVICE_ID">${deviceInfo.deviceId}</span></td>
															</c:otherwise>
														</c:choose>
													</tr>
													<tr>
														<td><span class="dinfo_title">시리얼번호</span></td>
														<td><span class="dinfo_value" id="device_SN">${deviceInfo.sn}</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">생성일자</span></td>
														<td><span class="dinfo_value" id="device_CREATE_TIME">${fn:substring(deviceInfo.createTime, 0, 16)}</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">등록일자</span></td>
														<td><span class="dinfo_value" id="device_REG_TIME">${fn:substring(deviceInfo.regTime, 0, 16)}</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">갱신일자</span></td>
														<td><span class="dinfo_value" id="device_REG_UPDATE_TIME">${fn:substring(deviceInfo.regUpdateTime, 0, 16)}</span></td>
													</tr>
												</table></td>
										<td>
											<table>
												<c:choose>
													<c:when test="${!empty basicProfileList}">
														<c:forEach items="${basicProfileList}" var="profile"
															varStatus="status">

															<c:if test="${!fn:contains('/2/-/0,/2/-/1,/2/-/13,/2/-/2,/2/-/3,/2/-/29', profile['resourceUri'])}">
															<c:if test="${!fn:contains(profile['operation'], 'E')}">
															<tr>
																<td><span class="dinfo_title">${profile['displayName']}</span></td>
																<td><span class="dinfo_value deviceProfile"
																	id="${fn:replace(profile['resourceUri'], '/', '_')}"></span></td>
															</tr>
															</c:if>
															</c:if>


														</c:forEach>
													</c:when>
													<c:otherwise>


													</c:otherwise>
												</c:choose>
											</table>
										</td>
										<td>
											<div class="dinfo_value" id="device_ADDRESS">-</div>
											<div
												style="width: 340px; height: 250px; background-color: #eeeeee">
												<div id="map-canvas" style="display:block;width:100%;height:100%;"></div>
											</div>
											<div>위도:<span
											id="_5_-_0" class="dinfo_value">-</span>, 경도:<span
											id="_5_-_1" class="dinfo_value">-</span>, 측정 시간:<span
											id="_5_-_3" class="dinfo_value">-</span></div>
										</td>
									</tr>
								</table>
							</div>
						</div>


						<div class="content_title_box obstacle_title">
							<p class="content_title">
							기타정보
							<img src="${pageContext.request.contextPath}/images/hitdm/common/more_up_btn.gif" class="image_btn view_toggle_btn" id="etcViewToggleBtn">
							</p> 
						</div>

						<div class="device_detail_content_wrap">
							<div class="device_info" id="etcView">
								<table width="100%">
									<tr>
										<td width="50%">
											<div class="search_title">서비스 정보</div>
										</td>
										<td width="50%">
											<div class="search_title">소프트웨어 정보</div>
										</td>
									</tr>
									<tr>
										<td  width="50%" rowspan="3">
											<div class="device_service_list">
												<table>
												<thead class="table_header">
												<tr>
													<th class="th col5">구분</th>
													<th class="th col2">상태</th>
													<th class="th col4">제어</th>
												</tr>
												</thead>
												<tbody class="table_body">
												<c:choose>
													<c:when test="${!empty serviceProfileList}">
														<c:forEach items="${serviceProfileList}" var="profile"
															varStatus="status">
														<c:set var="cmdClass" value="" />
														<c:if test="${fn:contains(profile['operation'], 'E')}">
															<c:set var="cmdClass" value="cmdProfile" />
														</c:if>
														<tr>
															<td class='col5'>
																${profile['displayName']}
																 (<span id="${fn:replace(profile['resourceUri'], '/', '_')}_updateTime" style="font-size:6px;font-color:gray;"></span>)
															</td>
															<td class='col2'>
																
															<c:choose>
																<c:when test="${!empty profile['optionDataList']}">
																<select id="${fn:replace(profile['resourceUri'], '/', '_')}" class="writeProfile ${cmdClass}">
																	<c:forEach items="${profile['optionDataList']}" var="optionData"
																		varStatus="status">
																	<option value="${optionData.data}">${optionData.displayData}</option>
																	</c:forEach>
																</select>
																</c:when>
																<c:when test="${empty profile['optionDataList'] && fn:contains(profile['operation'], 'W')}">
																<input type="text" id="${fn:replace(profile['resourceUri'], '/', '_')}" class="writeProfile ${cmdClass}" size="15">
																</c:when>
															<c:otherwise>
																<span id="${fn:replace(profile['resourceUri'], '/', '_')}" class="deviceProfile"></span>
															</c:otherwise>
															</c:choose>
															</td>
															<td class='col4'>
																<c:if test="${fn:contains(profile['operation'], 'E')}">
																<input name="button" type="button" id="controlBtn_${fn:replace(profile['resourceUri'], '/', '_')}"
																	class="search_bt_gray" value="실행"
																	style="width: 50px; height: 18px;" />
																</c:if>
																<c:if test="${fn:contains(profile['operation'], 'W')}">
																<input name="button" type="button" id="controlBtn_${fn:replace(profile['resourceUri'], '/', '_')}"
																	class="search_bt_gray" value="쓰기"
																	style="width: 50px; height: 18px;" />
																</c:if>
															</td>
														</tr>


														</c:forEach>
													</c:when>
													<c:otherwise>
	
														<tr>
															<td class='col5'> - </td>
															<td class='col2'></td>
															<td class='col4'></td>
														</tr>	
														<tr>
															<td class='col5'> - </td>
															<td class='col2'></td>
															<td class='col4'></td>
														</tr>	
														<tr>
															<td class='col5'> - </td>
															<td class='col2'></td>
															<td class='col4'></td>
														</tr>

													</c:otherwise>
												</c:choose>
												</tbody>
												</table>
											</div>
										</td>
										<td>
											<div class="software_list">
												<table>
												<thead class="table_header">
												<tr>
													<th class="th col5">패키지명</th>
													<th class="th col4">버전</th>
													<th class="th col2">비고</th>
												</tr>
												</thead>
												<tbody class="table_body" id="firmwareTable">
													<tr>
														<td class='col5'> - </td>
														<td class='col4'> - </td>
														<td class='col2'> - </td>
													</tr>
												</tbody>
												</table>
											</div>
										</td>
									</tr>
									<tr style="height:19px;">
										<td width="50%">
											<div class="search_title">오류 정보</div>
										</td>
									</tr> 
									<tr>
										<td width="50%">
											<div class="device_error_list">
												<table>
												<thead class="table_header">
													<tr>
														<th class="th col1">&nbsp;</th>
														<th class="th col2">TIME</th>
														<th class="th col4">LEVEL</th>
														<th class="th col5">MESSAGE</th>
													</tr>
												</thead>
												<tbody class="table_body" id="errorCodeTable">
													<tr>
														<td class='col1'> - </td>
														<td class='col2'> - </td>
														<td class='col4'> - </td>
														<td class='col5'> - </td>
													</tr>
												</tbody>
												</table>
											</div>
										</td>
									</tr>
								</table>
							</div>
						</div>

						<div class="content_title_box obstacle_title">
							<p class="content_title">
							네트워크정보							
							<img src="${pageContext.request.contextPath}/images/hitdm/common/more_up_btn.gif" class="image_btn view_toggle_btn" id="networkViewToggleBtn">
							</p>
						</div>
						<div class="device_detail_content_wrap">
							<div class="device_info" id="networkView">
								<table width="100%">
									<tr>
										<td width="40%">
											<div class="search_title">정보</div>
										</td>
										<td width="60%">
											<div class="search_title">TX/RX</div>
										</td>
									</tr>
									<tr>
										<td>
											<table>
												<c:choose>
													<c:when test="${!empty networkProfileList}">
														<c:forEach items="${networkProfileList}" var="profile"
															varStatus="status">

															<tr>
																<td><span class="dinfo_title">${profile['displayName']}</span></td>
																<td><span class="dinfo_value deviceProfile"
																	id="${fn:replace(profile['resourceUri'], '/', '_')}"></span></td>
															</tr>


														</c:forEach>
													</c:when>
													<c:otherwise>

														<tr>
															<td><span class="dinfo_title">-</span></td>
															<td><span class="dinfo_value">-</span></td>
														</tr>

													</c:otherwise>
												</c:choose>
											</table>
										</td>
										<td>
											<div id="traffic_chart" style="width: 550px; height: 200px;">																		
												<canvas class="flot-base" style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 550px; height: 250px;">
												</canvas>
											</div>
											<!-- <div>TX: <span id="traffic_tx"></span>KB, RX: <span id="traffic_tx"></span>KB</div> -->
										</td>
									</tr>
								</table>
							</div>
						</div>

						<div class="content_title_box obstacle_title">
							<p class="content_title">
							진단							
							<img src="${pageContext.request.contextPath}/images/hitdm/common/more_up_btn.gif" class="image_btn view_toggle_btn" id="diagnosisViewToggleBtn">
							</p>
						</div>
						<div class="device_detail_content_wrap">
							<div class="device_info" id="diagnosisView">
								<table width="100%">
									<tr>
										<td width="40%">
											<div class="search_title">로그수집</div>
										</td>
										<td width="60%">
											<div class="search_title">초기화</div>
										</td>
									</tr>
									<tr>
										<td width="40%">
											<p>수집상태: <span id="logStatus">수집안함</span></p>
											<input name="Submit" type="submit"
													class="search_bt_gray" id="btnDebugStart" value="수집 시작" />
											<input name="Submit" type="submit"
													class="search_bt_gray" id="btnDebugStop" value="수집 종료" />
										</td>
										<td width="60%">
											<input name="Submit" type="submit"
													class="search_bt_gray" id="btnReboot" value="리부팅" />
											<input name="Submit" type="submit"
													class="search_bt_gray" id="btnFactoryReset" value="공장초기화" />
										</td>
									</tr>
								</table>
							</div>
						</div>


						<div class="content_title_box obstacle_title">
							<p class="content_title">
							로그							
							<img src="${pageContext.request.contextPath}/images/hitdm/common/more_up_btn.gif" class="image_btn view_toggle_btn" id="logViewToggleBtn">
							</p>
						</div>
							<!--<div class="search_title">검색설정</div> -->
						<div class="device_detail_content_wrap">
							<div class="device_info" id="logView">
								<div class="search_form_div">
									시작시간: <input type="text" id="dpLogStart" class="date_picker"></input> 
											<select id="selLogStartHour">
												<option value="00">00</option><option value="01">01</option><option value="02">02</option><option value="03">03</option>
												<option value="04">04</option><option value="05">05</option><option value="06">06</option><option value="07">07</option>
												<option value="08">08</option><option value="09">09</option><option value="10">10</option><option value="11">11</option>
												<option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option>
												<option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option>
												<option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option>
											</select>
											<select id="selLogStartMinute">
												<option value="00">00</option><option value="10">10</option><option value="20">20</option>
												<option value="30">30</option><option value="40">40</option><option value="50">50</option>
											</select>
									종료시간: <input type="text" id="dpLogEnd" class="date_picker"></input> 
											<select id="selLogEndHour">
												<option value="00">00</option><option value="01">01</option><option value="02">02</option><option value="03">03</option>
												<option value="04">04</option><option value="05">05</option><option value="06">06</option><option value="07">07</option>
												<option value="08">08</option><option value="09">09</option><option value="10">10</option><option value="11">11</option>
												<option value="12">12</option><option value="13">13</option><option value="14">14</option><option value="15">15</option>
												<option value="16">16</option><option value="17">17</option><option value="18">18</option><option value="19">19</option>
												<option value="20">20</option><option value="21">21</option><option value="22">22</option><option value="23">23</option>
											</select> : 
											<select id="selLogEndMinute">
												<option value="00">00</option><option value="10">10</option><option value="20">20</option>
												<option value="30">30</option><option value="40">40</option><option value="50">50</option>
											</select>
									<input name="Submit" type="submit" class="search_bt_green" id="btnSearchLog" value="검색" />
									<!-- <input name="Submit" type="submit" class="search_bt_gray" id="btnDownloadLog" value="다운로드" /> -->
								</div>
										
								<table border="0" cellspacing="0" cellpadding="0"
									class="table_type_nomal_scroll_height" id="history_table">
									<thead>
										<tr>
											<!-- <th id="hd_select" class="history_manual_renderer"><input type="checkbox" id="cbDeviceLogAll" /></th>-->
											<th id="hd_sn">SN</th>
											<!-- <th id="hd_package">패키지명</th> -->
											<th id="hd_path" class="history_manual_renderer">경로</th>
											<th id="hd_filesize" class="history_manual_renderer">크기</th>
											<th id="hd_startTime">로그시작</th>
											<th id="hd_endTime">로그종료</th>
											<!-- <th id="hd_createTime">저장시간</th> -->
											<!-- <th id="hd_operation" class="history_manual_renderer">보기</th> -->
										</tr>
									</thead>
									<tbody>
										<tr>
											<td colspan="8">로그이력을 검색하세요.</td>
										</tr>
									</tbody>
								</table>
								<div class="paging_wrap">
		
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- /#container -->
			<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
		</div>
		<!-- /#wrapper -->
</body>
</html>