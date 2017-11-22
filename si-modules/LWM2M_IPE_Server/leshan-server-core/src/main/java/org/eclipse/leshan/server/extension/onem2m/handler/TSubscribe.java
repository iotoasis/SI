package org.eclipse.leshan.server.extension.onem2m.handler;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.extension.Lwm2mVO;
import org.json.JSONObject;

public class TSubscribe extends TimerTask implements Runnable{

	private Lwm2mVO vo = null;
	public TSubscribe(Lwm2mVO vo){
		this.vo = vo;
	}
	
	@Override
	public void run() {
		
		Onem2mOperator onem2mOperator = new Onem2mOperator();
		
		vo.setCurrCount(vo.getCurrCount()+1);
		int currCount = vo.getCurrCount();
		
		System.out.println();
		System.out.println();
		System.out.println("[ "+String.format("%15s", vo.getAuthId())+" ][ "+String.format("%4d", currCount)+" ] LongPolling START");
		
		try {
			// longPolling
			String response = onem2mOperator.retrievePCU();
			System.out.println(response);
			
			// exception
			if( response.indexOf("RESPONSE-CODE") > -1 && !new JSONObject(response).getString("RESPONSE-CODE").equals("408") ){
				throw new Exception("[ERROR] LongPolling");
			}
			
			// 디바이스 제어
			Runnable runnable = new TDeviceControl(response, currCount);
			Thread dct = new Thread(runnable);
			dct.start();
			
			// subscribe
			Runnable rSubscribe = new TSubscribe(vo);
			Thread tSubscribe = new Thread(rSubscribe);
			tSubscribe.start();

		} catch (Exception e) {
			System.err.println(e.getMessage());
			if(Lwm2mServerConfig.getInstance().isDebug()){
				e.printStackTrace();
			}
			// 대기 후 재시도
			TimerTask tSubscribeTask = new TSubscribe(vo);
			Timer tSubscribe = new Timer();
			tSubscribe.schedule(tSubscribeTask, Lwm2mServerConfig.getInstance().getRetryInterval());
		} finally {
			
			System.out.println("[ "+String.format("%15s", vo.getAuthId())+" ][ "+String.format("%4d", currCount)+" ] LongPolling END");
		}
		
	}
	
}
