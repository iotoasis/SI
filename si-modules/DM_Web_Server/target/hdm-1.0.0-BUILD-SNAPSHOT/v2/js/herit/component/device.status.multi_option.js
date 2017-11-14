// Custom scripts
$(document).ready(function () {
	
	dsmo.init();

	console.log("device.status.multi_options js initialized");
});


var dsmo = {
	divs: null,
	resources: null,
	init: function() {
		dsmo.divs = $(".hit-component-dsmo");
		dsmo.resources = [];
		$.each(dsmo.divs, function(idx, div) {
			var resArr = _ucc.getResListWithComponentDiv(div);
			dsmo.resources = resArr;
			
			var profile = _ucc.getProfileInfo(resArr[0]);
			if (profile == null || typeof profile == "undefined") {
				console.error("dsmo undefined profile:"+ resArr[0]);
				return; 
			}
			var options = profile.optionDataList;
			
			var optionTag = $(div).find(".dsmo-option");
			optionTag.empty();
			
			$.each(options, function(idx, option) {
				optionTag.append($("<option value='"+option.data+"'>"+option.displayData+"</option>"));
			});
			
			optionTag.on("change", dsmo.onChange);
		});
		
		_ucc.setResourceHandler("dsmo", dsmo.resources, dsmo.refresh);
		dsmo.refresh();
	}, 
	refresh: function(type) {
		console.log("dsmo.refresh("+type+") called");
		$.each(dsmo.divs, function(idx, div) {
			dsmo.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var moData = _ucc.getMoData(resources[0]);
		
		if (moData == null || typeof moData == "undefined") {
			console.error("dsmo.refreshComponent unknown resource:"+resources[0]);
			return;
		}
		
		$(div).find(".dsmo-option").val(moData.data);
		
	}, 
	onChange: function(evt) {
		console.log("dsmo.onChange(evt)");
		var option = $(evt.target);
		var compDiv = option.parents(".hit-component-dsmo");
		var res = _ucc.getSingleResWithComponentDiv(compDiv);
		var val = option.val();
		
		console.log("dsmo.onChange - "+_ucc.getDeviceId() +","+ res +","+ val);
		_ucc.executeOrWrite(_ucc.getDeviceId(), res, val);
	}
}

