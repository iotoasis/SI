/*******************************************************************************
 * Copyright (c) 2013-2015 Sierra Wireless and others.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 *    http://www.eclipse.org/org/documents/edl-v10.html.
 * 
 * Contributors:
 *     Sierra Wireless - initial API and implementation
 *******************************************************************************/
package org.eclipse.leshan.server.registration;

import java.net.InetSocketAddress;
import java.util.Date;

import org.eclipse.leshan.core.request.DeregisterRequest;
import org.eclipse.leshan.core.request.Identity;
import org.eclipse.leshan.core.request.RegisterRequest;
import org.eclipse.leshan.core.request.UpdateRequest;
import org.eclipse.leshan.core.response.DeregisterResponse;
import org.eclipse.leshan.core.response.RegisterResponse;
import org.eclipse.leshan.core.response.UpdateResponse;
import org.eclipse.leshan.server.Lwm2mServerConfig;
import org.eclipse.leshan.server.api.Connector;
import org.eclipse.leshan.server.api.Tokenization;
import org.eclipse.leshan.server.api.Util;
import org.eclipse.leshan.server.client.Client;
import org.eclipse.leshan.server.client.ClientRegistry;
import org.eclipse.leshan.server.client.ClientUpdate;
import org.eclipse.leshan.server.onem2m.CSEConnector;
import org.eclipse.leshan.server.onem2m.handler.CommandReceiver;
import org.eclipse.leshan.server.security.SecurityCheck;
import org.eclipse.leshan.server.security.SecurityInfo;
import org.eclipse.leshan.server.security.SecurityStore;
import org.eclipse.leshan.util.RandomStringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * Handle the client registration logic. Check if the client is allowed to register, with the wanted security scheme.
 * Create the {@link Client} representing the registered client and add it to the {@link ClientRegistry}
 */
