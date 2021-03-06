// Custom scripts
$(document).ready(function () {
	
	dsslg_tr069.init();

	console.log("device.status.single_line_graph js initialized");
});
//var graph = null;
var dsslg_tr069 = {
	divs: null,
	resource: null,
	init: function() {
		dsslg_tr069.divs = $(".hit-component-dsslg_tr069");
		dsslg_tr069.resources = [];
		var divIdx;
		var thresholdMap;
		var key;
		//var resUri;
		$.each(dsslg_tr069.divs, function(idx, div) {
			
			var graph;
			var gColor;
			var gThreshold;
			var gThresColor;

			$.each(_ucc.getResListWithComponentDiv(div), function(ix, val) {
				dsslg_tr069.resources.push(val);
				
				resUri = val.resourceUri;
				
				thresholdMap = _ucc.getThresholdWithComponentDiv(div);
				key = Object.keys(thresholdMap);
				if(key == "between"){
					gColor="#EB3232";
					threshold = [
					             {below: thresholdMap[key][1],
					             color: "#EB3232"},
					             {below: thresholdMap[key][0],
					             color: "#00B1B3"}
					             ];
				} else if (key == "above"){
					gColor="#00B1B3";
					threshold = [
					             {below: thresholdMap[key],
					             color: "#EB3232"}
					             ];
				} else {
					gColor="#EB3232";
					threshold = [
					             {below: thresholdMap[key],
					             color: "#00B1B3"}
					             ];
				}
				
				/*gColor = "#FA8300";
				gThreshold = "300";
				gThresColor = "#00B1B3";*/
				
				graph = new singleTrafficGraph(gColor, threshold);
			
			
				graph.initTrafficGraph($(div).find(".dsslg_tr069-chart"));
				div.graph = graph;
				
				/** 각 div에 id를 추가하여 어떤 component 인지 구별 */
				var divsName = $(div).find(".dsslg_tr069-chart");
				var idSelector = divsName.selector;
				$(div).attr('id', idSelector+"_"+idx);
				var divId = $(div).find(idSelector+"_"+idx);
				var divSelector = divId.selector;
				
				
				var val = _ucc.getMoData(dsslg_tr069.resources[idx]);
				
				try {
					
					dsslg_tr069.getStatusHist(val.resourceUri, val.resourceName, divSelector);
					
				} catch(e) {}

				if (val == null || typeof val == "undefined") {
					console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSLG:"+dsslg_tr069.resources[idx]+")");
					return;
				}	
				
				/*var val = _ucc.getMoData(dsslg_tr069.resources[idx]);
				
				if (val == null || typeof val == "undefined") {
					console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSLG:"+dsslg_tr069.resources[idx]+")");
					return;
				}	
				dsslg_tr069.getStatusHist(val.resourceUri, val.resourceName, divSelector);*/
				
				var unit = _ucc.getUnitString(dsslg_tr069.resources[idx]);
	
				$(div).find(".dsslg_tr069-res-1-unit").text(unit);
				$(div).find(".dsslg_tr069-res-1-value").text(val.data);
				$(div).find(".dsslg_tr069-res-1-time").text("상태시간 : " + val.dataTime);
				
				/** 숫자 깜빡임 */
				var max;
				var min;
				if(key == "between"){
					max = Number(thresholdMap[key][0]);
					min = Number(thresholdMap[key][1]);
					if (Number(val.data) < min || Number(val.data) > max) {
						dsslg_tr069.numberBlink(div);
					} else {
						$(div).find(".dsslg_tr069-res-1-value")[0].blinkFlag = false;
						$(div).find(".dsslg_tr069-res-1-unit")[0].blinkFlag = false;
					}
				} else if (key == "above"){
					min = Number(thresholdMap[key]);
					if (Number(val.data) < min) {
						dsslg_tr069.numberBlink(div);
					} else {
						$(div).find(".dsslg_tr069-res-1-value")[0].blinkFlag = false;
						$(div).find(".dsslg_tr069-res-1-unit")[0].blinkFlag = false;
					}
				} else {
					max = Number(thresholdMap[key]);
					if (Number(val.data) > max) {
						dsslg_tr069.numberBlink(div);
						
					} else {
						$(div).find(".dsslg_tr069-res-1-value")[0].blinkFlag = false;
						$(div).find(".dsslg_tr069-res-1-unit")[0].blinkFlag = false;
					}
				}
				
			});
			
		});
		_ucc.setResourceHandler("dsslg_tr069", dsslg_tr069.resources, dsslg_tr069.refresh);
	},
	
	numberBlink: function(div) {
		if ($(div).find(".dsslg_tr069-res-1-value")[0].blinkFlag != true) {
			$(div).find(".dsslg_tr069-res-1-value")[0].blinkFlag = true;
			$(div).find(".dsslg_tr069-res-1-unit")[0].blinkFlag = true;
			dsslg_tr069.blink($(div).find(".dsslg_tr069-res-1-value"), -1, 750);
			dsslg_tr069.blink($(div).find(".dsslg_tr069-res-1-unit"), -1, 750);
		}
	},
	
	blink: function(elem, times, speed) {
		if (times > 0 || times < 0) {
			if ($(elem).hasClass("blink")) $(elem).removeClass("blink");
			else $(elem).addClass("blink");
		}
		clearTimeout(function() {
			dsslg_tr069.blink(elem, times, speed);
		});
		if (times > 0 || times < 0) {
			if (elem[0].blinkFlag == true) {
				setTimeout(function() {
					dsslg_tr069.blink(elem, times, speed);
				}, speed);
			} else {
				if ($(elem).hasClass("blink")) $(elem).removeClass("blink");
			}
		}
	},
	
	/** 첫 그래프 화면에 가장 최근 20개 까지의 데이터 그려주기 위한 조회 */
	getStatusHist: function(resourceUri, resrouceName, divSelector) {
		console.log("getStatusHist called()");
		
		var context = {"resourceUri":resourceUri, "resourceName":resrouceName, "divSelector":divSelector, "handler":dsslg_tr069.getStatusHistHandler};
		hdh_get_statusHist_list(context, false);
	},
	
	getStatusHistHandler: function(msg, context) {
		console.log("getStatusHistHandler called()");
		console.log("msg: "+JSON.stringify(msg));
		console.log("context: "+JSON.stringify(context));
	
		var divSelector = context.divSelector;
		var graphDraw = $(dsslg_tr069.divs).find(divSelector);
		divIdx = divSelector.substring(13, 14);
		
		for (var i=msg.content.list.length-1; i >= 0; i--) {
			var histData = msg.content.list[i];
			if (histData != null && graphDraw.prevObject[divIdx].id == divSelector) {
				graphDraw.prevObject[divIdx].graph.addTrafficEx(histData.data, histData.dataTime);
			}
		}
		
		graphDraw.prevObject[divIdx].graph.showTraffic();
	},

	refresh: function(type) {
		console.log("dsslg_tr069.refresh("+type+") called");
		$.each(dsslg_tr069.divs, function(idx, div) {
			dsslg_tr069.refreshComponent(div);
			
		});
	},
	
	refreshComponent: function(div) {
		console.log("refreshComponent----------------------------------------");
		var resources = _ucc.getResListWithComponentDiv(div);

		var val = _ucc.getMoData(resources[0]);
		if (val == null || typeof val == "undefined") {
			console.error("dsslg_tr069.refreshComponent undefined resource"+resources[0]);
			return;
		}
		
		thresholdMap = _ucc.getThresholdWithComponentDiv(div);
		key = Object.keys(thresholdMap);
		
		/** 숫자 깜빡임 */
		var max;
		var min;
		if(key == "between"){
			max = Number(thresholdMap[key][0]);
			min = Number(thresholdMap[key][1]);
			if (Number(val.data) < min || Number(val.data) > max) {
				dsslg_tr069.numberBlink(div);
			} else {
				$(div).find(".dsslg_tr069-res-1-value")[0].blinkFlag = false;
				$(div).find(".dsslg_tr069-res-1-unit")[0].blinkFlag = false;
			}
		} else if (key == "above"){
			min = Number(thresholdMap[key]);
			if (Number(val.data) < min) {
				dsslg_tr069.numberBlink(div);
			} else {
				$(div).find(".dsslg_tr069-res-1-value")[0].blinkFlag = false;
				$(div).find(".dsslg_tr069-res-1-unit")[0].blinkFlag = false;
			}
		} else {
			max = Number(thresholdMap[key]);
			if (Number(val.data) > max) {
				dsslg_tr069.numberBlink(div);
				
			} else {
				$(div).find(".dsslg_tr069-res-1-value")[0].blinkFlag = false;
				$(div).find(".dsslg_tr069-res-1-unit")[0].blinkFlag = false;
			}
		}

	
		div.graph.addTraffic(val.data);
		
		////var vals = div.graph.getCurValues();
		var dataTime = val.dataTime;
		$(div).find(".dsslg_tr069-res-1-value").text(val.data=='-' ? '-' : val.data);
		$(div).find(".dsslg_tr069-res-1-time").text("상태시간 : " + dataTime);
		
	}
}