// Custom scripts
$(document).ready(function () {
	
	dsr_lwm2m.init();

	console.log("device.status.reboot_lwm2m js initialized");
});


var dsr_lwm2m = {
	divs: null,
	resources: null,
	init: function() {
		dsr_lwm2m.divs = $(".hit-component-dsr_lwm2m");
		dsr_lwm2m.resources = [];
		

		var rbProfile = _ucc.getProfileInfo("/3/-/4");
		var frProfile = _ucc.getProfileInfo("/4/-/5");
		if ((rbProfile == null || typeof rbProfile == "undefined") && 
				(frProfile == null || typeof frProfile == "undefined")) {
			console.error("dsmo undefined profile: /3/-/4, /4/-/5");
			return; 
		}
		
		$.each(dsr_lwm2m.divs, function(idx, div) {
			//dsr_lwm2m.resources.push(_ucc.getResListWithComponentDiv(div));
			dsr_lwm2m.resources = _ucc.getResListWithComponentDiv(div);
			
			$(div).find(".btn_reboot").on("click", dsr_lwm2m.onRebootClick);
			$(div).find(".btn_factory_reset").on("click", dsr_lwm2m.onFactoryResetClick);
		});
		
		_ucc.setResourceHandler("dsr_lwm2m", dsr_lwm2m.resources, dsr_lwm2m.refresh);
		dsr_lwm2m.refresh();
	}, 
	refresh: function(type) {
		console.log("dsr_lwm2m.refresh("+type+") called");
		
		$.each(dsr_lwm2m.divs, function(idx, div) {
			dsr_lwm2m.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		/*
		 /2/-/4 - reboot
		 /2/-/5 - factory reset
		 */
		var rbProfile = _ucc.getProfileInfo("/3/-/4");
		var frProfile = _ucc.getProfileInfo("/4/-/5");

		if (rbProfile == null && typeof rbProfile == "undefined") {
			$(div).find(".btn_reboot").prop("disabled", true);
		}

		if (frProfile == null && typeof frProfile == "undefined") {
			$(div).find(".btn_factory_reset").prop("disabled", true);
		}
	},
	onRebootClick: function(evt) {
		console.log("dsr_lwm2m.onRebootClick(evt)");
		
		bootbox.confirm("디바이스를 리부팅합니다. 계속하시겠습니까?", function(result) {
			  	
				if (result) {
					
					/*
					var data = {"device_id":_ucc.getDeviceInfo().deviceId,"oui":_ucc.getDeviceInfo().oui,"model_name":_ucc.getDeviceInfo().modelName,"sn":_ucc.getDeviceInfo().sn,"resource_uri":"/3/-/4","resource_name":"reboot","ctl_type":"C","ctl_data":"","ctl_result":"","error_code":"0","base_resource_uri":"/3/-/4","conn":"controlHistory" }

					$.ajax({
					  type: "POST",
					  dataType: "json",
					  url: "/hdm/lwm2m/conn.do",
					  contentType: "application/json",
					  data: JSON.stringify(data)
					})
					.done(function(msg) { 
						console.log("insert control history : ",msg);
						var context = {"deviceId":_ucc.getDeviceInfo().deviceId, "uri":"/3/-/4", "handler": dsr_lwm2m.executeResultHandler};		
						dm_reboot(context, false);
					});
					*/
					
					
					insertControlHistory("/3/-/4","reboot","","OK");
					
					var context = {"deviceId":_ucc.getDeviceInfo().deviceId, "uri":"/3/-/4", "handler": dsr_lwm2m.executeResultHandler};		
					dm_reboot(context, false);
				}
		}); 
	},
	onFactoryResetClick: function(evt) {
		console.log("dsr_lwm2m.onFactoryResetClick(evt)");
		
		bootbox.confirm("디바이스를 공장초기화 합니다. 디바이스에 저장된 모든 옵션 및 데이터가 삭제될 수 있습니다. 계속하시겠습니까?", function(result) {
			  	
				if (result == true) {
					var context = {"deviceId":_ucc.getDeviceInfo().deviceId, "handler": dsr_lwm2m.executeResultHandler};		
					dm_factory_reset(context, false);
				}
		}); 
	},
	executeResultHandler: function(msg, context) {
		console.log("dsr_lwm2m.executeResultHandler called ");
		//console.log("msg: "+JSON.stringify(msg));
		
	}
}




