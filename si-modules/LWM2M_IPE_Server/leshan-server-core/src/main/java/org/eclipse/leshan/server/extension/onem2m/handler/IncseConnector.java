package org.eclipse.leshan.server.extension.onem2m.handler;

public class IncseConnector implements Runnable{
	
	private String authId = null;
	public IncseConnector(String authId) {
		this.authId = authId;
	}

	private IncseOperator incseOperator = new IncseOperator();
	
	@Override
	public void run() {
		
		System.out.println("## Check AE exists.");
		if( !incseOperator.AeExist() ){
			System.out.println("## AE doesn't exist.");
			System.out.println("## Create new repository.");
			incseOperator.createRepository(authId);
        } else {
        	System.out.println("## AE exists.");
        }
		
		if( incseOperator.AeExist() ){
			System.out.println("## start subscribe");
			Subscriber.getInstance().subscribe();
        }
	}
}
