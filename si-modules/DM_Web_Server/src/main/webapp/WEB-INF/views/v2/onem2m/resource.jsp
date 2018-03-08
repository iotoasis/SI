<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/views/v2/common/common_head.jsp"%>
    <title>OASIS SI Admin</title>
	<%@ include file="/WEB-INF/views/v2/common/common_js.jsp"%>
	
	<script language="javascript" type="text/javascript" src="<c:url value="/v2/js/xml2json.min.js" />"></script>
	
	<style type="text/css">
		.tree_menu {overflow:hidden;}
		.tree_menu ul {list-style: none; padding: 0px;}
		.tree_menu > ul {}
		.tree_menu ul li {min-height:23px; padding-left:26px; margin-bottom:5px; background: url(<c:url value="/v2/img/tree_menu_line.png" />) 25px bottom repeat-y; position:relative; z-index:10;}
		.tree_menu ul li.open > ul {display:block;}
		.tree_menu ul li:last-child {margin-bottom:0px;}
		.tree_menu > ul > li {padding-left:7px; background:url(<c:url value="/v2/img/tree_menu_line.png" />) 6px bottom repeat-y;}
		.tree_menu ul li span {display:inline-block; padding-left:20px; margin-left:-26px; background: url(<c:url value="/v2/img/tree_menu_li_line.gif" />) 0 11px no-repeat; position:relative;}
		.tree_menu > ul > li > span {padding-left:0px; margin-left:-7px;}
		.tree_menu ul li span button {display:inline-block; width:13px; height:13px; margin-right:3px; background:url(<c:url value="/v2/img/tree_menu_plus.png" />) 0 0 no-repeat; position:relative; top:2px; border:0; cursor:pointer;}
		.tree_menu ul li span button.on {background-image:url(<c:url value="/v2/img/tree_menu_minus.png" />);}
		.tree_menu > ul > li > span > button {left:0;}
		.tree_menu ul li span a {line-height:23px; color:#333;}
		.tree_menu > ul li ul {display:none;}
		.tree_menu > ul > li ul li:last-child > span:after {display:block; content:''; width:1px; height:1000px; position:absolute; left:-1px; top:12px; }
	</style>	
	
    <script type="text/javaScript" language="javascript">
		$(document).ready(function() {
			treeObj = $('.tree_menu');
			initUI();
		});
		
		function initUI() {
			$("#side-menu_oneM2M").addClass("active");
			$("#side-menu_oneM2M ul").addClass("in");
			$("#side-menu_oneM2M ul li:eq(0)").addClass("active");
		}
		
		function fnSearch() {
			var uri = $("#uri").val().trim();
			if (uri.length == 0)
				uri = "/herit-in/herit-cse";
			
			$("#node_root").empty();
			nodes = [];
			pushNode({
				"parent" : "root",
				"uri" : uri,
				"rn" : uri,
				"ty" : "",
				"data" : {}
			});
			
			getResource(0);
		}
		
		
		var x2js = new X2JS();
		var nodes = [];
		
		function pushNode(obj) {
			obj.getChild = false;
			nodes.push(obj);
			return nodes.length - 1;
		}
		
		function getResource(idx, chFlag) {
			var param = {"url" :  nodes[idx].uri + "?rcn=5"};
			
			$.ajax({
				type: "POST",
				url: "<c:url value="/onem2m/getResource.do" />",
				data: param,
				dataType: "json",
				success: function(response){
					if (!response.result) {
						if (null == chFlag) {
							alert("리소스 브라우저 조회중 에러가 발생하였습니다!\n" + response.message);
						}
					}
					else {
						console.log("getResource: " + idx + ", " + response.status);
						if (response.status == 200) {
							var obj = x2js.xml_str2json(response.body);
							for (var key in obj) {
								nodes[idx].m2m = key;
								nodes[idx].data = obj[key];
								break;
							}
							writeNode(idx, chFlag);
						}
						else if (response.status == 404) {
							if (null == chFlag) {
								alert("해당 URI가 존재하지 않습니다!");
							}
						}
					}
				},
				error: function(e){
					//alert("리소스 브라우저 조회중 에러가 발생하였습니다!");
					console.log(nodes[idx]);
					console.log(e);
				}
			});
		}
		
		function writeNode(idx, chFlag) {
			console.log("writeNode: " + idx);
			console.log(nodes[idx]);
			var html = "";
			
			if (null != nodes[idx].data.ch && "undefined" != nodes[idx].data.ch) {
				html = "<li><span><button data-idx=" + idx + "></button><a href='javascript:showData(" + idx + ")'>" + nodes[idx].rn + "</a> </span><ul id='node_" + idx + "'></ul></li>";
			}
			else {
				html = "<li style='background-image: none;'><span><a href='javascript:showData(" + idx + ")'>" + nodes[idx].rn + "</a></span></li>";
			}
			
			$("#node_" + nodes[idx].parent).append(html);

			treeMenu();
			getChildResource(idx, chFlag);
		}
		
		function showData(idx) {
			var html = "";
			for (var key in nodes[idx].data) {
				if (key.charAt(0) == "_" || key == "ch")
					continue;
				html += "<tr>";
				html += "<td>" + key + "</td>";
			    html += "<td><span style='width:300px; overflow:hidden; text-overflow:ellipsis; display:inline-block;'>" + nodes[idx].data[key] + "</span></td>";
				html += "</tr>"
			}
			$("#tbody_detail").html(html);
			
			$(".modal-title").html(nodes[idx].rn);			
			$("#myModal").modal('show');
		}
		
		function getChild(idx) {
			if (!nodes[idx].getChild)
				getChildResource(idx);
		}

		function getChildResource(idx, chFlag) {
			
			if (nodes[idx].data.ch != null) {
				
				if (isArray(nodes[idx].data.ch)) {
					for (var key in nodes[idx].data.ch) {
						var i = pushNode({
							"parent" : idx,
							"uri" : nodes[idx].data.ch[key].__text,
							"rn" : nodes[idx].data.ch[key]._nm,
							"ty" : nodes[idx].data.ch[key]._typ,
							"data" : {}
						});
						
						console.log("pushNode: " + i);
						console.log(nodes[idx].data.ch[key]);
						
						if (null == chFlag) {
							nodes[idx].getChild = true;
							getResource(i, true);
						}
					}
				}
				else {
					var i = pushNode({
						"parent" : idx,
						"uri" : nodes[idx].data.ch.__text,
						"rn" : nodes[idx].data.ch._nm,
						"ty" : nodes[idx].data.ch._typ,
						"data" : {}
					});
					
					console.log("pushNode: " + i);
					if (null == chFlag) {
						nodes[idx].getChild = true;
						getResource(i, true);
					}
				}
			}
		}
		
		function isArray(obj) {
		    return Object.prototype.toString.call(obj) === '[object Array]';
		}
		
		function treeMenu(){
			var btn = treeObj.find('button');

			btn.unbind("click");
			btn.click(function(){
				$(this).toggleClass('on');
				$(this).parent().next().toggle(300);
				getChild($(this).data('idx'));
			});
		}

    </script>
</head>
<body>

<div id="wrapper">

	<nav class="navbar-default navbar-static-side" role="navigation">
		<div class="sidebar-collapse">
			<%@ include file="/WEB-INF/views/v2/common/common_sidemenu.jsp"%>
		</div>
	</nav>


	<div id="page-wrapper" class="gray-bg">
		<div class="row border-bottom">
			<%@ include file="/WEB-INF/views/v2/common/common_topbar.jsp"%>
		</div>
		

		<div class="row wrapper border-bottom white-bg page-heading">
			<div class="col-sm-12">
				<h2>리소스 트리 조회</h2>
				<ol class="breadcrumb">
					<li><a href="<c:url value="/index.do" />">Home</a></li>
					<li>oneM2M 서버 관리</li>
					<li class="active"><strong>리소스 트리 조회</strong></li>
				</ol>
			</div>
		</div>
		
		
		<div class="wrapper wrapper-content animated fadeInRight">
			<div class="row">
				<div class="col-lg-12">
					<div class="ibox float-e-margins">
						<div class="ibox-title">
							<h3 class="no-margins">리소스 트리 조회</h3>
						</div>
						<div class="ibox-content">
							<div class="row">
								<div class="col-sm-12">
									<label class="form-inline col-sm-12">
										URI&nbsp;
										<div class="input-group col-sm-9">
											<input type="text" id="uri" value="/herit-in/herit-cse" class="input-sm form-control" />
											<span class="input-group-btn">
												<button type="button" onclick="fnSearch(); return false;" class="btn btn-sm btn-primary" > 검색</button>
											</span>
										</div>
									</label>
								</div>
							</div>
						
							<div>&nbsp;</div>

							<div class="tree_menu">
								<ul id="node_root">
								</ul>
							</div>

						</div>
					</div>
				</div>
			</div>
		</div>
		
		
		<div class="footer">
			<%@ include file="/WEB-INF/views/v2/common/common_footer.jsp"%>
		</div>
	</div>
</div>


<div class="modal inmodal" id="myModal" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal-dialog">
<form id="modalForm">
<input type="hidden" id="modal_id" name="modal_id" />
		<div class="modal-content animated bounceInRight">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
				<h4 class="modal-title"></h4>
			</div>
			<div class="modal-body" style="max-height: 450px; overflow:auto;">
				<table class="table table-bordered table-hover">
					<tbody id="tbody_detail">
					</tbody>
				</table>											
			</div>
			<div class="modal-footer">
				<button type='button' class='btn btn-white' data-dismiss='modal'>닫기</button>
			</div>
		</div>
</form>
	</div>
</div>

</body>
</html>
