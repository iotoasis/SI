// Custom scripts
$(document).ready(function () {
	//tsm.init();	
	// 지도 초기화	
	google.maps.event.addDomListener(window, 'load', tsm.refreshMap);

    // Get all html elements for map
    var mapDiv = document.getElementById("tsm_map");
    
	gmap_initializeMap(mapDiv);

	tsm.refreshCount();
	
	setTimeout(tsm.refreshAll, tsm.refreshInterval*1000);

	console.log("total.status.map js initialized");
});

var tsm = {
	refreshInterval: 30,
	init: function() {
		console.log("tsm.init called ");
	},
	
	refreshAll: function() {
		console.log("tsm.refreshAll called ");
		
		tsm.refreshCount();
		tsm.refreshMap();
		
		setTimeout(tsm.refreshAll, tsm.refreshInterval*1000);
	},	
	
	refreshCount: function() {
		console.log("tsm.refresh called ");
		
		// 디바이스 등록수 요청
		var context = { "handler": tsm.deviceCountHandler };
		hdb_get_devicestat_count(context, false);
	},
	
	refreshMap: function() {
		console.log("tsm.refreshMap called ");
		
		var grade = 0;
		var context = { "grade":grade, "handler": tsm.errorDeviceListHandler };
		hdb_get_device_error_list(context, false);
	},
	
	errDeviceList: [],
	pinType: ["NORMAL", "MINOR", "MAJOR", "CRITICAL", "FATAL"],
	errorDeviceListHandler: function(msg, context) {
		console.log("tsm.errorDeviceListHandler called ");
		
		for (var i=0; i<msg.content.list.length; i++) {
			var device = msg.content.list[i];
			device.infoString = "<ul class='map_device_info'><li>모델명: "+device.modelName +"</li><li>SN: "+device.sn+"</li><li>상태: "+tsm.pinType[device.errGrade]+"</li><ul>";

			if (device.latitude != null && device.longitude != null) {
				console.log(device.deviceId+":"+device.latitude +","+device.longitude);
				msg.content.list[i].marker = gmap_setMarker(parseFloat(device.latitude), 
															parseFloat(device.longitude), 
															tsm.pinType[device.errGrade], 
															device, 
															function() {
						var device = this.data;					
						console.log("marker clicked - deviceId:"+ device.deviceId);
						window.location.href = "/hdm/v2/monitor/device.do?deviceId="+device.deviceId;
					});
			}
		}
		
		var oldErrDeviceList = tsm.errDeviceList;
		tsm.errDeviceList = msg.content.list;
		
		tsm.updateErrorDeviceMap();
		tsm.clearErrorDeviceMap(oldErrDeviceList);
	},
	
	clearErrorDeviceMap: function (deviceList) {
		console.log("tsm.clearErrorDeviceMap called");
		
		for (var i=0; i<deviceList.length; i++) {
			var device = deviceList[i];
			if (device.marker != null && typeof device.marker != "undefined") {
				gmap_removeMarker(device.marker);
			}
		}
	},

	updateErrorDeviceMap: function() {
		console.log("tsm.updateErrorDeviceMap called");
		
		//var show = [$("#cb_showNormal").prop("checked") == true,
		//            $("#cb_showMinor").prop("checked") == true,
		//            $("#cb_showMajor").prop("checked") == true,
		//            $("#cb_showCritical").prop("checked") == true];
		var show = [true, true, true, true];
		var maxLat = null, maxLng = null;
		var minLat = null, minLng = null;
		for (var i=0; i<tsm.errDeviceList.length; i++) {
			var device = tsm.errDeviceList[i];
			if (device.marker != null && typeof device.marker != "undefined") {				
				if (show[device.errGrade]) {
					if (maxLat == null || maxLat < device.latitude) {
						maxLat = device.latitude;
					}
					if (maxLng == null || maxLng < device.longitude) {
						maxLng = device.longitude;
					}
					if (minLat == null || minLat > device.latitude) {
						minLat = device.latitude;
					}
					if (minLng == null || minLng > device.longitude) {
						minLng = device.longitude;
					}
					gmap_showMarker(device.marker);
				} else {
					gmap_hideMarker(device.marker);
				}
			}
		}

		var maxLatLng = new google.maps.LatLng(maxLat, maxLng);
		var minLatLng = new google.maps.LatLng(minLat, minLng);

		var bounds = new google.maps.LatLngBounds(minLatLng, maxLatLng);
		gmap_map.fitBounds(bounds);
		
	},
		
	deviceCountHandler: function(msg, context) {
		console.log("tsm.deviceCountHandler called ");
		console.log("	msg: "+JSON.stringify(msg));
		
		// 카운트 업데이트
		var info = msg.content.info;
		$("#tsm_total_count").text(info.totalCount);
		$("#tsm_connected_count").text(info.connectedCount);
		$("#tsm_normal_count").text(info.normalCount);
		$("#tsm_minor_count").text(info.minorCount);
		$("#tsm_major_count").text(info.majorCount);
		$("#tsm_critical_count").text(info.criticalCount);
		$("#tsm_fatal_count").text(info.fatalCount);
		$("#tsm_connected_ratio").width((info.connectedCount*100/info.totalCount)+"%");
		
		// 파이그래프 업데이트
		var graphData = [
		                 {label: "NORMAL", data: info.normalCount,  color: "#0066FF"},
		                 {label: "MINOR", data: info.minorCount,  color: "#999999"},
		                 {label: "MAJOR", data: info.majorCount,  color: "#FAFA66"},
		                 {label: "CRITICAL", data: info.criticalCount,  color: "#FFCC00"},
		                 {label: "FATAL", data: info.fatalCount,  color: "#FF0000"}
		                 ];
		var plotObj = $.plot($("#tsm_count_pie_chart"), graphData, {
		    series: { pie: { show: true  } },
		    grid: { hoverable: true },
		    tooltip: true,
		    tooltipOpts: {
		        content: "%p.0%, %s", // show percentages, rounding to 2 decimal places
		        shifts: { x: 20, y: 0 },
		        defaultTheme: false
		    }
		});
	}
	
};




