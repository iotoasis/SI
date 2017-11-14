<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">디바이스 등록 수</h3>
						</div>
						<div class="ibox-content" style="height: 280px;">
							<div class="row">
								<div class="col-lg-7">
									<div class="table-responsive">
										<table class="table no-margins" style="height: 250px;">
											<thead>
												<tr>
													<th>회사명</th>
													<th>모델명</th>
													<th>등록수</th>
												</tr>
											</thead>
											<tbody id="tscr_model_list_body">
												<tr>
													<td>-</td>
													<td>-</td>
													<td>-</td>
												</tr>
												<tr>
													<td>-</td>
													<td>-</td>
													<td>-</td>
												</tr>
												<tr>
													<td>-</td>
													<td>-</td>
													<td>-</td>
												</tr>
												<tr>
													<td>-</td>
													<td>-</td>
													<td>-</td>
												</tr>
												<tr>
													<td>-</td>
													<td>-</td>
													<td>-</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<div class="col-lg-5">
									<!-- 개발자 도구로 보면 canvas가 나오지 않음 -->
									<div class="flot-chart" style="width: 200px; padding-top:30px;">
                                		<div class="flot-chart-pie-content" id="tscr_model_pie_chart"></div>
                            		</div>
								</div>
							</div>
                        </div>
                    </div>