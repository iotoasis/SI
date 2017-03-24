<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script>


	var serverIp = "<%=request.getServerName()%>";
	var contextPath = "${pageContext.request.contextPath}";

	var contextRoot = "/hdm";
	
	
	/* deviceModel Default value */
	// ["표시이름", "URI", "자료형", "단위", "기본값", "오퍼레이션", "노티옵션", "오류필드여부", "진단필드여부", "이력저장여부", "필수여부"]
	// ["DISPLAY_NAME", "RESOURCE_URI", "DATA_TYPE", "UNIT", "DEFAULT_VALUE", "OPERATION", "NOTI_TYPE", "IS_ERROR", "IS_DIAGNOSTIC", "IS_HISTORICAL", "IS_MANDATORY"]
	var deviceModelAttributeList = new Array();	
	deviceModelAttributeList.push(["DM Server URI", "/0/-/0", "S", "", "https://dm.herit.net", "RW", "0", "N", "N", "", "Y"]);
	deviceModelAttributeList.push(["Security Mode", "/0/-/1", "I", "", "2", "RW", "0", "N", "N", "", "Y"]);
	deviceModelAttributeList.push(["Lifetime", "/1/-/1", "I", "", "86400", "RW", "0", "N", "N", "", "Y"]);
	deviceModelAttributeList.push(["Manufacturer", "/2/-/0", "S", "", "", "R", "0", "N", "N", "", "Y"]);
	deviceModelAttributeList.push(["Model Number", "/2/-/1", "S", "", "", "R", "0", "N", "N", "", "Y"]);
	deviceModelAttributeList.push(["Serial Number", "/2/-/2", "S", "", "", "R", "1", "N", "N", "", "Y"]);
	deviceModelAttributeList.push(["Device Id", "/2/-/3", "S", "", "", "R", "0", "N", "N", "", "Y"]);
	deviceModelAttributeList.push(["MO Profile Version", "/2/-/30", "S", "", "", "R", "1", "N", "N", "Y", "Y"]);

</script>

