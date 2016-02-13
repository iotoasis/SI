package net.herit.iot.onem2m.incse.manager.dao;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mRequest.RESULT_CONT;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.core.convertor.ConvertorFactory;
import net.herit.iot.onem2m.core.convertor.JSONConvertor;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.facility.CfgManager;
import net.herit.iot.onem2m.incse.facility.OneM2mUtil;
import net.herit.iot.onem2m.resource.*;

import org.bson.Document;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.util.JSON;

import java.util.regex.Pattern;

public class ResourceDAO {

	public class KeyValue
	{
	    private String key;
	    private Object value;

		public KeyValue(String key, Object value) {			
			this.key = key;
			this.value = value;			
		}
		
		public Object getValue() {
			return value;
		}
		
		public String getKey() {
			return key;
		}
		
		public void setValue(Object value) {
			this.value = value;
		}
		
		public void setKey(String key) {
			this.key = key;
		}
	}
	
	protected static final String OID_KEY = "_id";
	protected static final String URI_KEY = "_uri";
	protected static final String RESID_KEY = "ri";
	protected static final String RESTYPE_KEY = Naming.RESOURCETYPE_SN;
	protected static final String RESNAME_KEY = Naming.RESOURCENAME_SN;
	protected static final String PARENTID_KEY = "pi";
	protected static final String CRETIME_KEY = "ct";
	protected static final String CUR_NOINST_KEY = "cni";
	protected static final String CUR_BSIZE_KEY = "cbs";
	protected static final String LASTMODTIME_KEY = "lt";
	protected static final String CREATETIME_KEY = "ct";
	protected static final String EXPIRETIME_KEY = "et";
	protected static final String NAME_KEY = "nm";
	protected static final String TYPE_KEY = Naming.TYPE_SN;
	protected static final String CHILDRES_KEY = "ch";
	protected static final String ACPIDS_KEY = "acpi";
	protected static final String PRIVILEGES_KEY = "pv";
	protected static final String CONTENTSIZE_KEY = "cs";

	protected static final String CSEID_KEY = "csi";
	protected static final String CSETYPE_KEY = "cst";

	protected static final String ACCCTRL_RULE_KEY = Naming.ACCESSCONTROLRULE_SN;	
	protected static final String ACCCTRL_ORIGS_KEY = Naming.ACCESSCONTROLORIGINATORS_SN;	
	protected static final String ACCCTRL_OPERS_KEY = Naming.ACCESSCONTROLOPERATIONS_SN;
	protected static final String ACCCTRL_CTXS_KEY = Naming.ACCESSCONTROLCONTEXTS_SN;
	protected static final String ACCCTRL_IPADDRS_KEY = Naming.ACCESSCONTROLIPADDRESSES_SN;
	protected static final String ACCCTRL_LOCREG_KEY = Naming.ACCESSCONTROLLOCATIONREGION_SN;
	protected static final String ACCCTRL_WINDOW_KEY = Naming.ACCESSCONTROWINDOW_SN;
	protected static final String COUNTRYCODE_KEY = Naming.COUNTRYCODE_SN;
	protected static final String CIRCREGION_KEY = "accr";
	protected static final String IPV4ADDRS_KEY = Naming.IPV4ADDRESSES_SN;
	protected static final String IPV6ADDRS_KEY = Naming.IPV6ADDRESSES_SN;

	protected static final String ANNCTO_KEY = "at";
	protected static final String ANNCDATTR_KEY = "aa";
	protected static final String ACCCTRLPOLICY_IDS_KEY = "acpi";
	protected static final String LABELS_KEY = "lbl";

	protected static final String UPDATE_SPECIFIC_FIELD = "$set";

	protected OneM2mContext context = null;
	
	protected String collectionName = null;

	private Logger log = LoggerFactory.getLogger(ResourceDAO.class);

	public ResourceDAO(OneM2mContext context) {
		this.context = context;
		this.collectionName = CfgManager.getInstance().getResourceDatabaseName();
	}

