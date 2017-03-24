// Custom scripts
$(document).ready(function () {
	
	dscg.init();
	
	console.log("device.status.dscg js initialized");
});

var dscg = {
	divs: null,
	resources: null,
	init: function() {
		dscg.divs = $(".hit-component-dscg");
		dscg.resources = [];
		$.each(dscg.divs, function(idx, div) {
			$.each(_ucc.getResListWithComponentDiv(div), function(ix, val) {
				dscg.resources.push(val);
				
			});
			
			$(div).find(".dscg-value").val(0);
			
			var resource = _ucc.getResListWithComponentDiv(div)[0];
			var profile = _ucc.getProfileInfo(resource);
			var gbtn = $(div).find(".inline");
			
			if (profile.operation.indexOf("E") >= 0) {
				//TODO 클릭 이벤트
				gbtn.on("click", dscg.onClick);
			}
		});
		
		_ucc.setResourceHandler("dscg", dscg.resources, dscg.refresh);
		dscg.refresh();
	},
	refresh: function(type) {
		console.log("dscg.refresh("+type+") called");
		 $.each(dscg.divs, function(idx, div) {
			dscg.refreshComponent(div); 
			$(".dial").knob();
		 });
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);
		
		var moData = _ucc.getMoData(resources[0]);
		
		$(div).find(".dscg-value").val(moData.data);
		$(".dial").knob();
	},
	onClick: function(evt) {
		console.log("dscg.onClick()");
		var div = $(evt.target).parents(".hit-component-dscg");
		var res = _ucc.getSingleResWithComponentDiv(div);
		
		/*var profile = _ucc.getProfileInfo(res);
		if (profile != null && profile.optionDataList != null && profile.optionDataList.length > 0) {
			optionData = profile.optionDataList[0].data;
		}*/
		var updateValue = $(div).find(".dscg-value").val();
		console.log("dscg.onClick - " + _ucc.getDeviceId() + "," + res + "," + updateValue);
		_ucc.executeOrWrite(_ucc.getDeviceId(), res, updateValue);
	}
};
