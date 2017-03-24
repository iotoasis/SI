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
	<script src="<c:url value="/v2/js/herit/information/deviceModelDetail.js" />"></script>
	
    <script type="text/javaScript" language="javascript">
		$(document).ready(function() {
			initUI();
		});
		
		var contextPath = "${pageContext.request.contextPath}";
		var param = ${paramJson};
		var deviceModelId = ${param.id};
		var oui = null;
		var modelName = null;

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
				<h2>디바이스 모델 상세정보</h2>
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
							<h3 class="no-margins">디바이스 모델 상세정보</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-5 b-r">
									<h3><strong>기본정보</strong></h3>
									<img src="" width="150" id="iconUrl" />
									<dl class="dl-horizontal">
										<dt>제조사</dt>
										<dd id="model_manufacturer"></dd>
										<dt>제조사 OUI</dt>
										<dd id="model_oui"></dd>
										<dt>모델번호</dt>
										<dd id="model_modelName"></dd>
									</dl>
								</div>
								<div class="col-sm-5">
									<h3><strong>부가정보</strong></h3>
									<dl class="dl-horizontal">
										<dt>설명</dt>
										<dd id="model_description"></dd>
										<dt>생성일시</dt>
										<dd id="model_createTime"></dd>
										<dt>갱신일시</dt>
										<dd id="model_updateTime"></dd>
									</dl>
									<input type="hidden" id="model_id" class="i_text" />
									<input type="hidden" id="model_deviceType" class="i_text" />
									<input type="hidden" id="model_applyYn" class="i_text" />
								</div>
								<div class="col-sm-2 text-right">
									<button type="button" class="btn btn-sm btn-primary" id="modifyDeviceModel">수정</button>
								</div>
							</div>
						</div>
					</div>
					<div class="ibox float-e-margins">
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-2">
									<select id="profileVerSelector" class="input-sm form-control input-s-sm inline"></select>
								</div>
								<div class="col-sm-10 text-right">
									<button type="button" id="addVersion"  class="btn btn-sm btn-primary"> 버전 추가</button>
									<button type="button" id="addResource" class="btn btn-sm btn-success" style="margin-left:2px;"> 리소스 추가</button> 
								</div>
							</div>
							<div>&nbsp;</div>
							<div class="row">
								<h3><strong>관리객체 목록</strong></h3>
							</div>
							<table class="table table-bordered table-hover" id="mo_table">
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
			</div>
</form>
		</div>
		
		
		<div class="footer">
			<%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
		</div>
	</div>
</div>


<input type="hidden" id="id" class="i_text" />
<input type="hidden" id="moProfileId" class="i_text" />
<input type="hidden" id="actionType" class="i_text" />


<!-- 디바이스모델 수정 -->
<div class="modal inmodal" id="modifyDeviceModelModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form id="modalForm">
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">디바이스 모델 수정</h4>
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
				<button type='button' onclick='deviceModel.fnModifyDeviceModel(); return false;' class='btn btn-primary'>저장</button>
				<button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>


<!-- 리소스 추가 -->
<div class="modal inmodal" id="addResourceModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form>
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">리소스 등록</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>표시이름</dt>
						<dd><input type="text" id="displayName" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>URI</dt>
						<dd><input type="text" id="resourceUri" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>자료형</dt>
						<dd><input type="text" id="dataType" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>단위</dt>
						<dd><input type="text" id="unit" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>기본값</dt>
						<dd><input type="text" id="defaultValue" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>오퍼레이션</dt>
						<dd><input type="text" id="operation" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>노티옵션</dt>
						<dd>
							<select id="notiType" class="form-control input-sm">
								<option value="">==선택==</option>
								<option value="0">노티안함</option>
								<option value="1">변경될때마다</option>
								<option value="2">노티조건에 부합할때마다</option>
								<option value="3">UPDATE주기마다</option>
							</select>
						</dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>오류필드여부</dt>
						<dd>
							<select id="isError" class="form-control input-sm">
								<option value="">==선택==</option>
								<option value="Y">예</option>
								<option value="N">아니오</option>
							</select>
						</dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>진단필드여부</dt>
						<dd>
							<select id="isDiagnostic" class="form-control input-sm">
								<option value="">==선택==</option>
								<option value="Y">예</option>
								<option value="N">아니오</option>
							</select>
						</dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>이력저장여부</dt>
						<dd>
							<select id="isHistorical" class="form-control input-sm">
								<option value="">==선택==</option>
								<option value="Y">예</option>
								<option value="N">아니오</option>
							</select>
						</dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>필수여부</dt>
						<dd>
							<select id="isMandatory" class="form-control input-sm">
								<option value="">==선택==</option>
								<option value="Y">예</option>
								<option value="N">아니오</option>
							</select>
						</dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' onclick='deviceModel.fnAddResource(); return false;' class='btn btn-primary'>저장</button>
				<button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>


