<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">오류이력</h3>
						</div>
						<div class="ibox-content">
							<div class="table-responsive">
								<table class="table table_error_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
									<thead>
										<tr>
											<th>시간</th>
											<th>에러코드</th>
											<th>장애등급</th>
											<th>에러데이터</th>
										</tr>
									</thead>
								</table>
							</div>
							<div class="table-responsive" style="height: 380px; overflow-x: hidden; overflow-y: auto;">
								<table class="table table-hover table_error_thead table-condensed" style="width: 100%;">
									<tbody id="deviceErrorHistoryList">
										<!-- <tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr> -->
									</tbody>
								</table> 
							</div>
						</div>
					</div>