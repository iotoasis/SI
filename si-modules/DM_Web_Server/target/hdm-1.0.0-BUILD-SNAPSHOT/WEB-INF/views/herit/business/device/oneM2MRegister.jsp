<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
var gr_id="device";
var sub_num="4";
$(document).ready(function() {
	initUI();
})
</script>

<script src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/dm.js"></script>
<script src="${pageContext.request.contextPath}/js/herit/oneM2MDeviceRegister.js"></script>
<%-- <script src="${pageContext.request.contextPath}/js/herit/deviceRegister.js"></script> --%>
</head>
<body>
	<!-- #wrapper -->
	<div id="wrapper">
		<!-- .header_wrap -->
		<%@ include file="/WEB-INF/views/common/include/hdm_menu.jsp"%>
		<!-- /.header_wrap -->
		<!-- #container -->
		<div class="sub0103">
			<div id="container">
				<div class="page_title">
					<h2>oneM2M 디바이스 등록</h2>
					<div class="position_text">
						<img src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif" alt="" />
						디바이스 관리 &gt; oneM2M 디바이스 등록
					</div>
				</div>
				<div class="content_wrap top_pad_30">
					<div class="content_title_box" style="height: 24px">
						<p class="content_title">oneM2M 디바이스 정보 입력</p>
					</div>
						<div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<form action="">
									<div class="search_form_wrap" style="padding:20px;">
										<table>
										<tr>
											<td height="30" width="150">디바이스 모델</td>
											<td>							
												<select name="deviceModel" id="search_deviceModel" value="${param.deviceModel}" >
													<c:forEach items="${deviceModelList}" var="deviceModel">
														<option value="${deviceModel.id}|${deviceModel.oui}|${deviceModel.modelName}"														
														<c:if test="${fn:contains(param.deviceModel, deviceModel.modelName)}">selected</c:if>
														>${deviceModel.manufacturer}-${deviceModel.modelName}
														</option>
													</c:forEach>			
												</select>											
											</td>
										</tr>
										<tr>
											<td height="30" width="150">시리얼번호</td>
											<td>
												<input type="text" id="serialNo" size="38" disabled="disabled"/>
											</td>
										</tr>
										<tr>
											<td height="30" width="150">ID</td>
											<td>
												<input type="text" id="authId" size="38" />
											</td>
										</tr>
										<tr>
											<td height="30" width="150">Password</td>
											<td>
												<input type="password" id="authPwd" size="38" />
											</td>
										</tr>
										<tr>
											<td height="30" width="150">연동 서버</td>
											<td>
												<select style="width: 120px; margin-right: 63px;">
													<option>KETI</option>
												</select>
												<input name="button" type="button" class="search_bt_green" style="width: 50px;" id="oneM2MSearch" value="검색" />
											</td>
										</tr>
										<tr>
											<td height="30" width="150">AEID</td>
											<td>
												<input type="text" id="oneM2MAEID" size="38" disabled="disabled" />
											</td>
										</tr>
						
										<tr>
											<td height="30" width="150">&nbsp;</td>
											<td height="30" colspan="2">
											<input name="button" type="button" class="search_bt_green" id="btnRegister" value="저장" />
											</td>
										</tr>
										</table>
									</div>
								</form>
							</div>
						</div>
						<!-- Search END-->
					</div>
				</div>
			</div>
			<!-- /#container -->
			<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
		</div>
	</div>
		<!-- /#wrapper -->
		
		<!-- oneM2M서버 디바이스 검색 다이얼로그 -->
		<div id="dialog-oneM2MDeviceSearch" title="oneM2M 디바이스 검색">
		  <form id="form-oneM2MDeviceSearch" name="form-oneM2MDeviceSearch">
		    <fieldset>
		    	<table style="width: 250px; height: 80px;">
		    		<tr style="margin-bottom: 15px;">
		    			<td><label id="oneM2M_sn" for="oneM2M_sn" class="dm_form_label" style="width: 70px;">시리얼번호</label></td>
		    			<td><input type="text" name="oneM2MSerialNo" id="oneM2MSerialNo" value="" class="text ui-widget-content ui-corner-all" /></td>
		    			<td><input id="oneM2MBtnSearch" type="button" style="margin-left: 20px;" value="검색" /></td>
		    		</tr>
		    	</table>
		    	
		    	<table border="0" cellspacing="0" cellpadding="0" class="table_type_nomal_scroll_height">
					<thead>
						<tr>
							<th>디바이스 명</th>
							<th>-</th>
						</tr>
					</thead>
					<tbody id="oneM2MDeviceList">
						<tr>
							<td colspan="2">시리얼번호를 입력 후 검색해주세요.</td>
						</tr>
					</tbody>
				</table>
		      
		      
		      <!-- <input type="text" name="firmware_des" id="firmware_des" value="" class="text ui-widget-content ui-corner-all"> -->
		      
		      <!-- Allow form submission with keyboard without duplicating the dialog button -->
		      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px" />
		    </fieldset>
		  </form>
		</div>
</body>
</html>