<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page session="true"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<title>HIT DM Admin - Firmware Detail</title>
<%@ include file="/WEB-INF/views/common/include/hdm_js.jsp"%>
<script type="text/javascript">
var gr_id="information";
var sub_num="2";
var contextPath = "${pageContext.request.contextPath}";
//var param = ${paramJson};

$(document).ready(function() {
	
	initUI();
	
})

</script>

<script language="javascript" src="${pageContext.request.contextPath}/js/herit/user.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/herit/hdb.js"></script>
<script language="javascript" src="${pageContext.request.contextPath}/js/jquery/jquery.validate.js"></script>

</head>
<body>
	<!-- #wrapper -->
	<div id="wrapper">

		<!-- .header_wrap -->
		<%@ include file="/WEB-INF/views/common/include/hdm_menu.jsp"%>
		<!-- /.header_wrap -->


		<!-- #container -->
		<div class="sub0101">
			<div id="container">
				<div class="page_title">
					<h2>사용자 관리</h2>
					<div class="position_text">
						<img
							src="${pageContext.request.contextPath}/images/hitdm/common/contents_title_home.gif"
							alt="" /> 정보 관리 &gt; 사용자 관리
					</div>
				</div>

				<div class="content_wrap top_pad_30">
					<div id="title_button_list">
						<div class="board_top_bt">
							<a href="#" id="btn_registerUser">사용자 등록</a>
						</div>
					</div>
					<ul class="tabs">
						<li class="active"><a href="#tab_container1">ADMIN</a></li>
						<li class='last'><a href="#tab_container2">MANAGER</a></li>
					</ul>

					<div class="tab_container" id="tab_container1">

						<!-- /-->
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							class="table_type_nomal_scroll2 bott_mar_30" id="admin_table">
							<thead>
							<tr>
								<th id="adm_th_name">이름</th>
								<th id="adm_th_loginId">ID</th>
								<th id="adm_th_email">이메일 주소</th>
								<th id="adm_th_lastAccessTime">최종 로그인시간</th>
								<th id="adm_th_updateTime">수정시간</th>
								<th id="adm_th_modify" class="manual_renderer">수정</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td colspan="6">데이터 로딩중</td>
								<!-- <td><a href="#"
									onclick="window.open('popup03.html', 'addfile', 'left=100, top=150, width=500, height=350, tollbar=0, scrollbars=0, resizeable=0')"><button
											class="level_button">상세</button></a></td>
											 -->
							</tr>
							</tbody>
						</table>
						<!-- /-->
					</div>

					<div class="tab_container" id="tab_container2">

						<!-- /-->
						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							class="table_type_nomal_scroll2 bott_mar_30" id="manager_table">
							<thead>
							<tr>
								<th id="adm_th_name">이름</th>
								<th id="adm_th_loginId">ID</th>
								<th id="adm_th_email">이메일 주소</th>
								<th id="adm_th_lastAccessTime">최종 로그인시간</th>
								<th id="adm_th_updateTime">수정시간</th>
								<th id="adm_th_modify" class="manual_renderer">수정</th>
							</tr>
							</thead>
							<tbody>
							<tr>
								<td colspan="6">데이터 로딩중</td>
								<!-- <td><a href="#"
									onclick="window.open('popup03.html', 'addfile', 'left=100, top=150, width=500, height=350, tollbar=0, scrollbars=0, resizeable=0')"><button
											class="level_button">상세</button></a></td>
											 -->
							</tr>
							</tbody>
						</table>
						<!-- /-->
					</div>

					<div class="tab_container" id="tab_container3">tab_container3</div>

					<div class="tab_container" id="tab_container4">tab_container4</div>

				</div>
			</div>
		</div>

		<div id="dialog-regUser" title="사용자 등록">
				 
		  <form id="form_regUser" name="form_regUser">
		    <fieldset>
		      <label for="name" class="dm_form_label">이름</label>
		      <input type="text" name="name" id="name" value="" class="text ui-widget-content ui-corner-all" />

		      <label for="loginId" class="dm_form_label">로그인ID</label>
		      <input type="text" name="loginId" id="loginId" value="" class="text ui-widget-content ui-corner-all" />

		      <label for="email" class="dm_form_label">이메일 주소</label>
		      <input type="text" name="email" id="email" value="" class="text ui-widget-content ui-corner-all" />

		      <label for="password" class="dm_form_label">패스워드</label>
		      <input type="password" name="password" id="password" value="" class="text ui-widget-content ui-corner-all" />

		      <label for="password_confirm" class="dm_form_label">패스워드 확인</label>
		      <input type="password" name="password_confirm" id="password_confirm" value="" class="text ui-widget-content ui-corner-all" />

		      <label for="accountGroupId" class="dm_form_label">그룹</label>
		      <select name="accountGroupId" id="accountGroupId" class="text ui-widget-content ui-corner-all" />
		      	<option value="2">ADMIN</option>
		      	<option value="3" selected>MANAGER</option>
		      </select>
		 
		      <!-- Allow form submission with keyboard without duplicating the dialog button -->
		      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px" />
		    </fieldset>
		  </form>
		</div>


		<div id="dialog-updateUser" title="사용자 정보 수정">
				 
		  <form id="form_updateUser" name="form_updateUser">
		    <fieldset>
		      <label for="u_name" class="dm_form_label">이름</label>
		      <input type="text" name="u_name" id="u_name" value="" class="text ui-widget-content ui-corner-all" />

		      <label for="u_loginId" class="dm_form_label">로그인ID</label>
		      <input type="text" name="u_loginId" id="u_loginId" value="" class="text ui-widget-content ui-corner-all" disabled />

		      <label for="u_email" class="dm_form_label">이메일 주소</label>
		      <input type="text" name="u_email" id="u_email" value="" class="text ui-widget-content ui-corner-all" />

		      <label for="u_password" class="dm_form_label">패스워드</label>
		      <input type="password" name="u_password" id="u_password" value="*****" class="text ui-widget-content ui-corner-all" />

		      <label for="u_password_confirm" class="dm_form_label">패스워드 확인</label>
		      <input type="password" name="u_password_confirm" id="u_password_confirm" value="*****" class="text ui-widget-content ui-corner-all" />

		      <label for="u_accountGroupId" class="dm_form_label">그룹</label>
		      <select name="u_accountGroupId" id="u_accountGroupId" class="text ui-widget-content ui-corner-all" />
		      	<option value="2">ADMIN</option>
		      	<option value="3" selected>MANAGER</option>
		      </select>
		 
		      <input type="hidden" name="u_id" id="u_id" value="" class="text ui-widget-content ui-corner-all" />
		      <!-- Allow form submission with keyboard without duplicating the dialog button -->
		      <input type="submit" tabindex="-1" style="position:absolute; top:-1000px" />
		    </fieldset>
		  </form>
		</div>

		<script type="text/javascript">
	$(function(){
	  $(".server_graph").each(function(){
		var percent=$(this).text();
		$(this).siblings(".graph_area").find("span").css('width', percent);
	  });
	});
				
	function changeSrc(object, mode)
	{
		var src=object.find('img').attr('src');
		if(mode=='add')
		{
			if(src.indexOf('ov.gif')>=0)	return false;
			else{return src.replace('.gif', 'ov.gif');}
		}
		else if(mode=='remove')
		{
			if(src.indexOf('ov.gif')>=0) return src.replace('ov.gif', '.gif');
			else return false;
		}

	}

	var open_selectbox = function($select) {
		$select.addClass("s-on").children("ul").show();
	}

	var close_selectbox = function($select) {
		$select.removeClass("s-on").children("ul").hide();
	}

	var selectbox = function(el) {

		var $select = jQuery(el);

		jQuery(el).children("a").toggle(
			function(){
				if(!$select.hasClass("s-on")) {
					close_selectbox(jQuery(".s-on"));
					open_selectbox($select);
					jQuery("body").click(function() {
						close_selectbox($select);
					})
				} else {
					close_selectbox($select);
					jQuery("body").click(function() {
						close_selectbox($select);
					})
				}
			},
			function(){
				if($select.hasClass("s-on")) {
					close_selectbox($select);
					jQuery("body").click(function() {
						close_selectbox($select);
					})
				} else {
					close_selectbox(jQuery(".s-on"));
					open_selectbox($select);
					jQuery("body").click(function() {
						close_selectbox($select);
					})
				}
			}
		)
	};

	$(function(){
		$(".select_box").each(function(){
			selectbox($(this));
		});
		$(".select_box>ul>li>a").click(function(){
			var value=$(this).attr('title');
			var html_value=$(this).html();
			$(this).parent().parent().siblings('.select_box_hidden').val(value);
			$(this).parent().parent().hide();
			$(this).parent().parent().siblings(".selected").html(html_value);

			return false;
		});
		$("#family_site>ul>li>a").click(function(){
			var site=$(this).attr('title');
			location.href(site);
		});
		$(".tab_container").hide(); 
		$(".tab_container:first").show();

		//On Click Event
		$("ul.tabs li").click(function() {
			$(".tabs .active").removeClass('active');
			$(this).addClass("active");
			$(".tab_container").hide();

			var activeTab = $(this).find("a").attr("href");
			$(activeTab).show();
			return false;
		});

	});
</script>



		<!-- /#container -->
		<%@ include file="/WEB-INF/views/common/include/hdm_footer.jsp"%>
	</div>
	<!-- /#wrapper -->

</body>
</html>