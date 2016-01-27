package net.herit.iot.onem2m.ae.lib;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.message.onem2m.OneM2mResponse.RESPONSE_STATUS;
import net.herit.iot.onem2m.ae.lib.dto.AuthReqDTO;
import net.herit.iot.onem2m.ae.lib.dto.LGUAuthData;
import net.herit.iot.onem2m.ae.lib.dto.LGUAuthData.Http;
import net.herit.iot.onem2m.ae.lib.dto.ServerAuthReqDTO;
import net.herit.iot.onem2m.bind.http.client.HttpClientBasic;
import net.herit.iot.onem2m.core.util.LogManager;
import net.herit.iot.onem2m.core.util.OneM2MException;

/**
 * LGU+ OneM2M MES서버 연동 기능을 구현한 클래스
 *  
 * @version : 1.0
 * @author  : Lee inseuk
 */
public class LGUAuthController implements AuthControllerInterface {
	
	private LogManager 		logManager = LogManager.getInstacne();	
	private Logger log = LoggerFactory.getLogger(LGUAuthController.class);

	private String deviceModel;
	private String serviceCode;
	private String m2mmType;
	private String deviceSerialNo; 
	private String mac; 
	private String ctn;
	private String deviceType;
	private String iccId;

	private String mefAddr;
	private int keySize = 128;
	
	private String mSvcServerCdSeq;
	private String mSvcCdSeq;
	private String mSvcCdNum;
	
	private LGUAuthData authData;

	private final String AUTH_HEADER_EKI = "X-MEF-EKI";
	private final String AUTH_HEADER_TK = "X-MEF-TK";
	private final String AUTH_HEADER_DM = "X-LGU-DM";
	private final String AUTH_HEADER_NI = "X-LGU-NI";
	
	public LGUAuthController(String mefAddr, String svcServerCdSeq, String svcCdSeq, String svcCdNum) {
		this.mefAddr = mefAddr;
		mSvcServerCdSeq = svcServerCdSeq;
		mSvcCdSeq = svcCdSeq;
		mSvcCdNum = svcCdNum;
	}

