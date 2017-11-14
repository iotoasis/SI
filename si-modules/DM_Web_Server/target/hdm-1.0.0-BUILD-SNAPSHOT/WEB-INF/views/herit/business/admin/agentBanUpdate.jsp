<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>에이전트 정책관리</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	
	
		/* ********************************************************
		 * 목록 처리
		 **********************************************************/	
		function fnSearch(){
			var myForm				 = document.getElementById("myForm");
			myForm.action           = "<c:url value='/admin/agentBan/list.do'/>";
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
			myForm.action           = "<c:url value='/admin/agentBan/update.do'/>";
		   	myForm.submit();
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
			$("#04_02").removeClass("snb_active");
			$("#04_03").removeClass("snb_active");
			$("#04_04").addClass("snb_active");

			jQuery.validator.addMethod("stopVersion", function(value, element) {
				return this.optional(element) || /^[0-9.~]+$/.test(value);
			}); 
			// validate signup form on keyup and submit
			$("#myForm").validate({
				rules : {
					stopVersion : {
						required : true,
						maxlength : 20,
						stopVersion : true
					}
				},
				messages : {
					stopVersion : {
						required : "실행 금지버전을 입력해 주세요",
						maxlength : "실행 금지버전은 {0}자 이하이어야 합니다.",
						stopVersion : "버전 정보의 형태가 잘못되었습니다."
					}
				}
			});
			
			
			//처리 결과 
			if("${result}" != "" && "${retCod}" == "0"){
				alert("${message}");
				fnSearch();
			}else if("${retCod}" == "1"){
				alert("${message}");
				fnSearch();
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
				< <a href="${pageContext.request.contextPath}/admin/agentBan/list.do">에이전트 정책 관리</a>
				< 에이전트 정책 수정
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
		<form id="myForm" name="myForm" action="<c:url value='/admin/agentBan/list.do'/>" method="post">
			<input type="hidden" name="actionType" value="${agentBanVO.actionType}">
			<input type="hidden" id="id" name="id" value="${agentBanVO.id}" />
	
	
			<div class="contents_title">
				에이전트 정책 수정
			</div>
			<div class="table_type3">
				<div class="tr first">
					<div class="th"><span>실행 금지버전</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="stopVersion" name="stopVersion" value="${agentBanVO.stopVersion}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>적용여부</span></div>
					<div class="td">
						<div class="td_content">
							<select id="expired" name="expired" style="width:auto" class="select_box">
								<option value="1" <c:if test="${'1' == agentBanVO.expired}">selected="selected"</c:if> >예</option>
								<option value="0" <c:if test="${'0' == agentBanVO.expired}">selected="selected"</c:if> >아니오</option>
							</select>
						</div>
					</div>
				</div>
			</div>	
			
	
			<div class="write_button_wrap">
				<div class="board_bott_bt_gray right_mar_270"><a href="javascript:fnSearch()">목록</a></div>
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
