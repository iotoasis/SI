<%@page pageEncoding="UTF-8"%>

<link rel="shortcut icon" href="${pageContext.request.contextPath}/images/hitdm/common/favicon.ico" >
<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css">

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/hitdm/reset.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/hitdm/style.css?v=1" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/hitdm/margin.css" />

<script language="javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
<script language="javascript" src="http://code.jquery.com/ui/1.11.2/jquery-ui.js"></script>
	
	<script type="text/javascript">
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
	}

	$(function(){
		$("#gnb .gnb_"+gr_id).addClass('gnb_active');
		$("#gnb .gnb_"+gr_id+" li").eq(parseInt(sub_num)-1).addClass('gnb_active');
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

		$("#gnb>ul>li").hover(function(){
			if($("#gnb .gnb_active").length)
			{
				$(".gnb_active").removeClass("gnb_active");
			}
			$(this).find("ul").show();
			$(this).addClass('gnb_hover');

		},function(){
			$(this).find("ul").hide();
			$(this).removeClass('gnb_hover');
			$("#gnb .gnb_"+gr_id).addClass('gnb_active');
			$("#gnb .gnb_"+gr_id+" li").eq(parseInt(sub_num)-1).addClass('gnb_active');
		});

		$("#gnb>ul>li>ul>li").hover(function(){
			$(this).addClass('gnb_hover');
		},function(){
			$(this).removeClass('gnb_hover');
		});
	});
</script>