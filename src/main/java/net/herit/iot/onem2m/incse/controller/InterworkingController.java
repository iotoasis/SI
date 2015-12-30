package net.herit.iot.onem2m.incse.controller;

import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.CSE_TYPE;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.AEAnncManager;
import net.herit.iot.onem2m.incse.manager.CSEBaseManager;
import net.herit.iot.onem2m.incse.manager.ManagerFactory;
import net.herit.iot.onem2m.incse.manager.RemoteCSEManager;

public class InterworkingController implements Runnable {
	
	private OneM2mContext context;

	private Logger log = LoggerFactory.getLogger(InterworkingController.class);
	
	private int delayStart = 3000;
	
	public InterworkingController(OneM2mContext context) {
		this.context = context;
	}

	public void registerRemoteCSE(List<CfgManager.RemoteCSEInfo> rcList) throws OneM2MException {
		
		if (rcList == null) return;
		
		Iterator<CfgManager.RemoteCSEInfo> it = rcList.iterator();
		
		RemoteCSEManager manager = (RemoteCSEManager)ManagerFactory.create(RESOURCE_TYPE.REMOTE_CSE, this.context);
		while (it.hasNext()) {
			CfgManager.RemoteCSEInfo rcInfo = it.next();
			if (!manager.checkIfRegistered(rcInfo.getCseId(), CSE_TYPE.IN_CSE, this.context)) {
				manager.registerToRemoteCSE(rcInfo.getCseId(), rcInfo.getPointOfAccess(), this.context);
			}
		}
	}

	@Override
	public void run() {
	
		try {
			Thread.sleep(delayStart);
			this.registerRemoteCSE(CfgManager.getInstance().getRemoteCSEList());
		} catch (Exception e) {
			e.printStackTrace();
			log.debug("Exception in InterworkingController.run:"+e.getMessage());
		}	
		
	}
}
