package org.eclipse.leshan.server.extension.onem2m.handler;

import java.net.HttpURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.extension.Constants;
import org.eclipse.leshan.server.extension.HttpOperator;
import org.eclipse.leshan.server.extension.Lwm2mVO;
import org.eclipse.leshan.server.extension.Util;
import org.eclipse.leshan.server.extension.onem2m.Onem2mHeaderMaker;
import org.eclipse.leshan.server.extension.onem2m.resources.AE;
import org.eclipse.leshan.server.extension.onem2m.resources.Container;
import org.eclipse.leshan.server.extension.onem2m.resources.ContentInstance;
import org.eclipse.leshan.server.extension.onem2m.resources.PollingChannel;
import org.eclipse.leshan.server.extension.onem2m.resources.SemanticDescriptor;
import org.eclipse.leshan.server.extension.onem2m.resources.Subscription;
import org.eclipse.leshan.util.Base64;

public class Onem2mOperator {
	
	private HttpOperator httpOperator = new HttpOperator();
	private Onem2mHeaderMaker headerMaker = new Onem2mHeaderMaker();
	
	
	// AE 존재 체크
	public boolean isAEExist(){
		
		boolean result = false;
		
		String strUri = Lwm2mServerConfig.getInstance().getSiUri()+"/"+Constants.BASIC_AE_NAME;
		HttpURLConnection conn = httpOperator.sendGet(strUri, headerMaker.getBasicHeader());
		JSONObject response = httpOperator.getResponse(conn);
		
		if( !response.has("RESPONSE-CODE") ){
			result = true;
		}
		
		return result;
	}
	
	// AE 생성
	public boolean createAE(){
		
		boolean result = false;
		String strUri = Lwm2mServerConfig.getInstance().getSiUri();
		
		AE ae = new AE();
		
		HttpURLConnection conn = httpOperator.sendPost(strUri, ae.getJSON(), headerMaker.getResourceCreationHeader(Constants.AE));
		JSONObject response = httpOperator.getResponse(conn);
		if( !response.has("RESPONSE-CODE") ){
			result = true;
		}
		
		return result;
	}
	
	// Container 생성
	public boolean createContainer(StringBuffer resUri, String resourceName){
		
		boolean result = false;
		StringBuffer sbUri = new StringBuffer(Lwm2mServerConfig.getInstance().getSiUri()).append("/").append(Constants.BASIC_AE_NAME);
		sbUri.append(resUri);
		
		Container cnt = new Container();
		cnt.setRn(resourceName);
		
		HttpURLConnection conn = httpOperator.sendPost(sbUri.toString(), cnt.getJSON(), headerMaker.getResourceCreationHeader(Constants.CONTAINER));
		JSONObject response = httpOperator.getResponse(conn);
		if( !response.has("RESPONSE-CODE") ){
			result = true;
		}
		
		return result;
	}
	
	// contentInstance 생성
	public boolean createContentInstance(String resourceName, JSONObject item) throws JSONException{
		
		boolean result = false;
		StringBuffer sbUri = new StringBuffer(Lwm2mServerConfig.getInstance().getSiUri()).append("/").append(Constants.BASIC_AE_NAME);
		
		ContentInstance cin = new ContentInstance();
		if(resourceName == null){
			sbUri.append("/").append(Constants.getResourceByUri(item.getString("resourceUri"))).append("/result");
			cin.setCon(item.getString("sv"));
			cin.setCnf("text/plain:0");
		} else {
			sbUri.append("/").append(resourceName).append("/status");
			cin.setCon(new String(Base64.encodeBase64(item.toString().getBytes())));
		}
		
		HttpURLConnection conn = httpOperator.sendPost(sbUri.toString(), cin.getJSON(), headerMaker.getResourceCreationHeader(Constants.CONTENTINSTANCE));
		JSONObject response = httpOperator.getResponse(conn);
		if( !response.has("RESPONSE-CODE") ){
			result = true;
		}
		
		return result;
	}
	
