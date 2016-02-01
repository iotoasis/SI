package net.herit.iot.onem2m.incse.manager.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.RemoteCSE;
import net.herit.iot.onem2m.resource.Request;
import net.herit.iot.onem2m.resource.Resource;


public class RequestDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(RequestDAO.class);
	
	public RequestDAO(OneM2mContext context) {
		super(context);
	}
	
	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			JSONConvertor<Request> jc = (JSONConvertor<Request>)ConvertorFactory.getJSONConvertor(Request.class, Request.SCHEMA_LOCATION);
			return jc.marshal((Request)res);
			
		} catch (Exception e) {
			e.printStackTrace();			
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}
	
	@Override
	public void create(Resource resource) throws OneM2MException {
		
		super.create(resource);
		
	}

	@Override
	public void update(Resource resource) throws OneM2MException {
		
		super.update(resource);
		
	}

//	@Override
//	public void deleteByUri(String uri) throws OneM2MException {
//		
//		deleteDocument(URI_KEY, uri);
//		
//	}
//
//	@Override
//	public void deleteByResId(String resId) throws OneM2MException {
//		
//		deleteDocument(RESID_KEY, resId);
//		
//	}
//
//	@Override
//	public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException {
//		
//		return retrieve(URI_KEY, uri, new JSONConvertor<Request>(Request.class), rc);
//			
//	}
//
//	@Override
//	public Resource retrieveByResId(String resId, RESULT_CONT rc) throws OneM2MException {
//		
//		return retrieve(RESID_KEY, resId, new JSONConvertor<Request>(Request.class), rc);
//		
//	}
	
	@Override
	public void delete(String id) throws OneM2MException {
		
		String resourceID = (String)this.getAttribute(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY,  id, RESID_KEY);	

		deleteChild(resourceID);
		
		deleteDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);
		
	}

	@Override
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException {
		
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, ConvertorFactory.getJSONConvertor(Request.class, Request.SCHEMA_LOCATION), rc);
		
	}
}
