<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>

<head>
	
	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
	<!-- Pixel Admin's stylesheets -->
	<style>
		.float-e-margins .btn {
		    
		}

		.table > thead > tr > th, .table > tbody > tr > th, .table > tfoot > tr > th, .table > thead > tr > td, .table > tbody > tr > td, .table > tfoot > tr > td {
			text-align: left;
		}

		.inmodal .modal-header {
			padding: 10px 10px;
			text-align: center;
		}

		.modal-body {
			padding: 20px 30px 20px 30px;
		}

		.modal-footer {
			text-align: center;
		}
		<!-- added in 2017-09-06 -->
		.sortable {
			width: 320px;
			font-size: 0;
			padding: 10px;
			border-radius: 5px;
		}

		.portlet {
			font: 12px/1.3 sans-serif;
			margin: 10px;
			padding: 1px;
			display: inline-block;
			vertical-align: top;
			min-height: 100px;
			border: 1px solid #ddd;
			box-shadow: 5px 5px 5px #ddd;
		}

		.sub-portlet {
			font: 12px/1.3 sans-serif;
			margin: 10px;
			padding: 1px;
			display: inline-block;
			vertical-align: top;
			min-height: 100px;
			border: 1px solid #ddd;
			box-shadow: 5px 5px 5px #ddd;
		}

		.sub-portlet.sub-span { width: 200px; height: 130px;  }
		.portlet.span-1 { width: 120px; }
		.portlet.span-2 { width: 200px; }
		.portlet.span-3 { width: 310px; }

		.portlet-header {
			padding: 4px 0 6px 6px;
			background-color: #9bccff;
			font-size: 12px;
			color: #666;
			font-weight: bold;
		}

		.sub-portlet-header {
			padding: 4px 0 6px 6px;
			background-color: #9bccff;
			font-size: 12px;
			color: #666;
			font-weight: bold;
		}

		.portlet-header .ui-icon {
			float: right;
		}

		.sub-portlet-header .ui-icon {
			float: right;
		}

		.portlet-content {
			padding: 0;
		}

		.sub-portlet-content {
			padding: 0;
		}

		.portlet-minimized {
			height: auto;
		}

		.portlet-minimized .portlet-content {
			display: none;
		}

		.ui-sortable-placeholder {
			border: 1px dotted black;
			visibility: visible !important;
		}

		/* SWITCH */
		.cube-switch {
			border-radius:10px;
			border:1px solid rgba(0,0,0,0.4);
			box-shadow: 0 0 8px rgba(0,0,0,0.6), inset 0 100px 50px rgba(255,255,255,0.1);
			/* Prevents clics on the back */
			cursor:default;    
			display: block;
			height: 100px;
			position: relative;
			margin: 5% 0px 0px 10%;
			overflow:hidden;
			/* Prevents clics on the back */
			pointer-events:none;
			width: 100px;
			white-space: nowrap;
			background:#333;
		}

		/* The switch */
		.cube-switch .switch {
			border:1px solid rgba(0,0,0,0.6);
			border-radius:0.7em;
			box-shadow:
			inset 0 1px 0 rgba(255,255,255,0.3),
			inset 0 -7px 0 rgba(0,0,0,0.2),
			inset 0 50px 10px rgba(0,0,0,0.2),
			0 1px 0 rgba(255,255,255,0.2);
			display:block;
			width: 60px;
			height: 60px;
			margin-left:-30px;
			margin-top:-30px;
			position:absolute;
			top: 50%;
			left: 50%;
			width: 60px;
		 
			background:#666;
			transition: all 0.2s ease-out;

			/* Allows click */
			cursor:pointer;
			pointer-events:auto;
		}

		/* SWITCH Active State */
		.cube-switch.active {
			/*background:#222;
			box-shadow:
			0 0 5px rgba(0,0,0,0.5),
			inset 0 50px 50px rgba(55,55,55,0.1);*/
		}

		.cube-switch.active .switch {
			background:#333;
			box-shadow:
			inset 0 6px 0 rgba(255,255,255,0.1),
			inset 0 7px 0 rgba(0,0,0,0.2),
			inset 0 -50px 10px rgba(0,0,0,0.1),
			0 1px 0 rgba(205,205,205,0.1);
		}

		.cube-switch.active:after,
		.cube-switch.active:before {
			background:#333; 
			box-shadow:
			0 1px 0 rgba(255,255,255,0.1),
			inset 1px 2px 1px rgba(0,0,0,0.5),
			inset 3px 6px 2px rgba(200,200,200,0.1),
			inset -1px -2px 1px rgba(0,0,0,0.3);
		}

		.cube-switch.active .switch:after,
		.cube-switch.active .switch:before {
			background:#222;
			border:none;
			margin-top:0;
			height:1px;
		}

		.cube-switch .switch-state {
			display: block;
			position: absolute;
			left: 40%;
			color: #FFF;

			font-size: .5em;
			text-align: center;
		}

		/* SWITCH On State */
		.cube-switch .switch-state.on {
			bottom: 15%;
		}

		/* SWITCH Off State */
		.cube-switch .switch-state.off {
			top: 15%;
		}

		#light-bulb2 {
		width: 150px;
		height: 150px;
		background: url(https://lh4.googleusercontent.com/-katLGTSCm2Q/UJC0_N7XCrI/AAAAAAAABq0/6GxNfNW-Ra4/s300/lightbulb.png) no-repeat 0 0;
		}

		#light-bulb {
		position: relative;
		width: 150px;
		height: 150px;
		top: 15%;
		background: url(https://lh4.googleusercontent.com/-katLGTSCm2Q/UJC0_N7XCrI/AAAAAAAABq0/6GxNfNW-Ra4/s300/lightbulb.png) no-repeat -150px 0;
		cursor: move;
		z-index: 800;
		float: right;
		margin-top: -110px;
		}
	</style>
	
	
    <title>HDM - oneM2M Device Manager</title>
