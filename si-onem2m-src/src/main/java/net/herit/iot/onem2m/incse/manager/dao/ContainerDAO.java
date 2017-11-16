package net.herit.iot.onem2m.incse.manager.dao;


import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;

import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.DaoJSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.AnnounceableResource;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContainerAnnc;
import net.herit.iot.onem2m.resource.Resource;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ContainerDAO extends ResourceDAO implements DAOInterface {
	
	private Logger log = LoggerFactory.getLogger(ContainerDAO.class);

	public ContainerDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			DaoJSONConvertor<Container> jc = (DaoJSONConvertor<Container>)ConvertorFactory.getDaoJSONConvertor(Container.class, Container.SCHEMA_LOCATION);
			return jc.marshal((Container)res);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}

	@Override
	public void create(Resource resource) throws OneM2MException {
		
		((Container)resource).setCurrentNrOfInstances(0);
		((Container)resource).setCurrentByteSize(0);
		((Container)resource).setStateTag(0);
		
		// added in 2017-03-08
		if(((Container)resource).getMaxNrOfInstances() == null) {
			((Container)resource).setMaxNrOfInstances(CfgManager.getInstance().getMaxCIPerContainer());
		}
		// added in 2017-03-08
		if(((Container)resource).getMaxInstanceAge() == null) {
			((Container)resource).setMaxInstanceAge(CfgManager.getInstance().getMaxCIAgePerContainer());
		}
		// added in 2017-03-08
		if(((Container)resource).getMaxByteSize() == null) {
			((Container)resource).setMaxByteSize(CfgManager.getInstance().getMaxCIByteSizePerContainer());
		}
		
		log.debug("container.getMaxNrOfInstances ==>" + ((Container)resource).getMaxNrOfInstances());
		
		super.create(resource);
		
//		Container res = (Container)resource;
//
//		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(COLLECTION_NAME);
//		Document doc = new Document("creator", res.getCreator())
//		    .append("maxNrOfInstances", res.getMaxNrOfInstances())
//		    .append("maxByteSize", res.getMaxByteSize())
//		    .append("maxInstanceAge", res.getMaxInstanceAge().intValue())
//		    //.append("currentNrOfInstances", res.getCurrentNrOfInstances())
//		    //.append("currentByteSize", res.getCurrentByteSize())
//		    .append("currentNrOfInstances", 0)
//		    .append("currentByteSize", 0)
//		    .append("locationID", res.getLocationID())
//		    .append("ontologyRef", res.getOntologyRef());
//		
//		this.appendAnnounceableAttributes(doc, res);
//
//		collection.insertOne(doc);
		
	}

	@Override
	public void update(Resource resource) throws OneM2MException {

		((Container)resource).setStateTag(((Container)resource).getStateTag() + 1);
		
		super.update(resource);			
	}

//	@Override
//	public Resource retrieveByUri(String uri, RESULT_CONT rc) throws OneM2MException {
//		
//		return this.retrieve(URI_KEY, uri, new JSONConvertor<Container>(Container.class), rc);
//		
//	}
//
//	@Override
//	public Resource retrieveByResId(String id, RESULT_CONT rc) throws OneM2MException {
//		
//		return this.retrieve("resourceID", id, new JSONConvertor<Container>(Container.class), rc);
//		
//	}
	
//	public Resource retrieve(String name, String value) throws OneM2MException {
//		
//		try {
//
//			MongoCollection<Document> collection = context.getDatabaseManager().getCollection("resEntity");
//			Document doc = collection.find(new BasicDBObject(name, value)).first();
//			
//			Container res = new Container();
//			this.setAnnouncableAttributes((AnnounceableResource)res, doc);
//			
//			res.setCreator((String)doc.get("creator"));
//			res.setMaxNrOfInstances((int)doc.get("maxNrOfInstances"));
//			res.setMaxByteSize((int)doc.get("maxByteSize"));
//			res.setMaxInstanceAge((int)doc.get("maxInstanceAge"));
//			res.setCurrentNrOfInstances((int)doc.get("currentNrOfInstances"));
//			res.setCurrentByteSize((int)doc.get("currentByteSize"));
//			res.setLocationID((String)doc.get("locationID"));
//			res.setOntologyRef((String)doc.get("ontologyRef"));
//			
//			return res;
//			
//		} catch (Exception e) {
//			log.debug("Handled exception", e);
//			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Fail to retrieve Container");
//		}
//	}

//	@Override
//	public void deleteByUri(String uri) throws OneM2MException {
//		
//		String resourceID = (String)this.getAttribute(URI_KEY,  uri, RESID_KEY);		
//
//		deleteDocument(PARENTID_KEY, resourceID);
//		
//		deleteDocument(URI_KEY, uri);
//		
//		
//	}
//
//	@Override
//	public void deleteByResId(String resId) throws OneM2MException {	
//
//		deleteDocument(RESID_KEY, resId);
//		
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
		
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, 
				(DaoJSONConvertor<Container>)ConvertorFactory.getDaoJSONConvertor(Container.class, Container.SCHEMA_LOCATION), rc);
		
	}
	
}
