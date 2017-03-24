package net.herit.iot.onem2m.incse.manager.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.DaoJSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.RestSemanticController;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.SemanticDescriptor;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class SemanticDescriptorDAO extends ResourceDAO implements DAOInterface {

	private Logger log = LoggerFactory.getLogger(SemanticDescriptorDAO.class);

	public SemanticDescriptorDAO(OneM2mContext context) {	
		super(context);
		
		RestSemanticController.getInstance().initialize(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			DaoJSONConvertor<SemanticDescriptor> jc = (DaoJSONConvertor<SemanticDescriptor>)ConvertorFactory.getDaoJSONConvertor(SemanticDescriptor.class, SemanticDescriptor.SCHEMA_LOCATION);
			return jc.marshal((SemanticDescriptor)res);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}
	
	@Override
	public void create(Resource resource) throws OneM2MException {
		super.create(resource);
	}
	
	@Override
	public void update(Resource resource) throws OneM2MException {
		
		SemanticDescriptor sdRes = (SemanticDescriptor)resource;
		
		if(sdRes.getSemanticOpExec() != null && sdRes.getDescriptor() != null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_ACCEPTABLE, "Both semanticOpExec and descriptor exist simultaneouly." );
		}
		
		SemanticDescriptor currRes = (SemanticDescriptor)this.getResource(URI_KEY, sdRes.getUri());
		try {
		/*
		 *  blocked in 2016-11-22
		 * 	if(currRes != null) {
		 *
				RestSemanticController.getInstance().deleteDescriptor(sdRes.getUri(), new String(Base64.decode(currRes.getDescriptor()), "UTF-8"), currRes.getDescriptorRepresentation());
			}
		*/	
			super.update(resource);	
			// blocked in 2016-11-22
			//RestSemanticController.getInstance().addDescriptor(sdRes.getUri(), Base64.encode(sdRes.getDescriptor()), sdRes.getDescriptorRepresentation());
		} catch(Exception e) {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Server error in semantic framework");
		}
	}
	
	@Override
	public void delete(String id) throws OneM2MException {
		String resourceID = (String)this.getAttribute(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY,  id, RESID_KEY);	
		
		SemanticDescriptor currRes = (SemanticDescriptor)this.getResource(RESID_KEY, resourceID);
	/*
	 *  blocked in 2016-11-22	
	 *
		try {
			if(currRes != null) {
				RestSemanticController.getInstance().deleteDescriptor(currRes.getUri(), new String(Base64.decode(currRes.getDescriptor()), "UTF-8"), currRes.getDescriptorRepresentation());
			}
		} catch(Exception e) {
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Server error in semantic framework");
		}
	*/	
		deleteChild(resourceID);
		
		deleteDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);

	}

	@Override
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException {
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, 
				(DaoJSONConvertor<SemanticDescriptor>)ConvertorFactory.getDaoJSONConvertor(SemanticDescriptor.class, SemanticDescriptor.SCHEMA_LOCATION), rc);
	}

}
