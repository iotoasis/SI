// Custom scripts
$(document).ready(function () {
	
	dssl_mem.init();

	console.log("device.status.single_line_graph js initialized");
});
//var graph = null;
var dssl_mem = {
	divs: null,
	resource: null,
	init: function() {
		dssl_mem.divs = $(".hit-component-dsslg_mem");
		dssl_mem.resources = [];
		var divIdx;
		var thresholdMap;
		var key;
		//var resUri;
		
		console.log(dssl_mem.divs);
		$.each(dssl_mem.divs, function(idx, div) {
			
			var graph;
			var gColor;
			var gThreshold;
			var gThresColor;
				
			$.each(_ucc.getResListWithComponentDiv(div), function(ix, v) {
				
				dssl_mem.resources.push(v);
				
				//resUri = val.resourceUri;
				resUri = v;
				
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
			
			
				graph.initTrafficGraph($(div).find(".dsslg_mem-chart"));
				div.graph = graph;
				
				/** 각 div에 id를 추가하여 어떤 component 인지 구별 */
				var divsName = $(div).find(".dsslg_mem-chart");
				var idSelector = divsName.selector;
				//$(div).attr('id', idSelector+"_"+idx);
				$(div).attr('id', idSelector+"_"+ix);
				//var divId = $(div).find(idSelector+"_"+idx);
				var divId = $(div).find(idSelector+"_"+ix);
				var divSelector = divId.selector;
				
				
				//var val = _ucc.getMoData(dssl_mem.resources[idx]);
				var val = _ucc.getMoData(dssl_mem.resources[ix]);
				
				var rname = val.resourceName;
				
				if(rname.indexOf("Total") > 0) {
					$("#h3_total_mem").text(Math.round(val.data/1000) + " MByte");
				} else {
					try {
						
						dssl_mem.getStatusHist(val.resourceUri, val.resourceName, divSelector);
						
					} catch(e) {}

					if (val == null || typeof val == "undefined") {
						//console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSLG:"+dssl_mem.resources[idx]+")");
						console.error("디바이스에 대해서 MO데이터가 정의되지 않았습니다. (DSSLG:"+dssl_mem.resources[ix]+")");
						return;
					}	
					
					var unit = _ucc.getUnitString(dssl_mem.resources[idx]);
					
					val.data = Math.round(val.data/1000);		// added in 2017-09-25
		
					$(div).find(".dsslg_mem-res-1-unit").text(unit); 
					
					$(div).find(".dsslg_mem-res-1-value").text(val.data);
					$(div).find(".dsslg_mem-res-1-time").text("상태시간 : " + val.dataTime);
					
					/** 숫자 깜빡임 */
					var max;
					var min;
					if(key == "between"){
						max = Number(thresholdMap[key][0]);
						min = Number(thresholdMap[key][1]);
						
						if (Number(val.data) < min || Number(val.data) > max) {
							dssl_mem.numberBlink(div);
						} else {
							$(div).find(".dsslg_mem-res-1-value")[0].blinkFlag = false;
							$(div).find(".dsslg_mem-res-1-unit")[0].blinkFlag = false;
						}
					} else if (key == "above"){
						min = Number(thresholdMap[key]);
						if (Number(val.data) < min) {
							dssl_mem.numberBlink(div);
						} else {
							$(div).find(".dsslg_mem-res-1-value")[0].blinkFlag = false;
							$(div).find(".dsslg_mem-res-1-unit")[0].blinkFlag = false;
						}
					} else {
						max = Number(thresholdMap[key]);
						if (Number(val.data) > max) {
							dssl_mem.numberBlink(div);
							
						} else {
							$(div).find(".dsslg_mem-res-1-value")[0].blinkFlag = false;
							$(div).find(".dsslg_mem-res-1-unit")[0].blinkFlag = false;
						}
					}
				}
				
				
			});
			
		});
		_ucc.setResourceHandler("dsslg_mem", dssl_mem.resources, dssl_mem.refresh);
	},
	
	numberBlink: function(div) {
		if ($(div).find(".dsslg_mem-res-1-value")[0].blinkFlag != true) {
			$(div).find(".dsslg_mem-res-1-value")[0].blinkFlag = true;
			$(div).find(".dsslg_mem-res-1-unit")[0].blinkFlag = true;
			dssl_mem.blink($(div).find(".dsslg_mem-res-1-value"), -1, 750);
			dssl_mem.blink($(div).find(".dsslg_mem-res-1-unit"), -1, 750);
		}
	},
	
	blink: function(elem, times, speed) {
		if (times > 0 || times < 0) {
			if ($(elem).hasClass("blink")) $(elem).removeClass("blink");
			else $(elem).addClass("blink");
		}
		clearTimeout(function() {
			dssl_mem.blink(elem, times, speed);
		});
		if (times > 0 || times < 0) {
			if (elem[0].blinkFlag == true) {
				setTimeout(function() {
					dssl_mem.blink(elem, times, speed);
				}, speed);
			} else {
				if ($(elem).hasClass("blink")) $(elem).removeClass("blink");
			}
		}
	},
	
	/** 첫 그래프 화면에 가장 최근 20개 까지의 데이터 그려주기 위한 조회 */
	getStatusHist: function(resourceUri, resrouceName, divSelector) {
		console.log("getStatusHist called()");
		
		var context = {"resourceUri":resourceUri, "resourceName":resrouceName, "divSelector":divSelector, "handler":dssl_mem.getStatusHistHandler};
		hdh_get_statusHist_list(context, false);
	},
	
	getStatusHistHandler: function(msg, context) {
		console.log("getStatusHistHandler called()");
		console.log("msg: "+JSON.stringify(msg));
		console.log("context: "+JSON.stringify(context));
	
		var divSelector = context.divSelector;
		var graphDraw = $(dssl_mem.divs).find(divSelector);
		divIdx = divSelector.substring(17, 18);
		
		for (var i=msg.content.list.length-1; i >= 0; i--) {
			var histData = msg.content.list[i];
			if (histData != null && graphDraw.prevObject[divIdx].id == divSelector) {
				graphDraw.prevObject[divIdx].graph.addTrafficEx(histData.data, histData.dataTime);
			}
		}
		
		graphDraw.prevObject[divIdx].graph.showTraffic();
	},

	refresh: function(type) {
		console.log("dsslg_mem.refresh("+type+") called");
		$.each(dssl_mem.divs, function(idx, div) {
			dssl_mem.refreshComponent(div);
		});
	},
	
	refreshComponent: function(div) {
		console.log("refreshComponent----------------------------------------");
		var resources = _ucc.getResListWithComponentDiv(div);

		var val = _ucc.getMoData(resources[0]);
		if (val == null || typeof val == "undefined") {
			console.error("dsslg_mem.refreshComponent undefined resource"+resources[0]);
			return;
		}
		
		val.data = Math.round((_ucc.getMoData(resources[1]).data-val.data)/1000);	// added in 2017-09-25
		
		
		thresholdMap = _ucc.getThresholdWithComponentDiv(div);
		
		key = Object.keys(thresholdMap);
		
		/** 숫자 깜빡임 */
		var max;
		var min;
		if(key == "between"){
			max = Number(thresholdMap[key][0]);
			min = Number(thresholdMap[key][1]);
			
			if (Number(val.data) < min || Number(val.data) > max) {
				dssl_mem.numberBlink(div);
			} else {
				$(div).find(".dsslg_mem-res-1-value")[0].blinkFlag = false;
				$(div).find(".dsslg_mem-res-1-unit")[0].blinkFlag = false;
			}
		} else if (key == "above"){
			min = Number(thresholdMap[key]);
			if (Number(val.data) < min) {
				dssl_mem.numberBlink(div);
			} else {
				$(div).find(".dsslg_mem-res-1-value")[0].blinkFlag = false;
				$(div).find(".dsslg_mem-res-1-unit")[0].blinkFlag = false;
			}
		} else {
			max = Number(thresholdMap[key]);
			if (Number(val.data) > max) {
				dssl_mem.numberBlink(div);
				
			} else {
				$(div).find(".dsslg_mem-res-1-value")[0].blinkFlag = false;
				$(div).find(".dsslg_mem-res-1-unit")[0].blinkFlag = false;
			}
		}

	
		div.graph.addTraffic(val.data);
		
		////var vals = div.graph.getCurValues();
		var dataTime = val.dataTime;
		
		$(div).find(".dsslg_mem-res-1-value").text(val.data=='-' ? '-' : val.data);
		$(div).find(".dsslg_mem-res-1-time").text("상태시간 : " + dataTime);
		
	}
}