// Custom scripts
$(document).ready(function () {
	deviceModel.initialize();
});

var deviceModel = (function(){

	//디바이스 모델 상세 페이지 이동 url
	var detailDeviceModelUrl = contextPath + "/information/deviceModel/detail.do";
	
	var initialize = function(){
		console.log("initialized~!!!");
		console.log("deviceModelAttributeList : ", deviceModelAttributeList);
		console.log("contextPath : ", contextPath);
		console.log("serverIp : ", serverIp);
		
		getDeviceModelList();
		initUIComponents();		
	};

	

	var getDeviceModelList = function(){			

		var context = { 
				"handler": getDeviceModelListSuccessCallback 
			};
		
		hdp_get_device_model_list(context, false);
	};
	


	var test = function(id){	
		console.log("id : ", id);
	};
	
	var getDeviceModelListSuccessCallback = function(msg, context){
		console.log("successCallback!!!");
		console.log("msg : ", msg);
		console.log("context : ", context);
		
		var content = msg.content;
		var dataList = content.list;

		var deviceModelListHtml = "";

		if(dataList.length != 0){
			$.each(dataList, function(index, data){
				deviceModelListHtml += "<tr>";
				deviceModelListHtml += "	<td><input type='radio' id='checkList' name='checkList' value='" + data.id + "'></td>";
				deviceModelListHtml += "	<td>" + data.manufacturer + "</td>";
				deviceModelListHtml += "	<td><a id='" + data.id + "' href='javascript:void(0);' class='clickBtn'>" + data.modelName + "</a></td>";
				deviceModelListHtml += "	<td>" + data.description + "</td>";
				deviceModelListHtml += "	<td>" + data.createTime + "</td>";
				deviceModelListHtml += "	<td>" + data.updateTime + "</td>";
				deviceModelListHtml += "</tr>";
			});
		}else{
			deviceModelListHtml += "<tr><td colspan='6'>데이터가 없습니다.</td></tr>";
		}
		
		$('#deviceModelList').html(deviceModelListHtml);
		
	};
	
	
	
	var addDeviceModelSuccessCallback = function(msg, context) {
//		console.log("addDeviceModelSuccessCallback!!!");
//		console.log("msg : ", msg);
//		console.log("context : ", context);

		getDeviceModelList();
		$("#addDialog").dialog("close");
		
	};
	
	
	var addFileUploadSuccessCallback = function(msg, context) {
//		console.log("addFileUploadSuccessCallback!!!");
//		console.log("msg : ", msg);
//		console.log("context : ", context);
//		console.log("parameter : ", msg.parameter);
		
		

		var oui = $("#oui").val();
		var manufacturer = $("#manufacturer").val();
		var modelName = $("#modelName").val();
		var deviceType = $("#deviceType").val();
		//var iconUrl = $("#iconUrl").val();
		var iconUrl = "";
		if (msg.parameter != null) {
			iconUrl = msg.parameter.hostUrl + msg.parameter.uploadDir + msg.content.fileInfo;
		}
		
		var profileVer = $("#profileVer").val();
		var description = $("#description").val();
		var applyYn = $("#applyYn").val();
		
		
		
		var data = {
				oui: oui,
				manufacturer: manufacturer,
				modelName: modelName,
				deviceType: deviceType,
				iconUrl: iconUrl,
				profileVer: profileVer,
				description: description,
				applyYn: applyYn,
				jsonList: deviceModelAttributeList
		};

		
		var requestParam = {	
				data: data,
				successCallback: addDeviceModelSuccessCallback
		};

		var context = { 
				"param" : requestParam.data, 
				"handler": requestParam.successCallback 
		};		


		hdp_add_device_model(context, false);
		
	};
	
	
	var fnCommFileUpload = function(){
		
			var oui = $("#oui").val();
			var manufacturer = $("#manufacturer").val();
			var modelName = $("#modelName").val();
			var deviceType = $("#deviceType").val();
			var iconUrl = $("#iconUrl").val();
			var profileVer = $("#profileVer").val();
			var description = $("#description").val();
			var applyYn = $("#applyYn").val();
		
			var dialogErrMsg = validationCheck(oui, manufacturer, modelName, deviceType, profileVer, applyYn);
			
			if(dialogErrMsg){
				

				if($("#iconUrl").val() == ""){
					var msg = {
							content: {
								fileUploadDir: "",
								fileInfo: ""
							}
					}
					addFileUploadSuccessCallback(msg, "");		
				}else{

					var data = new FormData();
					data.append("iconUrl", $("#iconUrl")[0].files[0]);

					//console.log("data1 : ", $("#iconUrl").val());	
					//console.log("data2 : ", data);	
					
					var requestParam = {	
							data: data,
							successCallback: addFileUploadSuccessCallback
					};

					var context = { 
							"param" : requestParam.data, 
							"handler": requestParam.successCallback 
					};			
			
					hdm_common_FileUpload(context, false);
					
				}			
			}else{
				createDialogMessage(dialogErrMsg);
			}
	};
	
	
	

	var createAddDialog = function(){
		
		$("#addDialog").dialog({
			autoOpen: false,
			width: 575,
	 		height: 350,
	 		modal: false,
	 		title: "디바이스 모델 등록",
	 		buttons: {
	 			"저장": function(){
	 				//$(this).dialog("close");	 				
	 				fnCommFileUpload();
	 				
	 			},
	 			"취소": function(){
					//입력값 초기화
					fnInputValInit();
	 				$(this).dialog("close");
	 			}
	 		},
	 		close: function(){

				//입력값 초기화
				fnInputValInit();
	 			//다이얼로그 종료
	 			$(this).dialog("close");
	 		}
		});
		
	};	
	
	
	var delDeviceModelSuccessCallback = function(msg, context) {
		console.log("delDeviceModelSuccessCallback!!!");
		console.log("msg : ", msg);
		console.log("context : ", context);				
		
		getDeviceModelList();
	};
	
	
	//입력값 초기화
	var fnInputValInit = function(){	
		$("#oui").val("");
		$("#manufacturer").val("");
		$("#modelName").val("");
		$("#deviceType").val("");
		$("#profileVer").val("");
		$("#description").val("");
		$("#applyYn").val("");		
	};
	
	var validationCheck = function(oui, manufacturer, modelName, deviceType, profileVer, applyYn){
		var errorMessage = true;
		
		if(oui == "" || oui == null){
			errorMessage = "제조사oui는 반드시 입력되어야 하는 필드입니다.</br>필드의 입력 상태를 확인해 주십시오.";
			createDialogMessage(errorMessage);
			errorMessage = false;
		}
		if(modelName == "" || modelName == null){
			errorMessage = "모델번호는 반드시 입력되어야 하는 필드입니다.</br>필드의 입력 상태를 확인해 주십시오.";
			createDialogMessage(errorMessage);
			errorMessage = false;
		}
		if(applyYn == "" || applyYn == null){
			errorMessage = "적용여부는 반드시 선택되어야 하는 필드입니다.</br>필드의 선택 상태를 확인해 주십시오.";
			createDialogMessage(errorMessage);
			errorMessage = false;
		}
		
		return errorMessage;
	};
	
	
	var initUIComponents = function(){
		addEventHandler();
		createAddDialog();
	};

	var addEventHandler = function(){
		
		$("#addDeviceModel").click(function() {
			$("#addDialog").dialog("open");
		});
		
		$("#delDeviceModel").click(function() {
			

			var idx = getRadioValue($("#deviceModelList input:radio"));
			if(idx != ""){
				$("#confirmDialogMessage").html("삭제하시겠습니까?");	
				$("#confirmDialog").dialog("open");
				$("#confirmDialog").dialog({
					autoOpen: false,
					width: 450,
					height: 180,
					modal: true,
					title: "결과",
					buttons: {
						"확인": function(){

							 
		 					var data = {
		 							"checkList": idx
		 					};						
		 							 					
		 					var requestParam = {	
		 							data: data,
		 							successCallback: delDeviceModelSuccessCallback
		 					};

		 					var context = { 
		 							"param" : requestParam.data, 
		 							"handler": requestParam.successCallback 
		 					};			

		 					console.log(context);			
		 					hdp_del_device_model(context, false);
		 					
							$(this).dialog("close");					
						},
						"취소": function(){
							$(this).dialog("close");
						}
					},
					close: function(){
						$("#confirmDialogMessage").html("");
					}
				});					
			}else{
				errorMessage = "선택된 항목이 없습니다.";
				createDialogMessage(errorMessage);
				errorMessage = false;
			}
			
			
			/*	check박스로 작업중 
			if(checkOneMoreCheckbox($("#deviceModelList input:checkbox"), "checkList")){
				$("#confirmDialogMessage").html("삭제하시겠습니까?");	
				$("#confirmDialog").dialog("open");
				$("#confirmDialog").dialog({
					autoOpen: false,
					width: 450,
					height: 180,
					modal: true,
					title: "결과",
					buttons: {
						"확인": function(){


							var delArrayDataList = new Array();							
							$("#deviceModelList input:checkbox:checked").each(function(idx, row) {
								delArrayDataList.push(row.value);
							});
							
							 
		 					var data = {
		 							"checkList": delArrayDataList
		 					};						
		 							 					
		 					var requestParam = {	
		 							data: data,
		 							successCallback: delDeviceModelSuccessCallback
		 					};

		 					var context = { 
		 							"param" : requestParam.data, 
		 							"handler": requestParam.successCallback 
		 					};			

		 					console.log(context);			
		 					hdp_del_device_model(context, false);
		 					
							$(this).dialog("close");					
						},
						"취소": function(){
							$(this).dialog("close");
						}
					},
					close: function(){
						$("#confirmDialogMessage").html("");
					}
				});
			}
			 */
			
			
			
		});
		
		
		
		//디바이스 모델 상세페이지 이동
		$("#deviceModelList").on("click", ".clickBtn",  function() {
			var id = $(this).context.id;
			window.location.href = detailDeviceModelUrl + "?id=" + id;
		});
		
	};


	var createDialogMessage = function(dialogMessage){
		$("#messageDialogMessage").html(dialogMessage);
		$("#messageDialog").dialog("moveToTop"); 
		$("#messageDialog").dialog("open");
	};
	
	
	return {
		initialize: initialize
	};
})();