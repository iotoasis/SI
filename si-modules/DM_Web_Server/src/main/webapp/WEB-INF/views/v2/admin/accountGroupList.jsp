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
    <script type="text/javaScript" language="javascript">
		$(document).ready(function() {
			initUI();
		});
		
		function initUI() {
			$("#side-menu_operator").addClass("active");
			$("#side-menu_operator ul").addClass("in");
			$("#side-menu_operator ul li:eq(0)").addClass("active");
			
			$('.i-checks').iCheck({
				checkboxClass: 'icheckbox_square-green',
				radioClass: 'iradio_square-green',
			});
			
			$("input[name='allChk']:checkbox").on('ifChecked ifUnchecked', function(event){
				if (event.type == 'ifChecked') {
					$("input[name='checkList']:checkbox").iCheck('check');
		        } else {
		        	$("input[name='checkList']:checkbox").iCheck('uncheck');
		        }
			});
		}
		
		function fnSearch() {
			var myForm					= document.getElementById("myForm");
			myForm.currPage.value		= "1";
			myForm.action				= "<c:url value='/admin/accountGroup/list.do'/>";
			myForm.submit();
		}
		
		function fnModal(options) {
			$("#modal_id").val("");
			$("#modal_groupCode").val("");
			$("#modal_groupName").val("");
			$("#modal_description").val("");
			
			switch (options.actionType) {
				case "I":
					$(".modal-title").html("권한 그룹 등록");
					$("#modal_groupCode").removeAttr("readonly");
					$(".modal-footer").html("<button type='button' onclick='execInsert(); return false;' class='btn btn-primary'>저장</button><button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>");
					break;
				
				case "V":
					$(".modal-title").html("권한 그룹 정보");
					$("#modal_id").val(options.data.id);
					$("#modal_groupCode").attr("readonly", "readonly");
					$(".modal-footer").html("<button type='button' onclick='execUpdate(); return false;' class='btn btn-primary'>수정</button><button type='button' class='btn btn-white' data-dismiss='modal'>취소</button>");
					$.ajax({
						type: "POST",
						url: "<c:url value="/admin/accountGroup/info.do" />",
						data: {id : options.data.id},
						dataType: "json",
						success: function(response){
							if (response.result) {
								$("#modal_id").val(response.data.id);
								$("#modal_groupCode").val(response.data.groupCode);
								$("#modal_groupName").val(response.data.groupName);
								$("#modal_description").val(response.data.description);
							}
							else {
								alert("권한 그룹 검색중 에러가 발생하였습니다!");
								$("#myModal").modal('hide');
							}
						},
						error: function(e){
							alert("권한 그룹 검색중 에러가 발생하였습니다!");
							$("#myModal").modal('hide');
						}
					});
					break;
			}
			$("#myModal").modal('show');
		}
		
		function fnInsert() {
			fnModal({
				actionType : "I"
			});
		}
		
		function execInsert() {
			if ( !Validation.validator("modalForm") )
				return;
			
			$.ajax({
				type: "POST",
				url: "<c:url value="/admin/accountGroup/insert.do" />",
				data: {
					groupCode : $("#modal_groupCode").val(),
					groupName : $("#modal_groupName").val(),
					description : $("#modal_description").val()
				},
				dataType: "json",
				success: function(response){
					if (response.result) {
						alert("권한 그룹을 등록하였습니다");
						window.location.href = "<c:url value="/admin/accountGroup/list.do" />";
					}
					else {
						alert(response.message);
					}
				},
				error: function(e){
					alert("권한 그룹 등록중 에러가 발생하였습니다!");
				}
			});
		}
		
		function fnDetail(id) {
			fnModal({
				actionType : "V",
				data : {
					id : id
				}
			});
		}
		
		function execUpdate() {
			if ( !Validation.validator("modalForm") )
				return;
			
			$.ajax({
				type: "POST",
				url: "<c:url value="/admin/accountGroup/update.do" />",
				data: {
					id : $("#modal_id").val(),
					groupCode : $("#modal_groupCode").val(),
					groupName : $("#modal_groupName").val(),
					description : $("#modal_description").val()
				},
				dataType: "json",
				success: function(response){
					if (response.result) {
						alert("권한 그룹을 수정하였습니다");
						window.location.href = "<c:url value="/admin/accountGroup/list.do" />";
					}
					else {
						alert(response.message);
					}
				},
				error: function(e){
					alert("권한 그룹 수정중 에러가 발생하였습니다!");
				}
			});
		}

		
		function fnCheckDelete(){
			if (checkOneMoreCheckbox(document.myForm, "checkList")) {
				if (confirm("삭제하시겠습니까?")) {
					var checkList = [];
					$("input[name='checkList']:checkbox").each(function() {
						if (this.checked)
							checkList.push(this.value);
					});
					
					$.ajax({
						type: "POST",
						url: "<c:url value="/admin/accountGroup/delete.do" />",
						data: {checkList : checkList.toString()},
						dataType: "json",
						success: function(response){
							if (response.result) {
								alert("권한 그룹을 삭제하였습니다");
								window.location.href = "<c:url value="/admin/accountGroup/list.do" />";
							}
							else {
								alert(response.message);
							}
						},
						error: function(e){
							alert("권한 그룹 삭제중 에러가 발생하였습니다!");
						}
					});
				}
			}
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
				<h2>권한 그룹 관리</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>운영 관리</li>
					<li class="active"><strong>권한 그룹 관리</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
<form id="myForm" name="myForm" action="<c:url value='/admin/accountGroup/list.do'/>" method="post">
<input type="hidden" name="currPage" value="${currPage}">
<input type="hidden" name="id" value=""/>

			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">권한 그룹</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-3 m-b-xs">
									<select id="searchKey" name="searchKey" class="input-sm form-control input-s-sm inline">
										<option value="GROUP_CODE" <c:if test="${'GROUP_CODE' == accountGroupVO.searchKey}">selected="selected"</c:if> >그룹코드</option>
										<option value="GROUP_NAME" <c:if test="${'GROUP_NAME' == accountGroupVO.searchKey}">selected="selected"</c:if> >그룹명</option>
									</select>
								</div>
								<div class="col-sm-9">
									<div class="input-group">
										<input type="text" name="searchVal" value="<c:out value="${accountGroupVO.searchVal}" />" placeholder="검색어 입력" class="input-sm form-control"> 
										<span class="input-group-btn">
											<button type="button" onclick="fnSearch(); return false;" class="btn btn-sm btn-primary"> 조회</button>
											<button type="button" onclick="fnInsert(); return false;" class="btn btn-sm btn-success" style="margin-left:2px;"> 등록</button> 
											<button type="button" onclick="fnCheckDelete(); return false;" class="btn btn-sm btn-danger" style="margin-left:2px;"> 삭제</button>
										</span>
									</div>
								</div>
							</div>
							
							<div>&nbsp;</div>
							
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th width="5%"><input type="checkbox" class="i-checks" name="allChk" ></th>
										<th width="20%">그룹 코드</th>
										<th width="30%">그룹 명</th>
										<th width="*">설명</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>						
										<c:when test="${!empty resultList}">
											<c:forEach items="${resultList}" var="resultInfo" varStatus="status">
												<tr>
													<td><input type="checkbox" class="i-checks" name="checkList" value="${resultInfo.id}" /></td>
													<td>
														<c:if test="${sessionScope.requestAuth.authorizationDBRead == '1'}">
															<a href="javascript:fnDetail('${resultInfo.id}');"><c:out value="${resultInfo.groupCode}" /></a>
														</c:if>
														<c:if test="${sessionScope.requestAuth.authorizationDBRead == '0'}">
															<c:out value="${resultInfo.groupCode}" />
														</c:if>
													</td>
													<td><c:out value="${resultInfo.groupName}" /></td>
													<td><c:out value="${resultInfo.description}" /></td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
												<tr>
													<td colspan=4>조회된 데이터가 없습니다.</td>
												</tr>
										</c:otherwise>
									</c:choose>
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
<input type="hidden" id="modal_id" value="" />
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body">
				<div class="row">
					<dl class="dl-horizontal">
						<dt>그룹코드</dt>
						<dd><input type="text" id="modal_groupCode" validateMsg="그룹코드" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>그룹명</dt>
						<dd><input type="text" id="modal_groupName" validateMsg="그룹명" class="form-control input-sm" /></dd>
					</dl>
					<dl class="dl-horizontal">
						<dt>설명</dt>
						<dd><input type="text" id="modal_description" validateMsg="설명" class="form-control input-sm" /></dd>
					</dl>
				</div>
			</div>
			<div class="modal-footer">
			</div>
		</div>
</form>
	</div>
</div>


</body>
</html>
