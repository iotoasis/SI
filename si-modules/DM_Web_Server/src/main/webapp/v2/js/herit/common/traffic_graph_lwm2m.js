
function trafficGraph() {

	this.plot = null;
	this.previousPoint = null;

	this.chartPlaceHolder = null;
	this.maxItem = 10;
	this.trafficInterval = 60;
	this.arrRx = [];
	this.arrTx = [];
	this.data = [{ data: this.arrRx, label: "RX (bytes)"}, { data: this.arrTx, label: "TX (bytes)" }];

	this.curTraffic = {
		"rx" : 0,
		"tx" : 0
	};
	this.traffic = new Array();
	
	this.option = {
		series : {
			lines : {
				show : true,
				lineWidth : 1,
				fill : true,
				fillColor : {
					colors : [ {
						opacity : 0.1
					}, {
						opacity : 0.13
					} ]
				}
			},
			points : {
				show : true,
				lineWidth : 2,
				radius : 3
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
		colors : [ "#a7b5c5", "#30a0eb" ],
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

trafficGraph.prototype.initTrafficGraph = function(chartDiv) {
	this.chartPlaceHolder = chartDiv;
	for (var i = 0; i < this.maxItem; i++) {
		this.option.xaxis.ticks[i] = [ i, i * this.trafficInterval + "" ];
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

						var month = item.series.xaxis.ticks[item.dataIndex].label;

						showTooltip(item.pageX, item.pageY, item.series.label
								+ " of " + month + " sec : " + y);
					}
				} else {
					$("#tooltip").remove();
					previousPoint = null;
				}
			});
	
}


trafficGraph.prototype.showTooltip = function(x, y, contents) {
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


trafficGraph.prototype.getTwoDigit = function(num) {
	if (num < 10) {
		return "0" + num;
	} else {
		return "" + num;
	}
}
trafficGraph.prototype.addTraffic = function(rx, tx) {
	rx = parseInt(rx) * 1024;
	tx = parseInt(tx) * 1024;
	var rxd = rx - this.curTraffic.rx;
	var txd = tx - this.curTraffic.tx;
	this.curTraffic.rx = rx;
	this.curTraffic.tx = tx;

	var curTime = new Date();
	// var strTime = getTwoDigit(curTime.getHours()) +":"+
	// getTwoDigit(curTime.getMinutes()) +":"+
	// getTwoDigit(curTime.getSeconds());
	var strTime = this.getTwoDigit(curTime.getMinutes()) + ":"
			+ this.getTwoDigit(curTime.getSeconds());
	this.traffic[this.traffic.length] = {
		"rx" : rx,
		"tx" : tx,
		"time" : strTime
	};

	if (rxd == rx && txd == tx) {
		return;
	}

	if (this.arrRx.length > this.maxItem) {
		for (var i = 0; i < this.maxItem - 1; i++) {
			this.arrRx[i][1] = this.arrRx[i + 1][1];
			this.arrTx[i][1] = this.arrTx[i + 1][1];
		}
		this.arrRx[this.maxItem - 1][1] = rxd / this.trafficInterval;
		this.arrTx[this.maxItem - 1][1] = txd / this.trafficInterval;
		this.traffic.shift();

	} else {
		this.arrRx[this.arrRx.length] = [ this.arrRx.length, rxd / this.trafficInterval ];
		this.arrTx[this.arrTx.length] = [ this.arrTx.length, txd / this.trafficInterval ];
	}
	this.option.xaxis.ticks = new Array();
	for (i = 0; i < this.traffic.length; i++) {
		this.option.xaxis.ticks[i] = [ i, this.traffic[i].time ];
	}
	this.data = [ {
		data : this.arrRx,
		label : "RX (KB)"
	}, {
		data : this.arrTx,
		label : "TX (KB)"
	} ];
	var plot = $.plot(this.chartPlaceHolder, this.data, this.option);

	console.log("addTraffic " + strTime + " count:" + this.traffic.length + ", rx:"
			+ rx + ", tx:" + tx);
}


trafficGraph.prototype.getCurValues = function() {
	if (this.arrRx.length == 0) {
		return ['-', '-'];
	} else {
		return [this.arrRx[this.arrRx.length-1][1], this.arrTx[this.arrTx.length-1][1]];		
	}
}

















/*
var plot = null;
var previousPoint = null;

var chartPlaceHolder = null;
var maxItem = 10;
var trafficInterval = 60;
var arrRx = [];
var arrTx = [];
var data = [{ data: arrRx, label: "RX (bytes)"}, { data: arrTx, label: "TX (bytes)" }];

var option = {
	series : {
		lines : {
			show : true,
			lineWidth : 1,
			fill : true,
			fillColor : {
				colors : [ {
					opacity : 0.1
				}, {
					opacity : 0.13
				} ]
			}
		},
		points : {
			show : true,
			lineWidth : 2,
			radius : 3
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
	colors : [ "#a7b5c5", "#30a0eb" ],
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
	
	
function initTrafficGraph(chartDiv) {
	chartPlaceHolder = chartDiv;
	for (var i = 0; i < maxItem; i++) {
		option.xaxis.ticks[i] = [ i, i * trafficInterval + "" ];
	}
	
	plot = $.plot(chartPlaceHolder, data, option);

	$(chartDiv).bind(
			"plothover",
			function(event, pos, item) {
				if (item) {
					if (previousPoint != item.dataIndex) {
						previousPoint = item.dataIndex;

						$("#tooltip").remove();
						var x = item.datapoint[0].toFixed(0), y = item.datapoint[1]
								.toFixed(0);

						var month = item.series.xaxis.ticks[item.dataIndex].label;

						showTooltip(item.pageX, item.pageY, item.series.label
								+ " of " + month + " sec : " + y);
					}
				} else {
					$("#tooltip").remove();
					previousPoint = null;
				}
			});
	
}


function showTooltip(x, y, contents) {
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


var curTraffic = {
	"rx" : 0,
	"tx" : 0
};
var traffic = new Array();
function getTwoDigit(num) {
	if (num < 10) {
		return "0" + num;
	} else {
		return "" + num;
	}
}
function addTraffic(rx, tx) {
	rx = parseInt(rx)/1024;
	tx = parseInt(tx)/1024;
	var rxd = rx - curTraffic.rx;
	var txd = tx - curTraffic.tx;
	curTraffic.rx = rx;
	curTraffic.tx = tx;

	var curTime = new Date();
	// var strTime = getTwoDigit(curTime.getHours()) +":"+
	// getTwoDigit(curTime.getMinutes()) +":"+
	// getTwoDigit(curTime.getSeconds());
	var strTime = getTwoDigit(curTime.getMinutes()) + ":"
			+ getTwoDigit(curTime.getSeconds());
	traffic[traffic.length] = {
		"rx" : rx,
		"tx" : tx,
		"time" : strTime
	};

	if (rxd == rx && txd == tx) {
		return;
	}

	if (arrRx.length > maxItem) {
		for (var i = 0; i < maxItem - 1; i++) {
			arrRx[i][1] = arrRx[i + 1][1];
			arrTx[i][1] = arrTx[i + 1][1];
		}
		arrRx[maxItem - 1][1] = rxd / trafficInterval;
		arrTx[maxItem - 1][1] = txd / trafficInterval;
		traffic.shift();

	} else {
		arrRx[arrRx.length] = [ arrRx.length, rxd / trafficInterval ];
		arrTx[arrTx.length] = [ arrTx.length, txd / trafficInterval ];
	}
	option.xaxis.ticks = new Array();
	for (i = 0; i < traffic.length; i++) {
		option.xaxis.ticks[i] = [ i, traffic[i].time ];
	}
	data = [ {
		data : arrRx,
		label : "RX (KB)"
	}, {
		data : arrTx,
		label : "TX (KB)"
	} ];
	var plot = $.plot(chartPlaceHolder, data, option);

	console.log("addTraffic " + strTime + " count:" + traffic.length + ", rx:"
			+ rx + ", tx:" + tx);
}*/