public class RegistrationHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RegistrationHandler.class);

    private SecurityStore securityStore;
    private ClientRegistry clientRegistry;
    private Thread thrRec = null;
    
    private String authId;
    private String authPwd;
    private JSONObject result;
    
    private Client client;
    private boolean isConnected = false;
    
    
    
    public RegistrationHandler(ClientRegistry clientRegistry, SecurityStore securityStore) {
        this.clientRegistry = clientRegistry;
        this.securityStore = securityStore;
    }

    public RegisterResponse register(Identity sender, RegisterRequest registerRequest, InetSocketAddress serverEndpoint){

		
        if (registerRequest.getEndpointName() == null || registerRequest.getEndpointName().isEmpty() || sender == null) {
            return RegisterResponse.badRequest(null);
        }

        // We must check if the client is using the right identity.
        if (!isAuthorized(registerRequest.getEndpointName(), sender)) {
            return RegisterResponse.forbidden(null);
        }

        Client.Builder builder = new Client.Builder(RegistrationHandler.createRegistrationId(),
                registerRequest.getEndpointName(), sender.getPeerAddress().getAddress(), sender.getPeerAddress()
                        .getPort(), serverEndpoint);

        builder.lwM2mVersion(registerRequest.getLwVersion()).lifeTimeInSec(registerRequest.getLifetime())
                .bindingMode(registerRequest.getBindingMode()).objectLinks(registerRequest.getObjectLinks())
                .smsNumber(registerRequest.getSmsNumber()).registrationDate(new Date()).lastUpdate(new Date())
                .additionalRegistrationAttributes(registerRequest.getAdditionalAttributes());

        client = builder.build();


        if (clientRegistry.registerClient(client)) {
            LOG.debug("New registered client: {}", client);
            RegisterResponse res = RegisterResponse.success(client.getRegistrationId()); 

            /*******************************************************************
             * 			CONNECT TO DEVICE MANAGEMENT SERVER & IN-CSE SERVER
             *******************************************************************/
            
            new Thread(new Runnable(){
    			@Override
    			public void run() {
		    		try{
		    			System.out.println("****************************   CONNECT TO DEVICE MANAGEMENT SERVER [START]   ****************************");
		    			// 토큰화
		    	        JSONObject token = Tokenization.makesTokenForConnect(authId, authPwd, client);
		    	        
		    	        // 커넥션
		    	        Connector conn = Connector.getInstance().connect();
		    	    	conn.sendRequest(token);
		    	    	JSONObject result = conn.getResponse();
		    	    	System.out.println(result);
		    			
		    	    	System.out.println("****************************   CONNECT TO DEVICE MANAGEMENT SERVER [E N D]   ****************************");
		    		} catch(Exception e) {
		    			e.printStackTrace();
		    		}
    			}
            }).start();
            
    		Lwm2mServerConfig config = Lwm2mServerConfig.getInstance();
        	if( config.isUsing() ){
        		
    			// repository 생성
    			new Thread(new Runnable(){
    				@Override
    				public void run() {
    					try{
    						System.out.println("****************************   CONNECT TO IN-CSE SERVER [START]   ****************************");
    				    	CSEConnector cseConn = CSEConnector.getInstance();
    				    	
    				    	if( cseConn.checkAEExist() ){
    				    		if( !cseConn.hasSameObjects() ){
    				    			System.out.println("Some issue has detected at current repository structure.");
    				    			//cseConn.removeRepository();
    				    			//cseConn.createRepository(Tokenization.makesBasicToken(authId, "connect_oneM2M"));
    				    		}
    				    	} else {
    				    		System.out.println("Repository doesn't exists.");
    				    		System.out.println("Create new repository.");
    				    		cseConn.createRepository(Tokenization.makesBasicToken(authId, "connect_oneM2M"));
    				    	}
    				    	
    				    	isConnected = true;
    				    	
    				    	System.out.println("****************************   CONNECT TO IN-CSE SERVER [E N D]   ****************************");
    				    	
    						while(isConnected){
    							try{
    								CommandReceiver cr = new CommandReceiver();
    								result = cr.receive();
    							} catch(Exception e) {
    								e.printStackTrace();
    								try {
    									System.out.println("Failed to connect to server. It will try again in 5 seconds.");
    									Thread.sleep(5000);
    								} catch (InterruptedException e1) {
    									e1.printStackTrace();
    								}
    							}
    						}
    				    	
    					} catch(Exception e) {
    						e.printStackTrace();
    					}
    				}
    	    	}).start();
    	    	//*/
    	    	   
        	}
            
        	return res;
        } else {
            return RegisterResponse.forbidden(null);
        }
    }

    public UpdateResponse update(Identity sender, UpdateRequest updateRequest) {
    	
        if (sender == null) {
            return UpdateResponse.badRequest(null);
        }

        // We must check if the client is using the right identity.
        client = clientRegistry.findByRegistrationId(updateRequest.getRegistrationId());
        if (client == null) {
            return UpdateResponse.notFound();
        }
        if (!isAuthorized(client.getEndpoint(), sender)) {
            return UpdateResponse.badRequest("forbidden");
        }

        client = clientRegistry.updateClient(new ClientUpdate(updateRequest.getRegistrationId(), sender
                .getPeerAddress().getAddress(), sender.getPeerAddress().getPort(), updateRequest.getLifeTimeInSec(),
                updateRequest.getSmsNumber(), updateRequest.getBindingMode(), updateRequest.getObjectLinks()));
        if (client == null) {
            return UpdateResponse.notFound();
        } else {
            return UpdateResponse.success();
        }
    }

    public DeregisterResponse deregister(Identity sender, DeregisterRequest deregisterRequest) {
    	
        if (sender == null) {
            return DeregisterResponse.badRequest(null);
        }
        
        // We must check if the client is using the right identity.
        client = clientRegistry.findByRegistrationId(deregisterRequest.getRegistrationID());
        authId = deregisterRequest.getRegistrationID();
        if (client == null) {
            return DeregisterResponse.notFound();
        }
        if (!isAuthorized(client.getEndpoint(), sender)) {
            return DeregisterResponse.badRequest("forbidden");
        }
        
        new Thread(new Runnable(){
			@Override
			public void run() {
				try{			    	   
			        System.out.println("****************************   DISCONNECT FROM DEVICE MANAGEMENT SERVER [START]   ****************************");
			        
			        // 토큰화
			        JSONObject token = Tokenization.makesTokenForDisconnect(authId, client);
			        
			        // 커넥션
			        Connector conn = Connector.getInstance().connect();
			    	conn.sendRequest(token);
			    	JSONObject result = conn.getResponse();
			       
			    	System.out.println("****************************   DISCONNECT FROM DEVICE MANAGEMENT SERVER [END]   ****************************");
			    	
			    	isConnected = false;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
    	}).start();

        Client unregistered = clientRegistry.deregisterClient(deregisterRequest.getRegistrationID());
        if (unregistered != null) {
            return DeregisterResponse.success();
        } else {
            LOG.debug("Invalid deregistration");
            return DeregisterResponse.notFound();
        }
        
        
    }

    
    
    private static String createRegistrationId() {
        return RandomStringUtils.random(10, true, true);
    }

    /**
     * Return true if the client with the given lightweight M2M endPoint is authorized to communicate with the given
     * security parameters.
     * 
     * @param lwM2mEndPointName the lightweight M2M endPoint name
     * @param clientIdentity the identity at TLS level
     * @return true if device get authorization
     */
    private boolean isAuthorized(String lwM2mEndPointName, Identity clientIdentity) {
        // do we have security information for this client?
        SecurityInfo expectedSecurityInfo = securityStore.getByEndpoint(lwM2mEndPointName);
        if( expectedSecurityInfo != null ){
	        authId = expectedSecurityInfo.getIdentity();
	        authPwd = Util.byteArrayToHex(expectedSecurityInfo.getPreSharedKey());
        }
        return SecurityCheck.checkSecurityInfo(lwM2mEndPointName, clientIdentity, expectedSecurityInfo);
    }

}
