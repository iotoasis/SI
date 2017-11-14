// Custom scripts
$(document).ready(function () {
	
	dsss_lwm2m.init();
	console.log("device.status.single_switch js initialized");
});



var dsss_lwm2m = {
	idx: 0,
	divs: null,
	resources: null,
	switcheries: [],
	init: function() {
		dsss_lwm2m.divs = $(".hit-component-dsss_lwm2m");
		dsss_lwm2m.resources = [];
		$.each(dsss_lwm2m.divs, function(idx, div) {
			//dsss_lwm2m.resources.push(_ucc.getResListWithComponentDiv(div));
			dsss_lwm2m.resources = _ucc.getResListWithComponentDiv(div);
			
			var swt = $(div).find(".dsss_lwm2m-switch");			
			//swt.on("change", dsss_lwm2m.onSwitchClick);
			//swt.on("click", dsss_lwm2m.onSwitchClick);
		});
	    
		_ucc.setResourceHandler("dsss_lwm2m", dsss_lwm2m.resources, dsss_lwm2m.refresh);
		dsss_lwm2m.refresh();
	}, 
	refresh: function(type) {
		console.log("dsss_lwm2m.refresh("+type+") called");
		
		$.each(dsss_lwm2m.divs, function(idx, div) {
			dsss_lwm2m.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var moData = _ucc.getMoData(resources[0]);
		var unit = _ucc.getUnitString(resources[0]);

		if (moData == null || typeof moData == "undefined") {
			console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (dsss_lwm2m:"+resources[0]+")");
			return;
		}		
		$(div).find(".dsss_lwm2m-value").text(moData.data=="49"?"ON":"OFF");
		
		var dsss_lwm2mSwitch = $(div).find(".dsss_lwm2m-switch");	
		var switchery = dsss_lwm2mSwitch[0].switchery;
		if (switchery == null || typeof switchery == "undefined") {
			// create switchery
			if (moData.data == "49") {
				dsss_lwm2mSwitch.prop("checked", true);	
			} else {
				dsss_lwm2mSwitch.prop("checked", false);	
			}
		    switchery = new Switchery(dsss_lwm2mSwitch[0], { color: '#1AB394'});
		    $(switchery.switcher).on("click", dsss_lwm2m.onSwitchClick);
		    dsss_lwm2mSwitch[0].switchery = switchery;
		} else {
			// update switchery status
			var checked = moData.data == "49";
			if (checked != switchery.isChecked()) {
				switchery.setPosition(true);
				switchery.handleOnchange(true);
			}
		}
	},
	onSwitchClick: function(evt) {
		console.log("dsss_lwm2m.onSwitchClick(evt)");
		var compDiv = $(evt.target).parents(".hit-component-dsss_lwm2m");
		var res = _ucc.getSingleResWithComponentDiv(compDiv);
		var val = dsss_lwm2m.getSwitchery(compDiv).isChecked() == true ? "ON" : "OFF";

		console.log("dsss_lwm2m.onSwitchClick - "+ _ucc.getDeviceId() +","+ res +","+ val);
		_ucc.executeOrWrite(_ucc.getDeviceId(), res, val);
		
		// 제어 이력 남기기
		var displayName = new Array("led","","sound");
		insertControlHistory(res,displayName[parseInt(res.substring(res.length-1))-1],val,"OK");
	},
	getSwitchery: function(div) {
		try {
			var dsss_lwm2mSwitch = $(div).find(".dsss_lwm2m-switch");
			return dsss_lwm2mSwitch[0].switchery;			
		} catch(e) {
			return null;
		}
	}
	
}
