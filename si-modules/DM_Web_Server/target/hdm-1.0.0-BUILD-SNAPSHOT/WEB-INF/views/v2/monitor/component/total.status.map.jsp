<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

					<div class="ibox float-e-margins">
						<!-- <div class="ibox-title">
							<h3 class="no-margins">시스템 현황</h3>
						</div> -->
						<div class="ibox-content" style="height: 320px;">
							<div class="row">
								<div class="col-lg-8">
									<div class="google-map m-t-xs" id="tsm_map" style="height: 280px;"></div>
								</div>
								<div class="col-lg-4">
								<!-- <div class="ibox-content ibox-heading" style="height: 50px;">
									<h3>연결</h3>
								</div> -->
									<h3 class="no margins font-bold">연결</h3>
									<ul class="list-group clear-list m-t-n-sm">
										<li class="list-group-item fist-item">
											<h2 class="no margins font-bold pull-left"><span id="tsm_connected_count">-</span> 개</h2>
										</li>
										<li class="list-group-item fist-item">
											<small class="text-right"> &nbsp; /<span id="tsm_total_count">-</span></small><br />
											<div class="progress progress-small">
												<div style="width: 100%;" class="progress-bar" id="tsm_connected_ratio"></div>
											</div>
										</li>
									</ul>
									
									<div class="row" style="position: relative; bottom: 50px;">
										<div class="col-lg-6">
											<ul class="list-group clear-list m-t" style="list-style-type: none;">
												<li>
													<span class="no-margins pull-right text-right"><span id="tsm_normal_count">-</span> 개</span>
													<span class="no-margins font-bold">NORMAL</span>
												</li>
												<li class="m-t-md">
														<span class="no-margins pull-right text-right"><span id="tsm_minor_count">-</span> 개</span>
														<span class="no-margins font-bold">MINOR</span>
												</li>
												<li class="m-t-md">
													<span class="no-margins pull-right text-right"><span id="tsm_major_count">-</span> 개</span>
													<span class="no-margins font-bold">MAJOR</span>
												</li>
												<li class="m-t-md">
													<span class="no-margins pull-right text-right"><span id="tsm_critical_count">-</span> 개</span>
													<span class="no-margins font-bold">CRITICAL</span>
												</li>
												<li class="m-t-md">
													<span class="no-margins pull-right text-right"><span id="tsm_fatal_count">-</span> 개</span>
													<span class="no-margins font-bold">FATAL</span>
												</li>
											</ul>
										</div>
										<div class="col-lg-6">
											<div class="flot-chart">
		                                		<div class="flot-chart-pie-content" style="position: relative; top: 15px;" id="tsm_count_pie_chart"></div>
		                                	</div>										
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>