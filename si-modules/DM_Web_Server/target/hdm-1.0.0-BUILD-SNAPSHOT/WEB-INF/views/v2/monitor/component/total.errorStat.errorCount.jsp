<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">장애 통계</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-lg-6" style="line-height: 40px;">
									<span class="start-percent font-bold text-danger pull-right">
									<i id="tesec_day_before1_arrow" class="fa "></i> <span id="tesec_day_before1_change">-</span>%</span>
									<span class="no-margins">어제 : <span id="tesec_day_before1">-</span>개</span>
									<br />
									<span class="start-percent font-bold text-danger pull-right">
									<i id="tesec_day_before2_arrow" class="fa "></i> <span id="tesec_day_before2_change">-</span>%</span>
									<span class="no-margins">그제 : <span id="tesec_day_before2">-</span>개</span>
									<br />
									<span class="no-margins">1개월 평균 : <span id="tesec_day_average">-</span>개</span>
								</div>
								<div class="col-lg-6" style="line-height: 40px;">
									<span class="start-percent font-bold text-danger pull-right">
									<i id="tesec_month_before1_arrow" class="fa "></i> <span id="tesec_month_before1_change">-</span>%</span>
									<span class="no-margins">지난달 : <span id="tesec_month_before1">-</span>개</span>
									<br />
									<span class="start-percent font-bold text-danger pull-right">
									<i id="tesec_month_before2_arrow" class="fa "></i> <span id="tesec_month_before2_change">-</span>%</span>
									<span class="no-margins">지지난달 : <span id="tesec_month_before2">-</span>개</span>
									<br />
									<span class="no-margins">1년 평균 : <span id="tesec_month_average">-</span>개</span>
								</div>
							</div>
						</div>
					</div>