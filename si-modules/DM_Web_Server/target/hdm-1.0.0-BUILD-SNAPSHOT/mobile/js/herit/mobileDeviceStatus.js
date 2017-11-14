// - 페이지 로딩시 1회 수행
function initUI() {
	console.log("initUI called");
	
	$("#search_deviceModel").change(function() {
		var deviceModel = $("#search_deviceModel").val();
		if (deviceModel == 'choose') {
			return;
		} else {
			deviceModelInfo();
		}
	});
}
/** 디바이스 선택 및 조회 */
function deviceModelInfo() {
	console.log("deviceModelInfo called");
	var tokens = $("#search_deviceModel").val().split("|");
	var id = tokens[0];
	
	$.ajax({
	  type: "GET",
	  dataType: "json",
	  url: "/mobile/api/hdp/deviceModel/info/get.do?id="+id
	})
	.done(function(msg) { 
		deviceModelInfoHandler(msg);
		});
}

function deviceModelInfoHandler(msg) {
	console.log("deviceModelInfoHandler called");
	console.log(JSON.stringify(msg));
	
	deviceStatusList();
	
	getDeviceId();
}

/** 해당 디바이스의 통계 데이터 리스트 */
function deviceStatusList(context, loading) {
	console.log("deviceStatusList called");
	var tokens = $("#search_deviceModel").val().split("|");
	var id = tokens[0];
	var oui = tokens[1];
	var modelName = tokens[2];
	console.log("id : " + id + " oui : " + oui + " modelName : " + modelName);
	
	var context = {"param":{"id":id, "oui":oui, "modelName":modelName}, "handler":deviceStatusListHandler};
	hdb_get_deviceProfile_list(context, false);
}

function deviceStatusListHandler(msg, context) {
	console.log("deviceStatusListHandler called");
	console.log("context:" + JSON.stringify(context));
	console.log("msg:" + JSON.stringify(msg));
	
	try {
		if (msg.result == 0) {
			$("#search_deviceProfile").empty();
			$("#search_deviceProfile").append("<option value='choose'>통계 데이터 항목</option>");
			for (var i=0; i < msg.content.list.length; i++) {
				var profile = msg.content.list[i];
				$("#search_deviceProfile").append("<option value='"+profile.displayName+"'>"+profile.displayName+"</option>");
			}
			
		}
	} catch (e) {
		console.log("exception in deviceStatusListHandler ");
		console.log(e);
	}
	
	
}

/** 데이터를 가져오기 위한 DEVICE_ID 검색!!!!! */
function getDeviceId(context, loading) {
	console.log("getDeviceId called");
	var tokens = $("#search_deviceModel").val().split("|");
	var id = tokens[0];
	var oui = tokens[1];
	var modelName = tokens[2];
	console.log("id : " + id + " oui : " + oui + " modelName : " + modelName);
	
	var context = {"param":{"id":id, "oui":oui, "modelName":modelName}, "handler":getDeviceIdHandler};
	hdb_searchDevice_for_select(context, false);
}

var deviceId;
function getDeviceIdHandler(msg, context) {
	console.log("getDeviceIdHandler called");
	console.log("context: "+ JSON.stringify(context));
	console.log("msg: " + JSON.stringify(msg));
	
	deviceId = msg.content.info.deviceId;
	console.log("deviceId >>>>>>>>>>>>>> " + deviceId);
	
}

//내 생각에는 쿼리가 다르니까 두개를 만들어서 조건을 걸어줘야 할 듯

/** 그래프 */
var search_standard;
var search_deviceProfile;
function drawGraphHandler() {
	console.log("drawGraphHandler called");
	
	search_standard = $("#search_standard option:selected").val();
	search_deviceProfile = $("#search_deviceProfile option:selected").val();
	console.log("search_standard option ->>> selected : " + search_standard);
	console.log("search_deviceProfile option ->>> selected : " + search_deviceProfile);
	
	//데이터
	var context = {"param":{"deviceId":deviceId, "resourceName":search_deviceProfile}, "handler":getStatusAvgHandler};
	
	if (search_standard == 'WEEK') {
		hdh_statusWeekAvg_list(context, false);
	} else if (search_standard == 'TIME') {
		hdh_stautsTimeAvg_list(context, false);
	}
}

