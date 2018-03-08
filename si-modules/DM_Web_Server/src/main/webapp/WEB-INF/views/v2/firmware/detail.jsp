<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/tld/herit.tld" prefix="herit" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
    <title>OASIS SI Admin</title>
	<%@ include file="/WEB-INF/views/v2/common/common_js.jsp"%>
	<script language="javascript" src="<c:url value="/v2/js/herit/firmwareDetail.js" />"></script>
	<script language="javascript" src="<c:url value="/v2/js/herit/hdb.js" />"></script>
	<script language="javascript" src="<c:url value="/v2/js/herit/dm.js" />"></script>
	
	
    <script type="text/javaScript" language="javascript">
		$(document).ready(function() {
			initUI();
			initUI2();
			
			$('.i-checks').iCheck({
				checkboxClass: 'icheckbox_square-green',
				radioClass: 'iradio_square-green',
			});
		});

		var gr_id = "firmware";
		var contextPath = "${pageContext.request.contextPath}";
		var deviceModelInfoJson = ${deviceModelInfoJson};
		var firmwareInfoJson = ${firmwareInfoJson};
		var firmwareVersionListJson = ${firmwareVersionListJson};
		
		function initUI() {
			$("#side-menu_firmware").addClass("active");
			$("#side-menu_firmware ul").addClass("in");
			$("#side-menu_firmware ul li:eq(0)").addClass("active");
		}
		
		function showBasicUpdate() {
			$("#basicModal").modal('show');
		}
		
		function showRegisterVersion() {
			$("#registerVersionModal").modal('show');
		}
    </script>
</head>
<body>

<div id="wrapper">

	<nav class="navbar-default navbar-static-side" role="navigation">
		<div class="sidebar-collapse">
			<%@ include file="/WEB-INF/views/v2/common/common_sidemenu.jsp"%>
		</div>
	</nav>


	<div id="page-wrapper" class="gray-bg">
		<div class="row border-bottom">
			<%@ include file="/WEB-INF/views/v2/common/common_topbar.jsp"%>
		</div>
		

		<div class="row wrapper border-bottom white-bg page-heading">
			<div class="col-sm-4">
				<h2>펌웨어 목록 조회</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>펌웨어 관리</li>
					<li class="active"><strong>펌웨어 목록 조회</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">상세정보</h3>
							<div class="ibox-tools">
							</div>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-5 b-r">
									<h3><strong>모델정보</strong></h3>
									<img src="<c:url value="/images/hitdm/temp/settopbox.jpg" />" width="150" />
									<dl class="dl-horizontal">
										<dt style="width: 100px;">제조사</dt>
										<dd style="margin-left: 120px;" id="device_MANUFACTURER"><c:out value="${deviceModelInfo.manufacturer}" /></dd>
										<dt style="width: 100px;">제조사 OUI</dt>
										<dd style="margin-left: 120px;" id="device_OUI"><c:out value="${deviceModelInfo.oui}" /></dd>
										<dt style="width: 100px;">모델번호</dt>
										<dd style="margin-left: 120px;" id="device_MODEL_NAME"><c:out value="${deviceModelInfo.modelName}" /></dd>
									</dl>
								</div>
								<div class="col-sm-5">
									<h3><strong>기본정보</strong></h3>
									<dl class="dl-horizontal">
										<dt style="width: 100px;">패키지명</dt>
										<dd style="margin-left: 120px;" id="firmwarePackageName"><c:out value="${firmwareInfo['package']}" /></dd>
										<dt style="width: 100px;">생성일시</dt>
										<dd style="margin-left: 120px;"><c:out value="${firmwareInfo.createTime}" /></dd>
										<dt style="width: 100px;">갱신일시</dt>
										<dd style="margin-left: 120px;"><c:out value="${firmwareInfo.updateTime}" /></dd>
										<dt style="width: 100px;">설명</dt>
										<dd style="margin-left: 120px;" id="firmwareDescription"><c:out value="${firmwareInfo.description}" /></dd>
									</dl>
									<span style="display: none;" id="basicFirmwareId"><c:out value="${firmwareInfo.id}" /></span>
								</div>
								<div class="col-sm-2 text-right">
									<button type="button" class="btn btn-sm btn-primary" onclick="showBasicUpdate();">편집</button>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">버전목록</h3>
						</div>
						<div class="ibox-content">
							<div class="col-sm-12 text-right">
										<button type="button" onclick="showRegisterVersion(); return false;" class="btn btn-sm btn-primary"> 추가</button>
										<button type="button" id="btn_updateVersion" class="btn btn-sm btn-success" style="margin-left:2px;"> 편집</button> 
										<button type="button" id="btn_deleteVersion" class="btn btn-sm btn-danger" style="margin-left:2px;"> 삭제</button>
							</div>
							<table class="table table-bordered table-hover" id="firmwareVersion_table">
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
												<td><input type="checkbox" id="cb_${firmwareVersion['firmwareId']}" name="versionChecked" curVersion="${firmwareVersion['version']}" curChecksum="${firmwareVersion['checksum']}" class="i-checks" /></td>
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
											<td colspan="5">조회된 데이터가 없습니다.</td>
										</tr>
									</c:otherwise>
								</c:choose>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
		<div class="footer">
			<%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
		</div>
	</div>
</div>


<div class="modal inmodal" id="basicModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form id="form-updateFirmBasic" name="form-updateFirmBasic">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">기본정보수정</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>패키지명</dt>
						<dd><input type="text" name="firmware_package" id="firmware_package" value="${firmwareInfo['package']}" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>설명</dt>
						<dd><textarea name="firmware_description" id="firmware_description" class="form-control input-sm">${firmwareInfo.description}</textarea></dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' onclick='updateFirmwareBasic(); return false;' class='btn btn-primary'>수정</button>
				<button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>


<div class="modal inmodal" id="registerVersionModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form id="form-registerVersion" name="form-registerVersion" enctype="multipart/form-data">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">버전추가</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>VERSION</dt>
						<dd><input type="text" name="firmware_inversion" id="firmware_inverson" value="" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>CHECKSUM</dt>
						<dd><input type="text" name="firmware_incheckSum" id="firmware_incheckSum" value="" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>FILE</dt>
						<dd><input type="file" id="inputFile" name="inputFile" class="form-control input-sm" /></dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' onclick='insertFirmwareVersion(); return false;' class='btn btn-primary'>저장</button>
				<button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>


<div class="modal inmodal" id="updateVersionModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form id="form-updateVersion" name="form-updateVersion" enctype="multipart/form-data">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">버전수정</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>VERSION</dt>
						<dd>
							<input type="text" disabled="disabled" name="firmware_upversion" id="firmware_upversion" value="" class="form-control input-sm" />
							<input type="hidden" name="firmware_upcheckSum" id="firmware_upcheckSum" value="" />
						</dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>FILE</dt>
						<dd><input type="file" id="updateFile" name="updateFile" class="form-control input-sm" /></dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' onclick='updateFirmwareVersion(); return false;' class='btn btn-primary'>저장</button>
				<button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>

</body>

</html>