	/**
	  * LGU+ 인증서버 연동을 위한 컨트롤러 생성자. 입력값은 미리 제공받아야 한다.
	  * 
	  * @param mefAddr - 인증서버 주소 (예: http://106.103.234.198/mef) 
	  * @param deviceModel - 디바이스 모델 명
	  * @param serviceCode - 서비스 코드  
	  * @param m2mmType - AE의 타입 (예: LTE-DEVICE)
	  * @param deviceSn - 디바이스 시리얼 번호
	  * @param ctn - 디바이스 CTN, 디바이스가 아니거나 CTN이 없는 디바이스일 경우 null 입력
	  * @param deviceType - 디바이스 타입 (AE의 경우 "AE"를 입력해야 함)
	  */
	public LGUAuthController(String mefAddr, String deviceModel, String serviceCode, String m2mmType, 
							String deviceSn, String mac, String ctn, String deviceType, String iccId) {
		try {
			
			this.mefAddr = mefAddr;
			this.deviceModel = deviceModel;
			this.serviceCode = serviceCode;
			this.m2mmType = m2mmType;
			this.deviceSerialNo = deviceSn;
			this.mac = mac;
			this.ctn = ctn;
			this.deviceType = deviceType;
			this.iccId = iccId;
			
			this.authData = null;
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	/**
	 * MEF 인증을 사용하지 않는 경우의 생성자
	 */
	public LGUAuthController() {
		this.authData = new LGUAuthData();
		authData.setHttp(new Http());
	}

	/**
	  * LGU+ 미리 공유된 인증정보를 셋팅한다.
	  * 미리 공유된 인증정보가 셋팅되면 인증서버를 통한 인증을 수행하지 않고 주어진 인증정보를 활용하여 oneM2M 서버와 통신을 수행한다.
	  * 
	  * @param authData - 인증 정보, 상세정보는 LGUAuthData 클래스 참조
	  */
	public void setPreSharedAuthData(LGUAuthData authData) throws OneM2MException {

		this.authData = authData;
		
	}
	
	@Override
	/**
	  * 라이브러리 내부에서 사용함
	  * 
	  */
	public void setAuthData(OneM2mRequest request) throws OneM2MException {

		request.addUserDefinedHeaders(AUTH_HEADER_EKI, this.authData.getKeId());
		request.addUserDefinedHeaders(AUTH_HEADER_TK, this.authData.getHttp().getToken());
		//request.addUserDefinedHeaders(AUTH_HEADER_DM, this.authData.getDeviceModel());
		//request.addUserDefinedHeaders(AUTH_HEADER_NI, this.authData.getNetworkInfo());
		
	} 
	
	public LGUAuthData getAuthData() {
		return this.authData;
	}
	
	@Override
	/**
	  * 라이브러리 내부에서 사용함
	  * 
	  */
	public boolean isAuthorized() {
		return this.authData != null;
	}
	
	@Override
	/**
	  * 라이브러리 내부에서 사용함
	  * 
	  */
	public String getEntityId() {
		try {
			return this.authData.getHttp().getEntityId().length() > 0 ? this.authData.getHttp().getEntityId() : null;
		} catch (NullPointerException e) {
			return null;
		}
	}
	
	@Override
	/**
	  * 디바이스 인증 or 서비스 서버 인증
	  * 
	  */
	public void doAuth() throws OneM2MException {
		try {			
			LGUAuthData authData = requestAccessPermission();
			
			//log.debug(authData.toString());
			
			SecretKey skey = decodeBase64ToAESKey(authData.getHttp().getEnrmtKey());
			String keId = encryptKey(skey, getShortUuid(authData.getHttp().getEntityId()));
			log.debug("keId: " + keId);
			
			if (keId == null) throw new OneM2MException(RESPONSE_STATUS.UNDEFINED, "Fail to create KeId");
			
			authData.setKeId(keId);
			this.authData = authData;

		} catch (InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException | NoSuchAlgorithmException | IllegalArgumentException e) {
			e.printStackTrace();
			throw new OneM2MException(RESPONSE_STATUS.UNDEFINED, "Fail to authrization");
		}
	}
	
	private String getShortUuid(String entity_id) {
		String [] data = entity_id.split("-");
		return data[2];
	}
	
	public SecretKey decodeBase64ToAESKey(String encodedKey ) throws IllegalArgumentException, NoSuchAlgorithmException {
		try {
			byte[] keyData = Base64.decodeBase64(encodedKey);
			int keysize = keyData.length * Byte.SIZE;

			switch ( keysize ) {
				case 128:
				case 192:
				case 256:
				   break;
				default:
				   throw new IllegalArgumentException();
			}

			if ( Cipher.getMaxAllowedKeyLength( "AES" ) < keysize ) {
				throw new IllegalArgumentException();
			}

			SecretKeySpec aesKey = new SecretKeySpec( keyData, "AES" );
			return aesKey;
		} catch ( NoSuchAlgorithmException e ) {
			throw new NoSuchAlgorithmException();
		}
	}
	
	private String encryptKey(SecretKey secureKey, String shortUuid) throws InvalidKeyException,
																		IllegalBlockSizeException, 
																		BadPaddingException {
		String encryptStr = null;
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secureKey);
			byte[] encryptedData = cipher.doFinal(shortUuid.getBytes());
			encryptStr = Base64.encodeBase64URLSafeString(encryptedData);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			
		}
		return encryptStr;
	}


