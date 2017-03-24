// - 페이지 로딩시 1회 수행
var switchery = null;
var switchery_2 = null;
function initUI() {
	console.log("initUI called");
	
	//스위치 생성
	var elem = document.querySelector('.js-switch');
	switchery = new Switchery(elem, { color: '#1AB394'});
    var elem2 = document.querySelector('.js-switch2');
    switchery_2 = new Switchery(elem2, { color: '#1AB394'});
	
	$("#search_deviceModel").change(function() {
		var deviceModel = $("#search_deviceModel").val();
		if (deviceModel == 'choose') {
			return;
		} else {
			
			//스위치 초기화
			reSwitch();
			deviceModelInfo();
		}
	});
	
}

//switch 초기화
function reSwitch() {
	console.log("reSwitch called");
	
	//스위치 제어해주기 (change멈추기!)
	$(".js-switch").unbind("change");
	$(".js-switch2").unbind("change");
	
	$(".js-switch").prop("checked", false);
	switchery.setPosition(false);
	switchery.handleOnchange(false);
	
	$(".js-switch2").prop("checked", false);
	switchery_2.setPosition(false);
	switchery_2.handleOnchange(false);
}
var id;
/** 디바이스 선택 및 조회 */
function deviceModelInfo() {
	console.log("deviceModelInfo called");
	var tokens = $("#search_deviceModel").val().split("|");
	id = tokens[0];
	
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
	
	//deviceMoProfileList();
	getDeviceId();
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

function getDeviceIdHandler(msg, context) {
	console.log("getDeviceIdHandler called");
	console.log("context: "+ JSON.stringify(context));
	console.log("msg: " + JSON.stringify(msg));
	
	var deviceId = msg.content.info.deviceId;
	console.log("deviceId >>>>>>>>>>>>>> " + deviceId);
	
	//제어에 대한 ALERT창 띄워주기 위한 검색
	deviceMoProfileList(deviceId);
	
	deviceMoDataList(deviceId);
}

function deviceMoDataList(deviceId) {
	console.log("deviceMoDataList called");
	
	var context = {"param":{"deviceId":deviceId}, "handler":deviceMoDataHandler};
	hdb_get_deviceMoData_list(context, false);
}

function deviceMoDataHandler(msg, context) {
	console.log("deviceMoDataHandler called");
	console.log("context:" + JSON.stringify(context));
	console.log("msg :" + JSON.stringify(msg));
	
	try {
		if (msg.result == 0) {
			$(".mobile_box").css('display', 'block');
			$("#infoTable").empty();
			$("#infoTable").append("<tr><td style='font-weight: bold; border-bottom: 2px solid #ccc;'>구분</td><td style='font-weight: bold; border-bottom: 2px solid #ccc;'>데이터</td></tr>");
			for (var i=0; i < msg.content.list.length; i++) {
				var moData = msg.content.list[i];
				if (moData.data != null) {
					$("#infoTable").append("<tr><td>"+moData.resourceName+"</td><td>"+moData.data+"</td></tr>");
				} else {
					$("#infoTable").append("<tr><td>"+moData.resourceName+"</td><td>"+"-"+"</td></tr>");
				}
				
			}
		}
	} catch (e) {
		console.log("exception in deviceMoDataHandler ");
		console.log(e);
	}
}

/** 해당 디바이스의 통계 데이터 리스트 */
function deviceMoProfileList(deviceId) {
	console.log("deviceMoProfileList called");
	var tokens = $("#search_deviceModel").val().split("|");
	var id = tokens[0];
	var oui = tokens[1];
	var modelName = tokens[2];
	console.log("id : " + id + " oui : " + oui + " modelName : " + modelName);
	console.log("lsdkjf;alksdjg;lkajsdlgkjasldkgj deviceId >>>>>>>>>>>>> " + deviceId);
	
	var context = {"param":{"id":id, "oui":oui, "modelName":modelName, "deviceId":deviceId}, "handler":deviceMoProfileListHandler};
	hdb_get_deviceProfile_list(context, false);

}

function deviceMoProfileListHandler(msg, context) {
	console.log("deviceMoProfileListHandler called");
	console.log("context:" + JSON.stringify(context));
	console.log("msg:" + JSON.stringify(msg));
	
	
	var deviceId = context.param.deviceId;
	console.log("88888888888888888888888888888888888888888 deviceId " + deviceId);
	
	try {
		if (msg.result == 0) {
			for (var i=0; i < msg.content.list.length; i++) {
				var profile = msg.content.list[i];
				var resourceUri = profile.resourceUri;
				var resourceName = profile.displayName;
				checkDeviceControl(resourceUri, resourceName, deviceId);
			}
			
		}
	} catch (e) {
		console.log("exception in deviceMoProfileListHandler ");
		console.log(e);
	}
}


/** 자율제어여부 */
function checkDeviceControl(resourceUri, resourceName, deviceId) {
	console.log("checkDeviceControl called");
	
	console.log("resourceUri >>>>>>>>>>>>>>>>>>>" + resourceUri);
	console.log("resourceName >>>>>>>>>>>>>>>>>>>" + resourceName);
	console.log("deviceId >>>>>>>>>>>>>>>>>>>>>>>" + deviceId);
	
	var context = {"param":{"deviceId":deviceId, "resourceUri":resourceUri, "resourceName":resourceName}, "handler":checkDeviceControlHandler};
	hdb_get_deviceMoData_info(context, false);
}


function checkDeviceControlHandler(msg, context) {
	console.log("checkDeviceControlHandler called");
	console.log("context:" + JSON.stringify(context));
	console.log("msg:" + JSON.stringify(msg));

	try {
		if (msg.result == 0) {
			var moProfile = msg.content.info;
			var data = moProfile.data;
			
			if (moProfile.resourceName == "VALVE") {
				//VALVE 제어 & 스위치 생성
				valveSwitchCheck(msg);
			} else if (moProfile.resourceName == "PUMP") {
				//PUMP 제어 & 스위치 생성
				pumpSwitchCheck(msg);
			} else if (data != null && data == "act") {
				console.log("data>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + data);
				//현재 data가 null이면 TypeError가 난다.
				alert("자율제어 중입니다.");
			} /*else if (moProfile.resourceName == "VALVE" && moProfile.data == null) {
				console.log("환영한다~~~");
				
				//액추에이터 제어 화면 초기화
				$(".mobile_control_box").css('display', 'none');
			}*/ else if (moProfile.resourceName == "VALVE_ANGLE") {
				//VALVE 제어 & 그래프 생성
				drawGraph(msg);
			}
			console.log("그냥 갈꾸야!!!!");
			
		}
	} catch (e) {
		console.log("exception in checkDeviceControlHandler ");
		console.log(e);
	}
}

//VALVE 제어 그래프
function drawGraph(msg) {
	console.log("drawGraph called");
	$(".inline").empty();
	$(".mobile_control_box").css('display', 'block');
	
	var moProfile = msg.content.info;
	var angleData = moProfile.data;
	
	//그래프 생성
	//if (angleData == "noData") {
		//angleData = "0";
		//$(".inline").append("<input type='text' value="+angleData+" class='dial m-r-sm' data-fgColor='#1AB394' data-width='100' data-height='100' />");
		//$(".dial").val("-");
		//$(".dial").knob();
	//} else {
		$(".inline").append("<input type='text' value="+angleData+" class='dial m-r-sm' data-fgColor='#1AB394' data-width='100' data-height='100' />");
		$(".dial").knob();
	//}
	
	
	$(".inline").click( function(evt) {
		console.log("inline click called");
		console.log("evt : ", evt);
		
		var data = $(".dial").val();
		var deviceId = moProfile.deviceId;
		var resourceUri = moProfile.resourceUri;
		var resourceName = moProfile.resourceName;
		
		console.log("밸브 그래프 제어 UPDATE 할 때 넘겨줄 값!!!!!!!!" + deviceId + resourceUri + resourceName + data);
		
		executeGraphUpdate(deviceId, resourceUri, resourceName, data);
	});
}

//VALVE BUTTON 초기값에 대한 스위치 상태
function valveSwitchCheck(msg) {
	console.log("valveSwitchCheck called");
	$(".mobile_control_box").css('display', 'block');
	
	$(".js-switch2").unbind("change");
	var moProfile = msg.content.info;
	console.log("///////////////////////////////////////////" + moProfile.data);
	
	if (moProfile.resourceName == "VALVE" && moProfile.data == "on") {
		switchery_2.setPosition(true);
		switchery_2.handleOnchange(true);
	} else {
		switchery_2.setPosition(false);
		switchery_2.handleOnchange(false);
	}
	
	//변경에 대한 스위치 상태
    $(".js-switch2").change( function(evt) {
    	console.log("onSwitchClick called");
    	console.log("evt : ", evt)
    	console.log("msg : ", msg);
    	
    	var deviceId = msg.content.info.deviceId;
    	var resourceUri = msg.content.info.resourceUri;
    	var resourceName = msg.content.info.resourceName;
    	var data = $(".js-switch2").is(':checked') == true ? "on" : "off";
    	console.log("ppppppppppppppppppppppppppppppppppppppppppppppp" + id + deviceId + resourceUri + resourceName + data);
    	
    	executeUpdate(deviceId, resourceUri, resourceName, data);
    });
}

//PUMP BUTTON 초기값에 대한 스위치 상태
function pumpSwitchCheck(msg) {
	console.log("pumpSwitchCheck called");
	$(".mobile_control_box").css('display', 'block');
	
    $(".js-switch").unbind("change");
   
    var moProfile = msg.content.info;
    console.log("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO" + moProfile.data);
    
    if (moProfile.resourceName == "PUMP" && moProfile.data == "on") {
    	switchery.setPosition(true);
		switchery.handleOnchange(true);
    } else {
    	switchery.setPosition(false);
		switchery.handleOnchange(false);
    }
    
    //변경에 대한 스위치 상태
    $(".js-switch").change( function(evt) {
    	console.log("onSwitchClick called");
    	console.log("evt : ", evt)
    	console.log("msg : ", msg);
    	
    	var deviceId = msg.content.info.deviceId;
    	var resourceUri = msg.content.info.resourceUri;
    	var resourceName = msg.content.info.resourceName;
    	var data = $(".js-switch").is(':checked') == true ? "on" : "off";
    	//var val = $(".js-switch")
    	console.log("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu" + deviceId + resourceUri + resourceName + data);
    	
    	executeUpdate(deviceId, resourceUri, resourceName, data);
    });
    
}

/** 밸브랑 펌프 ON/OFF 변경에 대한 스위치 상태 업데이트 */
function executeUpdate(deviceId, resourceUri, resourceName, data) {
	console.log("executeUpdate called");
	console.log("executeUpdate DATA" + deviceId + resourceUri + resourceName + data);
	
	//OPENAPI 사용
	var context = {"deviceId":deviceId, "resourceUris":[resourceUri],
			"e":[{"n":resourceUri, "sv":data}], "handler":executeUpdateHandler};
	dm_execute(context, false);
	
	//var context = {"param":{"deviceId":deviceId, "resourceUri":resourceUri, "resourceName":resourceName, "data":data}, "handler":executeUpdateHandler};
	//hdb_update_deviceMoData(context, false);
}

/** 그래프는 write 그래프 변경에 대한 업데이트 */
function executeGraphUpdate(deviceId, resourceUri, resourceName, data) {
	console.log("executeGraphUpdate called");
	console.log("executeGraphUpdate DATA" + deviceId + resourceUri + resourceName + data);
	
	//OPENAPI 사용
	var context = {"deviceId":deviceId, "resourceUris":[resourceUri],
			"e":[{"n":resourceUri, "sv":data}], "handler":executeUpdateHandler};
	dm_write(context, false);
	
	//var context = {"param":{"deviceId":deviceId, "resourceUri":resourceUri, "resourceName":resourceName, "data":data}, "handler":executeUpdateHandler};
	//hdb_update_deviceMoData(context, false);
}


function executeUpdateHandler(msg, context) {
	console.log("executeUpdateHandler called");
	console.log("context :" + JSON.stringify(context));
	console.log("msg " + JSON.stringify(msg));
	
	
	//위의 테이블을 바꿔줘야할거 같다.
	try {
		if (msg.result == 0) {
			var data = context.param.data;
			console.log("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyy" + data);
			alert("변경 되었습니다.");
			
		}
	} catch (e) {
		console.log("exception in executeUpdateHandler ");
		console.log(e);
	}
}

 
/*google.load('visualization', '1', {packages: ['corechart']});

function drawGraph(msg) {
	console.log("drawGraph called");
	$(".mobile_control_box").css('display', 'block');
	
	var data = google.visualization.arrayToDataTable([
      ['', ''],  
      ['VALVE', 50],
      ['', 50]
    ]);

    var options = {  
      backgroundColor: '#fff',
      pieHole: 0.6,
      pieSliceText: 'value',
      //pieStartAngle: 50,
      tooltip: {trigger: 'focus'},
      slices: {
      	0: {color: '#1AB394'},
        1: {color: '#F8F8F8'}  
      },`
        
      pieSliceTextStyle: {
        fontSize: 11,
      },
        //위치 조정
      chartArea: {
          left: 20,
          top: 30,
          width: '50%',
          height: '75%'
      },
      
      legend: 'none'
    };

    var chart = new google.visualization.PieChart(document.getElementById('donut_single'));
    chart.draw(data, options);
}*/
