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
		     margin-bottom: 0; 
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
			margin: 14px;
			padding: 1px;
			display: inline-block;
			vertical-align: top;
			min-height: 100px;
			border: 1px solid #ddd;
			box-shadow: 5px 5px 5px #ddd;
		}

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

		.portlet-header .ui-icon {
			float: right;
		}

		.portlet-content {
			padding: 14px;
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
	
	
    <title>HDM - Fi-Ware Device Manager</title>
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
				<h2>Fi-Ware 연동 디바이스 관리</h2>
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
                <h2>Fi-Ware Platform&nbsp;:&nbsp;[HTTP://${fiware_host}:${fiware_port}]</h2>
                <ol class="breadcrumb" style="padding: 10px;margin-bottom: 10px;" id="ol-main-uri">
                    <li>
                        <strong>Fi-Ware Service Name : ${fiware_service}&nbsp;&nbsp; / &nbsp;&nbsp;Fi-Ware Service Path Name : ${fiware_service_path}  </strong>
                    </li>
                    
                    <button type="button" class="btn btn-sm btn-primary" id="btn-refresh" style="float:right;margin-top: -6px;margin-right: -4px;">Refresh</button>

                </ol>
                
            </div>
            
        </div>
            
		<div class="row">
                <div class="col-lg-4">
                    <div class="ibox float-e-margins" style="min-height:580px;background-color: #fff;">
	                    <div class="ibox-title">
	                        <h3 class="no-margins">Device List</h3>
	                   <!--  <div class="ibox-tools" style="margin-top: -20px;">
	                            
	                            <a class="dropdown-toggle" id="icn-edit" href="#" data-toggle="modal" data-target="#myModal3" style="color:#fff; font-size:20px;">
	                                <i class="fa fa-gear"></i>
	                            </a>
	                            
	                        </div> -->
	                    </div>
						
						<div class="modal inmodal" id="myModal3" tabindex="-1" role="dialog"  aria-hidden="true">
							<div class="modal-dialog">
								<div class="modal-content animated fadeIn">
									<div class="modal-header">
										<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
										<h4 class="modal-title">Update Resource</h4>
										<small>oneM2M Resource can be updated by this popup window</small>
									</div>
									<div class="modal-body">
										<form class="form-horizontal" name="frmUpdate">
											<input type="hidden" name="ed_uri" value="${main_uri}">
											<div class="form-group"><label class="col-lg-2 control-label">URI</label>

												<div class="col-lg-10"><span class="help-block m-b-none">${main_uri}</span>
												</div>
											</div>
											
											<div class="form-group"><label class="col-lg-2 control-label">Type</label>

												<div class="col-lg-10"> <span class="help-block m-b-none" id="span-edit-rt"></span>
												</div>
											</div>
											<div class="form-group"><label class="col-lg-2 control-label">Content</label>

												<div class="col-lg-10"><textarea  class="form-control" rows="10" name="ed_con"></textarea></div>
											</div>
										</form>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
										<button type="button" class="btn btn-primary" id="btn-update">Update</button>
									</div>
								</div>
							</div>
						</div> <!-- end of div modal inmodal --> 

			            <div class="ibox-content" style="max-height: 580px; overflow: auto;">

	                            <p>This windows shows all the Fi-ware Devices registered through oneM2M SI(IN-CSE) Server.</p>
								<br/>
	                            <div class="table-responsive">
	                                <table class="table table-bordered table-striped" id="tbl-main-res">
	                                    <thead>
	                                    <tr>
											<th>#</th>
	                                        <th>Name</th>
	                                        <th>Type</th>
	                                        <th>Created</th>
	                                    </tr>
	                                    </thead>
	                                    <tbody>
									
	                                    </tbody>
	                                </table>
	                            </div>
	                          
	
	                    </div>    
                    </div>
                </div>
                <div class="col-lg-8">
                    <div class="ibox float-e-margins" style="min-height:610px;background-color: #fff;">
                        <div class="ibox-title">
                            <h3 class="no-margins">Device Detail</h3>
                            
                        </div>
                        <div class="ibox-content" style="max-height: 980px; overflow: auto;">
						
							<div class="sortable" id="div-sensor-item">
								
							</div>
							<div style="text-align: center; padding-top: 230px; font-size: 20px; color: #aaa;" id="div-init-text">왼쪽 디바이스 목록을 클릭하면 상세 정보를 볼 수 있습니다.</div>
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
	
	var init = function() {
		$('#tbl-ch-res > tbody').empty();
		$('#tbl-main-res > tbody').empty();
		$('#ol-main-uri > li').empty();
		$('select[name=rt] option').empty();
	};

	var gBtnLed = "";
	var gBtnText = "initialize";

	var entity_detail_func = function(entityId, subscriptionId) {
			
					$.ajax({
						type: 'GET',
						url: './entities.do',
						dataType: 'json',
						data: {'id': entityId},
						success: function(result) {		
							console.log(result);
							if(result.result == 0) {
								
								var cont = $.parseJSON(result.content);
								var arrSensors = [];

								var deviceTable = '<div class="portlet span-4 ui-widget ui-widget-content ui-corner-all">	'
									+ '<div class="portlet-header">Device Info</div>'
									+ '<div class="portlet-content" style="padding: 14px 14px 0 14px;">'
									+	'<div class="table-responsive">'
									+		'<table class="table table-bordered table-striped" id="tbl-device-info">'
									+			'<thead>'
									+			'<tr>'	
									+				'<th>Attribute</th>'
									+				'<th>Value</th>'
									+			'</tr>'	
									+			'</thead>'		
									+			'<tbody>'	
									+               '<tr><td>Device ID</td><td>' + entityId + '</td></tr>'
									+			'</tbody>'
									+		'</table>'
									+	'</div>'
									+ '</div>'	
									+ '</div>';
								
								$("div#div-sensor-item").append(deviceTable);

								var titleMap = {
									'humid': 'Humidity [Unit: %]',
									'temp': 'Temperature [Unit: ℃]',
									'led_info': 'LED Control'
								};

								for(var key in cont) {
									if(jQuery.isPlainObject(cont[key])) {
										var obj = cont[key];
										
										if(!jQuery.isNumeric( obj["value"] ) && obj["type"] == "string" && key.indexOf("_info") < 0 && key.indexOf("_status") < 0 ) { 
											
											template = '<tr><td>'
												+ key + '</td><td>'
												+ obj["value"] + '</td></tr>';
										   
											$('#tbl-device-info > tbody:last').append(template);
										} else {
											arrSensors.push({"key" : key, "value": cont[key]});
											
											var value, min, max;
											
											if(key == "led_info") {
												var btnName = "initialize", btnEnable = "enabled";
												value = ((obj["value"] == "UNKNOWN" || obj["value"] == " ")? 0 : obj["value"]);
												min = 0;
												max = 200;

												if(subscriptionId != "") {
													gBtnLed = "disabled";
													gBtnText = "initialized";
												} 
												$("div#div-sensor-item").append('<div class="portlet span-1 ui-widget ui-widget-content ui-corner-all">		\
													<div class="portlet-header">' + titleMap[key] + '</div>	\
													<div class="portlet-content">	\
														<div class="m-r-md inline">	\
															<input type="text" value="' + value +  '" class="dial m-r" data-fgColor="#ED5565" data-width="85" data-height="85" data-angleOffset=-125 data-angleArc=250 data-min="' + min + '" data-max="' + max + '" /> \
															<button type="button" ' + gBtnLed + ' class="btn btn-xs btn-danger" style="padding-left: 12px; padding-right:12px; position: relative; left: 4px;">&nbsp;' + gBtnText + '&nbsp;</button> \
														</div> \
													</div>	\
												</div>');
												
												$("#" +key).bind('click', function(e) {
														gBtnLed = "disabled";
														e.target.disabled = true;
														console.log(e);
														gBtnText = "initialized";
														e.target.innerText = "initialized";
														var jsonObj = {
																	"description": "A subscription to get info about " + entityId,
																	"subject": {
																		"entities": [
																			{
																				"id": entityId,
																				"type": "potSensor"
																			}
																		],
																		"condition": {
																			"attrs": [
																				subscriptionAttr
																			]
																		}
																	},
																	"notification": {
																		"attrs": [
																			subscriptionAttr
																		],
																		"httpCustom": {
																			"url": "${fiware_agent_url}",
																			"qs": {
																				"i": entityId,
																				"k": "${fiware_agent_key}",
																				"n": entityId
																			},
																			"headers": {
																				"Fiware-Service": "${fiware_service}",
																				"Fiware-ServicePath": "${fiware_service_path}"
																			}
																		}
																	},
																	"expires": "2018-04-05T14:00:00.00Z",	
																	"throttling": 5
																};
														 
															$.ajax({
																type: 'POST',
																url: './subscribe.do',
																dataType: 'json',
																data: JSON.stringify(jsonObj),
																success: function(result) {
																	if(result.result == 0) {
															            alert("성공적으로 LED 제어를 초기화 하였습니다.");
																		
																		
																		
																	} else {
																		alert('Error occurred : ' + result.content);
																	}
																}
															});


													
												});
											}

											if(key == "led_status") continue;

											if(obj["type"] == "string") {
												
												value = obj["value"];

												if(key == "temp") {
													minValue = -30;
													maxValue = 70;
												} else if(key = "humid") {
													minValue = 0;
													maxValue = 100;
												} else {
													minValue = -100;
													maxValue = 100;
												}
											
												$("div#div-sensor-item").append('<div class="portlet span-1 ui-widget ui-widget-content ui-corner-all" style="width: 200px; height: 164px;">	\
														<div class="portlet-header">' + titleMap[key] + '</div>	\
														<div class="portlet-content">	\
															<div id="g_' + key + '" style="margin-top: -15px;"></div>	\
														</div>	\
													</div>');

												g1 = new JustGage({
													id: "g_" + key,
													value: value,
													min: minValue,
													max: maxValue
												});
											}

										}
									} 
									
								}

								$('.portlet')
									.addClass('ui-widget ui-widget-content ui-corner-all')
									.find('.portlet-header')
										.addClass('ui-widget-header ui-corner-all')
										.prepend('<span class="ui-icon ui-icon-minusthick"></span>')
									.end() ;
								//	.find('.portlet-content')
								//		.text('Lorem ipsum dolor sit amet, consectetuer adipiscing elit');

								$('.portlet-header .ui-icon').on('click', function() {
									$(this).toggleClass('ui-icon-minusthick ui-icon-plusthick');
									$(this).closest('.portlet').toggleClass('portlet-minimized');
								});

								$(".dial").knob();
								

							} else {
								alert('Error occurred : ' + result.content);
							}
							
						}
					});
	};
	
	$(document).ready(function () {
		//init();

		$('.sortable').sortable().disableSelection();

		var entityList = ${entity_list};
		var subscriptionList = ${sub_list};
		var entity, subscription, entityLink, timeInstant, createDate;
		var timeoutId;
		var g_entityId;
		
		for(var i = 0; i < entityList.length; i++) {
			entity = entityList[i];
			timeInstant = entity['TimeInstant'];
			createDate = (timeInstant == undefined ? '' : timeInstant['value']);
			
			if(createDate == '') {
				entityLink = entity['id'];
			} else {
				var subId = "", subEntityId;
				
				for(var j = 0; j < subscriptionList.length; j++) {
					subscription = subscriptionList[j];
					
					//subId = subscription.id;
					subEntityId = subscription['subject']['entities'][0]['id'];
					
					if(subEntityId == entity['id']) {
						console.log("subEntityId = " + subEntityId + "\n");
						subId = subscription.id;
						break;
					}
				}

				entityLink = '<a href="#" class="entity_id" id="' + subId + '">' + entity['id'] + '</a>';
			}

			template = '<tr><td>'
                + i + '</td><td>'
                + entityLink + '</td><td>'
                + entity['type'] + '</td><td>'
                + createDate + '</td></tr>';
           
			$('#tbl-main-res > tbody:last').append(template);
		}
		
		$("#btn-refresh").bind("click", function(e) {
			location.reload();
		});

		$("a.entity_id").bind("click", function(e) {

			var entityId = e.target.innerText;
			var subscriptionId = e.target.id;
			var actuateLedValue = -1;
			
			g_entityId = entityId;
			
			$("div#div-sensor-item").empty();

			clearTimeout(timeoutId);
			$("div#div-init-text").remove();

			entity_detail_func(entityId, subscriptionId);
			
			(function poll() {
				timeoutId = setTimeout(function() {
					console.log("g_entityId = " + g_entityId + ", entityId = " + entityId);
					$.ajax({
						type: 'GET',
						url: './entities.do',
						dataType: 'json',
						data: {'id': entityId},
						success: function(result) {
							
							$("div#div-sensor-item").empty();

							if(result.result == 0) {
								
								var cont = $.parseJSON(result.content);
								var arrSensors = [];

								var deviceTable = '<div class="portlet span-4 ui-widget ui-widget-content ui-corner-all">	'
									+ '<div class="portlet-header">Device Info</div>'
									+ '<div class="portlet-content" style="padding: 14px 14px 0 14px;">'
									+	'<div class="table-responsive">'
									+		'<table class="table table-bordered table-striped" id="tbl-device-info">'
									+			'<thead>'
									+			'<tr>'	
									+				'<th>Attribute</th>'
									+				'<th>Value</th>'
									+			'</tr>'	
									+			'</thead>'		
									+			'<tbody>'	
									+               '<tr><td>Device ID</td><td>' + entityId + '</td></tr>'
									+			'</tbody>'
									+		'</table>'
									+	'</div>'
									+ '</div>'	
									+ '</div>';
								
								$("div#div-sensor-item").append(deviceTable);

								var titleMap = {
									'humid': 'Humidity [Unit: %]',
									'temp': 'Temperature [Unit: ℃]',
									'led_info': 'LED Control'
								};

								for(var key in cont) {
									if(jQuery.isPlainObject(cont[key])) {
										var obj = cont[key];

										if(!jQuery.isNumeric( obj["value"] ) && obj["type"] == "string" && key.indexOf("_info") < 0 && key.indexOf("_status") < 0 ) { 
											
											template = '<tr><td>'
												+ key + '</td><td>'
												+ obj["value"] + '</td></tr>';
										   
											$('#tbl-device-info > tbody:last').append(template);
										} else {
											arrSensors.push({"key" : key, "value": cont[key]});
											
											var subscriptionAttr, value, min, max;
											
											if(key == "led_info") {
												
												subscriptionAttr = "led_info";
												value = ((obj["value"] == "UNKNOWN" || obj["value"] == " ")? 0 : obj["value"]);
												min = 0;
												max = 200;
												
												value = (actuateLedValue == -1 ? value : actuateLedValue);

												$("div#div-sensor-item").append('<div class="portlet span-1 ui-widget ui-widget-content ui-corner-all">		\
													<div class="portlet-header">' + titleMap[key] + '</div>	\
													<div class="portlet-content">	\
														<div class="m-r-md inline">	\
															<input type="text" value="' + value +  '" class="dial m-r" data-fgColor="#ED5565" data-width="85" data-height="85" data-angleOffset=-125 data-angleArc=250 data-min="' + min + '" data-max="' + max + '" /> \
															<button type="button" ' + gBtnLed + ' class="btn btn-xs btn-danger" id="' + key + '"  style="padding-left: 12px; padding-right:12px; position: relative; left: 4px;">&nbsp;' + gBtnText + '&nbsp;</button> \
														</div> \
													</div>	\
												</div>');

												$("#" +key).bind('click', function(e) {
														gBtnLed = "disabled";
														e.target.disabled = true;
														console.log(e);
														gBtnText = "initialized";
														e.target.innerText = "initialized";
														var jsonObj = {
																	"description": "A subscription to get info about " + entityId,
																	"subject": {
																		"entities": [
																			{
																				"id": entityId,
																				"type": "potSensor"
																			}
																		],
																		"condition": {
																			"attrs": [
																				subscriptionAttr
																			]
																		}
																	},
																	"notification": {
																		"attrs": [
																			subscriptionAttr
																		],
																		"httpCustom": {
																			"url": "${fiware_agent_url}",
																			"qs": {
																				"i": entityId,
																				"k": "${fiware_agent_key}",
																				"n": entityId
																			},
																			"headers": {
																				"Fiware-Service": "${fiware_service}",
																				"Fiware-ServicePath": "${fiware_service_path}"
																			}
																		}
																	},
																	"expires": "2018-04-05T14:00:00.00Z",	
																	"throttling": 5
																};
														 
															$.ajax({
																type: 'POST',
																url: './subscribe.do',
																dataType: 'json',
																data: JSON.stringify(jsonObj),
																success: function(result) {
																	if(result.result == 0) {
															            alert("성공적으로 LED 제어를 초기화 하였습니다.");
																		
																		
																		
																	} else {
																		alert('Error occurred : ' + result.content);
																	}
																}
															});


													
												}); 
											}

											if(key == "led_status") continue;

											if(obj["type"] == "string") {
												
												value = obj["value"];
												
												console.log("===== value = " + value + "\n");

												if(key == "temp") {
													minValue = -30;
													maxValue = 70;
												} else if(key = "humid") {
													minValue = 0;
													maxValue = 100;
												} else {
													minValue = -100;
													maxValue = 100;
												}
											/*	
												$("div#div-sensor-item").append('<div class="portlet span-1 ui-widget ui-widget-content ui-corner-all">	\
														<div class="portlet-header">' + titleMap[key] + '</div>	\
														<div class="portlet-content">	\
															<div class="m-r-md inline">	\
																<input type="text" value="' + value + '" class="dial m-r" data-fgColor="#1AB394" data-width="85" data-height="85" data-angleOffset=-125 data-angleArc=250 data-min="' + min + '" data-max="' + max + '" readonly />	\
															</div>	\
														</div>	\
													</div>');
											 */
												$("div#div-sensor-item").append('<div class="portlet span-1 ui-widget ui-widget-content ui-corner-all" style="width: 200px; height: 164px;">	\
														<div class="portlet-header">' + titleMap[key] + '</div>	\
														<div class="portlet-content">	\
															<div id="g_' + key + '" style="margin-top: -15px;"></div>	\
														</div>	\
													</div>');

												g1 = new JustGage({
													id: "g_" + key,
													value: value,
													min: minValue,
													max: maxValue
												});
											}

										}
									} 
									
								}

								$('.portlet')
									.addClass('ui-widget ui-widget-content ui-corner-all')
									.find('.portlet-header')
										.addClass('ui-widget-header ui-corner-all')
										.prepend('<span class="ui-icon ui-icon-minusthick"></span>')
									.end() ;
								//	.find('.portlet-content')
								//		.text('Lorem ipsum dolor sit amet, consectetuer adipiscing elit');

								$('.portlet-header .ui-icon').on('click', function() {
									$(this).toggleClass('ui-icon-minusthick ui-icon-plusthick');
									$(this).closest('.portlet').toggleClass('portlet-minimized');
								});

								$(".dial").knob({
									'release': function(v) {
										console.log(v);
										
										if(gBtnText == "initialize") {
											alert("먼저 초기화버턴을 클릭하여 초기화해 주세요.");
											return;
										} else {
											$.ajax({
												type: 'POST',
												url: './actuate.do',
												dataType: 'json',
												data: {"id" : entityId, "attr": "led_info", "value": v},
												success: function(result) {
													if(result.result == 0) {
														alert("LED 제어를 성공적으로 처리하였습니다.");
														
														actuateLedValue = v;
														
													} else {
														alert('Error occurred : ' + result.content);
													}
												}
											});
	
										}

										
									}
								});
								

							} else {
								alert('Error occurred : ' + result.content);
							}


						}, complete: poll
					});
				}, 2000);
				
				if(g_entityId != entityId) {
					console.log("##################### g_entityId != entityId ###############\n");
					clearTimeout(timeoutId);
				}
			})();
					
			
		}); 
		
	});
</script>


</body>

</html>