	// KeID : xuSjIzEoisV2EWvArbgN36lgjuau-GV5fDL_J7vffLhu1XqxBYPxSrOHN5q1Q_Cd
	// token : 7b39ed3bc721401ba0e69e4bada2c7e4
	// CSE ID : MN_CSE-D-48b5886a084c3942baa86d587d9ebf9f-0078
	private LGUAuthData requestAccessPermission() throws OneM2MException {
		String body;
		
		if(mSvcServerCdSeq == null) {
			AuthReqDTO authDTO = new AuthReqDTO();
			authDTO.setDeviceModel(this.deviceModel);
			authDTO.setDeviceSerialNo(this.deviceSerialNo);
			authDTO.setServiceCode(this.serviceCode);
			authDTO.setDeviceType(this.deviceType);
			authDTO.setCtn(this.ctn);
			authDTO.setMac(this.mac);
			authDTO.setIccId(this.iccId);
			
			XMLConv<AuthReqDTO> reqConvertor = new XMLConv<AuthReqDTO>(AuthReqDTO.class);
			body = reqConvertor.marshal(authDTO);
		} else {
			ServerAuthReqDTO authDTO = new ServerAuthReqDTO();
			authDTO.setSvcServerCdSeq(mSvcServerCdSeq);
			authDTO.setSvcCdSeq(mSvcCdSeq);
			authDTO.setSvcCdNum(mSvcCdNum);
			
			XMLConv<ServerAuthReqDTO>  reqConvertor = new XMLConv<ServerAuthReqDTO>(ServerAuthReqDTO.class);
			body = reqConvertor.marshal(authDTO);
			log.debug(body);
		}
		
		HttpClientBasic httpClient = new HttpClientBasic();

		DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, 
																		HttpMethod.POST, 
																		mefAddr, 
																		Unpooled.copiedBuffer(body.getBytes()));
		httpRequest.headers().add("Content-Type", "application/xml");
		httpRequest.headers().add("Content-Length", body.length());
		httpRequest.headers().add("Host", "106.103.234.198");
		FullHttpResponse httpResponse = httpClient.process(mefAddr, httpRequest);
				 
		try {
			int code = httpResponse.getStatus().code();
			log.debug("status code: " + code);
			
			if ( code >=200 && code <300) {
				if(httpResponse.content().isReadable()) {
					String xml = new String(httpResponse.content().copy().array(), "UTF-8");
					
	
					XMLConv<LGUAuthData> XC2 = new XMLConv<LGUAuthData>(LGUAuthData.class);
					LGUAuthData auth = (LGUAuthData)XC2.unmarshal(xml);
					return auth;	
				}
			} else {
				switch (code) {
				case 400:
					throw new OneM2MException(RESPONSE_STATUS.BAD_REQUEST, RESPONSE_STATUS.BAD_REQUEST.Name());
				case 401:
					throw new OneM2MException(RESPONSE_STATUS.UNAUTHORIZED, RESPONSE_STATUS.UNAUTHORIZED.Name());
				case 403:
					throw new OneM2MException(RESPONSE_STATUS.ACCESS_DENIED, RESPONSE_STATUS.ACCESS_DENIED.Name());
				case 404:
					throw new OneM2MException(RESPONSE_STATUS.NOT_FOUND, RESPONSE_STATUS.NOT_FOUND.Name());
				case 409:
					throw new OneM2MException(RESPONSE_STATUS.GROUP_REQ_ID_EXISTS, RESPONSE_STATUS.GROUP_REQ_ID_EXISTS.Name());
				case 500:
					throw new OneM2MException(RESPONSE_STATUS.INTERNAL_SERVER_ERROR, RESPONSE_STATUS.INTERNAL_SERVER_ERROR.Name());
				case 501:
					throw new OneM2MException(RESPONSE_STATUS.NOT_IMPLEMENTED, RESPONSE_STATUS.NOT_IMPLEMENTED.Name());
				}
			}
		} catch (UnsupportedEncodingException e) {				
			e.printStackTrace();
			throw new OneM2MException(RESPONSE_STATUS.UNDEFINED, "Fail to encode response, "+e.getMessage());
		} catch (OneM2MException e) {
			log.debug("OneM2MException code: ");
			throw new OneM2MException(e.getResponseStatusCode(), e.getResponseStatusCode().Name());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new OneM2MException(RESPONSE_STATUS.UNDEFINED, "Exception in xml parsing, "+e.getMessage());
		}
	
		return null;
		
	}

}
