package net.herit.business.api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.net.*;

import net.herit.business.api.service.*;
import net.herit.common.conf.HeritProperties;
import net.herit.common.dataaccess.*;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.*;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("OpenApiService")
public class OpenApiService {
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";
	
	private String dmHost = "";
	private int dmPort = 8888;
	
	public OpenApiService() {
		dmHost = HeritProperties.getProperty("Globals.dmServerHost");
		dmPort = Integer.parseInt(HeritProperties.getProperty("Globals.dmServerPort"));
		//dmHost = "10.101.101.107";
		//dmPort = 8888;
	}
	
	public HashMap<String, Object> execute(String operation, String content) 
			throws JsonGenerationException, JsonMappingException, IOException, UserSysException
	{	
		HashMap<String, Object> res = callOpenAPI(operation, content);
		
		
		//try {
			//int status = (Integer)res.get("status");
			String body = (String)res.get("body");
			ObjectMapper mapper = new ObjectMapper();
			Object json = mapper.readValue(body, Object.class);
			res.put("json", json);
		//} catch (JsonGenerationException ex) {
		//	res.put("exception", ex);
		//} catch (JsonMappingException ex) {
		//	res.put("exception", ex);
		//} catch (IOException ex) {
		//	res.put("exception", ex);
		//} catch (UserSysException ex) {
			
		//}
		
		return res;
	}
	
	public HashMap<String, Object> callOpenAPI(String operation, String content) 
			throws IOException
	{
		METHOD_NAME = "execute";
		List resultList = null;
		
		HashMap<String, Object> res = new HashMap<String, Object>();

		URL url = new URL(getDmUrl(operation));

		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Content-Length", String.valueOf(content.length()));
		
		conn.setDoOutput(true);
		conn.setDoInput(true);

		OutputStream wr = conn.getOutputStream();
		wr.write(content.getBytes());
		wr.flush();
		wr.close();
		
		int resCode = conn.getResponseCode();
		
		if (resCode == 200) {

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			StringBuffer resBody = new StringBuffer();
		 
			while ((line = in.readLine()) != null) {
				resBody.append(line);
			}
			in.close();
			
			res.put("status", resCode);
			res.put("body", resBody.toString());
		} else {

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			String line;
			StringBuffer resBody = new StringBuffer();
		 
			while ((line = in.readLine()) != null) {
				resBody.append(line);
			}
			in.close();
			
			res.put("status", resCode);
			res.put("body", resBody.toString());
			
		}
			
		return res;
	}
	
	private String getDmUrl(String operation) {
		//return "http://10.101.101.90:8080/dm/"+ operation+".jsp";
		return "http://"+ dmHost +":"+ dmPort +"/hit/openapi/dm/"+ operation;
		//return "http://"+ dmHost +":"+ dmPort +"/"+ operation+".htm";
	}
		
}
