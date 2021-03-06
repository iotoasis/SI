package net.herit.business.protocol;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class Util {
	
	private static Util instance = null;
	public static Util getInstance(){
		if(instance == null){
			instance = new Util();
		}
		return instance;
	}
	
	// is Null Or Empty
	public boolean isNOE(String txt){
		boolean result = false;
		if(txt == null || txt.replaceAll(" ", "").equals("")){
			result = true;
		}
		return result;
	}
	
	// JSONObject 내용 전부 출력
	public void printJSONObject(JSONObject obj){
		//System.out.println("::::  JSONObject contains..");
		Iterator<String> it = obj.keys();
		while(it.hasNext()){
			try{
				String key = it.next();
				Object value = obj.get(key);
				//System.out.println("["+key+"]-["+value+"]");
			} catch(JSONException e){
				e.printStackTrace();
			}
		}
	}
	
	// HashMap 내용 전부 출력
	public void printMap(HashMap<String,String> map){
		//System.out.println("::::  HashMap contains..");
		Iterator<String> it = map.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = map.get(key);
			//System.out.println("["+key+"]-["+value+"]");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	public static String makeDeviceId(JSONObject token){
		
		StringBuffer deviceId = new StringBuffer();
		
		try {
			deviceId.append(token.get("oui")).append("_");
			deviceId.append(token.get("modelName")).append("_");
			deviceId.append(token.get("sn"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
		return deviceId.toString();
	}
	
	
	
	public static List<String[]> getUriAndName(JSONObject token){
		
		List<String[]> list = new ArrayList<String[]>();
		
		try {
			JSONArray objModels = token.getJSONArray("objectModels");
			for (int i = 0; i < objModels.length(); i++) {
				StringBuffer uriBase = new StringBuffer("/");
				uriBase.append(objModels.getJSONObject(i).get("id")).append("/-/");
				JSONArray resources = objModels.getJSONObject(i).getJSONArray("resources");
				for (int j = 0; j < resources.length(); j++) {
					StringBuffer uri = new StringBuffer(uriBase);
					uri.append(resources.getJSONObject(j).get("id"));
					
					String[] vo = new String[2];
					vo[0] = uri.toString();
					vo[1] = resources.getJSONObject(j).get("name").toString();
					list.add(vo);
				}
			}			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public HashMap<String, String> jsonToMap(JSONObject token){
		HashMap<String, String> map = new HashMap<String, String>();
		Iterator<String> keys = token.keySet().iterator();
		while(keys.hasNext()){
			String key = keys.next();
			map.put(key, token.getString(key));
		}
		return map;
	}
	
	
	
	
	public String getDataType(String txt){
		String result = "";
		String elseTxt = "";
		
		if("integer".equals(txt)){
			result = "I";
		} else if("string".equals(txt)){
			result = "S";
		} else if("boolean".equals(txt)){
			result = "B";
		} else if("float".equals(txt)){
			result = "F";
		} else if("long".equals(txt)){
			result = "L";
		} else if("double".equals(txt)){
			result = "D";
		} else if("time".equals(txt)){
			result = "T";
		} else if("opaque".equals(txt)){
			elseTxt = txt;
		} else {
			elseTxt = txt;
		}
		//System.out.println(result);
		//System.out.println(elseTxt);
		
		return result;
	}
	
	public static String getValueFromXml( String xml, String target ) {
        Node node = null;
        String make_xml = combineString(xml.substring(0,xml.indexOf(">")+1),"<root>",xml.substring(xml.indexOf(">")+1),"</root>");
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(make_xml.getBytes()));
            doc.getDocumentElement().normalize();
            XPath xpath = XPathFactory.newInstance().newXPath();
            String pathResult = "";
            if( target.equals("sur") ){
            	pathResult = "//sgn/sur";
            } else {
            	pathResult = "//sgn/nev/rep/cin/"+target;
            }
            node = (Node) xpath.evaluate(pathResult, doc, XPathConstants.NODE);
        } catch( Exception e ) {
            e.printStackTrace();
            return null;
        }
        return node.getTextContent();
    }
	
	// 글자 조합하기
	public static String combineString(String... str){
        StringBuffer sb = new StringBuffer();
        for( int i=0; i<str.length; i++ ) {
            sb.append(str[i]);
        }
        return sb.toString();
    }

}
