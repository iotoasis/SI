
	//context: {"deviceId":deviceInfo.deviceId, 
	//			"packageName": "herit.gasvalve.svc1", 
	//			"firmwareVersion": "1.1", 
	//			"handler": upgradeResultHandler};
	function dm_firmware_upgrade(context, loading)
	{
		console.log("dm_firmware_upgrade called context:");
		console.log(context);
		
		if (loading == true)
		{
			ajaxStart();
		}
		
		var deviceId = context.deviceId;
		var packageName = context.packageName;
		var version = context.version;
		
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/gw/device/firmware_upgrade.do",
		  contentType: "application/json",
		  data: JSON.stringify({"d":deviceId, "pk": packageName, "fv": version}),
		  context: context
		})
		.done(function(msg) { 
			dm_result_handler(msg, $(this)[0]); 
			});
	}
	
	//context: {"deviceId":deviceInfo.deviceId, 
	//			"handler": debugResultHandler};
	function dm_debug_start(context, loading)
	{
		console.log("dm_debug_start called context:");
		console.log(context);
		
		if (loading == true)
		{
			ajaxStart();
		}
		
		var deviceId = context.deviceId;
		
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/gw/device/debug_start.do",
		  contentType: "application/json",
		  data: JSON.stringify({"d":deviceId, "it":10, "du":600 }),
		  context: context
		})
		.done(function(msg) { 
			dm_result_handler(msg, $(this)[0]); 
			});
	}

	//context: {"deviceId":deviceInfo.deviceId, 
	//			"handler": debugResultHandler};
	function dm_debug_stop(context, loading)
	{
		console.log("dm_debug_stop called context:");
		console.log(context);
		
		if (loading == true)
		{
			ajaxStart();
		}
		
		var deviceId = context.deviceId;
		
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/gw/device/debug_stop.do",
		  contentType: "application/json",
		  data: JSON.stringify({"d":deviceId }),
		  context: context
		})
		.done(function(msg) { 
			dm_result_handler(msg, $(this)[0]); 
			});
	}

	//context: {"deviceId":deviceInfo.deviceId, 
	//			"handler": debugResultHandler};
	function dm_reboot(context, loading)
	{
		console.log("dm_reboot called context:");
		console.log(context);
		
		if (loading == true)
		{
			ajaxStart();
		}
		
		var deviceId = context.deviceId;
		
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/gw/device/reboot.do",
		  contentType: "application/json",
		  data: JSON.stringify({"d":deviceId }),
		  context: context
		})
		.done(function(msg) { 
			dm_result_handler(msg, $(this)[0]); 
			});
	}

	//context: {"deviceId":deviceInfo.deviceId, 
	//			"handler": debugResultHandler};
	function dm_factory_reset(context, loading)
	{
		console.log("dm_factory_reset called context:");
		console.log(context);
		
		if (loading == true)
		{
			ajaxStart();
		}
		
		var deviceId = context.deviceId;
		
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/gw/device/factory_reset.do",
		  contentType: "application/json",
		  data: JSON.stringify({"d":deviceId }),
		  context: context
		})
		.done(function(msg) { 
			dm_result_handler(msg, $(this)[0]); 
			});
	}
	
	//context: {"deviceId":deviceInfo.deviceId, 
	//			"resourceUris":[resUri], 
	//			"e":[ {"n":resUri, "sv":resVal} ], 
	//			"handler": executeResultHandler};
	function dm_execute(context, loading)
	{
		console.log("dm_execute called context:");
		console.log(context);
		
		if (loading == true)
		{
			ajaxStart();
		}
		
		var deviceId = context.deviceId;
		var resUri = context.resourceUris[0];
		var e = context.e; 
		
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/gw/device/execute.do",
		  contentType: "application/json",
		  data: JSON.stringify({"d":deviceId, "e": e}),
		  context: context
		})
		.done(function(msg) { 
			dm_result_handler(msg, $(this)[0]); 
			});
	}

	function dm_read(context, loading)
	{
		console.log("dm_read called context:");
		console.log(context);
		
		if (loading == true)
		{
			ajaxStart();
		}
		
		var deviceId = context.deviceId;
		var resUri = context.resourceUri;
		var e = context.e; 
		
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  contentType: "application/json",
		  url: "/hdm/api/gw/device/read.do",
		  data: JSON.stringify({"d":deviceId, "e": e }),
		  context: context
		})
		.done(function(msg) { 
			dm_result_handler(msg, $(this)[0]); 
			});
	}

	function dm_write(context, loading)
	{
		console.log("dm_writecalled context:");
		console.log(context);
		
		if (loading == true)
		{
			ajaxStart();
		}
		
		var deviceId = context.deviceId;
		var resUri = context.resourceUris[0];
		var e = context.e; 
		
		$.ajax({
		  type: "POST",
		  dataType: "json",
		  url: "/hdm/api/gw/device/write.do",
		  contentType: "application/json",
		  data: JSON.stringify({"d":deviceId, "e": e}),
		  context: context
		})
		.done(function(msg) { 
			dm_result_handler(msg, $(this)[0]); 
			});
	}
	
	function dm_result_handler(msg, context) {
		console.log("dm_result_handler called ");
		
		//try
		//{				
			console.log(msg);
			//var obj = jQuery.parseJSON(msg);
			var handled = false;
			if (msg.result == 0 && msg.content.r == "200")
			{	
				//show_with_result(obj);
				if (typeof context.handler != "undefined" && context.handler != null)
				{
					handled = context.handler(msg, context);
				}
				if (!handled) {
					// 응답처리 기본 로직 
				}
			} else {
				if (typeof context.errorHandler != "undefined" && context.errorHandler != null)
				{
					handled = context.errorHandler(msg, context);
				}
				if (!handled) {
					switch (msg.errorCode) {
					case 830:
					case 831:
						alert("디바이스가 연결되어 있지 않습니다. 디바이스가 네트워크와 연결되어 있는지 확인 후 다시 시도해주세요.(" + msg.errorCode +")");
						break;
					case 404:
						alert("디바이스가 등록(REGISTRATION)되지 않았습니다. 디바이스가 네트워크와 연결되어 있는지 확인 후 다시 시도해주세요.(" + msg.errorCode +")");				
						break;
					default:
						alert("요청 처리중 오류가 발생했습니다. 디바이스 연결을 확인 후 다시 시도해주세요.\r\nErrorCode:" + msg.errorCode +"\r\nErrorMsg:"+msg.content);				
					}
				}
				
			}
		//}
		//catch (e)
		//{
		//	console.log("exception in dm_result_handler ");
		//	console.log(e);
		//}
		ajaxEnd();
	}


	function dm_get_resource_value_from_msg(msg, uri) {
		var value;
		for (var i=0; i<msg.content.e.length; i++) {
			if (uri == msg.content.e[i].n) {
				value = msg.content.e[i].sv;
				break;
			}			
		}
		return value;
	}
	
	function show_with_result(obj)
	{
		obj.content.ns = new Array();
		for (var i=0; i<obj.content.e.length; i++)
		{
			if (typeof obj.content.e[i].e  == "undefined" )
			{
				var e2 = obj.content.e[i];
				try
				{
					console.log(e2.n+" : "+e2.sv +":"+ $("#n_"+e2.n.replace(" ", "_")).get(0).tagName);
					setTagValue(e2.n, e2.sv);
					obj.content.ns[obj.content.ns.length] = {"n":e2.n, "sv":e2.sv};
				}
				catch (e)
				{
					console.error("message:"+e.message);
					console.error("stack:"+e.stack);
				}
			} else 
			{
				var e2 = obj.content.e[i].e;
				for (var j=0; j<e2.length; j++)
				{
					try
					{
						console.log(e2[j].n+" : "+e2[j].sv);
						setTagValue(e2[j].n, e2[j].sv);
						//$("#n_"+e2[j].n.replace(" ", "_")).text(e2[j].sv);	
						obj.content.ns[obj.content.ns.length] = {"n":e2[j].n, "sv":e2[j].sv};							
					}
					catch (e)
					{
						console.error("message:"+e.message);
						console.error("stack:"+e.stack);
					}
				}
			}
		}
	}

	function setTagValue(name, value) {
		if ($("#n_"+name.replace(" ", "_")).get(0).tagName == "SELECT")
		{
			$("#n_"+name.replace(" ", "_")).val(value.split(":")[0]);
		} else  if  ($("#n_"+name.replace(" ", "_")).get(0).tagName == "INPUT")
		{
			$("#n_"+name.replace(" ", "_")).val(value);
		} else
		{
			$("#n_"+name.replace(" ", "_")).text(value);	
		}
	}

	var ajaxCount = 0;
	function ajaxStart()
	{
		return;
		if (ajaxCount == 0)
		{
			ajaxCount ++;
			$(".ajaxLoader").show();
		} else 
		{	
			ajaxCount ++;
		}
	}

	function ajaxEnd()
	{
		return;
		if (ajaxCount > 0)
		{
			ajaxCount --;
			if (ajaxCount == 0)
			{
				$(".ajaxLoader").hide();
			}
		}
	}