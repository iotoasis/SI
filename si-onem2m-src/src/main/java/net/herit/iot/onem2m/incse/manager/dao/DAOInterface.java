package net.herit.iot.onem2m.incse.manager.dao;

import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.resource.Resource;

public interface DAOInterface {
	public void create(Resource resource) throws OneM2MException;
	public void update(Resource resource) throws OneM2MException;

	public void delete(String id) throws OneM2MException;
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException;

	//public void deleteByUri(String uri) throws OneM2MException;
	//public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException;
	
	//public void deleteByResId(String resId) throws OneM2MException;
	//public Resource retrieveByResId(String resId, RESULT_CONT rc) throws OneM2MException;
}
