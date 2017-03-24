<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">제어이력</h3>
						</div>
						<div class="ibox-content">
							<div class="table-responsive">
								<table class="table table_cont_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
									<thead>
										<tr>
											<th>시간</th>
											<th>리소스명</th>
											<th>제어데이터</th>
											<th>결과</th>
											<th>오류</th>
										</tr>
									</thead>
								</table>
							</div>
							<div class="table-responsive" style="height: 380px; overflow-x: hidden; overflow-y: auto;">
								<table class="table table-hover table_cont_thead table-condensed" style="width: 100%;">
									<tbody id="deviceControlHistoryList">
										<!-- <tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>MAJOR</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>SERVICE CONF</td>
											<td>url=http://10.101.101.107:8000/</td>
											<td>200</td>
											<td>200</td>
										</tr> -->
									</tbody>
								</table> 
							</div>
						</div>
					</div>