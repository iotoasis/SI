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
import net.herit.iot.onem2m.incse.manager.dao.DAOInterface;
import net.herit.iot.onem2m.incse.manager.dao.NotificationTargetMgmtPolicyRefDAO;
import net.herit.iot.onem2m.incse.manager.dao.PolicyDeletionRulesDAO;
import net.herit.iot.onem2m.resource.NotificationTargetMgmtPolicyRef;
import net.herit.iot.onem2m.resource.PolicyDeletionRules;

public class PolicyDeletionRulesManager extends AbsManager {

	static String ALLOWED_PARENT = "notificationTargetPolicy";  
	static RESOURCE_TYPE RES_TYPE = RESOURCE_TYPE.POLICYDELETIONRULES; 
	
	private Logger log = LoggerFactory.getLogger(PolicyDeletionRulesManager.class);

	private static final String TAG = PolicyDeletionRulesManager.class.getName();

	public PolicyDeletionRulesManager(OneM2mContext context) {
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

	public OneM2mResponse retrieveLatest(OneM2mRequest reqMessage) throws OneM2MException {

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
	public DAOInterface getDAO() {
		return new PolicyDeletionRulesDAO(context);
	}

	@Override
	public XMLConvertor<?> getXMLConveter() throws OneM2MException {
		return ConvertorFactory.getXMLConvertor(PolicyDeletionRules.class, PolicyDeletionRules.SCHEMA_LOCATION);
	}

	@Override
	public JSONConvertor<?> getJSONConveter() throws OneM2MException {
		return ConvertorFactory.getJSONConvertor(PolicyDeletionRules.class, PolicyDeletionRules.SCHEMA_LOCATION);
	}

	@Override
	public Class<?> getResourceClass() {
		return PolicyDeletionRules.class;
	}

}
