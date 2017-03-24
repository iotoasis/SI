var Validation = {
	validator : function (frmName){
		var chkRtMsg = true;
		$("#"+frmName).children().find("input,select,textarea").each(function(){
			if($(this).attr("validateMsg") != "" && $(this).attr("validateMsg") != undefined  ){
				if($(this).val() == ""){
					alert("["+$(this).attr("validateMsg")+"] 항목이 입력되지 않았습니다");
					$(this).focus(); 
					chkRtMsg =  false;
					return false;
				}		
			}
		}); 
		
		return chkRtMsg;
		 
	}
};

function addbookmark(url,title) {
	var browser=navigator.userAgent.toLowerCase();

	// Mozilla, Firefox, Netscape
	if (window.sidebar) {
		window.sidebar.addPanel(title, url,"");
	}else if( window.external) {	// IE or chrome
		// IE
		if (browser.indexOf('chrome')==-1){
			window.external.AddFavorite( url, title);
		} else {// chrome
		
			alert('CTRL+D 또는 Command+D를 눌러 즐겨찾기에 추가해주세요.');
		}
	}else if(window.opera && window.print) { // Opera - automatically adds to sidebar if rel=sidebar in the tag
		return true;
	}else if (browser.indexOf('konqueror')!=-1) {// Konqueror
		alert('CTRL+B를 눌러 즐겨찾기에 추가해주세요.');
	}else if (browser.indexOf('webkit')!=-1){ // safari
		alert('CTRL+B 또는 Command+B를 눌러 즐겨찾기에 추가해주세요.');
	} else {
		alert('사용하고 계시는 브라우저에서는 이 버튼으로 즐겨찾기를 추가할 수 없습니다. 수동으로 링크를 추가해주세요.')
	}
}

function pw_check(password){
	 var alpha="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	 var number ="1234567890";
	 var sChar = "-_=+\|()*&^%$#@!~`?></;,.:'";
	 var total = alpha + number + sChar;
	 var patt_4num1 = /(\w)\1\1\1/; // 같은 영문자&숫자 연속 4번 정규식
	 var patt_4num2 = /([\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"])\1\1\1/; // 같은 특수문자 연속 4번 정규식
	 var patt_4num3 = /([가-힣ㄱ-ㅎㅏ-ㅣ\x20])\1\1\1/; // 같은 한글 연속 4번 정규식
	 var patt_cont = /(0123)|(1234)|(2345)|(3456)|(4567)|(5678)|(6789)|(7890)/; // 연속된 숫자 정규식
	 
	 if(patt_4num1.test(password) ||  patt_4num2.test(password) || patt_4num3.test(password)){
		 alert("비밀번호는 4자이상 동일한 문자,숫자, 특수문자는 사용할수 없습니다.");
		 return false;
	 }else if(patt_cont.test(password)){
		 alert("비밀번호는 연속된 숫자는 사용할수 없습니다.");
		 return false;
	 }
	 
	 var result_alpha = 0;
	 var result_number = 0;
	 var result_sChar = 0;
	 var result_hangle = 0;
	 var result = 0;
	 var msg = "비밀번호는 최소 8자리 이상 영문, 숫자, 특수문자 조합으로 해주십시오.";

	 var pw = password;
	 if ( (pw.length < 8 ) || (pw.length > 20 ) ){
		 alert(msg);
		 return false;
	 }

	 for (var i=0; i<pw.length; i++ ){
		 if (total.indexOf(pw.charAt(i)) == -1){
			 result_hangle = 1;  
		 }
		 if (alpha.indexOf(pw.charAt(i)) != -1){
			 result_alpha=1;
		 }
		 if (number.indexOf(pw.charAt(i)) != -1){
			 result_number = 1;
		 }
		 if (sChar.indexOf(pw.charAt(i)) != -1){  
			 result_sChar=1;
		 }
	 }
	 
	 if (result_hangle==1){
		 alert(msg);
		 return false;
	 }
	 result = result_alpha + result_number + result_sChar;
	 
	 if (result != 3){
		 alert(msg);
		 return false;
	 }
	 
	 return true;
}



function checkDetailUrl(strUrl) {   
	var expUrl = /^([a-z]+):\/\/((?:[a-z가-힣\d\-]{2,}\.)+[a-z]{2,})(:\d{1,5})?(\/[^\?]*)?(\?.+)?$/i;
	return expUrl.test(strUrl);
}

function chkNum(obj){
	var  id = $("#"+obj).val();
	if(id.match(/^\d+$/ig) == null){
		alert("숫자만 입력 가능합니다."); 
		$("#"+obj).val("");
		return;
	}
}