package net.herit.iot.onem2m.incse.facility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.Originator;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;
import net.herit.iot.onem2m.core.util.OneM2MException;
import net.herit.iot.onem2m.incse.context.OneM2mContext;
import net.herit.iot.onem2m.incse.manager.dao.AccessControlPolicyDAO;
import net.herit.iot.onem2m.incse.manager.dao.ResourceDAO;
import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.AccessControlRule;
import net.herit.iot.onem2m.resource.AccessControlRule.AccessControlContexts;
import net.herit.iot.onem2m.resource.AnnouncedResource;
import net.herit.iot.onem2m.resource.CSEBase;
import net.herit.iot.onem2m.resource.RegularResource;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.onem2m.resource.SetOfAcrs;


public class OneM2mUtil {
	
	private static Logger log = LoggerFactory.getLogger(OneM2mUtil.class);
	
	public static String createRequestId() {
		return "REQ_"+UUID.randomUUID().toString();
	}

//	public static boolean isCSEBaseIdOrCSEBaseName(String resId) {
//		if (resId.startsWith(CfgManager.getInstance().getCSEBaseCid()) || resId.startsWith("/"+CfgManager.getInstance().getCSEBaseName())) {
//			return true;
//		} else {
//			return false;
//		}
//	}
//	
	public static boolean isUri(String resId) {
		if (resId.startsWith(CfgManager.getInstance().getCSEBaseCid() + "/" +CfgManager.getInstance().getCSEBaseName())) {
			return true;
		} else {
			return false;
		}
	}

	public static String getParentUri(String uri) {
		
		int idx = uri.lastIndexOf("/");
		return uri.substring(0, idx);
		
	}

	
	public static boolean checkIfParentAllowed(Resource parent, String allowed) {
		String typeName = RESOURCE_TYPE.get(parent.getResourceType()).Name();
		return allowed.indexOf(typeName) >= 0;
	}
	
	/*protected static class Originator {
		private String DOMAIN_PREFIX = "//";
		private String ID_PREFIX = "/";
		public String domain = "";
		public String uri = "";
		public String resourceId = "";
		public Originator(String from) {
			
			if (from.startsWith(DOMAIN_PREFIX)) {
				int stId = from.indexOf(ID_PREFIX, DOMAIN_PREFIX.length());
				domain = from.substring(0, stId);
				String id = from.substring(stId); 
				if (id.startsWith(CfgManager.getInstance().getCSEBaseUri())) {
					uri = id;
					//resourceId = convertUriToResourceId(uri);
					resourceId = null;
				} else {
					resourceId = id;
					//uri = convertResourceIdToUri(resourceId);
					uri = null;
				}
			}
			
		}
	}*/
	
	//void checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.CREATE, getAccessControlPolicies(parent)) {
	// create:1, retrieve:2, update:4, delete:8, discovery:16, n:32
	public static boolean checkAccessControlPolicy(OneM2mRequest req, OPERATION op, List<AccessControlPolicy> acps) {
		
		String from = req.getFrom();
		
		OneM2mRequest.Originator ori = req.getOriginator();
		
	//	System.out.println("[ACP]====> ori=" + ori.getSPRelativeStructuredId() + ", from=" + from + ", req.getOriginator()=" + ori.toString());
		
		if (acps == null || acps.size() == 0) {
			return true;
		}

		try {
			Iterator<AccessControlPolicy> it = acps.iterator();
			
			while (it.hasNext()) {
				AccessControlPolicy acp = it.next();
				System.out.println("[ACP] acp========>" + acp.toString());
				if (checkAcessControlRule(acp.getPrivileges().getAccessControlRule(), op, ori, from)) {
					return true;
				}
				
			}
		} catch (Exception e) {
			log.debug("Handled exception", e);
		}
		
		return false;
		
	}

	public static boolean checkAcessControlRule(List<AccessControlRule> acrList, OPERATION op, Originator ori, String from) {
		
		Iterator<AccessControlRule> acrIt = acrList.iterator();
		while (acrIt.hasNext()) {
			System.out.println("[ACP]checkAcessControlRule.............");	
			if (checkAcessControlRule(acrIt.next(), op, ori, from)) {
				return true;
			}
		}
		
		System.out.println("checkAcessControlRule] return false..");
		
		return false;
	}

