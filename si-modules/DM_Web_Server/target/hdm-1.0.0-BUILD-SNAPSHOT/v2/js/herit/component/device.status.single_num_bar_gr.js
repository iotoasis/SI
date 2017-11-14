// Custom scripts
$(document).ready(function () {
	
	dssnbgr.init();

	console.log("device.status.single_num_bar_gr js initialized");
		
    /*$(".dssnb-bar-graph").peity("bar", {
        fill: ["#1ab394", "#d7d7d7"]
    })*/
});

var dssnbgr = {
	divs: null,
	resources: null,
	init: function() {
		dssnbgr.divs = $(".hit-component-dssnbgr");
		dssnbgr.resources = [];
		$.each(dssnbgr.divs, function(idx, div) {
			$.each(_ucc.getResListWithComponentDiv(div), function(ix, val) {
				dssnbgr.resources.push(val);
			});
			var resource = _ucc.getResListWithComponentDiv(div)[0];
			var profile = _ucc.getProfileInfo(resource);
			var btn = $(div).find(".btn-refresh");
			var updateTime = $(div).find(".span-update-time");
			if (profile.operation.indexOf("E") >= 0) {
				btn.on("click", dssnbgr.onRefreshClick);				
			} else {
				btn.hide();
				updateTime.hide();
			}
		});
		
		_ucc.setResourceHandler("dssnbgr", dssnbgr.resources, dssnbgr.refresh);
		dssnbgr.refresh();
	}, 
	refresh: function(type) {
		console.log("dssnbgr.refresh("+type+") called");
		
		$.each(dssnbgr.divs, function(idx, div) {
			dssnbgr.refreshComponent(div);
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
		console.log("dssnbgr.onRefreshClick()");
		var compDiv = $(evt.target).parents(".hit-component-dssnbgr");
		var res = _ucc.getSingleResWithComponentDiv(compDiv);
		
		var optionData = "";
		var profile = _ucc.getProfileInfo(res);
		if (profile != null && profile.optionDataList != null && profile.optionDataList.length > 0) {
			optionData = profile.optionDataList[0].data;
		} 
		console.log("dssnbgr.onRefreshClick - "+ _ucc.getDeviceId() +","+ res +","+optionData);
		_ucc.executeOrWrite(_ucc.getDeviceId(), res, optionData);
	}
}

