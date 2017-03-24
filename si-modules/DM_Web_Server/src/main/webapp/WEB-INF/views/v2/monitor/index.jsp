<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>

<head>

	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
	
    <title>OASIS SI - 통합 모니터링</title>
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
		<c:forEach var="row" items="${rows}">
			<div class="row">
			<c:forEach var="component" items="${row.components}">
				<div class="col-lg-${component.colSize}">
					<c:import url="./component/${component.compName}.jsp" charEncoding="UTF-8">
                    	<c:param name="colSize" value="${component.colSize}"></c:param>
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

<!--  test -->


<%@ include file="/WEB-INF/views/v2/common/common_js.jsp"%>

<!-- include Plugin javascript files for each component -->
<c:forEach var="js" items="${pluginJs}">
<script src="${js}"></script>
</c:forEach>

<!-- include Custom javascript files for each component -->
<c:forEach var="js" items="${customeJs}">
<script src="${js}"></script>
</c:forEach>

<!-- include page javascript files for each component -->
<script src="${pageContext.request.contextPath}/v2/js/herit/monitorIndex.js"></script>


</body>

</html>
