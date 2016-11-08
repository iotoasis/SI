package net.herit.iot.onem2m.core.util;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import net.herit.iot.onem2m.resource.AccessControlPolicy;
import net.herit.iot.onem2m.resource.Resource;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.OPERATION;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;

public class Utils {

	private static Logger log = LoggerFactory.getLogger(Utils.class);
	
	public static String createRequestId() {
		return "REQ_"+UUID.randomUUID().toString();
	}

	public static String getParentUri(String uri) {
		
		int idx = uri.lastIndexOf("/");
		return uri.substring(0, idx);
		
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

	public static String extractBaseurlFromUrl2(String fullUrl) throws MalformedURLException {
		if (!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("http") && 
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("coap") &&
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("mqtt")) {
			fullUrl = "http:"+ fullUrl;
		}
		
		URL url = new URL(fullUrl);
		String file = url.getFile();
		String baseUri = fullUrl;
		if (file.length() > 0) {
			baseUri = fullUrl.substring(0, fullUrl.length() - file.length());			
		}
		return baseUri;
		
	}
	
	public static String extractBaseurlFromUrl(String fullUrl) throws MalformedURLException {
		System.out.println("fullUrl: " + fullUrl);
//		if (!fullUrl.substring(0, 5).equalsIgnoreCase("http:"))
		if (!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("http") && 
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("coap") &&
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("mqtt")) {
			fullUrl = "http:"+ fullUrl;
		}
		
		try {
			String url1 = fullUrl.substring(fullUrl.indexOf("//")+2);
			System.out.println(url1);
			String host;
			if(url1.indexOf("/") > 0) {
				host = url1.substring(0, url1.indexOf("/"));
			} else {
				host = url1;
			}
			String path = url1.substring(url1.indexOf("/"), url1.length());
			System.out.println(host);
			System.out.println(path);
			
			
			String baseUri = fullUrl;
			if (path.length() > 0) {
				baseUri = fullUrl.substring(0, fullUrl.length() - path.length());			
			}

			return baseUri;
		} catch(Exception e) {
			throw new MalformedURLException("URL format Exception.");
		}
		
	}

	public static String extractResourceFromUrl2(String fullUrl) throws MalformedURLException {
		
//		if (fullUrl.substring(0, 4).equalsIgnoreCase("http") == false) {
//			fullUrl = "http:"+ fullUrl;
//		}
		
		if (!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("http") && 
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("coap") &&
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("mqtt")) {
			fullUrl = "http:"+ fullUrl;
		}
		
		URL url = new URL(fullUrl);
		return url.getFile();
		
	}
	
	public static String extractResourceFromUrl(String fullUrl) throws MalformedURLException {
		
//		if (fullUrl.substring(0, 4).equalsIgnoreCase("http") == false) {
//			fullUrl = "http:"+ fullUrl;
//		}
		
		if (!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("http") && 
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("coap") &&
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("mqtt")) {
			fullUrl = "http:"+ fullUrl;
		}
		
		try {
			String url1 = fullUrl.substring(fullUrl.indexOf("//")+2);
			System.out.println(url1);
			String host;
			if(url1.indexOf("/") > 0) {
				host = url1.substring(0, url1.indexOf("/"));
			} else {
				host = url1;
			}
			String path = url1.substring(url1.indexOf("/"), url1.length());
			System.out.println(host);
			System.out.println(path);
			
			return path;
		} catch(Exception e) {
			throw new MalformedURLException("URL format Exception.");
		}
		
	}

	public static boolean checkIfSameHost(String notiUri, String remoteHost) throws MalformedURLException {
		
		if (remoteHost == null || notiUri == null)	return false;
		
		//TODO: resource id 경우 체크 하는 부분....
		URL noti = new URL(notiUri);
		return remoteHost.indexOf(noti.getHost()) >= 0;
		
	}
	
	public static boolean checkIfExpired(String time) {
		if (time == null || time.length() == 0)	return false;
		
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime exp = LocalDateTime.parse(time, DateTimeFormat.forPattern("yyyyMMdd'T'HHmmss"));
		return exp.isBefore(now);
	}
	
	public static boolean isSuccessResponse(OneM2mResponse res) {
		
		return res.getResponseStatusCodeEnum() == RESPONSE_STATUS.ACCEPTED || 
				res.getResponseStatusCodeEnum() == RESPONSE_STATUS.OK || 
				res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CREATED || 
				res.getResponseStatusCodeEnum() == RESPONSE_STATUS.DELETED || 
				res.getResponseStatusCodeEnum() == RESPONSE_STATUS.CHANGED; 
	
	}

//	public static Object extractOriginator(String from, OneM2mContext context) {
//		
//		return null;
//		
//	}


	public static boolean laterThan(String time1, String time2) {
		
		return time1.compareTo(time2) > 0;
				
	}

	// get resource type by content type
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
		
		String addr = "http://10.101.101.33:8080/dkdkd/asdkd/dkdk";
		URL url = new URL(addr);
		
		String url1 = addr.substring(addr.indexOf("//")+2);
		System.out.println(url1);
		String host = url1.substring(0, url1.indexOf("/"));
		String path = url1.substring(url1.indexOf("/"), url1.length());
		System.out.println(host);
		System.out.println(path);
		
		String file = url.getFile();
		System.out.println(file);
		
		System.out.println(url.getHost());
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

	public static boolean checkIfAbsoluteResId(String resId) {
		return resId.startsWith("//") || resId.startsWith("http://") || resId.startsWith("https://")
				|| resId.startsWith("coap://") || resId.startsWith("coaps://")
				|| resId.startsWith("mqtt://") || resId.startsWith("mqtts://");
	}
	
	public static String format(String xml) {

        try {
            final InputSource src = new InputSource(new StringReader(xml));
            final Node document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(src).getDocumentElement();
            final Boolean keepDeclaration = Boolean.valueOf(xml.startsWith("<?xml"));

            //May need this: System.setProperty(DOMImplementationRegistry.PROPERTY,"com.sun.org.apache.xerces.internal.dom.DOMImplementationSourceImpl");


            final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            final LSSerializer writer = impl.createLSSerializer();

            writer.getDomConfig().setParameter("format-pretty-print", Boolean.TRUE); // Set this to true if the output needs to be beautified.
            writer.getDomConfig().setParameter("xml-declaration", keepDeclaration); // Set this to true if the declaration is needed to be outputted.

            return writer.writeToString(document);
        } catch (Exception e) {
            return xml;
        }
    }
	
	/**
	 * 
	 * authenticate
	 * 
	 **/

	// authenticate
	public static void authenticate( String[] bypassList, FullHttpRequest request ) throws OneM2MException {

		boolean isAskingAuthFromReq = request.headers().get("Authorization") != null && request.headers().get("Authorization").length() > 0;
		boolean isAskingAuthFromRes = bypassList.length > 0;

		if( isAskingAuthFromReq && isAskingAuthFromRes ){
			Utils.checkAuthentication( bypassList, request );
		} else if( isAskingAuthFromReq && !isAskingAuthFromRes ) {
			log.debug("++The authentication request has been detected. But no need to check it.");
		} else if( !isAskingAuthFromReq && isAskingAuthFromRes ) {
			throw new OneM2MException(RESPONSE_STATUS.INVALID_ARGUMENTS,"++There is no authentication header from the request.");
		} else {
			log.debug("++Not using way of authentication.");
		}

	}

	// isOnCondiotionForBypass
	public static boolean isOnCondiotionForBypass( String[] bypassList, FullHttpRequest request ) {
		for( int i=0; i<bypassList.length; i++ ){
			if( request.headers().get("X-M2M-Origin").equals(bypassList[i]) ){
				log.debug("++Condition has passed.");
				return true;
			}
		}
		return false;
	}
	
	
	// check authentication
	public static void checkAuthentication( String[] bypassList, FullHttpRequest request ) throws OneM2MException {
		
		// bypass 리스트에 존재 하지 않는 Origin 일 때,
		if( !isOnCondiotionForBypass( bypassList, request ) ){
			String authorizationEncoded = request.headers().get("Authorization");
			if( authorizationEncoded.indexOf(" ") > -1 ){
				authorizationEncoded = authorizationEncoded.substring(authorizationEncoded.indexOf(" ")+1);
			}
			String authorizationDecoded = new String(Base64.decodeBase64(authorizationEncoded));
			
			String[] _idpw = authorizationDecoded.split(":");
			if( _idpw.length == 2 ){

				// db 컬렉션 연결
				MongoCollection<Document> authData = DatabaseManager.getInstance().getMongoDB().getCollection("authorization");
				
				// db 조회
				Document doc = new Document();
				doc.put(Naming.AUTHENTICATION_ID, _idpw[0]);
				doc.put(Naming.AUTHENTICATION_PASSWORD, _idpw[1]);
				MongoCursor<Document> cursor = authData.find(doc).iterator();
				
				if( !cursor.hasNext() ){
					authData.insertOne(doc);
					System.out.println("-------------------------------------========================---------   INSERTED");
				} else {
					
					System.out.println("-------------------------------------========================---------   has passed");
				}
				
				
				//throw new OneM2MException(RESPONSE_STATUS.NO_PRIVILEGE, "You don't have permission for access.");
			} else {
				throw new OneM2MException(OneM2mResponse.RESPONSE_STATUS.INVALID_ARGUMENTS,"Invalid authorization");
			}		
		}
	}
	
	/*
	public boolean isBypassInList( String origin ){
		if( bypassList != null && bypassList.length > 0 ){
			for( int i=0; i<bypassList.length; i++ ){
				//if( request.headers().get("X-M2M-Origin").equals(bypassList[i]) ){
				if( origin.equals(bypassList[i]) ){
					isInListBypass = true;
					break;
				}
			}
			if( !isInListBypass ){
				checkAuthentication( request );
			}
		} else {
			checkAuthentication( request );
		}
	}
	*/
	
	/**
	 * 
	 *  end authenticate functions
	 * 
	 **/
	
	public static String getValueOfResponse(String xml, String path){
		Node node = null;
		String make_xml = combineString(xml.substring(0,xml.indexOf(">")+1),"<root>",xml.substring(xml.indexOf(">")+1),"</root>");
		try {
		    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(make_xml.getBytes()));
		    doc.getDocumentElement().normalize();
		    XPath xpath = XPathFactory.newInstance().newXPath();
		    node = (Node) xpath.evaluate("//pc/sgn/nev/rep/cin/"+path, doc, XPathConstants.NODE);
		} catch( Exception e ) {
		    Log.e("GET VALUE >> ", e.toString());
		}
		return node.getTextContent();
	}
	
	public static String combineString(String... str){
		StringBuffer sb = new StringBuffer();
		for( int i=0; i<str.length; i++ ) {
		    sb.append(str[i]);
		}
        	return sb.toString();
    	}
	
	// new function
	// 161103 temp
	public Object getObject(){
		// base
		System.out.println("test");
		Object obj = new Object();
		return obj;
	}
	public Object getObject(int value){
		// execute base first
		Object obj = getObject();
		
		// execute other
		if(value > 0){
			// +
		} else if( value == 0 ) {
			// 0
		} else {
			// -
		}
		return obj;
	}

}


