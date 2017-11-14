// Custom scripts
$(document).ready(function () {

	dsls_lwm2m.init();
	
	console.log("device.status.log_switch js initialized");
});


var dsls_lwm2m = {
	divs: null,
	resources: null,
	init: function() {
		dsls_lwm2m.divs = $(".hit-component-dsls_lwm2m");
		dsls_lwm2m.resources = [];
		$.each(dsls_lwm2m.divs, function(idx, div) {
			//dsls_lwm2m.resources.push(_ucc.getResListWithComponentDiv(div));
			dsls_lwm2m.resources = _ucc.getResListWithComponentDiv(div);
			
			var swt = $(div).find(".dsls_lwm2m-switch");			
			swt.on("change", dsls_lwm2m.onSwitchClick);
			
			var showBtn = $(div).find(".dsls_lwm2m-show-log");
			showBtn.on("click", dsls_lwm2m.showLogDialog)
		});
		
		_ucc.setResourceHandler("dsls_lwm2m", dsls_lwm2m.resources, dsls_lwm2m.refresh);
		dsls_lwm2m.refresh();
	}, 
	refresh: function(type) {
		console.log("dsls_lwm2m.refresh("+type+") called");
		$.each(dsls_lwm2m.divs, function(idx, div) {
			dsls_lwm2m.refreshComponent(div);
			
		});		
	},
	refreshComponent: function(div) {

		var moData = _ucc.getMoData("/3/-/10");	// debug status, /7/-/0: debug start, /7/-/1: debug stop
		if (moData == null || typeof moData == "undefined") {
			console.error("dsls_lwm2m.refreshComponent undefined mo:/3/-/10/");
			return;
		}

		$(div).find(".dsls_lwm2m-value").text(moData.data == "1" ? "ON" : "OFF");
		
		var dsls_lwm2mSwitch = $(div).find(".dsls_lwm2m-switch");	
		var switchery = dsls_lwm2mSwitch[0].switchery;
		if (switchery == null || typeof switchery == "undefined") {
			// create switchery
			if (moData.data == "1") {
				dsls_lwm2mSwitch.prop("checked", true);	
			} else {
				dsls_lwm2mSwitch.prop("checked", false);	
			}
		    switchery = new Switchery(dsls_lwm2mSwitch[0], { color: '#1AB394'});
		    dsls_lwm2mSwitch[0].switchery = switchery;			
		} else {
			// update switchery status
			var checked = moData.data == "1";
			if (checked != switchery.isChecked()) {
				switchery.setPosition(true);
				switchery.handleOnchange(true);
			}			
		}
		
	},
	onSwitchClick: function(evt) {
		var compDiv = $(evt.target).parents(".hit-component-dsls_lwm2m");
		var res = _ucc.getSingleResWithComponentDiv(compDiv);
		var checked = dsls_lwm2m.getSwitchery(compDiv).isChecked();

		var deviceInfo = _ucc.getDeviceInfo();
		var context = {"deviceId":deviceInfo.deviceId, "handler": dsls_lwm2m.debugResultHandler};

		console.log("dsls_lwm2m.onSwitchClick("+deviceInfo.deviceId+","+checked+")");
		if (checked == true) {
			dm_debug_start(context);
		} else {
			dm_debug_stop(context);
		}
	},
	
	degubResultHandler: function(msg, context) {
		console.log("dsls_lwm2m.debugResultHanlder");
	},
	
	getSwitchery: function(div) {
		try {
			var swt = $(div).find(".dsls_lwm2m-switch");
			return swt[0].switchery;			
		} catch(e) {
			return null;
		}
	},
	

	showLogDialog: function(evt) {
		console.log("dsls_lwm2m.showLogDialog(evt)");
		
	}
}

