// Custom scripts
$(document).ready(function () {
	dsd_tr069.init();
	
	/*
	$('#dsd_tr069_fw_update_form').on('show.bs.modal', function (e) {

		dsd_tr069.updateFUDialog();
		//if (!data) return e.preventDefault() // stops modal from being shown
	});
	*/
	
	console.log("device.summary.device js initialized");
});

var dsd_tr069 = {
	deviceInfo: null,
	package: null,
	init: function() {
		console.log("dsd_tr069.refresh called ");
		
		dsd_tr069.deviceInfo = _ucc.getDeviceInfo();
		
		$("#dsd_tr069_regdate").text(dsd_tr069.deviceInfo.createTime);
		$("#dsd_tr069_sn").text(dsd_tr069.deviceInfo.sn);
		
		//var mo = _ucc.getMoData("/4/0/1");
		var mo = _ucc.getMoData("");
		if (mo != null && typeof mo != "undefined") {
			
			dsd_tr069.package = mo.data;
			$("#dsd_tr069_fw_package").text(mo.data); // /4/0/1
			
		}
		
		//mo = _ucc.getMoData("/4/0/0");
		mo = _ucc.getMoData("Device/DeviceInfo/SoftwareVersion");
		if (mo != null && typeof mo != "undefined") {
			$("#dsd_tr069_fw_version").text(mo.data); // /4/0/0
			$("#dsd_tr069_fw_update_date").text(mo.updateTime);
			
			$("#dsd_tr069_fw_submit_btn").on("click", function() {
				//dsd_tr069.executeFirmwareUpgrade(dsd_tr069.deviceInfo.deviceId, dsd_tr069.package, $("#dsd_tr069_fw_version_option").val());
			});
			

			$("#dsd_tr069_fw_version_option").on("change", function() {
				var mo = _ucc.getMoData("Device/DeviceInfo/SoftwareVersion");
				var curVer = mo.data;
				if ($("#dsd_tr069_fw_version_option").val() != curVer) {
					$('#dsd_tr069_fw_submit_btn').removeClass("disabled");
				} else {
					$('#dsd_tr069_fw_submit_btn').addClass("disabled");					
				}
			});
			
		}
	},
	
	firmwareVersionList: null,
	updateFUDialog: function() {
		
		if (dsd_tr069.firmwareVersionList == null) {
			// 디바이스 등록수 요청
			var modelInfo = _ucc.getDeviceModelInfo();
			var res = _ucc.getMoData("Device/DeviceInfo/SerialNumber");
			var context = {"param": {"package":res.data, "oui":modelInfo.oui, "modelName":modelInfo.modelName}, "handler": dsd_tr069.fwVersionListHandler };
			hdb_get_firmware_version_list(context, false);
		}
	},
	
	fwVersionListHandler: function(msg, context) {
		console.log("dsd_tr069.fwVersionListHandler called ");
		
		dsd_tr069.firmwareVersionList = msg.content.list;
		$("#dsd_tr069_fw_version_option").empty();
		for (var i=0; i<dsd_tr069.firmwareVersionList.length; i++) {
			var ver = dsd_tr069.firmwareVersionList[i];
			$("#dsd_tr069_fw_version_option").append("<option value='"+ver.version+"'>"+ver.version+"</option>");
		}
		//var mo = _ucc.getMoData("/4/0/0");
		var mo = _ucc.getMoData("Device/DeviceInfo/SoftwareVersion");
		$("#dsd_tr069_fw_version_option").val(mo.data);
	},
	

	executeFirmwareUpgrade: function(deviceId, packageName, ver) {
		console.log("dsd_tr069.executeFirmwareUpgrade called - deviceId:"+deviceId +", packageName:"+packageName+", version:"+ ver);
		
		var context = {"deviceId":deviceId, "packageName":packageName, 
						"version": ver, "handler": dsd_tr069.executeUpgradeHandler};
		
		dm_firmware_upgrade(context, false);
	},
	
	executeUpgradeHandler: function(msg, context) {
		console.log("dsd_tr069.executeUpgradeHandler called ");
		console.log(JSON.stringify(msg));
		if (msg.content.r == "200") {
			alert("펌웨어 업그레이드 요청이 성공했습니다.");
		} else {
			alert("펌웨어 업그레이드 요청이 실패했습니다.");
		}
		$('#dsd_tr069_fw_update_form').modal('hide');
	}
	
};
