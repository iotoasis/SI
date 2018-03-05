package net.herit.iot.onem2m.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class Test {
	
	private HashMap<String, Timer> timerMap = new HashMap<String, Timer>();
	
	public Test() {}
	
	public void add(String key) {
		Timer timer = new Timer(key);
		timer.schedule(new TimeoutTask(key), 5000);
		timerMap.put(key, timer);
	}
	
	
	public static void main(String[] args) throws Exception {
		//new Test().add("test");
		
//		Class<?> cls = Class.forName("net.herit.iot.onem2m.util.Test");
//		Constructor<?> constructor = cls.getConstructors()[0];
//		Test test = (Test)constructor.newInstance(null);

		Test test = (Test)Class.forName("net.herit.iot.onem2m.util.Test").getConstructors()[0].newInstance();
	
		test.add("test");
	}
	
	
	private class TimeoutTask extends TimerTask {
		
		private String key;
		
		public TimeoutTask(String key) {
			this.key = key;
		}

		@Override
		public void run() {
			
//			Object a = null;
//			key = (String)a;
			
			System.out.println("key=" + key);
			Timer timer = timerMap.remove(key);
			if(timer != null) {
				System.out.println("timer is not null");
			}
			System.out.println("timeMap size=" + timerMap.size());
		}
		
	}
}
