<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<c:forEach var="resource" items="${param.resources}">
<!-- dlg-resource: ${resource} -->
</c:forEach>

<c:forEach var="parameter" items="${param.parameterList}">
<!-- dlg-parameters: ${parameter} -->
</c:forEach>
<!-- title: ${param.title} -->
<!-- resources: ${param.resources} -->
<!-- parameters: ${param.parameterList} -->

					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h5 class="">${param.title}</h5>
							
							<h5 class="dsslg-res-1-time" style="float: right;"></h5>
							
							<c:set var="parameterData" value="${param.parameterList}" />
							<c:set var="params" value="${fn:split(param.parameterList, ';')}" />
							<c:set var="paramsLength" value="${fn:length(parameterData)}" />
								
							<c:forEach items="${params}" var="parameter" varStatus="status">
								<c:choose>
									<c:when test="${fn:substring(parameterData,0,1) eq ';'}">
										<h5 style="float: right; margin-right: 15px;">${parameter} 이상</h5>
									</c:when>
									<c:when test="${fn:substring(parameterData, paramsLength-1, paramsLength) eq ';'}">
										<h5 style="float: right; margin-right: 15px;">${parameter} 이하</h5>
									</c:when>
									<c:when test="${parameterData eq ''}">
										<h5 style="float: right; margin-right: 15px;">-</h5>
									</c:when>
									<c:otherwise>
										<h5 style="float: right; margin-right: 15px;">${parameter}<c:if test="${status.last}">&nbsp;&nbsp;&nbsp;~</c:if></h5>
									</c:otherwise>
								</c:choose>
							</c:forEach>
							
							<%-- <c:forEach items="${params}" var="parameter">
									<c:if test="${fn:substring(parameterData,0,1) eq ';'}">
										<h5 style="float: right; margin-right: 15px; font-size: 20px; margin-top: -2px;">${parameter} 이하</h5>
									</c:if>
									<c:if test="${fn:substring(parameterData,0,1) ne ';'}">
										<h5 style="float: right; margin-right: 15px; font-size: 20px; margin-top: -2px;">${parameter}</h5>
									</c:if>
								</c:forEach> --%>
							
							<%-- <c:forEach items="${params}" var="parameter">
								<h5 style="float: right; margin-right: 15px; font-size: 20px; margin-top: -2px;">${parameter}</h5>
							</c:forEach> --%>
							<h5 style="float: right;">유효범위 :&nbsp;</h5>
							
						</div>
						<div class="ibox-content" style="height: 180px;">
							<div class="row">
								<div class="col-lg-3">
									<!-- <div class="dsslg-res-1-name" style="font-size: 15px; margin-left: 15px;">-</div> -->
									<h1 class="dsslg-res-1-value font-bold" style="margin-left: 5px; margin-right: 10px; font-size: 60px;">-</h1> 
									<h2 class="dsslg-res-1-unit" style="font-size: 30px;">-</h2>
								</div>
								<div class="col-lg-9">
									<div class="dsslg-chart" style="width: 100%; height: 155px;">																		
										<canvas class="flot-base" style="direction: ltr; position: absolute; left: 0px; top: 0px; width: 100%; height: 155px;">
										</canvas>
									</div>
									
								</div>
							</div>
						</div>
					</div>