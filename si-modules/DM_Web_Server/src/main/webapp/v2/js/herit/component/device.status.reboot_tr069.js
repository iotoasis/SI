// Custom scripts
$(document).ready(function () {
	
	dsr_tr069.init();

	console.log("device.status.reboot js initialized");
});

function downloadFinish(){
	console.log("downloadFinish() has been called.");
	$('button').prop('disabled',false);
	dsr_tr069.isProcessing = false;
}

var dsr_tr069 = {
	isProcessing: false,
	divs: null,
	resources: null,
	init: function() {
		dsr_tr069.divs = $(".hit-component-dsr_tr069");
		dsr_tr069.resources = [];

		$.each(dsr_tr069.divs, function(idx, div) {
			//dsr_tr069.resources.push(_ucc.getResListWithComponentDiv(div));
			dsr_tr069.resources = _ucc.getResListWithComponentDiv(div);
			
			$(div).find(".btn_reboot").on("click", dsr_tr069.onRebootClick);
			$(div).find(".btn_firmware_update").on("click", dsr_tr069.onFirmwareListClick);
		});
		
		
		_ucc.setResourceHandler("dsr_tr069", dsr_tr069.resources, dsr_tr069.refresh);
		dsr_tr069.refresh();
	}, 
	refresh: function(){
		
		var context = {
			"sn":_ucc.getDeviceInfo().sn,
			"deviceModel":_ucc.getDeviceModelInfo().id,
			"oui":_ucc.getDeviceInfo().oui,
			"modelName":_ucc.getDeviceInfo().modelName
		};
		console.log(context);
		
		ajaxStart();
		$.ajax({
			type : 'POST',
			dataType : 'json',
			contentType: "application/json",
			url : '/hdm/api/firmware/list.do',
			//data :  {"sn":context.sn, "deviceModel": context.deviceModel, "oui": context.oui, "modelName": context.modelName},
			data :  JSON.stringify({"sn":context.sn, "deviceModel": context.deviceModel, "oui": context.oui, "modelName": context.modelName}),
			//data : context,
			//context : context,
			success : function(data){
				console.log(data);
				
				var currList = null;
				try{
					currList = data.pagingUtil.currList;
				} catch(ex){}
				
				var tbody = $('#myForm table tbody');
				tbody.find("tr").remove();
				var form = "";
				if( currList != null ){
					for(var i=0; i<currList.length; i++){
						form += "<tr onClick='dsr_tr069.onFirmwareClick($(this));'>";
						form += 	"<td>"+currList[i].manufacturer+"</td>";
						form += 	"<td>"+currList[i].packageName+"</td>";
						form += 	"<td>"+currList[i].latestVersion+"</td>";
						form += 	"<td>"+currList[i].createTime+"</td>";
						form += 	"<td>"+currList[i].updateTime+"</td>";
						form += "</tr>";
					}
					tbody.append(form);
				} else {
					form += "<tr>";
					form += 	"<td colspan='5'>조회된 데이터가 없습니다.</td>";
					form += "</tr>";
					tbody.append(form);
				}
			},
			error : function(xhr, status, error){
				//alert(error);
			}
		});
	},
	onRebootClick: function(evt) {
		console.log("dsr_tr069.onRebootClick(evt)");
		
		bootbox.confirm("디바이스를 리부팅합니다. 계속하시겠습니까?", function(result) {
			  	
				if (result == true) {
					var context = {"deviceId":_ucc.getDeviceInfo().deviceId, "handler": dsr_tr069.executeResultHandler};		
					dm_reboot_tr069(context, false);
				}
		}); 
	},
	onFirmwareListClick: function(evt) {
		console.log("dsr_tr069.onFirmwareUpdateClick(evt)");
		
		$('div.modal').modal({
			backdrop:'static'
		});
	},
	onFirmwareClick: function(target){
		console.log(dsr_tr069.isProcessing);
		if(!dsr_tr069.isProcessing){
			bootbox.confirm(target.find('td:eq(1)').text()+" 펌웨어 업데이트를 진행합니다. 계속하시겠습니까?", function(result) {
			  	
				if (result == true) {
					var context = {
						"deviceId":_ucc.getDeviceInfo().deviceId,
						"deviceModelId":_ucc.getDeviceModelInfo().id,
						"fileName":target.find('td:eq(1)').text(),
						"version":target.find('td:eq(2)').text(),
						"handler": dsr_tr069.executeResultHandler
					};
					dsr_tr069.isProcessing = true;
					$('button').prop('disabled',true);
					//console.log(context);
					dm_firmware_download_tr069(context, false);
				}
			}); 
		}
		
	},
	onFirmwareDownloadClick: function(target){
		alert(target[0]);
	},
	executeResultHandler: function(msg, context) {
		console.log("dsr_tr069.executeResultHandler called ");
		//console.log("msg: "+JSON.stringify(msg));
		
	}
}




