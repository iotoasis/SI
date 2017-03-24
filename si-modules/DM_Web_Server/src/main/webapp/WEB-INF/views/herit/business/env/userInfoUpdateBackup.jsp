<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %> 
<%@ page session="true" %>
<html>
<head>
	<title>사용자관리</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	
	

		/* ********************************************************
		 * 상세 처리
		 **********************************************************/
		function fnDetail(){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "V";
			myForm.action           = "<c:url value='/env/userInfo/info.do'/>";
		   	myForm.submit();
		}		
		/* ********************************************************
		 * 수정 처리
		 **********************************************************/
		function fnUpdate(){
			if (!$("#myForm").validate({
				//rules: validationRules
			}).form()) {return;}			
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/env/userInfo/update.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 비밀번호 변경 체크 처리
		 **********************************************************/
		function changeCheck(){
			var myForm				 = document.getElementById("myForm");
			myForm.changeYn.value = "Y";
		}
		function changeYn2(){
			var myForm				 = document.getElementById("myForm");
			if(myForm.changeYn.value != "Y"){
				$("#myForm").find("[name=loginPwd]").val("");
				$("#myForm").find("[name=confirmLoginPwd]").val("");
			}
		}		
		/* ********************************************************
		 * 최초 실행
		 ******************************************************** */
		$(function() {
			
			//대메뉴 활성화 
			$("#01").removeClass("gnb_active");
			$("#02").removeClass("gnb_active");
			$("#03").removeClass("gnb_active");
			$("#04").removeClass("gnb_active");
			//서브메뉴 활성화
			$("#05_01").addClass("snb_active");
			
			jQuery.validator.addMethod("checkPassword", function(value, element) {
				return this.optional(element) || /((?=.*\d)(?=.*[a-zA-z])(?=.*[!@#$%^&*]))/gm.test(value);
			}); 			
			jQuery.validator.addMethod("ipArrayList", function(value, element) {
				return this.optional(element) || /^[0-9.]+$/.test(value);
			}); 
			// validate signup form on keyup and submit
			$("#myForm").validate({
				rules : {
					mngAccountGroupId : "required",
					name : {
						required : true,
						maxlength : 15
					},
					loginPwd : {
						required : true,
						minlength : 8,
						maxlength : 20,
						checkPassword : true
					},
					confirmLoginPwd : {
						required : true,
						minlength : 8,
						maxlength : 20,
						equalTo : "#loginPwd"
					},
					email : {
						required : true,
						email : true
					},
					phone : {
						digits : true,
						maxlength : 20
					},
					mobile : {
						digits : true,
						maxlength : 20
					},
					"ipArrayList" : {
						maxlength : 15,
						ipArrayList : true
					}
				},
				messages : {
					mngAccountGroupId : "그룹명을 입력해 주세요",
					name : {
						required : "이름을 입력해 주세요",
						maxlength : "이름은 {0}자 이하이어야 합니다."
					},
					loginPwd : {
						required : "비밀번호를 입력해 주세요",
						minlength : "비밀번호는 {0}자 이상이어야 합니다.",
						maxlength : "비밀번호는 {0}자 이하이어야 합니다.",
						checkPassword : "알파벳과 숫자, 특수문자를 조합하여 입력하세요."
					},
					confirmLoginPwd : {
						required : "비밀번호를 한 번 더 입력해 주세요",
						minlength : "비밀번호는 {0}자 이상이어야 합니다.",
						maxlength : "비밀번호는 {0}자 이하이어야 합니다.",
						equalTo : "비밀번호가 일치하지 않습니다."
					},
					email : "형식에 맞는 이메일을 입력해 주세요.",
					phone : {
						digits : "숫자만 입력하세요",
						maxlength : "전화번호는 {0}자 이하이어야 합니다."
					},
					mobile : {
						digits : "숫자만 입력하세요",
						maxlength : "휴대폰 번호는 {0}자 이하이어야 합니다."
					},
					"ipArrayList" : {
						maxlength : "",
						ipArrayList : ""
					}
				}
			});		
			
			
			
			//처리 결과 
			if("${result}" != "" && "${retCod}" == "0"){
				//alert("${message}");
				//fnDetail();
				alert("정상적으로 등록 처리되었습니다.\n변경된 정보를 반영하기 위헤 재로그인 하셔야 합니다.");
				location.href="${pageContext.request.contextPath}/security/authenticate.do";				
			}else if("${retCod}" == "1"){
				alert("${message}");
			}
			
		});
	</script>
</head>
<body>


<div id="wrapper">
	<!-- Header 시작 -->
	<%@ include file="/WEB-INF/views/herit/common/header/header.jsp" %>
	<!-- Header 끝 -->
	
	
	<div class="position_text">
		<div class="wrap_div">
			<a href="${pageContext.request.contextPath}"><img src="${pageContext.request.contextPath}/images/common/home_icon.gif" alt="" /></a>
			<span>
				< <a href="${pageContext.request.contextPath}/env/userInfo/info.do">환경설정</a> 
				< <a href="${pageContext.request.contextPath}/env/userInfo/info.do">사용자정보</a> 
				< 사용자정보 수정
			</span>
		</div>
	</div>


	<!-- .container_wrap -->
	<div class="container_wrap">
		<!-- .snb -->
		<%@ include file="/WEB-INF/views/herit/common/left/menu05.jsp" %>
		<!-- /.snb -->	
		

			

	
	

		<!-- #container -->
		<div id="container">
		<form id="myForm" name="myForm" action="<c:url value='/env/userInfo/info.do'/>" method="post">
			<input type="hidden" name="actionType" value="${accountVO.actionType}">
			<input type="hidden" id="id" name="id" value="${accountVO.id}" />
			<input type="hidden" id="changeYn" name="changeYn" value="" />
	

			<div class="contents_title">
				사용자 정보 수정
			</div>
			<div class="table_type3">
				<div class="tr first">
					<div class="th"><span>사용자ID</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="loginId" name="loginId" value="${accountVO.loginId}" class="i_text focus_input al readonly" size="40" readonly />
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>그룹명</span></div>
					<div class="td">
						<div class="td_content">
							<select id="mngAccountGroupId" name="mngAccountGroupId" style="width:auto" class="select_box">
								<c:forEach var="result" items="${mngAccountGroupIdList}" varStatus="status">							
									<option value='<c:out value="${result.code}"/>' <c:if test="${result.code == accountVO.mngAccountGroupId}">selected="selected"</c:if>><c:out value="${result.name}"/></option>
								</c:forEach>
							</select>		
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>사용자명</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="name" name="name" value="${accountVO.name}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>비밀번호</span></div>
					<div class="td">
						<div class="td_content">
							<input type="password" id="loginPwd" name="loginPwd" value="abcd1234!" onkeydown="changeCheck();" onfocus="changeYn2();" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>비밀번호 확인</span></div>
					<div class="td">
						<div class="td_content">
							<input type="password" id="confirmLoginPwd" name="confirmLoginPwd" value="abcd1234!" onkeydown="changeCheck();" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>소속부서</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="department" name="department" value="${accountVO.department}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>Email</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="email" name="email" value="${accountVO.email}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>전화번호</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="phone" name="phone" value="${accountVO.phone}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>휴대폰번호</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="mobile" name="mobile" value="${accountVO.mobile}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>사용여부</span></div>
					<div class="td">
						<div class="td_content">
							<select id="disabled" name="disabled" style="width:auto" class="select_box">
								<option value="1" <c:if test="${'1' == accountVO.disabled}">selected="selected"</c:if> >사용</option>
								<option value="0" <c:if test="${'0' == accountVO.disabled}">selected="selected"</c:if> >미사용</option>
							</select>
						</div>
					</div>
				</div>
			</div>
	
	
	
			<div class="contents_title top_mar_30">접속 허용 IP <span class="table_title3">(입력창이 붉은색이 나타나지 않도록 IP를 정확하게 입력해주세요.)</span></div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_type_left bott_mar_30">
				<c:choose>						
					<c:when test="${!empty ipLimitResultList}">
						<c:forEach items="${ipLimitResultList}" var="resultInfo" varStatus="status">
							<tr>
								<th width="20%">IP ${status.count}</th>
								<td><input type="text" id="ipArrayList" name="ipArrayList" value="${resultInfo.ip}" class="i_text focus_input al" size="40"/></td>
							</tr>
						</c:forEach>
						<c:if test="${fn:length(ipLimitResultList) < 5}">
							<c:forEach begin="${fn:length(ipLimitResultList) + 1}" end="5" step="1" var="resultInfo" varStatus="status">
								<tr>
									<th width="20%">IP ${status.index}</th>
									<td><input type="text" id="ipArrayList" name="ipArrayList" value="" class="i_text focus_input al" size="40"/></td>
								</tr>
							</c:forEach>	
						</c:if>
					</c:when>
					<c:otherwise>
						<c:forEach begin="1" end="5" step="1" var="resultInfo" varStatus="status">
							<tr>
								<th width="20%">IP ${status.count}</th>
								<td><input type="text" id="ipArrayList" name="ipArrayList" value="" class="i_text focus_input al" size="40"/></td>
							</tr>
						</c:forEach>	
					</c:otherwise> 
				</c:choose>		
			</table>
			
	
			<div class="write_button_wrap">
				<div class="board_bott_bt_gray right_mar_270"><a href="javascript:fnDetail()">이전</a></div>
				<c:if test="${sessionScope.requestAuth.authorizationDBUpdate == '1'}">
					<div class="board_bott_bt_pink"><a href="javascript:fnUpdate();">저장</a></div>
				</c:if>
			</div>			
		

	
		</form>
		</div>
		<!-- /#container -->			

	
	</div>
	<!-- .container_wrap -->



	<!-- Footer 시작 --> 
	<%@ include file="/WEB-INF/views/herit/common/footer/footer.jsp" %>
	<!-- Footer 끝 --> 	
</div>		
<!-- /#wrapper -->	
	

</body>
</html>
