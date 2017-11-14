// Custom scripts
$(document).ready(function () {
	// 지도 초기화	
	google.maps.event.addDomListener(window, 'dsma', dsma.refresh);

    // Get all html elements for map
    var mapDiv = $(".dsma-map");
    
    $.each(mapDiv, function(idx, div) {
    	gmap_initializeMap(div);
    });
	


	dsma.init();
	
	console.log("device.status.map js initialized");
});

var marker = null;
var pinType = ["NORMAL", "MINOR", "MAJOR", "CRITICAL", "FATAL"];

var dsma = {
	divs: null,
	init: function() {
		dsma.divs = $(".hit-component-dsma");

		dsma.divs = $(".hit-component-dsma");
		dsma.resources = [];
		$.each(dsma.divs, function(idx, div) {
			//dsma.resources.push(_ucc.getResListWithComponentDiv(div));
			dsma.resources = _ucc.getResListWithComponentDiv(div);
		});
		
		_ucc.setResourceHandler("dsma", dsma.resources, dsma.refresh);
		dsma.refresh();
	}, 
	refresh: function(type) {
		console.log("dsma.refresh("+type+") called");
		for (var i=0; i<dsma.divs.length; i++) {
			dsma.refreshMap(dsma.divs[i]);
		}			
	},
	refreshMap: function(mapDiv) {
		var resources = _ucc.getResListWithComponentDiv(mapDiv);

		var laMoData = _ucc.getMoData(resources[0]);
		var loMoData = _ucc.getMoData(resources[1]);
		if (laMoData == null || typeof laMoData == "undefined" || laMoData == null || typeof laMoData == "undefined") {
			console.error("dsma undefined resource:"+ resources[0] +","+ resources[1]);
			return;
		}
		
		var latitude = laMoData.data;
		var longitude = loMoData.data;
		
		var deviceInfo = _ucc.getDeviceInfo();

		if (latitude != null && typeof latitude != "undefined" && longitude != null && typeof longitude != "undefined") {
			if (marker == null) {
				marker = gmap_setMarker(parseFloat(latitude), parseFloat(longitude), pinType[deviceInfo.errGrade]);	
				gmap_showMarker(marker);		
			} else {
				gmap_moveMarker(marker, parseFloat(latitude), parseFloat(longitude), pinType[deviceInfo.errGrade]);
			}

			gmap_codeLatLng(parseFloat(latitude), parseFloat(longitude), marker, function(addr) {
				//$("#device_ADDRESS").text(addr);
			});
		}
		/*var date = new Date(parseInt(timestamp)*1000);
		updateResourceUI("/5/-/0", latitude);
		updateResourceUI("/5/-/1", longitude);
		updateResourceUI("/5/-/2", altitude);
		updateResourceUI("/5/-/3", getTimeString(date));*/
		
		
	}
}