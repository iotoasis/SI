package net.herit.iot.onem2m.incse.manager.dao;

import java.util.HashMap;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.DaoJSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.resource.Container;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.TimeSeries;
import net.herit.iot.onem2m.resource.TimeSeriesInstance;

public class TimeSeriesInstanceDAO extends ResourceDAO implements DAOInterface {

	private Logger log = LoggerFactory.getLogger(TimeSeriesInstanceDAO.class);

	public TimeSeriesInstanceDAO(OneM2mContext context) {
		super(context);
	}

	@Override
	public String resourceToJson(Resource res) throws OneM2MException {
		try {
			
			DaoJSONConvertor<TimeSeriesInstance> jc = (DaoJSONConvertor<TimeSeriesInstance>)ConvertorFactory.getDaoJSONConvertor(TimeSeriesInstance.class, ContentInstance.SCHEMA_LOCATION);
			return jc.marshal((TimeSeriesInstance)res);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);			
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, "Json generation error:"+res.toString());
		}
	}
	
	@Override
	public void create(Resource resource) throws OneM2MException {

		
		TimeSeriesInstance tiRes = (TimeSeriesInstance)resource;
		
		String parentID = tiRes.getParentID();
		
		TimeSeriesDAO tDao = new TimeSeriesDAO(context);
		TimeSeries tRes = (TimeSeries)tDao.retrieve(parentID, null);
		int currrentByteSize = 0;
		int currentNrOfInstances = 0;
			
		if(tRes.getMaxByteSize() != null && tRes.getCurrentByteSize() != null) {
			
			currrentByteSize = tRes.getCurrentByteSize() + tiRes.getContent().toString().length();
			
			if(currrentByteSize > tRes.getMaxByteSize()) {
				throw new OneM2MException(RESPONSE_STATUS.NOT_ACCEPTABLE, "Max Size:"+tRes.getMaxByteSize() +", Current Size:"+ currrentByteSize );
			}
		}
		
		if(tRes.getMaxNrOfInstances() != null && tRes.getCurrentNrOfInstances() != null) {
			
			currentNrOfInstances = tRes.getCurrentNrOfInstances() + 1;
			
			if(currentNrOfInstances > tRes.getMaxNrOfInstances()) {
				throw new OneM2MException(RESPONSE_STATUS.NOT_ACCEPTABLE, "Max Instances:"+tRes.getMaxNrOfInstances() +", Current Instances:"+ currentNrOfInstances );
			}
		}
		
		if(tiRes.getSequenceNr() != null && tRes.getMaxNrOfInstances() != null) {
			if(tiRes.getSequenceNr() > tRes.getMaxNrOfInstances()) {
				throw new OneM2MException(RESPONSE_STATUS.NOT_ACCEPTABLE, "Max Instances:"+tRes.getMaxNrOfInstances() +", Current Sequence Nr:"+ tiRes.getSequenceNr() );
			}
		}
		
		super.create(resource);
		
		TimeSeriesInstance res = (TimeSeriesInstance)resource;
		
		currrentByteSize = tRes.getCurrentByteSize() + tiRes.getContent().toString().length();
		currentNrOfInstances = tRes.getCurrentNrOfInstances() + 1;
		
		// update container attributes : currentNrOfInstances, currentByteSize
		updateTimeSeries(parentID, currentNrOfInstances, currrentByteSize);
        
	}	
	
	private void updateTimeSeries(String resId, int cnt, int size) {
		
		Document doc = getDocument(RESID_KEY, resId);
	    
	    HashMap<String, Object> map = new HashMap<String, Object>();
	    map.put(CUR_NOINST_KEY, (int)doc.get(CUR_NOINST_KEY) + cnt);
	    map.put(CUR_BSIZE_KEY, (int)doc.get(CUR_BSIZE_KEY) + size);
	    updateDocument(RESID_KEY, resId, map);
		
	}
	
	@Override
	public void delete(String id) throws OneM2MException {
		Document doc = getDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);
		int contentSize = ((String)doc.get(CONTENT_KEY)).length();
		String parentID = (String)doc.get(PARENTID_KEY);
		
		deleteDocument(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id);
		
		updateTimeSeries(parentID, -1, contentSize*-1);

	}

	@Override
	public Resource retrieve(String id, RESULT_CONT rc) throws OneM2MException {
		return retrieve(OneM2mUtil.isUri(id) ? URI_KEY : RESID_KEY, id, 
				(DaoJSONConvertor<TimeSeriesInstance>)ConvertorFactory.getDaoJSONConvertor(TimeSeriesInstance.class, TimeSeriesInstance.SCHEMA_LOCATION), rc);
		
	}

}
