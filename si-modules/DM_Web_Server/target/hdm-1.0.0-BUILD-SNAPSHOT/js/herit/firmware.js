
function btnSearchfirmware() {
	console.log("btnSearchfirmware called");
}


/*function refreshFirmwareTable(type, oui, modelName, sn, page) {
	console.log("refreshFirmwareTable called - type:"+type+", oui:"+oui+", modelName:"+modelName+", sn:"+sn +", page:"+page);
	
	var context = { "param": { "oui":oui, "modelName":modelName, "sn":sn, "page":page, "limit":30 }, "handler":getFrimwareHandler}
	hdb_get_firmware(type, context, false);
}*/

function refreshFirmwareTableContext(type, context) {
	console.log("refreshFirmwareTableContext called - type:"+type+", context:"+context);
	
	if (typeof context.handler == "undefined" || context.handler == null) {
		context.handler = getFrimwareHandler;
	}
	hdb_get_firmware(type, context, false);
}


function getFrimwareHandler(msg, context) {
	console.log("getFrimwareHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));

	var ths = $("#firmware_table thead th");
	var firmBody = $("#firmware_table tbody");
	firmBody.empty();
	
	// 목록 표시
	for (var i = 0; i < msg.content.list.length; i++) {
		var firm = msg.content.list[i];
		
		var tr = $("<tr></tr>");
		for (var j = 0; j < ths.length; j++) {
			if (typeof $(ths[j]).attr("id") == "undefined") {
				continue;
			} 
			var fn = $(ths[j]).attr("id").substring(3); 	// hd_<fieldName>
			if ($(ths[j]).hasClass("firmware_manual_renderer")) {
				tr.append(context.manualRenderer(fn, firm, context));
			} else {
				if (firm[fn] == null) {
					tr.append($("<td>-</td>"));						
				} else {
					tr.append($("<td>"+firm[fn]+"</td>"));						
				}			
			}
		}
		firmBody.append(tr);
	}
	if (msg.content.list.length == 0) {
		var tr = $("<tr></tr>");
		tr.append($("<td colspan='"+ths.length+"'>이력 없음</td>"));
		firmBody.append(tr);
	}

	// 페이징 계산
	var curPage = parseInt(msg.parameter.page);
	var pageSize = parseInt(msg.parameter.pageSize);
	var listCount = parseInt(msg.content.count);
	var pageCount = parseInt((listCount + pageSize - 1) / pageSize);
	var startPage = (curPage - 5) < 1 ? 1 : (curPage - 5);
	var endPage = (startPage + 9) > pageCount ? pageCount : (startPage + 9);

	// 페이징 표시
	$(".paging_wrap").empty();
	// 이전/시작 페이지 링크 추가
	if (curPage > 1) {
		$(".paging_wrap").append($('<a href="#" onclick="movePage(1);"><img src="'+contextPath+'/images/hitdm/common/start_bt.gif"/></a>'));
		$(".paging_wrap").append($('<a href="#" onclick="movePage('+(curPage-1)+');"><img src="'+contextPath+'/images/hitdm/common/prev_bt.gif"/></a>'));
	} else {
		$(".paging_wrap").append($('<img src="'+contextPath+'/images/hitdm/common/start_bt_disabled.gif"/>'));
		$(".paging_wrap").append($('<img src="'+contextPath+'/images/hitdm/common/prev_bt_disabled.gif"/>'));		
	}
	// 페이지 링크 추가
	for (i=startPage; i<=endPage; i++ ) {
		if (i == curPage) {
			$(".paging_wrap").append($('<span class="active"><a href="#">'+i+'</a></span>'));
		} else {
			$(".paging_wrap").append($('<span><a href="#" onclick="movePage('+i+');">'+i+'</a></span>'));			
		}
	}
	// 다음/끝 페이지 링크 추가
	if (curPage < endPage) {
		$(".paging_wrap").append($('<a href="#" onclick="movePage('+(curPage+1)+');"><img src="'+contextPath+'/images/hitdm/common/next_bt.gif"/></a>'));
		$(".paging_wrap").append($('<a href="#" onclick="movePage('+pageCount+');"><img src="'+contextPath+'/images/hitdm/common/end_bt.gif"/></a>'));
	} else {
		$(".paging_wrap").append($('<img src="'+contextPath+'/images/hitdm/common/next_bt_disabled.gif"/>'));
		$(".paging_wrap").append($('<img src="'+contextPath+'/images/hitdm/common/end_bt_disabled.gif"/>'));
	}
}