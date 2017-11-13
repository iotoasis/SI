package org.eclipse.leshan.server.extension.onem2m.handler;

import java.util.TimerTask;

public class Subscriber{
	
	private static Subscriber instance = null;
	public static Subscriber getInstance(){
		if(instance == null){
			instance = new Subscriber();
		}
		return instance;
	}

	// task and number of task
	private int currCount = 0;
	private TimerTask task = null;
	
	// task initialize
	private void initTask(){
		currCount += 1;
		task = new LongPollingThread(currCount);
	}
	
	// get task after initialize
	public TimerTask getTask(){
		initTask();
		return task;
	}
	
	// next operation
	public void subscribe() {
		Thread lpt = new Thread(getTask());
		lpt.start();
	}
	
	
}
