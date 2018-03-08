<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- Mainly scripts -->
<script language="javascript" src="<c:url value="/v2/js/jquery-2.1.1.js" />"></script>
<script language="javascript" src="http://malsup.github.com/jquery.form.js"></script>
<script language="javascript" src="<c:url value="/v2/js/bootstrap.min.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/plugins/bootbox/bootbox.min.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/plugins/metisMenu/jquery.metisMenu.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/plugins/slimscroll/jquery.slimscroll.min.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/jquery-ui-1.9.2.js" />"></script>

<!-- Custom and plugin javascript -->
<script language="javascript" src="<c:url value="/v2/js/inspinia.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/plugins/jsKnob/jquery.knob.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/plugins/guage/raphael-2.1.4.min.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/plugins/guage/justgage.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/plugins/pace/pace.min.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/plugins/iCheck/icheck.min.js" />"></script>

<script language="javascript" src="<c:url value="/v2/js/herit/common/common.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/herit/common/validator.js" />"></script>
<% //int dmType = (Integer)request.getAttribute("dmType"); %>
<% if( request.getAttribute("dmType")!=null && (Integer)request.getAttribute("dmType")==2 ){ %>
	<script language="javascript" src="<c:url value="/v2/js/herit/common/dm_lwm2m.js" />"></script>
	<script language="javascript" src="<c:url value="/v2/js/herit/common/uicomponent_lwm2m.js" />"></script>
<% //} else if( request.getAttribute("dmType")!=null && (Integer)request.getAttribute("dmType")==3 ) { %>
<!-- 
	<script language="javascript" src="<c:url value="/v2/js/herit/common/dm_tr069.js" />"></script>
	<script language="javascript" src="<c:url value="/v2/js/herit/common/uicomponent_tr069.js" />"></script>
 -->
<% } else { %>
	<script language="javascript" src="<c:url value="/v2/js/herit/common/dm.js" />"></script>
	<script language="javascript" src="<c:url value="/v2/js/herit/common/uicomponent.js" />"></script>
<% } %>
<script language="javascript" src="<c:url value="/v2/js/herit/common/hdb.js" />"></script>
<script language="javascript" src="<c:url value="/v2/js/jquery.validate.js" />"></script>
