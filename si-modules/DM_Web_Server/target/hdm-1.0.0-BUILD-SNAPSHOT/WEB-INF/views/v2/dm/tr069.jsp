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
	</style>
	
	
    <title>HDM - oneM2M based TR-069 Device Manager</title>
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
				<h2>oneM2M 연동 디바이스 관리</h2>
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
                        <strong>CSE base Name : ${cse_base}&nbsp;&nbsp; / &nbsp;&nbsp;CSE ID : ${cse_id}  </strong>
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
	                                        <th>AE Name</th>
	                                        <th>Node Id</th>
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
                        <div class="ibox-content" style="height: 600px; overflow: auto;">
							<span id="span_help_guide" style="position: relative; top: 200px; left: 34%; font-size: 20px;">왼쪽 디바이스 목록에서 NodeID를 선택하세요.</span>
							<div class="sortable">
							
									 <div id="div_device_info" class="portlet ui-widget ui-widget-content ui-corner-all" style="width: 340px; height: 284px; display:none;">
										 <div class="portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>Device Info</div>
										 <div class="portlet-content">
											<div class="ibox-content">
												<div class="table-responsive">
													<table class="table table_cont_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
														 <thead>
															<tr>
																<th>Attribute</th>
																<th>Value</th>
															</tr>
														 </thead>
													  </table>
												   </div>
												   <div class="table-responsive" style="height: 192px; overflow-x: hidden; overflow-y: auto;">
												   
													   <table class="table table-hover table_cont_thead table-condensed" style="width: 100%;">
														  
														  <tbody id="deviceInfoList">
															 <tr>
																<td>Device ID</td>
																<td>DEVICE_PI_43</td>
															 </tr>
															 <tr>
																<td>hardware ver.</td>
																<td>HW-20170710-0987654321</td>
															 </tr>
															 <tr>
																<td>manufacturer</td>
																<td>Herit</td>
															 </tr>
															 <tr>
																<td>model</td>
																<td>RP-20170905-1234567890</td>
															 </tr>
															 <tr>
																<td>serial</td>
																<td>FW20170905-1234567890</td>
															 </tr>
														  </tbody>
													   </table>
												</div>
											</div>
										 </div>
									  </div>

									  <div id="div_ctrl_history" class="portlet ui-widget ui-widget-content ui-corner-all" style="display: none; height: 284px;">
										 <div class="portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>제어 이력</div>
										 <div class="portlet-content">
												<div class="ibox-content">
												   <div class="table-responsive">
													  <table class="table table_cont_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
														 <thead>
															<tr>
															   <th>시간</th>
															   <th style="width: 180px;">리소스명</th>
															   <th style="width: 140px;">실행모드</th>
															   <th style="width: 80px;">결과</th>
															   <th>오류</th>
															</tr>
														 </thead>
													  </table>
												   </div>
												   <div class="table-responsive" style="height: 180px; overflow-x: hidden; overflow-y: auto;">
													  <table class="table table-hover table_cont_thead table-condensed" style="width: 100%;">
														 <tbody id="deviceControlHistoryList">
														 </tbody>
													  </table>
												   </div>
												</div>
												
										 </div>
									  </div>
								
									  <div id="div_memory_led" class="portlet ui-widget ui-widget-content ui-corner-all" style="width: 224px; border: none; box-shadow: none; margin: 0; display:none;">
										
										 <div id="div_memory" class="sub-portlet sub-span ui-widget ui-widget-content ui-corner-all" style="width: 200px; height: ">
											 <div class="sub-portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>Memory Free &nbsp;[Unit: MB]</div>
											 <div class="sub-portlet-content">
												<div id="g1" style="margin-top: -15px;"></div>
											 </div>
										  </div>
										  <div id="div_mgmt_control" class="sub-portlet sub-span ui-widget ui-widget-content ui-corner-all">
											 <div class="sub-portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>디바이스 제어</div>
											 <div class="sub-portlet-content" style="padding-top: 10px; padding-left: 28px;">
											
					                            <div class="input-group m-b" style="margin-top: 10px;">
		                                            <div class="input-group-btn">
		                                                <button data-toggle="dropdown" class="btn btn-white dropdown-toggle" type="button">Action <span class="caret"></span></button>
		                                                <ul class="dropdown-menu">
		                                                    <li><a href="#">Action</a></li>
		                                                    <li class="divider"></li>
		                                                    <li id="btn_reboot" style="display:none;"><a href="#">Reboot</a></li>
		                                                    <li id="btn_upload" style="display:none;"><a href="#">Upload</a></li>
		                                                    <li id="btn_download" style="display:none;"><a href="#">Download</a></li>
		                                                    
		                                                    <li id="btn_software_update" style="display:none;"><a href="#">Software Update</a></li>
		                                                    <li id="btn_software_install" style="display:none;"><a href="#">Software Install</a></li>
		                                                    <li id="btn_software_uninstall" style="display:none;"><a href="#">Software Uninstall</a></li>
		                                                    
		                                                    <li id="btn_reset" style="display:none;"><a href="#">Reset</a></li>
		                                                </ul>
		                                            </div>
		                                            <span class="input-group-btn" style="position: relative; left: -20px;">
		                                            	<button id="btn_exec_mgmt_cmd" type="button" class="btn btn-primary">Go!</button>
		                                            </span>
		                                        
	                                    		</div>
	                                    		<div style="margin-top: -10px; border: 1px solid #ddd; padding: 4px; margin-right: 40px; height: 28px;">
	                                    			<span id="span_selected_control" style="font-size: 14px;"></span>
	                                    		</div>
											 </div>
										  </div>  
									  </div>
								
									  <div id="div_firmware" class="portlet ui-widget ui-widget-content ui-corner-all" style="display:none;">
										 <div class="portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>Firmware</div>
										 <div class="portlet-content">
											<div class="ibox-content" style="height: 135px; border-style: none;">
											   <div class="row">
												  <span class="small-title">Name</span>
												  <h2 class="no-margins font-bold" id="fw_name"></h2>
												  <div style="height: 8px;"></div>
												  <span class="small-title">FW</span>
												  <h2 class="no-margins font-bold" style="float: left;" id="dsd_lwm2m_fw_package"></h2>
												  <span class="span-fw" style="margin-left:0;display:inline-block;overflow:hidden; text-overflow:ellipsis; width:180px;">ver:<span id="fw_version"></span></span>
											   </div>
											   <div class="row">
												  <span style="margin-left: 45px;" id="fw_create_date"></span>
												  <button type="button" id="btn_mgmtobj_firmware" class="btn btn-primary btn-smalls-size" style="width: 44px;"><small class="btn-smalls-text">Update</small></button>
												  <!-- &nbsp;&nbsp;<a data-toggle="modal" class="btn btn-primary btn-smalls-size" href="#dsd_lwm2m_fw_update_form" style="position:relative; top: 2px;"><small class="btn-smalls-text">버전업</small></a> -->                            
											   </div>
											</div>
										 </div>
									  </div>
									  <div id="div_software" class="portlet ui-widget ui-widget-content ui-corner-all" style="display:none; width: 280px;">
										 <div class="portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>Software</div>
										 <div class="portlet-content">
											<div class="ibox-content" style="height: 135px; border-style: none;">
											   <div class="row">
												  <span class="small-title">Name</span>
												  <h2 class="no-margins font-bold" id="sw_name"></h2>
												  <div style="height: 8px;"></div>
												  <span class="small-title">SW</span>
												  <h2 class="no-margins font-bold" style="float: left;" id="dsd_lwm2m_fw_package"></h2>
												  <span class="span-fw" style="margin-left:0;display:inline-block;overflow:hidden; text-overflow:ellipsis; width:220px;">ver:<span id="sw_version"></span></span>
											   </div>
											   <div class="row">
												  <span style="margin-left: 45px;" id="sw_create_date"></span>
												  <button type="button" id="btn_mgmtobj_software_install" class="btn btn-primary btn-smalls-size" style="width: 40px; margin-right: 4px;"><small class="btn-smalls-text">Install</small></button>
												  <button type="button" id="btn_mgmtobj_software_uninstall" class="btn btn-primary btn-smalls-size" style="width: 50px;"><small class="btn-smalls-text">Uninstall</small></button>
												                             
											   </div>
											</div>
										 </div>
									  </div>
									  
									  <div id="div_event_log" class="portlet  ui-widget ui-widget-content ui-corner-all" style="display:none;">
										 <div class="portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>EventLog</div>
										 <div class="portlet-content">
											<div class="ibox-content" style="height: 135px; border-style: none;">
											   <div class="row">
												  <span class="small-title">File</span>
												  <h2 class="no-margins font-bold" id="evl_file"></h2>
												  <div style="height: 8px;"></div>
												  <span class="small-title">Event</span>
												  <span class="span-fw" style="margin-left:0;display:inline-block;overflow:hidden; text-overflow:ellipsis; width:160px;">Type&nbsp;:&nbsp;<span id="evl_type"></span></span>
											   </div>
											   <div class="row">
												  <span class="small-title">Event</span>
												  <h2 class="no-margins font-bold" id="evl_file"></h2>
												  <span class="span-fw" style="margin-left:0;display:inline-block;overflow:hidden; text-overflow:ellipsis; width:120px;">Status&nbsp;:&nbsp;<span id="evl_status"></span></span>
											   </div>
											   <div class="row">
												  <span style="margin-left: 45px;" id="evl_create_date"></span>
												  <button type="button" id="btn_mgmtobj_eventlog" class="btn btn-primary btn-smalls-size"  style="width: 32px;"><small class="btn-smalls-text" >Start</small></button> 
												  <!-- &nbsp;&nbsp;<a data-toggle="modal" class="btn btn-primary btn-smalls-size" href="#dsd_lwm2m_fw_update_form" style="position:relative; top: 2px;"><small class="btn-smalls-text">Execute</small></a> -->                           
											   </div>
											</div>
										 </div>
									  </div>

									  <div id="div_log_history" class="portlet ui-widget ui-widget-content ui-corner-all" style="display: none; width: 420px;">
										 <div class="portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>이벤트 로그 목록</div>
										 <div class="portlet-content">
												<div class="ibox-content">
												   <div class="table-responsive">
													  <table class="table table_cont_thead table-condensed" style="width: 100%; padding: 0; margin:0; border:none;">
														 <thead>
															<tr>
															   <th style="width: 140px;">시간</th>
															   <th style="width: 240px;">로그정보</th>
															</tr>
														 </thead>
													  </table>
												   </div>
												   <div class="table-responsive" style="height: 100px; overflow-x: hidden; overflow-y: auto;">
													  <table class="table table-hover table_cont_thead table-condensed" style="width: 100%;">
														 <tbody id="eventLogHistoryList">
															<tr>
															   <td colspan="2"><center>데이터가 업습니다.</center></td>
															</tr>
														 </tbody>
													  </table>
												   </div>
												</div>
												
										 </div>
									  </div>
									<!--    
									  <div class="portlet  ui-widget ui-widget-content ui-corner-all" style="padding-bottom: 36px; display: none;" id="div_mgmt_cmd">
										 <div class="portlet-header ui-widget-header ui-corner-all"><span class="ui-icon ui-icon-minusthick"></span>제어</div>
										 <div class="portlet-content">
											<div class="ibox-content" style="height: 100px;">
												<button type="button" id="btn_reboot" class="btn btn-primary btn-re-size btn_reboot" style="display:none;"><span class="btn-re-text btn-disabled" >Reboot</span></button>
												<button type="button" id="btn_upload" class="btn btn-primary btn-re-size btn_reboot" style="display:none;"><span class="btn-re-text btn-disabled" >Upload</span></button>
												<button type="button" id="btn_download" class="btn btn-primary btn-re-size btn_reboot" style="display:none;"><span class="btn-re-text btn-disabled" >Download</span></button><br>
												<button type="button" id="btn_software_update" class="btn btn-primary btn-re-size btn_firmware_update" style="display:none;"><span class="btn-re-text btn-disabled" >Software Update</span></button>
												<button type="button" id="btn_software_install" class="btn btn-primary btn-re-size btn_firmware_update" style="display:none;"><span class="btn-re-text btn-disabled" >Software Install</span></button><br>
												<button type="button" id="btn_software_uninstall" class="btn btn-primary btn-re-size btn_firmware_update" style="display:none;"><span class="btn-re-text btn-disabled" >Software Uninstall</span></button>
												<button type="button" id="btn_reset" class="btn btn-primary btn-re-size btn_firmware_update" style="display:none;"><span class="btn-re-text btn-disabled" >Reset</span></button>
											</div>
										 </div>
									  </div>
									-->
										
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
		var mgmtCmdList = ${mgmt_cmd_list};
		
		console.log(deviceList);
		console.log("\n###################\n");
		console.log(mgmtCmdList);
		
		var device, node, nodeLink, nodeName;
		for(var i = 0; i < deviceList.length; i++) {
			
			device = deviceList[i];
			node = device.node["m2m:nod"];
			
			nodeLink = "<a class='node_link' id='" + node.rn + "'>" + node.ni + "</a>";

			console.log(node);
			
			nodeName = node.rn;
			if(nodeName.startsWith("tr069")) {	// tr069 디바이스만 노출
				template = '<tr><td>'
	                + i + '</td><td>'
	                + device.ae_rn + '</td><td>'
	                + nodeLink + '</td></tr>';
	           
				$('#tbl-main-res > tbody:last').append(template);
				
				mgmtObjMap[node.ni] = node.ch;	
			}
		}
		
		var tid, mgmtCmdType, nodeId, mgmtCmd;
		$("a.node_link").bind('click', function(e) {
			
			var resourceName = e.target.id;
			nodeId = e.target.innerText;
			
			$("#span_help_guide").css("display", "none");
			$("div#div_mgmt_cmd").css("display", "");
			
			// mgmtCmd setup
			for(var i = 0; i < mgmtCmdList.length; i++) {
				mgmtCmd = mgmtCmdList[i];
				if(mgmtCmd["m2m:mgc"].ext == nodeId) {
					tid = mgmtCmdType = mgmtCmd["m2m:mgc"].cmt;
					mgmtCmdType = mgmtCmdTypeArr[tid];

					$("#btn_" + mgmtCmdType).css("display", "");
					$("#btn_" + mgmtCmdType).attr("name", mainUri + "/" + mgmtCmd["m2m:mgc"].rn);
				}
			}
			
			$("#div_log_history").css("display", "");
			//$("#eventLogHistoryList").empty();

			if(mgmtCmdList.length > 0) {
				$("#div_ctrl_history").css("display", "");
				$("#deviceControlHistoryList").empty();
				
				var rn = "/${cse_id}/${cse_base}/" + resourceName;
				
				$.ajax({
					type: 'GET',
					url: './getMgmtCmdResult.do',
					dataType: 'json',
					data: {'rn': rn},
					success: function(result) {	
						//console.log(result);
						if(result.result == 0) {
							var execInstList = $.parseJSON(result.content);
							var execInst;
							
							for(var i = 0; i < execInstList.length; i++) {
								execInst = execInstList[i];
								var _uri = execInst._uri;
								//console.log(_uri.split('\/'));
								//console.log("\n")
								$("#deviceControlHistoryList").append("<tr>"
										   + "<td>" + execInst.ct + "</td>"
										   + "<td style='width: 180px;'>" + _uri.split('\/')[3] + "</td>"
										   + "<td style='width: 140px;'>" + mgmtCmdExecMode[execInst.exm] + "</td>"
										   + "<td style='width: 80px;'>" + mgmtCmdExecStatus[execInst.exs] + "</td>"
										   + "<td>-</td>"
										   + "</tr>");
							}
							
							if(execInstList.length == 0) {
								$("#deviceControlHistoryList").append("<tr>"
										   + "<td colspan=5><center>데이터가 없습니다.</center></td>"
										   + "</tr>");
							}
							
						}
					}
				});
				
				(function poll_control_history() {
					timeoutId = setTimeout(function() {
						
						$.ajax({
							type: 'GET',
							url: './getMgmtCmdResult.do',
							dataType: 'json',
							data: {'rn': rn},
							success: function(result) {	
								//console.log(result);
								
								$("#deviceControlHistoryList").empty();
								
								if(result.result == 0) {
									var execInstList = $.parseJSON(result.content);
									var execInst;
									
									for(var i = 0; i < execInstList.length; i++) {
										execInst = execInstList[i];
										var _uri = execInst._uri;
										//console.log(_uri.split('\/'));
										//console.log("\n")
										$("#deviceControlHistoryList").append("<tr>"
												   + "<td>" + execInst.ct + "</td>"
												   + "<td style='width: 180px;'>" + _uri.split('\/')[3] + "</td>"
												   + "<td style='width: 140px;'>" + mgmtCmdExecMode[execInst.exm] + "</td>"
												   + "<td style='width: 80px;'>" + mgmtCmdExecStatus[execInst.exs] + "</td>"
												   + "<td>-</td>"
												   + "</tr>");
									}
									
									if(execInstList.length == 0) {
										$("#deviceControlHistoryList").append("<tr>"
												   + "<td colspan=5><center>데이터가 없습니다.</center></td>"
												   + "</tr>");
									}
									
								}
							}, complete: poll_control_history
						});
					}, 5000);
				})();
				
			}
			
			//mgmtCmd reset
			$("#btn_reset").bind("click", function(e){
				
				$("#span_selected_control").text("Reset");
				$("#btn_exec_mgmt_cmd").attr('name', $(this).attr('name'));
				
				/*$.ajax({
					type: 'GET',
					url: './execMgmtCmd.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							
							alert("명령[Reset]이 성공적으로 수행되었습니다.");
						}
					}
				});*/
			});
			
			//mgmtCmd reboot
			$("#btn_reboot").bind("click", function(e){
				
				$("#span_selected_control").text("Reboot");
				$("#btn_exec_mgmt_cmd").attr('name', $(this).attr('name'));
				/*
				$.ajax({
					type: 'GET',
					url: './execMgmtCmd.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							
							alert("명령[Reboot]이 성공적으로 수행되었습니다.");
						}
					}
				}); */
			});
			
			//mgmtCmd upload
			$("#btn_upload").bind("click", function(e){
				
				$("#span_selected_control").text("Upload");
				$("#btn_exec_mgmt_cmd").attr('name', $(this).attr('name'));
				/*
				$.ajax({
					type: 'GET',
					url: './execMgmtCmd.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							
							alert("명령[Upload]이 성공적으로 수행되었습니다.");
						}
					}
				});
				*/
			});
			
			//mgmtCmd download
			$("#btn_download").bind("click", function(e){
				
				$("#span_selected_control").text("Download");
				$("#btn_exec_mgmt_cmd").attr('name', $(this).attr('name'));
				/*
				$.ajax({
					type: 'GET',
					url: './execMgmtCmd.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							
							alert("명령[Download]이 성공적으로 수행되었습니다.");
						}
					}
				});
				*/
			});
			
			//mgmtCmd software update
			$("#btn_software_update").bind("click", function(e){
				
				$("#span_selected_control").text("Software Update");
				$("#btn_exec_mgmt_cmd").attr('name', $(this).attr('name'));
				/*
				$.ajax({
					type: 'GET',
					url: './execMgmtCmd.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							
							alert("명령[Software update]이 성공적으로 수행되었습니다.");
						}
					}
				});
				*/
			});
			
			//mgmtCmd software uninstall
			$("#btn_software_uninstall").bind("click", function(e){
				
				$("#span_selected_control").text("Software Uninstall");
				$("#btn_exec_mgmt_cmd").attr('name', $(this).attr('name'));
				/*
				$.ajax({
					type: 'GET',
					url: './execMgmtCmd.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							
							alert("명령[Software uninstall]이 성공적으로 수행되었습니다.");
						}
					}
				});
				*/
			});
			
			// Go button click
			$("#btn_exec_mgmt_cmd").bind("click", function(e){
				var uri = $(this).attr("name");
				var cmdText = $("#span_selected_control")[0];
				
				$.ajax({
					type: 'GET',
					url: './execMgmtCmd.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							
							alert("명령[" + cmdText.innerText + "]이 성공적으로 수행되었습니다.");
							$("#span_selected_control").text("");
						}
					}
				});
				
			});
			
			
			////////////////////// MgmtObj ////////////////////////////
			
			// show firmware
			$("#div_firmware").on('change', function(e) {
				var uri = e.target.title;
				
				$.ajax({
					type: 'GET',
					url: './getOneM2mMgmtResource.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							console.log(result.content);
							var content = $.parseJSON(result.content);
							var body = content["m2m:fwr"];
							
							$("#fw_name").text(body["fwnnam"]);
							$("#fw_version").text(body["vr"]);
							$("#fw_create_date").text(body["ct"]);
						}
					}
				});
				
			});
			
			// update firmware
			$("#btn_mgmtobj_firmware").on('click', function(e) {
				var uri = $("#div_firmware").attr("title");
				
				$.ajax({
					type: 'POST',
					url: './execMgmtObj.do',
					dataType: 'json',
					data: {'uri': uri, 'attr' : 'ud', 'cmd' : 'fwr'},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							console.log(result.content);
							var content = $.parseJSON(result.content);
							alert("성공적으로 펌웨어가 업데이트되었습니다.");
							console.log("################### btn_mgmtobj_firmware #############\n");
							console.log(content);
						}
					}
				});
				
			});
			
			// show software
			$("#div_software").on('change', function(e) {
				var uri = e.target.title;
				
				$.ajax({
					type: 'GET',
					url: './getOneM2mMgmtResource.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							console.log(result.content);
							//alert("div_software");
							var content = $.parseJSON(result.content);
							var body = content["m2m:swr"];
							
							$("#sw_name").text(body["swn"]);
							$("#sw_version").text(body["vr"]);
							$("#sw_create_date").text(body["ct"]);
						}
					}
				});
				
			});
			
			// install software
			$("#btn_mgmtobj_software_install").on('click', function(e) {
				var uri = $("#div_software").attr("title");
				
				$.ajax({
					type: 'POST',
					url: './execMgmtObj.do',
					dataType: 'json',
					data: {'uri': uri, 'attr' : 'in', 'cmd' : 'swr'},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							console.log(result.content);
							var content = $.parseJSON(result.content);
							alert("성공적으로 소프트웨어가 설치되었습니다.");
							console.log("################### btn_mgmtobj_software_install #############\n");
							console.log(content);
						}
					}
				});
				
			});
			
			// install software
			$("#btn_mgmtobj_software_uninstall").on('click', function(e) {
				var uri = $("#div_software").attr("title");
				
				$.ajax({
					type: 'POST',
					url: './execMgmtObj.do',
					dataType: 'json',
					data: {'uri': uri, 'attr' : 'un', 'cmd' : 'swr'},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							console.log(result.content);
							var content = $.parseJSON(result.content);
							alert("성공적으로 소프트웨어가 삭제되었습니다.");
							console.log("################### btn_mgmtobj_software_uninstall #############\n");
							console.log(content);
						}
					}
				});
				
			});
			
			// show device info
			$("#div_device_info").on('change', function(e) {
				var uri = e.target.title;
				
				var divInfoMap = {
						"rn" : "Device Name",
						"cr" : "Created Date",
						"dlb" : "Device Label",
						"man" : "Manufacturer",
						"mod" : "Model Name",
						"dty" : "Device Type",
						"fwv" : "Firmware Ver.",
						"swv" : "Software Ver.",
						"hwv" : "Hardware Ver."
				}
				
				$.ajax({
					type: 'GET',
					url: './getOneM2mMgmtResource.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						//console.log(result);
						if(result.result == 0) {
							
							$('#deviceInfoList').empty();
							
							var content = $.parseJSON(result.content);
							var body = content["m2m:dvi"];
							var template;
							
							for(var key in body) {
								if(divInfoMap[key] != undefined) {
									
									template = '<tr>'
										           + '<td>' + divInfoMap[key] + '</td>'
												   +   '<td>' + body[key] + '</td>'
									 			   + '</tr>';
									$('#deviceInfoList').append(template);
									
								}
							}
							
						}
					}
				});
				
			});
			
			// show event log
			$("#div_event_log").on('change', function(e) {
				var uri = e.target.title;
				
				$.ajax({
					type: 'GET',
					url: './getOneM2mMgmtResource.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						console.log(result);
						if(result.result == 0) {
							console.log(result.content);
							
							$('#eventLogHistoryList').empty();
							
							var content = $.parseJSON(result.content);
							var body = content["m2m:evl"];
							
							var lgt = body["lgt"];
							var lgst = body["lgst"];
							var lgd = body["lgd"];
							
							$("#evl_file").text("debug.log");
							$("#evl_status").text(logStatus[lgst]);
							$("#evl_type").text(logType[lgt]);
							$("#evl_create_date").text(body["ct"]);
							
							if(!lgd.startsWith("{")) {
								$('#eventLogHistoryList').append('<tr><td colspan="2"><center>데이터가 없습니다.</center></td></tr>');
							} else {
								var jsonLgd = $.parseJSON(lgd);
								var eventLogList = jsonLgd["debug"];
								var row, template;
								for(var i = 0; i < eventLogList.length; i++) {
									row = eventLogList[i];
									template = '<tr>'
								           + '<td style="width: 140px;">' + row['datetime'] + '</td>'
										   +   '<td style="width: 240px;">' + row['content'] + '</td>'
							 			   + '</tr>';
									$('#eventLogHistoryList').append(template);
								}
								
								if(eventLogList.length == 0) {
									$('#eventLogHistoryList').append('<tr><td colspan="2"><center>데이터가 없습니다.</center></td></tr>');
								}
							}
							
						}
					}
				});
				
			});
			
			// start eventLog
			$("#btn_mgmtobj_eventlog").unbind('click').on('click', function(e) {
				var uri = $("#div_event_log").attr("title");
				var btnText = $(this).children("small")[0].innerText;
				var resultMsg = "";
				
				var attrName = "";
				if(btnText == 'Start') {
					attrName = "lga";
					$("#btn_mgmtobj_eventlog small").html("Stop");
					resultMsg = "이벤트 로그 실행을 시작합니다.";
				} else {
					attrName = "lgo";
					$("#btn_mgmtobj_eventlog small").html("Start");
					resultMsg = "이벤트 로그 실행을 완료합니다.";
				}
				
				alert(resultMsg);
				
				$.ajax({
					type: 'POST',
					url: './execMgmtObj.do',
					dataType: 'json',
					data: {'uri': uri, 'attr' : attrName, 'cmd' : 'evl'},
					success: function(result) {	
						
						console.log(result);
						if(result.result == 0) {
							console.log(result.content);
							var content = $.parseJSON(result.content);
							var contBody = content.content;
							var resultJson = $.parseJSON(contBody[0]);
							
							console.log("################### btn_mgmtobj_eventlog #############\n");
							
							var body = resultJson["m2m:evl"];
							
							var lgt = body["lgt"];
							var lgst = body["lgst"];
							var lgd = body["lgd"];
							
							$("#evl_status").text(logStatus[lgst]);
							$("#evl_type").text(logType[lgt]);
							$("#evl_create_date").text(body["ct"]);
							
							if(attrName == "lgo") {
								$('#eventLogHistoryList').empty();
								
								var jsonLgd = $.parseJSON(lgd);
								var eventLogList = jsonLgd["debug"];
								var row, template;
								for(var i = 0; i < eventLogList.length; i++) {
									row = eventLogList[i];
									template = '<tr>'
								           + '<td style="width: 140px;">' + row['datetime'] + '</td>'
										   +   '<td style="width: 240px;">' + row['content'] + '</td>'
							 			   + '</tr>';
									$('#eventLogHistoryList').append(template);
								}
								
								if(eventLogList.length == 0) {
									$('#eventLogHistoryList').append('<tr><td colspan="2"><center>데이터가 없습니다.</center></td></tr>');
								}
							}
							
						}
					}
				});
				
			});
			
			// show memory led
			$("#div_memory_led").on('change', function(e) {
				var uri = e.target.title;
				
				$.ajax({
					type: 'GET',
					url: './getOneM2mMgmtResource.do',
					dataType: 'json',
					data: {'uri': uri},
					success: function(result) {	
						//console.log(result);
						if(result.result == 0) {
							//console.log(result.content);
							
							var content = $.parseJSON(result.content);
							var body = content["m2m:mem"];
							var total = Math.floor(body.mmt/1000);
							var current = Math.floor(body.mma/1000);
							
							g1 = new JustGage({
					            id: "g1",
					            value: getRandomInt(current, total),
					            min: 0,
					            max: total
					        });
							
						}
					}
				});
				
				(function poll_memory() {
					timeoutId = setTimeout(function() {
						$.ajax({
							type: 'GET',
							url: './getOneM2mMgmtResource.do',
							dataType: 'json',
							data: {'uri': uri},
							success: function(result) {	
								//console.log(result);
								if(result.result == 0) {
									//console.log(result.content);
									
									var content = $.parseJSON(result.content);
									var body = content["m2m:mem"];
									var total = Math.floor(body.mmt/1000);
									var current = Math.floor(body.mma/1000);
									
									$("div#g1").empty();
									
									g1 = new JustGage({
							            id: "g1",
							           // value: getRandomInt(current, total),
							           value: (total - current),
							            min: 0,
							            max: total
							        });
									
								}
							}, complete: poll_memory
						});
					}, 5000);
				})();
				
			});
			
			// mgmtObj setup
			var mgmtObjList = mgmtObjMap[nodeId];
			var objType, nm, item;
			
			for(var i = 0; i < mgmtObjList.length; i++) {
				item = mgmtObjList[i];
				if(item.typ == 13) {
					nm = item.nm;
					objType = nm.split('_')[1];
					
					$("#" + mgmtObjTypeMap[objType]).attr("title", item.value);
					$("#" + mgmtObjTypeMap[objType]).show().trigger('change');
					
					console.log("###################### item.value = " + item.value + "\n");
					sleep(700);
				
				}
			}
			
			$('.sortable').sortable().disableSelection();

			console.log(mgmtObjList);
		});
		
		$("#btn-refresh").bind("click", function(e) {
			location.reload();
		});
			
	});
</script>


</body>

</html>
