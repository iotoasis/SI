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
	var gr_id = "firmware";
	var sub_num = "2";
	var contextPath = "${pageContext.request.contextPath}";
	var deviceModelInfoJson = ${deviceModelInfoJson};
	var firmwareInfoJson = ${firmwareInfoJson};
	var firmwareVersionListJson = ${firmwareVersionListJson};
	
	$(document).ready(function() {
		initUI();
	})	
</script>

<script src="${pageContext.request.contextPath}/js/herit/firmwareDetail.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/dm.js"></script>

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
							alt="" /> 펌웨어 관리 &gt; 상세정보
					</div>
				</div>

				<div class="content_wrap">

					<div class="content_wrap top_pad_30">
						<!-- /-->
						<div class="search_form_wrap">
							<!-- <div class="search_bottom">
								SN:<input type="text"> <input name="Submit"
									type="submit" class="search_bt_green" value="검색" />
							</div> -->
							<div class="device_info">
								<table width="100%">
									<tr>
										<td width="40%">
											<div class="search_title">모델정보</div>
										</td>
										<td width="60%">
											<div class="search_title">기본정보</div>
										</td>
										<td>
											<input type="submit" class="search_bt_green_detail" id="btn_updateFirmBasic" value="편집" />
										</td>
									</tr>
									<tr>
										<td><img src="${pageContext.request.contextPath}/images/hitdm/temp/settopbox.jpg" width="150" />
												<table>
													<tr>
														<td><span class="dinfo_title">제조사</span></td>
														<td><span class="dinfo_value"
															id="device_MANUFACTURER">${deviceModelInfo.manufacturer}</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">제조사 OUI</span></td>
														<td><span class="dinfo_value" id="device_OUI">${deviceModelInfo.oui}</span></td>
													</tr>
													<tr>
														<td><span class="dinfo_title">모델번호</span></td>
														<td><span class="dinfo_value" id="device_MODEL_NAME">${deviceModelInfo.modelName}</span></td>
													</tr>
												</table></td>
										<td>
											<table>																	
											<tr>
												<td><span class="dinfo_title">패키지명</span></td>
												<td><span class="dinfo_value" id="firmwarePackageName">${firmwareInfo['package']}</span></td>
												<td><span style="display: none;" id="basicFirmwareId">${firmwareInfo.id}</span></td>
											</tr>															
											<tr>
												<td><span class="dinfo_title">생성일시</span></td>
												<td><span class="dinfo_value" id="">${firmwareInfo.createTime}</span></td>
											</tr>															
											<tr>
												<td><span class="dinfo_title">갱신일시</span></td>
												<td><span class="dinfo_value" id="">${firmwareInfo.updateTime}</span></td>
											</tr>
											<tr>
												<td><span class="dinfo_title">설명</span></td>
												<td><span class="dinfo_value" id="firmwareDescription">${firmwareInfo.description}</span></td>
											</tr>
											</table>
										</td>
									</tr>
								</table>
							</div>
						</div>

					</div>
					
					<div style="height: 25px;">
						<input type="submit" class="search_bt_green_detail" id="btn_deleteVersion" value="삭제" />
						<input type="submit" class="search_bt_green_detail" id="btn_updateVersion" value="편집" />
						<input type="submit" class="search_bt_green_detail" id="btn_registerVersion" value="추가" />
					</div>
					<div class="content_title_box obstacle_title">
						<p class="content_title">버전 목록
							
						</p>
					</div>
					<div class="device_list_wrap_100py2">

						<table id="firmwareVersion_table" border="0" cellspacing="0" cellpadding="0" class="table_type_nomal_scroll_height">
							<tr>
								<th></th>
								<th>버전</th>
								<th>파일크기</th>
								<th>파일저장경로</th>
								<th>생성일시</th>
							</tr>
							<c:choose>
								<c:when test="${!empty firmwareVersionList}">
									<c:forEach items="${firmwareVersionList}" var="firmwareVersion" varStatus="status">
										<tr id="versionListRow">
											<td><input type="checkbox" id="cb_${firmwareVersion['firmwareId']}" name="versionChecked" curVersion="${firmwareVersion['version']}" curChecksum="${firmwareVersion['checksum']}" class="updateVersionCheckbox" /></td>
											<td id="firmVersion">${firmwareVersion['version']}</td>
											<td id="firmVerSize">${firmwareVersion['fileSize']}</td>
											<td id="firmVerUrl">${fn:substringAfter(firmwareVersion['fileUrl'], "}/")}</td>
											<td id="firmVerCreTime">${firmwareVersion['createTime']}</td>
											<td id="firmCheckSum" style="display: none;">${firmwareVersion['checksum']}</td>
										</tr>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<tr>
										<td colspan="6">조회된 데이터가 없습니다.</td>
									</tr>
								</c:otherwise>
							</c:choose>
						</table>


					</div>
					
				</div>
			</div>
			<!-- /#container -->
			<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
		</div>
		<!-- /#wrapper -->
		
		<!-- 기본정보편집 -->
		<div id="dialog-updateFirmBasic" title="기본정보수정">
		  <form id="form-updateFirmBasic" name="form-updateFirmBasic">
		    <fieldset>
		    	<table style="width: 250px; height: 80px;">
		    		<tr style="margin-bottom: 15px;">
		    			<td><label id="firmware_pack" for="firmware_pack" class="dm_form_label">패키지명</label></td>
		    			<td><input type="text" name="firmware_package" id="firmware_package" value="${firmwareInfo['package']}" class="text ui-widget-content ui-corner-all" /></td>
		    		</tr>
		    		<tr>
		    			<td><label for="firmware_des" class="firmware_des">설명</label></td>
		    			<td><textarea name="firmware_description" id="firmware_description" class="text ui-widget-content ui-corner-all" style="resize: none; width: 153px;">${firmwareInfo.description}</textarea></td>
		    		</tr>
		    	</table>
		      
		      
		      <!-- <input type="text" name="firmware_des" id="firmware_des" value="" class="text ui-widget-content ui-corner-all"> -->
		      
		      <!-- Allow form submission with keyboard without duplicating the dialog button -->
		      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px" />
		    </fieldset>
		  </form>
		</div>
		
		<!-- 버전정보편집 -->
		<div id="dialog-updateVersion" title="버전수정">
		  <form id="form-updateVersion" name="form-updateVersion" enctype="multipart/form-data">
		    <fieldset>
		    	<table style="width: 250px; height: 80px;">
		    		<input type="hidden" value="${firmwareVersion['checksum']}" id="fimware_checksum" />
		    		<tr style="margin-bottom: 15px;">
		    			<td><label id="firmware_ver" for="firmware_ver" class="dm_form_label">VERSION</label></td>
		    			<td><input type="text" disabled="disabled" name="firmware_upversion" id="firmware_upversion" value="" class="text ui-widget-content ui-corner-all" /></td>
		    		</tr>
		    		<!-- 잠시 HIDDEN으로 -->
		    		<tr style="margin-bottom: 15px; display: none;">
		    			<td><label id="firmware_check" for="firmware_check" class="dm_form_label">CHECKSUM</label></td>
		    			<td><input type="hidden" name="firmware_upcheckSum" id="firmware_upcheckSum" value="" class="text ui-widget-content ui-corner-all" /></td>
		    		</tr>
		    		<tr>
		    			<td><label for="firmware_file" class="firmware_file">FILE</label></td>
		    			<!-- <td>
		    				<input type="text" id="updateFileName" class="file_text_ver ui-widget-content ui-corner-all" placeholder="파일을 첨부해주세요" readonly="readonly" />
						</td> -->
						<td>
							<!-- <div class="file_button_ver">
								<input type="button" id="btnFileSelect" class="" value="파일선택" />	
								<input type="file" id="btnFileName" class="file_input_hidden" onchange="javascript: document.getElementById('updateFileName').value = this.value" />
							</div> -->
							<input type="file" id="updateFile" name="updateFile" style="width: 200px;"/>
						</td>
		    		</tr>
		    	</table>
		      
		      <!-- <input type="text" name="firmware_des" id="firmware_des" value="" class="text ui-widget-content ui-corner-all"> -->
		      
		      <!-- Allow form submission with keyboard without duplicating the dialog button -->
		      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px" />
		    </fieldset>
		  </form>
		</div>
		
		<!-- 버전정보추가 -->
		<div id="dialog-registerVersion" title="버전추가">
		  <form id="form-registerVersion" name="form-registerVersion" enctype="multipart/form-data">
		    <fieldset>
		    	<table style="width: 250px; height: 100px;">
		    		<tr style="margin-bottom: 15px;">
		    			<td><label id="firmware_inver" for="firmware_inver" class="dm_form_label">VERSION</label></td>
		    			<td><input type="text" name="firmware_inversion" id="firmware_inverson" value="" class="text ui-widget-content ui-corner-all" /></td>
		    		</tr>
		    		<tr style="margin-bottom: 15px;">
		    			<td><label id="firmware_incheck" for="firmware_incheck" class="dm_form_label">CHECKSUM</label></td>
		    			<td><input type="text" name="firmware_incheckSum" id="firmware_incheckSum" value="" class="text ui-widget-content ui-corner-all" /></td>
		    		</tr>
		    		<tr>
		    			<td><label for="firmware_infile" class="firmware_infile">FILE</label></td>
		    			<!-- <td>
		    				<input type="text" id="insertFileName" class="file_text_ver ui-widget-content ui-corner-all" placeholder="파일을 첨부해주세요" readonly="readonly" />
						</td> -->
						<td>
							<!-- <div class="file_button_ver">
								<input type="button" id="btnFileSelect" class="" value="파일선택" />	
								<input type="file" id="btnFileName" class="file_input_hidden" onchange="javascript: document.getElementById('insertFileName').value = this.value" />
								
							</div> -->
							<input type="file" id="inputFile" name="inputFile" />
						</td>
		    		</tr>
		    	</table>
		      
		      <!-- <input type="text" name="firmware_des" id="firmware_des" value="" class="text ui-widget-content ui-corner-all"> -->
		      
		      <!-- Allow form submission with keyboard without duplicating the dialog button -->
		      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px" />
		    </fieldset>
		  </form>
		</div>
		
		<!-- 버전정보삭제 -->
		<div id="dialog-deleteVersion" title="버전추가">
		  <form id="form-deleteVersion" name="form-deleteVersion">
		  </form>
		</div>
</body>
</html>