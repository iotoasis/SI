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
package net.herit.iot.lwm2m.dms.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.leshan.core.node.LwM2mNode;
import org.eclipse.leshan.core.node.LwM2mObjectInstance;
import org.eclipse.leshan.core.node.LwM2mSingleResource;
import org.eclipse.leshan.core.request.ContentFormat;
import org.eclipse.leshan.core.request.CreateRequest;
import org.eclipse.leshan.core.request.DeleteRequest;
import org.eclipse.leshan.core.request.ExecuteRequest;
import org.eclipse.leshan.core.request.ObserveRequest;
import org.eclipse.leshan.core.request.ReadRequest;
import org.eclipse.leshan.core.request.WriteRequest;
import org.eclipse.leshan.core.request.WriteRequest.Mode;
import org.eclipse.leshan.core.request.exception.RequestFailedException;
import org.eclipse.leshan.core.request.exception.ResourceAccessException;
import org.eclipse.leshan.core.response.CreateResponse;
import org.eclipse.leshan.core.response.DeleteResponse;
import org.eclipse.leshan.core.response.ExecuteResponse;
import org.eclipse.leshan.core.response.LwM2mResponse;
import org.eclipse.leshan.core.response.ObserveResponse;
import org.eclipse.leshan.core.response.ReadResponse;
import org.eclipse.leshan.core.response.WriteResponse;
import org.eclipse.leshan.server.LwM2mServer;
import org.eclipse.leshan.server.api.Util;
import org.eclipse.leshan.server.client.Client;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import net.herit.iot.lwm2m.dms.servlet.json.ClientSerializer;
import net.herit.iot.lwm2m.dms.servlet.json.LwM2mNodeDeserializer;
import net.herit.iot.lwm2m.dms.servlet.json.LwM2mNodeSerializer;
import net.herit.iot.lwm2m.dms.servlet.json.ResponseSerializer;

/**
 * Service HTTP REST API calls.
 */
public class ApiServlet extends HttpServlet {

    private static final String FORMAT_PARAM = "format";

    private static final Logger LOG = LoggerFactory.getLogger(ApiServlet.class);

    private static final long TIMEOUT = 5000; // ms

    private static final long serialVersionUID = 1L;

    private final LwM2mServer server;

    private final Gson gson;
    
