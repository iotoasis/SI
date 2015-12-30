package net.herit.iot.onem2m.incse.facility;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.manager.dao.SeqNumDAO;

public class SeqNumManager {

	private static Map<String, AtomicInteger> sequenceMap;
	
	private static SeqNumManager INSTANCE;
	
	private SeqNumDAO dao;
	
	public String GLOBAL_SEQUENCE = "GLOBAL_SEQUENCE"; 
	
	private SeqNumManager() {
		
 		sequenceMap = new HashMap<String, AtomicInteger>();
 		
	}
	
	public void initialize(OneM2mContext context) {
		
		dao = new SeqNumDAO(context);
		Map<String, Integer> map = dao.getAllSequenceNumber();
		
		Set<String> keys = map.keySet();
		for (String key: keys) {
			sequenceMap.put(key, new AtomicInteger(map.get(key)));
		}
		
	}
	
	public static SeqNumManager getInstance() {
		
		return INSTANCE == null ? INSTANCE = new SeqNumManager() : INSTANCE;
		
	}
	
	public int get(String name) {

		synchronized (this) {
			
			AtomicInteger seqNum = sequenceMap.get(name);
			if (seqNum == null) {

				sequenceMap.put(name, new AtomicInteger(0));
				dao.setSequence(name, 0);
				return 0;
				
			} else {
				
				int num = seqNum.addAndGet(1);
				dao.setSequence(name,  num);
				return num;
				
			}
			
		}
	}
}
