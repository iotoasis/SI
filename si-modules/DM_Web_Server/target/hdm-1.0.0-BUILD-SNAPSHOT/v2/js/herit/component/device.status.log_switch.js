// Custom scripts
$(document).ready(function () {

	dsls.init();
	
	console.log("device.status.log_switch js initialized");
});


var dsls = {
	divs: null,
	resources: null,
	init: function() {
		dsls.divs = $(".hit-component-dsls");
		dsls.resources = [];
		$.each(dsls.divs, function(idx, div) {
			//dsls.resources.push(_ucc.getResListWithComponentDiv(div));
			dsls.resources = _ucc.getResListWithComponentDiv(div);
			
			var swt = $(div).find(".dsls-switch");			
			swt.on("change", dsls.onSwitchClick);
			
			var showBtn = $(div).find(".dsls-show-log");
			showBtn.on("click", dsls.showLogDialog)
		});
		
		_ucc.setResourceHandler("dsls", dsls.resources, dsls.refresh);
		dsls.refresh();
	}, 
	refresh: function(type) {
		console.log("dsls.refresh("+type+") called");
		$.each(dsls.divs, function(idx, div) {
			dsls.refreshComponent(div);
			
		});		
	},
	refreshComponent: function(div) {

		var moData = _ucc.getMoData("/7/-/2");	// debug status, /7/-/0: debug start, /7/-/1: debug stop
		if (moData == null || typeof moData == "undefined") {
			console.error("dsls.refreshComponent undefined mo:/7/-/2/");
			return;
		}

		$(div).find(".dsls-value").text(moData.data == "1" ? "ON" : "OFF");
		
		var dslsSwitch = $(div).find(".dsls-switch");	
		var switchery = dslsSwitch[0].switchery;
		if (switchery == null || typeof switchery == "undefined") {
			// create switchery
			if (moData.data == "1") {
				dslsSwitch.prop("checked", true);	
			} else {
				dslsSwitch.prop("checked", false);	
			}
		    switchery = new Switchery(dslsSwitch[0], { color: '#1AB394'});
		    dslsSwitch[0].switchery = switchery;			
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
		var compDiv = $(evt.target).parents(".hit-component-dsls");
		var res = _ucc.getSingleResWithComponentDiv(compDiv);
		var checked = dsls.getSwitchery(compDiv).isChecked();

		var deviceInfo = _ucc.getDeviceInfo();
		var context = {"deviceId":deviceInfo.deviceId, "handler": dsls.debugResultHandler};

		console.log("dsls.onSwitchClick("+deviceInfo.deviceId+","+checked+")");
		if (checked == true) {
			dm_debug_start(context);
		} else {
			dm_debug_stop(context);
		}
	},
	
	degubResultHandler: function(msg, context) {
		console.log("dsls.debugResultHanlder");
	},
	
	getSwitchery: function(div) {
		try {
			var swt = $(div).find(".dsls-switch");
			return swt[0].switchery;			
		} catch(e) {
			return null;
		}
	},
	

	showLogDialog: function(evt) {
		console.log("dsls.showLogDialog(evt)");
		
	}
}

