
// - 페이지 로딩시 1회 수행
function initUI2() {
	console.log("initUI called");
	
	// 목록 업데이트

	// refresh
	$("#btnSearchfirmware").click(function() {
		
		var deviceModel = $("#search_deviceModelId").val();
		var tokens = deviceModel.split("|");
		
		if (tokens.length < 2) {
			alert("디바이스 모델을 선택해주세요.");
			return;
		}
		
		oui = tokens[0];
		modelName = tokens[1];
		
		sn = $("#search_sn").attr("value");
		
		if (sn == "") {
			movePage("firmware", oui, modelName, sn, 1);
		} else {
			sn = sn + "%";
			movePage("firmware", oui, modelName, sn, 1);
		}
		
	});
	
	btnSearchfirmware();
}

var UPGRADE_STATUS = [];
UPGRADE_STATUS[100] = "업그레이드 명령전송";
UPGRADE_STATUS[1] = "업그레이드 초기화";
UPGRADE_STATUS[2] = "다운로드중";
UPGRADE_STATUS[3] = "다운로드완료";
UPGRADE_STATUS[10] = "설치시작";
UPGRADE_STATUS[101] = "설치종료";
var UPGRADE_RESULT = [];
UPGRADE_RESULT[0] = "";
UPGRADE_RESULT[1] = "실패:Storage오류";
UPGRADE_RESULT[2] = "실패:Memory오류";
UPGRADE_RESULT[3] = "실패:Connection오류";
UPGRADE_RESULT[4] = "실패:CRC오류";
UPGRADE_RESULT[5] = "실패:PackageType오류";
UPGRADE_RESULT[6] = "실패:URI오류";
UPGRADE_RESULT[10] = "업그레이드성공";
UPGRADE_RESULT[11] = "실패:앱오류 ";
UPGRADE_RESULT[101] = "실패:명령연결실패";
UPGRADE_RESULT[102] = "실패:명령타임아웃";

function firmwareStatusManualRenderer(id, profile, context) {
	console.log("firmwareStatusManualRenderer called - id:"+id +":"+ profile['errorGrade']);
	var td = "<td></td>";
	switch (id) {
	case "upStatus":
		var strStatus = "";
		var upStatus = profile["upStatus"];
		var upResult = profile["upResult"];
		if (upResult == 0 || upResult == -1) {
			strStatus = UPGRADE_STATUS[upStatus]+" <span style='color:white;'>"+upStatus+","+upResult+"</span>";
		} else {
			strStatus = UPGRADE_RESULT[upResult]+" <span style='color:white;'>"+upStatus+","+upResult+"</span>";
		}
		td = "<td style='text-align:left;'>"+strStatus+"</td>";
		break;
	}
	return td;
}

function movePage(type, oui, modelName, sn, page){
	var context = {"param":{ "oui":oui, "modelName":modelName, "sn":sn, "page":page}, "manualRenderer":firmwareStatusManualRenderer};
	
	refreshFirmwareTableContext("firmware", context);
}