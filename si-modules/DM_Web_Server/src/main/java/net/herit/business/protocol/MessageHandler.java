package net.herit.business.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

import net.herit.business.lwm2m.exception.JsonFormatException;
import net.herit.business.protocol.constant.Errors;

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
		
		JSONObject result = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		
		try{
			is = request.getInputStream();
			baos = new ByteArrayOutputStream();
			byte[] byteBuffer = new byte[1024];
			byte[] byteData;
			int nLength = 0;
			while((nLength=is.read(byteBuffer,0,byteBuffer.length)) != -1){
				baos.write(byteBuffer,0,nLength);
			}
			byteData = baos.toByteArray();
			result = new JSONObject(new String(byteData));
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(baos != null){
					baos.close();
				}
				if(is != null){
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
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
