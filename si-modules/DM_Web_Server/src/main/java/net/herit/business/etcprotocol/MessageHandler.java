package net.herit.business.etcprotocol;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import net.herit.business.etcprotocol.constant.Errors;
import net.herit.business.lwm2m.exception.JsonFormatException;

public class MessageHandler {
	
	//singleton
	private static MessageHandler instance = null;
	public static MessageHandler getInstance(){
		if(instance == null){
			instance = new MessageHandler();
		}
		return instance;
	}
	
	// getByJSON
	public JSONObject jsonReceiver(HttpServletRequest request) throws JsonFormatException{
		InputStream is;
		String token;
		JSONObject result = null;
		
		try{
			is = request.getInputStream();
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] byteBuffer = new byte[1024];
			byte[] byteData;
			int nLength = 0;
			while((nLength=is.read(byteBuffer,0,byteBuffer.length)) != -1){
				baos.write(byteBuffer,0,nLength);
			}
			byteData = baos.toByteArray();
			token = new String(byteData);
			result = new JSONObject(token);
			
		} catch(Exception e) {
			throw new JsonFormatException();
		}
		System.out.println(result);
		return result;
	}
	
	
	/** set response **/
	public JSONObject setResponse(Errors caseOfResult, String msg) {
		JSONObject response = new JSONObject();
		try{
			response.put("RESPONSE-CODE", caseOfResult.name());
			
			switch(caseOfResult){
			// error
			case ERR_500:
				response.put("result", "Failed");
				break;
			// success
			case ERR_001:
				response.put("result", "Connected");
				break;
			case ERR_000:
				response.put("result", "Disconnected");
				break;
			}
			response.put("reason", caseOfResult.getMsg());			
			if(!Util.getInstance().isNOE(msg)){
				response.put("msg", msg);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return response;
	}
}
