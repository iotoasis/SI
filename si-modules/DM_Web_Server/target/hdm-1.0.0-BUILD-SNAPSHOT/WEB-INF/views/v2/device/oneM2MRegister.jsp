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

	<script src="<c:url value="/v2/js/herit/hdb.js" />"></script>
	<script src="<c:url value="/v2/js/herit/dm.js" />"></script>
	<script src="<c:url value="/v2/js/herit/oneM2MDeviceRegister.js" />"></script>
	
    <script type="text/javaScript" language="javascript">
	    var gr_id="device";
	    
		$(document).ready(function() {
			initUI();
			initUI2();
		});
		
		function initUI() {
			$("#side-menu_device").addClass("active");
			$("#side-menu_device ul").addClass("in");
			$("#side-menu_device ul li:eq(3)").addClass("active");
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
				<h2>oneM2M 디바이스 등록</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>디바이스 관리</li>
					<li class="active"><strong>oneM2M 디바이스 등록</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">oneM2M 디바이스 정보 입력</h3>
						</div>
						<div class="ibox-content form-horizontal">
							<div class="form-group">
								<label class="col-sm-2 control-label">디바이스 모델</label>
								<div class="col-sm-10">
									<select class="form-control" name="deviceModel" id="search_deviceModel" value="${param.deviceModel}" >
										<c:forEach items="${deviceModelList}" var="deviceModel">
											<option value="${deviceModel.id}|${deviceModel.oui}|${deviceModel.modelName}"														
											<c:if test="${fn:contains(param.deviceModel, deviceModel.modelName)}">selected</c:if>
											>${deviceModel.manufacturer}-${deviceModel.modelName}
											</option>
										</c:forEach>			
									</select>											
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">시리얼번호</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="serialNo" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">ID</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="authId" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">Password</label>
								<div class="col-sm-10">
									<input type="password" class="form-control" id="authPwd" />
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">연동 서버</label>
								<div class="col-sm-10">
									<label class="form-inline">
										<select class="form-control">
											<option>KETI</option>
										</select>
										<a class="btn btn-sm btn-primary" href="javascript:$('#myModal').modal('show');">검색</a>
									</label>
								</div>
							</div>
							<div class="form-group">
								<label class="col-sm-2 control-label">AEID</label>
								<div class="col-sm-10">
									<input type="text" class="form-control" id="oneM2MAEID" disabled="disabled" />
								</div>
							</div>
							<div class="hr-line-dashed"></div>
							<div class="text-center">
								<a class="btn btn-primary" href="javascript:oneM2MRegister();">저장</a>
							</div>
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


<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form id="form-oneM2MDeviceSearch" name="form-oneM2MDeviceSearch">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">oneM2M 디바이스 검색</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<label class="form-inline">
						시리얼번호&nbsp;
						<div class="input-group">
							<input type="text" name="oneM2MSerialNo" id="oneM2MSerialNo" class="input-sm form-control" placeholder="시리얼번호" />
							<span class="input-group-btn">
								<button type="button" id="oneM2MBtnSearch" class="btn btn-sm btn-primary"> 검색</button>
							</span>
						</div>
					</label>
				</div>
				<div>&nbsp;</div>
				<div class="row">
			    	<table border="0" cellspacing="0" cellpadding="0" class="table table-bordered table-hover">
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
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' class='btn btn-white' data-dismiss='modal'>닫기</button>
			</div>
		</div>
</form>
	</div>
</div>


</body>
</html>
