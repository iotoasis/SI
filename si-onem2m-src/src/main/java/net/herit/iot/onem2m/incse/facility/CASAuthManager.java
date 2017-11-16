package net.herit.iot.onem2m.incse.facility;

import org.bson.Document;

import net.herit.iot.onem2m.incse.context.RestContext;
import net.herit.iot.onem2m.incse.manager.dao.CASAuthDAO;


public class CASAuthManager {
	
	private CASAuthDAO dao;
	
	public void initialize(RestContext context) {
		
		dao = new CASAuthDAO(context);
		
	}
	
	public Document getAuth(String devId) {
		return dao.getAuthInfo(devId);
	}
	
	public boolean regAuth(String devId) {
		return dao.regAuthInfo(devId);
	}
	
	public boolean updateAuth(String devId, String pwd) {
		return dao.updateAuthInfo(devId, pwd);
	}
	
	public void deleteAuth(String devId) {
		dao.deleteAuthInfo(devId);
	}
	
	
}
