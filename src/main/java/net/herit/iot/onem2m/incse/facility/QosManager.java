package net.herit.iot.onem2m.incse.facility;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.db.mongo.MongoPool;
import net.herit.iot.onem2m.incse.InCse;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager.CSEQoSConfig;
import net.herit.iot.onem2m.incse.manager.dao.AEDAO;
import net.herit.iot.onem2m.incse.manager.dao.RemoteCSEDAO;
import net.herit.iot.onem2m.incse.manager.dao.SeqNumDAO;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class QosManager {
	
	private static QosManager INSTANCE = new QosManager();
	
	private AEDAO aeDao;
	private RemoteCSEDAO cseDao;

	private int maxAeNo;
	private int maxCseNo;
	private Integer aeNo;
	private Integer cseNo;
	
	private Map<String, TpsInfo> cseTpsMap = new HashMap<String, TpsInfo>();

	private Logger log = LoggerFactory.getLogger(QosManager.class);
	
	public class TpsInfo {
		private int maxTps = -1;
		private int curTps = -1;
		private long refTime = -1;	// reference time, curTps should be reset every 10 seconds
		public TpsInfo(int maxTps) { this.maxTps = maxTps; this.curTps = 0; this.refTime = 0; }
		public int getMaxTps() { return maxTps; }
		public int getCurTps() { return curTps; }
		public long getRefTime() { return refTime; }
		public void setMaxTps(int val) { maxTps = val; }
		public void setCurTps(int val) { curTps = val; }
		public void setRefTime(long val) { refTime = val; }
	};
	
	private QosManager() {}
	
	public static QosManager getInstance() {
		return INSTANCE;
	}
	
	public void initialize(OneM2mContext context) {
		
		try {
			
			log.info("QoS initialization started!!!");
			
			aeDao = new AEDAO(context);
			cseDao = new RemoteCSEDAO(context);
			
			aeNo = aeDao.getCount();
			cseNo = cseDao.getCount();
			
			CfgManager cfg = CfgManager.getInstance();
			
			maxAeNo = cfg.getQOSMaxAENo();
			maxCseNo = cfg.getQOSMaxCSENo();
			
			Map<String, CSEQoSConfig> cseQosMap = cfg.getCSEQoSConfigMap();
			
			for (Map.Entry<String, CSEQoSConfig> entry : cseQosMap.entrySet()) {
				String cseId = entry.getKey();
				CSEQoSConfig cseQosCfg = entry.getValue();
				
				cseTpsMap.put(cseId, new TpsInfo(cseQosCfg.getCseMaxTps()));
				log.info("MaxTps of "+ cseId +":"+ cseQosCfg.getCseMaxTps());
			}			
			
			
			log.info("AE- Max:{}, Current:{}", maxAeNo, aeNo);
			log.info("CSE- Max:{}, Current:{}", maxCseNo, cseNo);
			
		} catch (Exception e) {

			log.error("Exception during QoS initialization!!!", e);
			
		} finally {

			log.info("QoS initialization ended!!!");	
		}
		
						
	}

	public int getMaxAENumber() {
		return CfgManager.getInstance().getQOSMaxAENo();
	}
	public int getCurrentAENumber() {
		return aeNo;
	}

	public boolean checkNAddAENumber(int no) {
		
		synchronized (aeNo) {	
			if (maxAeNo == -1) {
				return true;
			}
			
			boolean ret = false;
			if (aeNo + no <= maxAeNo) {
				aeNo = aeNo + no;
				ret = true;
			} else {
				log.debug("AE number exceeded (Max:{}, Current:{})", maxAeNo, aeNo);
			}
			return ret;
		}
		
	}
	
	public int getMaxCSENumber() {
		return CfgManager.getInstance().getQOSMaxCSENo();
	}
	
	public int getCurrentCSENumber() {
		return cseNo;
	}



	public boolean checkNAddCSENumber(int no) {
		
		synchronized (cseNo) {
			if (maxCseNo == -1) {
				return true;
			}
			
			boolean ret = false;
			if (cseNo + no <= CfgManager.getInstance().getQOSMaxCSENo()) {
				cseNo = cseNo + no;
				ret = true;
			} else {
				log.debug("remoteCSE number exceeded (Max:{}, Current:{})", maxCseNo, cseNo);
			}
		
			return ret;
		}
		
	}

	public void reduceCSENumber(int no) {
		
		synchronized (cseNo) {
			cseNo -= no;
		}
	}

	public void reduceAENumber(int no) {

		synchronized (aeNo) {
			aeNo -= no;
			
		}
		
	}
	
	public TpsInfo getTpsInfo(String cseId) {
		return cseTpsMap.get(cseId);
	}
	
	public boolean checkCseTps(String cseId) {
		
		log.debug("checkCseTps:"+cseId);
		TpsInfo info = cseTpsMap.get(cseId);
		if (info == null) {
			return true;
		}
		
		synchronized (info) {
			int timeUnit = 10;
			long refTime = info.getRefTime();
			long curSec = System.currentTimeMillis() / 1000L;
			if (curSec/timeUnit == refTime/timeUnit) {
				info.setCurTps(info.getCurTps()+1);
				return info.getMaxTps()*timeUnit >= info.getCurTps(); 
			} else {
				info.setRefTime((curSec/timeUnit)*timeUnit);
				info.setCurTps(1);
				return true;
			}
			
		}
		
	}
	
}
