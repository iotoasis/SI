package net.herit.iot.onem2m.core.util;

import org.slf4j.Logger;

public class LogManager {

	private final static LogManager INSTANCE = new LogManager();
	
	private Logger log = null;
	private Logger clog = null;		// Call Log
	
	private LogManager() {
		
	}
	
	public static LogManager getInstacne() {
		return INSTANCE;
	}
	
	public void initialize(Logger logger, Logger callLogger) {
		this.log = logger;
		this.clog = callLogger;
	}
	
	public Logger getLogger() {
		return log;
	}
	
	public Logger getCallLogger() {
		return clog;
	}
	
	public void debug(String debug) {
		
		if(log == null) {
			System.out.println("DEBUG] " + debug);
			return;
		}
		
		if(log.isDebugEnabled()) {
			log.debug(debug);
		}
	}
	
	public void info(String info) {
		
		if(log == null) {
			System.out.println("INFO] " + info);
			return;
		}
		
		if(log.isInfoEnabled()) {
			log.info(info);
		}
	}
	
	public void warn(String warn) {
		
		if(log == null) {
			System.out.println("WARN] " + warn);
			return;
		}
		
		if(log.isWarnEnabled()) {
			log.warn(warn);
		}
	}
	
	public void error(String error) {
		
		if(log == null) {
			System.out.println("ERROR] " + error);
			return;
		}
		
		if(log.isErrorEnabled()) {
			log.error(error);
		}
	}
}