	//void checkAccessControlPolicy(reqMessage.getFrom(), OPERATION.CREATE, getAccessControlPolicies(parent)) {
	// create:1, retrieve:2, update:4, delete:8, discovery:16, n:32
	public static boolean checkAccessControlPolicy(OneM2mRequest req, OPERATION op, SetOfAcrs acrs) {
		
		String from = req.getFrom();

		OneM2mRequest.Originator ori = req.getOriginator();		
		
		if (acrs == null) {
			return true;
		}
		List<AccessControlRule> acrList = acrs.getAccessControlRule();

		if(acrList == null) return true; //2016.05.10 added..
		
//		if (checkAcessControlRule(acrs.getAccessControlRule(), op, ori, from)) { // 2016.05.10 modified..
		if (checkAcessControlRule(acrList, op, ori, from)) {
			return true;
		}
		
	//	System.out.println("checckAccessControlPolicy] return false..");
		
		return false;
		
	}
/*	
	public static boolean checkAcessControlRule(AccessControlRule acr, OPERATION op, Originator ori, String from) {
		
		if ((acr.getAccessControlOperations() & op.Value()) != op.Value()) {
			System.out.println("return false ..1");
			return false;
		}

		if(acr.getAccessControlOriginators() == null) return true;
		
		Iterator<String> idIt = acr.getAccessControlOriginators().iterator();
		while (idIt.hasNext()) {
			String aOri = idIt.next();
			//System.out.println("[ACP]=====> aOri=" + aOri + ", ori.getSPRelativeStructuredId()=" + ori.getSPRelativeStructuredId());
			
			if (ori != null) {
				if (aOri.equalsIgnoreCase("all") || 
						aOri.equalsIgnoreCase("*") || 
						aOri.equalsIgnoreCase(ori.getServiceProviderId()) || ori.isSameResource(aOri)) {
					
					//return true;
					// added in 2017-08-24
					Iterator<AccessControlContexts> accoIt = acr.getAccessControlContexts().iterator();
					
					if(!accoIt.hasNext()) {		// added in 2017-08-25, return true if no AccessControlContexts
						return true;
					}
					
					while(accoIt.hasNext()) {
						AccessControlContexts acco = accoIt.next();
						List<String> ipv4s = acco.getAccessControlIpAddresses().getIpv4Addresses();
						
						if(ipv4s != null && ipv4s.size() > 0) {
							Iterator<String> ipv4It = ipv4s.iterator();
							while(ipv4It.hasNext()) {
								String aIp4 = ipv4It.next();
								if(aIp4.equals(ori.getOriginIp())) {
									return true;
								}
							}
						} 
						
						List<String> ipv6s = acco.getAccessControlIpAddresses().getIpv6Addresses();
						
						if(ipv6s != null && ipv6s.size() > 0) {
							Iterator<String> ipv6It = ipv6s.iterator();
							while(ipv6It.hasNext()) {
								String aIp6 = ipv6It.next();
								if(aIp6.equals(ori.getOriginIp())) {
									return true;
								}
							}
						} 
					}
										
				}
			} else {

				if (aOri.equalsIgnoreCase("all") || 
						aOri.equalsIgnoreCase("*") || 
						aOri.equalsIgnoreCase(from)) {							
					
					return true;
				}	
			}
		}
		
//		System.out.println("return false ..2");
		return false;
	}
*/
	private static boolean checkPathMatch(String pattern, String path) {
		
		if(!pattern.contains("*")) return false;
				
		org.springframework.util.AntPathMatcher pathMatcher = new org.springframework.util.AntPathMatcher();
		
		Comparator<String> comp = pathMatcher.getPatternComparator(path);
		
		if(pathMatcher.match(pattern, path)) {
            return true;
        } 
        	
		return false;
        
	}
	
