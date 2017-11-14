
// - 페이지 로딩시 1회 수행
var dlg_regUser = null;
var frm_regUser = null;
var dlg_updateUser = null;
var frm_updateUser = null;
var userList = null;


function refreshUserList() {
	var context = { "param":{}, "handler": userListHandler, "manualRenderer": userListRenderer};
	hdb_get_user_list(context, false);
}

function userListRenderer(id, user, context) {
	var html = "<td>";
	html += "<button type='button' onclick='fnUpdate(\"" + user["id"] + "\"); return false;' class='btn btn-xs btn-success'> 수정</button>";
	html += "<button type='button' onclick='fnDelete(\"" + user["id"] + "\"); return false;' class='btn btn-xs btn-danger' style='margin-left:2px;'> 삭제</button>";
	html += "</td>";
	
	return html;
}

function userListHandler(msg, context) {
	console.log("userListHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));
	
	// 어드민 목록 표시
	var ths = $("#admin_table thead th");
	var body = $("#admin_table tbody");
	body.empty();
	
	// 목록 표시
	var count = 0;
	for (var i=0; i<msg.content.list.length; i++) {
		var data = msg.content.list[i];
		
		if (data["groupName"] != "ADMIN_SUPER" && data["groupName"] != "ADMIN_GENERAL") {
			continue;
		}
		
		var tr = $("<tr></tr>");
		for (var j=0; j<ths.length; j++) {
			if (typeof $(ths[j]).attr("id") == "undefined") {
				continue;
			} 
			var fn = $(ths[j]).attr("id").substring(7); 	// hd_<fieldName>
			if ($(ths[j]).hasClass("manual_renderer")) {
				tr.append(context.manualRenderer(fn, data, context));
			} else {
				var text = data[fn] == null ? "-" : data[fn];
				tr.append($("<td>"+text+"</td>"));				
			}
		}
		body.append(tr);
		count ++;
	}
	if (count == 0) {
		var tr = $("<tr></tr>");
		tr.append($("<td colspan='"+ths.length+"'>사용자 정보 없음</td>"));
		body.append(tr);
	}
	
	//
	// 매니저 목록 표시
	//	
	ths = $("#manager_table thead th");
	body = $("#manager_table tbody");
	body.empty();
	
	// 목록 표시
	count = 0;
	for (var i=0; i<msg.content.list.length; i++) {
		var data = msg.content.list[i];
		
		if (data["groupName"] == "ADMIN_SUPER" || data["groupName"] == "ADMIN_GENERAL") {
			continue;
		}
		
		var tr = $("<tr></tr>");
		for (var j=0; j<ths.length; j++) {
			if (typeof $(ths[j]).attr("id") == "undefined") {
				continue;
			} 
			var fn = $(ths[j]).attr("id").substring(7); 	// hd_<fieldName>
			if ($(ths[j]).hasClass("manual_renderer")) {
				tr.append(context.manualRenderer(fn, data, context));
			} else {
				var text = data[fn] == null ? "-" : data[fn];
				tr.append($("<td>"+text+"</td>"));				
			}
		}
		body.append(tr);
		count ++;
	}
	if (count == 0) {
		var tr = $("<tr></tr>");
		tr.append($("<td colspan='"+ths.length+"'>사용자 정보 없음</td>"));
		body.append(tr);
	}

	userList = msg.content.list;
}

function findUser(id) {
	for (index in userList) {
		var user = userList[index];
		if (user["id"] == id) {
			return user;
		}
	}
	return null;
}


function regUser() {
	
	console.log("regUser called!!!");
	
	var name = $("#name").val();
	var loginId = $("#loginId").val();
	var password = $("#password").val();
	var email = $("#email").val();
	var accountGroupId = $("#accountGroupId").val();
	var context = {"param":{
						"name":name, 
						"loginId":loginId, 
						"loginPwd":password,
						"email":email, 
						"mngAccountGroupId":accountGroupId},
					"handler": regUserHandler};

	hdb_add_user(context, false);
}

function removeUser(id) {
	
	console.log("removeUser called!!!");
	
	var context = {"param":{"id":id},
					"handler": removeUserHandler};

	hdb_delete_user(context, false);
}

function updateUser() {
	
	console.log("updateUser called!!!");

	var id = $("#u_id").val();
	var name = $("#u_name").val();
	var loginId = $("#u_loginId").val();
	var password = $("#u_password").val() == "*****" ? null : $("#u_password").val();
	var email = $("#u_email").val();
	var accountGroupId = $("#u_accountGroupId").val();
	
	var context = {"param":{
						"id":id,
						"name":name, 
						"mngAccountGroupId":accountGroupId,
						"loginId":loginId, 
						"loginPwd":password,
						"email":email},
					"handler": updateUserHandler};

	hdb_update_user(context, false);
	
}

function regUserHandler(msg, context) {
	console.log("regUserHandler called");
	console.log("context:" + JSON.stringify(context));
	console.log("msg:" + JSON.stringify(msg));
	dlg_regUser.dialog("close");
	
	refreshUserList();
	
}

function removeUserHandler(msg, context) {
	console.log("removeUserHandler called");
	console.log("context:" + JSON.stringify(context));
	console.log("msg:" + JSON.stringify(msg));
	
	refreshUserList();
	
}

function updateUserHandler(msg, context) {
	console.log("updateUserHandler called");
	console.log("context:" + JSON.stringify(context));
	console.log("msg:" + JSON.stringify(msg));
	dlg_updateUser.dialog("close");
	
	refreshUserList();
	
}
