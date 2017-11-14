package net.herit.business.etcprotocol;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import net.herit.business.etcprotocol.constant.Type;

public class DmModel {
	private JSONObject inform;
	private String deviceId;
	private String oui;
	private String modelName;
	private String serialNumber;
	private String authId;
	private String authPw;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getOui() {
		return oui;
	}
	public String getModelName() {
		return modelName;
	}
	public String getSerialNumber() {
		return serialNumber;
	}	
	public String getAuthId() {
		return authId;
	}
	public String getAuthPw() {
		return authPw;
	}
	public void initDeviceInfo(JSONObject token){
		initDeviceInfo(token, Type.LWM2M);
	}
	public JSONObject getInform() {
		return inform;
	}
	
	
	public JSONObject getXml(String xml){
		JSONObject result = new JSONObject();
		
		try{
			InputSource is = new InputSource(new StringReader(xml));
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			XPath  xpath = XPathFactory.newInstance().newXPath();
			String expression = "//*/ParameterValueStruct";
			NodeList cols = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
			
			expression = "//*/EventCode";
			String command = xpath.compile(expression).evaluate(document);
			//System.out.println("Command................"+command);
			
			result.put("command", command);
			
			for( int idx=0; idx<cols.getLength(); idx++ ){
				
				expression = "//*/ParameterValueStruct["+idx+"]/Name";
				String name = xpath.compile(expression).evaluate(document);
				//System.out.println("Name................"+name);
	
				expression = "//*/ParameterValueStruct["+idx+"]/Value";
				String value = xpath.compile(expression).evaluate(document);
				//System.out.println("Value................"+value);
				
				if(!name.equals("")){
					result.put(name, value);
				}
			}
			
		} catch(Exception e){}
		
		return result;
	}
	
	
	public void initDeviceInfo(JSONObject token, Type protocol){
		System.out.println("!!!    Trying to Device Information update...");
		switch(protocol){
		case LWM2M:
			oui = token.getString("oui");
			modelName = token.getString("modelName");
			serialNumber = token.getString("sn");
			deviceId = new StringBuffer(oui).append("_").append(modelName).append("_").append(serialNumber).toString();
			authId = token.getString("authId");
			authPw = token.getString("authPw");
			break;
		case TR_069:
			deviceId = token.getString("deviceId").replace("-", "_");
			inform = getXml(token.getString("httpRequest"));
			oui = inform.getString("InternetGatewayDevice.DeviceInfo.ManufacturerOUI");
			modelName = inform.getString("InternetGatewayDevice.DeviceInfo.ProductClass");
			serialNumber = inform.getString("InternetGatewayDevice.DeviceInfo.SerialNumber");
			authId = inform.getString("InternetGatewayDevice.ManagementServer.ConnectionRequestUsername");
			authPw = inform.getString("InternetGatewayDevice.ManagementServer.ConnectionRequestPassword");
			inform.put("deviceId", deviceId);
			//deviceId = new StringBuffer(oui).append("-").append(modelName).append("-").append(serialNumber).toString();
			break;
		}
		System.out.println("!!!    Device Information has initialized!");
		System.out.println(inform.toString());
		
	}
	
}
