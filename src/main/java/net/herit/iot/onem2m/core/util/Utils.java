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

	public static String extractBaseurlFromUrl(String fullUrl) throws MalformedURLException {
		if (!fullUrl.substring(0, 5).equalsIgnoreCase("http:"))
			fullUrl = "http:"+ fullUrl;
		URL url = new URL(fullUrl);
		String file = url.getFile();
		String baseUri = fullUrl;
		if (file.length() > 0) {
			baseUri = fullUrl.substring(0, fullUrl.length() - file.length());			
		}
		return baseUri.substring(5);
		
	}

	public static String extractResourceFromUrl(String fullUrl) throws MalformedURLException {
		
		if (fullUrl.substring(0, 4).equalsIgnoreCase("http") == false) {
			fullUrl = "http:"+ fullUrl;
		}
		
		URL url = new URL(fullUrl);
		return url.getFile();
		
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
			
			e.printStackTrace();
			return RESOURCE_TYPE.NONE;
			
		}
	}

	
	
	
	
	public static void main(String[] args) throws Exception {
		String pc = "<?xml version=\"1.0\" encoding=\"utf-8\"?><m2m:cin xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-contentInstance-v1_2_0.xsd\" xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" name=\"CONTENT_INST_118\"><ty>4</ty><ri>/CONTENT_INST_118</ri>";
		//String pc = "dsddd<ty>4</ty>dddd";
		
		//String re = "\"ty\":\\s*\"?\\d+\"?\\s*,";
		String re = "<ty>\\d+</ty>";
		//String re = "<ty>4</ty>";
		Pattern p = Pattern.compile(re);
		Matcher m = p.matcher(pc);
		boolean b = m.find();
		if (b) {
			String matched = m.group();
			System.out.println("Matched:"+matched);
		} else {
			System.out.println("No matched string found:"+re);
			System.out.println(pc);
		}
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
		return resId.startsWith("//") || resId.startsWith("http://") || resId.startsWith("https://");
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

}
