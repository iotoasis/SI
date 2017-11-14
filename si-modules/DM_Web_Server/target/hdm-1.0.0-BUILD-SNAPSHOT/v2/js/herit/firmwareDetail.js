function initUI2() {
	console.log("initUI called");
	
	/*
	//기본정보수정
	dlg_updateFirmBasic = $( "#dialog-updateFirmBasic" ).dialog({
		  autoOpen: false,
		  height: 200,
		  width: 300,
		  modal: true,
		  buttons: {
		    "저장": function() {
		    	updateFirmwareBasic();
		    	//dlg_updateFirmBasic.find( "form" ).submit();
		    	
		    },
		    "취소": function() {
		    	dlg_updateFirmBasic.dialog( "close" );
		    }
		  },
		  close: function() {
			  frm_updateFrimBasic[ 0 ].reset();
		  }
		});
	
		$("#btn_updateFirmBasic").click(function() {
			dlg_updateFirmBasic.dialog("open");
		});
		
		frm_updateFrimBasic = dlg_updateFirmBasic.find( "form" ).on( "submit", function( event ) {
		      //event.preventDefault();
	});
	*/	
		
	//버전정보수정
	$("#btn_updateVersion").click(function() {
		
		var checkedCount = $('input:checkbox[name="versionChecked"]:checked').length;
		var check = $('input:checkbox[name="versionChecked"]:checked');
		
		if (checkedCount > 1) {
			alert("하나의 항목만 선택해주세요.");
			
			check.each(function(index) {
				
				check.prop("checked", false);
				//$('input:checkbox[name="versionChecked"]:checked:first').prop("checked", true);
				check.filter(':first').prop("checked", true);
			});	
		} else {
			if ($('input:checkbox[name="versionChecked"]').is(":checked") == true) {
				
				$('input:checkbox[name="versionChecked"]:checked').each(function(index) {
					console.log(index + "- id:" + $(this).attr("id") +", curVersion:" + $(this).attr("curVersion"));
					
					var firmwareId = $(this).attr("id").substring(3);
					var version = $(this).parents("#versionListRow").children("#firmVersion").text();
					var checksum = $(this).parents("#versionListRow").children("#firmCheckSum").text();
					//var version = $(this).attr("curVersion");
					//var checksum = $(this).attr("curChecksum");
					//var checksum = $("#firmCheckSum").text();
					console.log("firmwareId :" + firmwareId + " checksum :" + checksum + " version :" + version);

					$("#firmware_upversion").val(version);
					$("#firmware_upcheckSum").val(checksum);
					
					$("#updateVersionModal").modal('show');
					//dlg_updateVersion.dialog("open");
				});
				
			} else {
				alert("편집 할 항목을 체크해주세요.");
			}
		}
	});
	
	/*
	dlg_updateVersion = $( "#dialog-updateVersion" ).dialog({
		  autoOpen: false,
		  height: 200,
		  width: 300,
		  modal: true,
		  buttons: {
		    "저장": function() {
		    	updateFirmwareVersion();
		    	//dlg_updateVersion.find( "form" ).submit();
		    },
		    "취소": function() {
		    	dlg_updateVersion.dialog( "close" );
		    }
		  },
		  close: function() {
			  frm_updateVersion[ 0 ].reset();
		  }
		});
		
		frm_updateVersion = dlg_updateVersion.find( "form" ).on( "submit", function( event ) {
		      //event.preventDefault();
	});	
	*/
	
	//버전정보추가
	$("#btn_registerVersion").click(function() {
		dlg_registerVersion.dialog("open");
	});
	
	/*
	dlg_registerVersion = $( "#dialog-registerVersion" ).dialog({
		  autoOpen: false,
		  height: 220,
		  width: 360,
		  modal: true,
		  buttons: {
		    "저장": function() {
		    	insertFirmwareVersion();
		    	//dlg_registerVersion.find( "form" ).submit();
		    },
		    "취소": function() {
		    	dlg_registerVersion.dialog( "close" );
		    }
		  },
		  close: function() {
			  frm_registerVersion[ 0 ].reset();
		  }
		});
		
		frm_registerVersion = dlg_registerVersion.find( "form" ).on( "submit", function( event ) {
		      //event.preventDefault();
	});	
	*/
	
	//버전정보삭제
	$("#btn_deleteVersion").click(function() {
		
		if ($('input:checkbox[name="versionChecked"]').is(":checked") == true) {
			deleteFirmwareVersion();
			
		} else {
			alert("삭제 할 항목을 체크해주세요.");
		}
	});
}

//버전정보수정
function updateFirmwareVersion() {
	console.log("updateFirmwareVersion called");
	
	$('input:checkbox[name="versionChecked"]:checked').each(function(index) {
		console.log(index + "- id:" + $(this).attr("id") +", curVersion:" + $(this).attr("curVersion"));
		
		var firmwareId = $(this).attr("id").substring(3);
		var packageName = $("#firmwarePackageName").text();
		var reversion = $(this).attr("curVersion");
		var version = $("#firmware_upversion").val();
		var checksum = $("#firmware_upcheckSum").val();
		
		if ($("#updateFile").val() != "") {
			var fileInfo = $("#updateFile").val().split("\\");
			var fileName = fileInfo[fileInfo.length-1];
			var fileSize = $("#updateFile")[0].files[0].size;
		} 
		
		console.log("FILE DATA : " + fileName + fileSize);
		
		console.log("firmwareId :" + firmwareId + " packageName : " + packageName + " reversion :" + reversion + " checksum :" + checksum + " version :" + version);
		
		var context = {"param":{"firmwareId": firmwareId, "packageName": packageName, "version": version, "reversion": reversion, "checksum": checksum, "fileName": fileName, "fileSize": fileSize}, "handler": firmwareVersionUpdateHandler};
	
		hdb_update_device_firmwareVersion(context, false);
	});
}

