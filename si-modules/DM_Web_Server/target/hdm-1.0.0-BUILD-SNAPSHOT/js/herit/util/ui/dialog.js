/**
 * 
 */
$(document).ready(function(){
	createMessageDialog();
});

function createMessageDialog(){
	appenDialogDivToBody();
	
	/* JQuery Dialog 정의 */
	//메시지 Dialog 정의
	$("#messageDialog").dialog({
		autoOpen: false,
		width: 450,
		height: 200,
		modal: true,
		title: "결과",
		buttons: {
			"확인": function(){
				$(this).dialog("close");
			}
		},
		close: function(){
			$("#messageDialogMessage").html("");
		}
	});
	
	/* JQuery Dialog 정의 */
	//메시지 Dialog 정의
	$("#confirmDialog").dialog({
		autoOpen: false,
		width: 450,
		height: 200,
		modal: true,
		title: "결과",
		buttons: {
			"확인": function(){
				$(this).dialog("close");
				
				//window.opener.closeWindow();
				window.close();
			},
			"취소": function(){
				$(this).dialog("close");
			}
		},
		close: function(){
			$("#messageDialogMessage").html("");
		}
	});
}

function appenDialogDivToBody(){
	var currentBodyHTML = $("body").html();
	currentBodyHTML += 
		"<div id='messageDialog' class='ui-widget'>" + 
			"<p>" + 
			    "<span id='messageDialogIcon' class='ui-icon ui-icon-circle-check' style='float:left; margin:0 7px 50px 0;'></span>" + 
			    "<span id='messageDialogMessage'></span>" + 
			"</p>" + 
		"</div>" + 
		"<div id='confirmDialog' class='ui-widget'>" + 
			"<p>" + 
			    "<span id='confirmDialogIcon' class='ui-icon ui-icon-alert' style='float:left; margin:0 7px 50px 0;'></span>" + 
			    "<span id='confirmDialogMessage'></span>" + 
			"</p>" + 
		"</div>";
	$("body").html(currentBodyHTML);
}
