package org.eclipse.leshan.server.extension.dm.handler;

import java.net.HttpURLConnection;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.extension.HttpOperator;
import org.eclipse.leshan.server.extension.Lwm2mVO;
import org.eclipse.leshan.server.extension.Tokenization;
import org.json.JSONException;
import org.json.JSONObject;

public class DmConnector implements Runnable{

	private Lwm2mVO vo = null;
	public DmConnector(Lwm2mVO vo){
		this.vo = vo;
	}
	
	@Override
	public void run() {
		
		// token and operators
		JSONObject token = null;
		Tokenization tokenization = new Tokenization();
		HttpOperator httpOperator = new HttpOperator();
		HttpURLConnection conn = null;
		
		// string url
		StringBuffer strUrl = new StringBuffer("http://");
		strUrl.append(Lwm2mServerConfig.getInstance().getDmIp()).append(":");
		strUrl.append(Lwm2mServerConfig.getInstance().getDmPort()).append("/hdm/lwm2m/conn.do");
		
		// connect
		try {
			token = tokenization.makeDmToken(vo, "connect");
			conn = httpOperator.sendPost(strUrl.toString(), token);
			JSONObject response = httpOperator.getResponse(conn);
			System.out.println(response);
			
		} catch (JSONException e) {
			System.err.println(e.getMessage());
			if(Lwm2mServerConfig.getInstance().isDebug()){
				e.printStackTrace();
			}
		}

	}

}
