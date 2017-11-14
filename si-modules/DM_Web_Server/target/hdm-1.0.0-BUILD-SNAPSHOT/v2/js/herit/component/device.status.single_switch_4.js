// Custom scripts
$(document).ready(function () {
	
	dsss_4.init();
	
	console.log("device.status.single_switch_4 js initialized");
});

var dsss_4 = {
	divs: null,
	resources: null,
	switcheries: [],
	init: function() {
		dsss_4.divs = $(".hit-component-dsss_4");
		dsss_4.resources = [];
		$.each(dsss_4.divs, function(idx, div) {
			//dsss.resources.push(_ucc.getResListWithComponentDiv(div));
			dsss_4.resources = _ucc.getResListWithComponentDiv(div);
			
			var swt = $(div).find(".dsss-switch");			
			//swt.on("change", dsss.onSwitchClick);
			//swt.on("click", dsss.onSwitchClick);
		});
	    
		_ucc.setResourceHandler("dsss_4", dsss_4.resources, dsss_4.refresh);
		dsss_4.refresh();
	}, 
	refresh: function(type) {
		console.log("dsss_4.refresh("+type+") called");
		
		$.each(dsss_4.divs, function(idx, div) {
			dsss_4.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var moData = _ucc.getMoData(resources[0]);
		var unit = _ucc.getUnitString(resources[0]);
		
		

		if (moData == null || typeof moData == "undefined") {
			console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSS:"+resources[0]+")");
			/*dsssSwitch.prop("checked", false);
			switchery = new Switchery(dsssSwitch[0], { color: '#1AB394'});
			switchery.setPosition(false);
			switchery.handleOnchange(false);*/
			return;
		}		
		$(div).find(".dsss-value").text(moData.data);
		
		var dsssSwitch = $(div).find(".dsss-switch");	
		var switchery = dsssSwitch[0].switchery;
		
		if (switchery == null || typeof switchery == "undefined") {
			// create switchery
			if (moData.data == "CONTACTED") {
				dsssSwitch.prop("checked", true);	
			} else {
				dsssSwitch.prop("checked", false);	
			}
		    switchery = new Switchery(dsssSwitch[0], { color: '#1AB394'});
		    $(switchery.switcher).on("click", dsss_4.onSwitchClick);
		    dsssSwitch[0].switchery = switchery;			
		} else {
			// update switchery status
			var checked = moData.data == "CONTACTED";
			if (checked != switchery.isChecked()) {
				switchery.setPosition(true);
				switchery.handleOnchange(true);
			}			
		}		
	},
	onSwitchClick: function(evt) {
		console.log("dsss_4.onSwitchClick(evt)");
		var compDiv = $(evt.target).parents(".hit-component-dsss_4");
		var res = _ucc.getSingleResWithComponentDiv(compDiv);
		var val = dsss_4.getSwitchery(compDiv).isChecked() == true ? "CONTACTED" : "NOT CONTACTED";

		console.log("dsss_4.onSwitchClick - "+ _ucc.getDeviceId() +","+ res +","+ val);
		_ucc.executeOrWrite(_ucc.getDeviceId(), res, val);
	},
	getSwitchery: function(div) {
		try {
			var dsssSwitch = $(div).find(".dsss-switch");
			return dsssSwitch[0].switchery;			
		} catch(e) {
			return null;
		}
	}
	
}
