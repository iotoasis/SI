package org.eclipse.leshan.server.onem2m.handler;

import org.eclipse.leshan.server.api.Util;
import org.eclipse.leshan.util.Base64;
import org.json.JSONObject;

public class CommandController {

	private JSONObject con;
	private String response;
	public CommandController(String response){
		this.response = response;
	}
	
	public JSONObject handle(){
		
		// con 내용 JSON 형태로 변경
		// null인지 확인
		if( Util.isNoE(response) ){
			System.err.println("response is null");
		}
		
		if( Util.isNoE(response) ){
			System.out.println("NULL");
		} else if( response.indexOf("RESPONSE-CODE") > -1 ) {	// RESPONSE-CODE 있는지 확인
			System.out.println("NOT REACHABLE : "+response);
		} else if( response.indexOf("<con>") > -1 && response.indexOf("</con>") > -1 ) {	// con 태그가 존재하는지 확인
			try{
				byte[] decoded = Base64.decodeBase64(Util.getValue(response, "con"));
				String strCon = new String(decoded);
				con = new JSONObject(strCon);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		// operation, value, uri로 구분
		Item item = new Item();
		JSONObject result = null;
		try{
			if( !con.isNull("operation") ){
				System.out.println(con.getString("operation"));
				item.setOperation(con.getString("operation"));
			}
			if( !con.isNull("resourceUri") ){
				System.out.println(con.getString("resourceUri"));
				item.setUri(con.getString("resourceUri"));
			}
			if( !con.isNull("sv") ){
				System.out.println(con.getString("sv"));
				item.setValue(con.getString("sv"));
			}
			if( !con.isNull("authId") ){
				System.out.println(con.getString("authId"));
				item.setAuthId(con.getString("authId"));
			}
			if( !con.isNull("displayName") ){
				System.out.println(con.getString("displayName"));
				item.setDisplayName(con.getString("displayName"));
			}
			
			JSONObject resp = null;
			
			//*
			// 제어 명령과 수집 시작/종료 명령 구분
			OrderExecutor oe = new OrderExecutor(item);
			oe.execute();
			//*/
			
		} catch(Exception e) {}
		
		return result;
	}
}