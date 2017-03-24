<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<html>
<head>
	<title>공지사항</title>		
	<%@ include file="/WEB-INF/views/common/include/include.jsp" %>
	<script type="text/javaScript" language="javascript">
	
	
		/* ********************************************************
		 * 목록 처리
		 **********************************************************/	
		function fnSearch(){
			var myForm				 = document.getElementById("myForm");
			myForm.noticeType.value = "";
			myForm.action           = "<c:url value='/admin/agentNotice/list.do'/>";
		   	myForm.submit();
		}
		/* ********************************************************
		 * 상세 처리
		 **********************************************************/
		function fnDetail(){
			var myForm				 = document.getElementById("myForm");
			myForm.actionType.value  = "V";
			myForm.action           = "<c:url value='/admin/agentNotice/info.do'/>";
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
			myForm.action           = "<c:url value='/admin/agentNotice/update.do'/>";
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
			$("#04_03").addClass("snb_active");
			$("#04_04").removeClass("snb_active");
			
			// validate signup form on keyup and submit
			$("#myForm").validate({
				rules : {
					startTime : {
						required : true
					},
					expireTime : {
						required : true
					},
					workStartTime : {
						required : function(element) {
							return $("#noticeType").val() == "1";
						}
					},
					workEndTime : {
						required : function(element) {
							return $("#noticeType").val() == "1";
						}
					},
					title : {
						required : true,
						maxlength : 40
					},
					link : {
						//url : true,
						maxlength : 150
					},
					content : {
						required : true,
						maxlength : 5000
					}
				},
				messages : {
					agentType : "에이전트 타입을 선택해 주세요",
					startTime : "공지 시작시간을 입력해 주세요",
					expireTime : "공지 종료시간을 입력해 주세요",
					workStartTime : {
						required : "작업 시작시간을 입력해 주세요"
					},
					workEndTime : {
						required : "작업 종료시간을 입력해 주세요"
					},
					title : {
						required : "제목을 입력해 주세요",
						maxlength : "제목은 {0}자 이하이어야 합니다."
					},
					link : {
						//url : "링크 주소를 올바로 입력해 주세요",
						maxlength : "링크는 {0}자 이하이어야 합니다."
					},
					content : {
						required : "내용을 입력해 주세요",
						maxlength : "내용은 {0}자 이하이어야 합니다."
					}
				}
			});
			
// 			//공지 시작시간
// 			$('#startTime').datetimepicker({
// 				dateFormat : 'yy-mm-dd'
//  				,dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ] 
// 				,monthNames : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월' ]		
// 				,timeText : "시간"
// 				,hourText : "시"
// 				,minuteText : "분"
// 				,currentText: '현재'
// 				,closeText: '닫기'
// 			});
// 			//공지 종료시간
// 			$('#expireTime').datetimepicker({
// 				dateFormat : 'yy-mm-dd'
//  				,dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ] 
// 				,monthNames : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월' ]		
// 				,timeText : "시간"
// 				,hourText : "시"
// 				,minuteText : "분"
// 				,currentText: '현재'
// 				,closeText: '닫기'
// 			});
			
			

            var dates = $( '#startTime, #expireTime' ).datetimepicker( {
				dateFormat : 'yy-mm-dd'
	 			,dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ] 
				,monthNames : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월' ]				
				,timeText : "시간"
				,hourText : "시"
				,minuteText : "분"
				,currentText: '현재'
				,closeText: '닫기',
                showAnim: 'clip',
                //minDate: -20,          //지정가능한 최소치
                numberOfMonths: 1,      //달력 표시숫자(1~)
                showCurrentAtPos: 0,    //숫자만큼 해당개월전을 표시(0~)
                // 최소최대일 검증 스크립트
                onSelect: function( selectedDate ) {
			        var startDateTextBox = $('#startTime');
                    var option = this . id == 'startTime' ? 'minDate' : 'maxDate',
                        instance = $( this ) . data( 'datepicker' ),
                        date = $ . datepicker . parseDate(
                            instance . settings . dateFormat ||
                            $ . datepicker . _defaults . dateFormat,
                            selectedDate, instance . settings );
                    dates . not( this ) . datepicker( 'option', option, date );
                },
                onClose: function(dateText, inst) {
			        var startDateTextBox = $('#startTime');
			        var endDateTextBox = $('#expireTime');
			        if (startDateTextBox.val() != '') {
			            var testStartDate = new Date(startDateTextBox.val());
			            var testEndDate = new Date(endDateTextBox.val());
			            if (testStartDate > testEndDate){
			            	startDateTextBox.val(dateText);
			            	endDateTextBox.val(dateText);
			            }
			        }else{
			            startDateTextBox.val(dateText);
			        }
			        
			        if(startDateTextBox.val() != 0 && startDateTextBox.val().length < 12){
			        	startDateTextBox.val(startDateTextBox.val()+"00:00");
			        }
			        if(endDateTextBox.val() != 0 && endDateTextBox.val().length < 12){
			        	endDateTextBox.val(endDateTextBox.val()+"00:00");
			        }
			        
                }
            } );

            var dates2 = $( '#workStartTime, #workEndTime' ).datetimepicker( {
				dateFormat : 'yy-mm-dd'
	 			,dayNamesMin : [ '일', '월', '화', '수', '목', '금', '토' ] 
				,monthNames : [ '1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월' ]				
				,timeText : "시간"
				,hourText : "시"
				,minuteText : "분"
				,currentText: '현재'
				,closeText: '닫기',
                showAnim: 'clip',
                //minDate: -20,          //지정가능한 최소치
                numberOfMonths: 1,      //달력 표시숫자(1~)
                showCurrentAtPos: 0,    //숫자만큼 해당개월전을 표시(0~)
                // 최소최대일 검증 스크립트
                onSelect: function( selectedDate ) {
                    var option = this . id == 'workStartTime' ? 'minDate' : 'maxDate',
                        instance = $( this ) . data( 'datepicker' ),
                        date = $ . datepicker . parseDate(
                            instance . settings . dateFormat ||
                            $ . datepicker . _defaults . dateFormat,
                            selectedDate, instance . settings );
                    dates2 . not( this ) . datepicker( 'option', option, date );
                },
                onClose: function(dateText, inst) {
			        var startDateTextBox = $('#workStartTime');
			        var endDateTextBox = $('#workEndTime');
			        if (startDateTextBox.val() != '') {
			            var testStartDate = new Date(startDateTextBox.val());
			            var testEndDate = new Date(endDateTextBox.val());
			            if (testStartDate > testEndDate){
			            	startDateTextBox.val(dateText);
			            	endDateTextBox.val(dateText);
			            }
			        }else{
			            startDateTextBox.val(dateText);
			        }
			        
			        if(startDateTextBox.val() != 0 && startDateTextBox.val().length < 12){
			        	startDateTextBox.val(startDateTextBox.val()+"00:00");
			        }
			        if(endDateTextBox.val() != 0 && endDateTextBox.val().length < 12){
			        	endDateTextBox.val(endDateTextBox.val()+"00:00");
			        }
                }
            } );
            
			
			//처리 결과 
			if("${result}" != "" && "${retCod}" == "0"){
				alert("${message}");
				fnDetail();
			}else if("${retCod}" == "1"){
				alert("${message}");
				fnDetail();
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
				< <a href="${pageContext.request.contextPath}/admin/agentNotice/list.do">공지사항</a>
				< <a href="javascript:fnDetail()">공지사항 정보</a>
				< 공지사항 수정
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
		<form id="myForm" name="myForm" action="<c:url value='/admin/agentNotice/list.do'/>" method="post">
			<input type="hidden" name="actionType" value="${agentNoticeVO.actionType}">
			<input type="hidden" id="id" name="id" value="${agentNoticeVO.id}" />
			<input type="hidden" id="mngAccountId" name="mngAccountId" value="${sessionScope.account.accountId}" />
	
	
	
			<div class="contents_title">
				공지사항 수정
			</div>
			<div class="table_type3">
				<div class="tr first">
					<div class="th"><span>에이전트 타입</span></div>
					<div class="td">
						<div class="td_content">
							<select id="agentType" name="agentType" style="width:auto" class="select_box">
								<option value="" <c:if test="${'' == agentNoticeVO.agentType}">selected="selected"</c:if> >== 전체 ==</option>
								<option value="01" <c:if test="${'01' == agentNoticeVO.agentType}">selected="selected"</c:if> >홈에이전트</option>
								<option value="02" <c:if test="${'02' == agentNoticeVO.agentType}">selected="selected"</c:if> >가스밸브</option>
							</select>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>종류</span></div>
					<div class="td">
						<div class="td_content">
							<select id="noticeType" name="noticeType" style="width:auto" class="select_box">
								<option value="0" <c:if test="${'0' == agentNoticeVO.noticeType}">selected="selected"</c:if> >일반</option>
								<option value="1" <c:if test="${'1' == agentNoticeVO.noticeType}">selected="selected"</c:if> >긴급</option>
							</select>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>공지 시작시간</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="startTime" name="startTime" value="${agentNoticeVO.startTime}" class="i_text focus_input al readonly" size="40" readonly />
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>공지 종료시간</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="expireTime" name="expireTime" value="${agentNoticeVO.expireTime}" class="i_text focus_input al readonly" size="40" readonly />
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>작업 시작시간</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="workStartTime" name="workStartTime" value="${agentNoticeVO.workStartTime}" class="i_text focus_input al readonly" size="40" readonly />
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>작업 종료시간</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="workEndTime" name="workEndTime" value="${agentNoticeVO.workEndTime}" class="i_text focus_input al readonly" size="40" readonly />
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>제목</span></div>
					<div class="td">
						<div class="td_content">
							<input type="text" id="title" name="title" value="${agentNoticeVO.title}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>링크</span></div>
					<div class="td">
						<div class="td_content">
							http://<input type="text" id="link" name="link" value="${agentNoticeVO.link}" class="i_text focus_input al" size="40"/>
						</div>
					</div>
				</div>
				<div class="tr">
					<div class="th"><span>내용</span></div>
					<div class="td">
						<div class="td_content">
							<textarea id="content" name="content" style="width:568px;" cols="80" rows="18">${agentNoticeVO.content}</textarea>
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
