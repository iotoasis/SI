package net.herit.iot.onem2m.incse.context;

import net.herit.iot.onem2m.core.util.LogManager;
import net.herit.iot.onem2m.incse.RestHandler;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.NseManager;

public class RestContext {
	
	private LogManager logManager;
	private DatabaseManager dbManager;
	private RestHandler restHandler;
	
	public RestContext(RestHandler restHandler) {
		this.logManager = LogManager.getInstacne();
		this.dbManager = DatabaseManager.getInstance();
		this.restHandler = restHandler;
	}
	
	public LogManager getLogManager() {
		return this.logManager;
	}
	
	public DatabaseManager getDatabaseManager() {
		return this.dbManager;
	}
	
	public RestHandler getRestHandler() {
		return this.restHandler;
	}
	
}