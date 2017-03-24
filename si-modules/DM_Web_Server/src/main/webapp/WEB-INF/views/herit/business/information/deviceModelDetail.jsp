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
	var gr_id = "information";
	var sub_num = "1";
	var contextPath = "${pageContext.request.contextPath}";
	var param = ${paramJson};
	var deviceModelId = ${param.id};
	var oui = null;
	var modelName = null;

	$(document).ready(function() {
		
		//initUI();
		
	})

</script>
<!-- Properties -->
<%@ include file="/js/herit/conf/prop.jsp" %>
<!-- JQuery Dialog Module -->
<script src="${pageContext.request.contextPath}/js/herit/util/ui/dialog.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/common/common.js"></script>

<script src="${pageContext.request.contextPath}/js/herit/information/deviceModelDetail.js"></script>
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
					<h2>디바이스모델 상세정보</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 정보관리 &gt; <a href="${pageContext.request.contextPath}/information/deviceModel.do">디바이스 모델 목록</a> &gt; 상세정보
					</div>
				</div>

				<div class="content_wrap">
				
				
					<br/>
					<div class="board_top_bt">
						<a id="modifyDeviceModel" href="javascript:void(0);">수정</a>
					</div>

					<div class="content_wrap top_pad_30">					
						<!-- /-->
						<div class="search_form_wrap">
							<div class="device_info">
								<table width="100%">
									<tr>
										<td width="20%">
											<div class="search_title">기본정보</div>
										</td>
										<td width="30%">
										</td>
										<td width="50%">
											<div class="search_title">부가정보</div>
										</td>
									</tr>
									<tr>
										<td><img src="" width="150" id="iconUrl" /></td>
										<td>
												<input type="hidden" id="model_id" class="i_text" />
												<input type="hidden" id="model_deviceType" class="i_text" />
												<input type="hidden" id="model_applyYn" class="i_text" />
												<table>
													<tr>
														<td width="120"><span class="dinfo_title">제조사</span></td>
														<td><span class="dinfo_value"
															id="model_manufacturer">...</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">제조사 OUI</span></td>
														<td><span class="dinfo_value" id="model_oui">...</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">모델번호</span></td>
														<td><span class="dinfo_value" id="model_modelName">...</span></td>
													</tr>
												</table>
										</td>
										<td>
											<table>																	
												<tr>
													<td width="120"><span class="dinfo_title">설명</span></td>
													<td><span class="dinfo_value" id="model_description">...</span></td>
												</tr>															
												<tr>
													<td><span class="dinfo_title">생성일시</span></td>
													<td><span class="dinfo_value" id="model_createTime">...</span></td>
												</tr>															
												<tr>
													<td><span class="dinfo_title">갱신일시</span></td>
													<td><span class="dinfo_value" id="model_updateTime">...</span></td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</div>

					</div>
					
				
					<select id="profileVerSelector"></select>
					
					<div class="board_top_bt">
						<a id="addResource" href="javascript:void(0);">리소스 추가</a>
					</div>
					<div class="board_top_bt">
						<a id="addVersion" href="javascript:void(0);">버전 추가</a>
					</div>
						
					<div class="content_title_box obstacle_title">
						<p class="content_title">관리객체 목록</p>
					</div>
					<div class="device_list_wrap_100py2">

						<table border="0" cellspacing="0" cellpadding="0"
							class="table_type_nomal_scroll_height" id="mo_table">
							<thead>
								<tr>
									<th id="hd_profileVer">버전</th>
									<th id="hd_displayName">표시이름</th>
									<th id="hd_resourceUri">URI</th>
									<th id="hd_dataType">자료형</th>
									<th id="hd_unit">단위</th>
									<th id="hd_defaultValue">기본값</th>
									<th id="hd_operation">오퍼레이션</th>
									<th id="hd_isError">오류/진단 필드여부</th>
									<th id="hd_isHistorical">이력저장여부</th>
									<th id="hd_optionData">옵션</th>
									<th id="hd_optionData">에러</th>
									<th id="hd_isHistorical">노티옵션</th>
									<th id="hd_isHistorical">&nbsp;</th>
								</tr>
							</thead>
							<tbody id="moProfileList">
								<tr>
									<td colspan="13">데이터 로딩중...</td>
								</tr>
							</tbody>
						</table>
					</div>
					
				</div>
			</div>
			<!-- /#container -->
			<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
		</div>
		<!-- /#wrapper -->
	</div>	
		
	<input type="hidden" id="id" class="i_text" />
	<input type="hidden" id="moProfileId" class="i_text" />
	<input type="hidden" id="actionType" class="i_text" />
		
		
	<!-- 디바이스모델 수정 -->
	<div id="modifyDeviceModelDialog" class="ui-widget">
		<table border="0" cellpadding="0" cellspacing="0" style="margin: 15px 0 0 0;">
			<tr>
				<td bgcolor="#e5e5e5">
					<table width="500" border="0" cellpadding="1" cellspacing="1">
						<tr height="26">
							<td width="35%">제조사 OUI</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="oui" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">제조사 이름</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="manufacturer" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">모델번호</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="modelName" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">디바이스타입</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="deviceType" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">아이콘URL</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="file" id="iconUrl" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">프로파일버전</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="profileVer" value="1.0" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">설명</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="description" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">적용여부</td>
							<td bgcolor="#ffffff">&nbsp; 
								<select id="applyYn" class="schedule_select">
									<option value="">==선택==</option>
									<option value="Y">예</option>
									<option value="N">아니오</option>
								</select>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>		
	
			
	<!-- 리소스 추가 -->	
	<div id="addResourceDialog" class="ui-widget">
		<table border="0" cellpadding="0" cellspacing="0" style="margin: 15px 0 0 0;">
			<tr>
				<td bgcolor="#e5e5e5">
					<table width="500" border="0" cellpadding="1" cellspacing="1">
						<tr height="26">
							<td width="35%">표시이름</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="displayName" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">URI</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="resourceUri" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">자료형</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="dataType" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">단위</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="unit" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">기본값</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="defaultValue" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">오퍼레이션</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="operation" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">노티옵션</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="notiType" class="i_text" value="1" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">오류필드여부</td>
							<td bgcolor="#ffffff">&nbsp; 
								<select id="isError" class="schedule_select">
									<option value="">==선택==</option>
									<option value="Y">예</option>
									<option value="N">아니오</option>
								</select>
							</td>
						</tr>
						<tr height="26">
							<td width="35%">진단필드여부</td>
							<td bgcolor="#ffffff">&nbsp; 
								<select id="isDiagnostic" class="schedule_select">
									<option value="">==선택==</option>
									<option value="Y">예</option>
									<option value="N">아니오</option>
								</select>
							</td>
						</tr>
						<tr height="26">
							<td width="35%">이력저장여부</td>
							<td bgcolor="#ffffff">&nbsp; 
								<select id="isHistorical" class="schedule_select">
									<option value="">==선택==</option>
									<option value="Y">예</option>
									<option value="N">아니오</option>
								</select>
							</td>
						</tr>
						<tr height="26">
							<td width="35%">필수여부</td>
							<td bgcolor="#ffffff">&nbsp; 
								<select id="isMandatory" class="schedule_select">
									<option value="">==선택==</option>
									<option value="Y">예</option>
									<option value="N">아니오</option>
								</select>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>		
	
	<!-- 버전 추가 -->	
	<div id="addVersionDialog" class="ui-widget">
		<table border="0" cellpadding="0" cellspacing="0" style="margin: 15px 0 0 0;">
			<tr>
				<td bgcolor="#e5e5e5">
					<table width="500" border="0" cellpadding="1" cellspacing="1">
						<tr height="26">
							<td width="35%">추가 버전 입력</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="newVersionSelector" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">현재 버전 선택</td>
							<td bgcolor="#ffffff">&nbsp; 
								<select id="preVersionSelector" class="schedule_select"></select>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>		
		
		

	<!-- 옵션 데이터 목록보기 -->	
	<div id="optionDataListDialog" class="ui-widget">
		<div class="device_list_wrap_100py2">
			<div id="title_button_list">
				<div class="board_top_bt">
					<a id="delOptionData" href="javascript:void(0);">선택 삭제</a>
				</div>
				<div class="board_top_bt">
					<a id="addOptionData" href="javascript:void(0);">옵션데이터 등록</a>
				</div>
			</div>

			<table border="0" cellspacing="0" cellpadding="0" class="table_type_nomal_scroll_height">
				<thead>
					<tr>
						<th>선택</th>
						<th>표시순서</th>
						<th>표시데이터</th>
						<th>데이터</th>
					</tr>
				</thead>
				<tbody id="moOptionDataList">
					<tr>
						<td colspan="4">데이터 로딩중...</td>
					</tr>
				</tbody>
			</table>
			
		</div>
	</div>
	<div id="addOptionDataDialog" class="ui-widget">
		<table border="0" cellpadding="0" cellspacing="0" style="margin: 15px 0 0 0;">
			<tr>
				<td bgcolor="#e5e5e5">
					<table width="500" border="0" cellpadding="1" cellspacing="1">
						<tr height="26">
							<td width="35%">표시순서</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="order" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">표시데이터</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="displayData" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">데이터</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="data" class="i_text" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	
		
	
	<!-- 에러코드 목록보기 -->	
	<div id="errorCodeListDialog" class="ui-widget">
		<div class="device_list_wrap_100py2">
			<div id="title_button_list">
				<div class="board_top_bt">
					<a id="delErrorCode" href="javascript:void(0);">선택 삭제</a>
				</div>
				<div class="board_top_bt">
					<a id="addErrorCode" href="javascript:void(0);">에러코드 등록</a>
				</div>
			</div>

			<table border="0" cellspacing="0" cellpadding="0" class="table_type_nomal_scroll_height">
				<thead>
					<tr>
						<th>선택</th>
						<th>오류코드</th>
						<th>장애등급</th>
						<th>오류이름</th>
						<th>설명</th>
					</tr>
				</thead>
				<tbody id="moErrorCodeList">
					<tr>
						<td colspan="5">데이터 로딩중...</td>
					</tr>
				</tbody>
			</table>
			
		</div>
	</div>	
	<div id="addErrorCodeDialog" class="ui-widget">
		<table border="0" cellpadding="0" cellspacing="0" style="margin: 15px 0 0 0;">
			<tr>
				<td bgcolor="#e5e5e5">
					<table width="500" border="0" cellpadding="1" cellspacing="1">
						<tr height="26">
							<td width="35%">오류코드</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="errorCode" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">장애등급</td>
							<td bgcolor="#ffffff">&nbsp; 
								<select id="errorGrade" class="schedule_select">
									<option value="0">NO ERROR</option>
									<option value="1">MINOR</option>
									<option value="2">MAJOR</option>
									<option value="3">CRITICAL</option>
									<option value="4">FATAL</option>
								</select>
							</td>
						</tr>
						<tr height="26">
							<td width="35%">오류이름</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="errorName" class="i_text" />
							</td>
						</tr>
						<tr height="26">
							<td width="35%">설명</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="errorDescription" class="i_text" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>

	<!-- 노티조건 목록보기 -->	
	<div id="notiConditionListDialog" class="ui-widget">
		<div class="device_list_wrap_100py2">
			<div id="title_button_list">
				<div class="board_top_bt">
					<a id="delNotiCondition" href="javascript:void(0);">선택 삭제</a>
				</div>
				<div class="board_top_bt">
					<a id="addNotiCondition" href="javascript:void(0);">노티조건 등록</a>
				</div>
			</div>

			<table border="0" cellspacing="0" cellpadding="0" class="table_type_nomal_scroll_height">
				<thead>
					<tr>
						<th>선택</th>
						<th>노티타입</th>
						<th>노티데이터</th>
					</tr>
				</thead>
				<tbody id="moNotiConditionList">
					<tr>
						<td colspan="3">데이터 로딩중...</td>
					</tr>
				</tbody>
			</table>
			
		</div>
	</div>	
	<div id="addNotiConditionDialog" class="ui-widget">
		<table border="0" cellpadding="0" cellspacing="0" style="margin: 15px 0 0 0;">
			<tr>
				<td bgcolor="#e5e5e5">
					<table width="500" border="0" cellpadding="1" cellspacing="1">
						<tr height="26">
							<td width="35%">노티타입</td>
							<td bgcolor="#ffffff">&nbsp; 
								<select id="conditionType" class="schedule_select">
									<option value="P">Period (sec)</option>
									<option value="G">Greater Than</option>
									<option value="L">Less Than</option>
									<option value="S">STEP</option>
								</select>
							</td>
						</tr>
						<tr height="26">
							<td width="35%">노티데이터</td>
							<td bgcolor="#ffffff">&nbsp; 
								<input type="text" id="condition" class="i_text" />
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
		

</body>
</html>