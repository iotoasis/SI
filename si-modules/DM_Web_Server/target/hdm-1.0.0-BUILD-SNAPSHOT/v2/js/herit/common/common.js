
/**
 * Translated default messages for the jQuery validation plugin.
 * Locale: KO
 * Filename: messages_ko.js
 */
/*jQuery.validator.addMethod("validationPassword", function(value, element){
	//정규식: 알파벳(대 소문자 구분 없이 모두 가능), 숫자, 특수문자를 반드시 하나 이상씩 가지고 있어야함
	var regExp = /((?=.*\d)(?=.*[a-zA-z])(?=.*[!@#$%^&*]))/gm;
	return this.optional(element) || regExp.test(value);
});*/

/**
 * Translated default messages for the jQuery validation plugin.
 * Locale: KO
 * Filename: messages_ko.js
 */
/*jQuery.extend(jQuery.validator.messages, {
	required: "반드시 입력해야 합니다.",
	remote: "수정 바랍니다.",
	email: "이메일 주소를 올바로 입력하세요.",
	url: "URL을 올바로 입력하세요.",
	date: "날짜가 잘못 입력됐습니다.",
	dateISO: "ISO 형식에 맞는 날짜로 입력하세요.",
	number: "숫자만 입력하세요.",
	digits: "숫자(digits)만 입력하세요.",
	creditcard: "올바른 신용카드 번호를 입력하세요.",
	equalTo: "값이 서로 다릅니다.",
	accept: "승낙해 주세요.",
	maxlength: jQuery.validator.format("{0}글자 이상은 입력할 수 없습니다."),
	minlength: jQuery.validator.format("적어도 {0}글자는 입력해야 합니다."),
	rangelength: jQuery.validator.format("{0}글자 이상 {1}글자 이하로 입력해 주세요."),
	range: jQuery.validator.format("{0}에서 {1} 사이의 값을 입력하세요."),
	max: jQuery.validator.format("{0} 이하로 입력해 주세요."),
	min: jQuery.validator.format("{0} 이상으로 입력해 주세요."),
	validationPassword: jQuery.validator.format("알파벳과 숫자, 특수문자를 조합하여 입력하세요")
});*/

/**
 * 라디오버튼의 체크된 값을 가져옴.
 * @param obj 라디오버튼 오브젝트
 * @return str
 */
function getRadioValue(obj){
	var objInfo = obj.value;
	var leng = obj.length;

	if(leng == undefined){
		leng = 1;
		return objInfo;
	}else{
		for(var i=0;i<leng;i++){
			if(obj[i].checked == true){
				return obj[i].value;
			}
		}
	}
	return "";
}
/**
 * 체크박스를 전체체크 혹은 전체해제
 * @param obj 이벤트 객체
 */
function checkAll(obj){
	if(obj.checked){
		allBoxCheck(true);
	}else{
		allBoxCheck(false);
	}
}
/**
 * 체크박스를 전체체크 혹은 전체해제
 * @param val (true, false)
 */
function allBoxCheck(val) {
	var allInput = document.getElementsByTagName("INPUT");
	for( var i=0 ; i<allInput.length ; i++ ) {
		var input = allInput[i];
		//체크박스가 disabled 된 것은 선택하지 않는다.
		if( input.type == "checkbox" && input.disabled == false) {
			input.checked = val;
		}
   }
}

/**
 * 전체 체크박스를 체크혹은 체크해제
 * @param obj form객체
 * @param targetName 체크할 객체의 이름
 */
function checkOneMoreCheckbox(obj, targetName) {
    var bCheck = false;
    fObj = obj;
    for (i = 0 ; i < fObj.length; i++) {
        if (fObj[i].type == "checkbox" && fObj[i].name == targetName) {
            if (fObj[i].checked) {
                bCheck = true;
                break;
            }
        }
    }
    if (!bCheck) {
        alert("체크박스를 선택해 주세요.");
    }
    return bCheck
}


/**
 * 윈도우를 팝업시킨다
 * @param url 화면 URL
 * @param target 오픈할폼이름
 * @param width 오픈윈도우가로길이
 * @param height 오픈윈도우세로길이
 */
function openWindow(url, target, width, height) {
		var win;

		var iLeft = (window.screen.width / 2) - (Number(width) / 2);
        var iTop = (window.screen.height / 2) - (Number(height) / 2);
		var features = "menubar=no,toolbar=no,status=no,resizable=yes,scrollbars=yes";
		features += ",left=";
	    features += iLeft;
	    features += ",top=";
	    features += iTop;
	    features += ",width=";
	    features += width;
	    features += ",height=";
	    features += height;
		win = window.open(url, target, features);
		win.focus();
}

/**
 * 윈도우를 팝업시킨다(고정윈도우)
 * @param url 화면 URL
 * @param target 오픈할폼이름
 * @param width 오픈윈도우가로길이
 * @param height 오픈윈도우세로길이
 */
function openWindowStatic(url, target, width, height) {
		var win;

		var iLeft = (window.screen.width / 2) - (Number(width) / 2);
        var iTop = (window.screen.height / 2) - (Number(height) / 2);
		var features = "menubar=no,toolbar=no,status=no,resizable=no,scrollbars=no";
		features += ",left=";
	    features += iLeft;
	    features += ",top=";
	    features += iTop;
	    features += ",width=";
	    features += width;
	    features += ",height=";
	    features += height;
		win = window.open(url, target, features);
		win.focus();
}

function movePage1(cPage, moveForm, actionUrl){
    moveForm.currPage.value = cPage;
    moveForm.action = actionUrl;
    moveForm.submit();
}
function movePage2(cPage, moveForm, actionUrl, target){
    moveForm.currPage.value = cPage;
    moveForm.target = target;
    moveForm.action = actionUrl;
    moveForm.submit();
}

