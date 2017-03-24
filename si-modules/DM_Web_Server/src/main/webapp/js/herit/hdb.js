
	//context: {"param": {"deviceId":deviceId, 
	//					  "page": 1},
	//			"handler": upgradeResultHandler};
	function hdb_get_history(type, context, loading)
	{
		console.log("hdb_error_history called context:");
		console.log(context);
		
		//var deviceId = context.deviceId;
		//var page = context.page;
		
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/history/"+type+"/listPage.do",
		  contentType: "application/json",
		  //data: {"deviceId":deviceId, "page": page},
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
	//context: {"oui":"001122", 
	//			"modelName": "HGW_01",
	//			"sn": "HR_GV_0001",
	//			"limit": 50, 
	//			"handler": searchDeviceHandler};
	function hdb_search_device_for_select(context, loading)
	{
		console.log("hdb_search_device called context:");
		console.log(context);
		
		var oui = context.oui;
		var modelName = context.modelName;
		var sn = context.sn;
		var limit = context.limit;
		
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/select/list.do",
		  contentType: "application/json",
		  data: {"oui":oui, "modelName":modelName, "sn": sn+"%", "limit":limit},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}

	
	//context: {"oui":"001122", "modelName": "HDB_GV_001", 
	//			"handler": handlerFunction};
	function hdb_get_device_package(context, loading)
	{
		console.log("hdb_get_device_package called context:");
		console.log(context);
		
		var oui = context.oui;
		var modelName = context.modelName;
		
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/firmwarePackage/list.do",
		  contentType: "application/json",
		  data: {"oui":oui, "modelName":modelName},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}


	//context: {"handler": handlerFunction};
	function hdb_get_deviceModel_list(context, loading)
	{
		console.log("hdb_get_deviceModel_list called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/model/list.do",
		  contentType: "application/json",
		  data: {},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
	//context: {"id": 123, "handler": handlerFunction};
	function hdb_get_deviceModel(context, loading)
	{
		console.log("hdb_get_deviceModel_info called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/info/get.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
	/****************************START Noti_Condition*******************************/
/*	//context: {"oui":"001122", "modelName": "HDB_GV_001", 
	//			"handler": handlerFunction};
	function hdb_get_notiCondition_profile(context, loading)
	{
		console.log("hdb_get_device_profile called context:");
		console.log(context);
		
		var moProfileId = context.moProfileId;
		var conditionType = context.conditionType;
		var condition = context.condition;
		
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/notiCondition/profile.do",
		  contentType: "application/json",
		  data: {"moProfileId":moProfileId,"conditionType":conditionType, "condition":condition},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}*/
	
/*	//context: {"param":{ "id":id, "name":name, "loginId":loginId, "loginPwd":password, "email":email, "accountGroupId":accountGroupId},
	//		"handler": regUserHandler};
	function hdb_update_notiCondition(context, loading) {
		console.log("hdb_update_notiCondition called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/notiCondition/set.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}
	*/
	function hdb_get_noti_condition(context, loading) {
		console.log("hdb_get_noti_condition called context:");
		console.log(context);
		
		$.ajax({
			type: "GET",
			dataType: "json",
			url: "/hdm/api/hdp/deviceModel/notiCondition/list.do",
			contentType: "application/json",
			data: context.param,
			context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	/****************************END Noti_Condition*******************************/

	//context: {"handler": handlerFunction};
	function hdb_get_device_count(context, loading)
	{
		console.log("hdb_get_device_count called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/count/get.do",
		  contentType: "application/json",
		  data: {},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}

	//context: {"param": {}, "handler": handlerFunction, "manualRenderer": rendererFunction};
	function hdb_get_user_list(context, loading) 
	{
		console.log("hdb_get_user_list called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/account/info/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}

	//context: {"param": {"deviceId":deviceId}, "handler": handlerFunction, "manualRenderer": rendererFunction};
	function hdb_get_device_firmware_list(context, loading) 
	{
		console.log("hdb_get_user_list called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/firmware/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
	
//HERE FIRMWARE STATUS SELECT ***********************************************
	function hdb_get_firmware(type, context, loading) {
		console.log("hdb_get_firmware called context:");
		console.log(context);
		
		//var deviceId = context.deviceId;
		//var page = context.page;
		
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/"+type+"/listPage.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
//HERE FIRMWRAE SCHEDULE INSERT
	function hdb_firmware_schedule(context, loading) {
		console.log("hdb_firmware_schedule called context");
		console.log(context);
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url: "/hdm/api/hdp/firmware/schedule/register.do",
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			data: context.param,
			context: context
		})
		.done(function(msg){
			hdb_result_handler(msg, $(this)[0]);
		});
	}
	
//HERE FIRMWARE DETAIL BASICINFO UPDATE
	function hdb_update_device_firmware(context, loading) {
		console.log("hdb_update_device_firmware called context:");
		console.log(context);
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url: "/hdm/api/hdp/firmware/info/update.do",
			//  contentType: "application/json",
			//url: "/hdm/api/hdm/firmware/info/update.do?id="+id,
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			data: context.param,
			context: context
		})
		.done(function(msg) {
			hdb_result_handler(msg, $(this)[0]);
			});
	}	
	
//HERE FIRMWARE VERSION DELETE
	function hdb_delete_device_firmwareVersion(context, loading) {
		console.log("hdb_delete_device_firmwareVersion called context:");
		console.log(context);
		
		$.ajax({
			type: "POST",
			dataType: "json",
			url: "/hdm/api/hdp/firmware/version/delete.do",
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			data: context.param,
			context: context
		})
		.done(function(msg) {
			hdb_result_handler(msg, $(this)[0]);
			});
	}

//HERE FIRMWARE VERSION INSERT (+FILEUPLOAD)
	function hdb_insert_device_firmwareVersion(context, loading) {
		console.log("hdb_insert_device_firmwareVersion called context:");
		console.log(context);
		
		var form = $('#form-registerVersion')[0];
		var formData = new FormData(form);
		
		//formData.append("inputfile",$("input[name=inputfile]")[0].files[0]);
		//formData.append("inputFile",$("#inputFile")[0].files[0]);
		
		formData.append("firmwareId", context.param.firmwareId);
		formData.append("packageName", context.param.packageName);
		
		//var data = new FormData();
		/*$.each($('#inputFile')[0].files, function(i, rmfjfile) { 
			console.log("up");
			console.log("i="+i+", file="+file);
			formData.append('inputFile-' + i, file);
			console.log("down");
        });*/
		
		$.ajax({
			type: "POST",
			url: "/hdm/api/hdp/firmware/version/upload.do",
			contentType: false,
			processData: false,
			data: formData,
			context: context 
		})
		.done(function(msg) {
			hdb_result_handler(msg, $(this)[0]);
			});
	}
	
//HERE FIRMWARE VERSION UPDATE (+FILEUPLOAD)
	function hdb_update_device_firmwareVersion(context, loading) {
		console.log("hdb_update_device_firmwareVersion called context:");
		console.log(context);
		
		var form = $('#form-updateVersion')[0];
		var formData = new FormData(form);
		
		formData.append("firmwareId", context.param.firmwareId);
		formData.append("packageName", context.param.packageName);
		formData.append("version", context.param.reversion);
		
		console.log("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee : " + context.param.firmwareId + context.param.packageName + context.param.reversion);
		
		$.ajax({
			type: "POST",
			url: "/hdm/api/hdp/firmware/versionUpdate/upload.do",
			contentType: false,
			processData: false,
			data: formData,
			context: context 
		})
		.done(function(msg) {
			hdb_result_handler(msg, $(this)[0]);
			});
	}
	
	//context: {"grade": 1, "handler": handlerFunction};
	function hdb_get_device_error_list(context, loading)
	{
		console.log("hdb_get_device_error_list called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/error/list.do",
		  contentType: "application/json",
		  data: {"grade":context.grade},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}

	//context: {"id": 0, "limit":20, "handler": handlerFunction};
	function hdb_get_history_error_list(context, loading)
	{
		console.log("hdb_get_history_error_list called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/history/error/list.do",
		  contentType: "application/json",
		  data: {"id":context.id, "limit":context.limit},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
//HERE
	//context: {"param":{ "deviceModelId":id, "sn":sn},
	//		"handler": regDeviceHandler};
	function hdb_register_device(context, loading) {
		console.log("hdb_register_device called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/info/register.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}
	
/** oneM2MDevice aeId ++ (디바이스 등록)*/
	function hdb_register_oneM2MDevice(context, loading) {
		console.log("hdb_register_oneM2MDevice called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/info/oneM2MRegister.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}
//HERE2	
	//context: {"param":{ "deviceModelId":id, "sn":sn},
	//		"handler": regDeviceHandler};
	function hdb_registers_device(context, loading) {
		console.log("hdb_registers_device called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/info/registers.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}

	//context: {"param":{ "name":name, "loginId":loginId, "loginPwd":password, "email":email, "accountGroupId":accountGroupId},
	//		"handler": regUserHandler};
	function hdb_add_user(context, loading) {
		console.log("hdb_add_user called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/account/info/insert.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}
	//context: {"param":{ "id":id, "name":name, "loginId":loginId, "loginPwd":password, "email":email, "accountGroupId":accountGroupId},
	//		"handler": regUserHandler};
	function hdb_update_user(context, loading) {
		console.log("hdb_update_user called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/account/info/set.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}
	
	//context: {"param":{ "id":id }, "handler": regUserHandler};
	function hdb_delete_user(context, loading) {
		console.log("hdb_delete_user called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/account/info/delete.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}

	//context: {"param":{"start":start, "end":end}, "handler": searchResultHandler };
	function hdb_get_stat_usage_day(context, loading) {
		console.log("hdb_get_stat_usage_day called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/stat/usageDay/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});				
	}

	//context: {"param":{"start":start, "end":end}, "handler": searchResultHandler };
	function hdb_get_stat_usage_month(context, loading) {
		console.log("hdb_get_stat_usage_month called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/stat/usageMonth/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}

	//context: {"param":{"start":start, "end":end}, "handler": searchResultHandler };
	function hdb_get_stat_register_day(context, loading) {
		console.log("hdb_get_stat_register_day called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/stat/registerDay/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}

	//context: {"param":{"start":start, "end":end}, "handler": searchResultHandler };
	function hdb_get_stat_register_month(context, loading) {
		console.log("hdb_get_stat_register_month called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/stat/registerMonth/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}


	//context: {"oui":"001122", "modelName": "HDB_GV_001", 
	//			"handler": handlerFunction};
	function hdb_get_device_profile(context, loading){
		console.log("hdb_get_device_profile called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/info/profile.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});
	}
	

	function hdp_get_device_model_list(context, loading) {
		console.log("hdp_get_device_model_list called context:");
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/model/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	

	function hdm_common_FileUpload(context, loading) {
		console.log("hdm_common_FileUpload called context:");
		
		$.ajax({
			url: "/hdm/api/hdp/deviceModel/info/upload.do",
			contentType : "multipart/form-data",
			type: "post",
			dataType: "json",
			processData: false,
			contentType: false,
			data: context.param,
			context: context
		})
		.done(function(msg) { 
			console.log("msg : ", msg);
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	
	function hdp_add_device_model(context, loading) {
		console.log("hdp_add_device_model called context:");
		console.log(context);
		
		$.ajax({
			url: "/hdm/api/hdp/deviceModel/info/insertAll/json.do",
			contentType : "application/json",
			type: "post",
		    async: true,
			dataType: "json",
		    timeout: 10000, //제한시간 지정
			data: JSON.stringify(context.param),
			context: context
		})
		.done(function(msg) { 
			console.log("msg : ", msg);
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	
	function hdp_update_device_model(context, loading) {
		console.log("hdp_update_device_model called context:");
		console.log(context);
		
		$.ajax({
			url: "/hdm/api/hdp/deviceModel/info/set/json.do",
			contentType : "application/json",
			type: "post",
		    async: true,
			dataType: "json",
		    timeout: 10000, //제한시간 지정
			data: JSON.stringify(context.param),
			context: context
		})
		.done(function(msg) { 
			console.log("msg : ", msg);
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	
	function hdp_del_device_model(context, loading) {
		console.log("hdp_del_device_model called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/info/deleteAll.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	

	//context: {"id": 123, "handler": handlerFunction};
	function hdb_get_info_deviceModel(context, loading){
		console.log("hdb_get_deviceModel_info called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/info/getInfo.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
	function hdp_get_mo_profile_list(context, loading) {
		console.log("hdp_get_mo_profile_list called context:");
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moProfile/listAll.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}


	function hdp_add_mo_profile(context, loading) {
		console.log("hdp_add_mo_profile called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moProfile/insert.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	function hdp_update_mo_profile(context, loading) {
		console.log("hdp_add_mo_profile called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moProfile/set.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	function hdp_del_mo_profile(context, loading) {
		console.log("hdp_del_mo_profile called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moProfile/deleteAll.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	function hdp_add_mo_profile_copy_version(context, loading) {
		console.log("hdp_add_mo_profile called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moProfile/copyVersionInsert.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}

	function hdp_get_mo_option_data_list(context, loading) {
		console.log("hdp_get_mo_option_data_list called context:");
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moOptionData/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	function hdp_get_mo_error_code_list(context, loading) {
		console.log("hdp_get_mo_error_code_list called context:");
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moErrorCode/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	function hdp_get_mo_noti_condition_list(context, loading) {
		console.log("hdp_get_mo_noti_condition_list called context:");
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moNotiCondition/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}

	function hdp_add_mo_option_data(context, loading) {
		console.log("hdp_add_mo_option_data called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moOptionData/insert.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	function hdp_del_mo_option_data(context, loading) {
		console.log("hdp_del_mo_option_data called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moOptionData/delete.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}

	function hdp_add_mo_error_code(context, loading) {
		console.log("hdp_add_mo_error_code called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moErrorCode/insert.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	function hdp_del_mo_error_code(context, loading) {
		console.log("hdp_del_mo_error_code called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moErrorCode/delete.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}

	function hdp_add_mo_noti_condition(context, loading) {
		console.log("hdp_add_mo_noti_condition called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moNotiCondition/insert.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	function hdp_del_mo_noti_condition(context, loading) {
		console.log("hdp_del_mo_noti_condition called context:");
		console.log(context);
				
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/hdp/deviceModel/moNotiCondition/delete.do",
		  contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});		
	}
	
	
	function hdb_result_handler(msg, context) {
		console.log("hdb_result_handler called ");

		
		try
		{				
			console.log(JSON.stringify(context));
			console.log(JSON.stringify(msg));
			
			console.log(msg);
			//var obj = jQuery.parseJSON(msg);
			var handled = false;
			if (msg.result == 0) {	
				//show_with_result(obj);
				if (typeof context.handler != "undefined" && context.handler != null)
				{
					handled = context.handler(msg, context);
				}
				if (!handled) {
					// 기본 처리 코드		
				}
			} else {
				if (typeof context.errorHandler != "undefined" && context.errorHandler != null)
				{
					handled = context.errorHandler(msg, context);
				}
				if (!handled) {
					alert("요청 처리중 오류가 발생했습니다. 입력값을 확인 후 다시 시도해주세요.\r\nErrorCode:" + msg.errorCode +"\r\nErrorMsg:"+msg.content);				
				}
			}
		}
		catch (e)
		{
			alert("요청 처리중 오류가 발생했습니다. 관리자에게 문의해주세요.")
			console.error("exception in dm_result_handler ");
			console.error("message:"+e.message);
			console.error("stack:"+e.stack);
		}
		//ajaxEnd();
	}
	