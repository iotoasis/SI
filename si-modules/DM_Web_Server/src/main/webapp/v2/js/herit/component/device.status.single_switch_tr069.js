// Custom scripts
$(document).ready(function () {
	
	dsss_tr069.init();
	console.log("device.status.single_switch js initialized");
});



var dsss_tr069 = {
	idx: 0,
	divs: null,
	resources: null,
	switcheries: [],
	init: function() {
		dsss_tr069.divs = $(".hit-component-dsss_tr069");
		dsss_tr069.resources = [];
		$.each(dsss_tr069.divs, function(idx, div) {
			//dsss_tr069.resources.push(_ucc.getResListWithComponentDiv(div));
			dsss_tr069.resources = _ucc.getResListWithComponentDiv(div);
			
			var swt = $(div).find(".dsss_tr069-switch");			
			//swt.on("change", dsss_tr069.onSwitchClick);
			//swt.on("click", dsss_tr069.onSwitchClick);
		});
	    
		_ucc.setResourceHandler("dsss_tr069", dsss_tr069.resources, dsss_tr069.refresh);
		dsss_tr069.refresh();
	}, 
	refresh: function(type) {
		console.log("dsss_tr069.refresh("+type+") called");
		
		$.each(dsss_tr069.divs, function(idx, div) {
			dsss_tr069.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var moData = _ucc.getMoData(resources[0]);
		var unit = _ucc.getUnitString(resources[0]);

		if (moData == null || typeof moData == "undefined") {
			console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (dsss_tr069:"+resources[0]+")");
			return;
		}		
		$(div).find(".dsss_tr069-value").text(moData.data);
		
		var dsss_tr069Switch = $(div).find(".dsss_tr069-switch");	
		var switchery = dsss_tr069Switch[0].switchery;
		if (switchery == null || typeof switchery == "undefined") {
			// create switchery
			if (moData.data == "ON") {
				dsss_tr069Switch.prop("checked", true);	
			} else {
				dsss_tr069Switch.prop("checked", false);	
			}
		    switchery = new Switchery(dsss_tr069Switch[0], { color: '#1AB394'});
		    $(switchery.switcher).on("click", dsss_tr069.onSwitchClick);
		    dsss_tr069Switch[0].switchery = switchery;
		} else {
			// update switchery status
			var checked = moData.data == "ON";
			if (checked != switchery.isChecked()) {
				switchery.setPosition(true);
				switchery.handleOnchange(true);
			}
		}
	},
	onSwitchClick: function(evt) {
		console.log("dsss_tr069.onSwitchClick(evt)");
		var compDiv = $(evt.target).parents(".hit-component-dsss_tr069");
		var res = _ucc.getSingleResWithComponentDiv(compDiv);
		var val = dsss_tr069.getSwitchery(compDiv).isChecked() == true ? "ON" : "OFF";

		console.log("dsss_tr069.onSwitchClick - "+ _ucc.getDeviceId() +","+ res +","+ val);
		_ucc.executeOrWrite(_ucc.getDeviceId(), res, val);
		
		// 제어 이력 남기기
		//var displayName = new Array("led","","sound");
		//insertControlHistory(res,displayName[parseInt(res.substring(res.length-1))-1],val,"OK");
	},
	getSwitchery: function(div) {
		try {
			var dsss_tr069Switch = $(div).find(".dsss_tr069-switch");
			return dsss_tr069Switch[0].switchery;			
		} catch(e) {
			return null;
		}
	}
	
}
