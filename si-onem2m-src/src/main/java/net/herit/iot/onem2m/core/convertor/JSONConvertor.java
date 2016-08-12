package net.herit.iot.onem2m.core.convertor;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import net.herit.iot.onem2m.resource.*;

import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSONConvertor<T> {
	
	private static boolean IsJsonIncludeRoot = true;
	private final Class<T> t;
	private JAXBContext context;
	private Unmarshaller um;
	private Marshaller m;
	private Logger log = LoggerFactory.getLogger(JSONConvertor.class);

	public JSONConvertor(Class<T> type, Class<T>[] types) {
		this.t = type;

		Map<String, Object> properties = new HashMap<String, Object>(3);
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, "net/herit/iot/onem2m/resource/oxm.xml");
		properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
//		properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, isJsonIncludeRoot);

		try {
			if (types != null) {
				context = JAXBContext.newInstance(types, properties);	
			} else {
				context = JAXBContext.newInstance(new Class[] {t}, properties);
			}

			initialize();
			
		} catch (Exception e) {
			// TBD
		}
	}

	public JSONConvertor(Class<T> type) {
		this.t = type;

		Map<String, Object> properties = new HashMap<String, Object>(3);
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, "net/herit/iot/onem2m/resource/oxm.xml");
		properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
		properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, IsJsonIncludeRoot);

		try {
			context = JAXBContext.newInstance(new Class[] {t}, properties);

			initialize();
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
		}
	}
	
	public JSONConvertor(Class<T> type, boolean isJsonIncludeRoot) {
		this.t = type;

		Map<String, Object> properties = new HashMap<String, Object>(3);
		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, "net/herit/iot/onem2m/resource/oxm.xml");
		properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
		properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, isJsonIncludeRoot);

		try {
			context = JAXBContext.newInstance(new Class[] {t}, properties);

			initialize();
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
		}
	}
	
	private void initialize() throws JAXBException {
		um = context.createUnmarshaller();
		// Set the Unmarshaller media type to JSON or XML
		um.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/json");
		// Set it to true if you need to include the JSON root element in the
//		um.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, isJsonIncludeRoot);
		

		m = context.createMarshaller();		
		//m.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
		// Set it to true if you need to include the JSON root element in the JSON output
		//m.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, false);
		// Set it to true if you need the JSON output to formatted
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
	}

	public T unmarshal(String json) throws Exception {
		synchronized (um) {
			StreamSource streamsource = new StreamSource(new StringReader(json));
					
			T obj = null;
	
			if(context == null) {
				throw new Exception("JAXBContext newInstance failed");
			}
	
			
			obj = (T)um.unmarshal(streamsource, t).getValue();
			
			return obj;
		}
	}
	
	public String marshal(T obj) throws Exception {
//		JAXBContext context = null;
//		Map<String, Object> properties = new HashMap<String, Object>(3);
//		properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, "net/herit/iot/onem2m/resource/oxm.xml");
//		properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
//		properties.put(JAXBContextProperties.JSON_INCLUDE_ROOT, false);
//
//		context = JAXBContext.newInstance(new Class[] {t}, properties);
		//context = JAXBContext.newInstance(t);
		
		
		synchronized (m) {
			
			if(context == null) {
				throw new Exception("JAXBContext newInstance failed");
			}
			
			Writer writer = new StringWriter();
			m.marshal(obj, writer);
			String json = writer.toString();
	//		System.out.println(out);
			
			return json;
		}
	}
	
	public static void main(String[] args) {
		String AE_json = "{ \"ae\": {\n" + 
				"   \"rn\" : \"ae0001\",\n" + 
				"   \"rty\" : 2,\n" + 
				"   \"ri\" : \"3234234293\",\n" + 
				"   \"pi\" : \"3234234292\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
				"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"at\" : [ \"//onem2m.hubiss.com/cse1\", \"//onem2m.hubiss.com/cse2\" ],\n" + 
				"   \"aa\" : [ \"pointOfAccess\", \"labels\" ],\n" + 
				"   \"apn\" : \"onem2mPlatformAdmin\",\n" + 
				"   \"api\" : \"C[authority-ID]/[registered-App-ID]\",\n" + 
				"   \"aei\" : \"//onem2m.herit.net/csebase/ae0001\",\n" + 
				"   \"poa\" : [ \"10.101.101.111:8080\" ],\n" + 
				"   \"or\" : \"[ontology access url]\",\n" + 
				"   \"nl\" : \"//onem2m.herit.net/csebase/node0001\",\n" + 
				"   \"rr\" : false,\n" + 
				"   \"ch\" : [ {\n" + 
				"      \"nm\" : \"container0001\",\n" + 
				"      \"typ\" : 17,\n" + 
				"      \"val\" : \"//onem2m.herit.net/csebase/container0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"group0001\",\n" + 
				"      \"typ\" : 14,\n" + 
				"      \"val\" : \"//onem2m.herit.net/csebase/group0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"acp0001\",\n" + 
				"      \"typ\" : 10001,\n" + 
				"      \"val\" : \"//onem2m.herit.net/csebase/acs0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"subscription0001\",\n" + 
				"      \"typ\" : 6,\n" + 
				"      \"val\" : \"//onem2m.herit.net/csebase/subscription0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"pollingChannel0001\",\n" + 
				"      \"typ\" : 22,\n" + 
				"      \"val\" : \"//onem2m.herit.net/csebase/pollingChannel0001\"\n" + 
				"   } ]\n" + 
				"} }";
	
		
		String Container_json = "{\n" + 
				"   \"rn\" : \"container0001\",\n" + 
				"   \"rty\" : 3,\n" + 
				"   \"ri\" : \"3234234293\",\n" + 
				"   \"pi\" : \"3234234292\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
				"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"at\" : [ \"//onem2m.hubiss.com/cse1\", \"//onem2m.hubiss.com/cse2\" ],\n" + 
				"   \"aa\" : [ \"pointOfAccess\", \"labels\" ],\n" + 
				"   \"st\" : 0,\n" + 
				"   \"cr\" : \"//onem2m.herit.net/csebase/ae0001\",\n" + 
				"   \"mni\" : 100,\n" + 
				"   \"mbs\" : 1024000,\n" + 
				"   \"mia\" : 36000,\n" + 
				"   \"cni\" : 10,\n" + 
				"   \"cbs\" : 10240,\n" + 
				"   \"li\" : \"//onem2m.hubiss.com/cseBase/lp1\",\n" + 
				"   \"or\" : \"[ontology access url]\",\n" + 
				"   \"ch\" : [ {\n" + 
				"      \"nm\" : \"container0001\",\n" + 
				"      \"typ\" : 3,\n" + 
				"      \"val\" : \"/container0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"contentInstance0001\",\n" + 
				"      \"typ\" : 4,\n" + 
				"      \"val\" : \"/contentInstance0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"subscription0001\",\n" +
				"      \"typ\" : 23,\n" + 
				"      \"val\" : \"/subscription0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"latest\",\n" + 
				"      \"typ\" : 4,\n" + 
				"      \"val\" : \"/latest\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"oldest\",\n" + 
				"      \"typ\" : 23,\n" + 
				"      \"val\" : \"/oldest\"\n" + 
				"   } ]\n" + 
				"}";
			
		String ContentInstance_json = "{\n" + 
				"   \"rn\" : \"ci0001\",\n" + 
				"   \"rty\" : 4,\n" + 
				"   \"ri\" : \"3234234293\",\n" + 
				"   \"pi\" : \"3234234292\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"at\" : [ \"//onem2m.hubiss.com/cse1\", \"//onem2m.hubiss.com/cse2\" ],\n" + 
				"   \"aa\" : [ \"contentInfo\", \"contentSize\", \"statTag\", \"ontologyRef\", \"content\" ],\n" + 
				"   \"st\" : 0,\n" + 
				"   \"cr\" : \"//onem2m.herit.net/csebase/ae0001\",\n" + 
				"   \"cnf\" : \"application/txt:0\",\n" + 
				"   \"cs\" : 2,\n" + 
				"   \"or\" : \"[ontology access url]\",\n" + 
				"   \"con\" : \"on\"\n" + 
				"}";
		
		String RemoteCSE_json = "{\n" + 
				"   \"rn\" : \"remoteCse001\",\n" + 
				"   \"rty\" : 16,\n" + 
				"   \"ri\" : \"rcse_00001\",\n" + 
				"   \"pi\" : \"csebase\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"gw001\" ],\n" + 
				"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"at\" : [ \"//onem2m.hubiss.com/cse1\", \"//onem2m.hubiss.com/cse2\" ],\n" + 
				"   \"aa\" : [ \"cseType\", \"pointOfAccess\", \"CSEBase\", \"CSE-ID\", \"requestReachability\" ],\n" + 
				"   \"cst\" : 1,\n" + 
				"   \"poa\" : [ \"http://10.101.101.12:8090\" ],\n" + 
				"   \"cb\" : \"//onem2m.herit.net/cse01\",\n" + 
				"   \"csi\" : \"cse000012\",\n" + 
				"   \"mei\" : \"TBD\",\n" + 
				"   \"tri\" : 3,\n" + 
				"   \"rr\" : true,\n" + 
				"   \"nl\" : \"//onem2m.herit.net/csebase/node0001\",\n" + 
				"   \"ch\" : [ {\n" + 
				"      \"nm\" : \"AE0001\",\n" + 
				"      \"typ\" : 2,\n" + 
				"      \"val\" : \"/AE0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"container0001\",\n" + 
				"      \"typ\" : 3,\n" + 
				"      \"val\" : \"/container0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"group0001\",\n" + 
				"      \"typ\" : 9,\n" + 
				"      \"val\" : \"/group0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"accessControlPolicy0001\",\n" + 
				"      \"typ\" : 1,\n" + 
				"      \"val\" : \"/accessControlPolicy0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"subscription0001\",\n" + 
				"      \"typ\" : 23,\n" + 
				"      \"val\" : \"/subscription0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"pollingChannel0001\",\n" + 
				"      \"typ\" : 15,\n" + 
				"      \"val\" : \"/pollingChannel0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"schedule0001\",\n" + 
				"      \"typ\" : 18,\n" + 
				"      \"val\" : \"/schedule0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"nodeAnnc0001\",\n" + 
				"      \"typ\" : 10014,\n" + 
				"      \"val\" : \"/nodeAnnc0001\"\n" + 
				"   } ]\n" + 
				"}";
		
		
		String AccessControlPolicy_json = "{\n" + 
				"   \"rn\" : \"acp0001\",\n" + 
				"   \"rty\" : 12,\n" + 
				"   \"ri\" : \"3234234293\",\n" + 
				"   \"pi\" : \"3234234292\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"acpForHomeSvc\" ],\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"at\" : [ \"//onem2m.hubiss.com/cse1\", \"//onem2m.hubiss.com/cse2\" ],\n" + 
				"   \"aa\" : [ \"pointOfAccess\", \"labels\" ],\n" + 
				"   \"pv\" : {\n" + 
				"      \"acr\" : [ {\n" + 
				"         \"acor\" : [ \"onem2m.herit.net\", \"//onem2m.hubiss.com/csebase/ae1\", \"//onem2m.hubiss.com/cse1\" ],\n" + 
				"         \"acop\" : 63,\n" + 
				"         \"acco\" : [ {\n" + 
				"            \"actw\" : [ \"* * 1-5 * * *\", \"* * 11-15 * * *\" ],\n" + 
				"            \"acip\" : {\n" + 
				"               \"ipv4\" : [ \"1.1.1.1\", \"1.1.1.2\" ]\n" + 
				"            },\n" + 
				"            \"aclr\" : {\n" + 
				"               \"accc\" : [ \"KR\" ],\n" + 
				"               \"accr\" : [ 3.141590118408203, 3.141590118408203, 3.141590118408203 ]\n" + 
				"            }\n" + 
				"         } ]\n" + 
				"      }, {\n" + 
				"         \"acor\" : [ \"onem2m.herit.net\", \"//onem2m.hubiss.com/csebase/ae1\", \"//onem2m.hubiss.com/cse1\" ],\n" + 
				"         \"acop\" : 2,\n" + 
				"         \"acco\" : [ {\n" + 
				"            \"actw\" : [ \"* * 1-5 * * *\", \"* * 11-15 * * *\" ],\n" + 
				"            \"acip\" : {\n" + 
				"               \"ipv4\" : [ \"1.1.1.1\", \"1.1.1.2\" ]\n" + 
				"            },\n" + 
				"            \"aclr\" : {\n" + 
				"               \"accc\" : [ \"KR\" ],\n" + 
				"               \"accr\" : [ 3.141590118408203, 3.141590118408203, 3.141590118408203 ]\n" + 
				"            }\n" + 
				"         } ]\n" + 
				"      } ]\n" + 
				"   },\n" + 
				"   \"pvs\" : {\n" + 
				"      \"acr\" : [ {\n" + 
				"         \"acor\" : [ \"onem2m.herit.net\", \"//onem2m.hubiss.com/csebase/ae1\", \"//onem2m.hubiss.com/cse1\" ],\n" + 
				"         \"acop\" : 63,\n" + 
				"         \"acco\" : [ {\n" + 
				"            \"actw\" : [ \"* * 1-5 * * *\", \"* * 11-15 * * *\" ],\n" + 
				"            \"acip\" : {\n" + 
				"               \"ipv4\" : [ \"1.1.1.1\", \"1.1.1.2\" ]\n" + 
				"            },\n" + 
				"            \"aclr\" : {\n" + 
				"               \"accc\" : [ \"KR\" ],\n" + 
				"               \"accr\" : [ 3.141590118408203, 3.141590118408203, 3.141590118408203 ]\n" + 
				"            }\n" + 
				"         } ]\n" + 
				"      }, {\n" + 
				"         \"acor\" : [ \"onem2m.herit.net\", \"//onem2m.hubiss.com/csebase/ae1\", \"//onem2m.hubiss.com/cse1\" ],\n" + 
				"         \"acop\" : 2,\n" + 
				"         \"acco\" : [ {\n" + 
				"            \"actw\" : [ \"* * 1-5 * * *\", \"* * 11-15 * * *\" ],\n" + 
				"            \"acip\" : {\n" + 
				"               \"ipv4\" : [ \"1.1.1.1\", \"1.1.1.2\" ]\n" + 
				"            },\n" + 
				"            \"aclr\" : {\n" + 
				"               \"accc\" : [ \"KR\" ],\n" + 
				"               \"accr\" : [ 3.141590118408203, 3.141590118408203, 3.141590118408203 ]\n" + 
				"            }\n" + 
				"         } ]\n" + 
				"      } ]\n" + 
				"   }\n" + 
				"}";
		

		String CSEBase_json = "{\n" + 
				"   \"rn\" : \"CSEBase\",\n" + 
				"   \"rty\" : 4,\n" + 
				"   \"ri\" : \"CSEBase0001\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"herit\", \"in\", \"cse\", \"platform\" ],\n" + 
				"   \"acpi\" : [ \"ACP0001\", \"ACP0002\" ],\n" + 
				"   \"cst\" : 1,\n" + 
				"   \"csi\" : \"CSEBase0001\",\n" + 
				"   \"srt\" : [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 10001, 10002, 10003, 10004, 10009 ],\n" + 
				"   \"poa\" : [ \"10.101.101.111:8080\" ],\n" + 
				"   \"nl\" : \"node0001\",\n" + 
				"   \"ch\" : [ {\n" + 
				"      \"nm\" : \"remoteCSE0011\",\n" + 
				"      \"typ\" : 16,\n" + 
				"      \"val\" : \"/remoteCSE0011\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"remoteCSE0012\",\n" + 
				"      \"typ\" : 16,\n" + 
				"      \"val\" : \"/remoteCSE0012\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"node1\",\n" + 
				"      \"typ\" : 14,\n" + 
				"      \"val\" : \"/node0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"AE0001\",\n" + 
				"      \"typ\" : 2,\n" + 
				"      \"val\" : \"/AE0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"AE0002\",\n" + 
				"      \"typ\" : 2,\n" + 
				"      \"val\" : \"/AE0002\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"ACP0001\",\n" + 
				"      \"typ\" : 1,\n" + 
				"      \"val\" : \"ACP0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"ACP0002\",\n" + 
				"      \"typ\" : 1,\n" + 
				"      \"val\" : \"ACP0002\"\n" +
				"   }, {\n" + 
				"      \"nm\" : \"SUBSCRITPION0001\",\n" + 
				"      \"typ\" : 23,\n" + 
				"      \"val\" : \"/SUBSCRITPION0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"SUBSCRITPION0002\",\n" + 
				"      \"typ\" : 23,\n" + 
				"      \"val\" : \"/SUBSCRITPION0002\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"localPolicy0001\",\n" + 
				"      \"typ\" : 10,\n" + 
				"      \"val\" : \"/localPolicy0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"schedule0001\",\n" + 
				"      \"typ\" : 18,\n" + 
				"      \"val\" : \"http://www.altova.com/\"\n" + 
				"   } ]\n" + 
				"}";
		
			
		String Group_json = "{\n" + 
				"   \"rn\" : \"group001\",\n" + 
				"   \"rty\" : 9,\n" + 
				"   \"ri\" : \"3234234293\",\n" + 
				"   \"pi\" : \"3234234292\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
				"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"at\" : [ \"//onem2m.hubiss.com/cse1\", \"//onem2m.hubiss.com/cse2\" ],\n" + 
				"   \"aa\" : [ \"memberType\", \"currentNrOfMembers\", \"maxNrOfMembers\", \"memberIDs\", \"membersAccessControlPolicyIDs\", \"memberTypeValidated\", \"consistencyStrategy\", \"groupName\" ],\n" + 
				"   \"cr\" : \"//onem2m.herit.net/csebase/ae0001\",\n" + 
				"   \"mt\" : 2,\n" + 
				"   \"cnm\" : 3,\n" + 
				"   \"mnm\" : 20,\n" + 
				"   \"mid\" : [ \"//onem2m.hubiss.com/cse1/ae0001\", \"C00001\", \"S00001\" ],\n" + 
				"   \"macp\" : [ \"3234234001\", \"3234234002\" ],\n" + 
				"   \"mtv\" : true,\n" + 
				"   \"csy\" : 1,\n" + 
				"   \"gn\" : \"stone's home app\",\n" + 
				"   \"ch\" : [ {\n" + 
				"      \"nm\" : \"subscription0001\",\n" + 
				"      \"typ\" : 23,\n" + 
				"      \"val\" : \"/subscription0001\"\n" + 
				"   } ]\n" + 
				"}";
		
		
		String PollingChannel_json = "{\n" + 
				"   \"rn\" : \"pcu0001\",\n" + 
				"   \"rty\" : 15,\n" + 
				"   \"ri\" : \"pc_0000001\",\n" + 
				"   \"pi\" : \"ae_0000001\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
				"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"pcu\" : \"http://test.org\"\n" + 
				"}";
		
		
		String Subscription_json = "{\n" + 
				"   \"rn\" : \"subscription0001\",\n" + 
				"   \"rty\" : 23,\n" + 
				"   \"ri\" : \"subs_000321\",\n" + 
				"   \"pi\" : \"ae_0001\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"subscription_for_ae1\" ],\n" + 
				"   \"acpi\" : [ \"acp0001\", \"acp0002\" ],\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"enc\" : {\n" + 
				"      \"crb\" : \"20150603T122321\",\n" + 
				"      \"cra\" : \"20150603T122321\",\n" + 
				"      \"ms\" : \"20150603T122321\",\n" + 
				"      \"us\" : \"20150603T122321\",\n" + 
				"      \"sts\" : 10,\n" + 
				"      \"stb\" : 100,\n" + 
				"      \"exb\" : \"20150603T122321\",\n" + 
				"      \"exa\" : \"20150603T122321\",\n" + 
				"      \"sza\" : 1024000,\n" + 
				"      \"szb\" : 1024,\n" + 
				"      \"om\" : [ 1, 2, 3, 4, 5 ],\n" + 
				"      \"atr\" : [ {\n" + 
				"         \"nm\" : \"creator\",\n" + 
				"         \"val\" : \"2342134234\"\n" + 
				"      }, {\n" + 
				"         \"nm\" : \"accessControlPolicyIDs\",\n" + 
				"         \"val\" : \"3234234001\"\n" + 
				"      } ],\n" + 
				"      \"net\" : [ 1, 2, 3, 4 ]\n" + 
				"   },\n" + 
				"   \"exc\" : 100,\n" + 
				"   \"nu\" : [ \"http://10.101.101.111:8080/notify\", \"http://10.101.101.112:8080/notify\" ],\n" + 
				"   \"gpi\" : \"//onem2m.herit.net/csebase/group001\",\n" + 
				"   \"nfu\" : \"http://10.101.101.111:8080/notify\",\n" + 
				"   \"bn\" : {\n" + 
				"      \"num\" : 10,\n" + 
				"      \"dur\" : \"P1Y2M2DT10H30M\"\n" + 
				"   },\n" + 
				"   \"rl\" : {\n" + 
				"      \"mnn\" : 50,\n" + 
				"      \"tww\" : \"P1Y2M2DT10H30M\"\n" + 
				"   },\n" + 
				"   \"psn\" : 20,\n" + 
				"   \"pn\" : 1,\n" + 
				"   \"nsp\" : 2,\n" + 
				"   \"ln\" : true,\n" + 
				"   \"nct\" : 3,\n" + 
				"   \"nec\" : \"3\",\n" + 
				"   \"cr\" : \"//onem2m.herit.net/csebase/ae0001\",\n" + 
				"   \"su\" : \"http://www.altova.com/\",\n" + 
				"   \"ch\" : {\n" + 
				"      \"nm\" : \"subscription0001\",\n" + 
				"      \"typ\" : 23,\n" + 
				"      \"val\" : \"/subscription0001\"\n" + 
				"   }\n" + 
				"}";
		
		String AEAnnc_json = "{\n" + 
				"   \"rn\" : \"aea_1\",\n" + 
				"   \"ty\" : 10002,\n" + 
				"   \"ri\" : \"3234234293\",\n" + 
				"   \"pi\" : \"3234234292\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"link\" : \"//homeiot.herit.net/csebase/ae1\",\n" + 
				"   \"poa\" : [ \"http://10.101.101.12:8090\" ],\n" + 
				"   \"ch\" : [ {\n" + 
				"      \"nm\" : \"subscription0001\",\n" + 
				"      \"typ\" : 23,\n" + 
				"      \"val\" : \"/subscription0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"container0001\",\n" + 
				"      \"typ\" : 3,\n" + 
				"      \"val\" : \"/container0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"containerAnnc0001\",\n" + 
				"      \"typ\" : 10003,\n" + 
				"      \"val\" : \"/containerAnnc0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"group0001\",\n" + 
				"      \"typ\" : 9,\n" + 
				"      \"val\" : \"/group0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"groupAnnc0001\",\n" + 
				"      \"typ\" : 10009,\n" + 
				"      \"val\" : \"/groupAnnc0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"accessControlPolicy0001\",\n" + 
				"      \"typ\" : 1,\n" + 
				"      \"val\" : \"/accessControlPolicy0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"accessControlPolicyAnnc0001\",\n" + 
				"      \"typ\" : 10001,\n" + 
				"      \"val\" : \"/accessControlPolicyAnnc0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"pollingChannel0001\",\n" + 
				"      \"typ\" : 15,\n" + 
				"      \"val\" : \"/pollingChannel0001\"\n" + 
				"   } ]\n" + 
				"}";
		
		String AccessControlPolicyAnnc_json = "{\n" + 
				"   \"rn\" : \"acpAnnc001\",\n" + 
				"   \"ty\" : 13,\n" + 
				"   \"ri\" : \"3234234293\",\n" + 
				"   \"pi\" : \"3234234292\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"link\" : \"//homeiot.herit.net/csebase/acp1\",\n" + 
				"   \"pv\" : {\n" + 
				"      \"acr\" : [ {\n" + 
				"         \"acor\" : [ \"onem2m.herit.net\", \"//onem2m.hubiss.com/csebase/ae1\", \"//onem2m.hubiss.com/cse1\" ],\n" + 
				"         \"acop\" : 63,\n" + 
				"         \"acco\" : [ {\n" + 
				"            \"actw\" : [ \"* * 1-5 * * *\", \"* * 11-15 * * *\" ],\n" + 
				"            \"acip\" : {\n" + 
				"               \"ipv4\" : [ \"1.1.1.1\", \"1.1.1.2\" ]\n" + 
				"            },\n" + 
				"            \"aclr\" : {\n" + 
				"               \"accc\" : [ \"KR\" ],\n" + 
				"               \"accr\" : [ 3.141590118408203, 3.141590118408203, 3.141590118408203 ]\n" + 
				"            }\n" + 
				"         } ]\n" + 
				"      }, {\n" + 
				"         \"acor\" : [ \"onem2m.herit.net\", \"//onem2m.hubiss.com/csebase/ae1\", \"//onem2m.hubiss.com/cse1\" ],\n" + 
				"         \"acop\" : 2,\n" + 
				"         \"acco\" : [ {\n" + 
				"            \"actw\" : [ \"* * 1-5 * * *\", \"* * 11-15 * * *\" ],\n" + 
				"            \"acip\" : {\n" + 
				"               \"ipv4\" : [ \"1.1.1.1\", \"1.1.1.2\" ]\n" + 
				"            },\n" + 
				"            \"aclr\" : {\n" + 
				"               \"accc\" : [ \"KR\" ],\n" + 
				"               \"accr\" : [ 3.141590118408203, 3.141590118408203, 3.141590118408203 ]\n" + 
				"            }\n" + 
				"         } ]\n" + 
				"      } ]\n" + 
				"   },\n" + 
				"   \"pvs\" : {\n" + 
				"      \"acr\" : [ {\n" + 
				"         \"acop\" : 63,\n" + 
				"         \"acco\" : [ {\n" + 
				"            \"actw\" : [ \"* * 1-5 * * *\", \"* * 11-15 * * *\" ],\n" + 
				"            \"acip\" : {\n" + 
				"               \"ipv4\" : [ \"1.1.1.1\", \"1.1.1.2\" ]\n" + 
				"            },\n" + 
				"            \"aclr\" : {\n" + 
				"               \"accc\" : [ \"KR\" ],\n" + 
				"               \"accr\" : [ 3.141590118408203, 3.141590118408203, 3.141590118408203 ]\n" + 
				"            }\n" + 
				"         } ]\n" + 
				"      }, {\n" + 
				"         \"acop\" : 2,\n" + 
				"         \"acco\" : [ {\n" + 
				"            \"actw\" : [ \"* * 1-5 * * *\", \"* * 11-15 * * *\" ],\n" + 
				"            \"acip\" : {\n" + 
				"               \"ipv4\" : [ \"1.1.1.1\", \"1.1.1.2\" ]\n" + 
				"            },\n" + 
				"            \"aclr\" : {\n" + 
				"               \"accc\" : [ \"KR\" ],\n" + 
				"               \"accr\" : [ 3.141590118408203, 3.141590118408203, 3.141590118408203 ]\n" + 
				"            }\n" + 
				"         } ]\n" + 
				"      } ]\n" + 
				"   }\n" + 
				"}";
		
		String ContainerAnnc_json = "{\n" + 
				"   \"rn\" : \"containerAnnc001\",\n" + 
				"   \"ty\" : 10003,\n" + 
				"   \"ri\" : \"3234234293\",\n" + 
				"   \"pi\" : \"3234234292\",\n" + 
				"   \"ct\" : \"20150603T122321\",\n" + 
				"   \"lt\" : \"20150603T122321\",\n" + 
				"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
				"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
				"   \"et\" : \"20150603T122321\",\n" + 
				"   \"link\" : \"//homeiot.herit.net/csebase/container001\",\n" + 
				"   \"st\" : 0,\n" + 
				"   \"mni\" : 100,\n" + 
				"   \"mbs\" : 1024000,\n" + 
				"   \"mia\" : 36000,\n" + 
				"   \"cni\" : 10,\n" + 
				"   \"cbs\" : 10240,\n" + 
				"   \"li\" : \"//onem2m.hubiss.com/cseBase/lp1\",\n" + 
				"   \"or\" : \"[ontology access url]\",\n" + 
				"   \"ch\" : [ {\n" + 
				"      \"nm\" : \"container0001\",\n" + 
				"      \"typ\" : 3,\n" + 
				"      \"val\" : \"/container0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"containerAnnc0001\",\n" + 
				"      \"typ\" : 10003,\n" + 
				"      \"val\" : \"/containerAnnc0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"contentInstance0001\",\n" + 
				"      \"typ\" : 4,\n" + 
				"      \"val\" : \"/contentInstance0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"contentInstanceAnnc0001\",\n" + 
				"      \"typ\" : 10004,\n" + 
				"      \"val\" : \"/contentInstanceAnnc0001\"\n" + 
				"   }, {\n" + 
				"      \"nm\" : \"subscription0001\",\n" + 
				"      \"typ\" : 23,\n" + 
				"      \"val\" : \"/subscription0001\"\n" + 
				"   } ]\n" + 
				"}";
		
			String ContentInstanceAnnc_json= "{\n" + 
					"   \"rn\" : \"cinA_001\",\n" + 
					"   \"ty\" : 10004,\n" + 
					"   \"ri\" : \"3234234293\",\n" + 
					"   \"pi\" : \"3234234292\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"link\" : \"//homeiot.herit.net/csebase/container001/ci001\",\n" + 
					"   \"st\" : 0,\n" + 
					"   \"cnf\" : \"application/txt:0\",\n" + 
					"   \"cs\" : 2,\n" + 
					"   \"or\" : \"[ontology access url]\",\n" + 
					"   \"con\" : \"on\"\n" + 
					"}";
		
			String Delivery_json = "{\n" + 
					"   \"rn\" : \"dlv_0001\",\n" + 
					"   \"ty\" : 4,\n" + 
					"   \"ri\" : \"deli_0000001\",\n" + 
					"   \"pi\" : \"csebase\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"delivery1\" ],\n" + 
					"   \"acpi\" : [ \"http://www.altova.com/\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"st\" : 0,\n" + 
					"   \"tg\" : \"gw_00000002\",\n" + 
					"   \"ls\" : \"20150603T122321\",\n" + 
					"   \"ec\" : \"1 \",\n" + 
					"   \"dmd\" : {\n" + 
					"      \"tcop\" : true,\n" + 
					"      \"tcin\" : [ \"http://www.altova.com/\" ]\n" + 
					"   },\n" + 
					"   \"arq\" : {\n" + 
					"      \"req\" : [ {\n" + 
					"      } ]\n" + 
					"   }\n" + 
					"}";
			
			String EventConfig_json = "{\n" + 
					"   \"rn\" : \"evcg_0001\",\n" + 
					"   \"ty\" : 16,\n" + 
					"   \"ri\" : \"evtcfg_00001/\",\n" + 
					"   \"pi\" : \"csebase/\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"evtcfg\" ],\n" + 
					"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"cr\" : \"gw_000001\",\n" + 
					"   \"evi\" : \"evt_00001\",\n" + 
					"   \"evt\" : 3,\n" + 
					"   \"evs\" : \"20150603T122321\",\n" + 
					"   \"eve\" : \"20150603T122321\",\n" + 
					"   \"opt\" : [ 4 ],\n" + 
					"   \"ds\" : 0,\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String ExecInstance_json = "{\n" + 
					"   \"rn\" : \"exin_001\",\n" + 
					"   \"ty\" : 9,\n" + 
					"   \"ri\" : \"execi_0000001\",\n" + 
					"   \"pi\" : \"mgmtcmd_000001\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"ei1\", \"ei2\" ],\n" + 
					"   \"acpi\" : [ \"acp0001\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"exs\" : 6,\n" + 
					"   \"exr\" : 9,\n" + 
					"   \"exd\" : true,\n" + 
					"   \"ext\" : \"token\",\n" + 
					"   \"exm\" : 2,\n" + 
					"   \"exf\" : \"P1Y2M2DT10H30M\",\n" + 
					"   \"exy\" : \"P1Y2M2DT10H30M\",\n" + 
					"   \"exn\" : 0,\n" + 
					"   \"exra\" : {\n" + 
					"      \"rst\" : [ {\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"rbo\" : [ {\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"uld\" : [ {\n" + 
					"         \"ftyp\" : \"String\",\n" + 
					"         \"url\" : \"http://www.altova.com/\",\n" + 
					"         \"unm\" : \"String\",\n" + 
					"         \"pwd\" : \"String\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"dld\" : [ {\n" + 
					"         \"ftyp\" : \"String\",\n" + 
					"         \"url\" : \"http://www.altova.com/\",\n" + 
					"         \"unm\" : \"String\",\n" + 
					"         \"pwd\" : \"String\",\n" + 
					"         \"fsi\" : 33,\n" + 
					"         \"tgf\" : \"String\",\n" + 
					"         \"dss\" : 0,\n" + 
					"         \"surl\" : \"http://www.altova.com/\",\n" + 
					"         \"stt\" : \"00000101T000000\",\n" + 
					"         \"cpt\" : \"00000101T000000\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"swin\" : [ {\n" + 
					"         \"url\" : \"http://www.altova.com/\",\n" + 
					"         \"uuid\" : \"String\",\n" + 
					"         \"unm\" : \"String\",\n" + 
					"         \"pwd\" : \"String\",\n" + 
					"         \"eer\" : \"String\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"swup\" : [ {\n" + 
					"         \"uuid\" : \"String\",\n" + 
					"         \"vr\" : \"String\",\n" + 
					"         \"url\" : \"http://www.altova.com/\",\n" + 
					"         \"unm\" : \"String\",\n" + 
					"         \"pwd\" : \"String\",\n" + 
					"         \"eer\" : \"String\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"swun\" : [ {\n" + 
					"         \"uuid\" : \"String\",\n" + 
					"         \"vr\" : \"String\",\n" + 
					"         \"eer\" : \"String\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ]\n" + 
					"   },\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String GroupAnnc_json = "{\n" + 
					"   \"rn\" : \"grpA_001\",\n" + 
					"   \"ty\" : 10009,\n" + 
					"   \"ri\" : \"3234234293\",\n" + 
					"   \"pi\" : \"3234234292\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
					"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"link\" : \"//homeiot.herit.net/csebase/group001\",\n" + 
					"   \"mt\" : 2,\n" + 
					"   \"cnm\" : 3,\n" + 
					"   \"mnm\" : 20,\n" + 
					"   \"mid\" : [ \"//onem2m.hubiss.com/cse1/ae0001\", \"C00001\", \"S00001\" ],\n" + 
					"   \"macp\" : [ \"3234234001\", \"3234234002\" ],\n" + 
					"   \"mtv\" : true,\n" + 
					"   \"csy\" : 1,\n" + 
					"   \"gn\" : \"stone's home app\",\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String LocationPolicy_json = "{\n" + 
					"   \"rn\" : \"lp0001\",\n" + 
					"   \"ty\" : 10,\n" + 
					"   \"ri\" : \"3234234293\",\n" + 
					"   \"pi\" : \"3234234292\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
					"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"at\" : [ \"//onem2m.hubiss.com/cse1\", \"//onem2m.hubiss.com/cse2\" ],\n" + 
					"   \"aa\" : [ \"locationSource\", \"locationUpdatePeriod\", \"locationTargetId\", \"locationServer\", \"locationContainerID\", \"locationContainerName\", \"locationStatus\" ],\n" + 
					"   \"los\" : 1,\n" + 
					"   \"lou\" : \"PT10M\",\n" + 
					"   \"lot\" : \"dddddaa\",\n" + 
					"   \"lor\" : \"adafas\",\n" + 
					"   \"loi\" : \"//onem2m.herit.net/csebase/ae0001/container0001\",\n" + 
					"   \"lon\" : \"locContainer001\",\n" + 
					"   \"lost\" : \"1\",\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			
			String LocationPolicyAnnc_json = "{\n" + 
					"   \"rn\" : \"lcpA_001\",\n" + 
					"   \"ty\" : 10010,\n" + 
					"   \"ri\" : \"3234234293\",\n" + 
					"   \"pi\" : \"3234234292\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
					"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"link\" : \"//homeiot.herit.net/csebase/lp0001\",\n" + 
					"   \"los\" : 1,\n" + 
					"   \"lou\" : \"PT10M\",\n" + 
					"   \"lot\" : \"dafdaf\",\n" + 
					"   \"lor\" : \"adasfd\",\n" + 
					"   \"loi\" : \"//onem2m.herit.net/csebase/ae0001/container0001\",\n" + 
					"   \"lon\" : \"locContainer001\",\n" + 
					"   \"lost\" : \"success\"\n" + 
					"}";
			
			String M2MServiceSubscriptionProfile_json = "{\n" + 
					"   \"rn\" : \"mssp_0001\",\n" + 
					"   \"ty\" : 10013,\n" + 
					"   \"ri\" : \"m2mSrvSubsPrf_0001/\",\n" + 
					"   \"pi\" : \"csebase/\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"mssp1\" ],\n" + 
					"   \"acpi\" : [ \"acp0001\", \"acp0002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"svr\" : [ \"04-001\", \"07-001\" ],\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"serviceSubscriptionNode0001\",\n" + 
					"      \"typ\" : 20,\n" + 
					"      \"val\" : \"/serviceSubscriptionNode0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String MgmtCmd_json = "{\n" + 
					"   \"rn\" : \"mgc_0001\",\n" + 
					"   \"ty\" : 8,\n" + 
					"   \"ri\" : \"mgmtcmd_00001\",\n" + 
					"   \"pi\" : \"node_00000001\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"reset\", \"cmd\" ],\n" + 
					"   \"acpi\" : [ \"acp0001\", \"acp0002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"dc\" : \"reset command\",\n" + 
					"   \"cmt\" : 1,\n" + 
					"   \"exra\" : {\n" + 
					"      \"rst\" : [ {\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"rbo\" : [ {\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"uld\" : [ {\n" + 
					"         \"ftyp\" : \"String\",\n" + 
					"         \"url\" : \"http://www.altova.com/\",\n" + 
					"         \"unm\" : \"String\",\n" + 
					"         \"pwd\" : \"String\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"dld\" : [ {\n" + 
					"         \"ftyp\" : \"String\",\n" + 
					"         \"url\" : \"http://www.altova.com/\",\n" + 
					"         \"unm\" : \"String\",\n" + 
					"         \"pwd\" : \"String\",\n" + 
					"         \"tgf\" : \"String\",\n" + 
					"         \"dss\" : 0,\n" + 
					"         \"surl\" : \"http://www.altova.com/\",\n" + 
					"         \"stt\" : \"00000101T000000\",\n" + 
					"         \"cpt\" : \"00000101T000000\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"swin\" : [ {\n" + 
					"         \"url\" : \"http://www.altova.com/\",\n" + 
					"         \"uuid\" : \"String\",\n" + 
					"         \"unm\" : \"String\",\n" + 
					"         \"pwd\" : \"String\",\n" + 
					"         \"eer\" : \"String\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"swup\" : [ {\n" + 
					"         \"uuid\" : \"String\",\n" + 
					"         \"vr\" : \"String\",\n" + 
					"         \"url\" : \"http://www.altova.com/\",\n" + 
					"         \"unm\" : \"String\",\n" + 
					"         \"pwd\" : \"String\",\n" + 
					"         \"eer\" : \"String\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ],\n" + 
					"      \"swun\" : [ {\n" + 
					"         \"uuid\" : \"String\",\n" + 
					"         \"vr\" : \"String\",\n" + 
					"         \"eer\" : \"String\",\n" + 
					"         \"any\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ]\n" + 
					"      } ]\n" + 
					"   },\n" + 
					"   \"exe\" : true,\n" + 
					"   \"ext\" : \"node_00001\",\n" + 
					"   \"exm\" : 2,\n" + 
					"   \"exf\" : \"P1Y2M2DT10H30M\",\n" + 
					"   \"exy\" : \"P1Y2M2DT10H30M\",\n" + 
					"   \"exn\" : 0,\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"execInstance0001\",\n" + 
					"      \"typ\" : 8,\n" + 
					"      \"val\" : \"/execInstance0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String Node_json = "{\n" + 
					"   \"rn\" : \"nod_0001\",\n" + 
					"   \"ty\" : 14,\n" + 
					"   \"ri\" : \"3234234293\",\n" + 
					"   \"pi\" : \"3234234292\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
					"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"at\" : [ \"//onem2m.hubiss.com/cse1\", \"//onem2m.hubiss.com/cse2\" ],\n" + 
					"   \"aa\" : [ \"hostedCSELink\" ],\n" + 
					"   \"ni\" : \"900001-HERITGW01-GW00001\",\n" + 
					"   \"hcl\" : \"/csebase/cse0001\",\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"memory0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/memory0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"battery0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/battery0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"areaNwkInfo0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/areaNwkInfo0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"areaNwkDeviceInfo0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/areaNwkDeviceInfo0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"firmware0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/firmware0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"software0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/software0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"deviceInfo0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/deviceInfo0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"deviceCapability0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/deviceCapability0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"reboot0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/reboot0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"eventLog0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/eventLog0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"cmdhPolicy0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/cmdhPolicy0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"activeCmdhPolicy0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/activeCmdhPolicy0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String NodeAnnc_json = "{\n" + 
					"   \"rn\" : \"nodA_0001\",\n" + 
					"   \"ty\" : 10014,\n" + 
					"   \"ri\" : \"3234234293\",\n" + 
					"   \"pi\" : \"3234234292\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"hubiss\", \"admin\" ],\n" + 
					"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"ni\" : \"900001-HERITGW01-GW00001\",\n" + 
					"   \"hcl\" : \"/csebase/cse0001\",\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"memory0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/memory0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"battery0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/battery0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"areaNwkInfo0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/areaNwkInfo0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"areaNwkDeviceInfo0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/areaNwkDeviceInfo0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"firmware0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/firmware0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"software0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/software0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"deviceInfo0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/deviceInfo0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"deviceCapability0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/deviceCapability0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"reboot0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/reboot0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"eventLog0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/eventLog0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"cmdhPolicy0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/cmdhPolicy0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"activeCmdhPolicy0001\",\n" + 
					"      \"typ\" : 10013,\n" + 
					"      \"val\" : \"/activeCmdhPolicy0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String RemoteCSEAnnc_json = "{\n" + 
					"   \"rn\" : \"csrA_0001\",\n" + 
					"   \"ty\" : 10016,\n" + 
					"   \"ri\" : \"3234234293\",\n" + 
					"   \"pi\" : \"csebase\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"hubiss\", \"home\", \"app1\" ],\n" + 
					"   \"acpi\" : [ \"3234234001\", \"3234234002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"link\" : \"//homeiot.herit.net/csebase\",\n" + 
					"   \"cst\" : 1,\n" + 
					"   \"poa\" : [ \"http://10.101.101.12:8090\" ],\n" + 
					"   \"cb\" : \"//onem2m.herit.net/gw01/csebase\",\n" + 
					"   \"csi\" : \"cse000012\",\n" + 
					"   \"rr\" : true,\n" + 
					"   \"nl\" : \"//onem2m.herit.net/csebase/node0001\",\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"AE0001\",\n" + 
					"      \"typ\" : 2,\n" + 
					"      \"val\" : \"/AE0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"AEAnnc0001\",\n" + 
					"      \"typ\" : 10002,\n" + 
					"      \"val\" : \"/AEAnnc0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"container0001\",\n" + 
					"      \"typ\" : 3,\n" + 
					"      \"val\" : \"/container0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"containerAnnc0001\",\n" + 
					"      \"typ\" : 10003,\n" + 
					"      \"val\" : \"/containerAnnc0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"group0001\",\n" + 
					"      \"typ\" : 9,\n" + 
					"      \"val\" : \"/group0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"groupAnnc0001\",\n" + 
					"      \"typ\" : 10009,\n" + 
					"      \"val\" : \"/groupAnnc0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"accessControlPolicy0001\",\n" + 
					"      \"typ\" : 1,\n" + 
					"      \"val\" : \"/accessControlPolicy0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"accessControlPolicyAnnc0001\",\n" + 
					"      \"typ\" : 10001,\n" + 
					"      \"val\" : \"/accessControlPolicyAnnc0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"schedule0001\",\n" + 
					"      \"typ\" : 18,\n" + 
					"      \"val\" : \"/schedule0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"pollingChannel0001\",\n" + 
					"      \"typ\" : 15,\n" + 
					"      \"val\" : \"/pollingChannel0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"nodeAnnc0001\",\n" + 
					"      \"typ\" : 10014,\n" + 
					"      \"val\" : \"/nodeAnnc0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"locationPolicyAnnc0001\",\n" + 
					"      \"typ\" : 10010,\n" + 
					"      \"val\" : \"/locationPolicyAnnc0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String Request_json = "{\n" + 
					"   \"rn\" : \"req_0001\",\n" + 
					"   \"ty\" : 12,\n" + 
					"   \"ri\" : \"req_000000001\",\n" + 
					"   \"pi\" : \"csebase\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"req01\", \"label\" ],\n" + 
					"   \"acpi\" : [ \"acp001\", \"acp002\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"st\" : 0,\n" + 
					"   \"opn\" : 1,\n" + 
					"   \"tg\" : \"//iot.herit.net/gw00001\",\n" + 
					"   \"og\" : \"//iot.herit.net/ap00001\",\n" + 
					"   \"rid\" : \"ap00001_req0000012\",\n" + 
					"   \"mi\" : {\n" + 
					"      \"ty\" : 4,\n" + 
					"      \"nm\" : \"String\",\n" + 
					"      \"ot\" : \"20150603T122321\",\n" + 
					"      \"rqet\" : \"20150603T122321\",\n" + 
					"      \"rset\" : \"20150603T122321\",\n" + 
					"      \"oet\" : \"20150603T122321\",\n" + 
					"      \"rt\" : {\n" + 
					"         \"rtv\" : 3,\n" + 
					"         \"nu\" : [ \"http://test.net\", \"http://test2.net\" ]\n" + 
					"      },\n" + 
					"      \"rp\" : \"P42D\",\n" + 
					"      \"rcn\" : 1,\n" + 
					"      \"ec\" : \"eventCategory\\n\\t\\t\\t\\t\\t\",\n" + 
					"      \"da\" : true,\n" + 
					"      \"gid\" : \"String\",\n" + 
					"      \"fc\" : {\n" + 
					"         \"crb\" : \"20150603T122321\",\n" + 
					"         \"cra\" : \"20150603T122321\",\n" + 
					"         \"ms\" : \"20150603T122321\",\n" + 
					"         \"us\" : \"20150603T122321\",\n" + 
					"         \"sts\" : 2,\n" + 
					"         \"stb\" : 0,\n" + 
					"         \"exb\" : \"20150603T122321\",\n" + 
					"         \"exa\" : \"20150603T122321\",\n" + 
					"         \"lbl\" : [ \"token\" ],\n" + 
					"         \"ty\" : 3,\n" + 
					"         \"sza\" : 0,\n" + 
					"         \"szb\" : 2,\n" + 
					"         \"cty\" : [ \"String\" ],\n" + 
					"         \"atr\" : [ {\n" + 
					"            \"nm\" : \"NCName\",\n" + 
					"            \"val\" : \"text\"\n" + 
					"         } ],\n" + 
					"         \"fu\" : 2,\n" + 
					"         \"lim\" : 0\n" + 
					"      },\n" + 
					"      \"drt\" : 2\n" + 
					"   },\n" + 
					"   \"pc\" : {\n" + 
					"   },\n" + 
					"   \"rs\" : 4,\n" + 
					"   \"ol\" : {\n" + 
					"      \"rsc\" : 4005,\n" + 
					"      \"rqi\" : \"token\",\n" + 
					"      \"pc\" : {\n" + 
					"      },\n" + 
					"      \"to\" : \"//originator of request\",\n" + 
					"      \"fr\" : \"//target of request\",\n" + 
					"      \"ot\" : \"20150603T122321\",\n" + 
					"      \"rset\" : \"20150603T122321\",\n" + 
					"      \"ec\" : \"4\"\n" + 
					"   },\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String Schedule_json = "{\n" + 
					"   \"rn\" : \"sce_0001\",\n" + 
					"   \"ty\" : 3,\n" + 
					"   \"ri\" : \"schd_0001\",\n" + 
					"   \"pi\" : \"rcse_0001\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"lbl\" : [ \"gw1_schedule\" ],\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"at\" : [ \"//onem2m.hubiss.com/cse1\", \"//onem2m.hubiss.com/cse2\" ],\n" + 
					"   \"aa\" : [ \"attr_list\" ],\n" + 
					"   \"se\" : {\n" + 
					"      \"sce\" : [ \"*\", \"0-5\", \"2,6,10\", \"*\", \"*\", \"*\" ]\n" + 
					"   },\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String ScheduleAnnc_json = "{\n" + 
					"   \"rn\" : \"schA_0001\",\n" + 
					"   \"ty\" : 10018,\n" + 
					"   \"ri\" : \"schdannc_0001\",\n" + 
					"   \"pi\" : \"rcse_0001\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"link\" : \"//homeiot.herit.net/csebase/lp0001/schda_00001\",\n" + 
					"   \"se\" : {\n" + 
					"      \"sce\" : [ \"*\", \"0-5\", \"2,6,10\", \"*\", \"*\", \"*\" ]\n" + 
					"   }\n" + 
					"}";
			
			String ServiceSubscribedAppRule_json = "{\n" + 
					"   \"rn\" : \"ssapprule1\",\n" + 
					"   \"ty\" : 19,\n" + 
					"   \"ri\" : \"ssapprule_0001\",\n" + 
					"   \"pi\" : \"casebase\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"apci\" : [ \"C123*X\", \"C124*X\", \"C125*X\" ],\n" + 
					"   \"aai\" : [ \"R111/HomeApp*\", \"R111/PublicApp*\" ],\n" + 
					"   \"aae\" : [ \"S109*\" ],\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String ServiceSubscribedNode_json = "{\n" + 
					"   \"rn\" : \"ssnode1\",\n" + 
					"   \"ty\" : 20,\n" + 
					"   \"ri\" : \"ssnode_00001\",\n" + 
					"   \"pi\" : \"casebase\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"ni\" : \"FFFFDDDD0EB0\",\n" + 
					"   \"csi\" : \"CSE00001\",\n" + 
					"   \"di\" : [ \"urn:dev:ops:001122-HERITGW1-GW0032432\", \"urn:dev:ops:001122-HERITGW1-GW0032433\", \"urn:dev:ops:001122-HERITGW1-GW0032434\" ],\n" + 
					"   \"ruleLinks\" : [ \"ssapprule_0001\", \"ssapprule_0002\", \"ssapprule_0003\" ],\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String StatsCollect_json = "{\n" + 
					"   \"rn\" : \"statcol_00001\",\n" + 
					"   \"ty\" : 21,\n" + 
					"   \"ri\" : \"statcol_00001\",\n" + 
					"   \"pi\" : \"csebase\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"cr\" : \"incse_herit\",\n" + 
					"   \"sci\" : \"stat_id_00001\",\n" + 
					"   \"cei\" : \"ae_id_of_accountingsystem\",\n" + 
					"   \"cdi\" : \"S190XX7T\",\n" + 
					"   \"srs\" : 1,\n" + 
					"   \"sm\" : 1,\n" + 
					"   \"cp\" : {\n" + 
					"      \"sce\" : [ \"*\", \"0-5\", \"2,6,10\", \"*\", \"*\", \"*\", \"*\", \"*\", \"8-20\", \"*\", \"*\", \"*\" ]\n" + 
					"   },\n" + 
					"   \"evi\" : \"eventcfg_id_0001\",\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
			String StatsConfig_json = "{\n" + 
					"   \"rn\" : \"statconfig1\",\n" + 
					"   \"ty\" : 22,\n" + 
					"   \"ri\" : \"statconfig_0001\",\n" + 
					"   \"pi\" : \"csebase\",\n" + 
					"   \"ct\" : \"20150603T122321\",\n" + 
					"   \"lt\" : \"20150603T122321\",\n" + 
					"   \"et\" : \"20150603T122321\",\n" + 
					"   \"cr\" : \"incse_id_or_inae_id\",\n" + 
					"   \"ch\" : [ {\n" + 
					"      \"nm\" : \"eventConfig0001\",\n" + 
					"      \"typ\" : 7,\n" + 
					"      \"val\" : \"/eventConfig0001\"\n" + 
					"   }, {\n" + 
					"      \"nm\" : \"subscription0001\",\n" + 
					"      \"typ\" : 23,\n" + 
					"      \"val\" : \"/subscription0001\"\n" + 
					"   } ]\n" + 
					"}";
			
		try {
			//-----------------------------------------------------------------------------------------
			String json;
//			JSONConvertor<AE> JC = new JSONConvertor<AE>(AE.class);
//			AE ae = (AE)JC.unmarshal(AE_json);
//			System.out.println("resourceName: " + ae.getResourceName());
//			
//			List<Resource> resources = ae.getContainerOrGroupOrAccessControlPolicy();
//			Container container = new Container();
//			container.setStateTag(BigInteger.TEN);
//			resources.add(container);
//			Group group = new Group();
//			group.setCreator("test2");
//			resources.add(group);
//
//			json = JC.marshal(ae);
//			System.out.println("json: " + json);

			
			//-----------------------------------------------------------------------------------------
			//-----------------------------------------------------------------------------------------
			// AE, Container, ContentInstance, RemoteCSE, AccessControlPolicy, CSEBase, Group
			// PollingChannel, Subscription, AEAnnc, AccessControlPolicyAnnc, ContainerAnnc
			// ContentInstanceAnnc, Delivery, EventConfig, ExecInstance, GroupAnnc
			// LocationPolicy, LocationPolicyAnnc, M2MServiceSubscriptionProfile, MgmtCmd, MgmtObj(????), MgmtObjAnnc(????)
			// Node, NodeAnnc, RemoteCSEAnnc, Request, Schedule, ScheduleAnnc, ServiceSubscribedAppRule
			// ServiceSubscribedNode, StatsCollect, StatsConfig

			JSONConvertor<AE> JC2 = new JSONConvertor<AE>(AE.class);
			AE resource = (AE)JC2.unmarshal(AE_json);
			System.out.println("resourceName: " + resource.getResourceName());
			json = JC2.marshal(resource);
			System.out.println(json);
			
			JSONConvertor<AE> JC3 = new JSONConvertor<AE>(AE.class, false);
			json = JC3.marshal(resource);
			System.out.println(json);
			
			AE resource2 = (AE)JC3.unmarshal(json);
			json = JC2.marshal(resource2);
			System.out.println(json);
			
			
			
//			System.out.println("AE_json");
//			System.out.println(AE_json);
//			System.out.println("Container_json");
//			System.out.println(Container_json);
//			System.out.println("ContentInstance_json		");
//			System.out.println(ContentInstance_json		);
//			System.out.println("RemoteCSE_json");
//			System.out.println(RemoteCSE_json);
//			System.out.println("AccessControlPolicy_json ");
//			System.out.println(AccessControlPolicy_json );
//			System.out.println("CSEBase_json"); 
//			System.out.println(CSEBase_json); 
//			System.out.println("Group_json");
//			System.out.println(Group_json);
//			System.out.println("PollingChannel_json ");
//			System.out.println(PollingChannel_json );
//			System.out.println("Subscription_json ");
//			System.out.println(Subscription_json );
//			System.out.println("AEAnnc_json ");
//			System.out.println(AEAnnc_json );
//			System.out.println("AccessControlPolicyAnnc_json ");
//			System.out.println(AccessControlPolicyAnnc_json );
//			System.out.println("ContainerAnnc_json ");
//			System.out.println(ContainerAnnc_json );
//			System.out.println("ContentInstanceAnnc_json");
//			System.out.println(ContentInstanceAnnc_json);
//			System.out.println("Delivery_json 	");		
//			System.out.println(Delivery_json 	);		
//			System.out.println("EventConfig_json ");			
//			System.out.println(EventConfig_json );			
//			System.out.println("ExecInstance_json");
//			System.out.println(ExecInstance_json);
//			System.out.println("GroupAnnc_json ");
//			System.out.println(GroupAnnc_json );
//			System.out.println("LocationPolicy_json 		");	
//			System.out.println(LocationPolicy_json 		);	
//			System.out.println("LocationPolicyAnnc_json ");
//			System.out.println(LocationPolicyAnnc_json );
//			System.out.println("M2MServiceSubscriptionProfile_json ");
//			System.out.println(M2MServiceSubscriptionProfile_json );
//			System.out.println("MgmtCmd_json"); 
//			System.out.println(MgmtCmd_json); 
//			System.out.println("Node_json ");
//			System.out.println(Node_json );
//			System.out.println("NodeAnnc_json ");
//			System.out.println(NodeAnnc_json );
//			System.out.println("RemoteCSEAnnc_json ");
//			System.out.println(RemoteCSEAnnc_json );
//			System.out.println("Request_json ");
//			System.out.println(Request_json );
//			System.out.println("Schedule_json");
//			System.out.println(Schedule_json);
//			System.out.println("ScheduleAnnc_json");
//			System.out.println(ScheduleAnnc_json);
//			System.out.println("ServiceSubscribedAppRule_json");
//			System.out.println(ServiceSubscribedAppRule_json);
//			System.out.println("ServiceSubscribedNode_json");
//			System.out.println(ServiceSubscribedNode_json);
//			System.out.println("StatsCollect_json ");
//			System.out.println(StatsCollect_json );
//			System.out.println("StatsCollect_json ");
//			System.out.println(StatsCollect_json );


			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
