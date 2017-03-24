<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">TODAY</h3>
						</div>
						<div class="ibox-content" style="height: 135px;">
							<div class="row">
								<div class="col-lg-4 font-bold" style="font-size: 13px;">알람
									<span class="pull-right start-percent font-bold text-navy"><i class="fa " id="dst_alarm_change_dir"></i> <span id="dst_alarm_yesterday">-</span> (<span id="dst_alarm_change">23</span>%)</span>
								</div>
								<div class="col-lg-4 font-bold" style="font-size: 13px;">노티
									<div class="pull-right start-percent font-bold text-info"><i class="fa " id="dst_noti_change_dir"></i> <span id="dst_noti_yesterday">-</span> (<span id="dst_noti_change">23</span>%)</div>
								</div>
								<div class="col-lg-4 font-bold" style="font-size: 13px;">에러
									<div class="pull-right start-percent font-bold text-warning"><i class="fa " id="dst_error_change_dir"></i> <span id="dst_error_yesterday">-</span> (<span id="dst_error_change">23</span>%)</div>
								</div>
							</div>
							<div class="row">
								<div class="col-lg-4">
									<div class="widget style1 navy-bg" style="height: 70px;">
										<div class="row vertical-align">
											<div class="col-xs-4">
												<i class="fa fa-bell-o fa-2x"></i>
											</div>
											<div class="col-xs-8 text-right">
												<h2 class="font-bold" style="font-size: 25px;" id="dst_alarm_today">217</h2>	
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-4">
									<div class="widget style1 lazur-bg" style="height: 70px;">
										<div class="row vertical-align">
											<div class="col-xs-4">
												<i class="fa fa-pencil-square fa-2x"></i>
											</div>
											<div class="col-xs-8 text-right">
												<h2 class="font-bold" style="font-size: 25px;" id="dst_noti_today">462</h2>
											</div>
										</div>
									</div>
								</div>
								<div class="col-lg-4">
									<div class="widget style1 yellow-bg" style="height: 70px;">
										<div class="row vertical-align">
											<div class="col-xs-4">
												<i class="fa fa-warning fa-2x"></i>
											</div>
											<div class="col-xs-8 text-right">
												<h2 class="font-bold" style="font-size: 25px;" id="dst_error_today">610</h2>
											</div>  
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>