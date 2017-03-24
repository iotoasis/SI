// Custom scripts
$(document).ready(function () {
	
	dssnbsk.init();

	console.log("device.status.single_num_bar_sk js initialized");
		
    $(".dssnb-bar-graph").peity("bar", {
        fill: ["#1ab394", "#d7d7d7"]
    })
});

var dssnbsk = {
	divs: null,
	resources: null,
	init: function() {
		dssnbsk.divs = $(".hit-component-dssnbsk");
		dssnbsk.resources = [];
		$.each(dssnbsk.divs, function(idx, div) {
			$.each(_ucc.getResListWithComponentDiv(div), function(ix, val) {
				dssnbsk.resources.push(val);
			});
			var resource = _ucc.getResListWithComponentDiv(div)[0];
			var profile = _ucc.getProfileInfo(resource);
			var btn = $(div).find(".btn-refresh");
			var updateTime = $(div).find(".span-update-time");
			if (profile.operation.indexOf("E") >= 0) {
				btn.on("click", dssnbsk.onRefreshClick);				
			} else {
				btn.hide();
				updateTime.hide();
			}
		});
		
		_ucc.setResourceHandler("dssnbsk", dssnbsk.resources, dssnbsk.refresh);
		dssnbsk.refresh();
	}, 
	refresh: function(type) {
		console.log("dssnbsk.refresh("+type+") called");
		
		$.each(dssnbsk.divs, function(idx, div) {
			dssnbsk.refreshComponent(div);
		});		
	},
	refreshComponent: function(div) {
		var resources = _ucc.getResListWithComponentDiv(div);

		var moData = _ucc.getMoData(resources[0]);
		var unit = _ucc.getUnitString(resources[0]);
		

		if (moData == null || typeof moData == "undefined") {
			console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSNB:"+resources[0]+")");
			//alert("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSNB:"+resources[0]+")");					
			$(div).find(".dssnb-value").text("-");
			$(div).find(".dssnb-unit").text("-");
			return;
		}					
		$(div).find(".dssnb-value").text(moData.data);
		$(div).find(".dssnb-unit").text(unit);
		$(div).find(".span-update-time").text(moData.dataTime);
		
	},
	onRefreshClick: function(evt) {
		console.log("dssnbsk.onRefreshClick()");
		var compDiv = $(evt.target).parents(".hit-component-dssnbsk");
		var res = _ucc.getSingleResWithComponentDiv(compDiv);
		
		var optionData = "";
		var profile = _ucc.getProfileInfo(res);
		if (profile != null && profile.optionDataList != null && profile.optionDataList.length > 0) {
			optionData = profile.optionDataList[0].data;
		} 
		console.log("dssnbsk.onRefreshClick - "+ _ucc.getDeviceId() +","+ res +","+optionData);
		_ucc.executeOrWrite(_ucc.getDeviceId(), res, optionData);
	}
}