	// PollingChannel 생성
	public boolean createPollingChannel(String resourceName){
		
		boolean result = false;
		StringBuffer sbUri = new StringBuffer(Lwm2mServerConfig.getInstance().getSiUri()).append("/").append(Constants.BASIC_AE_NAME);
		
		PollingChannel pch = new PollingChannel();
		pch.setRn(resourceName);
		
		HttpURLConnection conn = httpOperator.sendPost(sbUri.toString(), pch.getJSON(), headerMaker.getResourceCreationHeader(Constants.POLLING_CHANNEL));
		JSONObject response = httpOperator.getResponse(conn);
		if( !response.has("RESPONSE-CODE") ){
			result = true;
		}
		
		return result;
	}
	
	// Subscription 생성
	public boolean createSubscription(StringBuffer resUri, String resourceName, String pchName){
		
		boolean result = false;
		StringBuffer sbUri = new StringBuffer(Lwm2mServerConfig.getInstance().getSiUri()).append("/").append(Constants.BASIC_AE_NAME);
		sbUri.append(resUri);
		
		Subscription sub = new Subscription();
		sub.resetNu();
		sub.setRn(resourceName);
		sub.addNu(Util.makeUrl(Lwm2mServerConfig.getInstance().getSiUri(), Constants.BASIC_AE_NAME, pchName, "pcu"));
		
		HttpURLConnection conn = httpOperator.sendPost(sbUri.toString(), sub.getJSON(), headerMaker.getResourceCreationHeader(Constants.SUBSCRIPTION));
		JSONObject response = httpOperator.getResponse(conn);
		if( !response.has("RESPONSE-CODE") ){
			result = true;
		}
		
		return result;
	}
	
	// SemanticDescriptor 생성
	public boolean createSemanticDescriptor(){
		
		boolean result = false;
		StringBuffer sbUri = new StringBuffer(Lwm2mServerConfig.getInstance().getSiUri()).append("/").append(Constants.BASIC_AE_NAME);
		
		SemanticDescriptor sd = new SemanticDescriptor();
		
		HttpURLConnection conn = httpOperator.sendPost(sbUri.toString(), sd.getJSON(), headerMaker.getResourceCreationHeader(Constants.SEMANTIC_DESCRIPTOR));
		JSONObject response = httpOperator.getResponse(conn);
		if( !response.has("RESPONSE-CODE") ){
			result = true;
		}
		
		return result;
	}
	
	// LongPolling 대기
	public String retrievePCU(){
		
		StringBuffer sbUri = new StringBuffer(Lwm2mServerConfig.getInstance().getSiUri());
		sbUri.append("/").append(Constants.BASIC_AE_NAME);
		sbUri.append("/pch_srv/pcu");
		
		System.out.println("strUri(LP) : "+sbUri);
		HttpURLConnection conn = httpOperator.sendLongPolling(sbUri.toString(), headerMaker.getBasicHeader());
		String response = httpOperator.getResponseString(conn);
		
		return response;
	}
	
	
	// repository 생성
	public void createRepository(Lwm2mVO vo){
		
		// AE 생성
		createAE();
		createSemanticDescriptor();
		createPollingChannel("pch_srv");
		createPollingChannel("pch_svc");
		
		for(int i=0; i<Constants.RESOURCE.length; i++){
			
			StringBuffer resUri = new StringBuffer();
			createContainer(resUri, Constants.RESOURCE[i][1]);
			
			resUri.append("/").append(Constants.RESOURCE[i][1]);
			StringBuffer sbUri = new StringBuffer();
			
			if(Constants.RESOURCE[i][0].equals("control")){
				
				createContainer(resUri, Constants.CONTAINER_NAME_CONTROL);
				createContainer(resUri, Constants.CONTAINER_NAME_RESULT);

				sbUri = new StringBuffer(resUri);
				sbUri.append("/").append(Constants.CONTAINER_NAME_CONTROL);
				createSubscription(sbUri, "sub_"+Constants.RESOURCE[i][1]+"_"+Constants.CONTAINER_NAME_CONTROL, "pch_srv");
				
				sbUri = new StringBuffer(resUri);
				sbUri.append("/").append(Constants.CONTAINER_NAME_RESULT);
				createSubscription(sbUri, "sub_"+Constants.RESOURCE[i][1]+"_"+Constants.CONTAINER_NAME_RESULT, "pch_svc");
				
			} else if(Constants.RESOURCE[i][0].equals("report")) {
				
				createContainer(resUri, Constants.CONTAINER_NAME_STATUS);				
				sbUri = new StringBuffer(resUri);
				sbUri.append("/").append(Constants.CONTAINER_NAME_STATUS);
				createSubscription(sbUri, "sub_"+Constants.RESOURCE[i][1]+"_"+Constants.CONTAINER_NAME_STATUS, "pch_svc");
			}
		}
	}
	
	
	