function firmwareVersionUpdateHandler(msg, context) {
	console.log("firmwareVersionUpdateHandler called");
	console.log("context:"+JSON.stringify(context));
	console.log("msg:"+JSON.stringify(msg));
	
	var id = $("#basicFirmwareId").text();
	
	$("#updateVersionModal").modal('hide')
	//dlg_updateVersion.dialog("close");
	
	if (msg.result == 0) {
		alert("버전 수정이 완료되었습니다.");
		
		var packageName = msg.parameter.packageName;
		var version = msg.parameter.firmware_upversion;
		var checksum = msg.parameter.firmware_upcheckSum;
		var fileName = context.param.fileName;
		var fileSize = context.param.fileSize;
		var fileUrl = "{file_url}/"+packageName+"/"+fileName;
		var checked = $('input:checkbox[name="versionChecked"]:checked');
		
		console.log("UPDATE DATA : " + packageName + version + checksum + fileUrl + fileSize);
		
		checked.parents().children("#firmVersion").text(version);
		checked.parents().children("#firmVerSize").text(fileSize);
		checked.attr("curVersion", version);
		
		if (fileName != undefined && fileSize != undefined) {
			
			checked.parents().children("#firmVerUrl").text(fileUrl);
			checked.parents().children("#firmCheckSum").text(checksum);
		}
	}
}

//버전정보추가
function insertFirmwareVersion() {
	console.log("insertFirmwareVersion called");
	
	var firmwareId = $("#basicFirmwareId").text();
	var packageName = $("#firmwarePackageName").text();
	var version = $("#firmware_inverson").val();
	var checksum = $("#firmware_incheckSum").val();
	console.log("firmwareId : " + firmwareId + " packageName : " + packageName, "version : " + version + "checksum : " + checksum);
	
	if (!$("#inputFile").val()) {
		
		alert("파일을 첨부해주세요");
	} else {
		var context = {"param": {"firmwareId": firmwareId, "packageName": packageName, "version": version, "checksum": checksum}, "handler": firmwareVersionInsertHandler};
		
		hdb_insert_device_firmwareVersion(context, false);
	}
}

function firmwareVersionInsertHandler(msg, context) {
	console.log("firmwareVersionInsertHandler called");
	console.log("context:"+JSON.stringify(context));
	console.log("msg:"+JSON.stringify(msg));
	
	var id = $("#basicFirmwareId").text();
	
	$("#registerVersionModal").modal('hide');
	//dlg_registerVersion.dialog("close");
	
	if (msg.result == 0) {
		alert("버전이 추가되었습니다.");
	}
	
	window.location.href = contextPath + "/firmware/detail.do?id="+id;
}

//기본정보수정
function updateFirmwareBasic() {
	console.log("updateFirmwareBasic called");
	
	var id = $("#basicFirmwareId").text();
	var packageName = $("#firmware_package").val();
	var description = $("#firmware_description").val();
	console.log("id : " + id + " packageName : " + packageName + " description : " + description);
	
	var context = {"param":{"id": id, "package": packageName, "description": description}, "handler": firmwareBasicHandler};
	
	hdb_update_device_firmware(context, false);
}

function firmwareBasicHandler(msg, context) {
	console.log("firmwareBasicHandler called");
	console.log("context:"+JSON.stringify(context));
	console.log("msg:"+JSON.stringify(msg));
	
	//var id = $("#basicFirmwareId").text();
	
	$("#basicModal").modal('hide');
	//dlg_updateFirmBasic.dialog("close");
	
	if (msg.result == 0) {
		alert("업데이트 완료되었습니다.");
		
		var packageName = msg.parameter.package;
		var description = msg.parameter.description;
		console.log("DATA : " + packageName + " " + description);
		
		$("#firmwarePackageName").text(packageName);
		$("#firmwareDescription").text(description);
		
		//window.location.href = contextPath + "/firmware/detail.do?id="+id;
		return;
	}
}

//버전정보삭제
function deleteFirmwareVersion() {
	console.log("deleteFirmwareVersion called");
	
	$('input:checkbox[name="versionChecked"]:checked').each(function(index) {
		var firmwareId = $(this).attr("id").substring(3);
		var version = $(this).attr("curVersion");
		console.log("FIRMWAREID : " + firmwareId + " DELETE FIRMWARE VERSION : " + version);
		
		var context = {"param":{"firmwareId": firmwareId, "version": version}, "handler": firmwareVersionDeleteHandler};
		
		hdb_delete_device_firmwareVersion(context, false);
	});
	
	alert("버전이 삭제되었습니다.");
}	

function firmwareVersionDeleteHandler(msg, context) {
	console.log("firmwareVersionDeleteHandler called");
	console.log("context:"+JSON.stringify(context));
	console.log("msg:"+JSON.stringify(msg));
	
	//var firmwareId = $(".updateVersionCheckbox").attr("id").substring(3);
	
	if (msg.result == 0) {
		
		$('input:checkbox[name="versionChecked"]:checked').each(function() {
			$(this).parent().parent().remove();
		});
		//window.location.href = contextPath + "/firmware/detail.do?id="+firmwareId;
		return;
	}
	
}