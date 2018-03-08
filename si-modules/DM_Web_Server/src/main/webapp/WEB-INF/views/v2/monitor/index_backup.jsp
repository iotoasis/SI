<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
	
    <title>HDM - 통합 모니터링</title>
</head>

<body class="mini-navbar">

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
        <div class="wrapper wrapper-content animated fadeInRight">
		<!----------------->
		<!-- 컨텐츠 영역 시작 -->
		<!----------------->
		
			<div class="row">
				<div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">시스템 현황</h5>
							<!-- <span class="label label-success pull-right">Monthly</span> -->
						</div>
						<div class="ibox-content">
							<span class="font-bold text-success pull-right text-right">1개</span>
							<span class="no-margins">디바이스 모델</span>
							<br />
							<span class="font-bold text-success pull-right text-right">1231개</span>
							<span class="no-margins">디바이스</span>
							<br />
							<span class="font-bold text-success pull-right text-right">3명</span>
							<span class="no-margins">관리자</span>
						</div>
					</div>
				
				<!-- <div class="col-lg-2"> -->
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<!-- <span class="label label-info pull-right">Annual</span> -->
							<h5 class="no-margins">등록</h5>
						</div>
						<div class="ibox-content">
							<span class="label label-info pull-right">오늘</span>
							<span class="no-margins font-bold" style="font-size: 20px;">213개</span>
							<br />
							<span class="start-percent font-bold text-success pull-right"><i class="fa fa-plus"></i> 12%</span>
							<br />
							<br />
							<span class="label label-info pull-right">어제</span>
							<span class="no-margins">128개</span>
						</div>
					</div>
				</div>
				
				<div class="col-lg-5">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">모델 별 디바이스 수</h5>
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
													<th>총 등록 수</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td>ct2p</td>
													<td>CT2P_GW_01</td>
													<td>3</td>
												</tr>
												<tr>
													<td>Herit</td>
													<td>EMUL_GAS_01</td>
													<td>5</td>
												</tr>
												<tr>
													<td>herit,inc</td>
													<td>HIT_GAS01</td>
													<td>4</td>
												</tr>
												<tr>
													<td>herit,inc</td>
													<td>HIT_GAS01</td>
													<td>4</td>
												</tr>
												<tr>
													<td>herit,inc</td>
													<td>HIT_GAS01</td>
													<td>4</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
								<div class="col-lg-5">
									<div class="flot-chart">
										<div class="flot-chart-pie-content2" style="position: relative; top: 25px; left: -15px;" id="flot-pie-chart2"></div>
									</div>
								</div>
							</div>
                        </div>
                    </div>
				</div>
				<div class="col-lg-5">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<!-- <span class="label label-primary pull-right">Today</span> -->
							<h5 class="no-margins">통계 그래프</h5>
						</div>
						<div class="ibox-content" style="height: 280px;">
							<div id="morris-line-chart" style="height: 240px;"></div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- 실시간 상태 정보 -->
		
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<!-- <div class="ibox-title">
							<h3 class="no-margins">시스템 현황</h3>
						</div> -->
						<div class="ibox-content">
							<div class="row">
								<div class="col-lg-8">
									<div class="google-map m-t-xs" id="map1" style="height: 330px;"></div>
								</div>
								<div class="col-lg-4">
								<!-- <div class="ibox-content ibox-heading" style="height: 50px;">
									<h3>연결</h3>
								</div> -->
									<h3 class="no margins font-bold">연결</h3>
									<ul class="list-group clear-list m-t-n-sm">
										<li class="list-group-item fist-item">
											<h2 class="no margins font-bold pull-left">123개</h2>
										</li>
										<li class="list-group-item fist-item">
											<small class="text-right"> &nbsp; /1000</small><br />
											<div class="progress progress-small" style="position: relative; top: 25px;">
												<div style="width: 43%;" class="progress-bar"></div>
											</div>
										</li>
									</ul>
									
									<div class="row" style="position: relative;">
										<div class="col-lg-6">
											<ul class="list-group clear-list m-t" style="list-style-type: none;">
												<li>
													<span class="no-margins pull-right text-right">112개</span>
													<span class="no-margins font-bold">Nromal</span>
												</li>
												<li class="m-t-md">
														<span class="no-margins pull-right text-right">112개</span>
														<span class="no-margins font-bold">Major</span>
												</li>
												<li class="m-t-md">
													<span class="no-margins pull-right text-right">112개</span>
													<span class="no-margins font-bold">Minor</span>
												</li>
												<li class="m-t-md">
													<span class="no-margins pull-right text-right">112개</span>
													<span class="no-margins font-bold">Critical</span>
												</li>
												<li class="m-t-md">
													<span class="no-margins pull-right text-right">112개</span>
													<span class="no-margins font-bold">Fatal</span>
												</li>
											</ul>
										</div>
										<div class="col-lg-6">
											<div class="flot-chart">
		                                		<div class="flot-chart-pie-content" style="position: relative; top: 15px;" id="flot-pie-chart"></div>
		                                	</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<!-- <div class="ibox-title">
							<h3 class="no-margins">시스템 현황</h3>
						</div> -->
						<div class="ibox-content">
							<div class="row">
								<div class="col-lg-8">
								</div>
								<div class="col-lg-4">
									<h3 class="no margins font-bold">연결</h3>
									<ul class="list-group clear-list m-t-n-sm">
										<li class="list-group-item fist-item">
											<h2 class="no margins font-bold pull-left">123개</h2>
										</li>
										<li class="list-group-item fist-item">
											<small class="text-right"> &nbsp; /1000</small><br />
											<div class="progress progress-small" style="position: relative; top:25px;">
												<div style="width: 43%;" class="progress-bar"></div>
											</div>
										</li>
									</ul>
									
									<div class="row" style="position: relative;">
										<div class="col-lg-6">
											<ul class="list-group clear-list m-t" style="list-style-type: none;">
												<li>
													<span class="no-margins pull-right text-right">112개</span>
													<span class="no-margins font-bold">Nromal</span>
												</li>
												<li class="m-t-md">
														<span class="no-margins pull-right text-right">112개</span>
														<span class="no-margins font-bold">Major</span>
												</li>
												<li class="m-t-md">
													<span class="no-margins pull-right text-right">112개</span>
													<span class="no-margins font-bold">Minor</span>
												</li>
												<li class="m-t-md">
													<span class="no-margins pull-right text-right">112개</span>
													<span class="no-margins font-bold">Critical</span>
												</li>
												<li class="m-t-md">
													<span class="no-margins pull-right text-right">112개</span>
													<span class="no-margins font-bold">Fatal</span>
												</li>
											</ul>
										</div>
										<div class="col-lg-6">
											<div class="flot-chart">
												<div class="flot-chart-pie-content" style="position: relative; top: 15px;" id="flot-pie-chart3"></div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- 장애 통계 -->
			<div class="row">
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">장애 통계</h5>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-lg-6" style="line-height: 40px;">
									<span class="start-percent font-bold text-danger pull-right"><i class="fa fa-level-down"></i> 60%</span>
									<span class="no-margins">오늘 : <span>1개</span></span>
									<br />
									<span class="start-percent font-bold text-danger pull-right"><i class="fa fa-level-down"></i> 30%</span>
									<span class="no-margins">어제 : <span>2개</span></span>
									<br />
									<span class="no-margins">1개월 평균 : <span>3개</span></span>
								</div>
								<div class="col-lg-6" style="line-height: 40px;">
									<span class="start-percent font-bold text-danger pull-right"><i class="fa fa-level-down"></i> 60%</span>
									<span class="no-margins">이번달 : <span>90개</span></span>
									<br />
									<span class="start-percent font-bold text-danger pull-right"><i class="fa fa-level-down"></i> 30%</span>
									<span class="no-margins">저번달 : <span>100개</span></span>
									<br />
									<span class="no-margins">1년 평균 : <span>150개</span></span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<!-- <span class="label label-primary pull-right">Today</span> -->
							<h5 class="no-margins">통계 그래프</h5>
						</div>
						<div class="ibox-content" style="height: 155px;">
							<div id="morris-line-chart2" style="height: 160px; position: relative; bottom: 20px;"></div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">장애 디바이스</h5>
						</div>
						<div class="ibox-content" style="height: 450px;">
							<div class="table-responsive">
								<table class="table table_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
									<thead>
										<tr>
											<th>모델</th>
											<th>제조사</th>
											<th>시리얼 번호</th>
											<th>장애 등급</th>
											<th>장애 상세</th>
											<th>진단 버튼</th>
										</tr>
									</thead>
								</table>
							</div>
							<div class="table-responsive" style="height: 380px; overflow-x: hidden; overflow-y: auto;">
								<table class="table table-hover table_thead table-condensed" style="width: 100%; height: 380px;">
									<tbody>
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
										</tr>
									</tbody>
								</table> 
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">오류 메시지</h5>
						</div>
						<div class="ibox-content" style="height: 450px;">
							<div class="table-responsive">
								<table class="table table_errorthead table-condensed" style="width: 100%; padding:0; margin: 0;">
									<thead>
										<tr>
											<th>모델</th>
											<th>제조사</th>
											<th>시리얼 번호</th>
											<th>장애 등급</th>
											<th>상세 메시지</th>
											<th>발생 시간</th>
										</tr>
									</thead>								
								</table>
							</div>
							<div class="table-responsive" style="height: 380px; overflow-x: hidden; overflow-y: auto;">
								<table class="table table_errorthead table-hover table-condensed" style="width: 100%; height: 380px;">
									<tbody>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
										<tr>
											<td>EMUL_LIGHT_01</td>
											<td>Herit</td>
											<td>EMULLT0000001</td>
											<td>MAJOR</td>
											<td>3</td>
											<td>15-02-13 15:47:05</td>
										</tr>
									</tbody>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<!-- 디바이스 모니터링 -->
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">디바이스 Alarm/Notification</h5>
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
								<div class="col-lg-8">
									<div class="table-responsive">
										<table class="table">
											<thead>
												<tr>
													<th>모델</th>
													<th>제조사</th>
													<th>시리얼 번호</th>
													<th>Notification 내용</th>
													<th>시간</th>
												</tr>
											</thead>
											<tbody>
												<tr>
													<td>EMUL_LIGHT_01</td>
													<td>Herit</td>
													<td>EMULLT0000001</td>
													<td>온도 초과 (60도)</td>
													<td>15-02-13 15:47:05</td>
												</tr>
												<tr>
													<td>EMUL_LIGHT_01</td>
													<td>Herit</td>
													<td>EMULLT0000001</td>
													<td>온도 초과 (55도)</td>
													<td>15-02-13 15:47:05</td>
												</tr>
												<tr>
													<td>EMUL_LIGHT_01</td>
													<td>Herit</td>
													<td>EMULLT0000001</td>
													<td>배터리 부족 (25%)</td>
													<td>15-02-13 15:47:05</td>
												</tr>
											</tbody>
										</table>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">필드 모니터링</h5>
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
				</div>
			</div>
			
			<!-- 디바이스 모니터링 (//box align)-->
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<button type="button" class="btn btn-position btn-a-big-size btn-primary pull-right"><span class="btn-f-text">디바이스 선택</span></button>
							<button type="button" class="btn btn-position btn-big-size btn-primary pull-right" style="margin-right: 5px;"><span class="btn-f-text">필드 편집</span></button>
							<h5 class="no-margins">디바이스 모니터링</h5>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-lg-3">
									<div class="ibox-heading-info">
										<small style="margin-right: 50px;">LG STB_001</small>
										<small>231-3421-43234</small>
										<button type="button" class="btn btn-default btn-small-size pull-right"><i class="fa fa-cog btn-i-text"></i></button>
									</div>
									<div class="ibox-content content-border">
										<div class="row">
											<div class="col-lg-5">
												<button type="button" class="btn btn-outline btn-warning">Normal</button>
											</div>
											<div class="col-lg-7">
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-box-size">
															<div class="col-xs-5">
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
															</div>
															<div class="col-xs-5 content">
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
															</div>
														</div>	
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-3">
									<div class="ibox-heading-info">
										<small style="margin-right: 60px;">LG STB_001</small>
										<small>231-3421-43234</small>
										<button type="button" class="btn btn-default btn-small-size pull-right" style="margin-right: 5px;"><i class="fa fa-cog btn-i-text"></i></button>
									</div>
									<div class="ibox-content content-border">
										<div class="row">
											<div class="col-lg-5">
												<button type="button" class="btn btn-outline btn-warning">Normal</button>
												<br />
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-upbox-size">
															<div class="col-xs-6">
																<small>타입</small><br>
																<small>코드</small>
															</div>
															<div class="col-xs-6 content">
																<small>xxx</small><br>
																<small>A01</small>
															</div>
														</div>	
													</div>
												</div>
											</div>
											<div class="col-lg-7">
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-box-size">
															<div class="col-xs-5">
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
															</div>
															<div class="col-xs-5 content">
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
															</div>
														</div>	
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								
								<div class="col-lg-3">
									<div class="ibox-heading-info">
										<small style="margin-right: 50px;">LG STB_001</small>
										<small>231-3421-43234</small>
										<button type="button" class="btn btn-default btn-small-size pull-right" style="margin-right: 5px;"><i class="fa fa-cog btn-i-text"></i></button>
									</div>
									<div class="ibox-content content-border">
										<div class="row">
											<div class="col-lg-5">
												<button type="button" class="btn btn-outline btn-warning">Normal</button>
											</div>
											<div class="col-lg-7">
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-box-size">
															<div class="col-xs-5">
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
															</div>
															<div class="col-xs-5 content">
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
															</div>
														</div>	
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								
								<div class="col-lg-3">
									<div class="ibox-heading-info">
										<small style="margin-right: 50px;">LG STB_001</small>
										<small>231-3421-43234</small>
										<button type="button" class="btn btn-default btn-small-size pull-right" style="margin-right: 5px;"><i class="fa fa-cog btn-i-text"></i></button>
									</div>
									<div class="ibox-content content-border">
										<div class="row">
											<div class="col-lg-5">
												<button type="button" class="btn btn-outline btn-warning">Normal</button>
											</div>
											<div class="col-lg-7">
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-box-size">
															<div class="col-xs-5">
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
															</div>
															<div class="col-xs-5 content">
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
															</div>
														</div>	
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
							
							<br><!-- 두번째 줄 -->
							<div class="row">
								<div class="col-lg-3">
									<div class="ibox-heading-info">
										<small style="margin-right: 50px;">LG STB_001</small>
										<small>231-3421-43234</small>
										<button type="button" class="btn btn-default btn-small-size pull-right" style="margin-right: 5px;"><i class="fa fa-cog btn-i-text"></i></button>
									</div>
									<div class="ibox-content content-border">
										<div class="row">
											<div class="col-lg-5">
												<button type="button" class="btn btn-outline btn-warning">Normal</button>
											</div>
											<div class="col-lg-7">
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-box-size">
															<div class="col-xs-5">
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
															</div>
															<div class="col-xs-5 content">
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
															</div>
														</div>	
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								
								<div class="col-lg-3">
									<div class="ibox-heading-info">
										<small style="margin-right: 50px;">LG STB_001</small>
										<small>231-3421-43234</small>
										<button type="button" class="btn btn-default btn-small-size pull-right" style="margin-right: 5px;"><i class="fa fa-cog btn-i-text"></i></button>
									</div>
									<div class="ibox-content content-border">
										<div class="row">
											<div class="col-lg-5">
												<button type="button" class="btn btn-outline btn-warning">Normal</button>
												<br />
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-upbox-size">
															<div class="col-xs-6">
																<small>타입</small>
																<small>코드</small>
															</div>
															<div class="col-xs-6 content">
																<small>xxx</small>
																<small>A01</small>
															</div>
														</div>	
													</div>
												</div>
											</div>
											<div class="col-lg-7">
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-box-size">
															<div class="col-xs-5">
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
															</div>
															<div class="col-xs-5 content">
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
															</div>
														</div>	
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								
								<div class="col-lg-3">
									<div class="ibox-heading-info">
										<small style="margin-right: 50px;">LG STB_001</small>
										<small>231-3421-43234</small>
										<button type="button" class="btn btn-default btn-small-size pull-right" style="margin-right: 5px;"><i class="fa fa-cog btn-i-text"></i></button>
									</div>
									<div class="ibox-content content-border">
										<div class="row">
											<div class="col-lg-5">
												<button type="button" class="btn btn-outline btn-warning">Normal</button>
											</div>
											<div class="col-lg-7">
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-box-size">
															<div class="col-xs-5">
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
															</div>
															<div class="col-xs-5 content">
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
															</div>
														</div>	
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
								
								<div class="col-lg-3">
									<div class="ibox-heading-info">
										<small style="margin-right: 50px;">LG STB_001</small>
										<small>231-3421-43234</small>
										<button type="button" class="btn btn-default btn-small-size pull-right" style="margin-right: 5px;"><i class="fa fa-cog btn-i-text"></i></button>
									</div>
									<div class="ibox-content content-border">
										<div class="row">
											<div class="col-lg-5">
												<button type="button" class="btn btn-outline btn-warning">Normal</button>
											</div>
											<div class="col-lg-7">
												<div class="inspinia-timeline">
													<div class="timeline-item">
														<div class="row timeline-box-size">
															<div class="col-xs-5">
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
																<span>온도</span>
															</div>
															<div class="col-xs-5 content">
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
																<span>30도</span>
															</div>
														</div>	
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		<!----------------->
		<!--  컨텐츠 영역 끝  -->
		<!----------------->
			 
        </div>
        <div class="footer">
        
        	<%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
        
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/views/v2/common/common_js.jsp"%>

<!-- Morris -->
<script language="javascript" src="/hdm/v2/js/plugins/morris/raphael-2.1.0.min.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/morris/morris.js"></script>

<!-- Flot -->
<script language="javascript" src="/hdm/v2/js/plugins/flot/jquery.flot.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/flot/jquery.flot.pie.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/flot/jquery.flot.resize.js"></script>

<!-- ChartJS-->
<script language="javascript" src="/hdm/v2/js/plugins/chartJs/Chart.min.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/chartJs/chartjs-demo.js"></script>

<!-- Custom and plugin javascript -->
<script language="javascript" src="/hdm/v2/js/inspinia.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/pace/pace.min.js"></script>

<!-- Morris demo data-->
<script language="javascript" src="/hdm/v2/js/plugins/morris/morris-demo-2.js"></script>

<!-- Flot demo data -->
<script language="javascript" src="/hdm/v2/js/plugins/flot/flot-demo-2.js"></script>

<!-- Map -->
<script language="javascript" type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDQTpXj82d8UpCi97wzo_nKXL7nYrd4G70"></script>
<script language="javascript" src="/hdm/v2/js/plugins/map/map.js"></script>

</body>

</html>
