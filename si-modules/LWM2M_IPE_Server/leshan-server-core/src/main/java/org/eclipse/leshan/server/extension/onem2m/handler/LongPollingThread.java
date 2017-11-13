package org.eclipse.leshan.server.extension.onem2m.handler;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.extension.Constants;
import org.eclipse.leshan.server.extension.onem2m.HttpOneM2MOperation;

public class LongPollingThread extends TimerTask implements Runnable{

	private int number = 0;
	
	public LongPollingThread(int number){
		this.number = number;
	}
	
	@Override
	public void run() {
		
		System.out.println();
		System.out.println();
		System.out.println("["+String.format("%6d", number)+"] LongPolling START");
		
		HttpOneM2MOperation op = new HttpOneM2MOperation();
		try {
			op.init(Constants.BASIC_AE_NAME, "pch_srv", "pcu");
			String response = op.retrievePCU(Constants.POLLING_CHANNEL);
			System.out.println(response);
			
			if( response.indexOf("RESPONSE-CODE") > -1 ){
				throw new Exception("[ERROR] LongPolling");
			}

			Runnable runnable = new DeviceControlThread(response, number);
			Thread dct = new Thread(runnable);
			dct.start();
			
			Subscriber.getInstance().subscribe();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			if(Lwm2mServerConfig.getInstance().isDebug()){
				e.printStackTrace();
			}
			Timer timer = new Timer();
			timer.schedule(Subscriber.getInstance().getTask(), Lwm2mServerConfig.getInstance().getRetryInterval());
			/*
			try {
				Thread.sleep(Lwm2mServerConfig.getInstance().getRetryInterval());
				ThreadHandler.getInstance().next();
			} catch (InterruptedException e1) {
				System.err.println(e1.getMessage());
				if(Lwm2mServerConfig.getInstance().isDebug()){
					e1.printStackTrace();
				}
			}
			*/
		}
		System.out.println("["+String.format("%6d", number)+"] LongPolling END");
		
	}
	
}
