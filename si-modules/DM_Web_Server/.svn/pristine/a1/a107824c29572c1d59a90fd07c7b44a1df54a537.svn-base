
// - 페이지 로딩시 1회 수행
var frm_updateNotiOption = null;
var dlg_updateNotiOption = null;

function initUI() {
	console.log("initUI called");
	
	// UI Update
	if (deviceModelId != "") {
		refreshDeviceModelInfo(deviceModelId);
		
		
	} else {}
	dlg_updateNotiOption = $( "#dialog-updateNotiOption" ).dialog({
		  autoOpen: false,
		  height: 320,
		  width: 300,
		  modal: true,
		  buttons: {
		    "저장": function() {
		    	dlg_updateNotiOption.find( "form" ).submit();
		    },
		    "취소": function() {
		    	dlg_updateNotiOption.dialog( "close" );
		    }
		  },
		  close: function() {
			  frm_updateNotiOption[ 0 ].reset();
		  }
		});
		
		frm_updateNotiOption = dlg_updateNotiOption.find( "form" ).on( "submit", function( event ) {
		      //event.preventDefault();
		});	
		
	
}



function refreshMoList() {
	console.log("refreshMoList called - deviceModelId:"+deviceModelId);
	
	var context = {"oui":oui, "modelName": modelName, "handler": getMoListHandler, "manualRenderer": resItemRenderer};
	
	hdb_get_device_profile(context, false);
}

function refreshDeviceModelInfo(deviceModelId) {
	console.log("refreshDeviceModelInfo called - deviceModelId:"+deviceModelId);
	
	var context = { "param": { "id":deviceModelId }, "handler":getDeviceModelInfoHandler }
	hdb_get_deviceModel(context, false);
}
/***************************************noti****************************************/
/*function refreshNotiConditionList() {
	console.log("refreshNotiConditionList called - moProfileId:"+moProfileId);
	
	var context = {"moProfileId":moProfileId,"conditionType":conditionType, "condition":condition, "handler": getNotiConditionInfoHandler};
	
	hdb_get_notiCondition_profile(context, false);
}*/

function refreshNotiConditionInfo(moProfileId) {
	console.log("refreshNotiConditionInfo called - moProfileId:" + moProfileId);
	
	var context = { "param": { "moProfileId":moProfileId }, "handler":getNotiConditionInfoHandler }
	hdb_get_noti_condition(context, false);
}
/****************************************\noti***************************************/

var ERROR_GRADE = ["NORMAL", "MINOR", "MAJOR", "CRITICAL", "FATAL"];
function resItemRenderer(id, profile, context) {
	console.log("resItemRenderer called - id:"+id +":"+ profile['resourceUri']);

	/*console.log("노티옵션 : " + profile['notiType']);*/
/******************************NOTITYPE*********************************/	
	switch (id) {
		case 'notiType' :
			var td = "<td class='profile_resource'></td>";
			if (profile['notiType'] == 0) {
				var text = "NONE";
				td = "<td class='profile_resource'>"+text+"</td>";
			} else if (profile['notiType'] == 1) {
				var text = "ON CHANGE";
				td = "<td class='profile_resource'>"+text+"</td>";
			} else if (profile['notiType'] == 2) {
				td = "<td class='profile_resource'><button class='noti_option_update' moProfileId='"+profile['id']+"'>수정</button></td>";
			} else if (profile['notiType'] == 3) {
				var text = "NO UPDATE";
				td = "<td class='profile_resource'>"+text+"</td>";
			}
			break;
			
		case 'optionData' :
			var td = "<td class='profile_resource'>-</td>";
			if (profile['hasOptionData'] == 'Y') {
				var text = "";
				for (var optionId in profile.optionDataList) {
					var option = profile.optionDataList[optionId];
					text += option.data +": "+option.displayData+"<br>";						
				}
				td = "<td class='profile_resource'  style='text-align:left'>"+text+"</td>";
			} else if (profile['isError'] == 'Y') {
				var text = "";
				for (var errorId in profile.errorCodeList) {
					var error = profile.errorCodeList[errorId];
					text += error.errorCode +": "+error.errorName+" ("+ERROR_GRADE[error.errorGrade]+")<br>";						
				}
				td = "<td class='profile_resource' style='text-align:left'>"+text+"</td>";
			}
			break;
	}
	return td;

	/*
	var toolTip = "";
	if (fn == "hasOptionData" && profile[fn] == "Y") {
		for (optionId in profile.optionDataList) {
			var option = profile.optionDataList[optionId];
			toolTip += option.data +":"+option.displayData+",";						
		}
		toolTip = toolTip.substring(0,toolTip.length-1);
	}
	*/
}

