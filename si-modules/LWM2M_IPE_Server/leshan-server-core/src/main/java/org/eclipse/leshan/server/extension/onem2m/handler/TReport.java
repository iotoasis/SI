package org.eclipse.leshan.server.extension.onem2m.handler;

import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.extension.Constants;
import org.eclipse.leshan.server.extension.HttpOperator;
import org.eclipse.leshan.server.extension.Lwm2mVO;
import org.json.JSONException;
import org.json.JSONObject;

public class TReport extends TimerTask implements Runnable{
	
	private Lwm2mVO vo = null;
	public TReport(Lwm2mVO vo){
		this.vo = vo;
	}
	
	private HttpOperator httpOperator = new HttpOperator();
	private Onem2mOperator onem2mOperator = new Onem2mOperator();

	@Override
	public void run() {
		
		StringBuffer baseUri = new StringBuffer("http://");
		baseUri.append(Lwm2mServerConfig.getInstance().getIpeIp()).append(":");
		baseUri.append(Lwm2mServerConfig.getInstance().getIpePortWeb()).append("/api/clients/");
		baseUri.append(vo.getAuthId());
		JSONObject result = null;
		
		for(int i=0; i<Constants.RESOURCE.length; i++){
			
			try {
				if(Constants.RESOURCE[i][0].equals("report")){
					StringBuffer sbUri = new StringBuffer(baseUri);
					sbUri.append(Constants.RESOURCE[i][2]);
					
					HttpURLConnection conn = httpOperator.sendGet(sbUri.toString());
					result = httpOperator.getResponse(conn);
					result.getJSONObject("content").put("id", Constants.RESOURCE[i][1]);
					System.out.println(result);
					
					onem2mOperator.createContentInstance(Constants.RESOURCE[i][1], result);
				}
			} catch (JSONException e) {
				System.out.println(e.getMessage());
				System.out.println(result);
				if(Lwm2mServerConfig.getInstance().isDebug()){
					e.printStackTrace();
				}
			}
		}
		
		TimerTask tReportTask = new TReport(vo);
		Timer tReport = new Timer();
		tReport.schedule(tReportTask, Lwm2mServerConfig.getInstance().getReportInterval());
	}

}
