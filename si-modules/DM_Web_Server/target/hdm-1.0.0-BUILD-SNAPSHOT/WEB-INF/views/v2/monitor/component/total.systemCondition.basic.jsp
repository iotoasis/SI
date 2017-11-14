<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">시스템 현황</h3>
							<!-- <span class="label label-success pull-right">Monthly</span> -->
						</div>
						<div class="ibox-content">
							<span class="font-bold text-success pull-right text-right"><span id="tscb_model">-</span>개</span>
							<span class="no-margins">디바이스 모델</span>
							<div style="height:6px;"></div>
							<span class="font-bold text-success pull-right text-right"><span id="tscb_device">-</span>개</span>
							<span class="no-margins">디바이스</span>
							<div style="height:6px;"></div>
							<span class="font-bold text-success pull-right text-right"><span id="tscb_user">-</span>명</span>
							<span class="no-margins">관리자</span>
						</div>
					</div>


					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<!-- <span class="label label-info pull-right">Annual</span> -->
							<h3 class="no-margins">등록</h3>
						</div>
						<div class="ibox-content">
							<span class="label label-info pull-right">오늘</span>
							<span class="no-margins font-bold" style="font-size: 20px;line-height: 26px;height: 26px;"><span id="tscb_today">-</span>개</span>
							<br />
							<span class="start-percent font-bold text-success pull-right"><i id="tscb_today_change_sign" class="fa "></i> <span id="tscb_today_change">-</span>%</span>
							<br />
							<br />
							<span class="label label-info pull-right">어제</span>
							<span class="no-margins font-bold" style="font-size: 20px;height: 20px;line-height: 20px;"><span id="tscb_yesterday">-</span>개</span>
						</div>
					</div>