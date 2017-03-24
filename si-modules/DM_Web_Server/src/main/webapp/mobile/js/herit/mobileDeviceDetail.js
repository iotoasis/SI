// - 페이지 로딩시 1회 수행
function initUI() {
	console.log("initUI called");
	
	$("#search_deviceModel").change(function() {
		var deviceModel = $("#search_deviceModel").val();
		if (deviceModel == 'choose') {
			return;
		} else {
			deviceModelInfo();
		}
	});
}
/** 디바이스 선택 및 조회 */
function deviceModelInfo() {
	console.log("deviceModelInfo called");
	var tokens = $("#search_deviceModel").val().split("|");
	var id = tokens[0];
	console.log("deviceModelId >>>>>>>>>>>>>>>>>>>>>" + id);
	
	/*var context = {"param":{"id":id}, "handler":deviceModelInfoHandler};
	hdb_get_deviceModel(context, false);*/
	
	$.ajax({
	  type: "GET",
	  dataType: "json",
	  url: "/mobile/api/hdp/deviceModel/info/get.do?id="+id
	})
	.done(function(msg) { 
		deviceModelInfoHandler(msg);
		});
	/*window.location.href = contextPath + "/mdevice/detail.do?id="+id;*/
}

function deviceModelInfoHandler(msg) {
	console.log("deviceModelInfoHandler called");
	console.log(JSON.stringify(msg));
	
	try {
		if (msg.result == 0) {
			var deviceModel = msg.content.info;
			$(".mobile_box").css('display', 'block');
			$("#infoTable").empty();
			
			$("#infoTable").append("<tr><td class='detailTableTd'>ID</td><td>"+deviceModel.id+"</td></tr>");
			$("#infoTable").append("<tr><td class='detailTableTd'>OUI</td><td>"+deviceModel.oui+"</td></tr>");
			$("#infoTable").append("<tr><td class='detailTableTd'>MANUFACTURER</td><td>"+deviceModel.manufacturer+"</td></tr>");
			$("#infoTable").append("<tr><td class='detailTableTd'>MODEL_NAME</td><td>"+deviceModel.modelName+"</td></tr>");
			$("#infoTable").append("<tr><td class='detailTableTd'>DEVICE_TYPE</td><td>"+deviceModel.deviceType+"</td></tr>");
			$("#infoTable").append("<tr><td class='detailTableTd'>ICON_URL</td><td class='detailTableTd_overFlow'>"+deviceModel.iconUrl+"</td></tr>");
			$("#infoTable").append("<tr><td class='detailTableTd'>DESCRIPTION</td><td>"+deviceModel.description+"</td></tr>");
		}
	} catch (e) {
		console.log("exception in deviceModelInfoHandler");
		console.log(e);
	}
}