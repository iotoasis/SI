// - 페이지 로딩시 1회 수행
function initUI() {
	console.log("initUI called");
	
	//스위치
	changeSwitch();
	
	$("#search_deviceModel").change(function() {
		var deviceModel = $("#search_deviceModel").val();
		if (deviceModel == 'choose') {
			return;
		} else {
			//Switch 초기화
			reSwitch();
			
			//Slider 초기화
			reSlider();
			
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
	
	//제어에 대한 정보를 가져오기 위함
	//deviceMoProfileList(deviceId);
	
	deviceMoDataList(deviceId);
}


var elem;
var elem_2;
var elem_3;
var elem_4;
var switchery;
var switchery_2;
var switchery_3;
var switchery_4;

function changeSwitch() {
	console.log("changeSwitch called");
	//Switch 생성
	elem = document.querySelector('.js-switch');
	switchery = new Switchery(elem, { color: '#1AB394'});
	elem_2 = document.querySelector('.js-switch2');
	switchery_2 = new Switchery(elem_2, { color: '#1AB394'});
	elem_3 = document.querySelector('.js-switch3');
	switchery_3 = new Switchery(elem_3, { color: '#1AB394'});
	elem_4 = document.querySelector('.js-switch4');
	switchery_4 = new Switchery(elem_4, { color: '#1AB394'});
	
}

function reSwitch() {
	console.log("reSwitch called");
	
	$(".js-switch").unbind("change");
	$(".js-switch2").unbind("change");
	$(".js-switch3").unbind("change");
	$(".js-switch4").unbind("change");
	
	//자율적 상황판단 제어
	$(".js-switch").prop("checked", false);
	switchery.setPosition(false);
	switchery.handleOnchange(false);
	
	//이벤트 push 알림
	$(".js-switch2").prop("checked", false);
	switchery_2.setPosition(false);
	switchery_2.handleOnchange(false);
	
	//센서 데이터 업데이트
	$(".js-switch3").prop("checked", false);
	switchery_3.setPosition(false);
	switchery_3.handleOnchange(false);
	
	//업데이트 주기 설정
	$(".js-switch4").prop("checked", false);
	switchery_4.setPosition(false);
	switchery_4.handleOnchange(false);
}

function reSlider() {
	console.log("reSlider called");
	
	//$("#slider").slider("disable");
	$("#slider").css('display', 'none');
	$("#sliderInfo").css('display', 'none');
}

function deviceMoDataList(deviceId) {
	console.log("deviceMoDataList called");
	
	var context = {"param":{"deviceId":deviceId}, "handler":deviceMoDataHandler};
	hdb_get_deviceMoData_list(context, false);
}


var periodData;
var perResourceUri;
var perResourceName;
var preDeviceId;
var moData;
var deviceId;
var resourceUri;
var resourceName;
var controlData;

function deviceMoDataHandler(msg, context) {
	console.log("deviceMoDataHandler called");
	console.log("context:" + JSON.stringify(context));
	console.log("msg :" + JSON.stringify(msg));
	
	try {
		if (msg.result == 0) {
			for (var i=0; i < msg.content.list.length; i++) {
				moData = msg.content.list[i];
				
				deviceId = moData.deviceId;
				resourceUri = moData.resourceUri;
				resourceName = moData.resourceName;
				//console.log("--------------------------------- " + deviceId + resourceUri + resourceName);
				
				//Slider 생성을 위한 데이터 값 추출
				if (moData.resourceName == "UPDATE_PERIOD") {
					periodData = moData.data;
					perResourceUri = moData.resourceUri;
					perResourceName = moData.resourceName;
					preDeviceId = moData.deviceId;
					console.log("UPDATE_PERIOD에 의한 파라미터 값들!!!!!" + perResourceUri + perResourceName + preDeviceId + periodData);
					//alert("-----------------------------" + periodData);
				}
				
				 //&& "on" != switchery.isChecked()
				//자율적 상황판단 제어에 대한 초기 화면 설정
				if (moData.data == "act" || moData.data == "act_off") {
					//스위치 변경에 의한 업데이트
					controlData = moData.data;
					onControlSwitchClick(deviceId, resourceUri, resourceName, controlData);
				} else if (moData.data == "alert" || moData.data == "alert_off") {
					//스위치 변경에 의한 업데이트
					controlData = moData.data;
					onPushSwitchClick(deviceId, resourceUri, resourceName, controlData);
				} else if (moData.data == "update" || moData.data == "update_off") {
					//스위치 변경에 의한 업데이트
					controlData = moData.data;
					onUpdateSwitchClick(deviceId, resourceUri, resourceName, controlData);
				} else if (moData.data == "period" || moData.data == "period_off") {
					//스위치 변경에 의한 업데이트
					controlData = moData.data;
					onPeriodSwitchClick(deviceId, resourceUri, resourceName, controlData);
					//drawSlider(periodData, perResourceUri, perResourceName, preDeviceId, periodData);
				}
			}
			
		}
	} catch (e) {
		console.log("exception in deviceMoDataHandler ");
		console.log(e);
	}
}

function drawSlider(periodData, perResourceUri, perResourceName, preDeviceId) {
	console.log("drawSlider called");
	console.log("여기로 와용~~~~~~~~~~~~~~" + perResourceUri + perResourceName + preDeviceId);
	
	var secData = periodData.substring(0, 2);
	
	$("#slider").css('display', 'block');
	$("#sliderInfo").css('display', 'block');
	
	$("#slider").slider({
      value: secData,
      min: 1,
      max: 60,
      step: 1,
      slide: function(event, ui) {
        $("#amount").val(ui.value + "sec");
      },
      stop: function(event, ui) {
  		$("#changevalue").val(ui.value + "sec");
  		var data = $("#changevalue").val();
  		
  		console.log("//////////////////////////////////////////////////// " + data);
  		executeUpdateSlider(preDeviceId, perResourceUri, perResourceName, data);
  	  }
    });
    $("#amount").val($("#slider").slider("value") + "sec");
    //executeUpdateSlider(preDeviceId, perResourceUri, perResourceName, periodData);
}

/** 슬라이더 값 변경에 의한 업데이트 */
function executeUpdateSlider(preDeviceId, perResourceUri, perResourceName, data) {
	console.log("executeUpdateSlider called");
	console.log("executeUpdateSlider DATA " + preDeviceId + perResourceUri + perResourceName + data);
	
	var context = {"deviceId":preDeviceId, "resourceUris":[perResourceUri],
			"e":[{"n":perResourceUri, "sv":data}], "handler":executeUpdateSliderHandler};
	dm_write(context, false);
	//var context = {"param":{"deviceId":preDeviceId, "resourceUri":perResourceUri, "resourceName":perResourceName, "data":data}, "handler":executeUpdateSliderHandler};
	//hdb_update_deviceMoData(context, false);
}

/** 업데이트 주기 설정을 off로 바꿨을 때 주기 default값으로 업데이트 (10초) */
function executeDefaultSliderData(preDeviceId, perResourceUri, perResourceName, defaultData) {
	//alert("안녕? default값 업데이트 해주러 왔어!!!!!");
	console.log("executeDefaultSliderData called");
	console.log("executeDefaultSliderData DATA " + preDeviceId + perResourceUri + perResourceName + defaultData);
	
	
	var context = {"deviceId":preDeviceId, "resourceUris":[perResourceUri],
			"e":[{"n":perResourceUri, "sv":defaultData}], "handler":executeUpdateSliderHandler};
	dm_write(context, false);
	//var context = {"param":{"deviceId":preDeviceId, "resourceUri":perResourceUri, "resourceName":perResourceName, "data":defaultData}};
	//hdb_update_deviceMoData(context, false);
}

function executeUpdateSliderHandler(msg, context) {
	console.log("executeUpdateSliderHandler called");
	console.log("context :" + JSON.stringify(context));
	console.log("msg " + JSON.stringify(msg));
	
	if (msg.result == 0) {
		alert("업데이트 주기가 변경되었습니다.");
	
		return;
	}
}
