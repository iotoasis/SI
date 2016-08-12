package net.herit.iot.onem2m.incse.manager.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.client.MongoCollection;

import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Node;
import net.herit.iot.onem2m.resource.PollingChannel;
import net.herit.iot.onem2m.resource.Resource;

public class PollingChannelDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(PollingChannelDAO.class);

	public PollingChannelDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			JSONConvertor<PollingChannel> jc = (JSONConvertor<PollingChannel>)ConvertorFactory.getJSONConvertor(PollingChannel.class, PollingChannel.SCHEMA_LOCATION);
			return jc.marshal((PollingChannel)res);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}

	@Override
	public void create(Resource resource) throws OneM2MException {
		
		super.create(resource);
//		
//		PollingChannel res = (PollingChannel)resource;
//
//		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(COLLECTION_NAME);
//		
//		context.getLogManager().debug("resourceName: " + res.getResourceName() + ", resourceID: " + res.getResourceID());
//		
//		JSONConvertor<PollingChannel> jc = new JSONConvertor<PollingChannel>(PollingChannel.class);
//		String strJson;
//		try {
//			strJson = jc.marshal(res);
//		} catch (Exception e) {
//			log.debug("Handled exception", e);
//			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Exception in marshal:"+res.toString());
//		}
//		context.getLogManager().debug("PollingChannel json: " + strJson);
//
//		Document doc = Document.parse(strJson);
//		
//		collection.insertOne(doc);
		
	}

	@Override
	public void update(Resource resource) throws OneM2MException {
		
		super.update(resource);
		
//		AE ae = (AE)resource;
//
//		HashMap<String, Object> map = new HashMap<String, Object>();		
//		
//		for (Field f: ae.getClass().getDeclaredFields()) {
//			f.getName();
//			Object val;
//			try {
//				val = f.get(ae);
//				if (val != null) {
//					map.put(f.getName(), val);
//				}
//			} catch (IllegalArgumentException | IllegalAccessException e) {
//				log.debug("Handled exception", e);
//				throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Exception during extract resource field");
//			}
//		}
//		
//	    updateDocument("resourceID", resource.getId(), map);  
		
	}
//
//	@Override
//	public void deleteByUri(String uri) throws OneM2MException {
//		deleteDocument(URI_KEY, uri);
//	}
//
//	@Override
//	public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException {
//		return retrieve(URI_KEY, uri, new JSONConvertor<PollingChannel>(PollingChannel.class), rc);
//	}
//
//	@Override
//	public void deleteByResId(String resId) throws OneM2MException {
//		deleteDocument(RESID_KEY, resId);		
//	}
//
//	@Override
//	public Resource retrieveByResId(String resId, RESULT_CONT rc) throws OneM2MException {
//		return retrieve(RESID_KEY, resId, new JSONConvertor<PollingChannel>(PollingChannel.class), rc);
//	}

	
	@Override
	public void delete(String id) throws OneM2MException {
		
		deleteDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);
		
	}

	@Override
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException {
		
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, 
				ConvertorFactory.getJSONConvertor(PollingChannel.class, PollingChannel.SCHEMA_LOCATION), rc);
		
	}
}
