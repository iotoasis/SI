<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<c:forEach var="resource" items="${param.resources}">
<!-- dlg-resource: ${resource} -->
</c:forEach>
<!-- title: ${param.title} -->
<!-- resources: ${param.resources} -->

					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">Network</h3>
						</div>
						<div class="ibox-content" style="height: 180px;">
							<div class="row">
								<div class="col-lg-2">
									<div class="dsdlg_lwm2m-res-1-name" style="font-size: 15px; margin-left: 15px; padding-top: 8px;">-</div>
									<h1 class="dsdlg_lwm2m-res-1-value font-bold" style="margin-left: 15px; margin-right: 15px; float: left;">-</h1> 
									<h2 class="dsdlg_lwm2m-res-1-unit" style="margin-top: 10px;">-</h2>
									<div style="height: 20px;"></div>
									<div class="dsdlg_lwm2m-res-2-name" style="font-size: 15px; margin-left: 15px; padding-top: 20px;">-</div>
									<h1 class="dsdlg_lwm2m-res-1-value font-bold" style="margin-left: 15px; margin-right: 15px; float: left;">-</h1> 
									<h2 class="dsdlg_lwm2m-res-1-unit" style="margin-top: 10px;">-</h2>
								</div>
								<div class="col-lg-10">
									<div class="dsdlg_lwm2m-chart" style="width: 100%; height: 155px;">																		
										<canvas class="flot-base" style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 100%; height: 155px;">
										</canvas>
									</div>
									
									<!-- <div id="morris-line-chart" style="height: 155px;"></div> -->
								</div>
							</div>
						</div>
					</div>