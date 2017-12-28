package net.herit.iot.onem2m.bind.http.codec;

import java.net.URI;
import java.util.List;
import java.util.Map.Entry;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse;
import net.herit.iot.message.onem2m.OneM2mRequest.RESOURCE_TYPE;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.bind.http.status.OneM2mResponseStatus;
import net.herit.iot.onem2m.core.util.OneM2MException;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

public class HttpResponseCodec extends HttpAbsCodec {
//	protected final static String HTTP_HEADER_REQUESTID 			= "X-M2M-RI";
//	protected final static String HTTP_HEADER_FROM 					= "X-M2M-Origin";
//	protected final static String HTTP_HEADER_ORIGIN_TIME 			= "X-M2M-OT";
//	protected final static String HTTP_HEADER_RESULT_EXPIRETIME 	= "X-M2M-RST";
//	protected final static String HTTP_HEADER_EVENT_CATEGORI 		= "X-M2M-EC";
	
	private final static String HTTP_HEADER_RESPONSE_STATUS		= "X-M2M-RSC";

	protected final static String HTTP_HEADER_CONTENT_LOCATION 	= "Content-Location";
	protected final static String HTTP_HEADER_ETAG 				= "Etag";
	
	public static DefaultFullHttpResponse encode(OneM2mResponse resMessage, HttpVersion version) throws Exception {
		RESPONSE_STATUS resCode = resMessage.getResponseStatusCodeEnum();
		
		DefaultFullHttpResponse response = null;
		HttpResponseStatus httpStatus = OneM2mResponseStatus.valueOf(resCode);
		byte[] content = resMessage.getContent();
		if(content != null) {
			
			// Added to support application/json in 2017-05-22
			if(resMessage.getContentType().Name().toLowerCase().contains("cbor")) {
				String strContent = new String(content, "UTF-8");
				byte[] cbor = net.herit.iot.onem2m.core.util.Utils.encodeCBOR(strContent);
				response = new DefaultFullHttpResponse(version, httpStatus, Unpooled.copiedBuffer(cbor));
				response.headers().set(HTTP_HEADER_CONTENT_LENGTH, cbor.length);
			} else {
				response = new DefaultFullHttpResponse(version, httpStatus, Unpooled.copiedBuffer(content));
				response.headers().set(HTTP_HEADER_CONTENT_LENGTH, content.length);
			}
			//response = new DefaultFullHttpResponse(version, httpStatus, Unpooled.copiedBuffer(content));			// blocked in 2017-05-22
			//response.headers().set(HTTP_HEADER_CONTENT_LENGTH, content.length);									// blocked in 2017-05-22
		} else {
			response = new DefaultFullHttpResponse(version, httpStatus);
		}

		response.headers().add(HTTP_HEADER_RESPONSE_STATUS, resCode.Value());

		if(resMessage.getRequestIdentifier() != null) {
			response.headers().add(HTTP_HEADER_REQUESTID, resMessage.getRequestIdentifier());
		}
		if(resMessage.getFrom() != null) {
			response.headers().add(HTTP_HEADER_FROM, resMessage.getFrom());
		}
		if(resMessage.getOriginatingTimestamp() != null) {
			response.headers().add(HTTP_HEADER_ORIGIN_TIME, resMessage.getOriginatingTimestamp());
		}
		if(resMessage.getResultExpirationTimestamp() != null) {
			response.headers().add(HTTP_HEADER_RESULT_EXPIRETIME, resMessage.getResultExpirationTimestamp());
		}
		if(resMessage.getEventCategory() != null) { //OneM2mResponse.NOT_SET) {
			response.headers().add(HTTP_HEADER_EVENT_CATEGORI, resMessage.getEventCategory());
		}
		if(resMessage.getContentLocation() != null) {
			response.headers().add(HTTP_HEADER_CONTENT_LOCATION, resMessage.getContentLocation());
		}
	
		if (RESPONSE_STATUS.isSuccess(resCode)) {
			if (resMessage.getContentType() != null && content != null) {
				response.headers().add(HTTP_HEADER_CONTENT_TYPE, resMessage.getContentType().Name());
			}
		}
		
		return response;
	}

	public static OneM2mResponse decode(DefaultFullHttpResponse response) throws Exception {
		return decode(response, null);
	}
	
	public static OneM2mResponse decode(FullHttpResponse response, String remoteHost) throws Exception {
		OneM2mResponse resMessage = new OneM2mResponse();
		
		if(remoteHost != null) {
			resMessage.setRemoteHost(remoteHost);
		}
		
		//List<Entry<String, String>> headers = response.headers().entries();
		resMessage.setHttpStatusCode(response.getStatus().code());		
		
		String value = response.headers().get(HTTP_HEADER_RESPONSE_STATUS);
		if(null != value) {
			int m2m_rsc = Integer.parseInt(value);
			resMessage.setResponseStatusCodeEnum(OneM2mResponse.RESPONSE_STATUS.get(m2m_rsc));
		}
		
		value = response.headers().get(HTTP_HEADER_REQUESTID);
		if(null != value)
			resMessage.setRequestIdentifier(value);
		
		value = response.headers().get(HTTP_HEADER_FROM);
		if(null != value)
			resMessage.setFrom(value);
		
		value = response.headers().get(HTTP_HEADER_ORIGIN_TIME);
		if(null != value)
			resMessage.setOriginatingTimestamp(value);
		
		value = response.headers().get(HTTP_HEADER_RESULT_EXPIRETIME);
		if(null != value)
			resMessage.setResultExpirationTimestamp(value);

		value = response.headers().get(HTTP_HEADER_EVENT_CATEGORI);
		if(null != value)
			resMessage.setEventCategory(Integer.parseInt(value));
		
		value = response.headers().get(HTTP_HEADER_CONTENT_LOCATION);
		if(null != value)
			resMessage.setContentLocation(value);
		
		value = response.headers().get(HTTP_HEADER_CONTENT_TYPE);
		if(null != value) {
			if (null != value) {
				String[] split = value.split(";");
				for(String type : split) {
					type = type.trim();
					try {
						resMessage.setContentType(type);
						break;
					} catch (Exception e) {
						continue;
					}
				}
			}
			
			if(resMessage.getContentType() == null) {
				throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, "Invalid Content-Type header:"+value);
			}
//			resMessage.setContentType(value);
		}
		
		if(response.content().isReadable()) {
			resMessage.setContent(response.content().copy().array());
		}
		
		return resMessage;
	}
	
}
