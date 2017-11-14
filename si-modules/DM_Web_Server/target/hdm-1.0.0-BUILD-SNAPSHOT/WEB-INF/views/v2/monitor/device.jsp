<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
    <title>OASIS SI Admin</title>
	<%@ include file="/WEB-INF/views/v2/common/common_js.jsp"%>
    <script type="text/javaScript" language="javascript">
		$(document).ready(function() {
			initUI();
		});
		
		function initUI() {
			$("#side-menu_device").addClass("active");
			$("#side-menu_device ul").addClass("in");
			$("#side-menu_device ul li:eq(1)").addClass("active");
		}
    </script>
</head>

<body>

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
		
		<c:forEach var="row" items="${rows}">
			<div class="row">
			<c:forEach var="component" items="${row.components}">
			
				<c:set var="compResources" value="" />
				<c:forEach var="resource" items="${component.resources}">				
					<c:set var="compResources" value="${compResources}${resource};" />
				</c:forEach>
				
				<c:set var="compParameters" value="" />
				<c:forEach var="parameter" items="${component.parameterList}">
					<c:set var="compParameters" value="${compParameters}${parameter};" />
				</c:forEach>
				
				<div class="col-lg-${component.colSize} hit-component-${component.compShortName}" resources="${compResources}" parameters="${compParameters}">
				
					<!-- title: ${component.title} -->
					<c:import url="./component/${component.compName}.jsp" charEncoding="UTF-8">
                    	<c:param name="colSize" value="${component.colSize}" />
                    	<c:param name="resources" value="${component.resources}" />
                    	<c:param name="parameterList" value="${component.parameterList}" />
                    	<c:param name="title" value="${component.title}" />
                    </c:import>
				</div>				
			</c:forEach>
			</div>			
		</c:forEach>
		
		<!----------------->
		<!--  컨텐츠 영역 끝  -->
		<!----------------->
			 
       	</div>
        <div class="footer">
        
        	<%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
        
        </div>
    </div>
</div>

<script>

var deviceInfo = ${deviceInfoJson};
var deviceModelInfo = ${deviceModelInfoJson};
var moMap = ${moMapJson};
var profileList = ${profileListJson};
/* var deviceStatusHistList = ${deviceStatusHistList}; */
var statCompMapJson = ${statCompMapJson};

</script>


<!-- include Plugin javascript files for each component -->
<c:forEach var="js" items="${pluginJs}">
<script src="${js}"></script>
</c:forEach>

<!-- include Custom javascript files for each component -->
<c:forEach var="js" items="${customeJs}">
<script src="${js}"></script>
</c:forEach>

<!-- include page javascript files for each component -->
<script src="${pageContext.request.contextPath}/v2/js/herit/monitorDevice.js"></script>
<script>

$(document).ready(function() {

	$('a.navbar-minimalize')[0].click();

});

</script>
</body>

</html>
