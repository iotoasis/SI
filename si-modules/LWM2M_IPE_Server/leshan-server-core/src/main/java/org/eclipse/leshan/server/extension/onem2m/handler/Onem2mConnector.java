package org.eclipse.leshan.server.extension.onem2m.handler;

import org.eclipse.leshan.server.extension.Lwm2mVO;

public class Onem2mConnector implements Runnable{
	
	private Lwm2mVO vo = null;
	public Onem2mConnector(Lwm2mVO vo) {
		this.vo = vo;
	}

	private Onem2mOperator onem2mOperator = new Onem2mOperator();
	
	@Override
	public void run() {
		
		System.out.println("## Check AE exists.");
		if( !onem2mOperator.isAEExist() ){
			System.out.println("## AE doesn't exist.");
			System.out.println("## Create new repository.");
			onem2mOperator.createRepository(vo);
        } else {
        	System.out.println("## AE exists.");
        }
		
		if( onem2mOperator.isAEExist() ){
			
			System.out.println("## Start subscribe.");
			Runnable rSubscribe = new TSubscribe(vo);
			Thread tSubscribe = new Thread(rSubscribe);
			tSubscribe.start();

			System.out.println("## Start report.");
			Runnable rReport = new TReport(vo);
			Thread tReport = new Thread(rReport);
			tReport.start();
        }
		
		
	}
}
