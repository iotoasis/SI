<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>

    <title>HDM - 디바이스 상세 모니터링</title>
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
				<div class="col-lg-3">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">모델정보</h5>
						</div>
						<div class="ibox-content" style="height: 135px;">
							<div class="row">
								<div class="col-lg-6">
									<img alt="image" class="img-responsive" src="${pageContext.request.contextPath}/images/hitdm/model/gw_01.jpg" style="width: 130px; height: 110px;"/>
								</div>
								<div class="col-lg-6" style="margin-top: 5px; border:line-height: 20px;">
									<div>제조사</div>
									<div class="div-text-indent font-bold">Herit.inc.</div>
									<div class="div-text-indent font-bold">(900000)</div>
									<div>모델번호</div>
									<div class="div-text-indent font-bold">HIT_GW_01</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">디바이스 정보</h5>
							<span class="pull-right">등록 : 2015.02.10</span>
						</div>
						<div class="ibox-content" style="height: 135px;">
							<div class="row">
								<span class="small-title">SN</span>
								<h2 class="no-margins font-bold">HG_00001_23423</h2>
								<br />
								<span class="small-title">FW</span>
								<h2 class="no-margins font-bold" style="float: left;">HERIT.GW</h2>
								<span class="span-fw">V0.1.32.3432</span>
								
								<!-- <span class="small-date">등록</span>
								<div class="div-date">2015.02.10</div> -->
							</div>
							<div class="row">
								<span style="margin-left: 45px;">2015-02-11 23:34:23</span>
								<button type="button" class="btn btn-primary btn-smalls-size"><small class="btn-smalls-text">버전업</small></button>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">상태</h5>
						</div>
						<div class="ibox-content" style="height: 135px;">
							<i class="fa fa-circle i-circle"></i>
							<h2 class="font-bold" style="position: relative; top: -5px;">MAJOR</h2>
							<div class="font-bold" style="font-size: 14px;">GPS Module Failure</div>
							<br />
							<small>2015-02-11 23:34:23</small>
							<button type="button" class="btn btn-primary btn-small-size"><small class="btn-small-text">진단</small></button>
						</div>
					</div>
				</div>
				<!-- <div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">펌웨어</h5>
						</div>
						<div class="ibox-content" style="height: 135px;">
							<h2 class="font-bold" style="position: relative; top: -5px;">HERIT.GW</h2>
							<div style="font-size: 14px; margin-top: -13px;">V0.1.32.3432</div>
							<br />
							
							<small>2015-02-11 23:34:23</small><br>
							<button type="button" class="btn btn-primary btn-smalls-size"><small class="btn-smalls-text">upgrade</small></button>
						</div>
					</div>
				</div> -->
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">TODAY</h5>
						</div>
						<div class="ibox-content" style="height: 135px;">
							<div class="row">
								<div class="col-lg-4 font-bold" style="font-size: 13px;">알람
									<span class="pull-right start-percent font-bold text-navy"><i class="fa fa-level-up"></i> 23%</span>
								</div>
								<div class="col-lg-4 font-bold" style="font-size: 13px;">노티
									<div class="pull-right start-percent font-bold text-info"><i class="fa fa-level-up"></i> 23%</div>
								</div>
								<div class="col-lg-4 font-bold" style="font-size: 13px;">에러
									<div class="pull-right start-percent font-bold text-warning"><i class="fa fa-level-up"></i> 23%</div>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-4">
									<div class="widget style1 navy-bg" style="height: 80px;">
										<!-- <div class="row">
											<div class="pull-right start-percent font-bold text-success">23%</div>
										</div> -->
										<div class="row vertical-align">
											<div class="col-xs-3">
												<i class="fa fa-user fa-2x"></i>
											</div>
											<div class="col-xs-9 text-right">
												<h2 class="font-bold" style="font-size: 25px;">217</h2>	
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-4">
									<div class="widget style1 lazur-bg" style="height: 80px;">
										<div class="row vertical-align">
											<div class="col-xs-3">
												<i class="fa fa-euro fa-2x"></i>
											</div>
											<div class="col-xs-9 text-right">
												<h2 class="font-bold" style="font-size: 25px;">462</h2>
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-4">
									<div class="widget style1 yellow-bg" style="height: 80px;">
										<div class="row vertical-align">
											<div class="col-xs-3">
												<i class="fa fa-shield fa-2x"></i>
											</div>
											<div class="col-xs-9 text-right">
												<h2 class="font-bold" style="font-size: 25px;">610</h2>
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
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-content" style="height: 400px;">
							<div class="google-map m-t-xs" id="map1" style="height: 360px;"></div>
						</div>
					</div>
				</div>
				<div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">온도</h5>
						</div>
						<div class="ibox-content">
							<div class="pull-right">
								<span class="bar">5,3,9,6,5,9,7,3,5,2</span>
							</div>
							<br />
							<h1 class="font-bold" style="margin-left: 15px; margin-right: 15px; float: left;">22</h1> 
							<h2 style="margin-top: 10px;">도</h2>
						</div>
					</div>
				</div>
				<div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">습도</h5>
						</div>
						<div class="ibox-content">
							<div class="pull-right">
								<span class="bar">5,3,9,6,5,9,7,3,5,2</span>
							</div>
							<br />
							<h1 class="font-bold" style="margin-left: 15px; margin-right: 15px; float: left;">45</h1> 
							<h2 style="margin-top: 10px;">%</h2>
						</div>
					</div>
				</div>
				<div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">스위치</h5>
						</div>
						<div class="ibox-content" style="height: 100px;">
							<br />	
							<h1 class="font-bold" style="margin-left: 15px; margin-right: 15px; float: left;">ON</h1>
							<input type="checkbox" class="js-switch" checked/>
						</div>
					</div>
				</div>
				<div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">제어</h5>
						</div>
						<div class="ibox-content">
							<button type="button" class="btn btn-primary btn-re-size"><span class="btn-re-text">Reboot</span></button><br>
							<button type="button" class="btn btn-primary btn-re-size"><span class="btn-re-text">Factory Reset</span></button>
						</div>
					</div>
				</div>
			<!-- </div>
			
			<div class="row"> -->
				
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">네트워크</h5>
						</div>
						<div class="ibox-content" style="height: 180px;">
							<div class="row">
								<div class="col-lg-3">
									<div style="font-size: 15px; margin-left: 15px;">TX</div>
									<h1 class="font-bold" style="margin-left: 15px; margin-right: 15px; float: left;">21</h1> 
									<h2 style="margin-top: 10px;">Kb</h2>
									<br />
									<div style="font-size: 15px; margin-left: 15px;">RX</div>
									<h1 class="font-bold" style="margin-left: 15px; margin-right: 15px; float: left;">243</h1> 
									<h2 style="margin-top: 10px;">Kb</h2>
								</div>
								<div class="col-lg-9">
									<div id="morris-line-chart" style="height: 155px;"></div>
								</div>
							</div>
						</div>
					</div>
				</div>
				</div>
				<div class="row">
				<div class="col-lg-2 col-md-offset-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">로그 수집</h5>
							<button type="button" class="btn btn-primary btn-log-size pull-right"><span class="btn-log-text">보기</span></button><br>
						</div>
						<div class="ibox-content" style="height: 128px;">
							<h1 class="font-bold" style="margin-left: 15px; margin-right: 15px; float: left;">ON</h1>
							<input type="checkbox" class="js-switch2" checked/>
							<br />
							<br />
							<span style="margin-left: 15px;">시작 : 23:34:23</span><br />
							<span style="margin-left: 15px;">종료 : 23:34:23</span>
						</div>
					</div>
				</div>

				<div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">다중옵션 항목</h5>
						</div>
						<div class="ibox-content" style="height: 128px;">
							<br />
							<div class="btn-group" style="margin-top: 10px;">
								<button class="btn btn-white" type="button" style="width: 40px;"><span style="position: relative; left: -5px; font-size: 13px;">Left</span></button>
								<button class="btn btn-primary" type="button" style="width: 60px;"><span style="position: relative; left: -5px;">Middle</span></button>
								<button class="btn btn-white" type="button" style="width: 40px;"><span style="position: relative; left: -9px; font-size: 13px;">Right</span></button>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">다중옵션 항목</h5>
						</div>
						<div class="ibox-content" style="height: 128px;">
							<br />
							<div class="btn-group" style="margin-top: 10px; margin-left: 30px;">
                            <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle">Action <span class="caret"></span></button>
                            <ul class="dropdown-menu">
                                <li><a href="#">Action</a></li>
                                <li><a href="#" class="font-bold">Another action</a></li>
                                <li><a href="#">Something else here</a></li>
                                <li class="divider"></li>
                                <li><a href="#">Separated link</a></li>
                            </ul>
                        </div>
						</div>
					</div>
				</div>
				<div class="col-lg-2">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">배터리</h5>
						</div>
						<div class="ibox-content" style="height: 128px;">
							<div class="pull-right">
								<span class="pie">226,134</span>
							</div>
							<br />
							<h1 class="font-bold" style="margin-left: 15px; margin-right: 15px; float: left;">45</h1> 
							<h2 style="margin-top: 10px;">%</h2>
						</div>
					</div>
				</div>
			</div>
				
			<div class="row">
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">오류이력</h5>
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
								<table class="table table-hover table_error_thead table-condensed" style="width: 100%; height: 380px;">
									<tbody>
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
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>1</td>
											<td>MAJOR</td>
											<td>1</td>
										</tr>
									</tbody>
								</table> 
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">알람이력</h5>
						</div>
						<div class="ibox-content">
							<div class="table-responsive">
								<table class="table table_alarm_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
									<thead>
										<tr>
											<th>시간</th>
											<th>알람이름</th>
											<th>알람데이터</th>
										</tr>
									</thead>
								</table>
							</div>
							<div class="table-responsive" style="height: 380px; overflow-x: hidden; overflow-y: auto;">
								<table class="table table-hover table_alarm_thead table-condensed" style="width: 100%; height: 380px;">
									<tbody>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
										<tr>
											<td>15-02-13 15:47:05</td>
											<td>alarm_name</td>
											<td>1</td>
										</tr>
									</tbody>
								</table> 
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">제어이력</h5>
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
								<table class="table table-hover table_cont_thead table-condensed" style="width: 100%; height: 380px;">
									<tbody>
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
										</tr>
									</tbody>
								</table> 
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-3">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">하드웨어 정보</h5>
						</div>
						<div class="ibox-content">
							<div class="table-responsive">
								<table class="table table_info_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
									<thead>
										<tr>
											<th>#</th>
											<th>이름</th>
											<th>데이터</th>
										</tr>
									</thead>
								</table>
							</div>
							<div class="table-responsive" style="height: 380px; overflow-x: hidden; overflow-y: auto;">
								<table class="table table-hover table_info_thead table-condensed" style="width: 100%; height: 380px;">
									<tbody>
										<tr>
											<td>1</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>2</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>3</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>4</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>5</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>6</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>7</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>8</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>9</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>10</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>11</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>12</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
									</tbody>
								</table> 
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">네트워크 정보</h5>
						</div>
						<div class="ibox-content">
							<div class="table-responsive">
								<table class="table table_info_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
									<thead>
										<tr>
											<th>#</th>
											<th>이름</th>
											<th>데이터</th>
										</tr>
									</thead>
								</table>
							</div>
							<div class="table-responsive" style="height: 380px; overflow-x: hidden; overflow-y: auto;">
								<table class="table table-hover table_info_thead table-condensed" style="width: 100%; height: 380px;">
									<tbody>
										<tr>
											<td>1</td>
											<td>hard_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>2</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>3</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>4</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>5</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>6</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>7</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>8</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>9</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>10</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>11</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>12</td>
											<td>net_info</td>
											<td>1</td>
										</tr>
									</tbody>
								</table> 
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">서비스 정보</h5>
						</div>
						<div class="ibox-content">
							<div class="table-responsive">
								<table class="table table_info_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
									<thead>
										<tr>
											<th>#</th>
											<th>이름</th>
											<th>데이터</th>
										</tr>
									</thead>
								</table>
							</div>
							<div class="table-responsive" style="height: 380px; overflow-x: hidden; overflow-y: auto;">
								<table class="table table-hover table_info_thead table-condensed" style="width: 100%; height: 380px;">
									<tbody>
										<tr>
											<td>1</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>2</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>3</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>4</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>5</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>6</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>7</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>8</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>9</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>10</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>11</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>12</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
									</tbody>
								</table> 
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-3">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="no-margins">서비스 정보</h5>
						</div>
						<div class="ibox-content">
							<div class="table-responsive">
								<table class="table table_info_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
									<thead>
										<tr>
											<th>#</th>
											<th>이름</th>
											<th>데이터</th>
										</tr>
									</thead>
								</table>
							</div>
							<div class="table-responsive" style="height: 380px; overflow-x: hidden; overflow-y: auto;">
								<table class="table table-hover table_info_thead table-condensed" style="width: 100%; height: 380px;">
									<tbody>
										<tr>
											<td>1</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>2</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>3</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>4</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>5</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>6</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>7</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>8</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>9</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>10</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>11</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
										<tr>
											<td>12</td>
											<td>service_info</td>
											<td>1</td>
										</tr>
									</tbody>
								</table> 
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-content">
							<div class="row">
								<div class="col-lg-3">
									<div class="dd" id="nestable2">
										<ol class="dd-list">
											<li class="dd-item" data-id="1">
												<div class="dd-handle">
													<span class="pull-right">
														<a href="#" class="check-link"><i class="fa fa-check-square"></i></a>
													</span>
													<img alt="image" class="img-responsive" src="${pageContext.request.contextPath}/images/hitdm/model/gw_01.jpg" style="width: 40px; height: 30px; float: left; margin-right: 10px; margin-top: -5px;"/> AP_SN_0001
												</div>
												<ol class="dd-list">
													<li class="dd-item" data-id="2">
		                                                <div class="dd-handle">
		                                                    <!-- <span class="pull-right">
		                                                    	<a href="#" class="check-link"><i class="fa fa-check-square"></i></a>
		                                                    </span> -->
		                                                    <img alt="image" class="img-responsive" src="${pageContext.request.contextPath}/images/hitdm/model/gas_01.jpg" style="width: 40px; height: 30px; float: left; margin-right: 10px; margin-top: -5px;"/> AP_SN_0001
		                                                </div>
                                            		</li>
		                                            <li class="dd-item" data-id="3">
		                                                <div class="dd-handle">
		                                                    <!-- <span class="pull-right">
		                                                    	<a href="#" class="check-link"><i class="fa fa-check-square"></i></a>
		                                                    </span> -->
		                                                    <img alt="image" class="img-responsive" src="${pageContext.request.contextPath}/images/hitdm/model/gas_01.jpg" style="width: 40px; height: 30px; float: left; margin-right: 10px; margin-top: -5px;"/> AP_SN_0001
		                                                </div>
		                                            </li>
												</ol>
											</li>
										</ol>
									</div>
								</div>
								<div class="col-lg-9">
									<div class="ibox-content">
										<div class="row">
											<div class="col-lg-4">
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
											
											<div class="col-lg-4">
												<div class="ibox-heading-info">
												
													<small style="margin-right: 60px;">LG STB_001</small>
													<small>231-3421-43234</small>
													<button type="button" class="btn btn-defa
													ult btn-small-size pull-right" style="margin-right: 5px;"><i class="fa fa-cog btn-i-text"></i></button>
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
											
											<div class="col-lg-4">
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
								
										</div>									
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			
			<div class="row">
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins" style="float: left;">오류</h3>
							<button type="button" class="btn btn-primary btn-ae-size pull-right"><span class="btn-ae-text">상세통계보기</span></button>
						</div>
						<div class="ibox-content">
							<div class="flot-chart">
                                <div class="flot-chart-content" id="flot-line-chart-multi"></div>
                            </div>
                            <br />
                            <div class="row">
                            	<div class="col-md-4">일평균 오류수</div>
                            	<div class="col-md-4">일평균 장애시간</div>
                            	<div class="col-md-4">일평균 OFF시간</div>
                            </div>
						</div>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins" style="float: left;">알람</h3>
							<button type="button" class="btn btn-primary btn-ae-size pull-right"><span class="btn-ae-text">상세통계보기</span></button>
						</div>
						<div class="ibox-content">
							<div class="flot-chart">
                                <div class="flot-chart-content" id="flot-line-chart-multi2"></div>
                            </div>
                            <br />
                            <div class="row">
                            	<div class="col-md-4">일평균 A알람수</div>
                            	<div class="col-md-4">일평균 B알람수</div>
                            	<div class="col-md-4">일평균 C알람수</div>
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