	public String resourceToJson(Resource res) throws OneM2MException {

		try {

			JSONConvertor<Resource> jc = (JSONConvertor<Resource>) ConvertorFactory
					.getJSONConvertor(Resource.class, null);
			return jc.marshal((Resource) res);

		} catch (Exception e) {
			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR,
					"Json generation error:" + res.toString());
		}
	}

	protected void create(Resource res) throws OneM2MException {

		Document curDoc = getDocument(URI_KEY, res.getUri());
		if (curDoc != null) {

			throw new OneM2MException(RESPONSE_STATUS.CONFLICT,
					"Resource already exist!!! :" + res.getUri());
		}		

		String strJson = resourceToJson(res);
		log.debug("Res json: {} ", strJson);
		
		String currentTime = getTimeString(LocalDateTime.now());
		res.setCreationTime(currentTime);
		res.setLastModifiedTime(currentTime);

		Document doc = Document.parse(strJson);
		doc.append(ResourceDAO.URI_KEY, res.getUri());
		doc.append(ResourceDAO.CRETIME_KEY, currentTime);
		doc.append(ResourceDAO.LASTMODTIME_KEY, currentTime);

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);

		collection.insertOne(doc);

	}

	public void update(Resource resource) throws OneM2MException {

		resource.setLastModifiedTime(getTimeString(LocalDateTime.now()));

		String strJson = this.resourceToJson(resource);

		BasicDBObject dbObj = (BasicDBObject) JSON.parse(strJson);
		BasicDBObject update = new BasicDBObject(UPDATE_SPECIFIC_FIELD, dbObj);
		BasicDBObject query = new BasicDBObject(URI_KEY, resource.getUri());
		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.findOneAndUpdate(query, update);

		//context.getLogManager().debug(doc.toString());
		log.debug(doc.toString());
	}

	public void delete(String key, String value) throws Exception,
			OneM2MException {
		this.deleteDocument(key, value);
	}

	public Resource retrieve(String key, String value, JSONConvertor<?> cvt,
			RESULT_CONT rc) throws OneM2MException {

		Document doc = getDocument(key, value);

		if (doc == null) {
			return null;
		}


		try {

			Resource res = (Resource) cvt.unmarshal(doc.toJson());
			res.setUri(doc.getString(URI_KEY));

			String resId = doc.getString(RESID_KEY);
			
//			List<Document> childList = null;
//			if (rc == RESULT_CONT.ATTR_N_CHILD_RES) {
//				childList = getDocuments(PARENTID_KEY, resId);
//				doc.append(CHILDRES_KEY, childList);
//
//				try {
//					Class[] cArg = new Class[1];
//					cArg[0] = ChildResourceRef.class;
//					Method method = res.getClass().getDeclaredMethod("addChildResource", cArg);
//
//					List<ChildResourceRef> childRefs = getChildResourceRefs(PARENTID_KEY, resId);
//					
//					Iterator<ChildResourceRef> it = childRefs.iterator();
//					while (it.hasNext()) {
//						Object[] oArg = new Object[1];
//						oArg[0] = it.next();
//						method.invoke(res, oArg);						
//					}
//
//				} catch (Exception e) {
//					log.debug("Handled exception", e);
//				}
//			} else
			if (rc == RESULT_CONT.ATTR_N_CHILD_RES_REF || rc == RESULT_CONT.CHILD_RES_REF || rc == RESULT_CONT.ATTR_N_CHILD_RES) {

				try {
					Class[] cArg = new Class[1];
					cArg[0] = ChildResourceRef.class;
					Method method = res.getClass().getDeclaredMethod("addChildResource", cArg);

					List<ChildResourceRef> childRefs = getChildResourceRefs(PARENTID_KEY, resId);
					
					Iterator<ChildResourceRef> it = childRefs.iterator();
					while (it.hasNext()) {
						Object[] oArg = new Object[1];
						oArg[0] = it.next();
						method.invoke(res, oArg);						
					}

				} catch (Exception e) {
					log.debug("Handled exception", e);
				}
			}
			
			return res;

		} catch (Exception e) {

			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR,
					"Exception in converting:" + doc.toString());

		}

	}

	public List<Resource> discovery(String uri, FilterCriteria fc)
			throws OneM2MException {
		List<Resource> resources = new ArrayList<Resource>();
		List<String> labels = fc.getLabels();
		Integer limit = fc.getLimit();
		RESOURCE_TYPE resType = fc.getResourceType() == null ? null : RESOURCE_TYPE.get(fc.getResourceType());
		String createdAfter = fc.getCreatedAfter();
		String createdBefore = fc.getCreatedBefore();
		String expiredAfter = fc.getExpireAfter();
		String expiredBefore = fc.getExpireBefore();
		String modifiedSince = fc.getModifiedSince();
		String unmodifiedSince = fc.getUnmodifiedSince();
		Integer sizeAbove = fc.getSizeAbove();
		Integer sizeBelow = fc.getSizeBelow();
		Integer stBigger = fc.getStateTagBigger();
		Integer stSmaller = fc.getStateTagSmaller();


		BasicDBObject query = null;
		
		if (labels != null && labels.size() > 0) {
		
			BasicDBList labelOr = new BasicDBList();
			Iterator<String> it = labels.iterator();
			while (it.hasNext()) {
				labelOr.add(new BasicDBObject(LABELS_KEY, Pattern.compile(it.next())));
			}
			
			query = new BasicDBObject("$or", labelOr);
		
		} else {
			query = new BasicDBObject();
		}
		
		query.put(URI_KEY, new BasicDBObject("$regex", uri+"/"));

		if (resType != null && resType != RESOURCE_TYPE.NONE) {
			query.append(RESTYPE_KEY, resType.Value());
		}
		if (createdAfter != null) {
			query.append(Naming.CREATIONTIME_SN, new BasicDBObject("$gt", createdAfter));
		}
		if (createdBefore != null) {
			query.append(Naming.CREATIONTIME_SN, new BasicDBObject("$lt", createdBefore));
		}
		if (expiredAfter != null) {
			query.append(Naming.EXPIREAFTER_SN, new BasicDBObject("$gt", expiredAfter));
		}
		if (expiredBefore != null) {
			query.append(Naming.EXPIREBEFORE_SN, new BasicDBObject("$lt", expiredBefore));
		}
		if (modifiedSince != null) {
			query.append(Naming.LASTMODIFIEDTIME_SN, new BasicDBObject("$gt", modifiedSince));
		}
		if (unmodifiedSince != null) {
			query.append(Naming.LASTMODIFIEDTIME_SN, new BasicDBObject("$lt", unmodifiedSince));
		}
		if (sizeAbove != null && sizeAbove > 0) {
			query.append(Naming.CONTENTSIZE_SN, new BasicDBObject("$lt", sizeAbove));
		}
		if (sizeBelow != null && sizeBelow > 0) {
			query.append(Naming.CONTENTSIZE_SN, new BasicDBObject("$lt", sizeBelow));
		}
		if (stBigger != null && stBigger > 0) {
			query.append(Naming.STATETAG_SN, new BasicDBObject("$gt", stBigger));
		}
		if (stSmaller != null && stSmaller > 0) {
			query.append(Naming.STATETAG_SN, new BasicDBObject("$lt", stSmaller));
		}
		
		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		FindIterable<Document> docs = collection.find(query);
		if (limit != null && limit > 0) {
			docs = docs.limit(limit);
		}
		MongoCursor<Document> cursors = docs.iterator();

		while (cursors.hasNext()) {
			Document doc = cursors.next();
			Resource res = new Resource();
			res.setResourceID(doc.getString(RESID_KEY));
			res.setUri(doc.getString(URI_KEY));
			// Resource res = createResourceWithDoc(doc); -- to enhance
			// performance
			resources.add(res);
		}

		return resources;
	}

	public Resource createResourceWithDoc(Document doc) throws OneM2MException {

		int iType = doc.getInteger(RESTYPE_KEY);
		RESOURCE_TYPE resType = RESOURCE_TYPE.get(iType);
		JSONConvertor<?> jc = this.getJsonConvertor(resType);

		Resource res;
		try {
			if(jc == null) log.error("JSonConvertor is null");
			res = (Resource) jc.unmarshal(doc.toJson());
			res.setUri(doc.getString(URI_KEY));
			return res;
		} catch (Exception e) {
			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR,
					"Exception in making res from doc:" + e.getMessage());
		}

		// try {
		//
		// String name = resType.Name();
		// String clsName = name.substring(0, 1).toUpperCase() +
		// name.substring(1);
		// Class<?> c =
		// Class.forName(Resource.class.getPackage().getName()+"/"+clsName);
		// Constructor<?> cons = c.getConstructor(String.class);
		// Resource res = (Resource)cons.newInstance();
		// res.initializeWithDoc(doc);
		// return res;
		//
		// } catch (Exception e) {
		// throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR,
		// "Fail to create Resource instance for doc:"+resType.name());
		// }
	}

	// public Resource retrieve(String uri, Resource res) throws OneM2MException
	// {
	//
	// Document doc = getDocument(URI_KEY, uri);
	//
	// if (doc == null) {
	// return null;
	// }
	//
	// try {
	// res.initializeWithDoc(doc);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// log.debug("Handled exception", e);
	//
	// throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR,
	// "Exception during initialization of resource using document.");
	// }
	//
	// return res;
	//
	// }

	public JSONConvertor<?> getJsonConvertor(RESOURCE_TYPE resType)
			throws OneM2MException {

		switch (resType) {
		case ACCESS_CTRL_POLICY:
			return ConvertorFactory.getJSONConvertor(AccessControlPolicy.class,
					AccessControlPolicy.SCHEMA_LOCATION);
		case AE:
			return ConvertorFactory.getJSONConvertor(AE.class,
					AE.SCHEMA_LOCATION);
		case CONTAINER:
			return ConvertorFactory.getJSONConvertor(Container.class,
					Container.SCHEMA_LOCATION);
		case CONTENT_INST:
			return ConvertorFactory.getJSONConvertor(ContentInstance.class,
					ContentInstance.SCHEMA_LOCATION);
		case CSE_BASE:
			return ConvertorFactory.getJSONConvertor(CSEBase.class,
					CSEBase.SCHEMA_LOCATION);
		case DELIVERY:
			return ConvertorFactory.getJSONConvertor(Delivery.class,
					Delivery.SCHEMA_LOCATION);
		case EVENT_CONFIT:
			return ConvertorFactory.getJSONConvertor(EventConfig.class,
					EventConfig.SCHEMA_LOCATION);
		case EXEC_INST:
			return ConvertorFactory.getJSONConvertor(ExecInstance.class,
					ExecInstance.SCHEMA_LOCATION);
		case GROUP:
			return ConvertorFactory.getJSONConvertor(Group.class,
					Group.SCHEMA_LOCATION);
		case LOCAT_POLICY:
			return ConvertorFactory.getJSONConvertor(LocationPolicy.class,
					LocationPolicy.SCHEMA_LOCATION);
			// case M2M_SVC_SUBSC_PROF: return
			// ConvertorFactory.getJSONConvertor(M2mServiceSubscriptionProfile.class,
			// M2mServiceSubscriptionProfile.SCHEMA_LOCATION);
		case MGMT_CMD:
			return ConvertorFactory.getJSONConvertor(MgmtCmd.class,
					MgmtCmd.SCHEMA_LOCATION);
			// case MGMT_OBJ: return
			// ConvertorFactory.getJSONConvertor(MgmtObj.class,
			// MgmtObj.SCHEMA_LOCATION);
		case NODE:
			return ConvertorFactory.getJSONConvertor(Node.class,
					Node.SCHEMA_LOCATION);
		case POLLING_CHANN:
			return ConvertorFactory.getJSONConvertor(PollingChannel.class,
					PollingChannel.SCHEMA_LOCATION);
		case REMOTE_CSE:
			return ConvertorFactory.getJSONConvertor(RemoteCSE.class,
					RemoteCSE.SCHEMA_LOCATION);
		case REQUEST:
			return ConvertorFactory.getJSONConvertor(Request.class,
					Request.SCHEMA_LOCATION);
		case SCHEDULE:
			return ConvertorFactory.getJSONConvertor(Schedule.class,
					Schedule.SCHEMA_LOCATION);
		case SVC_SUBSC_APP_RULE:
			return ConvertorFactory.getJSONConvertor(
					ServiceSubscribedAppRule.class,
					ServiceSubscribedAppRule.SCHEMA_LOCATION);
		case SVC_SUBSC_NODE:
			return ConvertorFactory.getJSONConvertor(
					ServiceSubscribedNode.class,
					ServiceSubscribedNode.SCHEMA_LOCATION);
		case STATS_COLLECT:
			return ConvertorFactory.getJSONConvertor(StatsCollect.class,
					StatsCollect.SCHEMA_LOCATION);
		case STATS_CONFIG:
			return ConvertorFactory.getJSONConvertor(StatsConfig.class,
					StatsConfig.SCHEMA_LOCATION);
		case SUBSCRIPTION:
			return ConvertorFactory.getJSONConvertor(Subscription.class,
					Subscription.SCHEMA_LOCATION);
		case ACCESS_CTRL_POLICY_ANNC:
			return ConvertorFactory.getJSONConvertor(
					AccessControlPolicyAnnc.class,
					AccessControlPolicyAnnc.SCHEMA_LOCATION);
		case AE_ANNC:
			return ConvertorFactory.getJSONConvertor(AEAnnc.class,
					AEAnnc.SCHEMA_LOCATION);
		case CONTAINER_ANNC:
			return ConvertorFactory.getJSONConvertor(ContainerAnnc.class,
					ContainerAnnc.SCHEMA_LOCATION);
		case CONTENT_INST_ANNC:
			return ConvertorFactory.getJSONConvertor(ContentInstanceAnnc.class,
					ContentInstanceAnnc.SCHEMA_LOCATION);
		case GROUP_ANNC:
			return ConvertorFactory.getJSONConvertor(GroupAnnc.class,
					GroupAnnc.SCHEMA_LOCATION);
		case LOCAT_POLICY_ANNC:
			return ConvertorFactory.getJSONConvertor(LocationPolicyAnnc.class,
					LocationPolicyAnnc.SCHEMA_LOCATION);
			// case MGMT_OBJ_ANNC: return
			// ConvertorFactory.getJSONConvertor(MgmtObjAnnc.class,
			// MgmtObjAnnc.SCHEMA_LOCATION);
		case NODE_ANNC:
			return ConvertorFactory.getJSONConvertor(NodeAnnc.class,
					NodeAnnc.SCHEMA_LOCATION);
		case REMOTE_CSE_ANNC:
			return ConvertorFactory.getJSONConvertor(RemoteCSEAnnc.class,
					RemoteCSEAnnc.SCHEMA_LOCATION);
		case SCHEDULE_ANNC:
			return ConvertorFactory.getJSONConvertor(ScheduleAnnc.class,
					ScheduleAnnc.SCHEMA_LOCATION);
		default:
			log.debug("ResourceDAO.getJsonConvertor not implemented for {}", resType);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR,
					"ResourceDAO.getJsonConvertor not implemented");
		}
	}

	public Resource getResource(String key, String value) throws OneM2MException {

		Document doc = this.getDocument(key, value);
		if (doc == null) {
			return null;
		}

		RESOURCE_TYPE resType = RESOURCE_TYPE.get((int) doc.get(RESTYPE_KEY));
		JSONConvertor<?> jc = getJsonConvertor(resType);
		Resource res;
		try {
			res = (Resource) jc.unmarshal(doc.toJson());
			res.setUri(doc.getString(URI_KEY));
			// res.setId(doc.getString(OID_KEY));
		} catch (Exception e) {
			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR,
					"unmarshal file in ResouceDAO.getResourceWithUri:"
							+ doc.toJson());
		}
		return res;
	}

	public Resource getResource(String id) throws OneM2MException {

		Document doc = this.getDocument(id);
		if (doc == null) {
			return null;
		}

		RESOURCE_TYPE resType = RESOURCE_TYPE.get((int) doc.get(RESTYPE_KEY));
		JSONConvertor<?> jc = getJsonConvertor(resType);
		Resource res;
		try {
			res = (Resource) jc.unmarshal(doc.toJson());
			res.setUri(doc.getString(URI_KEY));
			// res.setId(doc.getString(OID_KEY));
		} catch (Exception e) {
			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR,
					"unmarshal file in ResouceDAO.getResourceWithUri:"
							+ doc.toJson());
		}
		return res;
	}

	public Resource getResourceWithID(String resourceID) throws OneM2MException {

		Document doc = this.getDocument(resourceID);
		
		if (doc == null) {
			return null;
		}

		RESOURCE_TYPE resType = RESOURCE_TYPE.get((int) doc.get(RESTYPE_KEY));
		JSONConvertor<?> jc = getJsonConvertor(resType);
		Resource res;
		try {
			res = (Resource) jc.unmarshal(doc.toJson());
			res.setUri(doc.getString(URI_KEY));
			// res.setId(doc.getString(OID_KEY));
		} catch (Exception e) {
			log.debug("Handled exception", e);
			throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR,
					"unmarshal file in ResouceDAO.getResourceWithID:"
							+ doc.toJson());
		}
		return res;

	}

	public int getResourceType(String key) throws OneM2MException {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.find(new BasicDBObject(OneM2mUtil.isUri(key) ? URI_KEY : RESID_KEY, key)).first();

		if (doc == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND,
					"resource not found");
		}
		
		int resType = (int) doc.get(Naming.RESOURCETYPE_SN);
		if (resType == RESOURCE_TYPE.MGMT_OBJ.Value()){
			return (int) doc.get(Naming.MGMTDEFINITION_SN);
		}

		// return (int)doc.get("resourceType");
		return (int) doc.get(Naming.RESOURCETYPE_SN);

	}

	public HashMap<String, Object> getAttributesWithUri(String uri,
			List<String> attrNames) throws OneM2MException {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.find(new BasicDBObject(URI_KEY, uri)).first();

		if (doc == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND,
					"resource not found");
		}

		HashMap<String, Object> results = new HashMap<String, Object>();
		Iterator<String> it = attrNames.iterator();
		while (it.hasNext()) {
			String attr = it.next();
			results.put(attr, doc.get(attr));
		}

		return results;

	}

	public HashMap<String, Object> getAttributesWithID(String resourceID,
			List<String> attrNames) throws OneM2MException {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection
				.find(new BasicDBObject(RESID_KEY, resourceID)).first();

		HashMap<String, Object> results = new HashMap<String, Object>();
		Iterator<String> it = attrNames.iterator();
		while (it.hasNext()) {
			String attr = it.next();
			results.put(attr, doc.get(attr));
		}

		return results;

	}

	public HashMap<String, Object> getAttributes(String keyName,
			String keyValue, List<String> attrNames) throws OneM2MException {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.find(new BasicDBObject(keyName, keyValue))
				.first();

		if (doc == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND,
					"resource not found");
		}

		HashMap<String, Object> results = new HashMap<String, Object>();
		Iterator<String> it = attrNames.iterator();
		while (it.hasNext()) {
			String attr = it.next();
			results.put(attr, doc.get(attr));
		}

		return results;

	}

	public Object getAttributeWithUri(String uri, String attrName)
			throws OneM2MException {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.find(new BasicDBObject(URI_KEY, uri)).first();

		if (doc == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND,
					"resource not found");
		}

		return doc.get(attrName);

	}

	public Object getAttributeWithId(String resourceID, String attrName)
			throws OneM2MException {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection
				.find(new BasicDBObject(RESID_KEY, resourceID)).first();

		if (doc == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND,
					"resource not found");
		}

		return doc.get(attrName);
	}

	
	public List<Subscription> getSubscriptionWithParentId(String pi) throws OneM2MException {
		ArrayList<Subscription> subscriptions = new ArrayList<Subscription>();

		BasicDBObject query = new BasicDBObject(PARENTID_KEY, pi).append(RESTYPE_KEY, RESOURCE_TYPE.SUBSCRIPTION.Value());
		
		MongoCollection<Document> collection = context.getDatabaseManager().getCollection(collectionName);
		MongoCursor<Document> cursor = collection.find(query).iterator();
		
		while(cursor.hasNext()) {
			subscriptions.add((Subscription)createResourceWithDoc(cursor.next()));
		}
	
		return subscriptions;
	}
	
	
	public boolean isVaildPollingChannelUri(String uri) throws OneM2MException {
		
		Document doc = getDocument(Naming.POLLINGCHANNELURI_SN, uri);
		
		if(doc == null) return false;
		
		return true;
	}
	


	public List<String> getPollingChannelUriWithUri(String uri)
			throws OneM2MException {
		ArrayList<String> pollingChannels = new ArrayList<String>();

		Document doc = getDocument(URI_KEY, uri);

		if (doc != null) {
			String resId = doc.getString(RESID_KEY);

			BasicDBObject query = new BasicDBObject(PARENTID_KEY, resId)
					.append(RESTYPE_KEY, RESOURCE_TYPE.POLLING_CHANN.Value());

			MongoCollection<Document> collection = context.getDatabaseManager()
					.getCollection(collectionName);
			MongoCursor<Document> cursor = collection.find(query).iterator();
			
			while (cursor.hasNext()) {
				pollingChannels.add(cursor.next().getString(Naming.POLLINGCHANNELURI_SN));

//			if (pollingDoc != null) {
//				pollingChannels.add(pollingDoc
//						.getString(PollingChannel.POLLINGCHANNELURI_SHORTNAME));
			}
		}

		return pollingChannels;
	}

	public List<Object> getAttributesWithUri(String key, String id, String attrName)
			throws OneM2MException {

		ArrayList<Object> list = new ArrayList<Object>();
		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);

		Document doc = collection.find(new BasicDBObject(key, id)).first();
		if (doc != null) {
			// if(doc.getBoolean(attrName) != null) {
			if (doc.get(attrName) != null) {
				list.add(doc.get(attrName));
			}
		}

		return list;
	}

	public Object getAttribute(String keyName, String keyValue, String attrName)
			throws OneM2MException {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.find(new BasicDBObject(keyName, keyValue))
				.first();

		if (doc == null) {
			throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND,
					"resource not found");
		}

		return doc.get(attrName);
	}
	
	protected void deleteChild(String resourceId) {

		List<Document> childs = getDocuments(PARENTID_KEY, resourceId);
		if (childs == null || childs.size() == 0) return;
		
		for (Document child: childs) {
			String childId = child.getString(RESID_KEY); 
			deleteChild(childId);
			deleteDocument(RESID_KEY, childId);
			log.debug("{}/{} deleted in deleteChild", resourceId, childId);
		}
		
	}

	public DeleteResult deleteDocument(String keyName, String keyValue) {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		DeleteResult result = collection.deleteMany(new BasicDBObject(keyName,
				keyValue));
		log.debug(result.toString());

		return result;
	}

	public Document getDocument(String id) {
		if (OneM2mUtil.isUri(id)) {
			return getDocument(URI_KEY, id);
		} else {
			return getDocument(RESID_KEY, id);
		}
	}

	public Document getDocument(String keyName, String keyValue) {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.find(new BasicDBObject(keyName, keyValue))
				.first();

		return doc;
	}

	public List<Document> getDocuments(String keyName, String keyValue) {

		ArrayList<Document> docList = new ArrayList<Document>();

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		MongoCursor<Document> cursor = collection.find(
				new BasicDBObject(keyName, keyValue)).iterator();
		while (cursor.hasNext()) {
			docList.add(cursor.next());
		}

		return docList;

	}

