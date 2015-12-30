package net.herit.iot.onem2m.incse.controller;

import java.net.URI;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.onem2m.bind.http.client.AsyncResponseListener;
import net.herit.iot.onem2m.bind.http.client.HttpClient;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.AccessPointManager;
import net.herit.iot.onem2m.incse.RestHandler;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.context.RestContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.DatabaseManager;
import net.herit.iot.onem2m.incse.facility.NseManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.incse.manager.AbsManager;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.incse.manager.dao.RestSubscriptionDAO;
import net.herit.iot.onem2m.resource.Attribute;
import net.herit.iot.onem2m.resource.EventNotificationCriteria;
import net.herit.iot.onem2m.resource.Naming;
import net.herit.iot.onem2m.resource.Notification;
import net.herit.iot.onem2m.resource.PrimitiveContent;
import net.herit.iot.onem2m.resource.RequestPrimitive;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.RestSubscription;
import net.herit.iot.onem2m.resource.Subscription;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONCONTENT_TYPE;
import net.herit.iot.onem2m.resource.Subscription.NOTIFICATIONEVENT_TYPE;


public class RestNotificationController extends AbsController implements AsyncResponseListener {

	private Logger log = LoggerFactory.getLogger(RestNotificationController.class);
	private OneM2mContext ctx = null;
	private DatabaseManager dbManager = null;
	
	private static RestNotificationController INSTANCE = new RestNotificationController();
	
	private RestNotificationController() {
	}
	
	public static RestNotificationController getInstance() {
		return INSTANCE;
	}

	public void initialize(OneM2mContext context) {
		
		this.ctx = context;
		this.dbManager = context.getDatabaseManager();

	}
	
	public void notify(Resource parentRes, Document reqDoc, Resource reqRes, Document orgDoc, Resource orgRes, OPERATION op, AbsManager manager) {
		try {
			RestSubscriptionDAO dao = new RestSubscriptionDAO(this.ctx.getDatabaseManager());
			String notiUri = dao.getNotificationUri(parentRes.getUri());
			if (notiUri != null) {
				OneM2mRequest reqMessage = new OneM2mRequest();
				
				JSONConvertor<?> jsonCvt = ConvertorFactory.getJSONConvertor(RestSubscription.class, RestSubscription.SCHEMA_LOCATION);
				reqDoc.put("_uri", reqRes.getUri());
				String json = reqDoc.toJson();
				reqMessage.setContent(json.getBytes());
				
				String to = "/";
				URI url = new URI(notiUri);
				String path = url.getPath();
				if (path.length() > 1) {
					int i = notiUri.indexOf(path);
					to = notiUri.substring(i);
				}
				reqMessage.setTo(to);
				reqMessage.setOperation(OPERATION.CREATE);
				reqMessage.setContentType(CONTENT_TYPE.JSON);
				//reqMessage.setFrom("SI");
				
				new HttpClient().process(notiUri, reqMessage);
			}
			
		} catch (Exception e) {
			log.error("rest notify process exception: ", e);
		}
	}

	@Override
	public OneM2mResponse processRequest(OneM2mRequest reqMessage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean processAsyncRequest(OneM2mRequest reqMessage) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void asyncResponse(OneM2mResponse resMessage) {
		// TODO Auto-generated method stub
		
	}
	
}
