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
<title>HIT DM Admin - Firmware Detail</title>
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp"%>


<script type="text/javascript">
var gr_id="information";
var sub_num="1";

var deviceModelList = ${deviceModelListJson};
</script>

<!-- Properties -->
<%@ include file="/js/herit/conf/prop.jsp" %>
<!-- JQuery Dialog Module -->
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/util/ui/dialog.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/common/common.js"></script>

<script language="javascript" src="${pageContext.request.contextPath}/js/herit/information/deviceModel.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
</head>
<body>
	<!-- #wrapper -->
	<div id="wrapper">

		<!-- .header_wrap -->
		<%@ include file="/WEB-INF/views/common/include/hdm_menu.jsp"%>
		<!-- /.header_wrap -->


		<!-- #container -->
		<div class="sub0101">
			<div id="container">
				<div class="page_title">
					<h2>디바이스 모델 목록</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 정보관리 &gt; 디바이스 모델 관리
					</div>
				</div>

				<div class="content_wrap top_pad_30">
					<div id="title_button_list">
						<div class="board_top_bt">
							<a id="delDeviceModel" href="javascript:void(0);">선택 삭제</a>
						</div>
						<div class="board_top_bt">
							<a id="addDeviceModel" href="javascript:void(0);">모델 등록</a>
						</div>
					</div>

					<!-- Search-->
					<!-- <div class="bott_mar_30">
						<div>
							<div class="search_form_only">
								<div class="search_form_wrap">
									<div class="search_form_div">
										<span class="left_pad_30">모델번호</span> <input type="text"
											name="" id="" class="i_text" /> <input name="Submit"
											type="submit" class="search_bt_green" value="검색" />
									</div>
								</div>
							</div>
						</div>
						 -->

					</div>
					<!-- Search END-->
					<!-- /-->
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_type_nomal_scroll2 bott_mar_30">
							<thead>
							<tr>
								<th>선택</th>
								<th>개발사</th>
								<th>모델번호</th>
								<th>설명</th>
								<th>등록일자</th>
								<th>수정일자</th>
							</tr>
						</thead>
						<tbody id="deviceModelList">
						</tbody>
						<%-- <c:choose>						
							<c:when test="${!empty deviceModelList}">
								<c:forEach items="${deviceModelList}" var="deviceModel" varStatus="status">
								
								<tr>
									<td>${deviceModel['manufacturer']}</td>
									<td><a href="${pageContext.request.contextPath}/information/deviceModel/detail.do?id=${deviceModel['id']}">${deviceModel['modelName']}</a></td>
									<td>${deviceModel['description']}</td>
									<td>${deviceModel['createTime']}</td>
									<td>${deviceModel['updateTime']}</td>
								</tr>
							
								</c:forEach>
							</c:when>
							<c:otherwise>
							
							<tr>
								<td colspan="6">조회된 데이터가 없습니다.</td>
							</tr>
			
							</c:otherwise> 
						</c:choose> --%>
					</table>
					<!-- /-->

				</div>
			</div>
		</div>



		<!-- /#container -->
		<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
	</div>
	<!-- /#wrapper -->

	<div id="addDialog" class="ui-widget">
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
	
	
</body>
</html>