</head>


<body class="mini-navbar" >

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
        
        <div class="row wrapper border-bottom white-bg page-heading">
			<div class="col-sm-4">
				<h2>MOBIUS 연동 디바이스 관리</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>디바이스 관리</li>
					<li class="active"><strong>디바이스 모니터링</strong></li>
				</ol>
			</div>
		</div>
		
        <div class="wrapper wrapper-content animated fadeInRight">
		<!----------------->
		<!-- 컨텐츠 영역 시작 -->
		<!----------------->
		<div class="row">
            <div class="col-lg-12">
                <h2>oneM2M Platform&nbsp;:&nbsp;[${onem2m_host}]</h2>
                <ol class="breadcrumb" style="padding: 10px;margin-bottom: 10px;" id="ol-main-uri">
                    <li>
                        <strong>CSE base Name : ${remote_cse_base}&nbsp;&nbsp; / &nbsp;&nbsp;CSE ID : ${remote_cse_id}  </strong>
                    </li>
                    
                    <button type="button" class="btn btn-sm btn-primary" id="btn-refresh" style="float:right;margin-top: -6px;margin-right: -4px;">Refresh</button>

                </ol>
                
            </div>
            
        </div>
            
		<div class="row">
                <div class="col-lg-3">
                    <div class="ibox float-e-margins" style="height: 632px; overflow: auto;background-color: #fff;">
	                    <div class="ibox-title">
	                        <h3 class="no-margins">Device List</h3>
	                        <div class="ibox-tools" style="margin-top: -20px;">
	                            
	                            <a class="dropdown-toggle" id="icn-edit" href="#" data-toggle="modal" data-target="#myModal3" style="color:#fff; font-size:20px;">
	                                <i class="fa fa-gear"></i>
	                            </a>
	                            
	                        </div>
	                    </div>
						
			            <div class="ibox-content" style="max-height: 680px; overflow: auto;">

	                            <p>This windows shows AE name and node Id of all the devices registered in SI. </p>
								<br/>
	                            <div class="table-responsive">
	                                <table class="table table-bordered table-striped" id="tbl-main-res">
	                                    <thead>
	                                    <tr>
											<th>#</th>
	                                        <th>AE ID</th>
	                                        <th>Created Date</th>
	                                    </tr>
	                                    </thead>
	                                    <tbody>
									
	                                    </tbody>
	                                </table>
	                            </div>
	                          
	
	                    </div>    
                    </div>
                </div>
                <div class="col-lg-9">
                    <div class="ibox float-e-margins" style="min-height:580px;background-color: #fff;">
                        <div class="ibox-title">
                            <h3 class="no-margins">Device Detail</h3>
                            
                        </div>
                        <div class="ibox-content" style="height: 600px; overflow: auto; ">
							<span id="span_help_guide" style="position: relative; top: 200px; left: 34%; font-size: 20px;">왼쪽 디바이스 목록에서 NodeID를 선택하세요.</span>
							<div class="sortable">
							
									  
										
									 <div id="div_sensor" class="portlet ui-widget ui-widget-content ui-corner-all" style="display:none;">
										 <div class="portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>제실 감지 센서</div>
										 <div class="portlet-content">
											<div class="ibox-content" style="border-style: none;  width: 340px; padding-bottom: 0px;">
												<div id="g1"></div>
											</div>
										 </div>
									  </div>
																	
									  <div id="div_led" class="portlet ui-widget ui-widget-content ui-corner-all" style="display:none;">
										 <div class="portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>LED</div>
										 <div class="portlet-content" >
											<div class="ibox-content" style="border-style: none;  width: 340px; background: rgb(70, 72, 75); padding-bottom: 40px;">
												<div href="" class="cube-switch">
														<span class="switch">
															<span class="switch-state off">Off</span>
															<span class="switch-state on">On</span>
														</span>
												</div>
												<div id="light-bulb" class="off ui-draggable" ><div id="light-bulb2" style="opacity: 0; "></div></div>	
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
<script type="text/javascript">
	
	var mainUri ='${main_uri}';

	var mgmtCmdTypeArr = ['',
						  'reset',
						  'reboot',
						  'upload',
						  'download',
						  'software_install',
						  'software_uninstall',
						  'software_update'
	];
	
	var mgmtCmdExecStatus = ['',
	                         'initiated',
	                         'pending',
	                         'finished',
	                         'cancelling',
	                         'cancelled',
	                         'status_non_cancellable'
	];
	
	var mgmtCmdExecMode = ['',
	                       'immediateOnce',
	                       'immediateRepeat',
	                       'randomOnce',
	                       'randomRepeat'
	];

	var mgmtObjTypeMap = {
		"memory" : "div_memory_led",
		"firmware" : "div_firmware",
		"software" : "div_software",
		"battery" : "div_battery",
		"deviceInfo" : "div_device_info",
		"reboot" : "div_reboot",
		"eventLog" : "div_event_log"
	};
	
	var logStatus = ["", "Started", "Stopped", "Unknown", "Not Present", "Error"];
	
	var logType = ["", "System", "Security", "Event", "Trace", "Panic"];
	
	var mgmtObjMap = {};
	
	var init = function() {
		$('#tbl-ch-res > tbody').empty();
		$('#tbl-main-res > tbody').empty();
		$('#ol-main-uri > li').empty();
		$('select[name=rt] option').empty();
	};

	var sleep = function(m) {
		var start = new Date().getTime();
	    for (var i = 0; i < 1e7; i++) {
	      if ((new Date().getTime() - start) > m){
	        break;
	      }
	    }
	};
	
	$(document).ready(function () {
		//init();

		var deviceList = ${device_list};
		var mgmtCmdList = [];
		
		console.log(deviceList);
		
		var device, node, nodeLink;
		for(var i = 0; i < deviceList.length; i++) {
			
			device = deviceList[i];
			console.log("###### aei= " + device.aei + ", ct = " + device.ct);
			
			aeLink = "<a class='ae_link' id='" + device.ae_rn + "'>" + device.aei + "</a>";

			template = '<tr><td>'
                + i + '</td><td>'
                + aeLink + '</td><td>'
                + device.ct + '</td></tr>';
           
			$('#tbl-main-res > tbody:last').append(template);
		/*	
			node = device.node["m2m:nod"];
			
			nodeLink = "<a class='node_link' id='" + node.rn + "'>" + node.ni + "</a>";

			console.log(node);

			template = '<tr><td>'
                + i + '</td><td>'
                + device.ae_rn + '</td><td>'
                + nodeLink + '</td></tr>';
           
			$('#tbl-main-res > tbody:last').append(template);
			
			mgmtObjMap[node.ni] = node.ch;
		*/
		}
		
		$("a.ae_link").bind('click', function(e) {
			var resourceName = e.target.id;

			$("#span_help_guide").css("display", "none");
			
			var rn = "/${remote_cse_id}/${remote_cse_base}/" + resourceName ;

			$.ajax({
					type: 'GET',
					url: './getOneM2mContainers.do',
					dataType: 'json',
					data: {'uri': rn},
					success: function(result) {
						
						var containerList = $.parseJSON(result.content);
						var cont, rn;
						for(var i = 0; i < containerList.length; i++) {
								cont = containerList[i];
								rn = cont.cnt_rn;
		
								$("div#div_" + rn.substring(4)).css("display", "");
						}
						
						$("div#g1").empty();

						g1 = new JustGage({
							id: "g1",
							value: getRandomInt(1, 100),
							min: 0,
							max: 100
						});

						(function poll_sensor() {
							timeoutId = setTimeout(function() {
								$.ajax({
									type: 'GET',
									url: './getCurrentNotification.do',
									dataType: 'json',
									success: function(result) {	
										//console.log(result);
										if(result.result == 0) {
											var content = $.parseJSON(result.content);
											var total = 10;
											var current = content.con;
											
											$("div#g1").empty();
											
											g1 = new JustGage({
												id: "g1",
												value: current,
												min: 0,
												max: total
											});

											if(current > 0) {
												
											}
											
										}
									}, complete: poll_memory
								});
							}, 5000);
						})();

						console.log(result.content);
					}
			});

/*			$.ajax({
					type: 'GET',
					url: './getCurrentNotification.do',
					dataType: 'json',
					success: function(result) {	
						
						console.log(result.content);
					}
			});
*/
		});

		

		$('.cube-switch .switch').click(function() {
			if ($('.cube-switch').hasClass('active')) {
				$('.cube-switch').removeClass('active');
				$('#light-bulb2').css({'opacity': '0'});
			} else {
				$('.cube-switch').addClass('active');
				$('#light-bulb2').css({'opacity': '1'});
			}
		});


		$('.cube-switch .switch').trigger("click");
		
		
	});
</script>


</body>

</html>
