package net.herit.iot.onem2m.incse.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.convertor.XMLConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.controller.AnnounceController;
import net.herit.iot.onem2m.incse.manager.dao.ContentInstanceDAO;
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.TimeSeriesInstanceDAO;
import net.herit.iot.onem2m.resource.ContentInstance;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.TimeSeriesInstance;

public class TimeSeriesInstanceManager extends AbsManager {

	static String ALLOWED_PARENT = "timeSeries,timeSeriesAnnc"; 
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.TIMESERIESINSTANCE; 
	
	private Logger log = LoggerFactory.getLogger(TimeSeriesInstanceManager.class);

	private static final String TAG = TimeSeriesInstanceManager.class.getName();

	public TimeSeriesInstanceManager(OneM2mContext context) {
		super(RES_TYPE, ALLOWED_PARENT);
		this.context = context;
	}
	
	@Override
	public OneM2mResponse create(OneM2mRequest reqMessage) throws OneM2MException {
		return create(reqMessage, this);
		
	}
	
	@Override
	public OneM2mResponse retrieve(OneM2mRequest reqMessage) throws OneM2MException {

		return retrieve(reqMessage, this);
			
	}
	
	@Override
	public OneM2mResponse update(OneM2mRequest reqMessage) throws OneM2MException {

		return update(reqMessage, this);
	}
	
	@Override
	public OneM2mResponse delete(OneM2mRequest reqMessage) throws OneM2MException {

		return delete(reqMessage, this);
	}
	
	@Override
	public OneM2mResponse notify(OneM2mRequest reqMessage) throws OneM2MException {
		// TODO Auto-generated method stub

		return new OneM2mResponse();
	}
	
	@Override
	public void announceTo(OneM2mRequest reqMessage, Resource resource, Resource orgRes) throws OneM2MException {
		new AnnounceController(context).announce(reqMessage, resource, orgRes, this);
	}
	
	@Override
	public DAOInterface getDAO() {
		// TODO Auto-generated method stub
		return new TimeSeriesInstanceDAO(context);
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(TimeSeriesInstance.class, TimeSeriesInstance.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {	
		return ConvertorFactory.getJSONConvertor(TimeSeriesInstance.class, TimeSeriesInstance.SCHEMA_LOCATION);
	}

	@Override
	public Class<?> getResourceClass() {
		// TODO Auto-generated method stub
		return TimeSeriesInstance.class;
	}

}
