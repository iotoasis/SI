<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>그룹권한관리</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>

	<script type="text/javaScript" language="javascript">
	


		/* ********************************************************
		 * 목록 처리
		 **********************************************************/
		function fnSearch() {
			var myForm = document.getElementById("myForm");
			myForm.action = "<c:url value='/admin/accountGroup/list.do'/>";
			myForm.submit();
		}
		/* ********************************************************
		 * 등록 처리
		 **********************************************************/
		function fnInsert() {
			if (!$("#myForm").validate({
				//rules: validationRules
			}).form()) {return;}
			var myForm = document.getElementById("myForm");
			myForm.action = "<c:url value='/admin/accountGroup/insert.do'/>";
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
			$("#04_01").addClass("snb_active");
			$("#04_02").removeClass("snb_active");
			$("#04_03").removeClass("snb_active");
			$("#04_04").removeClass("snb_active");
			
			// validate signup form on keyup and submit
			$("#myForm").validate({
				rules : {
					groupCode : {
						required : true,
						rangelength : [4, 12],
						remote : {
					        url: "<c:url value='/admin/accountGroup/isExistGroupCode.do'/>",
					        type: "post",
					        data: {
					        	groupCode: function() {
					            	return $("#groupCode").val();
					          	}
					        }
						}
					},				
					groupName : {
						required : true,
						maxlength : 20
					},				
					description : {
						maxlength : 100
					}
				},
				messages : {
					groupCode : {
						required : "아이디를 입력해 주세요",
						rangelength : "{0}에서 {1} 사이의 글자 길이로 입력하세요.",
						remote : "그룹코드가 중복됩니다."
					},			
					groupName : {
						required : "그룹명을 입력해 주세요",
						maxlength : "그룹명은 {0}자 이하이어야 합니다."
					},			
					description : {
						maxlength : "설명은 {0}자 이하이어야 합니다."
					}
				}
			});
		

			//처리 결과 
			if ("${result}" != "" && "${retCod}" == "0") {
				alert("${message}");
				fnSearch();
			} else if ("${retCod}" == "1") {
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
				< <a href="${pageContext.request.contextPath}/admin/accountGroup/list.do">권한 그룹 관리</a>
				< 권한 그룹 등록
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
		<form id="myForm" name="myForm" action="<c:url value='/admin/accountGroup/list.do'/>" method="post">
			<input type="hidden" name="actionType" value="${accountGroupVO.actionType}">
	
	
	
			<div class="contents_title">
				권한 그룹 등록
			</div>
			<div class="table_type3">
				<div class="tr first">
					<div class="th"><span>그룹코드</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="groupCode" name="groupCode" value="${accountGroupVO.groupCode}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>그룹명</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="groupName" name="groupName" value="${accountGroupVO.groupName}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>설명</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="description" name="description" value="${accountGroupVO.description}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
			</div>	
			
			
			
			
			<div class="contents_title top_mar_30">메뉴 권한 설정</div>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_type_nomal bott_mar_30">
				<tr>
		            <th>메뉴명</th>
		            <th>생성권한</th>
		            <th>읽기권한</th>
		            <th>수정권한</th>
		            <th>삭제권한</th>
				</tr>
				<c:choose>						
					<c:when test="${!empty resultList}">
						<c:forEach items="${resultList}" var="resultInfo" varStatus="status">
							<tr>
								<td>
									<c:out value="${resultInfo.menuName}" />
									<input type="hidden" name="checkMenuIdList" value="${resultInfo.menuId}">
								</td>
								<td>
									<input type="checkbox" name="checkRightCList" value="${resultInfo.menuId}">
								</td>
								<td>
									<c:if test="${resultInfo.menuId == 'MENU_100'}">
										<input type="checkbox" name="checkRightRList" value="${resultInfo.menuId}" checked onclick="return false;">
									</c:if>
									<c:if test="${resultInfo.menuId != 'MENU_100'}">
										<input type="checkbox" name="checkRightRList" value="${resultInfo.menuId}">
									</c:if>									
								</td>
								<td>
									<input type="checkbox" name="checkRightUList" value="${resultInfo.menuId}">
								</td>
								<td>
									<input type="checkbox" name="checkRightDList" value="${resultInfo.menuId}">
								</td>
							</tr>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<tr>
							<td colspan="5">조회된 데이터가 없습니다.</td>
						</tr>						
					</c:otherwise> 
				</c:choose>
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