	/*
	
	
	
	
	
	private HttpOneM2MOperation op;
	private Lwm2mServerConfig config = Lwm2mServerConfig.getInstance();
	
	// constructor
	public Onem2mOperator(){
		op = new HttpOneM2MOperation();
	}
	
	// AE 존재 확인
	public synchronized boolean AeExist(){
		boolean result = false;
		try {
    		op.init(Constants.BASIC_AE_NAME);
    		JSONObject response = op.operate(new AE().makes(), Constants.AE, Constants.REQUEST_METHOD_TYPE.GET, null);
			
			if( !response.has("RESPONSE-CODE") ){
				result = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
		return result;
	}
	
	
	//*
	// repository 존재 확인
	public synchronized boolean hasSameObjects(){
		
		boolean result = false;
		try {
			System.out.println("Check current repository status.");
    		op.init(Constants.BASIC_AE_NAME,"?rcn=5");
    		JSONObject response = op.operate(new AE().makes(), Constants.AE, Constants.REQUEST_METHOD_TYPE.GET, null);
    		
    		if( !response.getJSONObject("ae").isNull("ch") ){
	    		JSONArray chs = response.getJSONObject("ae").getJSONArray("ch");
	    		int containerCount = 0; 
	    		for(int i=0; i<chs.length(); i++){
	    			if( chs.getJSONObject(i).getInt("typ") == 3 ){
	    				containerCount += 1;
	    			}
	    		}
	    		
	    		if( containerCount+3 != Constants.RESOURCE.length ){
	    			result = true;
	    		}
    		}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
		return result;
	}//*/
	/*
	
	// repository 생성
	public synchronized void createRepository(String authId){
		try {
			System.out.println("Create new repository.");
			
			// AE 생성
    		op.init("");
    		JSONObject resp_ae = op.operate(new AE().makes(), Constants.AE, Constants.REQUEST_METHOD_TYPE.POST, null);
    		
    		op.init(Constants.BASIC_AE_NAME);
    		JSONObject semantic = op.operate(new SemanticDescriptor().makes(), Constants.SEMANTIC_DESCRIPTOR, Constants.REQUEST_METHOD_TYPE.POST, null);
    		
    		// pollingChannel 생성
    		op.init(Constants.BASIC_AE_NAME);
    		PollingChannel pch_svc = new PollingChannel();
    		pch_svc.setRn("pch_svc");
    		JSONObject resp_pch_svc = op.operate(pch_svc.makes(), Constants.POLLING_CHANNEL, Constants.REQUEST_METHOD_TYPE.POST, null);
    		
    		op.init(Constants.BASIC_AE_NAME);
    		PollingChannel pch_srv = new PollingChannel();
    		pch_srv.setRn("pch_srv");
    		JSONObject resp_pch_srv = op.operate(pch_srv.makes(), Constants.POLLING_CHANNEL, Constants.REQUEST_METHOD_TYPE.POST, null);
    		
    		// container 및 subscription 생성
    		for(int i=0; i<Constants.RESOURCE.length; i++){
    			
	    		op.init(Constants.BASIC_AE_NAME);
	    		Container cnt = new Container();
	    		cnt.setRn(Constants.RESOURCE[i][1]);
	    		//cnt.addLbl(Tokenization.getDeviceInfoEncoded(authId));
	    		JSONObject resp_cnt = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		
	    		// resource_write 
	    		if(Constants.RESOURCE[i][0].equals("control")){
		    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1]);
		    		cnt.setRn(Constants.CONTAINER_NAME_CONTROL);
		    		JSONObject resp_cnt_w = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		}
	    		// resource_read
	    		if(Constants.RESOURCE[i][0].equals("control")){
		    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1]);
		    		cnt.setRn(Constants.CONTAINER_NAME_RESULT);
		    		JSONObject resp_cnt_r = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		} else if(Constants.RESOURCE[i][0].equals("report")) {
	    			op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1]);
		    		cnt.setRn(Constants.CONTAINER_NAME_STATUS);
		    		JSONObject resp_cnt_r = op.operate(cnt.makes(), Constants.CONTAINER, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		}
	    		
	    		// subscription 생성
	    		Subscription sub = new Subscription();
	    		if(Constants.RESOURCE[i][0].equals("control")){
		    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1], Constants.CONTAINER_NAME_CONTROL);
		    		sub.setRn("sub_"+Constants.RESOURCE[i][1]+"_execute");
		    		sub.addNu(Util.makeUrl(config.getSiUri(), Constants.BASIC_AE_NAME, "pch_srv", "pcu"));
		    		JSONObject resp_sub_w = op.operate(sub.makes(), Constants.SUBSCRIPTION, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		}
	    		if(Constants.RESOURCE[i][0].equals("control")){
		    		op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1], Constants.CONTAINER_NAME_RESULT);
		    		sub.resetNu();
		    		sub.setRn("sub_"+Constants.RESOURCE[i][1]+"_"+Constants.CONTAINER_NAME_RESULT);
		    		sub.addNu(Util.makeUrl(config.getSiUri(), Constants.BASIC_AE_NAME, "pch_svc", "pcu"));
		    		JSONObject resp_sub_r = op.operate(sub.makes(), Constants.SUBSCRIPTION, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		} else if(Constants.RESOURCE[i][0].equals("report")) {
	    			op.init(Constants.BASIC_AE_NAME, Constants.RESOURCE[i][1], Constants.CONTAINER_NAME_STATUS);
		    		sub.resetNu();
		    		sub.setRn("sub_"+Constants.RESOURCE[i][1]+"_"+Constants.CONTAINER_NAME_STATUS);
		    		sub.addNu(Util.makeUrl(config.getSiUri(), Constants.BASIC_AE_NAME, "pch_svc", "pcu"));
		    		JSONObject resp_sub_r = op.operate(sub.makes(), Constants.SUBSCRIPTION, Constants.REQUEST_METHOD_TYPE.POST, null);
	    		}
    		}
    		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
	}
	
	// repository 생성
	public synchronized void createContentInstanceResult(JSONObject item){
		try {
			String resourceName = Constants.getResourceByUri(item.getString("resourceUri"));
			String repo = Constants.CONTAINER_NAME_RESULT;
			
			op.init(Constants.BASIC_AE_NAME, resourceName, repo);
			ContentInstance cin = new ContentInstance();
			cin.setCon(item.getString("sv"));
			cin.setCnf("text/plain:0");
    		JSONObject resp_cin = op.operate(cin.makes(), Constants.CONTENTINSTANCE, Constants.REQUEST_METHOD_TYPE.POST, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
	}
	
	// repository 생성
	public synchronized void createContentInstanceStatus(String resourceName, JSONObject item){
		try {
			String repo = Constants.CONTAINER_NAME_STATUS;
			
			op.init(Constants.BASIC_AE_NAME, resourceName, repo);
			ContentInstance cin = new ContentInstance();
			cin.setCon(new String(Base64.decode(item.toString().getBytes())));
    		JSONObject resp_cin = op.operate(cin.makes(), Constants.CONTENTINSTANCE, Constants.REQUEST_METHOD_TYPE.POST, null);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			op.closeConnecton();
		}
	}
	*/
}