//	public List<Document> getRefDocuments(String keyName, String keyValue) {
//
//		ArrayList<Document> docList = new ArrayList<Document>();
//
//		MongoCollection<Document> collection = context.getDatabaseManager()
//				.getCollection(collectionName);
//		MongoCursor<Document> cursor = collection.find(
//				new BasicDBObject(keyName, keyValue)).iterator();
//
//		while (cursor.hasNext()) {
//			Document doc = cursor.next();
//			Document ref = new Document();
//			ref.append(NAME_KEY, doc.get(RESNAME_KEY)); // "nm" "rn"
//			ref.append(TYPE_KEY, doc.get(RESTYPE_KEY)); // "typ" "rty"
//			
//			
//			docList.add(ref);
//		}
//
//		return docList;
//
//	}

	public List<ChildResourceRef> getChildResourceRefs(String keyName, String keyValue) {

		ArrayList<ChildResourceRef> refList = new ArrayList<ChildResourceRef>();

		String now = LocalDateTime.now().toString(DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss"));

		DBObject c1 = new BasicDBObject(EXPIRETIME_KEY, null);
		DBObject c2 = new BasicDBObject(EXPIRETIME_KEY, new BasicDBObject("$gt", now));
		
		BasicDBList or = new BasicDBList();
		or.add(c1);
		or.add(c2);
		
		BasicDBObject query = new BasicDBObject("$or", or).append(keyName,  keyValue);

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		//MongoCursor<Document> cursor = collection.find(
		//		new BasicDBObject(keyName, keyValue)).iterator();
		MongoCursor<Document> cursor = collection.find(query).iterator();

		while (cursor.hasNext()) {
			Document doc = cursor.next();
			
			ChildResourceRef ref = new ChildResourceRef();
			ref.setName(doc.getString(RESNAME_KEY));
			ref.setType(doc.getInteger(RESTYPE_KEY));
			ref.setValue(doc.getString(URI_KEY));
			
			refList.add(ref);
		}

		return refList;

	}

	public List<Document> getDocuments(BasicDBObject query, RESOURCE_TYPE resType, String sortKey, boolean asc, int limit) {
		
		ArrayList<Document> docList = new ArrayList<Document>();
			
		BasicDBObject sort = new BasicDBObject(sortKey, asc ? 1 : -1);

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		MongoCursor<Document> cursor = collection.find(query).sort(sort)
				.limit(limit).iterator();
		while (cursor.hasNext()) {
			docList.add(cursor.next());
		}

		return docList;
		
	}

	public List<Document> getDocuments(List<KeyValue> cond, RESOURCE_TYPE resType, String sortKey, boolean asc, int limit) {

		ArrayList<Document> docList = new ArrayList<Document>();
		
		Iterator<KeyValue> it = cond.iterator();
		KeyValue kv = it.next();
		BasicDBObject query = new BasicDBObject(kv.getKey(), kv.getValue());
		while (it.hasNext()) {
			kv = it.next();
			query.append(kv.getKey(), kv.getValue());
		}
	
		BasicDBObject sort = null;
		if (sortKey != null) {
			sort = new BasicDBObject(sortKey, asc ? 1 : -1);
		}

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		MongoCursor<Document> cursor;
		FindIterable<Document> find = collection.find(query);
		if (sort != null) {
			find = find.sort(sort);			
		}
		if (limit >= 0) {
			find = find.limit(limit);
		}
		cursor = find.iterator();
				
		while (cursor.hasNext()) {
			docList.add(cursor.next());
		}

		return docList;

	}

	public List<Document> getDocuments(String keyName, String keyValue,
			RESOURCE_TYPE resType, String sortKey, boolean asc, int limit) {

		ArrayList<Document> docList = new ArrayList<Document>();

		BasicDBObject query = new BasicDBObject(keyName, keyValue).append(
				RESTYPE_KEY, resType.Value());
		BasicDBObject sort = new BasicDBObject(sortKey, asc ? 1 : -1);

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		MongoCursor<Document> cursor = collection.find(query).sort(sort)
				.limit(limit).iterator();
		while (cursor.hasNext()) {
			docList.add(cursor.next());
		}

		return docList;

	}

	public void updateDocument(String keyName, String keyValue,
			HashMap<String, Object> map) {

		BasicDBObject query = new BasicDBObject(keyName, keyValue);

		Iterator<String> keys = map.keySet().iterator();
		BasicDBObject param = new BasicDBObject();
		while (keys.hasNext()) {
			String key = keys.next();
			param.append(key, map.get(key));
		}
		BasicDBObject update = new BasicDBObject(UPDATE_SPECIFIC_FIELD, param);

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);
		Document doc = collection.findOneAndUpdate(query, update);
		log.debug(doc.toString());

	}

	public void createDocument(HashMap<String, Object> map) {

		MongoCollection<Document> collection = context.getDatabaseManager()
				.getCollection(collectionName);

		Document doc = new Document();

		Iterator<String> keys = map.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			Object val = map.get(key);
			doc.append(key, val);
		}

		collection.insertOne(doc);

	}

	/* function for simple retrieve - start */
	protected void updateAnnouncableAttributesMap(
			AnnounceableResource resource, HashMap<String, Object> updateMap) {
		updateRegularAttributesMap((RegularResource) resource, updateMap);

		if (resource.getAnnounceTo() != null)
			updateMap.put(ANNCTO_KEY, resource.getAnnounceTo()); // "announceTo"
		if (resource.getAnnouncedAttribute() != null)
			updateMap.put(ANNCDATTR_KEY, resource.getAnnouncedAttribute()); // "announcedAttribute"

	}

	protected void setAnnouncableAttributes(AnnounceableResource resource,
			Document doc) {
		setRegularAttributes((RegularResource) resource, doc);

		List<String> annTo = resource.getAnnounceTo();
		annTo.addAll((ArrayList<String>) doc.get(ANNCTO_KEY));

		List<String> annAttr = resource.getAnnouncedAttribute();
		Object obj = doc.get(ANNCDATTR_KEY);
		annAttr.addAll((ArrayList<String>) obj);

	}

	protected void updateAnnounceableSubordinateResourceMap(
			AnnounceableSubordinateResource resource,
			HashMap<String, Object> updateMap) {

		updateResourceAttributesMap(resource, updateMap);

		if (resource.getExpirationTime() != null)
			updateMap.put(EXPIRETIME_KEY, resource.getExpirationTime());
		if (resource.getAnnounceTo() != null)
			updateMap.put(ANNCTO_KEY, resource.getAnnounceTo());
		if (resource.getAnnouncedAttribute() != null)
			updateMap.put(ANNCDATTR_KEY, resource.getAnnouncedAttribute());

	}

	protected void setAnnounceableSubordinateResource(
			AnnounceableSubordinateResource resource, Document doc) {

		setResourceAttributes((Resource) resource, doc);

		resource.setExpirationTime(doc.getString(EXPIRETIME_KEY));
		List<String> annTo = resource.getAnnounceTo();
		annTo.addAll((ArrayList<String>) doc.get(ANNCTO_KEY));
		List<String> annAttr = resource.getAnnouncedAttribute();
		annAttr.addAll((ArrayList<String>) doc.get(ANNCDATTR_KEY));

	}

	protected void updateRegularAttributesMap(RegularResource resource,
			HashMap<String, Object> updateMap) {

		updateResourceAttributesMap((Resource) resource, updateMap);

		if (resource.getExpirationTime() != null)
			updateMap.put(EXPIRETIME_KEY, resource.getExpirationTime());
		if (resource.getAccessControlPolicyIDs() != null)
			updateMap.put(ACCCTRLPOLICY_IDS_KEY,
					resource.getAccessControlPolicyIDs());

	}

	protected void setRegularAttributes(RegularResource resource, Document doc) {

		setResourceAttributes((Resource) resource, doc);

		resource.setExpirationTime(doc.getString(EXPIRETIME_KEY));
		List<String> acps = resource.getAccessControlPolicyIDs();
		acps.addAll((ArrayList<String>) doc.get(ACCCTRLPOLICY_IDS_KEY));

	}

	protected void updateResourceAttributesMap(Resource resource,
			HashMap<String, Object> updateMap) {

		// if (resource.getCreationTime() != null) updateMap.put("creationTime",
		// resource.getCreationTime());
		if (resource.getParentID() != null)
			updateMap.put(PARENTID_KEY, resource.getParentID());
		if (resource.getResourceID() != null)
			updateMap.put(RESID_KEY, resource.getResourceID());
		if (resource.getResourceName() != null)
			updateMap.put(RESNAME_KEY, resource.getResourceName());
		if (resource.getUri() != null)
			updateMap.put(URI_KEY, resource.getUri());
		if (resource.getLabels() != null)
			updateMap.put(LABELS_KEY, resource.getLabels());
		if (resource.getResourceType() != null)
			updateMap.put(RESTYPE_KEY, resource.getResourceType());

		updateMap.put(LASTMODTIME_KEY, getTimeString(LocalDateTime.now()));
	}

	protected void setResourceAttributes(Resource resource, Document doc) {

		resource.setCreationTime(doc.getString(this.CREATETIME_KEY));
		resource.setId(doc.get(this.OID_KEY).toString());
		resource.setLastModifiedTime(doc.getString(LASTMODTIME_KEY));
		resource.setParentID(doc.getString(PARENTID_KEY));
		resource.setResourceID(doc.getString(RESID_KEY));
		resource.setResourceName(doc.getString(RESNAME_KEY));
		resource.setUri(doc.getString(URI_KEY));

		List<String> labels = resource.getLabels();
		List<String> lbls = (List<String>) doc.get(LABELS_KEY);
		if (lbls != null) {
			labels.addAll((ArrayList<String>) lbls);
		}

		resource.setResourceType((int) doc.get(RESTYPE_KEY));

	}

	protected Document appendAnnounceableAttributes(Document doc,
			AnnounceableResource res) {
		// TODO Auto-generated method stub

		doc.append(ANNCTO_KEY, res.getAnnounceTo()).append(ANNCDATTR_KEY,
				res.getAnnouncedAttribute());

		appendRegularAttributes(doc, (RegularResource) res);

		return doc;

	}

	protected Document appendAnnounceableSubordinateAttributes(Document doc,
			AnnounceableSubordinateResource res) {

		appendBasicAttributes(doc, (Resource) res);

		doc.append(EXPIRETIME_KEY, res.getExpirationTime());
		doc.append(ANNCTO_KEY, res.getAnnounceTo());
		doc.append(ANNCDATTR_KEY, res.getAnnouncedAttribute());

		return doc;

	}

	protected Document appendRegularAttributes(Document doc, RegularResource res) {
		appendBasicAttributes(doc, (Resource) res);

		doc.append(EXPIRETIME_KEY, res.getExpirationTime()).append(
				ACCCTRLPOLICY_IDS_KEY, res.getAccessControlPolicyIDs());

		return doc;
	}

	protected Document appendBasicAttributes(Document doc, Resource res) {

		doc.append(URI_KEY, res.getUri())
				.append(RESNAME_KEY, res.getResourceName())
				.append(RESTYPE_KEY, res.getResourceType().intValue())
				.append(RESID_KEY, res.getResourceID())
				.append(PARENTID_KEY, res.getParentID())
				.append(LABELS_KEY, res.getLabels());

		String now = getTimeString(LocalDateTime.now());
		if (res.getCreationTime() == null) {
			doc.append(CREATETIME_KEY, now);
		} else {
			doc.append(CREATETIME_KEY, res.getCreationTime());
		}
		if (res.getLastModifiedTime() == null) {
			doc.append(LASTMODTIME_KEY, now);
		} else {
			doc.append(LASTMODTIME_KEY, res.getLastModifiedTime());
		}

		return doc;

	}

	/* function for simple retrieve - end */

	protected String getTimeString(LocalDateTime time) {
		// return time.toString("yyyyMMddTHHmmss");
		return time.toString(DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss"));
	}

	public BigInteger bigInteger(Object num) {
		if (num instanceof BigInteger) {
			return (BigInteger) num;
		}
		if (num instanceof Integer) {
			return BigInteger
					.valueOf(Long.valueOf(((Integer) num).longValue()));
		}
		if (num instanceof Long) {
			return BigInteger.valueOf((long) num);
		}
		if (num instanceof String) {
			return new BigInteger((String) num);
		}
		return null;
	}

//	public int getLastIndexOfChild(String parentId, RESOURCE_TYPE type) {
//
//		MongoCollection<Document> collection = context.getDatabaseManager()
//				.getCollection(collectionName);
//
//		BasicDBObject query = new BasicDBObject(PARENTID_KEY, parentId).append(
//				RESTYPE_KEY, type.Value());
//		BasicDBObject sort = new BasicDBObject(RESNAME_KEY, -1);
//
//		Document doc = collection.find(query).sort(sort).limit(1).first();
//		if (doc == null) {
//			return 0;
//		} else {
//			String name = doc.getString(RESNAME_KEY);
//			try {
//			return Integer.parseInt(name
//					.substring(type.toString().length() + 1));
//			} catch (Exception e) {
//				return 1;
//			}
//		}
//
//	}

	public boolean checkIfResourceExist(String acpId) {
		Document doc = null;
		if (OneM2mUtil.isUri(acpId)) {
			doc = getDocument(URI_KEY, acpId);
		} else {
			doc = getDocument(RESID_KEY, acpId);
		}
		return doc != null;
	}

}
