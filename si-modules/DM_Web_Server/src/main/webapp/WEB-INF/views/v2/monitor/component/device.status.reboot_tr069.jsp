<%@page pageEncoding="UTF-8"%>
<%@ page import="net.herit.common.util.PagingUtil" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="ibox float-e-margins">
	<div class="ibox-title">
		<h3 class="no-margins">제어</h3>
	</div>
	<div class="ibox-content">
		<button type="button" class="btn btn-primary btn-re-size btn_reboot"><span class="btn-re-text btn-disabled">Reboot</span></button><br>
		<button type="button" class="btn btn-primary btn-re-size btn_firmware_update"><span class="btn-re-text btn-disabled">Firmware Update</span></button>
	</div>
</div>
<!-- 
<button class="btn btn-default" data-target="#layerpop" data-toggle="modal">모달출력버튼</button><br/>
 -->
<div class="modal fade" id="layerpop" >
	<div class="modal-dialog">
		<div class="modal-content">
			<!-- header -->
			<div class="modal-header">
				<!-- 닫기(x) 버튼 -->
				<button type="button" class="close" data-dismiss="modal">×</button>
				<!-- header title -->
				<h4 class="modal-title">펌웨어 목록</h4>
			</div>
			<!-- body -->
			<div class="modal-body">
				<form action="" name="myForm" id="myForm" method="POST">
					<input type="hidden" name="page" value="1" />
					<input type="hidden" name="currPage" value="1" />
					<div class="row">
						<div class="col-lg-12">
							<table class="table table-bordered table-hover">
								<thead>
									<tr>
										<th>제조사</th>
										<th>패키지명</th>
										<th>최신버전</th>
										<th>생성일시</th>
										<th>등록갱신일시</th>
									</tr>
								</thead>
								<tbody>
										
								</tbody>
							</table>
						</div>
					</div>
				</form>
			</div>
			<!-- Footer -->
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">취소</button>
			</div>
		</div>
	</div>
</div>