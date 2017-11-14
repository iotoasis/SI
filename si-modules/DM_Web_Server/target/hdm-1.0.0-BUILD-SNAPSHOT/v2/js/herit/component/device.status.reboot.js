// Custom scripts
$(document).ready(function () {
	
	dsr.init();

	console.log("device.status.reboot js initialized");
});


var dsr = {
	divs: null,
	resources: null,
	init: function() {
		dsr.divs = $(".hit-component-dsr");
		dsr.resources = [];
		

		var rbProfile = _ucc.getProfileInfo("/2/-/4");
		var frProfile = _ucc.getProfileInfo("/2/-/5");
		if ((rbProfile == null || typeof rbProfile == "undefined") && 
				(frProfile == null || typeof frProfile == "undefined")) {
			console.error("dsmo undefined profile: /2/-/4, /2/-/4");
			return; 
		}
		
		$.each(dsr.divs, function(idx, div) {
			//dsr.resources.push(_ucc.getResListWithComponentDiv(div));
			dsr.resources = _ucc.getResListWithComponentDiv(div);
			
			$(div).find(".btn_reboot").on("click", dsr.onRebootClick);
			$(div).find(".btn_factory_reset").on("click", dsr.onFactoryResetClick);
		});
		
		_ucc.setResourceHandler("dsr", dsr.resources, dsr.refresh);
		dsr.refresh();
	}, 
	refresh: function(type) {
		console.log("dsr.refresh("+type+") called");
		
		$.each(dsr.divs, function(idx, div) {
			dsr.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		/*
		 /2/-/4 - reboot
		 /2/-/5 - factory reset
		 */
		var rbProfile = _ucc.getProfileInfo("/2/-/4");
		var frProfile = _ucc.getProfileInfo("/2/-/5");

		if (rbProfile == null && typeof rbProfile == "undefined") {
			$(div).find(".btn_reboot").prop("disabled", true);
		}

		if (frProfile == null && typeof frProfile == "undefined") {
			$(div).find(".btn_factory_reset").prop("disabled", true);
		}
	},
	onRebootClick: function(evt) {
		console.log("dsr.onRebootClick(evt)");
		
		bootbox.confirm("디바이스를 리부팅합니다. 계속하시겠습니까?", function(result) {
			  	
				if (result == true) {
					var context = {"deviceId":_ucc.getDeviceInfo().deviceId, "handler": dsr.executeResultHandler};		
					dm_reboot(context, false);
				}
		}); 
	},
	onFactoryResetClick: function(evt) {
		console.log("dsr.onFactoryResetClick(evt)");
		
		bootbox.confirm("디바이스를 공장초기화 합니다. 디바이스에 저장된 모든 옵션 및 데이터가 삭제될 수 있습니다. 계속하시겠습니까?", function(result) {
			  	
				if (result == true) {
					var context = {"deviceId":_ucc.getDeviceInfo().deviceId, "handler": dsr.executeResultHandler};		
					dm_factory_reset(context, false);
				}
		}); 
	},
	executeResultHandler: function(msg, context) {
		console.log("dsr.executeResultHandler called ");
		//console.log("msg: "+JSON.stringify(msg));
		
	}
}




