
// - 페이지 로딩시 1회 수행
var dlg_regUser = null;
var frm_regUser = null;
var dlg_updateUser = null;
var frm_updateUser = null;
var userList = null;
function initUI() {
	console.log("initUI called");
	
	// UI Update
	refreshUserList();
		
	dlg_regUser = $( "#dialog-regUser" ).dialog({
	  autoOpen: false,
	  height: 350,
	  width: 350,
	  modal: true,
	  buttons: {
	    "등록": function() {
	    	dlg_regUser.find( "form" ).submit();
	    },
	    "취소": function() {
	    	dlg_regUser.dialog( "close" );
	    }
	  },
	  close: function() {
		  frm_regUser[ 0 ].reset();
		  //allFields.removeClass( "ui-state-error" );
	  }
	});
	
	$("#btn_registerUser").click(function() {
		dlg_regUser.dialog("open");
	});
	
	frm_regUser = dlg_regUser.find( "form" ).on( "submit", function( event ) {
	      //event.preventDefault();
	      
	    });

	$("#form_regUser").validate({
		rules : {
			loginId : {	required : true, minlength: 3, maxlength: 12 },
			name : { required : true, minlength: 3, maxlength: 12 },
			password : { required : true },
			password_confirm : { equalTo : "#password" },
			email : {required : true, email : true },
		},
		messages : {
			loginId : { 
				required : "이름을 입력하세요.",
				minlength : jQuery.validator.format("아이디는 {0}자 이상")
			},
			name : {
				required : "아이디를 입력하세요.",
				minlength : jQuery.validator.format("아이디는 {0}자 이상")
			},
			password : {
				required : "암호를 입력하세요."
			},
			password_confirm : {
				equalTo : "암호를 다시 확인하세요."
			},
			email : {
				required : "이메일주소를 입력하세요.",
				email : "올바른 이메일주소를 입력하세요."
			},
		},
		submitHandler : function(frm) {
			regUser();
		},
		success : function(e) {
			
		}
	});
	
	
	dlg_updateUser = $( "#dialog-updateUser" ).dialog({
	  autoOpen: false,
	  height: 350,
	  width: 350,
	  modal: true,
	  buttons: {
	    "저장": function() {
	    	dlg_updateUser.find( "form" ).submit();
	    },
	    "취소": function() {
	    	dlg_updateUser.dialog( "close" );
	    }
	  },
	  close: function() {
		  frm_updateUser[ 0 ].reset();
	  }
	});
	
	frm_updateUser = dlg_updateUser.find( "form" ).on( "submit", function( event ) {
	      //event.preventDefault();
	      
	    });

	$("#form_updateUser").validate({
		rules : {
			u_loginId : {	required : true, minlength: 3, maxlength: 12 },
			u_name : { required : true, minlength: 3, maxlength: 12 },
			u_password : { required : true },
			u_password_confirm : { equalTo : "#u_password" },
			u_email : {required : true, email : true },
		},
		messages : {
			u_loginId : { 
				required : "이름을 입력하세요.",
				minlength : jQuery.validator.format("아이디는 {0}자 이상")
			},
			u_name : {
				required : "아이디를 입력하세요.",
				minlength : jQuery.validator.format("아이디는 {0}자 이상")
			},
			u_password : {
				required : "암호를 입력하세요."
			},
			u_password_confirm : {
				equalTo : "암호를 다시 확인하세요."
			},
			u_email : {
				required : "이메일주소를 입력하세요.",
				email : "올바른 이메일주소를 입력하세요."
			},
		},
		submitHandler : function(frm) {
			updateUser();
		},
		success : function(e) {
			
		}
	});
}

function refreshUserList() {
	console.log("refreshUserList called");
	
	var context = { "param":{}, "handler": userListHandler, "manualRenderer": userListRenderer};
	
	hdb_get_user_list(context, false);
}


function userListRenderer(id, user, context) {
	console.log("userListRenderer called - id:"+id +":"+ user);
	
	return '<td><button id="ubtn_'+user["id"]+'" class="level_button update_button">수정</button>'+
				'<button id="ubtn_'+user["id"]+'" class="level_button delete_button">삭제</button></td>';	
}

function userListHandler(msg, context) {
	console.log("userListHandler called ");
	console.log("context: "+JSON.stringify(context));
	console.log("msg: "+JSON.stringify(msg));
	
	var ths = $("#admin_table thead th");
	var body = $("#admin_table tbody");
	body.empty();
	
	// 목록 표시
	for (var i=0; i<msg.content.list.length; i++) {
		var data = msg.content.list[i];
		
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
	}
	if (msg.content.list.length == 0) {
		var tr = $("<tr></tr>");
		tr.append($("<td colspan='"+ths.length+"'>사용자 정보 없음</td>"));
		body.append(tr);
	}

	//
	// 어드민 목록 표시
	//	
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

	$(".update_button").click(function(evt) {
		var id = parseInt($(evt.target).attr("id").substring(5));
		console.log("update_button clicked - ID:"+id);
		
		var user = findUser(id);

		$("#u_id").val(id);
		$("#u_name").val(user["name"]);
		$("#u_loginId").val(user["loginId"]);
		$("#u_email").val(user["email"]);
		$("#u_accountGroupId").val(user["accountGroupId"]);
		
		dlg_updateUser.dialog("open");		
	});
	$(".delete_button").click(function(evt) {
		var id = parseInt($(evt.target).attr("id").substring(5));
		console.log("delete_button clicked - ID:"+id);
		removeUser(id);
	});
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