	private JSONArray result;
	
	
    public ApiServlet(LwM2mServer server, int securePort) {
        this.server = server;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeHierarchyAdapter(Client.class, new ClientSerializer(securePort));
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mResponse.class, new ResponseSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mNode.class, new LwM2mNodeSerializer());
        gsonBuilder.registerTypeHierarchyAdapter(LwM2mNode.class, new LwM2mNodeDeserializer());
        gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        this.gson = gsonBuilder.create();
    }
    

	private JSONObject getRequestBody(HttpServletRequest req) {
    	// body
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		byte[] byteData = null;
		JSONObject result = null;
		try{
	    	is = req.getInputStream();
	    	baos = new ByteArrayOutputStream();
	        byte[] byteBuffer = new byte[1024];
	        int nLength = 0;
	        while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
	            baos.write(byteBuffer, 0, nLength);
	        }
	        byteData = baos.toByteArray();
	        result = new JSONObject(new String(byteData).replace("-", "0"));
		} catch(Exception e){
			e.printStackTrace();
		} finally {
			try{
				if(baos != null){ baos.close(); }
				if(is != null){ is.close(); }
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
        return result;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // all registered clients
        if (req.getPathInfo() == null) {
            Collection<Client> clients = server.getClientRegistry().allClients();

            String json = this.gson.toJson(clients.toArray(new Client[] {}));
            resp.setContentType("application/json");
            resp.getOutputStream().write(json.getBytes("UTF-8"));
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        String[] path = StringUtils.split(req.getPathInfo(), '/');
        if (path.length < 1) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid path");
            return;
        }
        String clientEndpoint = path[0];

        // /endPoint : get client
        if (path.length == 1) {
            Client client = server.getClientRegistry().get(clientEndpoint);
            if (client != null) {
                resp.setContentType("application/json");
                resp.getOutputStream().write(this.gson.toJson(client).getBytes("UTF-8"));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().format("no registered client with id '%s'", clientEndpoint).flush();
            }
            return;
        }

        // /clients/endPoint/LWRequest : do LightWeight M2M read request on a given client.
        try {
            String target = StringUtils.removeStart(req.getPathInfo(), "/" + clientEndpoint);
            Client client = server.getClientRegistry().get(clientEndpoint);
            if (client != null) {
                // get content format
                String contentFormatParam = req.getParameter(FORMAT_PARAM);
                ContentFormat contentFormat = contentFormatParam != null
                        ? ContentFormat.fromName(contentFormatParam.toUpperCase()) : null;

                // create & process request
                ReadRequest request = new ReadRequest(contentFormat, target);
                ReadResponse cResponse = server.send(client, request, TIMEOUT);
                processDeviceResponse(req, resp, cResponse);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().format("No registered client with id '%s'", clientEndpoint).flush();
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Invalid request", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().append(e.getMessage()).flush();
        } catch (ResourceAccessException | RequestFailedException e) {
            LOG.warn(String.format("Error accessing resource %s%s.", req.getServletPath(), req.getPathInfo()), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().append(e.getMessage()).flush();
        } catch (InterruptedException e) {
            LOG.warn("Thread Interrupted", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().append(e.getMessage()).flush();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	result = new JSONArray();

		// end point
    	String endPoint = req.getPathInfo().substring(1);
    	
    	try {
    		JSONObject msg = getRequestBody(req);
    		JSONArray e = msg.getJSONArray("e");
    		
    		if(msg.getString("o").equals("e")) {
    			String target = e.getJSONObject(0).getString("n");
    	    	Client client = server.getClientRegistry().get(endPoint);
    			
    			if (client != null) {
                    // get content format
                    String contentFormatParam = req.getParameter(FORMAT_PARAM);
                    ContentFormat contentFormat = contentFormatParam != null
                            ? ContentFormat.fromName(contentFormatParam.toUpperCase()) : null;

                    // create & process request                            
                    LwM2mNode node = extractLwM2mNode(target, req, e.getJSONObject(0).getString("sv").equals("ON")?"1":"0");
                    WriteRequest request = new WriteRequest(Mode.REPLACE, contentFormat, target, node);
                    WriteResponse cResponse = server.send(client, request, TIMEOUT);
                    
                    String cRes = this.gson.toJson(cResponse);
    	            JSONObject withUri = new JSONObject(cRes);
    	            withUri.put("uri", target);
    	            result.put(withUri);
                    processDeviceResponse(req, resp, result);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().format("No registered client with id '%s'", endPoint).flush();
                }
    		}
    		
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	result = new JSONArray();
    	
    	// end point
    	String endPoint = req.getPathInfo().substring(1);
    	
    	try {
    		JSONObject msg = getRequestBody(req);
    		JSONArray e = msg.getJSONArray("e");
    		
    		if(msg.getString("o").equals("o")){
    			// observe
    			observe(req, resp, e, endPoint);
    			
    		} else if(msg.getString("o").equals("r")) {
	    		// read
    			read(req, resp, e, endPoint);
    			
    		} else if(msg.getString("o").equals("rb")) {
    			// reboot
    			execute(req, resp, msg, endPoint);
    			
    		}
    		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("none of data");
		}
    }
    
    private synchronized void read(HttpServletRequest req, HttpServletResponse resp, JSONArray resources, String endPoint) throws IOException{
	    
    	result = new JSONArray();
    	
    	for(int i=0; i<resources.length(); i++){
	    	try {
	            String target = resources.getJSONObject(i).getString("n");
	            Client client = server.getClientRegistry().get(endPoint);
	            if (client != null) {
	                // get content format
	                String contentFormatParam = req.getParameter(FORMAT_PARAM);
	                ContentFormat contentFormat = contentFormatParam != null
	                        ? ContentFormat.fromName(contentFormatParam.toUpperCase()) : null;
	
	                // create & process request
	                ReadRequest request = new ReadRequest(contentFormat, target);
	                String cRes = this.gson.toJson(server.send(client, request, TIMEOUT));
		            JSONObject withUri = new JSONObject(cRes);
		            withUri.put("uri", target);
		            if( cRes.indexOf("values") > -1 ){
		            	JSONObject content = withUri.getJSONObject("content");
		            	content.put("value", withUri.getJSONObject("content").getJSONObject("values").getString("0"));
		            	withUri.put("content", content);
		            }
		            result.put(withUri);
	                
	            } else {
	                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	                resp.getWriter().format("No registered client with id '%s'", endPoint).flush();
	            }
	        } catch (IllegalArgumentException e) {
	            LOG.warn("Invalid request", e);
	            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            resp.getWriter().append(e.getMessage()).flush();
	        } catch (ResourceAccessException | RequestFailedException e) {
	            LOG.warn(String.format("Error accessing resource %s%s.", req.getServletPath(), req.getPathInfo()), e);
	            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            resp.getWriter().append(e.getMessage()).flush();
	        } catch (InterruptedException e) {
	            LOG.warn("Thread Interrupted", e);
	            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            resp.getWriter().append(e.getMessage()).flush();
	        } catch (JSONException e) {
				e.printStackTrace();
			}
    	}
    	
    	processDeviceResponse(req, resp, result);
    	
    }
    
    private synchronized void observe(HttpServletRequest req, HttpServletResponse resp, JSONArray resources, String endPoint) throws JSONException, InterruptedException, IOException{
    	
    	for(int i=0; i<resources.length(); i++){
	    	try {
    	
		    	String target = resources.getJSONObject(i).getString("n");
		    	Client client = server.getClientRegistry().get(endPoint);
				
		        if (client != null) {
		            // get content format
		            String contentFormatParam = req.getParameter(FORMAT_PARAM);
		            ContentFormat contentFormat = contentFormatParam != null ? ContentFormat.fromName(contentFormatParam.toUpperCase()) : null;
		            
		            // create & process request
		            ObserveRequest request = new ObserveRequest(contentFormat, target);
		            ObserveResponse cResponse = server.send(client, request, TIMEOUT);
		            
		            String cRes = this.gson.toJson(cResponse);
		            JSONObject withUri = new JSONObject(cRes);
		            withUri.put("uri", target);
		            if( cRes.indexOf("values") > -1 ){
		            	JSONObject content = withUri.getJSONObject("content");
		            	content.put("value", withUri.getJSONObject("content").getJSONObject("values").getString("0"));
		            	withUri.put("content", content);
		            }
		            result.put(withUri);
		        } else {
		            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		            resp.getWriter().format("no registered client with id '%s'", endPoint).flush();
		        }
		        
	    	} catch (IllegalArgumentException e) {
	            LOG.warn("Invalid request", e);
	            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	            resp.getWriter().append(e.getMessage()).flush();
	        } catch (ResourceAccessException | RequestFailedException e) {
	            LOG.warn(String.format("Error accessing resource %s%s.", req.getServletPath(), req.getPathInfo()), e);
	            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            resp.getWriter().append(e.getMessage()).flush();
	        } catch (InterruptedException e) {
	            LOG.warn("Thread Interrupted", e);
	            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	            resp.getWriter().append(e.getMessage()).flush();
	        } catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		processDeviceResponse(req, resp, result);
    }
    
    private synchronized void execute(HttpServletRequest req, HttpServletResponse resp, JSONObject msg, String endPoint) throws JSONException, InterruptedException, IOException{
    	
		String target = msg.getString("n");
    	Client client = server.getClientRegistry().get(endPoint);
    	
        if (client != null) {

            ExecuteRequest request = new ExecuteRequest(target, IOUtils.toString(req.getInputStream()));
            ExecuteResponse cResponse = server.send(client, request, TIMEOUT);
            processDeviceResponse(req, resp, cResponse);
        } else {
        	resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().format("no registered client with id '%s'", endPoint).flush();
        }
        
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String[] path = StringUtils.split(req.getPathInfo(), '/');
        String clientEndpoint = path[0];

        // /clients/endPoint/LWRequest/observe : cancel observation for the given resource.
        if (path.length >= 4 && "observe".equals(path[path.length - 1])) {
            try {
                String target = StringUtils.substringsBetween(req.getPathInfo(), clientEndpoint, "/observe")[0];
                Client client = server.getClientRegistry().get(clientEndpoint);
                if (client != null) {
                    server.getObservationRegistry().cancelObservations(client, target);
                    resp.setStatus(HttpServletResponse.SC_OK);
                } else {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    resp.getWriter().format("no registered client with id '%s'", clientEndpoint).flush();
                }
            } catch (IllegalArgumentException e) {
                LOG.warn("Invalid request", e);
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().append(e.getMessage()).flush();
            } catch (ResourceAccessException | RequestFailedException e) {
                LOG.warn(String.format("Error accessing resource %s%s.", req.getServletPath(), req.getPathInfo()), e);
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().append(e.getMessage()).flush();
            }
            return;
        }

        // /clients/endPoint/LWRequest/ : delete instance
        try {
            String target = StringUtils.removeStart(req.getPathInfo(), "/" + clientEndpoint);
            Client client = server.getClientRegistry().get(clientEndpoint);
            if (client != null) {
                DeleteRequest request = new DeleteRequest(target);
                DeleteResponse cResponse = server.send(client, request, TIMEOUT);
                processDeviceResponse(req, resp, cResponse);
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().format("no registered client with id '%s'", clientEndpoint).flush();
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Invalid request", e);
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().append(e.getMessage()).flush();
        } catch (ResourceAccessException | RequestFailedException e) {
            LOG.warn(String.format("Error accessing resource %s%s.", req.getServletPath(), req.getPathInfo()), e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().append(e.getMessage()).flush();
        } catch (InterruptedException e) {
            LOG.warn("Interrupted request", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().append(e.getMessage()).flush();
        }
    }

    private void processDeviceResponse(HttpServletRequest req, HttpServletResponse resp, LwM2mResponse cResponse)
            throws IOException {
        String response = null;
        if (cResponse == null) {
            LOG.warn(String.format("Request %s%s timed out.", req.getServletPath(), req.getPathInfo()));
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().append("Request timeout").flush();
        } else {
            response = this.gson.toJson(cResponse);
            resp.setContentType("application/json");
            resp.getOutputStream().write(response.getBytes());
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private void processDeviceResponse(HttpServletRequest req, HttpServletResponse resp, JSONArray result)
            throws IOException {
    	
    	String response = result.toString();
    	System.out.println("resp : "+response);
        if ( Util.isNoE(response) && result.length() == 0 ) {
            LOG.warn(String.format("Request %s%s timed out.", req.getServletPath(), req.getPathInfo()));
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().append("Request timeout").flush();
        } else {
	        resp.setContentType("application/json");
	        resp.getOutputStream().write(response.getBytes());
	        resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
    
    private LwM2mNode extractLwM2mNode(String target, HttpServletRequest req) throws IOException {
        String contentType = StringUtils.substringBefore(req.getContentType(), ";");
        
        if ("application/json".equals(contentType)) {
            String content = IOUtils.toString(req.getInputStream(), req.getCharacterEncoding());
            LwM2mNode node;
            try {
                node = gson.fromJson(content, LwM2mNode.class);
            } catch (JsonSyntaxException e) {
                throw new IllegalArgumentException("unable to parse json to tlv:" + e.getMessage(), e);
            }
            return node;
        } else if ("text/plain".equals(contentType)) {
            String content = IOUtils.toString(req.getInputStream(), req.getCharacterEncoding());
            int rscId = Integer.valueOf(target.substring(target.lastIndexOf("/") + 1));
            return LwM2mSingleResource.newStringResource(rscId, content);
        }
        throw new IllegalArgumentException("content type " + req.getContentType() + " not supported");
    }
    
    private LwM2mNode extractLwM2mNode(String target, HttpServletRequest req, String content) throws IOException {
        int rscId = Integer.valueOf(target.substring(target.lastIndexOf("/") + 1));
        return LwM2mSingleResource.newStringResource(rscId, content);
    }
}
