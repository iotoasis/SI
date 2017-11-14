// Custom scripts
$(document).ready(function () {
	dsd.init();
	
	$('#dsd_fw_update_form').on('show.bs.modal', function (e) {

		dsd.updateFUDialog();
		//if (!data) return e.preventDefault() // stops modal from being shown
	});
	
	console.log("device.summary.device js initialized");
});

var dsd = {
	deviceInfo: null,
	package: null,
	init: function() {
		console.log("dsd.refresh called ");
		
		dsd.deviceInfo = _ucc.getDeviceInfo();
		
		$("#dsd_regdate").text(dsd.deviceInfo.createTime);
		$("#dsd_sn").text(dsd.deviceInfo.sn);
		
		var mo = _ucc.getMoData("/4/0/1");
		if (mo != null && typeof mo != "undefined") {
			
			dsd.package = mo.data;
			$("#dsd_fw_package").text(mo.data); // /4/0/1
			
		}
		
		mo = _ucc.getMoData("/4/0/0");
		if (mo != null && typeof mo != "undefined") {
			$("#dsd_fw_version").text(mo.data); // /4/0/0
			$("#dsd_fw_update_date").text(mo.updateTime);
			
			$("#dsd_fw_submit_btn").on("click", function() {
				dsd.executeFirmwareUpgrade(dsd.deviceInfo.deviceId, dsd.package, $("#dsd_fw_version_option").val());
			});
			

			$("#dsd_fw_version_option").on("change", function() {
				var mo = _ucc.getMoData("/4/0/0");
				var curVer = mo.data;
				if ($("#dsd_fw_version_option").val() != curVer) {
					$('#dsd_fw_submit_btn').removeClass("disabled");
				} else {
					$('#dsd_fw_submit_btn').addClass("disabled");					
				}
			});
			
		}
	},
	
	firmwareVersionList: null,
	updateFUDialog: function() {
		
		if (dsd.firmwareVersionList == null) {
			// 디바이스 등록수 요청
			var modelInfo = _ucc.getDeviceModelInfo();
			var res = _ucc.getMoData("/4/0/1");
			var context = {"param": {"package":res.data, "oui":modelInfo.oui, "modelName":modelInfo.modelName}, "handler": dsd.fwVersionListHandler };
			hdb_get_firmware_version_list(context, false);
		}
	},
	
	fwVersionListHandler: function(msg, context) {
		console.log("dsd.fwVersionListHandler called ");
		
		dsd.firmwareVersionList = msg.content.list;
		$("#dsd_fw_version_option").empty();
		for (var i=0; i<dsd.firmwareVersionList.length; i++) {
			var ver = dsd.firmwareVersionList[i];
			$("#dsd_fw_version_option").append("<option value='"+ver.version+"'>"+ver.version+"</option>");
		}
		var mo = _ucc.getMoData("/4/0/0");
		$("#dsd_fw_version_option").val(mo.data);
	},
	

	executeFirmwareUpgrade: function(deviceId, packageName, ver) {
		console.log("dsd.executeFirmwareUpgrade called - deviceId:"+deviceId +", packageName:"+packageName+", version:"+ ver);
		
		var context = {"deviceId":deviceId, "packageName":packageName, 
						"version": ver, "handler": dsd.executeUpgradeHandler};
		
		dm_firmware_upgrade(context, false);
	},
	
	executeUpgradeHandler: function(msg, context) {
		console.log("dsd.executeUpgradeHandler called ");
		console.log(JSON.stringify(msg));
		if (msg.content.r == "200") {
			alert("펌웨어 업그레이드 요청이 성공했습니다.");
		} else {
			alert("펌웨어 업그레이드 요청이 실패했습니다.");
		}
		$('#dsd_fw_update_form').modal('hide');
	}
	
};
