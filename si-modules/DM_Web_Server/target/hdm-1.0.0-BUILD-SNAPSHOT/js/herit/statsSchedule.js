
// - 페이지 로딩시 1회 수행
function initSchedule() {
	console.log("initUI called");

	/*$("#btnSearch").click(function() {
		search();
	});*/
	
	/*$("#selSearchType").change(function() {
		updateDatePicker();
	});*/

    $("#dpStart").datepicker({dateFormat: "yy-mm-dd"});
	updateDatePicker();
}

function updateDatePicker() {
	
	var startMonth = new Date();

    $("#dpStart").datepicker("option", "dateFormat", "yy-mm-dd");
    $("#dpStart").datepicker("setDate", startMonth);
}

/*function search() {
	var start = $("#dpStart").val();
	var end = $("#dpEnd").val();
	console.log("search called - start:"+start +", end:"+ end);

	var searchType = $("#selSearchType").val();
	if (searchType == "day") {
		var context = {"param":{"start":start, "end":end}, 
						"handler": searchResultHandler };
		hdb_get_stat_register_day(context, false);
	} else if (searchType == "month") {
		var context = {"param":{"start":start+"-1", "end":end+"-1"}, 
						"handler": searchResultHandler };
		hdb_get_stat_register_month(context, false);
	}
}

function searchResultHandler(msg, context) {
	console.log("searchResultHandler called");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));

	var ths = $("#stat_table thead th");
	var histBody = $("#stat_table tbody");
	
	histBody.empty();
	
	// 목록 표시
	var period = $("#selSearchType").val() == "day" ? "statDate" : "statMonth";
	var total = 0;
	for (var i=0; i<msg.content.list.length; i++) {
		var hist = msg.content.list[i];
		total += hist['statCount'];
		var tr = $("<tr></tr>");
		tr.append($("<td>"+hist[period]+"</td><td>"+hist["statCount"]+"</td>"));
		histBody.append(tr);
	}
	if (msg.content.list.length == 0) {
		var tr = $("<tr></tr>");
		tr.append($("<td colspan='"+ths.length+"'>통계 없음</td>"));
		histBody.append(tr);
	}
	
	// total
	var tr = $("<tr></tr>");
	tr.append($("<td class='total'>TOTAL</td><td class='total'>"+total+"</td>"));	
	histBody.append(tr);

}*/