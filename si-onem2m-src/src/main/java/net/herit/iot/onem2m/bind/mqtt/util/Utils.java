package net.herit.iot.onem2m.bind.mqtt.util;

import net.herit.iot.message.onem2m.format.Enums.CONTENT_TYPE;

public class Utils {
	
	public static CONTENT_TYPE getContentType(String strContents) {
		String contents = strContents.trim();
		CONTENT_TYPE contentType = CONTENT_TYPE.NONE;
		if(contents.startsWith("<")) {				// XML format
			contentType = CONTENT_TYPE.XML;
		} else if(contents.startsWith("{")) {		// JSON format
			contentType = CONTENT_TYPE.JSON;
		}
		
		return contentType;
	}
	
	/*********************************************************************************
	 * @param oneM2mId
	 * @return
	 *   
	 *   ID is the SP-relative-AE-ID or SP-relative-CSE-ID.
	 *   Omitting any leading "/". In the AE-ID case, any "/" characters
	 *   		embedded in the ID shall be replaced with ":" characters.
	 */
	public static String getTopicID(String oneM2mId) { 
		String id = oneM2mId;
		if(!id.startsWith("/")) return null;
		
		id = id.substring(1).replace("/", ":");
		return id;	
	}
	
	
	
	/*********************************************************************************
	 * @param topic
	 * @param start
	 * @return
	 * 
	 *  topic: "/oneM2M/req|resp/SP-relative-AE-ID|SP-relative-CSE-ID/SP-relative-AE-ID|SP-relative-CSE-ID
	 *     ex: /oneM2M/req/herit-in:ae_0001/herit-in
	 */
	
	public static String[] getOneM2mIDsFromTopic(String topic, String start) {
		if(!topic.startsWith(start)) {
			return null;
		}
	
		String[] ids = topic.substring(start.length()+1).split("/", 2);
		if(ids.length != 2) return null;
		
		ids[0] = "/" + ids[0].replace(":", "/");
		ids[1] = "/" + ids[1].replace(":", "/");

		return ids;
	}
	
	public static void main(String[] args) {
		String ONEM2M_TOPIC_ID = "/oneM2M";
		String REQ_TOPIC_ID = "/req";
		String RES_TOPIC_ID = "/resp";
		String topicBase = ONEM2M_TOPIC_ID + REQ_TOPIC_ID;
		String aeID = "/herit-cse/ae_0001";
		String cseID = "/herit-cse";
		
		String topic = topicBase + "/" + getTopicID(aeID) + "/" + getTopicID(cseID);
		System.out.println("topic=" + topic);
		
		String[] oneM2mIDs = getOneM2mIDsFromTopic(topic, topicBase);
		System.out.println(oneM2mIDs[0] + ", " + oneM2mIDs[1]);
		
	}
	
}