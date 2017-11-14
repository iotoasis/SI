/*$(document).ready(function () {
	device.initialize();
});

var device = (function(){
	
	var initialize = function(){
		console.log("initialized !!!!!!");
		
		initUIComponents();
		
	};
	
	var initUIComponents = function(){
		addEventHandler();
		//createDialog();
	};
	
	var addEventHandler = function(){
		console.log("addEventHandler called");
		*//** oneM2M 디바이스 검색 *//*
		$("#oneM2MSearch").click(function() {
			$("#oneM2MDeviceListDialog").dialog("open");
		});
	};
	
	return {
		initialize: initialize
	};
})();*/


function initUI2() {
	console.log("initUI called");
	
	/** oneM2M 디바이스 검색 다이얼로그 
	dlg_oneM2MDeviceSearch = $("#dialog-oneM2MDeviceSearch").dialog({
		autoOpen: false,
		height: 300,
		width: 500,
		modal: true,
		buttons: {
			"닫기": function() {
				dlg_oneM2MDeviceSearch.dialog("close");
			}
		},
		close: function() { 
			frm_oneM2MDeviceSearch[0].reset();
		}
	});
	*/
	
	$("#oneM2MSearch").click(function() {
		$("#myModal").modal('show');
	});
	
	/*
	frm_oneM2MDeviceSearch = dlg_oneM2MDeviceSearch.find("form").on("submit", function(event) {
		//event.preventDefault();
	});
	*/	
	
	/** oneM2M 디바이스 aeId 검색 */
	$("#oneM2MBtnSearch").click(function() {
		oneM2MDeviceList();
	});
	
	/** 디바이스 등록 */
	$("#btnRegister").click(function() {
		oneM2MRegister();
	});
	
}

function oneM2MDeviceList() {
	var sn = $("#oneM2MSerialNo").val();
	console.log("oneM2MSerialNo : " + sn);
	
	if (sn.length == 0) {
		alert("시리얼 번호를 입력하세요.");
		return;
	}
	
	//var context = {"e":[{"n":sn}], "handler":oneM2MDeviceListHandler};
	var context = {"sn":sn, "handler":oneM2MDeviceListHandler};
	dm_discovery(context, false);
	
	
}

function oneM2MDeviceListHandler(msg, context) {
	console.log("oneM2MDeviceListHandler called");
	console.log("context:"+JSON.stringify(context));
	console.log("msg:"+JSON.stringify(msg));
	
	//var deviceList = msg.content.list;
	var snData = context.sn;
	var deviceDataListHtml = "";
	var devIdList = msg.content.devId[snData];

	
	if (devIdList != null) {
		for (i=0; i < devIdList.length; i++) {
			var id = "selectedAEID_"+i;
			deviceDataListHtml += "<tr>";
			deviceDataListHtml += "<td>"+devIdList[i]+"</td>";
			deviceDataListHtml += "<td><input id='selectedAEID_"+i+"' type='button' onclick='selectedBtn(\""+snData+"\", \""+devIdList[i]+"\")' value='선택' /></td>";
			deviceDataListHtml += "</tr>";
		}
	} else {
		deviceDataListHtml += "<tr>";
		deviceDataListHtml += "<td colspan='2'>디바이스 정보 없음</td>";
		deviceDataListHtml += "</tr>";
	}
	
	$("#oneM2MDeviceList").html(deviceDataListHtml);

}

function selectedBtn(sn, deviceId) {
	console.log("selectedBtn called");
	
	$("#serialNo").val(sn);
	$("#oneM2MAEID").val(deviceId);
	dlg_oneM2MDeviceSearch.dialog("close");
}

/**  */
function oneM2MRegister(context, loading) {
	var tokens = $("#search_deviceModel").val().split("|");
	var id = tokens[0];
	var oui = tokens[1];
	var modelName = tokens[2];
	var sn = $("#serialNo").val();
	var authId = $("#authId").val();
	var authPwd = $("#authPwd").val();
	var aeId = $("#oneM2MAEID").val();
	console.log("aeId : " + aeId);
	
	/*if (sn.length == 0 || aeId.length == 0) {
		alert("시리얼 번호를 입력하세요.");
		return;
	}*/
	var context = { "param":{ "deviceModelId":id, "sn":sn, "authId":authId, "authPwd":authPwd, "extDeviceId":aeId}, "handler":regDeviceHandler, "errorHandler": regDeviceErrorHandler} ;
	hdb_register_oneM2MDevice(context, false);
	
}


function regDeviceErrorHandler(msg, context) {
	console.log("regDeviceErrorHandler called ");
	
	if (msg.errorCode == "102") {
		alert("중복된 디바이스ID가 이미 존재합니다. 시리얼번호를 확인 후 다시 시도해주세요.");
		return true;
	} 
	return false;
}

function regDeviceHandler(msg, context) {
	console.log("regDeviceHandler called ");
	
	if (msg.result != 0) {
		if (msg.errorCode == "102") {
			alert("중복된 디바이스ID가 이미 존재합니다. 시리얼번호를 확인 후 다시 시도해주세요.");
			return;
		}
	} else {
		alert("디바이스 등록에 성공했습니다.");
		$("#serialNo").val("");
		$("#authId").val("");
		$("#authPwd").val("");
		$("#oneM2MAEID").val("");
	}
	return true;
}



