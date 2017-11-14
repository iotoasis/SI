
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
}