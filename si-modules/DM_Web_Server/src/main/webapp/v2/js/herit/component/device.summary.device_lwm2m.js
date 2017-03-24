// Custom scripts
$(document).ready(function () {
	dsd_lwm2m.init();
	
	$('#dsd_lwm2m_fw_update_form').on('show.bs.modal', function (e) {

		dsd_lwm2m.updateFUDialog();
		//if (!data) return e.preventDefault() // stops modal from being shown
	});
	
	console.log("device.summary.device js initialized");
});

var dsd_lwm2m = {
	deviceInfo: null,
	package: null,
	init: function() {
		console.log("dsd_lwm2m.refresh called ");
		
		dsd_lwm2m.deviceInfo = _ucc.getDeviceInfo();
		
		$("#dsd_lwm2m_regdate").text(dsd_lwm2m.deviceInfo.createTime);
		$("#dsd_lwm2m_sn").text(dsd_lwm2m.deviceInfo.sn);
		
		var mo = _ucc.getMoData("/3/-/2");
		if (mo != null && typeof mo != "undefined") {
			
			dsd_lwm2m.package = mo.data;
			$("#dsd_lwm2m_fw_package").text(mo.data); // /4/0/1
			
		}
		
		mo = _ucc.getMoData("/3/-/3");
		if (mo != null && typeof mo != "undefined") {
			$("#dsd_lwm2m_fw_version").text(mo.data); // /4/0/0
			$("#dsd_lwm2m_fw_update_date").text(mo.updateTime);
			
			$("#dsd_lwm2m_fw_submit_btn").on("click", function() {
				dsd_lwm2m.executeFirmwareUpgrade(dsd_lwm2m.deviceInfo.deviceId, dsd_lwm2m.package, $("#dsd_lwm2m_fw_version_option").val());
			});
			

			$("#dsd_lwm2m_fw_version_option").on("change", function() {
				var mo = _ucc.getMoData("/3/-/3");
				var curVer = mo.data;
				if ($("#dsd_lwm2m_fw_version_option").val() != curVer) {
					$('#dsd_lwm2m_fw_submit_btn').removeClass("disabled");
				} else {
					$('#dsd_lwm2m_fw_submit_btn').addClass("disabled");					
				}
			});
			
		}
	},
	
	firmwareVersionList: null,
	updateFUDialog: function() {
		
		if (dsd_lwm2m.firmwareVersionList == null) {
			// 디바이스 등록수 요청
			var modelInfo = _ucc.getDeviceModelInfo();
			var res = _ucc.getMoData("/3/-/2");
			var context = {"param": {"package":res.data, "oui":modelInfo.oui, "modelName":modelInfo.modelName}, "handler": dsd_lwm2m.fwVersionListHandler };
			hdb_get_firmware_version_list(context, false);
		}
	},
	
	fwVersionListHandler: function(msg, context) {
		console.log("dsd_lwm2m.fwVersionListHandler called ");
		
		dsd_lwm2m.firmwareVersionList = msg.content.list;
		$("#dsd_lwm2m_fw_version_option").empty();
		for (var i=0; i<dsd_lwm2m.firmwareVersionList.length; i++) {
			var ver = dsd_lwm2m.firmwareVersionList[i];
			$("#dsd_lwm2m_fw_version_option").append("<option value='"+ver.version+"'>"+ver.version+"</option>");
		}
		var mo = _ucc.getMoData("/3/-/3");
		$("#dsd_lwm2m_fw_version_option").val(mo.data);
	},
	

	executeFirmwareUpgrade: function(deviceId, packageName, ver) {
		console.log("dsd_lwm2m.executeFirmwareUpgrade called - deviceId:"+deviceId +", packageName:"+packageName+", version:"+ ver);
		
		var context = {"deviceId":deviceId, "packageName":packageName, 
						"version": ver, "handler": dsd_lwm2m.executeUpgradeHandler};
		
		dm_firmware_upgrade(context, false);
	},
	
	executeUpgradeHandler: function(msg, context) {
		console.log("dsd_lwm2m.executeUpgradeHandler called ");
		console.log(JSON.stringify(msg));
		if (msg.content.r == "200") {
			alert("펌웨어 업그레이드 요청이 성공했습니다.");
		} else {
			alert("펌웨어 업그레이드 요청이 실패했습니다.");
		}
		$('#dsd_lwm2m_fw_update_form').modal('hide');
	}
	
};
