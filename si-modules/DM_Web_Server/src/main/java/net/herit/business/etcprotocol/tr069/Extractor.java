package net.herit.business.etcprotocol.tr069;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import net.herit.business.etcprotocol.constant.KeyName;
import net.herit.business.etcprotocol.constant.Target;

public class Extractor {
	
	// device ID 불러오기
	public String getDeviceId(JSONObject token){
		return getDeviceId(token, Target.DM);
	}
	public String getDeviceId(JSONObject token, Target target){
		String deviceId = token.getString("deviceId");
		if(target == Target.DM){
			deviceId = deviceId.replace("-", "_");
		}
		return deviceId;
	}
	
	// 필수 항목 불러오기
	public String getKeyFromId(String deviceId, KeyName keyName){
		String result = null;
		String[] idSplit = deviceId.split("_");
		if(idSplit.length == 0){
			idSplit = deviceId.split("_");
		}
		switch(keyName){
		case MODEL_NAME:
			result = idSplit[1];
			break;
		case MANUFACTURER_OUI:
			result = idSplit[0];
			break;
		case SERIAL_NUMBER:
			result = idSplit[2];
			break;
		}
		return result;
	}
	
	// inform 메시지 불러오기
	public JSONObject getInform(JSONObject token){
		return parametersToJSON(token.getString("httpRequest"));
	}
	
	
	// getByXml
	public JSONObject parametersToJSON(String xml){
		JSONObject result = new JSONObject();
		
		try{
			InputSource is = new InputSource(new StringReader(xml));
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
			XPath  xpath = XPathFactory.newInstance().newXPath();
			
			String expression = "//ParameterList/ParameterValueStruct";
			//NodeList cols = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
			NodeList cols = (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
			
			expression = "//*/EventCode";
			String command = xpath.compile(expression).evaluate(document);
			//System.out.println("Command................"+command);
			
			result.put("command", command);
			
			for( int idx=1; idx<=cols.getLength(); idx++ ){
				
				expression = "//ParameterList/ParameterValueStruct["+idx+"]/Name";
				String name = xpath.compile(expression).evaluate(document);
				//System.out.println("Name................"+name);
	
				expression = "//ParameterList/ParameterValueStruct["+idx+"]/Value";
				String value = xpath.compile(expression).evaluate(document);
				//System.out.println("Value................"+value);
				
				System.out.println("["+idx+"] name : " + name + " / value : " + value);
				if(!name.equals("")){
					result.put(name, value);
				}
			}
			
		} catch(Exception e){}
		
		return result;
	}
}
