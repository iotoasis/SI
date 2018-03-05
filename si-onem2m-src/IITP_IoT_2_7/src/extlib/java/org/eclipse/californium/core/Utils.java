/*******************************************************************************
 * Copyright (c) 2015 Institute for Pervasive Computing, ETH Zurich and others.
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
 *    Matthias Kovatsch - creator and main architect
 *    Martin Lanter - architect and re-implementation
 *    Dominique Im Obersteg - parsers and initial implementation
 *    Daniel Pauli - parsers and initial implementation
 *    Kai Hudalla - logging
 *    Achim Kraus (Bosch Software Innovations GmbH) - add toHexText 
 *                                                    (for message tracing)
 ******************************************************************************/
package org.eclipse.californium.core;

import java.util.concurrent.ThreadFactory;

import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.coap.Response;

/**
 * The class Utils contains auxiliary methods for Californium.
 *
 */
public class Utils {

	/*
	 * Prevent initialization
	 */
	private Utils() { }
	
	/**
	 * Converts the specified byte array to a hexadecimal string.
	 *
	 * @param bytes the byte array
	 * @return the hexadecimal code string
	 */
	public static String toHexString(byte[] bytes) {
		if (bytes == null) return "null";
		   StringBuilder sb = new StringBuilder();
		   for(byte b:bytes)
		      sb.append(String.format("%02x", b & 0xFF));
		   return sb.toString();
	}

	/**
	 * Converts the specified byte array up to the specified length into a hexadecimal text.
	 * Separate bytes by spaces and group them in lines. Append length of array, if specified 
	 * length is smaller then the length of the array.
	 * 
	 * @param bytes the array of bytes. If null, the text "null" is returned.
	 * @param length length up to the bytes should be converted into hexadecimal text. 
	 *               If larger then the array length, reduce it to the array length.
	 * @return byte array as hexadecimal text
	 */
	public static String toHexText(byte[] bytes, int length) {
		if (bytes == null) return "null";
		if (length > bytes.length) length = bytes.length;
		StringBuilder sb = new StringBuilder();
		if (16 < length) sb.append('\n');
		for(int index = 0; index < length; ++index) {
			sb.append(String.format("%02x", bytes[index] & 0xFF));
			if (31 == (31 & index)) {
				sb.append('\n');
			} else {
				sb.append(' ');
			}
		}
		if (length < bytes.length) {
			sb.append(" .. ").append(bytes.length).append(" bytes");
		}
		return sb.toString();
	}

	/**
	 * Formats a {@link Request} into a readable String representation. 
	 * 
	 * @param r the Request
	 * @return the pretty print
	 */
	public static String prettyPrint(Request r) {
	
	        StringBuilder sb = new StringBuilder();
	        
	        sb.append("##[ CoAP Request ]##############################################\n");
	        sb.append(String.format("MID    : %d\n", r.getMID()));
	        sb.append(String.format("Token  : %s\n", r.getTokenString()));
	        sb.append(String.format("Type   : %s\n", r.getType().toString()));
	        sb.append(String.format("Method : %s\n", r.getCode().toString()));
	        sb.append(String.format("Options: %s\n", r.getOptions().toString()));
	        sb.append(String.format("Payload: %d Bytes\n", r.getPayloadSize()));
	        if (r.getPayloadSize() > 0 && MediaTypeRegistry.isPrintable(r.getOptions().getContentFormat())) {
	        	sb.append("---------------------------------------------------------------");
	        	sb.append(r.getPayloadString());
	        }
	        sb.append("############################################################");
	
	        return sb.toString();
	}

	/**
	 * Formats a {@link CoapResponse} into a readable String representation. 
	 * 
	 * @param r the CoapResponse
	 * @return the pretty print
	 */
	public static String prettyPrint(CoapResponse r) {
		return prettyPrint(r.advanced());
	}

	/**
	 * Formats a {@link Response} into a readable String representation. 
	 * 
	 * @param r the Response
	 * @return the pretty print
	 */
	public static String prettyPrint(Response r) {
	
	        StringBuilder sb = new StringBuilder();
	        
	        sb.append("##[ CoAP Response ]##############################################\n");
	        sb.append(String.format("MID    : %d\n", r.getMID()));
	        sb.append(String.format("Token  : %s\n", r.getTokenString()));
	        sb.append(String.format("Type   : %s\n", r.getType().toString()));
	        sb.append(String.format("Status : %s\n", r.getCode().toString()));
	        sb.append(String.format("Options: %s\n", r.getOptions().toString()));
	        sb.append(String.format("Payload: %d Bytes\n", r.getPayloadSize()));
	        if (r.getPayloadSize() > 0 && MediaTypeRegistry.isPrintable(r.getOptions().getContentFormat())) {
	        	sb.append("---------------------------------------------------------------\n");
	        	sb.append(r.getPayloadString());
	        	sb.append("\n");
	        }
	        sb.append("#################################################################");
	
	        return sb.toString();
	}
	
	/**
	 * A factory to create executor services with daemon threads.
	 */
	public static class DaemonThreadFactory implements ThreadFactory {
	    public Thread newThread(Runnable r) {
	        Thread thread = new Thread(r);
	        thread.setDaemon(true);
	        return thread;
	    }
	}
}
