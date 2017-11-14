
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
	
	/******* START SK C&C ********/
	function hdh_get_statusHist_list(context, loading) {
		console.log("hdh_get_statusHist_list called context:");
		console.log(context);

		var resourceUri = context.resourceUri;
		var resourceName = context.resourceName;
		
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/history/statusData/list.do",
		  contentType: "application/json",
		  data: {"resourceUri":resourceUri, "resourceName":resourceName},
		  context: context
		})
		.done(function(msg) {
			hdb_result_handler(msg, context); 
			});
		
	}
	
	function hdp_get_thresholdData(context, loading) {
		console.log("hdp_get_thresholdData called context:");
		console.log(context);
		
		var resourceUri = context.resourceUri;
		
		$.ajax({
			type: "GET",
			dataType: "json",
			url: "/hdm/api/hdp/deviceModel/threshold/get.do",
			contentType: "application/json",
			data: {"resourceUri":resourceUri},
			context: context
		})
		.done(function(msg) {
			hdb_result_handler(msg, $(this)[0]);
			});
	}
	/******* END SK C&C ********/

	
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
	function hdb_get_platform_summary(context, loading)
	{
		console.log("hdb_get_platform_summary called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/platform/summary/get.do",
		  contentType: "application/json",
		  data: {},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
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
	
	//context: {"handler": handlerFunction};
	function hdb_get_devicestat_count(context, loading)
	{
		console.log("hdb_get_devicestat_count called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/statCount/get.do",
		  contentType: "application/json",
		  data: {},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
	//context: {"handler": handlerFunction};
	function hdb_get_devicecount_per_model(context, loading)
	{
		console.log("hdb_get_devicecount_per_model called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdm/device/countPerModel/list.do",
		  contentType: "application/json",
		  data: {},
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});
	}
	
	//context: {"handler": handlerFunction};
	function hdb_get_devicereg_count(context, loading)
	{
		console.log("hdb_get_devicereg_count called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdm/deviceReg/count/get.do",
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

//USE
	//context: {"param": {"deviceId":deviceId}, "handler": handlerFunction, "manualRenderer": rendererFunction};
	/*function hdb_get_device_firmware_list(context, loading) 
	{
		console.log("hdb_get_device_firmware_list called context:");
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
	}*/

	//context: {"param": {"package":package, "oui":oui, "modelName":modelName}, "handler": handlerFunction};
	function hdb_get_firmware_version_list(context, loading) 
	{
		console.log("hdb_get_firmware_version_list called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdp/firmware/version/list.do",
		  contentType: "application/json",
		  data: context.param,
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

	//context: {"param":{"today":today}, "handler": handlerFunction};
	function hdb_get_device_today(context, loading)
	{
		console.log("hdb_get_device_today called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/history/deviceToday/get.do",
		  contentType: "application/json",
		  data: context.param,
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
	function hdb_get_stat_register_day_per_model(context, loading) {
		console.log("hdb_get_stat_register_day_per_model called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/stat/registerDayPerModel/list.do",
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

	//context: {"param":{"start":start, "end":end}, "handler": searchResultHandler };
	function hdb_get_stat_register_month_per_model(context, loading) {
		console.log("hdb_get_stat_register_month_per_model called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/stat/registerMonthPerModel/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}


	//context: {"param":{"today":"2015-04-20 00:00:00", "thisMonth":"2015-04-01 00:00:00"}, "handler": searchResultHandler };
	function hdb_get_stat_error_summary(context, loading) {
		console.log("hdb_get_stat_error_summary called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/stat/errorSummary/get.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}

	//context: {"param":{"start":start, "end":end}, "handler": searchResultHandler };
	function hdb_get_stat_error_day(context, loading) {
		console.log("hdb_get_stat_error_day called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/stat/errorDay/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}


	//context: {"param":{"start":start, "end":end}, "handler": searchResultHandler };
	function hdb_get_stat_error_month(context, loading) {
		console.log("hdb_get_stat_error_month called context:");
		console.log(context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/stat/errorMonth/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
			});		
	}
	
	
	function hdm_get_error_device_list(context, loading){
		console.log("hdm_get_error_device_list called context : ", context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdm/error/device/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});
	}	
	
	function hdm_get_error_message_list(context, loading){
		console.log("hdm_get_error_message_list called context : ", context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/error/message/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});
	}	
	

	function hdm_get_device_controll_history_list(context, loading){
		console.log("hdm_get_device_controll_history_list called context : ", context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/devicecontroll/history/list.do",
		  contentType: "application/json",
		  data: context.param,
		  context: context
		})
		.done(function(msg) { 
			hdb_result_handler(msg, $(this)[0]); 
		});
	}
	function hdm_get_device_error_history_list(context, loading){
		console.log("hdm_get_device_error_history_list called context : ", context);
				
		$.ajax({
		  type: "GET",
		  dataType: "json",
		  url: "/hdm/api/hdh/deviceerror/history/list.do",
		  contentType: "application/json",
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
			console.log("v1 : " + JSON.stringify(context));
			//console.log(JSON.stringify(msg));
			
			console.log("msg is : "+JSON.stringify(msg));
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
	