<!-- 버전 추가 -->
<div class="modal inmodal" id="addVersionModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form>
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">버전 등록</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>추가 버전 입력</dt>
						<dd><input type="text" id="newVersionSelector" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>현재 버전 선택</dt>
						<dd>
							<select id="preVersionSelector" class="form-control input-sm">
							</select>
						</dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' onclick='deviceModel.fnAddVersion(); return false;' class='btn btn-primary'>저장</button>
				<button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>


<!-- 옵션 데이터 목록보기 -->
<div class="modal inmodal" id="optionDataListModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form>
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">옵션 데이터 목록</h4>
			</div>
			<div class="modal-body" style="height:400px; overflow:auto;">
				<div class="row">
					<div class="col-sm-12 text-right">
						<button type="button" id="addOptionData" class="btn btn-sm btn-primary"> 옵션데이터 등록</button>
						<button type="button" id="delOptionData" class="btn btn-sm btn-danger" style="margin-left:2px;"> 선택 삭제</button>
					</div>
					<div>&nbsp;</div>
					<table class="table table-bordered table-hover">
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
			<div class="modal-footer">
				<button type='button' class='btn btn-white' data-dismiss='modal'>닫기</button>
			</div>
		</div>
</form>
	</div>
</div>
<div class="modal inmodal" id="addOptionDataModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form>
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">옵션 데이터 등록</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>표시순서</dt>
						<dd><input type="text" id="order" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>표시데이터</dt>
						<dd><input type="text" id="displayData" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>데이터</dt>
						<dd><input type="text" id="data" class="form-control input-sm" /></dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' onclick='deviceModel.fnAddOptionData(); return false;' class='btn btn-primary'>저장</button>
				<button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>


<!-- 에러코드 목록보기 -->
<div class="modal inmodal" id="errorCodeListModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form>
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">에러 데이터 목록</h4>
			</div>
			<div class="modal-body" style="height:400px; overflow:auto;">
				<div class="row">
					<div class="col-sm-12 text-right">
						<button type="button" id="addErrorCode" class="btn btn-sm btn-primary"> 에러코드 등록</button>
						<button type="button" id="delErrorCode" class="btn btn-sm btn-danger" style="margin-left:2px;"> 선택 삭제</button>
					</div>
					<div>&nbsp;</div>
					<table class="table table-bordered table-hover">
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
			<div class="modal-footer">
				<button type='button' class='btn btn-white' data-dismiss='modal'>닫기</button>
			</div>
		</div>
</form>
	</div>
</div>
<div class="modal inmodal" id="addErrorCodeModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form>
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">에러코드 등록</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>오류코드</dt>
						<dd><input type="text" id="errorCode" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>장애등급</dt>
						<dd>
							<select id="errorGrade" class="form-control input-sm">
								<option value="0">NO ERROR</option>
								<option value="1">MINOR</option>
								<option value="2">MAJOR</option>
								<option value="3">CRITICAL</option>
								<option value="4">FATAL</option>
							</select>
						</dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>오류이름</dt>
						<dd><input type="text" id="errorName" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>설명</dt>
						<dd><input type="text" id="errorDescription" class="form-control input-sm" /></dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' onclick='deviceModel.fnAddErrorCode(); return false;' class='btn btn-primary'>저장</button>
				<button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>


<!-- 노티조건 목록보기 -->
<div class="modal inmodal" id="notiConditionListModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form>
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">노티 옵션 목록</h4>
			</div>
			<div class="modal-body" style="height:400px; overflow:auto;">
				<div class="row">
					<div class="col-sm-12 text-right">
						<button type="button" id="addNotiCondition" class="btn btn-sm btn-primary"> 노티조건 등록</button>
						<button type="button" id="delNotiCondition" class="btn btn-sm btn-danger" style="margin-left:2px;"> 선택 삭제</button>
					</div>
					<div>&nbsp;</div>
					<table class="table table-bordered table-hover">
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
			<div class="modal-footer">
				<button type='button' class='btn btn-white' data-dismiss='modal'>닫기</button>
			</div>
		</div>
</form>
	</div>
</div>
<div class="modal inmodal" id="addNotiConditionModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form>
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title">노티조건 등록</h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>노티타입</dt>
						<dd>
							<select id="conditionType" class="form-control input-sm">
								<option value="P">Period (sec)</option>
								<option value="G">Greater Than</option>
								<option value="L">Less Than</option>
								<option value="S">STEP</option>
							</select>
						</dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>노티데이터</dt>
						<dd><input type="text" id="condition" class="form-control input-sm" /></dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
				<button type='button' onclick='deviceModel.fnAddNotiCondition(); return false;' class='btn btn-primary'>저장</button>
				<button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>
			</div>
		</div>
</form>
	</div>
</div>


</body>
</html>
