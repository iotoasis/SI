<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>사용자관리</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	


		/* ********************************************************
		 * 목록 처리
		 **********************************************************/	
		function fnSearch(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/admin/account/list.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 등록 처리
		 **********************************************************/
		function fnInsert(){
			if (!$("#myForm").validate({
				//rules: validationRules
			}).form()) {return;}
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/admin/account/insert.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 수정 처리
		 **********************************************************/
		function fnUpdate(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/admin/account/update.do'/>";
		   	myForm.submit();
		}		
		/* ********************************************************
		 * 중복확인 처리
		 **********************************************************/
		function fnIsExistId(){
			var loginId = $("#loginId").val();
			
			var requestParam = loginId;
						
			$.ajax({
				type: "POST",
				traditional: true,
		        contentType: "application/json",
				url: "<c:url value='/admin/account/isExistId.do'/>",
// 				data: JSON.stringify(requestParam),
				data: loginId,
				dataType: "json",
				success: function(response){
					
				}
			});
		}	
		
		/* ********************************************************
		 * 최초 실행
		 ******************************************************** */
		$(function() {
			
			//대메뉴 활성화 
			$("#01").removeClass("gnb_active");
			$("#02").removeClass("gnb_active");
			$("#03").removeClass("gnb_active");
			$("#04").addClass("gnb_active");
			//서브메뉴 활성화
			$("#04_01").removeClass("snb_active");
			$("#04_02").addClass("snb_active");
			$("#04_03").removeClass("snb_active");
			$("#04_04").removeClass("snb_active");


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
					loginId : {
						required : true,
						rangelength : [4, 12],
						remote : {
					        url: "<c:url value='/admin/account/isExistId.do'/>",
					        type: "post",
					        data: {
					        	loginId: function() {
					            	return $("#loginId").val();
					          	}
					        }
						}
					},
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
						number : true
					},
					mobile : {
						number : true
					},
					ipArrayList : {
						maxlength : 15,
						ipArrayList : true
					}
				},
				messages : {
					mngAccountGroupId : "그룹명을 입력해 주세요",
					loginId : {
						required : "아이디를 입력해 주세요",
						rangelength : "{0}에서 {1} 사이의 글자 길이로 입력하세요.",
						remote : "아이디가 중복됩니다."
					},
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
						number : "숫자만 입력하세요"
					},
					mobile : {
						number : "숫자만 입력하세요"
					},
					"ipArrayList" : {
						maxlength : "",
						ipArrayList : ""
					}
				}
			});			

			
			//처리 결과 
			if("${result}" != "" && "${retCod}" == "0"){
				alert("${message}");
				fnSearch();
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
				< <a href="${pageContext.request.contextPath}/admin/accountGroup/list.do">운영 관리</a> 
				< <a href="${pageContext.request.contextPath}/admin/account/list.do">사용자 관리</a>
				< 사용자 등록
			</span>
		</div>
	</div>


	<!-- .container_wrap -->
	<div class="container_wrap">
		<!-- .snb -->
		<%@ include file="/WEB-INF/views/herit/common/left/menu04.jsp" %>
		<!-- /.snb -->	
		

			

	
	

		<!-- #container -->
		<div id="container">
		<form id="myForm" name="myForm" action="<c:url value='/admin/account/list.do'/>" method="post">
			<input type="hidden" name="actionType" value="${accountVO.actionType}">
			<input type="hidden" id="id" name="id" value="${accountVO.id}" />
			<input type="hidden" id="isExistId" name="isExistId" value="" />
	
	


			<div class="contents_title">
				사용자 정보 등록
			</div>
			<div class="table_type3">
				<div class="tr first">
					<div class="th"><span>사용자ID</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="loginId" name="loginId" value="${accountVO.loginId}" class="i_text focus_input al" size="40"/>
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
							<input type="text" id="name" name="name" value="${accountVO.name}" class="i_text focus_input al" size="40" />
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>비밀번호</span></div>
					<div class="td">
						<div class="td_content">
							<input type="password" id="loginPwd" name="loginPwd" value="${accountVO.loginPwd}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>비밀번호 확인</span></div>
					<div class="td">
						<div class="td_content">
							<input type="password" id="confirmLoginPwd" name="confirmLoginPwd" value="${accountVO.loginPwd}" class="i_text focus_input al" size="40"/>
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
				<tr>
					<th width="20%">IP 1</th>
					<td><input type="text" id="ipArrayList" name="ipArrayList" value="" class="i_text focus_input al" size="40"/></td>
				</tr>
				<tr>
					<th width="20%">IP 2</th>
					<td><input type="text" id="ipArrayList" name="ipArrayList" value="" class="i_text focus_input al" size="40"/></td>
				</tr>
				<tr>
					<th width="20%">IP 3</th>
					<td><input type="text" id="ipArrayList" name="ipArrayList" value="" class="i_text focus_input al" size="40"/></td>
				</tr>
				<tr>
					<th width="20%">IP 4</th>
					<td><input type="text" id="ipArrayList" name="ipArrayList" value="" class="i_text focus_input al" size="40"/></td>
				</tr>
				<tr>
					<th width="20%">IP 5</th>
					<td><input type="text" id="ipArrayList" name="ipArrayList" value="" class="i_text focus_input al" size="40"/></td>
				</tr>
			</table>


			<div class="write_button_wrap">
				<div class="board_bott_bt_gray right_mar_270"><a href="javascript:fnSearch()">목록</a></div>
				<c:if test="${sessionScope.requestAuth.authorizationDBUpdate == '1'}">
					<div class="board_bott_bt_pink"><a href="javascript:fnInsert();">저장</a></div>
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
