var gmap_geocoder;
var gmap_map = null;
var gmap_infowindow;
var gmap_pin = new Array();
var gmap_zindex = new Array();

function gmap_initializeMap(canvas) {
	gmap_infowindow = new google.maps.InfoWindow();
	gmap_geocoder = new google.maps.Geocoder();
	var latlng = new google.maps.LatLng(37.511530, 127.043138);
	var mapOptions = {
		zoom : 10,
		center : latlng,
		mapTypeId : 'roadmap'
	}
	gmap_map = new google.maps.Map(canvas, mapOptions);

    gmap_pin["NORMAL"] = gmap_makePin("33FF66");
    gmap_pin["MINOR"] = gmap_makePin("999999");
    gmap_pin["MAJOR"] = gmap_makePin("FAFA66");
    gmap_pin["CRITICAL"] = gmap_makePin("FFCC00");
    gmap_pin["FATAL"] = gmap_makePin("FF0000");
    gmap_zindex["NORMAL"] = 0;
    gmap_zindex["MINOR"] = 10;
    gmap_zindex["MAJOR"] = 20;
    gmap_zindex["CRITICAL"] = 30;
    gmap_zindex["FATAL"] = 40;
        
}

function gmap_makePin(color) {
	var pin = {};
	pin.icon = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|" + color,
		new google.maps.Size(21, 34), new google.maps.Point(0,0), new google.maps.Point(10, 34));
	pin.shadow = new google.maps.MarkerImage("http://chart.apis.google.com/chart?chst=d_map_pin_shadow",
		new google.maps.Size(40, 37), new google.maps.Point(0, 0), new google.maps.Point(12, 35));
	return pin;
}

function gmap_setMarker(lat, lng, pinType, data, clickHandler) {
	if (gmap_map == null) {
		initializeMap();
	}
	console.log("gmap_setMarker called - lat:"+lat+", lng:"+lng+", pinType:"+pinType);
	
	var latlng = new google.maps.LatLng(lat, lng);
	
	var pin = gmap_pin[pinType];
	if (pin == null) {
		pin = gmap_pin["NORMAL"];
	}
	
	gmap_map.setCenter(latlng);
	var marker = new google.maps.Marker({
		position : latlng,
		map : gmap_map,
		icon: pin.icon,
		shadow: pin.shadow,
		visible: false,
		zindex: gmap_zindex[pinType]
	});
	marker.data = data;
	marker.title = "device";
	google.maps.event.addListener(marker,'click', clickHandler);
	google.maps.event.addListener(marker,'mouseover', function() {
		gmap_infowindow.setContent(marker.data.infoString);
		gmap_infowindow.open(gmap_map, marker);
	});
	//google.maps.event.addListener(marker,'mouseout', function() {
	//	gmap_infowindow.close();
	//});

	return marker;
}

function gmap_moveMarker(marker, lat, lng, pinType) {
	console.log("gmap_moveMarker called - lat:"+lat+", lng:"+lng+", pinType:"+pinType);
	
	var latlng = new google.maps.LatLng(lat, lng);
	
	var pin = gmap_pin[pinType];
	if (pin == null) {
		pin = gmap_pin["NORMAL"];
	}
	
	gmap_map.setCenter(latlng);
	marker.setPosition(latlng);
	marker.setIcon(pin.icon);
	return marker;
}

function gmap_showMarker(marker) {
	marker.setVisible(true);
}

function gmap_hideMarker(marker) {
	marker.setVisible(false);
}

function gmap_removeMarker(marker) {
	marker.setMap(null);
}

function gmap_codeLatLng(lat, lng, marker, addrHandler) {
	if (gmap_map == null) {
		initializeMap();
	}
	// input = 37.511530, 127.043138;
	var latlng = new google.maps.LatLng(lat, lng);
	
	gmap_map.setZoom(10);
	gmap_map.setCenter(latlng);
	if (marker == null || typeof marker == "undefined") {
		marker = new google.maps.Marker({
			position : latlng,
			map : gmap_map
		});	
	}
	
	gmap_geocoder.geocode({
		'latLng' : latlng
	}, function(results, status) {
		if (status == google.maps.GeocoderStatus.OK) {
			if (results[0]) {
				
				// alert(results[0].formatted_address);
				gmap_infowindow.setContent(results[0].formatted_address);
				gmap_infowindow.open(gmap_map, marker);
				if (addrHandler != null) {
					addrHandler(results[0].formatted_address);
				}
			} else {
				alert('No results found');
			}
		} else {
			alert('Geocoder failed due to: ' + status);
		}
	});
	return marker;
}

// google.maps.event.addDomListener(window, 'load', initialize);
