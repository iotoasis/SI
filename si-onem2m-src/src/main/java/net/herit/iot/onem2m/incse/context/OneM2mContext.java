package net.herit.iot.onem2m.incse.context;

import net.herit.iot.onem2m.core.util.LogManager;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.NseManager;

public class OneM2mContext {
	
	private LogManager logManager;
	private DatabaseManager dbManager;
	private NseManager nseManager;
	
	public OneM2mContext(NseManager nseManager) {
		this.logManager = LogManager.getInstacne();
		this.dbManager = DatabaseManager.getInstance();
		this.nseManager = nseManager;
	}
	
	public LogManager getLogManager() {
		return this.logManager;
	}
	
	public DatabaseManager getDatabaseManager() {
		return this.dbManager;
	}
	
	public NseManager getNseManager() {
		return this.nseManager;
	}
	
}