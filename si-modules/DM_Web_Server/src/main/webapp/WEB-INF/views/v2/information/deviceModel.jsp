<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
    <title>OASIS SI Admin</title>
	<%@ include file="/WEB-INF/views/v2/common/common_js.jsp"%>
	
	<%@ include file="/v2/js/herit/conf/prop.jsp" %>
	<script src="<c:url value="/v2/js/herit/hdb.js" />"></script>
	<script src="<c:url value="/v2/js/herit/information/deviceModel.js" />"></script>
	
    <script type="text/javaScript" language="javascript">
		$(document).ready(function() {
			initUI();
			
			$('.i-checks').iCheck({
				checkboxClass: 'icheckbox_square-green',
				radioClass: 'iradio_square-green'
			});
		});
		
		var deviceModelList = ${deviceModelListJson};

		function initUI() {
			$("#side-menu_operator").addClass("active");
			$("#side-menu_operator ul").addClass("in");
			$("#side-menu_operator ul li:eq(3)").addClass("active");
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
				<h2>디바이스 모델 관리</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>운영 관리</li>
					<li class="active"><strong>디바이스 모델 관리</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">디바이스 모델 목록</h3>
						</div>
						<div class="ibox-content">
							<div class="row text-right">
								<button type="button" id="addDeviceModel" class="btn btn-sm btn-success"> 모델 등록</button> 
								<button type="button" id="delDeviceModel" class="btn btn-sm btn-danger" style="margin-left:2px;"> 선택 삭제</button>
							</div>

							<div>&nbsp;</div>
							
							<table class="table table-bordered table-hover">
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
							</table>
						</div>
					</div>
				</div>
			</div>
</form>
		</div>
		
		
		<div class="footer">
			<%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
		</div>
	</div>
</div>


<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form id="modalForm">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">디바이스 모델 등록</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>제조사 OUI</dt>
						<dd><input type="text" id="oui" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>제조사 이름</dt>
						<dd><input type="text" id="manufacturer" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>모델번호</dt>
						<dd><input type="text" id="modelName" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>디바이스타입</dt>
						<dd><input type="text" id="deviceType" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>아이콘URL</dt>
						<dd><input type="text" id="iconUrl" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>프로파일버전</dt>
						<dd><input type="text" id="profileVer" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>설명</dt>
						<dd><input type="text" id="description" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>적용여부</dt>
						<dd>
							<select id="applyYn" class="form-control input-sm">
								<option value="">==선택==</option>
								<option value="Y">예</option>
								<option value="N">아니오</option>
							</select>
						</dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' onclick='deviceModel.fnCommFileUpload(); return false;' class='btn btn-primary'>저장</button>
				<button type='button' onclick='deviceModel.fnInputValInit();' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>


</body>
</html>
