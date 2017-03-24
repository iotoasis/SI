
function singleTrafficGraph(gColor, threshold) {

	this.plot = null;
	this.previousPoint = null;

	this.chartPlaceHolder = null;
	this.maxItem = 100;
	this.trafficInterval = 20;
	this.arrRx = [];
	this.arrTx = [];
	this.data = [{data: this.arrRx, label: ""}, {data: this.arrTx, label: ""}];
	// this.data = [{ data: this.arrRx, label: "RX (bytes)"}, { data:
	// this.arrTx, label: "TX (bytes)" }];

	this.curTraffic = {
		"rx" : 0,
		"tx" : 0
	};
	this.traffic = new Array();
	
	this.option = {
		series : {
			lines : {
				show : true,
				lineWidth : 3,
				fill : true,
				fillColor : {
					colors : [ {
						opacity : 0.1
					}, {
						opacity : 0
					} ]
				}
			},
			/*
			 * threshold: { below: gThreshold, color: gThresColor },
			 */
			
			threshold: threshold,
			points : {
				show : false,
				lineWidth : 5,
				radius : 1
			},
			shadowSize : 0,
			stack : true
		},
		grid : {
			hoverable : true,
			clickable : true,
			tickColor : "#f9f9f9",
			borderWidth : 0
		},
		legend : {
			// show: false
			labelBoxBorderColor : "#fff"
		},
		colors : [ gColor, "#30a0eb" ],
		xaxis : {
			ticks : [],
			font : {
				size : 9,
				family : "Open Sans, Arial",
				variant : "small-caps",
				color : "#697695"
			}
		},
		yaxis : {
			ticks : 3,
			tickDecimals : 0,
			font : {
				size : 12,
				color : "#9da3a9"
			}
		}
	};
}

singleTrafficGraph.prototype.initTrafficGraph = function(chartDiv) {
	this.chartPlaceHolder = chartDiv;
	for (var i = 0; i < this.maxItem; i++) {
		this.option.xaxis.ticks[i] = [ i, i * this.trafficInterval + "" ];
		// this.dataCount++;
	}
	
	this.plot = $.plot(this.chartPlaceHolder, this.data, this.option);

	$(chartDiv).bind(
			"plothover",
			function(event, pos, item) {
				if (item) {
					if (previousPoint != item.dataIndex) {
						previousPoint = item.dataIndex;

						$("#tooltip").remove();
						var x = item.datapoint[0].toFixed(0), y = item.datapoint[1]
								.toFixed(0);

						/*
						 * var month =
						 * item.series.xaxis.ticks[item.dataIndex].label;
						 */

						/*
						 * showTooltip(item.pageX, item.pageY, item.series.label + "
						 * of " + month + " sec : " + y);
						 */
					}
				} else {
					$("#tooltip").remove();
					previousPoint = null;
				}
			});
	
}


singleTrafficGraph.prototype.showTooltip = function(x, y, contents) {
	$('<div id="tooltip">' + contents + '</div>').css({
		position : 'absolute',
		display : 'none',
		top : y - 30,
		left : x - 50,
		color : "#fff",
		padding : '2px 5px',
		'border-radius' : '6px',
		'background-color' : '#000',
		opacity : 0.80
	}).appendTo("body").fadeIn(200);
}


singleTrafficGraph.prototype.getTwoDigit = function(num) {
	if (num < 10) {
		return "0" + num;
	} else {
		return "" + num;
	}
}

// 초기
singleTrafficGraph.prototype.addTrafficEx = function(rx, tx) {
	var rxd = rx;
	var txd = tx - this.curTraffic.tx;
	// var txd = tx;
	this.curTraffic.rx = rx;
	// this.curTraffic.tx = tx;

	this.traffic[this.traffic.length] = {
		"rx" : rx,
		"tx" : tx.substring(11, 19),
		// "time" : strTime
	};

	if (rxd == rx && txd == tx) {
		return;
	}

	// maxItem을 넘어가면 옆으로 밀려남
	if (this.arrRx.length > this.maxItem) {
		for (var i = 0; i < this.maxItem - 1; i++) {
			this.arrRx[i][1] = this.arrRx[i + 1][1];
		}
		this.traffic.shift();

	} else {
		
		this.arrRx[this.arrRx.length] = [this.arrRx.length, rxd];
		this.arrTx[this.arrTx.length] = [this.arrTx.length, txd];
	}
	
	this.option.xaxis.ticks = new Array();
	var unit = this.traffic.length > 10 ? parseInt(this.traffic.length / 6) : 1;
	for (i = 0; i < this.traffic.length; i+=unit) {
		this.option.xaxis.ticks[i] = [ i, this.traffic[i].tx ];
	}
}

// 초기
singleTrafficGraph.prototype.showTraffic = function() {
	this.data = [ {
		data : this.arrRx,
		label : ""
	}];
	var plot = $.plot(this.chartPlaceHolder, this.data, this.option);
	
}

// 주기적으로
singleTrafficGraph.prototype.addTraffic = function(rx, tx) {
	var rxd = rx;
	var txd = tx - this.curTraffic.tx;
	this.curTraffic.rx = rx;

	var curTime = new Date();
	/*var strTime = this.getTwoDigit(curTime.getHours() +":"+ curTime.getMinutes()) + ":"
			+ this.getTwoDigit(curTime.getSeconds());*/
	var strTime = _ucc.getShortTimeString(new Date(curTime));
	
	
	this.traffic[this.traffic.length] = {
		"rx" : rx,
		"tx" : strTime
		// "time" : strTime
	};

	if (rxd == rx && txd == tx) {
		return;
	}

	this.option.xaxis.ticks = new Array();
	
	// maxItem을 넘어가면 옆으로 밀려남
	if (this.arrRx.length > this.maxItem) {
		this.arrRx[this.arrRx.length] = [ this.arrRx.length, rxd];
		for (var i = 0; i < this.maxItem + 1; i++) {
			this.arrRx[i][1] = this.arrRx[i + 1][1];
		}
		this.traffic.shift();
		this.arrRx.shift();

	} else {
		
		this.arrRx[this.arrRx.length] = [ this.arrRx.length, rxd];
	}

	
	// 시:분:초 찍어줌
	var unit = this.traffic.length > 10 ? parseInt(this.traffic.length / 6) : 1;
	for (i = 0; i < this.traffic.length; i+=unit) {
		this.option.xaxis.ticks[i] = [ i, this.traffic[i].tx ];
	}
	
	/*
	 * var size = this.traffic.length; var plus = size-1;
	 * this.option.xaxis.ticks[plus] = [ plus, this.traffic[plus].time ];
	 */
	
	for(i=0; i<this.arrRx.length; i++) {
		this.arrRx[i][0] = i;
	}
	
	this.data = [ {
		data : this.arrRx,
		label : ""
	}];
	
	var plot = $.plot(this.chartPlaceHolder, this.data, this.option);
}


singleTrafficGraph.prototype.getCurValues = function() {
	if (this.arrRx.length == 0) {
		return ['-', '-'];
	} else {
		return [this.arrRx[this.arrRx.length-1][1]];		
	}
}
