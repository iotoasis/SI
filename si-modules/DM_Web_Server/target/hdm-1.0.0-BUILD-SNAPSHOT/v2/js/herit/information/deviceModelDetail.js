// Custom scripts
$(document).ready(function () {
	deviceModel.initialize();
});

var deviceModel = (function(){
	

	var moProfileListModelMap;
	
	var initialize = function(){
		console.log("initialized~!!!");
		
		initUIComponents();	

		// UI Update
		if (deviceModelId != "") {
			refreshDeviceModelInfo();
		} else {
			
		}
			
	};

	
	
	var initUIComponents = function(){
		addEventHandler();
		//createDialog();
	};

	var addEventHandler = function(){

		//관리객체 추가
		$("#addResource").click(function() {

			var version = $("#profileVerSelector").val();
			if(version == ""){
				var errorMessage = "버전을 선택해 주세요.";
				createDialogMessage(errorMessage);
				errorMessage = false;
				return false;
			}
			
			
			$("#actionType").val("I");
			$("#addResourceModal").modal("show");
		});
		
		//디바이스 모델 수정
		$("#modifyDeviceModel").click(function() {
			
			$("#actionType").val("U");
			$("#manufacturer").val($("#model_manufacturer").text());
			$("#oui").val($("#model_oui").text());
			$("#modelName").val($("#model_modelName").text());
			$("#description").val($("#model_description").text());
			$("#deviceType").val($("#model_deviceType").val());
			$("#applyYn").val($("#model_applyYn").val());
			
			$("#modifyDeviceModelModal").modal("show");
		});
		
		//버전 추가
		$("#addVersion").click(function() {
			$("#actionType").val("I");
			$("#addVersionModal").modal("show");
		});
		
		//관리객체 수정
		$("#moProfileList").on("click", ".moProfileUpdateBtn",  function() {
			var id = $(this).context.id;
			var idx = $(this).context.attributes.idx.value;
			
			$("#actionType").val("U");
			$("#id").val(id);
			$("#displayName").val(moProfileListModelMap[idx].displayName);
			$("#resourceUri").val(moProfileListModelMap[idx].resourceUri);
			$("#dataType").val(moProfileListModelMap[idx].dataType);
			$("#unit").val(moProfileListModelMap[idx].unit);
			$("#vdefaultValue").val(moProfileListModelMap[idx].vdefaultValue);
			$("#operation").val(moProfileListModelMap[idx].operation);
			$("#notiType").val(moProfileListModelMap[idx].notiType);
			$("#isError").val(moProfileListModelMap[idx].isError);
			$("#isDiagnostic").val(moProfileListModelMap[idx].isDiagnostic);
			$("#isHistorical").val(moProfileListModelMap[idx].isHistorical);
			$("#isMandatory").val(moProfileListModelMap[idx].isMandatory);
			
			$("#addResourceModal").modal("show");
		});
		
		//관리객체 삭제
		$("#moProfileList").on("click", ".moProfileDeleteBtn",  function() {
			var id = $(this).context.id;

			if (!confirm("삭제 하시겠습니까?"))
				return;

			var data = {
				id: id
			};	 					
			var requestParam = {	
				data: data,
				successCallback: addResourceSuccessCallback
			};
			var context = { 
				"param" : requestParam.data, 
				"handler": requestParam.successCallback 
			};
			hdp_del_mo_profile(context, false);	
		});
		
		//옵션데이터 목록보기 클릭
		$("#moProfileList").on("click", ".optionDataBtn",  function() {
			var id = $(this).context.id;
			$("#moProfileId").val(id);						
			moOptionDataList();			
		});
		//옵션데이터 추가
		$("#addOptionData").click(function() {
			$("#actionType").val("I");
			$("#addOptionDataModal").modal("show");
		});
		//옵션데이터 삭제
		$("#delOptionData").click(function() {
			var id = getRadioValue($("#moOptionDataList input:radio"));
			
			if (id != "") {
				if (!confirm("삭제 하시겠습니까?"))
					return;
				
				var data = {
					id: id
				};	 					
				var requestParam = {	
					data: data,
					successCallback: moOptionDataList
				};
				var context = { 
					"param" : requestParam.data, 
					"handler": requestParam.successCallback 
				};
				hdp_del_mo_option_data(context, false);	
			}else{
				errorMessage = "선택된 항목이 없습니다.";
				createDialogMessage(errorMessage);
				errorMessage = false;
			}
		});
		
			
		
		//에러코드 목록보기 클릭
		$("#moProfileList").on("click", ".errorDataBtn",  function() {
			var id = $(this).context.id;
			$("#moProfileId").val(id);			
			moErrorCodeList();			
		});
		//에러코드 추가
		$("#addErrorCode").click(function() {
			console.log("addErrorCode!");
			$("#actionType").val("I");
			$("#addErrorCodeModal").modal("show");
		});
		//에러코드 삭제
		$("#delErrorCode").click(function() {
			var id = getRadioValue($("#moErrorCodeList input:radio"));
			if(id != ""){
				
				if (!confirm("삭제 하시겠습니까?"))
					return;
				

				var data = {
					id: id
				};	 					
				var requestParam = {	
					data: data,
					successCallback: moErrorCodeList
				};
				var context = { 
					"param" : requestParam.data, 
					"handler": requestParam.successCallback 
				};
				hdp_del_mo_error_code(context, false);	
			}else{
				errorMessage = "선택된 항목이 없습니다.";
				createDialogMessage(errorMessage);
				errorMessage = false;
			}
			
		});
		
		
		//노티조건 목록보기 클릭
		$("#moProfileList").on("click", ".notiOptionBtn",  function() {
			var id = $(this).context.id;
			$("#moProfileId").val(id);
			moNotiConditionList();
		});
		//노티조건 추가
		$("#addNotiCondition").click(function() {
			$("#actionType").val("I");
			$("#addNotiConditionModal").modal("show");
		});
		//노티조건 삭제
		$("#delNotiCondition").click(function() {
			var id = getRadioValue($("#moNotiConditionList input:radio"));
			if(id != ""){
				if (!confirm("삭제 하시겠습니까?"))
					return;

							
				var data = {
					id: id
				};	 					
				var requestParam = {	
					data: data,
					successCallback: moNotiConditionList
				};
				var context = { 
					"param" : requestParam.data, 
					"handler": requestParam.successCallback 
				};
				hdp_del_mo_noti_condition(context, false);	
		 					
			}else{
				errorMessage = "선택된 항목이 없습니다.";
				createDialogMessage(errorMessage);
				errorMessage = false;
			}
		});

		//버전 선택
		$("#profileVerSelector").change(function(){
			moProfileList();		
		});
	};
	

	var moOptionDataList = function(){
		
		$("#addOptionDataModal").modal("hide");
		moProfileList();
		
		var data = {
				moProfileId: $("#moProfileId").val()
		};	 					
		var requestParam = {	
				data: data,
				successCallback: moOptionDataListSuccessCallback
		};
		var context = { 
				"param" : requestParam.data, 
				"handler": requestParam.successCallback 
		};
		hdp_get_mo_option_data_list(context, false);
	};
	var moOptionDataListSuccessCallback = function(msg, context){
//		console.log("moOptionDataListSuccessCallback called ");
//		console.log("context: "+JSON.stringify(context));
//		console.log("msg: "+JSON.stringify(msg));

		var moOptionDataList = msg.content.list;
		//console.log("moOptionDataList : ", moOptionDataList);

		
		var moOptionDataListHtml = "";
		if(moOptionDataList.length != 0){

			$.each(moOptionDataList, function(idx, moOptionData){								
				moOptionDataListHtml += "<tr>";
				moOptionDataListHtml += "	<td><input type='radio' name='moOptionDataId' value='" + moOptionData.id + "'></td>";
				moOptionDataListHtml += "	<td>" + moOptionData.order + "</td>";
				moOptionDataListHtml += "	<td>" + moOptionData.displayData + "</td>";
				moOptionDataListHtml += "	<td>" + moOptionData.data + "</td>";
				moOptionDataListHtml += "</tr>";
			});
			
		}else{
			moOptionDataListHtml += "<tr>";
			moOptionDataListHtml += "	<td colspan='4'>옵션 데이터 정보 없음</td>";
			moOptionDataListHtml += "</tr>";
		}
		
		$("#moOptionDataList").html(moOptionDataListHtml);		
		$("#optionDataListModal").modal("show");
	};
	
	var moErrorCodeList = function(){
		$("#addErrorCodeModal").modal("hide");
		moProfileList();
		
		var data = {
				moProfileId: $("#moProfileId").val()
		};	 					
		var requestParam = {	
				data: data,
				successCallback: moErrorCodeListSuccessCallback
		};
		var context = { 
				"param" : requestParam.data, 
				"handler": requestParam.successCallback 
		};
		hdp_get_mo_error_code_list(context, false);
	};
	var moErrorCodeListSuccessCallback = function(msg, context){
//		console.log("moErrorCodeListSuccessCallback called ");
//		console.log("context: "+JSON.stringify(context));
//		console.log("msg: "+JSON.stringify(msg));

		var moErrorCodeList = msg.content.list;
		//console.log("moErrorCodeList : ", moErrorCodeList);

		
		var moErrorCodeListHtml = "";
		if(moErrorCodeList.length != 0){

			$.each(moErrorCodeList, function(idx, moErrorCode){
				
				var errorGrade = "";
				switch (moErrorCode.errorGrade) {
				case 0:
					errorGrade = "NO ERROR";
					break;
				case 1:
					errorGrade = "MINOR";
					break;
				case 2:
					errorGrade = "MAJOR";
					break;
				case 3:
					errorGrade = "CRITICAL";
					break;
				case 4:
					errorGrade = "FATAL";
					break;
				}
				
				moErrorCodeListHtml += "<tr>";
				moErrorCodeListHtml += "	<td><input type='radio' name='moErrorCodeId' value='" + moErrorCode.id + "'></td>";
				moErrorCodeListHtml += "	<td>" + moErrorCode.errorCode + "</td>";
				moErrorCodeListHtml += "	<td>" + errorGrade + "</td>";
				moErrorCodeListHtml += "	<td>" + moErrorCode.errorName + "</td>";
				moErrorCodeListHtml += "	<td>" + moErrorCode.description + "</td>";
				moErrorCodeListHtml += "</tr>";				
					
			});
			
		}else{
			moErrorCodeListHtml += "<tr>";
			moErrorCodeListHtml += "	<td colspan='5'>에러코드 정보 없음</td>";
			moErrorCodeListHtml += "</tr>";
		}
		
		$("#moErrorCodeList").html(moErrorCodeListHtml);		
		$("#errorCodeListModal").modal("show");
	};

	var moNotiConditionList = function(){	
		$("#addNotiConditionModal").modal("hide");
		moProfileList();
		
		var data = {
				moProfileId: $("#moProfileId").val()
		};	 					
		var requestParam = {	
				data: data,
				successCallback: moNotiConditionListSuccessCallback
		};
		var context = { 
				"param" : requestParam.data, 
				"handler": requestParam.successCallback 
		};
		hdp_get_mo_noti_condition_list(context, false);		
	};
	var moNotiConditionListSuccessCallback = function(msg, context){
//		console.log("moNotiConditionListSuccessCallback called ");
//		console.log("context: "+JSON.stringify(context));
//		console.log("msg: "+JSON.stringify(msg));

		var moNotiConditionList = msg.content.list;
		//console.log("moNotiConditionList : ", moNotiConditionList);

		
		var moNotiConditionListHtml = "";
		if(moNotiConditionList.length != 0){

			$.each(moNotiConditionList, function(idx, moNotiCondition){
				
				var conditionType = "";
				switch (moNotiCondition.conditionType) {
				case 'P':
					conditionType = "Period (sec)";
					break;
				case 'G':
					conditionType = "Greater Than";
					break;
				case 'L':
					conditionType = "Less Than";
					break;
				case 'S':
					conditionType = "STEP";
					break;
				}
				
				moNotiConditionListHtml += "<tr>";
				moNotiConditionListHtml += "	<td><input type='radio' name='moNotiConditionId' value='" + moNotiCondition.id + "'></td>";
				moNotiConditionListHtml += "	<td>" + conditionType + "</td>";
				moNotiConditionListHtml += "	<td>" + moNotiCondition.condition + "</td>";
				moNotiConditionListHtml += "</tr>";				
					
			});
			
		}else{
			moNotiConditionListHtml += "<tr>";
			moNotiConditionListHtml += "	<td colspan='3'>노티조건 정보 없음</td>";
			moNotiConditionListHtml += "</tr>";
		}
		
		$("#moNotiConditionList").html(moNotiConditionListHtml);		
		$("#notiConditionListModal").modal("show");
	};
	
	
	var fnModifyDeviceModel = function() {
		var data = {
			actionType: $("#actionType").val(),
			deviceModelId: deviceModelId,
			oui: $("#oui").val(),
			manufacturer: $("#manufacturer").val(),
			modelName: $("#modelName").val(),
			deviceType: $("#deviceType").val(),
			resourceUri: $("#resourceUri").val(),
			iconUrl: $("#iconUrl").val(),
			profileVer: $("#profileVer").val(),
			displayName: $("#displayName").val(),
			description: $("#description").val(),
			applyYn: $("#applyYn").val()
		};		
			
		var requestParam = {	
			data: data,
			successCallback: refreshDeviceModelInfo
		};
		
		var context = { 
			"param" : requestParam.data, 
			"handler": requestParam.successCallback
		};

		$("#modifyDeviceModelModal").modal("hide");
		hdp_update_device_model(context, false);
	};
	
	var fnAddResource = function() {
		var data = {
			id: $("#id").val(),
			actionType: $("#actionType").val(),
			deviceModelId: deviceModelId,
			profileVer: $("#profileVerSelector").val(),
			displayName: $("#displayName").val(),
			resourceUri: $("#resourceUri").val(),
			dataType: $("#dataType").val(),
			unit: $("#unit").val(),
			defaultValue: $("#defaultValue").val(),
			operation: $("#operation").val(),
			notiType: $("#notiType").val(),
			isError: $("#isError").val(),
			isDiagnostic: $("#isDiagnostic").val(),
			isHistorical: $("#isHistorical").val(),
			isMandatory: $("#isMandatory").val(),
			isMultiple: null
		};		
			
		var requestParam = {	
			data: data,
			successCallback: addResourceSuccessCallback
		};
		
		var context = { 
			"param" : requestParam.data, 
			"handler": requestParam.successCallback
		};
		
		
		if ($("#actionType").val() == "I") {
			hdp_add_mo_profile(context, false);	
		} else if($("#actionType").val() == "U") {
			hdp_update_mo_profile(context, false);
		}
		
	};
	
	
	var fnAddVersion = function() {
		var data = {
			deviceModelId: deviceModelId,
			newVersionSelector: $("#newVersionSelector").val(),
			profileVer: $("#preVersionSelector").val()
		};
		
		
		var requestParam = {	
			data: data,
			successCallback: refreshDeviceModelInfo
		};
		
		var context = { 
			"param" : requestParam.data, 
			"handler": requestParam.successCallback
		};

		$("#addVersionModal").modal("hide");
		hdp_add_mo_profile_copy_version(context, false);	
	};

	var fnAddOptionData = function() {
		
		var data = {
			moProfileId: $("#moProfileId").val(),
			order: $("#order").val(),
			displayData: $("#displayData").val(),
			data: $("#data").val()
		};		
		
		var requestParam = {	
			data: data,
			successCallback: moOptionDataList
		};
		
		var context = { 
			"param" : requestParam.data, 
			"handler": requestParam.successCallback
		};

		hdp_add_mo_option_data(context, false);
	};

	var fnAddErrorCode = function() {
		var data = {
			moProfileId: $("#moProfileId").val(),
			errorCode: $("#errorCode").val(),
			errorGrade: $("#errorGrade").val(),
			errorName: $("#errorName").val(),
			description: $("#errorDescription").val()
		};		
		
		var requestParam = {	
			data: data,
			successCallback: moErrorCodeList
		};
		
		var context = { 
			"param" : requestParam.data, 
			"handler": requestParam.successCallback
		};

		hdp_add_mo_error_code(context, false);
	};
	
	var fnAddNotiCondition = function() {
		var data = {
			moProfileId: $("#moProfileId").val(),
			conditionType: $("#conditionType").val(),
			condition: $("#condition").val()
		};		
		
		var requestParam = {	
			data: data,
			successCallback: moNotiConditionList
		};
		
		var context = { 
			"param" : requestParam.data, 
			"handler": requestParam.successCallback
		};

		hdp_add_mo_noti_condition(context, false);
	};
	

	//입력값 초기화
	var fnInputValInit = function(){	
		$("#id").val("");
		$("#actionType").val("");
		
		$("#displayName").val("");
		$("#resourceUri").val("");
		$("#dataType").val("");
		$("#unit").val("");
		$("#vdefaultValue").val("");
		$("#operation").val("");
		$("#notiType").val("");
		$("#isError").val("");
		$("#isDiagnostic").val("");
		$("#isHistorical").val("");
		$("#isMandatory").val("");	
	};
	
	
	var refreshDeviceModelInfo = function() {
//		console.log("refreshDeviceModelInfo called - deviceModelId:"+deviceModelId);	
		//$("#addVersionDialog").dialog("close");	
		//$("#modifyDeviceModelDialog").dialog("close");
		var context = { "param": { "id":deviceModelId }, "handler":getDeviceModelInfoHandler }
		hdb_get_info_deviceModel(context, false);
	};
	
	var getDeviceModelInfoHandler = function(msg, context) {
		console.log("getDeviceModelInfoHandler called ");
		console.log("context: "+JSON.stringify(context));
		console.log("msg: "+JSON.stringify(msg));
		
		if (msg.content.info == null) {
			alert("디바이스 모델 정보가 존재하지 않습니다. 모델정보를 다시 선택해주세요.");
			window.location.href = contextPath + "/information/deviceModel.do";
		}

		if(msg.content.info["iconUrl"] != null){
			$("#iconUrl").attr("src", msg.content.info["iconUrl"]);	
		}else{
			$("#iconUrl").attr("src", contextRoot + "/images/hitdm/temp/settopbox.jpg");
		}

		$("#model_id").val(msg.content.info["id"]);
		$("#model_deviceType").val(msg.content.info["deviceType"]);
		$("#model_applyYn").val(msg.content.info["applyYn"]);
		
		$("#model_manufacturer").text(msg.content.info["manufacturer"]);
		$("#model_oui").text(msg.content.info["oui"]);
		$("#model_modelName").text(msg.content.info["modelName"]);
		$("#model_description").text(msg.content.info["description"]);
		$("#model_createTime").text(msg.content.info["createTime"]);
		$("#model_updateTime").text(msg.content.info["updateTime"]);

		oui = msg.content.info["oui"];
		modelName = msg.content.info["modelName"];
		

		var profileVerCodeList = msg.content.profileVerCodeList;

		var profileVerSelectorHtml = "";
		profileVerSelectorHtml += "<option value=''>전체</option>";		
		$.each(profileVerCodeList, function(idx, profileVerCodeData){			
			if((profileVerCodeList.length - 1) == idx){
				profileVerSelectorHtml += "<option value='" + profileVerCodeData.code + "' selected>" + profileVerCodeData.name + "</option>";
				$("#profileVer").val(profileVerCodeData.code);
			}else{
				profileVerSelectorHtml += "<option value='" + profileVerCodeData.code + "'>" + profileVerCodeData.name + "</option>";
			}
		});		
		$("#profileVerSelector").html(profileVerSelectorHtml);
		$("#preVersionSelector").html(profileVerSelectorHtml);
		
		
				
		moProfileList();
	};
	

	var moProfileList = function() {
		console.log("moProfileList called - deviceModelId:"+deviceModelId);

			
		var data = {
				deviceModelId: deviceModelId,
				profileVer: $("#profileVerSelector").val()
		};		
		
		var requestParam = {	
				data: data,
				successCallback: getMoProfileListSuccessCallback
		};
		
		var context = { 
				"param" : requestParam.data, 
				"handler": requestParam.successCallback
		};

		//hdb_get_device_profile(context, false);
		hdp_get_mo_profile_list(context, false);
				
	};
	
	
	var getMoProfileListSuccessCallback = function(msg, context) {
//		console.log("getMoProfileListSuccessCallback called ");
//		console.log("context: "+JSON.stringify(context));
//		console.log("msg: "+JSON.stringify(msg));
		

		moProfileListModelMap = new Array();
		
		
		var moProfileList = msg.content.moProfileList;
		console.log("moProfileList : ", moProfileList);

		
		var moProfileListHtml = "";
		if(moProfileList.length != 0){

			$.each(moProfileList, function(idx, moProfileData){
								
				var notiType = "";
				switch (moProfileData.notiType) {
				case 0:
					notiType = "NONE";
					break;
				case 1:
					notiType = "ON CHANGE";
					break;
				case 2:
					notiType = "<button id='" + moProfileData.id + "' class='notiOptionBtn btn btn-xs btn-info'>목록(" + moProfileData.moNotiConditionCnt + "건)</button>";
					break;
				case 3:
					notiType = "NO UPDATE";
					break;
				}
				
				moProfileListHtml += "<tr>";
				moProfileListHtml += "	<td>" + moProfileData.profileVer + "</td>";
				moProfileListHtml += "	<td>" + moProfileData.displayName + "</td>";
				moProfileListHtml += "	<td>" + moProfileData.resourceUri + "</td>";
				moProfileListHtml += "	<td>" + moProfileData.dataType + "</td>";
				moProfileListHtml += "	<td>" + moProfileData.unit + "</td>";
				moProfileListHtml += "	<td>" + moProfileData.defaultValue + "</td>";
				moProfileListHtml += "	<td>" + moProfileData.operation + "</td>";
				moProfileListHtml += "	<td>" + moProfileData.isError + " / " + moProfileData.isDiagnostic + "</td>";
				moProfileListHtml += "	<td>" + moProfileData.isHistorical + "</td>";
				moProfileListHtml += "	<td><button id='" + moProfileData.id + "' class='optionDataBtn btn btn-xs btn-info'>목록(" + moProfileData.moOptionDataCnt + "건)</button></td>";
				moProfileListHtml += "	<td><button id='" + moProfileData.id + "' class='errorDataBtn btn btn-xs btn-info'>목록(" + moProfileData.moErrorCodeCnt + "건)</button></td>";
				moProfileListHtml += "	<td>" + notiType + "</td>";
				moProfileListHtml += "	<td>" +
											"<button id='" + moProfileData.id + "' idx='"+idx+"' class='moProfileUpdateBtn btn btn-xs btn-success'>수정</button>" +
											"<button id='" + moProfileData.id + "' idx='"+idx+"' class='moProfileDeleteBtn btn btn-xs btn-danger' style='margin-left:2px;'>삭제</button>" +
										"</td>";
				moProfileListHtml += "</tr>";
				
				moProfileListModelMap.push({
					"id":moProfileData.id, 
					"deviceModelId":moProfileData.deviceModelId, 
					"profileVer":moProfileData.profileVer, 
					"displayName":moProfileData.displayName, 
					"resourceUri":moProfileData.resourceUri, 
					"dataType":moProfileData.dataType, 
					"unit":moProfileData.unit, 
					"defaultValue":moProfileData.defaultValue, 
					"operation":moProfileData.operation, 
					"notiType":moProfileData.notiType, 
					"isError":moProfileData.isError, 
					"isDiagnostic":moProfileData.isDiagnostic, 
					"isHistorical":moProfileData.isHistorical, 
					"isMandatory":moProfileData.isMandatory
				});

					
			});
			
		}else{
			moProfileListHtml += "<tr>";
			moProfileListHtml += "	<td colspan='12'>관리 객체 정보 없음</td>";
			moProfileListHtml += "</tr>";
		}

		

		
		$("#moProfileList").html(moProfileListHtml);
		
	};
	
	
	
	

	var addResourceSuccessCallback = function(msg, context) {
		$("#addResourceModal").modal("hide");
		$("#addVersionModal").modal("hide");
		moProfileList();
	};
	
	
/*	
	var getMoListHandler = function(msg, context) {
		console.log("getMoListHandler called ");
		console.log("context: "+JSON.stringify(context));
		console.log("msg: "+JSON.stringify(msg));

		var ths = $("#mo_table thead th");
		var moBody = $("#mo_table tbody");
		moBody.empty();
		
		// 목록 표시
		for (var i=0; i<msg.content.profile.length; i++) {
			var profile = msg.content.profile[i];
			
			var tr = $("<tr></tr>");
			for (var j=0; j<ths.length; j++) {
				if (typeof $(ths[j]).attr("id") == "undefined") {
					continue;
				} 
				var fn = $(ths[j]).attr("id").substring(3); 	// hd_<fieldName>
				if ($(ths[j]).hasClass("manual_renderer")) {
					tr.append(resItemRenderer(fn, profile, context));
				} else {
					var text = profile[fn] == null ? "-" : profile[fn];
					tr.append($("<td class='profile_resource'>"+text+"</td>"));				
				}
			}
			moBody.append(tr);
		}
		if (msg.content.profile.length == 0) {
			var tr = $("<tr></tr>");
			tr.append($("<td colspan='"+ths.length+"'>관리 객체 정보 없음</td>"));
			moBody.append(tr);
		}	
			
	};
	
	
	
	
	var resItemRenderer = function(id, profile, context) {
		//console.log("resItemRenderer called - id:"+id +":"+ profile['resourceUri']);
		//console.log("노티옵션 : " + profile['notiType']);

		switch (id) {
			case 'notiType' :
				var td = "<td class='profile_resource'></td>";
				if (profile['notiType'] == 0) {
					var text = "NONE";
					td = "<td class='profile_resource'>"+text+"</td>";
				} else if (profile['notiType'] == 1) {
					var text = "ON CHANGE";
					td = "<td class='profile_resource'>"+text+"</td>";
				} else if (profile['notiType'] == 2) {
					td = "<td class='profile_resource'><button class='noti_option_update' moProfileId='"+profile['id']+"'>수정</button></td>";
				} else if (profile['notiType'] == 3) {
					var text = "NO UPDATE";
					td = "<td class='profile_resource'>"+text+"</td>";
				}
				break;
				
			case 'optionData' :
				var td = "<td class='profile_resource'>-</td>";
				if (profile['hasOptionData'] == 'Y') {
					var text = "";
					for (var optionId in profile.optionDataList) {
						var option = profile.optionDataList[optionId];
						text += option.data +": "+option.displayData+"<br>";						
					}
					td = "<td class='profile_resource'  style='text-align:left'>"+text+"</td>";
				} else if (profile['isError'] == 'Y') {
					var text = "";
					for (var errorId in profile.errorCodeList) {
						var error = profile.errorCodeList[errorId];
						text += error.errorCode +": "+error.errorName+" ("+ERROR_GRADE[error.errorGrade]+")<br>";						
					}
					td = "<td class='profile_resource' style='text-align:left'>"+text+"</td>";
				}
				break;
		}
		return td;

	};
*/	


	var createDialogMessage = function(dialogMessage){
		alert(dialogMessage);
	};
	
	return {
		initialize: initialize,
		fnModifyDeviceModel: fnModifyDeviceModel,
		fnAddResource: fnAddResource,
		fnAddVersion: fnAddVersion,
		fnAddOptionData: fnAddOptionData,
		fnAddErrorCode: fnAddErrorCode,
		fnAddNotiCondition: fnAddNotiCondition
	};
})();