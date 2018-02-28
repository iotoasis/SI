package org.eclipse.leshan.server.extension.onem2m.handler;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.extension.Util;
import org.eclipse.leshan.util.Base64;
import org.json.JSONObject;

public class TDeviceControl implements Runnable{

	private JSONObject item = null;
	private String response = null;
	private int number = 0;
	public TDeviceControl(String response, int number){
		this.response = response;
		this.number = number;
	}
	
	@Override
	public void run() {

		try{
			if( Util.isNoE(response) ){
				throw new Exception("response is null");
			} else if( response.indexOf("RESPONSE-CODE") > -1 && !(new JSONObject(response)).getString("RESPONSE-CODE").equals("408") ) {	// RESPONSE-CODE 있는지 확인
				throw new Exception("NOT REACHABLE : "+response);
			} else if( response.indexOf("<con>") > -1 && response.indexOf("</con>") > -1 ) {	// con 태그가 존재하는지 확인
				String[] cnf = Util.getValue(response, "cnf").split(":");
				String contentType = cnf[0];
				String contentEncoded = cnf[1];
				
				if( contentType.equals("application/json") ){
					if( contentEncoded.equals("1") ){
						byte[] decoded = Base64.decodeBase64(Util.getValue(response, "con"));
						String strCon = new String(decoded);
						item = new JSONObject(strCon);
						
						Executor exe = new Executor(item, number);
						JSONObject result = new JSONObject(exe.execute());
						System.out.println(result);
						if(result.isNull("status")){
							throw new Exception("Cannot confirm status.");
						} else {
							if(result.getString("status").equals("CHANGED")){
								Util.printJSONObject(item);
								// exe.execute() 결과 받은 처리 작성 할 부분 ex) {"status":"CHANGED"}
								Onem2mOperator incseOperator = new Onem2mOperator();
								incseOperator.createContentInstance(null, item);
								//incseOperator.createContentInstanceResult(item);
							} else {
								System.out.println("## execute() result = "+result);
							}
						}
					} else {
						throw new Exception("Content to encode with Base64 is mandatory.");
					}
				} else {
					throw new Exception("Do not compatible with "+contentType);
				}
			}
		} catch(Exception e) {
			System.err.println(e.getMessage());
			if(Lwm2mServerConfig.getInstance().isDebug()){
				e.printStackTrace();
			}
		}
	}
	
}
