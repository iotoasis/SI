<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>

    <title>OASIS SI - 펌웨어 목록</title>
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
		
			<div class="row">
				<div class="col-md-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">Views</h3>
							<span class="label label-success pull-right">Monthly</span>
						</div>
						<div class="ibox-content">
						</div>
					</div>
				</div>
				<div class="col-md-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<span class="label label-info pull-right">Annual</span>
							<h3 class="no-margins">Orders</h3>
						</div>
						<div class="ibox-content">
						</div>
					</div>
				</div>
				<div class="col-md-4">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<span class="label label-primary pull-right">Today</span>
							<h3 class="no-margins">Visits</h3>
						</div>
						<div class="ibox-content">
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

<!-- Custom and plugin javascript -->
<script language="javascript" src="/hdm/v2/js/inspinia.js"></script>
<script language="javascript" src="/hdm/v2/js/plugins/pace/pace.min.js"></script>

</body>

</html>
