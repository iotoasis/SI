package net.herit.iot.onem2m.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.Array;
import co.nstant.in.cbor.model.DataItem;
import co.nstant.in.cbor.model.SimpleValue;
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
//		if (!fullUrl.substring(0, 5).equalsIgnoreCase("http:"))
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
//		return baseUri.substring(5);
		return baseUri;
		
	}
	
	public static String extractBaseurlFromUrl(String fullUrl) throws MalformedURLException {
//		//System.out.println("fullUrl: " + fullUrl);
//		if (!fullUrl.substring(0, 5).equalsIgnoreCase("http:"))
		if (!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("http") && 
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("coap") &&
				!fullUrl.toLowerCase().substring(0, 4).equalsIgnoreCase("mqtt")) {
			fullUrl = "http:"+ fullUrl;
		}
		
		try {
			String url1 = fullUrl.substring(fullUrl.indexOf("//")+2);
//			//System.out.println(url1);
			String host;
			if(url1.indexOf("/") > 0) {
				host = url1.substring(0, url1.indexOf("/"));
			} else {
				host = url1;
			}
			String path = url1.substring(url1.indexOf("/"), url1.length());
//			//System.out.println(host);
//			//System.out.println(path);
			
			
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
//			//System.out.println(url1);
			String host;
			if(url1.indexOf("/") > 0) {
				host = url1.substring(0, url1.indexOf("/"));
			} else {
				host = url1;
			}
			String path = url1.substring(url1.indexOf("/"), url1.length());
//			//System.out.println(host);
//			//System.out.println(path);
			
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
//			//System.out.println("Matched:"+matched);
//		} else {
//			//System.out.println("No matched string found:"+re);
//			//System.out.println(pc);
//		}
		
		String addr = "http://10.101.101.33:8080/dkdkd/asdkd/dkdk";
		URL url = new URL(addr);
		
		String url1 = addr.substring(addr.indexOf("//")+2);
		//System.out.println(url1);
		String host = url1.substring(0, url1.indexOf("/"));
		String path = url1.substring(url1.indexOf("/"), url1.length());
		//System.out.println(host);
		//System.out.println(path);
		
		String file = url.getFile();
		//System.out.println(file);
		
		//System.out.println(url.getHost());
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
	
	public static boolean checkIfUnstructuredCseRelative(String to) {
		String[] arrItems = to.split("/");
		return arrItems.length == 2;
		
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
	
	// added for CBOR Byte array to JSON String in 2017-05-16
	
	private static HashMap getCborHashMap(DataItem dataItem) {
		HashMap hMap = new HashMap();
		
		co.nstant.in.cbor.model.Map map = (co.nstant.in.cbor.model.Map)dataItem;
		HashMap hashMap = new HashMap();
		Iterator<DataItem> itr = map.getKeys().iterator();
		
		while(itr.hasNext()) {
			
			DataItem key = itr.next();
			
			if(map.get(key).getMajorType().toString().equals("MAP")) {
				
				hMap.put(key.toString(), getCborHashMap(map.get(key)));
			} else if(map.get(key).getMajorType().toString().equals("ARRAY")) {
				Array cborArr = (Array)map.get(key);
				
				ArrayList<String> arr = new ArrayList<String>();
				for(int i = 0; i < cborArr.getDataItems().size(); i++) {
					arr.add( cborArr.getDataItems().get(i).toString() );
				}
				
				hMap.put(key.toString(), arr);
			} else if(map.get(key).getMajorType().name().contains("SPECIAL")){
				SimpleValue simpleValue = (SimpleValue)map.get(key);
				
				String temp = simpleValue.getSimpleValueType().toString().toLowerCase();
				if(temp.equals("true") || temp.equals("false")) {
					hMap.put(key.toString(), Boolean.getBoolean(temp));
				} else {
					hMap.put(key.toString(), temp);
				}
			} else {
				String value = map.get(key).toString();
				if(map.get(key).getMajorType().name().contains("INTEGER")) {
					hMap.put(key.toString(),Integer.parseInt(value) );
				} else {
					hMap.put(key.toString(), value);
				}
				
			}
		}
			
		return hMap;
	}
	
	// added for JSON String to CBOR Byte Stream in 2017-05-19
	
	private static DataItem getDataItem(HashMap<String, Object> map) {
		DataItem dataItem = null;
		
		co.nstant.in.cbor.model.Map cborMap = new co.nstant.in.cbor.model.Map();
		
		Set<?> set = map.entrySet();
		Iterator<?> iterator = set.iterator();
        String valueClassType="";
        while (iterator.hasNext()) 
        {
            Entry entry = (Entry) iterator.next();
            valueClassType = entry.getValue().getClass().getSimpleName();
            
            DataItem diKey = new co.nstant.in.cbor.model.UnicodeString(entry.getKey().toString());
            DataItem diValue = null;
            
            if(valueClassType.equals("LinkedHashMap")) {
            	HashMap<String, Object> subMap = (HashMap<String, Object>)entry.getValue();
            	diValue = getDataItem(subMap);
            } else if(valueClassType.equals("ArrayList")) {
            	ArrayList<Object> arrObjs = (ArrayList<Object>)entry.getValue();
            	Array diArr = new Array();
            	DataItem tmpDi;
            	for(Object obj : arrObjs ) {
            		tmpDi = new co.nstant.in.cbor.model.UnicodeString(obj.toString());
            		diArr.add(tmpDi);
            	}
            	diValue = (Array)diArr;
            } else if(valueClassType.equals("Boolean")) {
            	Boolean val = (Boolean)entry.getValue();
            	if(val) {
            		diValue = SimpleValue.TRUE;
            	} else {
            		diValue = SimpleValue.FALSE;
            	}
            } else {
            	if(entry.getValue().getClass().getSimpleName().contains("Integer")) {
            		int nVal = Integer.parseInt(entry.getValue().toString());
            		if(nVal >= 0) {
            			diValue = new co.nstant.in.cbor.model.UnsignedInteger(Long.parseLong(entry.getValue().toString()));
            		} else {
            			diValue = new co.nstant.in.cbor.model.NegativeInteger(Long.parseLong(entry.getValue().toString()));
            		}
            		
            	} else {
            		diValue = new co.nstant.in.cbor.model.UnicodeString(entry.getValue().toString());
            	}
            	
            }
            
            cborMap.put(diKey, diValue);
            
        }
        
		return cborMap;
	}
	
	public static String decodeCBOR(byte[] cbor) {
		String result = "";
		
		try {
			StringBuilder sb = new StringBuilder(cbor.length);
			for(byte b : cbor) {
				sb.append((char) b);
			}
			String hexText = sb.toString();
			
			byte[] bytes = new BigInteger(hexText, 16).toByteArray();
			
			InputStream inputStream = new ByteArrayInputStream(bytes);
			
			CborDecoder decoder = new CborDecoder(inputStream);
			
			
			List<DataItem> dataItems = decoder.decode();
			
			// added and test, then blocked in 2017-12-08 to test with NTELS, it should be deleted after that.
			//DataItem dt = dataItems.get(0);
			////System.out.println("######### dt===>" + dt.toString());
			//return dt.toString();
		
			HashMap hashMap = new HashMap();
			
			hashMap = getCborHashMap(dataItems.get(1));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("__message__", hashMap);
			
			jsonObject = (JSONObject)jsonObject.get("__message__");
			
			result = jsonObject.toString();
			
		}catch(CborException ex) {
			return null;
		}
		
		return result;
		
	}
	
	public static byte[] encodeCBOR(String json) {
		byte[] result = {};
		try {
			ObjectMapper mapper = new ObjectMapper();
			
			HashMap<String, Object> map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});
			
			DataItem dataItem = getDataItem(map);
			
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			CborEncoder encoder = new CborEncoder(byteOutputStream);
			encoder.encode(dataItem);
			
			byte[] cborBytes = byteOutputStream.toByteArray();
			
			StringBuffer sb = new StringBuffer();
			
			//String cborEncodedStr = new BigInteger(cborBytes).toString(16);
			for(int i = 0; i < cborBytes.length; i++) {
				sb.append(Integer.toHexString(0x0100 + (cborBytes[i] & 0x00FF)).substring(1));

			}
			
			result = sb.toString().getBytes();
			
		}catch(Exception ex) {
			return null;
		}
		
		return result;
	}
	
	// added in 2017-08-04
    public static String getTimeStamp() {
		String rtnStr = null;
	
		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMddhhmmssSSS";
	
		try {
		    SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		    Timestamp ts = new Timestamp(System.currentTimeMillis());
	
		    rtnStr = sdfCurrent.format(ts.getTime());
		} catch (Exception e) {
		    //e.printStackTrace();
			
		    //throw new RuntimeException(e);	// 보안점검 후속조치
		}
		return rtnStr;
    }

}
