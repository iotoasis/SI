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
			$("#side-menu_oneM2M").addClass("active");
			$("#side-menu_oneM2M ul").addClass("in");
			$("#side-menu_oneM2M ul li:eq(1)").addClass("active");
			
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
		
		function fnInsert() {
			this.location = "<c:url value="/onem2m/platform/new.do" />";
		}
		
		function fnDetail(id) {
			var myForm					= document.getElementById("myForm");
			myForm.id.value				= id;
			myForm.action				= "<c:url value='/onem2m/platform/info.do'/>";
		   	myForm.submit();
		} 
		
		function fnCheckDelete() {
			if (checkOneMoreCheckbox(document.myForm, "checkList")) {
				if (confirm("삭제하시겠습니까?")) {
					var checkList = [];
					$("input[name='checkList']:checkbox").each(function() {
						if (this.checked)
							checkList.push(this.value);
					});
					
					$.ajax({
						type: "POST",
						url: "<c:url value="/onem2m/platform/delete.do" />",
						data: {checkList : checkList.toString()},
						dataType: "json",
						success: function(response){
							if (response.result) {
								alert("플랫폼 연동을 삭제하였습니다");
								window.location.href = "<c:url value="/onem2m/platform/list.do" />";
							}
							else {
								alert(response.message);
							}
						},
						error: function(e){
							alert("플랫폼 연동 삭제중 에러가 발생하였습니다!");
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
			<div class="col-sm-12">
				<h2>플랫폼 연동 관리</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>oneM2M 서버 관리</li>
					<li class="active"><strong>플랫폼 연동 관리</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
<form id="myForm" name="myForm" method="post">
<input type="hidden" name="id" value="" />
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">플랫폼 연동 관리</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-12">
									<span class="input-group-btn text-right">
										<button type="button" onclick="fnInsert(); return false;" class="btn btn-sm btn-success"> 연동 등록</button> 
										<button type="button" onclick="fnCheckDelete(); return false;" class="btn btn-sm btn-danger" style="margin-left:2px;"> 연동 삭제</button>
									</span>
								</div>
							</div>
						
							<div>&nbsp;</div>

							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th width="5%"><input type="checkbox" class="i-checks" name="allChk" ></th>
										<th width="20%">서버명</th>
										<th width="15%">SP ID</th>
										<th width="20">CSEBASE URI</th>
										<th width="20%">등록일자</th>
										<th width="20%">수정일자</th>
									</tr>
								</thead>
								<tbody>
									<c:choose>						
										<c:when test="${!empty resultList}">
											<c:forEach items="${resultList}" var="resultInfo" varStatus="status">
												<tr>
													<td><input type="checkbox" class="i-checks" name="checkList" value="<c:out value="${resultInfo.id}" />" /></td>
													<td><c:out value="${resultInfo.serverName}" /></td>
													<td><a href="javascript:fnDetail('<c:out value="${resultInfo.id}" />');"><c:out value="${resultInfo.spId}" /></a></td>
													<td><c:out value="${resultInfo.cseName}" /></td>
													<td><c:out value="${resultInfo.createTime}" /></td>
													<td><c:out value="${resultInfo.updateTime}" /></td>
												</tr>
											</c:forEach>
										</c:when>
										<c:otherwise>
												<tr>
													<td colspan=6>조회된 데이터가 없습니다.</td>
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

</body>
</html>
