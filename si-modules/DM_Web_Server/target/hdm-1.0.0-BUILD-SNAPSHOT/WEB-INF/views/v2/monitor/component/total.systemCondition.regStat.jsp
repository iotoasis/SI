<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">디바이스 등록 통계</h3>
						</div>
						<div class="ibox-content" style="height: 280px;" >
							<div id="tscrs_reg_stat_line_graph" style="height: 240px;"  class="flot-chart-content"></div>
                            <canvas style="height: 100px;" id="lineChart" height="100"></canvas>
						</div>
					</div>