package org.eclipse.leshan.server.onem2m.handler;

import org.eclipse.leshan.server.Constants;
import org.eclipse.leshan.server.onem2m.HttpOneM2MOperation;
import org.json.JSONObject;

public class CommandReceiver {
	
	private HttpOneM2MOperation op = new HttpOneM2MOperation();
	
	private JSONObject result;
	
	public void init(HttpOneM2MOperation op){
		this.op = op;
	}
	
	public JSONObject receive(){
		try{
			System.out.println("ReceiveThread starts");
			
			op.init(Constants.BASIC_AE_NAME, "pch_srv", "pcu");
			String response = op.retrievePCU(Constants.POLLING_CHANNEL);
			CommandController cc = new CommandController(response);
			result = cc.handle();
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
		
		return result;
	}
}
