package org.eclipse.leshan.server.extension;

public class TaskTimer implements Runnable{

	@Override
	public void run() {
		int count = 0;
		while(true){
			System.out.println(count+" sec");
			try {
				Thread.sleep(1000);
				count += 1;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
