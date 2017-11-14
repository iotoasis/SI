<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">장애 디바이스</h3>
						</div>
						<div class="ibox-content" style="height: 440px;  overflow: auto;">
							<div class="table-responsive">
								<table class="table table-hover">
									<thead>
										<tr>
											<th>모델</th>
											<th>제조사</th>
											<th>시리얼 번호</th>
											<th>장애 등급</th>
											<th>장애 상세</th>
											<th>상세 보기</th>
										</tr>
									</thead>
									<tbody id="errorDeviceList">
										<!-- <tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><button type="button" class="btn btn-primary btn-xs" style="font-size: 10px;"><i class="fa fa-cogs"></i></button></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><button type="button" class="btn btn-primary btn-xs" style="font-size: 10px;"><i class="fa fa-cogs"></i></button></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><i class="fa fa-cogs"></i></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><button type="button" class="btn btn-primary btn-xs" style="font-size: 10px;"><i class="fa fa-cogs"></i></button></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><button type="button" class="btn btn-primary btn-xs" style="font-size: 10px;"><i class="fa fa-cogs"></i></button></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><i class="fa fa-cogs"></i></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><button type="button" class="btn btn-primary btn-xs" style="font-size: 10px;"><i class="fa fa-cogs"></i></button></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><button type="button" class="btn btn-primary btn-xs" style="font-size: 10px;"><i class="fa fa-cogs"></i></button></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><i class="fa fa-cogs"></i></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><button type="button" class="btn btn-primary btn-xs" style="font-size: 10px;"><i class="fa fa-cogs"></i></button></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><i class="fa fa-cogs"></i></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><button type="button" class="btn btn-primary btn-xs" style="font-size: 10px;"><i class="fa fa-cogs"></i></button></td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td><i class="fa fa-cogs"></i></td>
										</tr> -->
									</tbody>
								</table>
							</div>
						</div>
					</div>