function getStatusAvgHandler(msg, context) {
	console.log("getStatusAvgHandler called");
	console.log("context: " + JSON.stringify(context));
	console.log("msg: " + JSON.stringify(msg));
	
	//그래프 생성
	$(".mobile_box").css('display', 'block');
	drawGraph(search_standard, search_deviceProfile, msg);
	google.setOnLoadCallback(drawGraph);
	
	detailDataInfo(search_standard, search_deviceProfile, msg);
}

/** 상세 데이터 정보 */
function detailDataInfo(search_standard, search_deviceProfile, msg) {
	console.log("detailDataInfo called");
	console.log("msg: " + JSON.stringify(msg));
	
	//table 생성
	var getData;
	try {
		if (msg.result == 0) {
			$(".mobile_box").css('display', 'block');
			$("#infoTable").empty();
			$("#title_put").empty();
			$("#title_put").append("<h5>"+search_deviceProfile+" 데이터 평균</h5>");
			$("#infoTable").append("<tr><th>"+search_standard+"</th><th>데이터 평균</th></tr>")
			
			for (var i=0; i < msg.content.list.length; i++) {
				var avgData = msg.content.list[i];
				if (search_standard == 'WEEK') {
					getData = avgData.data;
					var avg = getData.toFixed(2);
					$("#infoTable").append("<tr><td>"+avgData.dayName+"</td><td>"+avg+"</td></tr>");
				} else if (search_standard == 'TIME') {
					var getTime = avgData.time;
					var hour = getTime.substring(0, 2);
					getData = avgData.data;
					var avg = getData.toFixed(2);
					$("#infoTable").append("<tr><td>"+hour+"</td><td>"+avg+"</td></tr>");
				}
				
			}
		}
	} catch (e) {
		console.log("exception in detailDataInfo ");
		console.log(e);
	}
}

//google.load('visualization', '1', {packages: ['line']});
google.load('visualization', '1', {packages: ['corechart']});

function drawGraph(search_standard, search_deviceProfile, msg) {
	console.log("drawGraph called");
	console.log("msg: " + JSON.stringify(msg));
	
	var avgData;
	var avg_dayName = new Array();
	var avg_time = new Array();
	var avg_data = new Array();
	try {
		if (msg.result == 0) {
			for (var i=0; i < msg.content.list.length; i++) {
				avgData = msg.content.list[i];
				
				if (search_standard == 'WEEK') {
					console.log("WEEK");
					var dayName = avgData.dayName;
					avg_dayName[i] = dayName;
					console.log("avg_dayName[i] >> " + avg_dayName[i]);
				} else if (search_standard == 'TIME') {
					console.log("TIME");
					var time = avgData.time;
					var hour = time.substring(0, 2);
					avg_time[i] = hour;
					console.log("avg_time[i] >> " + avg_time[i]);
				}
				
				var dataAvg = avgData.data;
				avg_data[i] = dataAvg;
				console.log("avg_data[i] >> " + avg_data[i]);
				
				var dataArray = [[search_standard, search_deviceProfile]];
			}
		}
	} catch (e) {
		console.log("exception in drawGraph ");
		console.log(e);
	}
	
	if (search_standard == 'WEEK') {
		for (var y=0; y < avg_data.length; y++){
			dataArray.push([avg_dayName[y], avg_data[y]]);
		}
	} else if (search_standard == 'TIME') {
		for (var y=0; y < avg_data.length; y++){
			dataArray.push([avg_time[y], avg_data[y]]);
		}
	}
	
	
	var data = google.visualization.arrayToDataTable(dataArray);
	
	
	/*var data = google.visualization.arrayToDataTable([
	  
	  [search_standard, search_standard],
	
	  ['00:00 ~ 01:00', 1762],
	  ['01:00 ~ 02:00', 1658],
	  ['02:00 ~ 03:00', 1749],
	  ['04:00 ~ 05:00', 1603]
	]);*/
	
	var options = {
			
		title: search_deviceProfile,
		legend: {position: 'bottom'},
		curveType: 'function',
		width: 330,
	    height: 400
	};
	
	
	$("#mobile_graphbox").empty();
	$("#mobile_graphbox").append("<div class='ibox-content'><div id='linechart_material'></div></div>")
	var chart = new google.visualization.LineChart(document.getElementById('linechart_material'));
    chart.draw(data, options);
	
}