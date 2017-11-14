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
    <script type="text/javaScript" language="javascript">

		$(document).ready(function() {
			var form = $('div.form-group:eq(1)')[0];
			$('div.form-group:eq(1)').remove();
			
			initUI();
			
			// 디바이스 아이디 변경시 파일타입 추가/삭제
			$('#search_deviceModelId').on('change',function(){
				
				if($('#search_deviceModelId option:selected').attr("dt") == "TR-069"){
					$('div.form-group:eq(0)').after(form);
				} else {
					$('#fileType').parent().remove();
				}
				
				$('#oui').val($('#search_deviceModelId option:selected').attr("oui"));
				$('#productClass').val($('#search_deviceModelId option:selected').attr("pc"));
				
			});
			
			// 펌웨어 선택창
			$("#firmware_path").on('click',function(){
				$('#packageName').click();
			});
			
			// 펌웨어 이름 설정
			$("#packageName").on('change',function(){
				var fname=document.myForm.packageName.value;
				var arr=("file:///"+fname.replace(/\\/gi,"/")).split("/");
				$("#firmware_path").text(arr[arr.length-1]);
			});
			
			// 업로드
			$("#upload").on('click',function(){
				
				fnUpload();
			});
		});
		
		// upload 완료시
		function uploadFinish(){
			console.log("uploadFinish() has been called!");
			$('select,input,textarea,button').prop('disabled',false);
			$('#upload').val("업로드");
			alert("File has been uploaded!");
		}
		
		// ui 초기화
		function initUI() {
			$("#side-menu_firmware").addClass("active");
			$("#side-menu_firmware ul").addClass("in");
			$("#side-menu_firmware ul li:eq(1)").addClass("active");
		}
		
		// upload
		function fnUpload() {
			var isPass = false;
			isPass = validation();
			
			if(isPass){
				dm_firmware_upload($('#myForm'));
				$('select,input,textarea,button').prop('disabled',true);
				$('#upload').val("업로드 진행중..");
			}
		}
		
		// 유효성 검사
		function validation(){
			if($('[name=deviceModel]').val() == ""){
				alert("디바이스 모델을 선택해 주세요.");
				$('[name=deviceModel]').focus();
				return false;
			} else if($('[name=version]').val() == ""){
				alert("펌웨어 버전을 입력해 주세요.");
				$('[name=version]').focus();
				return false;
			} else if($('[name=packageName]').val() == ""){
				alert("업로드할 펌웨어 파일을 선택해 주세요.");
				return false;
			}
			
			return true;
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
				<h2>펌웨어 업로드</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>펌웨어 관리</li>
					<li class="active"><strong>펌웨어 업로드</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
			<form action="" name="myForm" id="myForm" method="POST" enctype="multipart/form-data">
			
				<div class="row">
					<div class="col-lg-12">
						<div class="ibox float-e-margins">
							<div class="ibox-title">
								<h3 class="no-margins">펌웨어 업로드</h3>
							</div>
							<div class="ibox-content">
							
								<div class="row">
									<div class="col-sm-6">
											
										<div class="form-group">
											<label for="sel1">디바이스 모델:</label>
											<select name="deviceModel" id="search_deviceModelId" class="form-control">
												<option value="">Select a device.</option>
												<c:forEach items="${deviceModelList}" var="deviceModel">
													<option value="${deviceModel.id}"  oui="${deviceModel.oui}" pc="${deviceModel.modelName}" dt="${deviceModel.deviceType}" <c:if test="${deviceModel.id == param.deviceModel}">selected="selected"</c:if> >${deviceModel.manufacturer}-${deviceModel.modelName}</option>
												</c:forEach>
											</select>
										</div>
										
										<div class="form-group">
											<label for="fileType">파일 타입:</label>
											<select name="fileType" id="fileType" class="form-control">
												<option value="1 Firmware Upgrade Image">1. Firmware Upgrade Image</option>
												<option value="2 Web Content">2. Web Content</option>
												<option value="3 Vendor Configuration File">3. Vendor Configuration File</option>
											</select>
										</div>
										
										<div class="form-group">
											<label for="version">버전:</label>
											<input type="text" name="version" class="form-control" id="version">
										</div>
										
										<div class="form-group">
										    <textarea class="form-control" name="description" placeholder="펌웨어 설명"></textarea>
										</div>
										
										<div class="form-group">
											<label for="packageName">펌웨어:</label>
											<input type="file" name="packageName" id="packageName" style="display:none;"/>
											<div id="firmware_path" class="input-sm form-control input-s-sm inline">&nbsp;</div>
										</div>
			                              
										<button type="button" class="btn btn-primary" id="upload">업로드</button>
										
										<input type="hidden" name="oui" id="oui"/>
										<input type="hidden" name="productClass" id="productClass"/>
									</div>
									
								</div>
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
