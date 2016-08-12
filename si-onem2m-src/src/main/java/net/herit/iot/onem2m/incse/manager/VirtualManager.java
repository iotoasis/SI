package net.herit.iot.onem2m.incse.manager;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VirtualManager {
	
	private Logger log = LoggerFactory.getLogger(VirtualManager.class);

	private static VirtualManager INSTANCE = null;

	public enum PROCESS_RESULT { 
		COMPLETED(1, "COMPLETED"),
		PROCESSING(2, "PROCESSING"),
		PROCESSED(3, "PROCESSED");
	
		final int value;
		final String name;
		private PROCESS_RESULT(int value, String name) {
			this.value = value;
			this.name = name;
		}
		
		public int Value() {
			return this.value;
		}
		
		public String Name() {
			return this.name;
		}
	
		private static final Map<Integer, PROCESS_RESULT> map = 
				new HashMap<Integer, PROCESS_RESULT>();
		static {
			for(PROCESS_RESULT en : PROCESS_RESULT.values()) {
				map.put(en.value, en);
			}
		}
		
		public static PROCESS_RESULT get(int format) {
			PROCESS_RESULT en = map.get(format);
			return en;
		}	
	};
	private HashMap<String, VirtualManagerInterface> managerMap = new HashMap<String, VirtualManagerInterface>();
	
	private VirtualManager() {	}
	
	public void initialize() {
		
		this.addManager(VLatestManager.KEY, VLatestManager.getInstance());
		this.addManager(VOldestManager.KEY, VOldestManager.getInstance());
//		this.addManager(VFanOutPointManager.KEY+"/"+VLatestManager.KEY, VFanOutPointManager.getInstance());
//		this.addManager(VFanOutPointManager.KEY+"/"+VOldestManager.KEY, VFanOutPointManager.getInstance());
		this.addManager(VFanOutPointManager.KEY, VFanOutPointManager.getInstance());
		this.addManager(VPollingChannelUriManager.KEY, VPollingChannelUriManager.getInstance());
		
	}
	
	public static VirtualManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new VirtualManager();
			INSTANCE.initialize();
		}
		
		return INSTANCE;
	}	
	
	public VirtualManagerInterface getManager(String to) {
		VirtualManagerInterface vm = null;
		String curTo = to.indexOf("?") >= 0 ? to.substring(0, to.indexOf("?")) : to;
		while (true) {
			String name = curTo.substring(curTo.lastIndexOf("/")+1);

			VirtualManagerInterface cur = managerMap.get(name);
			if (cur != null) {
				vm = cur;
				curTo = curTo.substring(0, curTo.length()-name.length()-1);
				continue;
			}
			break;
		}
		return vm;
	}
	
	protected void addManager(String key, VirtualManagerInterface manager) {
		managerMap.put(key,  manager);
	}
}
