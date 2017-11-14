// UI Component common function

var _ucc = {
	
	refreshInterval: 10, // seconds
	
	timerHandle: null,
	
	// 필수 조회값: 에러코드(2/-/29)
	//resourceMap: {"/2/-/29":true},	// 중복검사를 위한 resourceMap, 초기화(setResourceHandler)과정에서만 사용됨	
	//resourceUris: ["/2/-/29"],	// array of resourceuris for dm command parameter	
	//e: [{"n":"/2/-/29//"}],	// array of name-uri for dm command parameter
	resourceMap: {},	// 중복검사를 위한 resourceMap, 초기화(setResourceHandler)과정에서만 사용됨	
	resourceUris: [],	// array of resourceuris for dm command parameter	
	e: [],	// array of name-uri for dm command parameter
	count: 0,
	
	setResourceHandler: function(compName, resources, statusHandler) {
		var compMap = _ucc.getStatCompMap();
		compMap[compName]["statusHandler"] = statusHandler;
		$.each(resources, function(idx, val) {

//			_ucc.resourceMap[val] = true;
//			if (typeof val == "array") {
//				$.each(val, function(idx, str) {
//					_ucc.resourceUris[_ucc.resourceUris.length] = str;
//					_ucc.e[_ucc.e.length] = {"n": str};
//				});
//
//			} else {
//				
//				_ucc.resourceUris[_ucc.resourceUris.length] = val;
//				_ucc.e[_ucc.e.length] = {"n": val};
//
//			}
			if (_ucc.resourceMap[val] == null && val != "") {
				_ucc.resourceMap[val] = true;
				_ucc.resourceUris[_ucc.resourceUris.length] = val;
				_ucc.e[_ucc.e.length] = {"n": val};
			}
			
		});
		
		var handlerSetted = true;
		$.each(compMap, function(idx, comp) {
			if (comp["statusHandler"] == null || typeof comp["statusHandler"] == "undefined") {
				handlerSetted = false;
			}
		});
		
		if (handlerSetted && _ucc.timerHandle == null) {
			timerHandle = setTimeout(_ucc.refreshStatus, 1000);
		}
	},
	
	refreshStatus: function() {
		console.log("_ucc.refreshStatus called()");

		var context = {"deviceId":deviceInfo.deviceId, "resourceUris": _ucc.resourceUris, "e": _ucc.e, "handler":_ucc.refreshStatusHandler, "errorHandler":_ucc.refreshStatusErrorHandler}; 
		dm_read(context, false);
	},
	
	refreshStatusHandler: function(msg, context) {
		console.log("_ucc.refreshStatusHandler called(msg, context)");
		
		_ucc.updateMoMapWithMsg(msg);
		
		// component별 handler 호출		
		var compMap = _ucc.getStatCompMap();
		
		$.each(compMap, function(idx, comp) {
			var statusHandler = comp["statusHandler"];
			
			if (statusHandler != null && typeof statusHandler != "undefined") {
				statusHandler("periodic");
			}
		});
		_ucc.updateDeviceStatus(msg.errorCode);
		
		setTimeout(_ucc.refreshStatus, _ucc.refreshInterval*1000);
	},
	
	refreshStatusErrorHandler: function(msg, context) {
		console.log("refreshStatusErrorHandler called(msg, context)");
		
		var handled = _ucc.updateDeviceStatus(msg.errorCode);
		
		if (handled == false && msg.errorCode != null && _ucc.count == 0) {
			alert("디바이스가 연결되어 있지 않습니다.");
			_ucc.count++;
		}		
		handled = true;
		
		setTimeout(_ucc.refreshStatus, _ucc.refreshInterval*1000);
		
		return handled;
		
	},
	
	updateDeviceStatus: function(statusCode) {

		try {
			if (dsssk != null && dsssk != undefined) {
				dsssk.updateDeviceStatus(statusCode);
				return true;
			}				
		} catch (e) {
			console.log("Faile to handle device status");
		}
		return true;
	},
	
	readStatus: function(deviceId, uris) {
		console.log("_ucc.refreshStatus called()");
		
		var e = [];
		$.each(uris, function(idx, uri) {
			e[e.length] = {"name": uri};
		});

		var context = {"deviceId":deviceId, "resourceUris": uris, "e": e, "handler":_ucc.readStatusHandler}; 
		
	},
	
	readStatusHandler: function(msg, context) {
		console.log("_ucc.readStatusHandler called()");
		
		_ucc.updateMoMapWithMsg(msg);
		
		// component별 handler 호출		
		var compMap = _ucc.getStatCompMap();
		
		$.each(compMap, function(idx, comp) {
			var statusHandler = comp["statusHandler"];
			
			if (statusHandler != null && typeof statusHandler != "undefined") {
				statusHandler("called");
			}
		});

		var handled = _ucc.updateDeviceStatus(msg.errorCode);
		
	},

	executeOrWrite: function(deviceId, resUri, val) {
		console.log("ucc.execute called("+deviceId+", "+resUri+", "+ val+")");
		
		var context = {"deviceId":deviceId, "resourceUris":[resUri], 
						"e":[ {"n":resUri, "sv":val} ], "handler": _ucc.executeResultHandler};
		
		var profile = _ucc.getProfileInfo(resUri);
		if (profile.operation.indexOf("W") >= 0) {
			dm_write(context, false);			
		} else if (profile.operation.indexOf("E") >= 0) {
			dm_execute(context, false);
		} else {
			console.error("exectueOrWrite called for non executable/writable resources:"+resUri );
			throw "exectueOrWrite called for non executable/writable resources:"+resUri;
		}
		
	},

	executeResultHandler: function (msg, context) {
		console.log("_ucc.executeResultHandler called ");
		//console.log("msg: "+JSON.stringify(msg));
		//console.log("context: "+JSON.stringify(context));

		context = {"deviceId":deviceInfo.deviceId, "resourceUris":context.resourceUris, 
					"e":[ {"n":context.resourceUris[0]} ], "handler": _ucc.readStatusHandler};
		
		dm_read(context, false);
	},
	
	

	

	updateMoMapWithMsg: function(msg) {
		var moMap = _ucc.getMoMap();
		var uri;
		var timeStr = _ucc.getTimeString(new Date());
		var errs = [];
		for (var i=0; i<msg.content.e.length; i++) {
			uri = msg.content.e[i].n;
			if (uri.indexOf('/2/-/29') >= 0) {
				errs[errs.length] = msg.content.e[i];
				continue;
			}
			if (moMap[uri] == null) {
				moMap[uri] = {"createTime": timeStr, 
						"resourceName": uri, 
						"resourceUri": uri}
			}
			moMap[uri].data = msg.content.e[i].sv;
			moMap[uri].updateTime = timeStr;
			moMap[uri].dataTime = _ucc.getShortTimeString(new Date(parseInt(msg.content.e[i].ti)));
		}
		if (errs.length > 0) {
			// clear err
			for (var moName in moMap) {
				if (moName.indexOf("/2/-/29/") >= 0) {
					delete moMap[moName];
				}
			}
			// set err
			for (var i=0; i<errs.length; i++) {
				uri = errs[i].n;
				if (moMap[uri] == null) {
					moMap[uri] = {"createTime": timeStr, 
							"resourceName": uri, 
							"resourceUri": uri}
				}
				moMap[uri].data = errs[i].sv;
				moMap[uri].updateTime = timeStr;
				moMap[uri].dataTime = _ucc.getShortTimeString(new Date(parseInt(msg.content.e[i].ti)));
			}
		}
	},	
	
	getResListWithComponentDiv: function(div) {
		console.log("_ucc.getResListWithComponentDiv called ");
		var arr = $(div).attr("resources").split(";");
		var newArr = new Array();
		for (var i=0; i<arr.length; i++) {
			if (arr[i] != null && arr[i] != "") {
				newArr[newArr.length] = arr[i];
			}
		}
		//return $(div).attr("resources").split(";");
		return newArr;
	},
	
	/** 해당 센서의 기준치 값 분리 */
	getThresholdWithComponentDiv: function(div) {
		console.log("_ucc.getThresholdWithComponentDiv called ");
		var arr = $(div).attr("parameters").split(";");
		var parameterMap = {};
		
		if (arr[0] != "" && arr[1] != "") {// 최대, 최소 존재(사이값)
			parameterMap["between"] = [arr[0], arr[1]];
		} else if (arr[0] == "" && arr[1] != "") {// 최소값만 존재(이상)
			parameterMap["above"] = arr[1];
		} else {// 최대값만 존재(이하)
			parameterMap["below"] = arr[0];
		}
		return parameterMap;
	},
	
	getSingleResWithComponentDiv: function(div) {
		console.log("_ucc.getSingleResWithComponentDiv called ");
		
		return $(div).attr("resources").split(";")[0];
	},
	
	getStatCompMap: function() {
		console.log("_ucc.getStatCompMap called ");
		
		if (typeof statCompMapJson == "undefined" || statCompMapJson == null) {
			throw "statCompMapJson object not defined.";
		} else {
			return statCompMapJson;
		}
	},
	
	getDeviceInfo: function() {
		console.log("_ucc.getDeviceInfo called ");
		
		if (typeof deviceInfo == "undefined" || deviceInfo == null) {
			throw "deviceInfo object not defined.";
		} else {
			return deviceInfo;
		}
	},
	
	getDeviceId: function() {
		console.log("_ucc.getDeviceId called ");
		
		if (typeof deviceInfo == "undefined" || deviceInfo == null) {
			throw "deviceInfo object not defined.";
		} else {
			return deviceInfo.deviceId;
		}
	},

	getDeviceModelInfo: function() {
		console.log("_ucc.getDeviceModelInfo called ");
		
		if (typeof deviceModelInfo == "undefined" || deviceModelInfo == null) {
			throw "deviceModelInfo object not defined.";
		} else {
			return deviceModelInfo;
		}
	},

	getMoMap: function() {
		console.log("_ucc.getMoMap called ");
		
		if (typeof moMap == "undefined" || moMap == null) {
			throw "moMap object not defined.";
		} else {
			return moMap;
		}
	},
	
	getMoData: function(uri) {
		console.log("_ucc.getMoData("+uri+") called ");
		
		if (typeof moMap == "undefined" || moMap == null) {
			throw "moMap object not defined.";
		} else {
			
			return moMap[uri];
			
		}
		
	},

	getProfileList: function() {
		console.log("_ucc.getProfileList called ");
		
		if (typeof profileList == "undefined" || profileList == null) {
			throw "profileList object not defined.";
		} else {
			return profileList;
		}
	},
	
	getProfileInfo: function(uri) {
		
		if (typeof profileList == "undefined" || profileList == null) {
			throw "profileList object not defined.";
		} else {
			for (var id in profileList) {
				var profile = profileList[id];
				if (profile.resourceUri == uri) {
					return profile;
				}
			}
			return null;
		}
		
	},
	
	getErrorCodeName: function(uri, code) {
		var profile = _ucc.getProfileInfo(uri);
		
		try {
			var name = "";
			for (var i=0; i<profile.errorCodeList.length; i++) {
				var errCodeInfo = profile.errorCodeList[i];
				if (parseInt(errCodeInfo.errorCode) == parseInt(code)) {
					name = errCodeInfo.errorName;
					break;
				}
			}
			return name;
		} catch (e) {
			console.error("_ucc.getErrorCodeName("+uri+","+code+")");
			return null;
		}
	},
	
	unitStringMap: {"By":"bytes", "KB":"kbytes", "MB":"MB", "Pe":"%", "Ce":"℃", "Vo":"V", "MA":"mA", 
					"dBm":"dBm", "La":"", "Lo":"", "Al":"", "Pp":"ppm", "PeL":"%LEL", 
					"Pr":"%RH", "Pb":"ppb", "CM":"cm"},
	getUnitString: function(uri) {
		
		var unit = "";
		var profile = _ucc.getProfileInfo(uri);
		
		if (profile == null || typeof profile == "undefined") {
			unit = "undefined";
		} else {
			unit = _ucc.unitStringMap[profile.unit];
			if (unit == null || typeof unit == "undefined") {
				unit = "";
			}
		}
		
		return unit;		
	},
	
	getErrorCodeList: function() {
		
		var errCode = _ucc.getProfileInfo("/2/-/29");
		return errCode.errorCodeList;
		
	},
	
	getErrorCode: function(errCode) {
		
		var list = _ucc.getErrorCodeList();
		for (var i=0; i<list.length; i++) {
			if (parseInt(list[i].errorCode) == parseInt(errCode)) {
				return list[i];
			}
		}
		return null;
		
	},
	

	getTimeString: function(date) {
		var timeStr = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
		console.log("getTimeString return "+timeStr);
		return timeStr;
	},
	
	getShortTimeString: function(date) {
		var timeStr = (date.getHours()<10?"0"+date.getHours():date.getHours()) +":"
				+(date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes())+":"
				+(date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds());
		console.log("getShortTimeString return "+timeStr);
		return timeStr;
	}
};