<!-- Nestable List -->
<script language="javascript" src="/hdm/v2/js/plugins/nestable/jquery.nestable.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/nestable/nestable.js"></script>

<!-- Morris -->
<script language="javascript" src="/hdm/v2/js/plugins/morris/raphael-2.1.0.min.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/morris/morris.js"></script>

<!-- ChartJS-->
<script language="javascript" src="/hdm/v2/js/plugins/chartJs/Chart.min.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/chartJs/chartjs-demo.js"></script>

<!-- Flot -->
<script language="javascript" src="/hdm/v2/js/plugins/flot/jquery.flot.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/flot/jquery.flot.pie.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/flot/jquery.flot.resize.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/flot/jquery.flot.time.js"></script>

<!-- Peity -->
<script language="javascript" src="/hdm/v2/js/plugins/peity/jquery.peity.min.js"></script>

<!-- Custom and plugin javascript -->
<script language="javascript" src="/hdm/v2/js/inspinia.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/pace/pace.min.js"></script>

<!-- Flot demo data -->
<script language="javascript" src="/hdm/v2/js/plugins/flot/flot-demo-1.js"></script>

<!-- Peity demo data -->
<script language="javascript" src="/hdm/v2/js/plugins/peity/peity-demo.js"></script>

<!-- Morris demo data-->
<script language="javascript" src="/hdm/v2/js/plugins/morris/morris-demo-1.js"></script>

<!-- Switchery -->
<script language="javascript" src="/hdm/v2/js/plugins/switchery/switchery.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/switchery/switchery-demo.js"></script>

<!-- Map -->
<script language="javascript" type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDQTpXj82d8UpCi97wzo_nKXL7nYrd4G70"></script>
<script language="javascript" src="/hdm/v2/js/plugins/map/map.js"></script>

</body>

</html>