	public static boolean checkAcessControlRule(AccessControlRule acr, OPERATION op, Originator ori, String from) {
		
		if ((acr.getAccessControlOperations() & op.Value()) != op.Value()) {
			System.out.println("return false ..1");
			return false;
		}
		
		boolean bCheckOrigin = false, bCheckOption = false;

		
		Iterator<String> idIt = acr.getAccessControlOriginators().iterator();
		
		while (idIt.hasNext()) {
			String aOri = idIt.next();
			
			if (ori != null) {
				if (aOri.equalsIgnoreCase("all") || 
						aOri.equalsIgnoreCase("*") || 
						aOri.equalsIgnoreCase("/*") ||			// added in 2017-08-28, refer to Table 7.1.3-1 in TS-0003  
						aOri.equalsIgnoreCase(ori.getServiceProviderId()) || ori.isSameResource(aOri)) {
					
					//return true;
					bCheckOrigin = true;
					break;					
				} else {			// added in 2017-08-28 to check pattern including asterisk in the path.
					
					if(checkPathMatch(aOri, ori.getAbsoluteStructuredId()) 
							|| checkPathMatch(aOri, ori.getServiceProviderId()) 
							|| checkPathMatch(aOri, ori.getSPRelativeStructuredId()) 
							|| checkPathMatch(aOri, ori.getSPRelativeUnstructuredId())) {
						
						bCheckOrigin = true;
						break;
					}
				}
			} else {

				if (aOri.equalsIgnoreCase("all") || 
						aOri.equalsIgnoreCase("*") || 
						aOri.equalsIgnoreCase("/*") ||			// added in 2017-08-28, refer to Table 7.1.3-1 in TS-0003
						aOri.equalsIgnoreCase(from)) {							
					
					//return true;
					bCheckOrigin = true;
					break;
				}	
			}
		}
		
		// added in 2017-08-25
		if(acr == null || acr.getAccessControlContexts() == null) {	// added in 2017-10-24 for TTA
			return true;
		}
		
		Iterator<AccessControlContexts> accoIt = acr.getAccessControlContexts().iterator();
		
		if(!accoIt.hasNext()) {		// added in 2017-08-25, return true if no AccessControlContexts
			bCheckOption = true;
		} else {
			while(accoIt.hasNext()) {
				
				boolean bIpAddr = false, bWindows = false;
				
				AccessControlContexts acco = accoIt.next();
				
				if(acco.getAccessControlIpAddresses() != null) {
					List<String> ipv4s = acco.getAccessControlIpAddresses().getIpv4Addresses();
					
					if(ipv4s != null && ipv4s.size() > 0) {
						Iterator<String> ipv4It = ipv4s.iterator();
						while(ipv4It.hasNext()) {
							String aIp4 = ipv4It.next();
							if(aIp4.equals(ori.getOriginIp())) {
								bIpAddr = true;
								break;
							}
						}
					} 
					
					List<String> ipv6s = acco.getAccessControlIpAddresses().getIpv6Addresses();
					
					if(ipv6s != null && ipv6s.size() > 0) {
						Iterator<String> ipv6It = ipv6s.iterator();
						while(ipv6It.hasNext()) {
							String aIp6 = ipv6It.next();
							if(aIp6.equals(ori.getOriginIp())) {
								bIpAddr = true;
								break;
							}
						}
					} 
				} else {
					bIpAddr = true;
				}
				
				List<String> actws = acco.getAccessControlWindow();
				
				if(actws != null && actws.size() > 0) {
					Date date;
					CronExpression exp;
					Iterator<String> actwIt = actws.iterator();
					
					try {
						while(actwIt.hasNext()) {
							String actw = actwIt.next();
							
							exp = new CronExpression(actw);
							date = exp.getNextValidTimeAfter(new Date());
							
							if(date != null) {
								bWindows = true;
								break;
							}
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				} else {
					bWindows = true;
				}
				
				if(bIpAddr && bWindows) {   // set true if ipaddress and controlwindows are both true
					bCheckOption = true;
					break;
				}
			}
			
		}
		
		if(bCheckOrigin && bCheckOption) {
			return true;
		} 
		
//		System.out.println("return false ..2");
		return false;
	}
	
	public static List<AccessControlPolicy> extractAccessControlPolicies(Resource res, OneM2mContext context) throws OneM2MException {
		List<String> acpIds = null;
		if (res instanceof RegularResource) {
			RegularResource res2 = (RegularResource)res;
			acpIds = res2.getAccessControlPolicyIDs();
		} else if (res instanceof AnnouncedResource) {
			AnnouncedResource res2 = (AnnouncedResource)res;
			acpIds = res2.getAccessControlPolicyIDs();
		} else if (res instanceof CSEBase) {
			CSEBase res2 = (CSEBase)res;
			acpIds = res2.getAccessControlPolicyIDs();
		}
		
		return extractAccessControlPolicies(acpIds, context);
		
	}
	
	public static List<AccessControlPolicy> extractAccessControlPolicies(List<String> acpIds, OneM2mContext context) throws OneM2MException {
		
		ArrayList<AccessControlPolicy> acps = new ArrayList<AccessControlPolicy>();
		if (acpIds != null) {
			Iterator<String> it = acpIds.iterator();
			while (it.hasNext()) {
				AccessControlPolicyDAO dao = new AccessControlPolicyDAO(context);
				AccessControlPolicy acp = (AccessControlPolicy) dao.retrieve(it.next(), null);
				if (acp != null) {
					acps.add(acp);
				}
			}
		}
		//System.out.println("[ACP]===========> acps.size()=" + acps.size());
		return acps;
	}

	public static boolean isXMLContentType(CONTENT_TYPE contentType) {
		if (contentType == null) {
			return false;
		}
		switch (contentType) {
		case XML:
		case RES_XML:
		case NTFY_XML:
		case ATTRS_XML:
			return true;

		case ATTRS_JSON:
		case NTFY_JSON:
		case RES_JSON:
		case JSON:
		default:
			return false;
		}
	}
//
//	public static String extractBaseurlFromUrl(String fullUrl) throws MalformedURLException {
//		if (!fullUrl.substring(0, 5).equalsIgnoreCase("http:"))
//			fullUrl = "http:"+ fullUrl;
//		URL url = new URL(fullUrl);
//		String file = url.getFile();
//		String baseUri = fullUrl;
//		if (file.length() > 0) {
//			baseUri = fullUrl.substring(0, fullUrl.length() - file.length());			
//		}
//		return baseUri.substring(5);
//		
//	}
//
//	public static String extractResourceFromUrl(String fullUrl) throws MalformedURLException {
//
//		fullUrl = "http:"+ fullUrl;
//		URL url = new URL(fullUrl);
//		return url.getFile();
//		
//	}
//
//	public static boolean checkIfSameHost(String notiUri, String remoteHost) throws MalformedURLException {
//		
//		if (remoteHost == null || notiUri == null)	return false;
//		
//		URL noti = new URL(notiUri);
//		return remoteHost.indexOf(noti.getHost()) >= 0;
//		
//	}
//	
//	public static boolean checkIfExpired(String time) {
//		if (time == null || time.length() == 0)	return false;
//		
//		LocalDateTime now = LocalDateTime.now();
//		LocalDateTime exp = LocalDateTime.parse(time, DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss"));
//		return exp.isBefore(now);
//	}
//	
//	public static boolean isSuccessResponse(OneM2mResponse res) {
//		
//		return res.getResStatusCode() == RESPONSE_STATUS.ACCEPTED || 
//				res.getResStatusCode() == RESPONSE_STATUS.OK || 
//				res.getResStatusCode() == RESPONSE_STATUS.CREATED || 
//				res.getResStatusCode() == RESPONSE_STATUS.DELETED || 
//				res.getResStatusCode() == RESPONSE_STATUS.CHANGED; 
//	
//	}

//	public static Object extractOriginator(String from, OneM2mContext context) {
//		
//		return null;
//		
//	}

	private static String toSPRelativeIfAbsolute(String from) {
		String baseHost = CfgManager.getInstance().getServiceProviderId();	// //iot.herit.net
		if (from.startsWith(baseHost)) {
			from = from.substring(baseHost.length());
		}
		
		return from;
	}
	
	public static String extractServiceProviderId(String from) {
		if (from.startsWith("//")) {
			int eod = from.indexOf("/", 2);
			if (eod > 0) {
				return from.substring(0, eod);
			} else {
				return from;
			}
		}
		return null;
	}

	public static String extractResourceId(String from) {
		String spId = OneM2mUtil.extractServiceProviderId(from);
		from = (spId != null ? from.substring(spId.length()+1) : from);
		
		if (from.startsWith("/")) from = from.substring(1);
		
		return from;
	}
	
	public static String toUriOrResourceId(String from) {
		if(from.startsWith(CfgManager.getInstance().getCSEBaseCid())) {
			from = from.substring(CfgManager.getInstance().getCSEBaseCid().length()+1);
		}
		if(from.startsWith(CfgManager.getInstance().getServiceProviderId())) {
			from = toSPRelativeIfAbsolute(from);
		}
		
		if(from.startsWith(CfgManager.getInstance().getCSEBaseName())) {
			from = CfgManager.getInstance().getCSEBaseCid() + "/" + from;
		}
		
		return from;
	}
	
//
//	public static boolean laterThan(String time1, String time2) {
//		
//		return time1.compareTo(time2) > 0;
//				
//	}

	public static RESOURCE_TYPE getResTypeWithContentString(String pc,
			CONTENT_TYPE contentType) {
		
		try {
			if (contentType == null)	contentType = CONTENT_TYPE.XML;
			
			String re = "";
			switch (contentType) {
			case JSON:
			case RES_JSON:
			case ATTRS_JSON:
				re = "\"ty\":\\s*\"?\\d+\"?\\s*,";
				break;
			case XML:
			case RES_XML:
			case ATTRS_XML:
				re = "<ty>\\d+</ty>";
				break;
			default:
				re = "<ty>\\d+</ty>";			
			}
			Pattern p = Pattern.compile(re);
			Matcher m = p.matcher(pc);
			
			if (m.find()) {
				String matched = m.group();
				
				p = Pattern.compile("\\d+");
				m = p.matcher(matched);
				if (m.find()) {
					String ty = m.group();
					return RESOURCE_TYPE.get(Integer.parseInt(ty));
				}
			}

			return RESOURCE_TYPE.NONE;
			
		} catch (Exception e) {
			
			log.debug("Handled exception", e);
			return RESOURCE_TYPE.NONE;
			
		}
	}

	public static void main(String[] args) throws Exception {
//		String pc = "<?xml version=\"1.0\" encoding=\"utf-8\"?><m2m:cin xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-contentInstance-v1_2_0.xsd\" xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" name=\"CONTENT_INST_118\"><ty>4</ty><ri>/CONTENT_INST_118</ri>";
//		//String pc = "dsddd<ty>4</ty>dddd";
//		
//		//String re = "\"ty\":\\s*\"?\\d+\"?\\s*,";
//		String re = "<ty>\\d+</ty>";
//		//String re = "<ty>4</ty>";
//		Pattern p = Pattern.compile(re);
//		Matcher m = p.matcher(pc);
//		boolean b = m.find();
//		if (b) {
//			String matched = m.group();
//			System.out.println("Matched:"+matched);
//		} else {
//			System.out.println("No matched string found:"+re);
//			System.out.println(pc);
//		}
		
//		String id = "/herit-in/herit-cse/test";
//		System.out.println(toUriOrResourceId(id));
	
		String from = "//herit-cse";
		
		System.out.println("eod=" + OneM2mUtil.extractServiceProviderId(from));
	}

	public static String extractCseIdFromSPResId(String spResId) {
		// SP-Relative-ResourceId: /<cseid>/...		
		int second = spResId.indexOf("/", 1);
		if (second >= 0) {
			return spResId.substring(0, second);
		} else {
			return spResId;
		}
	}
//
//	public static boolean checkIfAbsoluteResId(String resId) {
//		return resId.startsWith("//") || resId.startsWith("http://") || resId.startsWith("https://");
//	}
//
//	

	public static Originator createOriginator(OneM2mRequest reqMessage, OneM2mContext context, String originIp) {		// updated in 2017-08-25
		
		try {
			String from = reqMessage.getFrom();
			String structuredId = "", unstructuredId = "", serviceProviderId = "";
			serviceProviderId = OneM2mUtil.extractServiceProviderId(from);
			String resourceId = serviceProviderId != null ? from.substring(serviceProviderId.length()) : from;
			
			if (serviceProviderId == null) CfgManager.getInstance().getServiceProviderId();
			ResourceDAO dao = new ResourceDAO(context);
			
			Resource res = dao.getResourceWithID(resourceId);
			if (res != null) {
				structuredId = res.getUri();
				unstructuredId = res.getResourceID();
			} else {
//				if (OneM2mUtil.isUri(resourceId)) {   // 2015.09.14 modified.
				if (resourceId.startsWith("/" + CfgManager.getInstance().getCSEBaseName())) {
					// structuredId = resourceId.substring(1);		// remove "/"  blocked in 2017-08-24 sp-relative-structuredId should start with '/' 
					structuredId = resourceId;				// added in 2017-08-24
				} else {
					unstructuredId = resourceId;
				}
			}
			//Originator ori = reqMessage.new Originator(structuredId, unstructuredId, serviceProviderId);
			Originator ori = null;
			if(originIp != null && !originIp.equals("")) {	// added in 2017-08-25	
				ori = reqMessage.new Originator(structuredId, unstructuredId, serviceProviderId, originIp); 
			} else {
				ori = reqMessage.new Originator(structuredId, unstructuredId, serviceProviderId);
			}
			return ori;
		} catch (OneM2MException e) {
			
			log.debug("Handled exception", e);
		
		} catch (Exception e) {
			
			log.debug("Handled exception", e);
			
		}
		return null;
	}

}