function getDeviceModelInfoHandler(msg, context) {
	console.log("getDeviceModelInfoHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));
	
	if (msg.content.info == null) {
		alert("디바이스 모델 정보가 존재하지 않습니다. 모델정보를 다시 선택해주세요.");
		window.location.href = contextPath + "/information/deviceModel.do";
	}
	
	$("#model_manufacturer").text(msg.content.info["manufacturer"]);
	$("#model_oui").text(msg.content.info["oui"]);
	$("#model_modelName").text(msg.content.info["modelName"]);
	$("#model_description").text(msg.content.info["description"]);
	$("#model_createTime").text(msg.content.info["createTime"]);
	$("#model_updateTime").text(msg.content.info["updateTime"]);

	oui = msg.content.info["oui"];
	modelName = msg.content.info["modelName"];
	
	refreshMoList();
}
/**
 * TODO: NotiCondition
 */
/****************************Noti*****************************************************/
var notiList = null;
function getNotiConditionInfoHandler(msg, context) {
	console.log("getNotiConditionInfoHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));
	
	for(var i=0; i<msg.content.list.length; i++) {
		console.log("yyyyyyyyyyyyyyyyyyy" + msg.content.list.length);
		
		var moProfileId = context.param.moProfileId;
		var noti = msg.content.list[i];
		
		console.log("moProfileId: " + moProfileId);
		console.log("noti: " + noti);
		/*var iii = input.length;
		console.log("ddddddddddddddddddddddd " + iii)*/
		
		var notiCon = noti['conditionType'];
		console.log("qqqqqqqqqqqqqqqqqqqqqq " + notiCon);
			
			//var conditionType = noti['conditionType'];
			var condition = noti['condition'];
			//console.log("tttttttttttttttttttttttttttttttttttt" + conditionType);
			console.log("pppppppppppppppppppppppppppppppppppp" + condition);
			/*var id = noti['id'];
			console.log("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + id);
			*/
			var conditionType = "conditionType";
			
			//$("#noti_min").val(noti["condition"]);
			//$("#noti_max").val(noti["condition"]);
			
			/*if (noti['conditionType'] == 'M') {
				$("#noti_min").val(noti["condition"]);
				alert("conditionType - > M");
			} else if (noti['conditionType'] == 'X') {
				$("#noti_max").val(noti["condition"]);
				alert("conditionType - > X");
			} else if (noti['conditionType'] == 'LT') {
				$("#noti_lt").val(noti["condition"]);
				alert("conditionType - > LT");
			} else if (noti['conditionType'] == 'GT') {
				$("#noti_gt").val(noti["condition"]);
				alert("conditionType - > GT");
			} else if (noti['conditionType'] == 'S'){
				$("#noti_s").val(noti["condition"]);
				alert("conditionType - > S");
			}*/
			
			switch (conditionType) {
				case 'conditionType' :
					//var conditionType = noti['conditionType'];
					if (noti['conditionType'] == 'M') {
						$("#noti_min").val(noti["condition"]);
						alert("conditionType - > M");
					} else if (noti['conditionType'] == 'X') {
						$("#noti_max").val(noti["condition"]);
						alert("conditionType - > X");
					} else if (noti['conditionType'] == 'LT') {
						$("#noti_lt").val(noti["condition"]);
						alert("conditionType - > LT");
					} /*else if (noti['conditionType'] == 'GT') {
						$("#noti_gt").val(noti["condition"]);
						alert("conditionType - > GT");
					} else if (noti['conditionType'] == 'S') {
						$("#noti_s").val(noti["condition"]);
						alert("conditionType - > S");
					}*/ break;
			}
		
		
		/*for (var j=0; j<input.length; j++) {
			if (typeof $(input[j]).attr("id") == "undefined") {
				continue;
			}
			var idName = $(input[j]).attr("id").substring(5);
			console.log("yyyyyyyyyyyyyyyyyyyyyyyyy " + idName);
			
		}*/
	}
	dlg_updateNotiOption.dialog("open");
}

/////////////////////////////////////////////////////////S update
/*function updateNotiCondition() {
	console.log("updateNotiCondition called!!");
	
	var condition = $("#noti_min").val();
	
	var context = {"param":{
						"condition":condition
							},
						"handler": updateNotiCoditionHandler};
	
	hdb_update_notiCondition(context, false);
}

function updateNotiCoditionHandler(msg, context) {
	console.log("updateNotiCoditionHandler called");
	console.log("context:" + JSON.stringify(context));
	console.log("msg:" + JSON.stringify(msg));
	
	dlg_updateNotiOption.dialog("close");
}*/
/////////////////////////////////////////////////////////E update
/****************************\Noti*****************************************************/
function getMoListHandler(msg, context) {
	console.log("getMoListHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));

	var ths = $("#mo_table thead th");
	var moBody = $("#mo_table tbody");
	moBody.empty();
	
	// 목록 표시
	for (var i=0; i<msg.content.profile.length; i++) {
		var profile = msg.content.profile[i];
		
		var tr = $("<tr></tr>");
		for (var j=0; j<ths.length; j++) {
			if (typeof $(ths[j]).attr("id") == "undefined") {
				continue;
			} 
			var fn = $(ths[j]).attr("id").substring(3); 	// hd_<fieldName>
			if ($(ths[j]).hasClass("manual_renderer")) {
				tr.append(context.manualRenderer(fn, profile, context));
			} else {
				var text = profile[fn] == null ? "-" : profile[fn];
				tr.append($("<td class='profile_resource'>"+text+"</td>"));				
			}
		}
		moBody.append(tr);
	}
	if (msg.content.profile.length == 0) {
		var tr = $("<tr></tr>");
		tr.append($("<td colspan='"+ths.length+"'>관리 객체 정보 없음</td>"));
		moBody.append(tr);
	}	
	
	// NotiOption Button Click 
	$(".noti_option_update").click(function(evt) {
		console.log("noti_option_update_button clicked");
		
		var moProfileId = parseInt($(evt.srcElement).attr('moProfileId'));
		console.log("moProfileId: "+moProfileId);
		refreshNotiConditionInfo(moProfileId);
		/*
		var noti = findNotiOption(moProfileId);
		
		console.log("moProfileId: "+moProfileId);
		console.log("noti: "+noti);
		//for(var i=0; i<noti.length; i++) {
			
		//}
		$("#noti_type").text(noti["conditionType"]);
		$("#noti_min").val(noti["condition"]);
		
		dlg_updateNotiOption.dialog("open");
		*/
	});
	
}


