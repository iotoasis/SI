<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">필드 모니터링</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-lg-4">
									<button type="button" class="btn btn-default btn-size btn-sm pull-right"><span class="btn-text">삭제</span></button>
									<button type="button" class="btn btn-primary btn-size btn-sm pull-right" style="margin-right: 5px;"><span class="btn-text">추가</span></button>
									<h3 class="no-margins">EMUL_LIGHT_01</h3>
									<br />
									<table class="table table-bordered">
											<thead>
												<tr>
													<th>#</th>
													<th>시리얼 번호</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td>1</td>
													<td>EMULLT0000001</td>
												</tr>
												<tr>
													<td>2</td>
													<td>EMULLT0000001</td>
												</tr>
											</tbody>
										</table>
								</div>
								<div class="col-lg-4">
									<button type="button" class="btn btn-big-size btn-primary pull-right"><span class="btn-f-text">필드 선택</span></button>
									<br>
									<div id="morris-line-chart3" style="height: 150px; position: relative; bottom: 15px;"></div>
								</div>
								<div class="col-lg-4">
									<button type="button" class="btn btn-big-size btn-primary pull-right"><span class="btn-f-text">필드 선택</span></button>
									<br>
									<div id="morris-line-chart4" style="height: 150px; position: relative; bottom: 15px;"></div>
								</div>
							</div>
						</div>
					</div>