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
	</style>
	
	
    <title>HDM - OneM2M Manager</title>
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
				<h2>oneM2M 통합 리소스 관리</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>oneM2M 서버 관리</li>
					<li class="active"><strong>통합 리소스 관리</strong></li>
				</ol>
			</div>
		</div>
		
        <div class="wrapper wrapper-content animated fadeInRight">
		<!----------------->
		<!-- 컨텐츠 영역 시작 -->
		<!----------------->
		<div class="row">
            <div class="col-lg-12">
                <h2>IN-CSE&nbsp;:&nbsp;[${cse_addr}]</h2>
                <ol class="breadcrumb" style="padding: 10px;margin-bottom: 10px;" id="ol-main-uri">
                    <li>
                        <strong>Resource URI : &nbsp; </strong>
                    </li>
                    
                    <button type="button" class="btn btn-sm btn-primary" data-toggle="modal" data-target="#myModal4" style="float:right;margin-top: -6px;margin-right: -4px;">Create Resource</button>

					<div class="modal inmodal" id="myModal4" tabindex="-1" role="dialog"  aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content animated fadeIn">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
									<h4 class="modal-title">Create Resource</h4>
									<small>oneM2M Resource can be created by this popup window</small>
								</div>
								<div class="modal-body">
									<form class="form-horizontal" name="frmCreate">
										<input type="hidden" name="uri" value="${main_uri}">
										<div class="form-group"><label class="col-lg-2 control-label">URI</label>

											<div class="col-lg-10"><span class="help-block m-b-none">${main_uri}</span>
											</div>
										</div>
										<div class="form-group"><label class="col-lg-2 control-label">From</label>

											<div class="col-lg-10"><input type="text" placeholder="Type in Origin" class="form-control" name="rn"> <span class="help-block m-b-none">type in 'C or 'S' for AE, None for other resources.</span>
											</div>
										</div>
										<div class="form-group"><label class="col-lg-2 control-label">Type</label>

											<div class="col-lg-10"><select class="form-control m-b" name="rt">
										<option>Select</option>
                                        
                                    </select> <span class="help-block m-b-none">Example block-level help text here.</span>
											</div>
										</div>
										<div class="form-group"><label class="col-lg-2 control-label">Content</label>

											<div class="col-lg-10"><textarea  class="form-control" rows="10" name="con"></textarea></div>
										</div>
									</form>
								</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-white" data-dismiss="modal">Close</button>
									<button type="button" class="btn btn-primary" id="btn-create">Create</button>
								</div>
							</div>
						</div>
					</div> <!--  end of div modal inmodal  -->


                </ol>
                
            </div>
            
        </div>
            
		<div class="row">
                <div class="col-lg-4">
                    <div class="ibox float-e-margins" style="min-height:580px;background-color: #fff;">
	                    <div class="ibox-title">
	                        <h3 class="no-margins">Resource Detail</h3>
	                        <div class="ibox-tools" style="margin-top: -20px;">
	                            
	                            <a class="dropdown-toggle" id="icn-edit" href="#" data-toggle="modal" data-target="#myModal3" style="color:#fff; font-size:20px;">
	                                <i class="fa fa-gear"></i>
	                            </a>
	                            
	                        </div>
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

			            <div class="ibox-content" style="max-height: 680px; overflow: auto;">

	                            <p>This windows shows all the attributes for the oneM2M resource from above 'Resource URI'</p>
								<br/>
	                            <div class="table-responsive">
	                                <table class="table table-bordered table-striped" id="tbl-main-res">
	                                    <thead>
	                                    <tr>
	                                        <th>Attributes</th>
	                                        <th>
	                                            Values
	                                        </th>
	                                        
	                                    </tr>
	                                    </thead>
	                                    <tbody>
	                                    <tr>
	                                        <th>Grid behavior</th>
	                                        <td>Horizontal at all times</td>
	                                        
	                                    </tr>
	                                    <tr>
	                                        <th>Max container width</th>
	                                        <td>None (auto)</td>
	                                        
	                                    </tr>
	                                    <tr>
	                                        <th>Class prefix</th>
	                                        <td>
	                                            <code>.col-xs-</code>
	                                        </td>
	                                        
	                                    </tr>
	                                    <tr>
	                                        <th># of columns</th>
	                                        <td >12</td>
	                                    </tr>
	                                    <tr>
	                                        <th>Max column width</th>
	                                        <td class="text-muted">Auto</td>
	                                        
	                                    </tr>
	                                    <tr>
	                                        <th>Gutter width</th>
	                                        <td >30px (15px on each side of a column)</td>
	                                    </tr>
	                                    <tr>
	                                        <th>Nestable</th>
	                                        <td >Yes</td>
	                                    </tr>
	                                    <tr>
	                                        <th>Offsets</th>
	                                        <td >Yes</td>
	                                    </tr>
	                                    <tr>
	                                        <th>Column ordering</th>
	                                        <td >Yes</td>
	                                    </tr>
	                                    </tbody>
	                                </table>
	                            </div>
	                          <!--  <p>Grid classes apply to devices with screen widths greater than or equal to the breakpoint sizes, and override grid classes targeted at smaller devices. Therefore, applying any
	                                <code>.col-md-</code> class to an element will not only affect its styling on medium devices but also on large devices if a
	                                <code>.col-lg-</code> class is not present.</p> -->
	
	                    </div>    
                    </div>
                </div>
                <div class="col-lg-8">
                    <div class="ibox float-e-margins" style="min-height:580px;background-color: #fff;">
                        <div class="ibox-title">
                            <h3 class="no-margins">Child Resources</h3>
                            
                        </div>
                        <div class="ibox-content" style="max-height: 680px; overflow: auto;">

                            <table class="table table-bordered" id="tbl-ch-res">
                                <thead>
                                <tr>
                                    <th>#</th>
                                    <th>Resource Name</th>
                                    <th>Resource Type</th>
                                    <th>Resource URI</th>
                                    <th style="width:40px;">Function</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>Mark</td>
                                    <td>Otto</td>
                                    <td>@mdo</td>
                                    <td><button type="button" class="btn btn-danger btn-xs">Delete</button></td>
                                </tr>
                                <tr>
                                    <td>2</td>
                                    <td>Jacob</td>
                                    <td>Thornton</td>
                                    <td>@fat</td>
                                    <td><button type="button" class="btn btn-danger btn-xs">Delete</button></td>
                                </tr>
                                <tr>
                                    <td>3</td>
                                    <td>Larry</td>
                                    <td>the Bird</td>
                                    <td>@twitter</td>
                                    <td><button type="button" class="btn btn-danger btn-xs">Delete</button></td>
                                </tr>
                                </tbody>
                            </table>

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
	var resourceType = ['Select Resource Type',
	                    'accessControlPolicy',
	                    'AE',
	                    'container',
	                    'contentInstance',
	                    'CSEBase',
	                    'delivery',
	                    'eventConfig',
	                    'execInstance',
	                    'group',
	                    'locationPolicy',
	                    'm2mServiceSubscriptionProfile',
	                    'mgmtCmd',
	                    'mgmtObj',
	                    'node',
	                    'pollingChannel',
	                    'remoteCSE',
	                    'request',
	                    'schedule',
	                    'serviceSubscribedAppRule',
	                    'serviceSubscribedNode',
	                    'statsCollect',
	                    'statsConfig',
	                    'subscription',
	                    'semanticDescriptor',
	                    'notificationTargetMgmtPolicyRef',
	                    'notificationTargetPolicy',
	                    'policyDeletionRules',
	                    'flexContainer',
	                    'timeSeries',
	                    'timeSeriesInstance',
	                    'role',
	                    'token',
	                    'trafficCharacteristics',
	                    'trafficPattern',
	                    'dynamicAuthorizationConsultation'
	                    ];
			
	var attributeName = {
		'rn' : 'resourceName',
        'ty' : 'resourceType',
		'ri' : 'resourceID',
		'ct' : 'creationTime',
		'lt' : 'lastMofifiedTime',
		'cst' : 'cseType',
		'csi' : 'CSE-ID',
		'srt' : 'supportedResourceType',
		'poa' : 'pointOfAccess',
		'apn' : 'appName',
		'api' : 'App-ID',
		'aei' : 'AE-ID',
		'rr' : 'requestReachability',
        'at' : 'announceTo',
		'lbl' : 'label',
		'et' : 'expirationTime',
		'st' : 'stateTag',
		'cr' : 'creator',
		'cni' : 'currentNrOfInstances',
		'cbs' : 'currentByteSize',
		'nu' : 'notificationURI',
		'enc' : 'eventNotificationCriteria',
		'pcu' : 'pollingChannelURI',
		'pi' : 'parentID',
		'cs' : 'contentSize',
		'con' : 'content',
		'cb' : 'CSEBase'

	};
	var mainUri ='${main_uri}';
	var jsonStr = ${content};
	
	var init = function() {
		$('#tbl-ch-res > tbody').empty();
		$('#tbl-main-res > tbody').empty();
		$('#ol-main-uri > li').empty();
		$('select[name=rt] option').empty();
	};
	
	$(document).ready(function () {
		
		init();
		
		var jsonBody = null;
		for(key in jsonStr) {
			jsonBody = jsonStr[key];
		}
		
		var arrChildRes = jsonBody['ch'];
		var arrUriItems = mainUri.split('/');
		
		
		if(arrChildRes == undefined){
			$('#tbl-ch-res > tbody:last').append('<tr><td colspan="5"><center>No child resource is dectected.</center></td></tr>');
		}

		$("#ol-main-uri").append('<li><strong>Resource URI : &nbsp;</strong></li>');

		for(key in resourceType) {
			var selected = '';
			rt = resourceType[key];
			if(key == 0) selected = 'selected';
			else selected = '';
			
			$('select[name="rt"]').append('<option value=' + key + ' ' + selected + '>' + rt + '</option>');
		}

		var parentUri = "";
		for (var i in arrUriItems) {
			
			if(i > 0) {
				parentUri = parentUri + "/" + arrUriItems[i];
				if(i == 1) {
					$("#ol-main-uri").append('<li><a>' + arrUriItems[i] + '</a></li>');
				} else {
					$("#ol-main-uri").append('<li><a href="./manager.do?uri=' + parentUri + '">' + arrUriItems[i] + '</a></li>');
				}
				
			}
		}
		
		var row, template = '';
		
		for(key in jsonBody) {
			if(key != 'ch') {
				if(key == 'ty') {
					row = resourceType[jsonBody[key]] + '(' + jsonBody[key] + ')';
					$("#span-edit-rt").html(row);
				} else {
					row = jsonBody[key];
				}
				
				template = '<tr><th>'
                   + attributeName[key] + '</th><td>'
                   + row + '</td></tr>';
            
                $('#tbl-main-res > tbody').append(template);
			}
		}
		
		for(var i in arrChildRes) {
			row = arrChildRes[i];
			template = '<tr><td>'
                + i + '</td><td>'
                + row['nm'] + '</td><td>'
                + resourceType[row['typ']] + '</td><td>'
                + '<a href="./manager.do?uri=' + row['value'] + '">' + row['value'] + '</a></td><td><button type="button" class="btn btn-danger btn-xs" value="' + row['value'] + '">Delete</button></td></tr>';
           
			$('#tbl-ch-res > tbody:last').append(template);
		}

		$("button.btn-danger").bind("click", function(e) {
			var delUri = e.target.value;
			

			$.ajax({
				type: 'POST',
				url: './delete.do',
				dataType: 'json',
				data: {'uri': delUri},
				success: function(result) {		
					console.log(result);
					if(result.result == 0) {
						alert('resource[' + delUri + '] is normally deleted.');

						$(location).attr('href', 'manager.do?uri=' + mainUri );
					} else {
						alert('Error occurred : ' + result.content);
					}
					
				}
			});
			
		});

		
	//	$("#icn-edit").bind("click", function(e) {
	//		alert('rrewrfefgthgrhtgftr');
	//	});
		$("#btn-create").bind("click", function(e) {
			var rnText = $("input[name=rn]").val(),
				conTextArea = $("textarea[name=con]").val(),
				rtSelect = $("select[name=rt]").val();

			if(rtSelect == 0) {
				alert('Select Resource Type');
				$("select[name=rt]").focus();
				return false;
			}

			if(rtSelect == 2 && rnText == '') {
				alert('Type in resource name for AE');
				$("input[name=rn]").focus();
				return false;
			}
			
			if(conTextArea == '') {
				alert('Type in content');
				$("textarea[name=con]").focus();
				return false;
			}
			
			$.ajax({
				type: 'POST',
				url: './create.do',
				dataType: 'json',
				data: $("form[name=frmCreate]").serialize(),
				async: true,
				success: function(result) {		
					if(result.result == 0) {
						console.log('result.content ===> ' + result.content);
						if(result.content == 'Created') {
							alert("Your resource is created successfully.");
							$(location).attr('href', 'manager.do?uri=' + mainUri );
						} else {
							alert('Error occurred : ' + result.content);
						}
						
					} else {
						alert('Error occurred : ' + result.content);
					}
				}
			}); 

		});
		

		$("#btn-update").bind("click", function(e) {
			var rnText = $("input[name=ed_rn]").val(),
				rtVal = $("#span-edit-rt").text(),
				conTextArea = $("textarea[name=ed_con]").val();

			
			if(conTextArea == '') {
				alert('Type in content');
				$("textarea[name=con]").focus();
				return false;
			}
			
			$.ajax({
				type: 'POST',
				url: './update.do',
				dataType: 'json',
				data: $("form[name=frmUpdate]").serialize(),
				async: true,
				success: function(result) {		
					if(result.result == 0) {
						console.log('result.content ===> ' + result.content);
						if(result.content == 'OK') {
							alert("Your resource is updated successfully.");
							$(location).attr('href', 'manager.do?uri=' + mainUri );
						} else {
							alert('Error occurred : ' + result.content);
						}
						
					} else {
						alert('Error occurred : ' + result.content);
					}
				}
			}); 

		});
		
		
	});
</script>


</body>

</html>
