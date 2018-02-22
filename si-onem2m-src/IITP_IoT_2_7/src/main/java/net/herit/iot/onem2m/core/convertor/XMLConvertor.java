package net.herit.iot.onem2m.core.convertor;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import net.herit.iot.message.onem2m.OneM2mRequest;
import net.herit.iot.onem2m.resource.*;

import org.eclipse.persistence.jaxb.MarshallerProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.internal.bind.v2.WellKnownNamespace;

public class XMLConvertor<T> {

	private final Class<T> t;
	private JAXBContext context;
	private Unmarshaller um;
	private Marshaller m;
	
	private static Logger log = LoggerFactory.getLogger(XMLConvertor.class);
	
//	private final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	public XMLConvertor(Class<T> type, String schema, Class<T>[] types) {
		this.t = type;
		
		try {
			if (types != null) {
				context = JAXBContext.newInstance(types);
													//ServiceSubscriptionProfile.class,MgmtObj.class,MgmtObjAnnc.class,
			} else {
				context = JAXBContext.newInstance(t);//AE.class);
			}
			if(context == null) {
				throw new Exception("JAXBContext newInstance failed");
			}			
			initialize(schema);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
		}
		
	}

	public XMLConvertor(Class<T> type, String schema) {
		this.t = type;
		
		try {
			context = JAXBContext.newInstance(t);//AE.class);
			
			if(context == null) {
				throw new Exception("JAXBContext newInstance failed");
			}
			initialize(schema);
			
		} catch (Exception e) {
			log.debug("Handled exception", e);
		}
		
	}
	
	private void initialize(String schema) throws JAXBException {

		um = context.createUnmarshaller();
		um.setProperty(UnmarshallerProperties.MEDIA_TYPE, "application/xml");
	
		m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper",
				new NamespacePrefixMapper() {
					@Override
					public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
						if(namespaceUri.equals(WellKnownNamespace.XML_SCHEMA_INSTANCE)) {
							return "xsi";
						} else 
							return "m2m";
					}
				});
		String schemaLocation = schema;

		if(schemaLocation != null) {
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.onem2m.org/xml/protocols " + schemaLocation);
		}
		m.setProperty(MarshallerProperties.MEDIA_TYPE, "application/xml");
	}

	public T unmarshal(String xml) throws Exception {
		//JAXBContext context = null;
		synchronized (um) {
			InputSource ins = new InputSource(new StringReader(xml));
	
			T obj = null;
			
			obj = (T)um.unmarshal(ins);
			
//			log.debug("############## {}" + m.);
			
			return obj;
		}
	}

	public String marshal(T obj) throws Exception {

		synchronized (m) {
			String resourceName = t.getSimpleName();
					
			JAXBElement<T> element = new JAXBElement<T> (new QName("http://www.onem2m.org/xml/protocols", resourceName), t, (T)obj);
	
			Writer writer = new StringWriter();
			m.marshal(obj, writer);
			String xml = writer.toString();
			
			return xml;
		}
		
	}

/*	public String marshal(T obj, String schema) throws Exception {
		String resourceName = t.getSimpleName();
		

		
		
		
//		switch(resType) {
//		case AE:
//			context = JAXBContext.newInstance(t);//AE.class);
//			schemaLocation = AE.SCHEMA_LOCATION;
//			resourceName = "AE";
//			//System.out.println("AE name=" + t.getSimpleName() );
//			break;
//			
//		case CONTAINER:
//			context = JAXBContext.newInstance(t);//Container.class);
//			schemaLocation = Container.SCHEMA_LOCATION;
//			resourceName = "container";
//			break;
//			
//		case CONTENT_INST:
//			context = JAXBContext.newInstance(t);//ContentInstance.class);
//			schemaLocation = ContentInstance.SCHEMA_LOCATION;
//			resourceName = "contentInstance";
//			break;
//			
//		default:
//			break;
//		}

		
//		m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
//		try{
//			m.setProperty("com.sun.xml.internal.bind.xmlHeaders", XML_HEADER);
//		} catch(PropertyException pex) {
//			m.setProperty("com.sun.xml.bind.xmlHeaders", XML_HEADER);
//		}

		String schemaLocation = schema;

		if(schemaLocation != null) {
			m.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, "http://www.onem2m.org/xml/protocols " + schemaLocation);
		}
		
		JAXBElement<T> element = new JAXBElement<T> (new QName("http://www.onem2m.org/xml/protocols", resourceName), t, (T)obj);
		//JAXBElement<AE> element = new JAXBElement<AE> (new QName("http://www.onem2m.org/xml/protocols", AE.class.getName()), AE.class, (AE)obj);
		
//		m.setProperty(MarshallerProperties.MEDIA_TYPE, "application/json");
//		// Set it to true if you need to include the JSON root element in the JSON output
//		m.setProperty(MarshallerProperties.JSON_INCLUDE_ROOT, true);
//		// Set it to true if you need the JSON output to formatted
//		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		
		Writer writer = new StringWriter();
		m.marshal(obj, writer);
		String xml = writer.toString();
//		//System.out.println(out);
		
		return xml;
	}
	*/

	public static void main(String[] args) {
	
		String AE_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->" +
				"<m2m:ae xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-AE-v1_2_0.xsd\" rn=\"ae0001\">" +
					"<ty>2</ty>        <!-- resourceType -->" +
					"<ri>3234234293</ri>        <!-- resourceID -->" +
					"<pi>3234234292</pi>        <!-- parentID -->" +
					"<ct>20150603T122321</ct>        <!-- creationTime -->" +
					"<lt>20150603T122321</lt>        <!-- lastModifiedTime -->" +
					"<lbl>hubiss admin</lbl>        <!-- labels -->" +
					"<acpi>3234234001 3234234002</acpi>        <!-- accessControlPolicyIDs -->" +
					"<et>20150603T122321</et>        <!-- expirationTime -->" +
					"<at>//onem2m.hubiss.com/cse1 //onem2m.hubiss.com/cse2</at>        <!-- announceTo -->" +
					"<aa>pointOfAccess labels</aa>        <!-- announcedAttribute -->" +
					"<apn>onem2mPlatformAdmin</apn>        <!-- appName -->" +
					"<api>C[authority-ID]/[registered-App-ID]</api>        <!-- App-ID -->" +
					"<aei>//onem2m.herit.net/csebase/ae0001</aei>        <!-- AE-ID -->" +
					"<poa>10.101.101.111:8080</poa>        <!-- pointOfAccess -->" +
					"<or>[ontology access url]</or>        <!-- ontologyRef -->" +
					"<nl>//onem2m.herit.net/csebase/node0001</nl>        <!-- nodeLink -->" +
					"<ch nm=\"container0001\" typ=\"17\">//onem2m.herit.net/csebase/container0001</ch>" +
					"<ch nm=\"group0001\" typ=\"14\">//onem2m.herit.net/csebase/group0001</ch>" +
					"<ch nm=\"acp0001\" typ=\"10001\">//onem2m.herit.net/csebase/acs0001</ch>" +
					"<ch nm=\"subscription0001\" typ=\"6\">//onem2m.herit.net/csebase/subscription0001</ch>" +
					"<ch nm=\"pollingChannel0001\" typ=\"22\">//onem2m.herit.net/csebase/pollingChannel0001</ch>" +
				"</m2m:ae>";
		
		String Container_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->" +
				"<m2m:cnt xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"container0001\">" +
					"<ty>3</ty> <!--resourceType-->" +
					"<ri>3234234293</ri> <!--resourceID-->" +
					"<pi>3234234292</pi> <!--parentID-->" +
					"<ct>20150603T122321</ct> <!--creationTime-->" +
					"<lt>20150603T122321</lt> <!--lastModifiedTime-->" +
					"<lbl>hubiss admin</lbl> <!--labels-->" +
					"<acpi>3234234001 3234234002</acpi> <!--accessControlPolicyIDs-->" +
					"<et>20150603T122321</et> <!--expirationTime-->" +
					"<at>//onem2m.hubiss.com/cse1 //onem2m.hubiss.com/cse2</at> <!--announceTo-->" +
					"<aa>pointOfAccess labels</aa> <!--announcedAttribute-->" +
					"<st>0</st> <!--stateTag-->" +
					"<cr>//onem2m.herit.net/csebase/ae0001</cr> <!--creator-->" +
					"<mni>100</mni> <!--maxNrOfInstances-->" +
					"<mbs>1024000</mbs> <!--maxByteSize-->" +
					"<mia>36000</mia> <!--maxInstanceAge-->" +
					"<cni>10</cni> <!--currentNrOfInstances-->" +
					"<cbs>10240</cbs> <!--currentByteSize-->" +
					"<li>//onem2m.hubiss.com/cseBase/lp1</li> <!--locationID-->" +
					"<or>[ontology access url]</or> <!--ontologyRef-->" +
					"<ch nm=\"container0001\" typ=\"3\">/container0001</ch>" +
					"<ch nm=\"contentInstance0001\" typ=\"4\">/contentInstance0001</ch>" +
					"<ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>" +
					"<ch nm=\"latest\" typ=\"4\">/latest</ch>" +
					"<ch nm=\"oldest\" typ=\"23\">/oldest</ch>" +
				"</m2m:cnt>";

		String ContentInstance_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->" +
				"<m2m:cin xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-contentInstance-v1_2_0.xsd\" rn=\"ci0001\">" +
					"<ty>4</ty> <!--resourceType-->" +
					"<ri>3234234293</ri> <!--resourceID-->" +
					"<pi>3234234292</pi> <!--parentID-->" +
					"<ct>20150603T122321</ct> <!--creationTime-->" +
					"<lt>20150603T122321</lt> <!--lastModifiedTime-->" +
					"<lbl>hubiss admin</lbl> <!--labels-->" +
					"<et>20150603T122321</et> <!--expirationTime-->" +
					"<at>//onem2m.hubiss.com/cse1 //onem2m.hubiss.com/cse2</at> <!--announceTo-->" +
					"<aa>contentInfo contentSize statTag ontologyRef content</aa> <!--announcedAttribute-->" +
					"<st>0</st> <!--stateTag-->" +
					"<cr>//onem2m.herit.net/csebase/ae0001</cr> <!--creator-->" +
					"<cnf>application/txt:0</cnf> <!--contentInfo-->" +
					"<cs>2</cs> <!--contentSize-->" +
					"<or>[ontology access url]</or> <!--ontologyRef-->" +
					"<con>on</con> <!--content-->" +
				"</m2m:cin>";
		
		String RemoteCSE_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"		<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"		<m2m:csr xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"remoteCse001\">\n" + 
				"			<ty>16</ty> <!--resourceType-->\n" + 
				"			<ri>rcse_00001</ri> <!--resourceID-->\n" + 
				"			<pi>csebase</pi> <!--parentID-->\n" + 
				"			<ct>20150603T122321</ct> <!--creationTime-->\n" + 
				"			<lt>20150603T122321</lt> <!--lastModifiedTime-->\n" + 
				"			<lbl>gw001</lbl> --> <!--labels-->\n" + 
				"			<acpi>3234234001 3234234002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"			<et>20150603T122321</et> <!--expirationTime-->\n" + 
				"			<at>//onem2m.hubiss.com/cse1 //onem2m.hubiss.com/cse2</at> <!--announceTo-->\n" + 
				"			<aa>cseType pointOfAccess CSEBase CSE-ID requestReachability</aa> <!--announcedAttribute-->\n" + 
				"			<cst>1</cst> --> <!--cseType-->\n" + 
				"			<poa>http://10.101.101.12:8090</poa> <!--pointOfAccess--> <!-- pollingChannel과 pointOfAccess 둘중 하나를 제공해야 함 -->\n" + 
				"			<cb>//onem2m.herit.net/cse01</cb> <!--CSEBase-->\n" + 
				"			<csi>cse000012</csi> <!--CSE-ID-->\n" + 
				"			<mei>TBD</mei> <!--M2M-Ext-ID-->" + 
				"			<tri>3</tri> <!--Trigger-Recipient-ID-->\n" + 
				"			<rr>true</rr> <!--requestReachability-->\n" + 
				"			<nl>//onem2m.herit.net/csebase/node0001</nl> <!--nodeLink-->\n" + 
				"		    <ch nm=\"AE0001\" typ=\"2\">/AE0001</ch>\n" + 
				"		    <ch nm=\"container0001\" typ=\"3\">/container0001</ch>\n" + 
				"		    <ch nm=\"group0001\" typ=\"9\">/group0001</ch>\n" + 
				"		    <ch nm=\"accessControlPolicy0001\" typ=\"1\">/accessControlPolicy0001</ch>\n" + 
				"		    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"		    <ch nm=\"pollingChannel0001\" typ=\"15\">/pollingChannel0001</ch>\n" + 
				"		    <ch nm=\"schedule0001\" typ=\"18\">/schedule0001</ch>\n" + 
				"		    <ch nm=\"nodeAnnc0001\" typ=\"10014\">/nodeAnnc0001</ch>\n" + 
				"		</m2m:csr>";
		
		String AccessControlPolicy_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:acp xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"acp0001\" >\n" + 
				"	<ty>12</ty> <!--resourceType-->\n" + 
				"	<ri>3234234293</ri> <!--resourceID-->\n" + 
				"	<pi>3234234292</pi> <!--parentID-->\n" + 
				"	<et>20150603T122321</et> <!--expirationTime-->\n" + 
				"	<lbl>acpForHomeSvc</lbl> <!--labels-->\n" + 
				"	<ct>20150603T122321</ct> <!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt> <!--lastModifiedTime-->\n" + 
				"	<at>//onem2m.hubiss.com/cse1 //onem2m.hubiss.com/cse2</at> <!--announceTo-->\n" + 
				"	<aa>pointOfAccess labels</aa> <!--announcedAttribute-->\n" + 
				"	<pv>\n" + 
				"		<acr>\n" + 
				"			<acor>onem2m.herit.net //onem2m.hubiss.com/csebase/ae1 //onem2m.hubiss.com/cse1</acor> <!-- accessControlOriginators -->\n" + 
				"			<acop>63</acop> <!-- accessControlOperations Create + Retrieve + Update + Delete + Notify + Discover -->\n" + 
				"			<acco>\n" + 
				"				<actw>* * 1-5 * * *</actw> <!-- accessControlWindow -->\n" + 
				"				<actw>* * 11-15 * * *</actw>\n" + 
				"				<acip>\n" + 
				"					<ipv4>1.1.1.1 1.1.1.2</ipv4> <!-- ipv4Addresses -->\n" + 
				"					<!-- <ipv6>::</ipv6> -->\n" + 
				"				</acip> <!-- accessControlIpAddresses -->\n" + 
				"				<aclr> <!-- accessControlLocationRegion -->\n" + 
				"					<accc>KR</accc> <!-- countryCode -->\n" + 
				"					<accr>3.1415901184082031 3.1415901184082031 3.1415901184082031</accr> <!-- circRegion -->\n" + 
				"				</aclr>\n" + 
				"			</acco> <!-- accessControlContexts -->\n" + 
				"		</acr> <!--accessControlRule-->\n" + 
				"		<acr>\n" + 
				"			<acor>onem2m.herit.net //onem2m.hubiss.com/csebase/ae1 //onem2m.hubiss.com/cse1</acor>\n" + 
				"			<acop>2</acop> <!-- Retrieve -->\n" + 
				"			<acco>\n" + 
				"				<actw>* * 1-5 * * *</actw>\n" + 
				"				<actw>* * 11-15 * * *</actw>\n" + 
				"				<acip>\n" + 
				"					<ipv4>1.1.1.1 1.1.1.2</ipv4>\n" + 
				"					<!-- <ipv6>::</ipv6> -->\n" + 
				"				</acip>\n" + 
				"				<aclr>\n" + 
				"					<accc>KR</accc> <!-- countryCode -->\n" + 
				"					<accr>3.1415901184082031 3.1415901184082031 3.1415901184082031</accr> <!-- circRegion -->\n" + 
				"				</aclr>\n" + 
				"			</acco>\n" + 
				"		</acr>\n" + 
				"	</pv> <!--privileges-->\n" + 
				"	<pvs>\n" + 
				"		<acr>\n" + 
				"			<acor>onem2m.herit.net //onem2m.hubiss.com/csebase/ae1 //onem2m.hubiss.com/cse1</acor>\n" + 
				"			<acop>63</acop> <!-- Create + Retrieve + Update + Delete + Notify + Discover -->\n" + 
				"			<acco>\n" + 
				"				<actw>* * 1-5 * * *</actw>\n" + 
				"				<actw>* * 11-15 * * *</actw>\n" + 
				"				<acip>\n" + 
				"					<ipv4>1.1.1.1 1.1.1.2</ipv4>\n" + 
				"					<!-- <ipv6>::</ipv6> -->\n" + 
				"				</acip>\n" + 
				"				<aclr>\n" + 
				"					<accc>KR</accc>\n" + 
				"					<accr>3.1415901184082031 3.1415901184082031 3.1415901184082031</accr>\n" + 
				"				</aclr>\n" + 
				"			</acco>\n" + 
				"		</acr>\n" + 
				"		<acr>\n" + 
				"			<acor>onem2m.herit.net //onem2m.hubiss.com/csebase/ae1 //onem2m.hubiss.com/cse1</acor>\n" + 
				"			<acop>2</acop> <!-- Retrieve -->\n" + 
				"			<acco>\n" + 
				"				<actw>* * 1-5 * * *</actw>\n" + 
				"				<actw>* * 11-15 * * *</actw>\n" + 
				"				<acip>\n" + 
				"					<ipv4>1.1.1.1 1.1.1.2</ipv4>\n" + 
				"					<!-- <ipv6>::</ipv6> -->\n" + 
				"				</acip>\n" + 
				"				<aclr>\n" + 
				"					<accc>KR</accc>\n" + 
				"					<accr>3.1415901184082031 3.1415901184082031 3.1415901184082031</accr>\n" + 
				"				</aclr>\n" + 
				"			</acco>\n" + 
				"		</acr>\n" + 
				"	</pvs> <!--selfPrivileges-->\n" + 
				"</m2m:acp>";
		
		String CSEBase_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<m2m:cb xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-CSEBase-v1_0_0.xsd\" rn=\"CSEBase\">\n" + 
				"	<ty>4</ty>		<!-- resourceType -->\n" + 
				"	<ri>CSEBase0001</ri>		<!-- resourceID -->\n" + 
				"	<ct>20150603T122321</ct>	<!-- creationTime -->\n" + 
				"	<lt>20150603T122321</lt>	<!-- lastModifiedTime -->\n" + 
				"	<lbl>herit in cse platform</lbl>	<!-- labels -->\n" + 
				"	<acpi>ACP0001 ACP0002</acpi>	<!-- accessControlPolicyIDs -->\n" + 
				"	<cst>1</cst>		<!-- cseType -->\n" + 
				"	<csi>CSEBase0001</csi>		<!-- CSE-ID -->\n" + 
				"	<srt>1 2 3 4 5 6 7 8 9 10 11 12 10001 10002 10003 10004 10009</srt>		<!-- supportedResourceType -->\n" + 
				"	<poa>10.101.101.111:8080</poa>		<!-- pointOfAccess -->\n" + 
				"	<nl>node0001</nl>		<!-- nodeLink -->\n" + 
				"	<ch nm=\"remoteCSE0011\" typ=\"16\">/remoteCSE0011</ch>		<!-- childResource -->\n" + 
				"	<ch nm=\"remoteCSE0012\" typ=\"16\">/remoteCSE0012</ch>\n" + 
				"	<ch nm=\"node1\" typ=\"14\">/node0001</ch>\n" + 
				"	<ch nm=\"AE0001\" typ=\"2\">/AE0001</ch>\n" + 
				"	<ch nm=\"AE0002\" typ=\"2\">/AE0002</ch>\n" + 
				"	<ch nm=\"ACP0001\" typ=\"1\">ACP0001</ch>\n" + 
				"	<ch nm=\"ACP0002\" typ=\"1\">ACP0002</ch>\n" + 
				"	<ch nm=\"SUBSCRITPION0001\" typ=\"23\">/SUBSCRITPION0001</ch>\n" + 
				"	<ch nm=\"SUBSCRITPION0002\" typ=\"23\">/SUBSCRITPION0002</ch>\n" + 
				"	<ch nm=\"localPolicy0001\" typ=\"10\">/localPolicy0001</ch>\n" + 
				"	<ch nm=\"schedule0001\" typ=\"18\">http://www.altova.com/</ch>\n" + 
				"</m2m:cb>";
		
		String Group_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:grp xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"group001\">\n" + 
				"	<ty>9</ty> <!--resourceType-->\n" + 
				"	<ri>3234234293</ri> <!--resourceID-->\n" + 
				"	<pi>3234234292</pi> <!--resourceID-->\n" + 
				"	<ct>20150603T122321</ct> <!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt> <!--lastModifiedTime-->\n" + 
				"	<lbl>hubiss admin</lbl> <!--labels-->\n" + 
				"	<acpi>3234234001 3234234002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et> <!--expirationTime-->\n" + 
				"	<at>//onem2m.hubiss.com/cse1 //onem2m.hubiss.com/cse2</at> <!--announceTo-->\n" + 
				"	<aa>memberType currentNrOfMembers maxNrOfMembers memberIDs membersAccessControlPolicyIDs memberTypeValidated consistencyStrategy groupName</aa> <!--announcedAttribute-->\n" + 
				"	<cr>//onem2m.herit.net/csebase/ae0001</cr> <!--creator-->\n" + 
				"\n" + 
				"	<mt>2</mt> <!--memberType--> <!-- resourceType + mixed:24 -->\n" + 
				"	<cnm>3</cnm> <!--currentNrOfMembers-->\n" + 
				"	<mnm>20</mnm> <!--maxNrOfMembers-->\n" + 
				"	<mid>//onem2m.hubiss.com/cse1/ae0001 C00001 S00001</mid> <!--memberIDs-->\n" + 
				"	<macp>3234234001 3234234002</macp> <!--membersAccessControlPolicyIDs-->\n" + 
				"	<mtv>true</mtv> <!--memberTypeValidated-->\n" + 
				"	<csy>1</csy> <!--consistencyStrategy-->	<!-- ABANDON_MEMBER, ABANDON_GROUP, SET_MIXED --> \n" + 
				"	<gn>stone's home app</gn> <!--groupName-->\n" + 
				"    \n" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:grp>";
		
		String PollingChannel_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:pch xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"pcu0001\">\n" + 
				"        <ty>15</ty> <!--resourceType-->\n" + 
				"        <ri>pc_0000001</ri> <!--resourceType-->\n" + 
				"        <pi>ae_0000001</pi> <!--parentID-->\n" + 
				"        <ct>20150603T122321</ct> <!--creationTime-->\n" + 
				"        <lt>20150603T122321</lt> <!--lastModifiedTime-->\n" + 
				"        <lbl>hubiss admin</lbl> <!--labels-->\n" + 
				"        <acpi>3234234001 3234234002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"        <et>20150603T122321</et> <!--expirationTime-->\n" + 
				"    <pcu>http://test.org</pcu> <!--pollingChannelURI--> <!--virtual resource to receive long polling request of related ae/cse -->\n" + 
				"</m2m:pch>";

		String Subscription_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:sub xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"subscription0001\">\n" + 
				"	<ty>23</ty> <!--resourceType-->\n" + 
				"	<ri>subs_000321</ri> <!--resourceID-->\n" + 
				"	<pi>ae_0001</pi> <!--parentID-->\n" + 
				"	<ct>20150603T122321</ct> <!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt> <!--lastModifiedTime-->\n" + 
				"	<lbl>subscription_for_ae1</lbl> <!--labels-->\n" + 
				"	<acpi>acp0001 acp0002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et> <!--expirationTime-->\n" + 
				"	<enc>\n" + 
				"		<crb>20150603T122321</crb> <!--createdBefore-->\n" + 
				"		<cra>20150603T122321</cra> <!--createdAfter-->\n" + 
				"		<ms>20150603T122321</ms> <!--modifiedSince-->\n" + 
				"		<us>20150603T122321</us> <!--unmodifiedSince-->\n" + 
				"		<sts>10</sts> <!--stateTagSmaller-->\n" + 
				"		<stb>100</stb> <!--stateTagBigger-->\n" + 
				"		<exb>20150603T122321</exb> <!--expireBefore-->\n" + 
				"		<exa>20150603T122321</exa> <!--expireAfter-->\n" + 
				"		<sza>1024000</sza> <!--sizeAbove-->\n" + 
				"		<szb>1024</szb> <!--sizeBelow-->\n" + 
				"		<rss>1</rss> <!--rss>1</rss--> <!--resourceStatus-->	<!-- childCreated -->\n" + 
				"		<net>2</net> <!--rss>2</rss--> <!--resourceStatus-->	<!-- childDeleted -->\n" + 
				"		<net>3</net> <!--rss>3</rss--> <!--resourceStatus-->	<!-- updated -->\n" + 
				"		<net>4</net> <!--rss>4</rss--> <!--resourceStatus-->	<!-- deleted -->\n" + 
				"		<om>1</om> <!--operationMonitor--> 	<!-- create -->\n" + 
				"		<om>2</om> <!--operationMonitor-->	<!-- retrieve -->\n" + 
				"		<om>3</om> <!--operationMonitor-->	<!-- update -->\n" + 
				"		<om>4</om> <!--operationMonitor-->	<!-- delete -->\n" + 
				"		<om>5</om> <!--operationMonitor-->	<!-- notify -->\n" + 
				"		<atr>\n" + 
				"			<nm>creator</nm> <!--name>creator</name-->\n" + 
				"			<val>2342134234</val> <!--value>2342134234</value-->\n" + 
				"		</atr> <!--attribute-->\n" + 
				"		<atr>\n" + 
				"			<nm>accessControlPolicyIDs</nm> <!--name>accessControlPolicyIDs</name-->\n" + 
				"			<val>3234234001</val> <!--value>3234234001</value-->\n" + 
				"		</atr> <!--attribute-->\n" + 
				"	</enc> <!--eventNotificationCriteria-->\n" + 
				"	<exc>100</exc> <!--expirationCounter-->\n" + 
				"	<nu>http://10.101.101.111:8080/notify http://10.101.101.112:8080/notify</nu> <!--notificationURI-->\n" + 
				"	<gpi>//onem2m.herit.net/csebase/group001</gpi> <!--groupID-->\n" + 
				"	<nfu>http://10.101.101.111:8080/notify</nfu> <!--notificationForwardingURI-->\n" + 
				"	<bn>\n" + 
				"		<num>10</num>\n" + 
				"		<dur>P1Y2M2DT10H30M</dur>\n" + 
				"	</bn> <!--batchNotify-->\n" + 
				"	<rl>\n" + 
				"		<mnn>50</mnn> <!--maxNrOfNotify>50</maxNrOfNotify-->\n" + 
				"		<tww>P1Y2M2DT10H30M</tww> <!--timeWindow>P1Y2M2DT10H30M</timeWindow-->\n" + 
				"	</rl> <!--rateLimit-->\n" + 
				"	<psn>20</psn> <!--preSubscriptionNotify-->\n" + 
				"	<pn>1</pn> <!--pendingNotification-->  <!-- 1:sendLatest, 2:sendAllPending -->\n" + 
				"	<nsp>2</nsp> <!--notificationStoragePriority-->\n" + 
				"	<ln>true</ln> <!--latestNotify-->\n" + 
				"	<nct>3</nct> <!--notificationContentType-->	<!-- 1:modifiedAttributes, 2:wholeResource, 3:referenceOnly -->\n" + 
				"    <nec>3</nec> <!--notificationEventCat-->	<!-- 1:Default, 2:Immediate, 3:BestEffort, 4:Latest -->\n" + 
				"	<cr>//onem2m.herit.net/csebase/ae0001</cr> <!--creator-->\n" + 
				"	<su>http://www.altova.com/</su> <!--subscriberURI-->\n" + 
				"    \n" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:sub>";
		
		String AEAnnc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:aeA xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"aea_1\">\n" + 
				"	<ty>10002</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>3234234292</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<!-- <lbl>hubiss home app1</lbl> --> <!--labels-->\n" + 
				"	<!-- <acpi>3234234001 3234234002</acpi> --> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime--> \n" + 
				"	<link>//homeiot.herit.net/csebase/ae1</link>\n" + 
				"	<!-- <apn>onem2mPlatformAdmin</apn> --> <!--appName-->\n" + 
				"	<!-- <api>C[authority-ID]/[registered-App-ID]</api> --> <!--App-ID-->\n" + 
				"	<!-- <aei>//onem2m.herit.net/csebase/ae0001</aei> --> <!--AE-ID-->\n" + 
				"	<poa>http://10.101.101.12:8090</poa>\n" + 
				"	<!-- <or>[ontology access url]</or> --> <!--ontologyRef-->\n" + 
				"	<!-- <nl>//onem2m.herit.net/csebase/node0001</nl> --> <!--nodeLink-->\n" + 
				"\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	<container>0..n</container>\n" + 
				"	<containerAnnc>0..n</containerAnnc>\n" + 
				"	<group>0..n</group>\n" + 
				"	<groupAnnc>0..n</groupAnnc>\n" + 
				"	<accessControlPolicy>0..n</accessControlPolicy>\n" + 
				"	<accessControlPolicyAnnc>0..n</accessControlPolicyAnnc>\n" + 
				"	<pollingChannel>0..n</pollingChannel>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"    <ch nm=\"container0001\" typ=\"3\">/container0001</ch>\n" + 
				"    <ch nm=\"containerAnnc0001\" typ=\"10003\">/containerAnnc0001</ch>\n" + 
				"    <ch nm=\"group0001\" typ=\"9\">/group0001</ch>\n" + 
				"    <ch nm=\"groupAnnc0001\" typ=\"10009\">/groupAnnc0001</ch>\n" + 
				"    <ch nm=\"accessControlPolicy0001\" typ=\"1\">/accessControlPolicy0001</ch>\n" + 
				"    <ch nm=\"accessControlPolicyAnnc0001\" typ=\"10001\">/accessControlPolicyAnnc0001</ch>\n" + 
				"    <ch nm=\"pollingChannel0001\" typ=\"15\">/pollingChannel0001</ch>\n" + 
				"</m2m:aeA>";
		
		String AccessControlPolicyAnnc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:acpA xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"acpAnnc001\">\n" + 
				"	<ty>13</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>3234234292</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<!--<labels>acpForHomeSvc</labels>-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<link>//homeiot.herit.net/csebase/acp1</link><!--link-->\n" + 
				"	\n" + 
				"	<pv>\n" + 
				"		<acr>\n" + 
				"			<acor>onem2m.herit.net //onem2m.hubiss.com/csebase/ae1 //onem2m.hubiss.com/cse1</acor><!--accessControlOriginators-->\n" + 
				"			<acop>63</acop> <!-- Create + Retrieve + Update + Delete + Notify + Discover --> <!--accessControlOperations-->\n" + 
				"			<acco>\n" + 
				"				<actw>* * 1-5 * * *</actw><!--accessControlWindow-->\n" + 
				"				<actw>* * 11-15 * * *</actw><!--accessControlWindow-->\n" + 
				"				<acip>\n" + 
				"					<ipv4>1.1.1.1 1.1.1.2</ipv4><!--ipv4Addresses-->\n" + 
				"					<!-- <ipv6Addresses>::</ipv6Addresses> -->\n" + 
				"				</acip><!--accessControlIpAddresses-->\n" + 
				"				<aclr>\n" + 
				"					<accc>KR</accc><!--countryCode-->\n" + 
				"					<accr>3.1415901184082031 3.1415901184082031 3.1415901184082031</accr><!--circRegion-->\n" + 
				"				</aclr><!--accessControlLocationRegion-->\n" + 
				"			</acco><!--accessControlContexts-->\n" + 
				"		</acr><!--accessControlRule-->\n" + 
				"		<acr>\n" + 
				"			<acor>onem2m.herit.net //onem2m.hubiss.com/csebase/ae1 //onem2m.hubiss.com/cse1</acor><!--accessControlOriginators-->\n" + 
				"			<acop>2</acop> <!-- Retrieve --> <!--accessControlOperations-->\n" + 
				"			<acco>\n" + 
				"				<actw>* * 1-5 * * *</actw><!--accessControlWindow-->\n" + 
				"				<actw>* * 11-15 * * *</actw><!--accessControlWindow-->\n" + 
				"				<acip>\n" + 
				"					<ipv4>1.1.1.1 1.1.1.2</ipv4><!--ipv4Addresses-->\n" + 
				"					<!-- <ipv6Addresses>::</ipv6Addresses> -->\n" + 
				"				</acip><!--accessControlIpAddresses-->\n" + 
				"				<aclr>\n" + 
				"					<accc>KR</accc><!--countryCode-->\n" + 
				"					<accr>3.1415901184082031 3.1415901184082031 3.1415901184082031</accr><!--circRegion-->\n" + 
				"				</aclr><!--accessControlLocationRegion-->\n" + 
				"			</acco><!--accessControlContexts-->\n" + 
				"		</acr><!--accessControlRule-->\n" + 
				"	</pv><!--privileges-->\n" + 
				"	<pvs>\n" + 
				"		<acr>\n" + 
				"			<acop>onem2m.herit.net //onem2m.hubiss.com/csebase/ae1 //onem2m.hubiss.com/cse1</acop><!--accessControlOriginators-->\n" + 
				"			<acop>63</acop> <!-- Create + Retrieve + Update + Delete + Notify + Discover --> <!--accessControlOperations-->\n" + 
				"			<acco>\n" + 
				"				<actw>* * 1-5 * * *</actw><!--accessControlWindow-->\n" + 
				"				<actw>* * 11-15 * * *</actw><!--accessControlWindow-->\n" + 
				"				<acip>\n" + 
				"					<ipv4>1.1.1.1 1.1.1.2</ipv4><!--ipv4Addresses-->\n" + 
				"					<!-- <ipv6Addresses>::</ipv6Addresses> -->\n" + 
				"				</acip><!--accessControlIpAddresses-->\n" + 
				"				<aclr>\n" + 
				"					<accc>KR</accc><!--countryCode-->\n" + 
				"					<accr>3.1415901184082031 3.1415901184082031 3.1415901184082031</accr><!--circRegion-->\n" + 
				"				</aclr><!--accessControlLocationRegion-->\n" + 
				"			</acco><!--accessControlContexts-->\n" + 
				"		</acr><!--accessControlRule-->\n" + 
				"		<acr>\n" + 
				"			<acop>onem2m.herit.net //onem2m.hubiss.com/csebase/ae1 //onem2m.hubiss.com/cse1</acop><!--accessControlOriginators-->\n" + 
				"			<acop>2</acop> <!-- Retrieve --> <!--accessControlOperations-->\n" + 
				"			<acco>\n" + 
				"				<actw>* * 1-5 * * *</actw><!--accessControlWindow-->\n" + 
				"				<actw>* * 11-15 * * *</actw><!--accessControlWindow-->\n" + 
				"				<acip>\n" + 
				"					<ipv4>1.1.1.1 1.1.1.2</ipv4><!--ipv4Addresses-->\n" + 
				"					<!-- <ipv6Addresses>::</ipv6Addresses> -->\n" + 
				"				</acip><!--accessControlIpAddresses-->\n" + 
				"				<aclr>\n" + 
				"					<accc>KR</accc><!--countryCode-->\n" + 
				"					<accr>3.1415901184082031 3.1415901184082031 3.1415901184082031</accr><!--circRegion-->\n" + 
				"				</aclr><!--accessControlLocationRegion-->\n" + 
				"			</acco><!--accessControlContexts-->\n" + 
				"		</acr><!--accessControlRule-->\n" + 
				"	</pvs><!--selfPrivileges-->\n" + 
				"</m2m:acpA>";
		
		String ContainerAnnc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:cntA xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"containerAnnc001\">\n" + 
				"	<ty>10003</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>3234234292</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>hubiss admin</lbl> <!--labels-->\n" + 
				"	<acpi>3234234001 3234234002</acpi>  <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime--> \n" + 
				"	<link>//homeiot.herit.net/csebase/container001</link>\n" + 
				"	<st>0</st> <!--stateTag-->\n" + 
				"	<mni>100</mni> <!--maxNrOfInstances-->\n" + 
				"  <mbs>1024000</mbs> <!--maxByteSize-->\n" + 
				"	<mia>36000</mia>  <!--maxInstanceAge-->\n" + 
				"	<cni>10</cni> <!--currentNrOfInstances-->\n" + 
				"	<cbs>10240</cbs>  <!--currentByteSize-->\n" + 
				"	<li>//onem2m.hubiss.com/cseBase/lp1</li>  <!--locationID-->\n" + 
				"	<or>[ontology access url]</or> <!--ontologyRef-->\n" + 
				"	\n" + 
				"	<!--" + 
				"	<container>0..n</container>\n" + 
				"	<containerAnnc>0..n</containerAnnc>\n" + 
				"	<contentInstance>0..n</contentInstance>\n" + 
				"	<contentInstanceAnnc>0..n</contentInstanceAnnc>\n" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"container0001\" typ=\"3\">/container0001</ch>\n" + 
				"    <ch nm=\"containerAnnc0001\" typ=\"10003\">/containerAnnc0001</ch>\n" + 
				"    <ch nm=\"contentInstance0001\" typ=\"4\">/contentInstance0001</ch>\n" + 
				"    <ch nm=\"contentInstanceAnnc0001\" typ=\"10004\">/contentInstanceAnnc0001</ch>\n" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:cntA>";
		
		
		String ContentInstanceAnnc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:cinA xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"cinA_001\">\n" + 
				"	<ty>10004</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>3234234292</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>hubiss admin</lbl> <!--labels-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<link>//homeiot.herit.net/csebase/container001/ci001</link>\n" + 
				"	<st>0</st> <!--stateTag-->\n" + 
				"	<cnf>application/txt:0</cnf>  <!--contentInfo-->\n" + 
				"	<cs>2</cs> <!--contentSize-->\n" + 
				"	<or>[ontology access url]</or>  <!--ontologyRef-->\n" + 
				"	<con>on</con> <!--content-->\n" + 
				"</m2m:cinA>";
		
		
		String Delivery_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:dlv xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-delivery-v1_0_0.xsd\" rn=\"dlv_0001\">\n" + 
				"	<ty>4</ty><!--resourceType-->\n" + 
				"	<ri>deli_0000001</ri><!--resourceID-->\n" + 
				"	<pi>csebase</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>delivery1</lbl>  <!--labels-->\n" + 
				"	<acpi>http://www.altova.com/</acpi> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<st>0</st><!--stateTag-->\n" + 
				"	<sr>gw_00000001</sr><!--source-->\n" + 
				"	<tg>gw_00000002</tg><!--target-->\n" + 
				"	<ls>20150603T122321</ls><!--lifespan-->\n" + 
				"	<ec>1 <!-- Default:1, Immediate:2, BestEffort:3, 4:Latest --></ec><!--eventCat-->\n" + 
				"	<dmd>\n" + 
				"		<tcop>true</tcop><!--tracingOption-->\n" + 
				"		<tcin>http://www.altova.com/</tcin><!--tracingInfo-->\n" + 
				"	</dmd><!--deliveryMetaData-->\n" + 
				"	<arq>\n" + 
				"		<req>1..n</req> <!--request-->\n" + 
				"	</arq><!--aggregatedRequest-->\n" + 
				"</m2m:dlv>";
		
		String EventConfig_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:evcg xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-eventConfig-v1_0_0.xsd\" rn=\"evcg_0001\">\n" + 
				"	<ty>16</ty><!--resourceType-->\n" + 
				"	<ri>evtcfg_00001/</ri><!--resourceID-->\n" + 
				"	<pi>csebase/</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>evtcfg </lbl> <!--labels-->\n" + 
				"	<acpi>3234234001 3234234002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<cr>gw_000001</cr><!--creator-->\n" + 
				"	<evi>evt_00001</evi><!--eventID-->\n" + 
				"	<evt>3</evt> <!-- Data Operation:1, Storage based:2, Timer Based --> <!--eventType-->\n" + 
				"	<evs>20150603T122321</evs> <!--eventStart-->\n" + 
				"	<eve>20150603T122321</eve> <!--eventEnd-->\n" + 
				"	<opt>4</opt> <!--C:1, R:2, U:3, D:4, N:5--> <!--operationType-->\n" + 
				"	<ds>0</ds> <!--dataSize-->\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"   <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:evcg>";
		
		String ExecInstance_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:exin xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-execInstance-v1_0_0.xsd\" rn=\"exin_001\">\n" + 
				"	<ty>9</ty><!--resourceType-->\n" + 
				"	<ri>execi_0000001</ri><!--resourceID-->\n" + 
				"	<pi>mgmtcmd_000001</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>ei1 ei2</lbl> <!--labels-->\n" + 
				"	<acpi>acp0001</acpi><!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<exs>6</exs> <!-- INITIATED:1, PENDING:2, FINISHED:3, CANCELLING:4, CANCELLED:5, STATUS_ NON_CANCELLABLE:6 --> <!--execStatus-->\n" + 
				"	<exr>9</exr> <!-- STATUS_REQUEST_UNSUPPORTED:1, ~ STATUS_INVALID_DEPLOYMENT_UNIT_UPDATE_VERSION_EXISTS:30 --> <!--execResult-->\n" + 
				"	<exd>true</exd>  <!--execDisable-->\n" + 
				"	<ext>token</ext><!--execTarget-->\n" + 
				"	<exm>2</exm>  <!-- IMMEDIATEONCE:1, IMMEDIATEREPEAT:2, RANDOMONCE:3, RANDOMREPEAT:4 --> <!--execMode-->\n" + 
				"	<exf>P1Y2M2DT10H30M</exf>  <!--execFrequency-->\n" + 
				"	<exy>P1Y2M2DT10H30M</exy> <!--execDelay-->\n" + 
				"	<exn>0</exn> <!--execNumber-->\n" + 
				"	<exra>\n" + 
				"		<rst>\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</rst> <!-- reset -->\n" + 
				"		<rbo>\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</rbo> <!-- reboot -->\n" + 
				"		<uld>\n" + 
				"			<ftyp>String</ftyp> <!-- fileType -->\n" + 
				"			<url>http://www.altova.com/</url> <!-- URL -->\n" + 
				"			<unm>String</unm> <!-- username -->\n" + 
				"			<pwd>String</pwd> <!-- password -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</uld> <!-- upload -->\n" + 
				"		<dld>\n" + 
				"			<ftyp>String</ftyp> <!-- fileType -->\n" + 
				"			<url>http://www.altova.com/</url> <!-- URL -->\n" + 
				"			<unm>String</unm> <!-- username -->\n" + 
				"			<pwd>String</pwd> <!-- password -->\n" + 
				"			<fsi>33</fsi> <!-- filesize -->\n" + 
				"			<tgf>String</tgf> <!-- targetFile -->\n" + 
				"			<dss>0</dss> <!-- delaySeconds -->\n" + 
				"			<surl>http://www.altova.com/</surl> <!-- successURL -->\n" + 
				"			<stt>00000101T000000</stt> <!-- startTime -->\n" + 
				"			<cpt>00000101T000000</cpt> <!-- completeTime -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</dld> <!-- download -->\n" + 
				"		<swin>\n" + 
				"			<url>http://www.altova.com/</url> <!-- URL -->\n" + 
				"			<uuid>String</uuid> <!-- UUID -->\n" + 
				"			<unm>String</unm> <!-- username -->\n" + 
				"			<pwd>String</pwd> <!-- password -->\n" + 
				"			<eer>String</eer> <!-- executionEnvRef -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</swin> <!-- softwareInstall -->\n" + 
				"		<swup>\n" + 
				"			<uuid>String</uuid> <!-- UUID -->\n" + 
				"			<vr>String</vr> <!-- version -->\n" + 
				"			<url>http://www.altova.com/</url> <!-- URL -->\n" + 
				"			<unm>String</unm> <!-- username -->\n" + 
				"			<pwd>String</pwd> <!-- password -->\n" + 
				"			<eer>String</eer> <!-- executionEnvRef -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</swup> <!-- softwareUpdate -->\n" + 
				"		<swun>\n" + 
				"			<uuid>String</uuid> <!-- UUID -->\n" + 
				"			<vr>String</vr> <!-- version -->\n" + 
				"			<eer>String</eer> <!-- executionEnvRef -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</swun> <!-- softwareUninstall --> \n" + 
				"	</exra>  <!--execReqArgs-->\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:exin>";
		
		String GroupAnnc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:grpA xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"grpA_001\">\n" + 
				"	<ty>10009</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>3234234292</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>hubiss admin</lbl><!--labels-->\n" + 
				"	<acpi>3234234001 3234234002</acpi><!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<link>//homeiot.herit.net/csebase/group001</link>\n" + 
				"	<mt>2</mt>  <!--memberType-->\n" + 
				"	<cnm>3</cnm>  <!--currentNrOfMembers-->\n" + 
				"	<mnm>20</mnm> <!--maxNrOfMembers-->\n" + 
				"	<mid>//onem2m.hubiss.com/cse1/ae0001 C00001 S00001</mid> <!--memberIDs-->\n" + 
				"	<macp>3234234001 3234234002</macp> <!--membersAccessControlPolicyIDs-->\n" + 
				"	<mtv>true</mtv> <!--memberTypeValidated-->\n" + 
				"	<csy>1</csy>	<!-- ABANDON_MEMBER, ABANDON_GROUP, SET_MIXED --> <!--consistencyStrategy-->\n" + 
				"	<gn>stone's home app</gn> <!--groupName-->\n" + 
				"\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:grpA>";
		
		String LocationPolicy_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:lcp xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"lp0001\">\n" + 
				"	<ty>10</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>3234234292</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>hubiss admin</lbl> <!--labels-->\n" + 
				"	<acpi>3234234001 3234234002</acpi>  <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<at>//onem2m.hubiss.com/cse1 //onem2m.hubiss.com/cse2</at> <!--announceTo-->\n" + 
				"	<aa>locationSource locationUpdatePeriod locationTargetId locationServer locationContainerID locationContainerName locationStatus </aa> <!--announcedAttribute-->\n" + 
				"	<los>1</los><!--locationSource-->\n" + 
				"	<lou>PT10M</lou> <!--locationUpdatePeriod-->\n" + 
				"	<!-- attribute for node using location server to get location -->" + 
				"	<lot>dddddaa</lot> locationTargetID\n" + 
				"	<lor>adafas</lor> locationServer\n" + 
				"	<loi>//onem2m.herit.net/csebase/ae0001/container0001</loi> <!--locationContainerID-->\n" + 
				"	<lon>locContainer001</lon> <!--locationContainerName-->\n" + 
				"	<lost>1</lost> <!-- Successful:1, Failure:2, In-Process:3 --> <!--locationStatus--> \n" + 
				"	<!-- " + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:lcp>";
		
		
		String LocationPolicyAnnc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:lcpA xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"lcpA_001\">\n" + 
				"	<ty>10010</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>3234234292</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>hubiss admin</lbl> <!--labels-->\n" + 
				"	<acpi>3234234001 3234234002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<link>//homeiot.herit.net/csebase/lp0001</link>\n" + 
				"	<los>1</los> <!--locationSource-->\n" + 
				"	<lou>PT10M</lou> <!--locationUpdatePeriod-->\n" + 
				"	<!-- attribute for -->" + 
				"	<lot>dafdaf</lot> locationTargetID\n" + 
				"	<lor>adasfd</lor> locationServer\n" + 
				"	<loi>//onem2m.herit.net/csebase/ae0001/container0001</loi> <!--locationContainerID-->\n" + 
				"	<lon>locContainer001</lon> <!--locationContainerName-->\n" + 
				"	<lost>success</lost>  <!--locationStatus-->\n" + 
				"\n" + 
				"</m2m:lcpA>";
		
		String M2MServiceSubscriptionProfile_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:mssp xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-m2mServiceSubscriptionProfile-v1_0_0.xsd\" rn=\"mssp_0001\">\n" + 
				"	<ty>10013</ty><!--resourceType-->\n" + 
				"	<ri>m2mSrvSubsPrf_0001/</ri><!--resourceID-->\n" + 
				"	<pi>csebase/</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>mssp1</lbl><!--labels-->\n" + 
				"	<acpi>acp0001 acp0002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<svr>04-001 07-001</svr> <!--serviceRoles-->\n" + 
				"	<!-- " + 
				"	<subscription>0..n</subscription>\n" + 
				"	<serviceSubscriptionNode>0..n</serviceSubscribedNode>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"    <ch nm=\"serviceSubscriptionNode0001\" typ=\"20\">/serviceSubscriptionNode0001</ch>\n" + 
				"</m2m:mssp>";
		
		String MgmtCmd_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:mgc xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-mgmtCmd-v1_0_0.xsd\" rn=\"mgc_0001\">\n" + 
				"	<ty>8</ty><!--resourceType-->\n" + 
				"	<ri>mgmtcmd_00001</ri><!--resourceID-->\n" + 
				"	<pi>node_00000001</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>reset cmd</lbl>  <!--labels-->\n" + 
				"	<acpi>acp0001 acp0002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<dc>reset command</dc> <!--description-->\n" + 
				"	<cmt>1</cmt> <!-- RESET:1, REBOOT:2, UPLOAD:3, DODWNLOAD:4, SWINSTAQLL:5, SWUNINSTALL:6, SWUPDATE:7 --> <!--cmdType-->\n" + 
				"	<exe>true</exe><!--execEnable-->\n" + 
				"	<ext>node_00001</ext><!--execTarget-->\n" + 
				"	<exm>2</exm> <!-- IMMEDIATEONCE:1, IMMEDIATEREPEAT:2, RANDOMONCE:3, RANDOMREPEAT:4 --> <!--execMode-->\n" + 
				"	<exf>P1Y2M2DT10H30M</exf> <!--execFrequency-->\n" + 
				"	<exy>P1Y2M2DT10H30M</exy><!--execDelay-->\n" + 
				"	<exn>0</exn> <!--execNumber-->\n" + 
				"	<exra>\n" + 
				"		<rst>\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</rst> <!-- reset -->\n" + 
				"		<rbo>\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</rbo> <!-- reboot -->\n" + 
				"		<uld>\n" + 
				"			<ftyp>String</ftyp> <!-- fileType -->\n" + 
				"			<url>http://www.altova.com/</url> <!-- URL -->\n" + 
				"			<unm>String</unm> <!-- username -->\n" + 
				"			<pwd>String</pwd> <!-- password -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</uld> <!-- upload -->\n" + 
				"		<dld>\n" + 
				"			<ftyp>String</ftyp> <!-- fileType -->\n" + 
				"			<url>http://www.altova.com/</url> <!-- URL -->\n" + 
				"			<unm>String</unm> <!-- username -->\n" + 
				"			<pwd>String</pwd> <!-- password -->\n" + 
				"			<fsi>String</fsi> <!-- filesize -->\n" + 
				"			<tgf>String</tgf> <!-- targetFile -->\n" + 
				"			<dss>0</dss> <!-- delaySeconds -->\n" + 
				"			<surl>http://www.altova.com/</surl> <!-- successURL -->\n" + 
				"			<stt>00000101T000000</stt> <!-- startTime -->\n" + 
				"			<cpt>00000101T000000</cpt> <!-- completeTime -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</dld> <!-- download -->\n" + 
				"		<swin>\n" + 
				"			<url>http://www.altova.com/</url> <!-- URL -->\n" + 
				"			<uuid>String</uuid> <!-- UUID -->\n" + 
				"			<unm>String</unm> <!-- username -->\n" + 
				"			<pwd>String</pwd> <!-- password -->\n" + 
				"			<eer>String</eer> <!-- executionEnvRef -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</swin> <!-- softwareInstall -->\n" + 
				"		<swup>\n" + 
				"			<uuid>String</uuid> <!-- UUID -->\n" + 
				"			<vr>String</vr> <!-- version -->\n" + 
				"			<url>http://www.altova.com/</url> <!-- URL -->\n" + 
				"			<unm>String</unm> <!-- username -->\n" + 
				"			<pwd>String</pwd> <!-- password -->\n" + 
				"			<eer>String</eer> <!-- executionEnvRef -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</swup> <!-- softwareUpdate -->\n" + 
				"		<swun>\n" + 
				"			<uuid>String</uuid> <!-- UUID -->\n" + 
				"			<vr>String</vr> <!-- version -->\n" + 
				"			<eer>String</eer> <!-- executionEnvRef -->\n" + 
				"			<any>\n" + 
				"				<nm>NCName</nm> <!-- name -->\n" + 
				"				<val>text</val> <!-- value -->\n" + 
				"			</any> <!-- anyArg -->\n" + 
				"		</swun> <!-- softwareUninstall -->\n" + 
				"	</exra> <!-- execReqArgs -->\n" + 
				"	-->\n" + 
				"\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	<execInstance>1</execInstance>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"    <ch nm=\"execInstance0001\" typ=\"8\">/execInstance0001</ch>\n" + 
				"</m2m:mgc>";
		
		
		String Node_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:nod xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"nod_0001\">\n" + 
				"	<ty>14</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>3234234292</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>hubiss admin</lbl> <!--labels-->\n" + 
				"	<acpi>3234234001 3234234002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<at>//onem2m.hubiss.com/cse1 //onem2m.hubiss.com/cse2</at> <!--announceTo-->\n" + 
				"	<aa>hostedCSELink</aa> <!--announcedAttribute-->\n" + 
				"	<ni>900001-HERITGW01-GW00001</ni><!--nodeID-->\n" + 
				"	<hcl>/csebase/cse0001</hcl> <!--hostedCSELink-->\n" + 
				"	<!--" + 
				"	<memory>0..1</memory> - mgmtObj\n" + 
				"	<battery>0..n</battery> - mgmtObj\n" + 
				"	<areaNwkInfo>0..n</areaNwkInfo>  - mgmtObj\n" + 
				"	<areaNwkDeviceInfo>0..n</areaNwkDeviceInfo> - mgmtObj\n" + 
				"	<firmware>0..n</firmware> - mgmtObj\n" + 
				"	<software>0..n</software> - mgmtObj\n" + 
				"	<deviceInfo>0..n</deviceInfo> - mgmtObj\n" + 
				"	<deviceCapability>0..n</deviceCapability> - mgmtObj\n" + 
				"	<reboot>0..1</reboot> - mgmtObj\n" + 
				"	<eventLog>0..1</eventLog> - mgmtObj\n" + 
				"	<cmdhPolicy>0..n</cmdhPolicy> - mgmtObj\n" + 
				"	<activeCmdhPolicy>0..n</activeCmdhPolicy> - mgmtObj\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"    <ch nm=\"memory0001\" typ=\"10013\">/memory0001</ch>\n" + 
				"    <ch nm=\"battery0001\" typ=\"10013\">/battery0001</ch>\n" + 
				"    <ch nm=\"areaNwkInfo0001\" typ=\"10013\">/areaNwkInfo0001</ch>\n" + 
				"    <ch nm=\"areaNwkDeviceInfo0001\" typ=\"10013\">/areaNwkDeviceInfo0001</ch>\n" + 
				"    <ch nm=\"firmware0001\" typ=\"10013\">/firmware0001</ch>\n" + 
				"    <ch nm=\"software0001\" typ=\"10013\">/software0001</ch>\n" + 
				"    <ch nm=\"deviceInfo0001\" typ=\"10013\">/deviceInfo0001</ch>\n" + 
				"    <ch nm=\"deviceCapability0001\" typ=\"10013\">/deviceCapability0001</ch>\n" + 
				"    <ch nm=\"reboot0001\" typ=\"10013\">/reboot0001</ch>\n" + 
				"    <ch nm=\"eventLog0001\" typ=\"10013\">/eventLog0001</ch>\n" + 
				"    <ch nm=\"cmdhPolicy0001\" typ=\"10013\">/cmdhPolicy0001</ch>\n" + 
				"    <ch nm=\"activeCmdhPolicy0001\" typ=\"10013\">/activeCmdhPolicy0001</ch>\n" + 
				"</m2m:nod>";
		
		String NodeAnnc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:nodA xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"nodA_0001\">\n" + 
				"	<ty>10014</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>3234234292</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTimelastModifiedTime-->\n" + 
				"	<lt>20150603T122321</lt>\n" + 
				"	<lbl>hubiss admin</lbl> <!--labels-->\n" + 
				"	<acpi>3234234001 3234234002</acpi> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<link>//homeiot.herit.net/csebase/node001</link>\n" + 
				"	<ni>900001-HERITGW01-GW00001</ni><!--nodeID-->\n" + 
				"	<hcl>/csebase/cse0001</hcl> <!--hostedCSELink-->\n" + 
				"	<!--" + 
				"	<memory>0..1</memory> - mgmtObjAnnc\n" + 
				"	<battery>0..n</battery> - mgmtObjAnnc\n" + 
				"	<areaNwkInfo>0..n</areaNwkInfo> - mgmtObjAnnc\n" + 
				"	<areaNwkDeviceInfo>0..n</areaNwkDeviceInfo> - mgmtObjAnnc\n" + 
				"	<firmware>0..n</firmware> - mgmtObjAnnc\n" + 
				"	<software>0..n</software> - mgmtObjAnnc\n" + 
				"	<deviceInfo>0..n</deviceInfo> - mgmtObjAnnc\n" + 
				"	<deviceCapability>0..n</deviceCapability> - mgmtObjAnnc\n" + 
				"	<reboot>0..1</reboot> - mgmtObjAnnc\n" + 
				"	<eventLog>0..1</eventLog> - mgmtObjAnnc\n" + 
				"	<cmdhPolicy>0..n</cmdhPolicy> - mgmtObjAnnc\n" + 
				"	<activeCmdhPolicy>0..n</activeCmdhPolicy> - mgmtObjAnnc\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"    <ch nm=\"memory0001\" typ=\"10013\">/memory0001</ch>\n" + 
				"    <ch nm=\"battery0001\" typ=\"10013\">/battery0001</ch>\n" + 
				"    <ch nm=\"areaNwkInfo0001\" typ=\"10013\">/areaNwkInfo0001</ch>\n" + 
				"    <ch nm=\"areaNwkDeviceInfo0001\" typ=\"10013\">/areaNwkDeviceInfo0001</ch>\n" + 
				"    <ch nm=\"firmware0001\" typ=\"10013\">/firmware0001</ch>\n" + 
				"    <ch nm=\"software0001\" typ=\"10013\">/software0001</ch>\n" + 
				"    <ch nm=\"deviceInfo0001\" typ=\"10013\">/deviceInfo0001</ch>\n" + 
				"    <ch nm=\"deviceCapability0001\" typ=\"10013\">/deviceCapability0001</ch>\n" + 
				"    <ch nm=\"reboot0001\" typ=\"10013\">/reboot0001</ch>\n" + 
				"    <ch nm=\"eventLog0001\" typ=\"10013\">/eventLog0001</ch>\n" + 
				"    <ch nm=\"cmdhPolicy0001\" typ=\"10013\">/cmdhPolicy0001</ch>\n" + 
				"    <ch nm=\"activeCmdhPolicy0001\" typ=\"10013\">/activeCmdhPolicy0001</ch>\n" + 
				"</m2m:nodA>";
		
		String RemoteCSEAnnc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:csrA xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-remoteCSE-v1_0_0.xsd\" rn=\"csrA_0001\">\n" + 
				"	<ty>10016</ty><!--resourceType-->\n" + 
				"	<ri>3234234293</ri><!--resourceID-->\n" + 
				"	<pi>csebase</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>hubiss home app1</lbl><!--labels-->\n" + 
				"	<acpi>3234234001 3234234002</acpi>  <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<link>//homeiot.herit.net/csebase</link>\n" + 
				"	<cst>1</cst>  <!--cseType-->\n" + 
				"	<poa>http://10.101.101.12:8090</poa> <!--pointOfAccess-->\n" + 
				"	<cb>//onem2m.herit.net/gw01/csebase</cb> <!--CSEBase-->\n" + 
				"	<csi>cse000012</csi> <!--CSE-ID-->\n" + 
				"	<rr>true</rr> <!--requestReachability-->\n" + 
				"	<nl>//onem2m.herit.net/csebase/node0001</nl> <!--nodeLink-->\n" + 
				"	<!--" + 
				"	<AE>0..n</AE>\n" + 
				"	<AEAnnc>0..n</AEAnnc>\n" + 
				"	<container>0..n</container>\n" + 
				"	<containerAnnc>0..n</containerAnnc>\n" + 
				"	<group>0..n</group>\n" + 
				"	<groupAnnc>0..n</groupAnnc>\n" + 
				"	<accessControlPolicy>0..n</accessControlPolicy>\n" + 
				"	<accessControlPolicyAnnc>0..n</accessControlPolicyAnnc>\n" + 
				"	<subscription>0..n</subscription>\n" + 
				"	<schedule>0..1</schedule>\n" + 
				"	<pollingChannel>0..1</pollingChannel>\n" + 
				"	<nodeAnnc>0..1</nodeAnnc>\n" + 
				"	<locationPolicyAnnc>0..n</locationPolicyAnnc>\n" + 
				"	-->" + 
				"    <ch nm=\"AE0001\" typ=\"2\">/AE0001</ch>\n" + 
				"    <ch nm=\"AEAnnc0001\" typ=\"10002\">/AEAnnc0001</ch>\n" + 
				"    <ch nm=\"container0001\" typ=\"3\">/container0001</ch>\n" + 
				"    <ch nm=\"containerAnnc0001\" typ=\"10003\">/containerAnnc0001</ch>\n" + 
				"    <ch nm=\"group0001\" typ=\"9\">/group0001</ch>\n" + 
				"    <ch nm=\"groupAnnc0001\" typ=\"10009\">/groupAnnc0001</ch>\n" + 
				"    <ch nm=\"accessControlPolicy0001\" typ=\"1\">/accessControlPolicy0001</ch>\n" + 
				"    <ch nm=\"accessControlPolicyAnnc0001\" typ=\"10001\">/accessControlPolicyAnnc0001</ch>\n" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"    <ch nm=\"schedule0001\" typ=\"18\">/schedule0001</ch>\n" + 
				"    <ch nm=\"pollingChannel0001\" typ=\"15\">/pollingChannel0001</ch>\n" + 
				"    <ch nm=\"nodeAnnc0001\" typ=\"10014\">/nodeAnnc0001</ch>\n" + 
				"    <ch nm=\"locationPolicyAnnc0001\" typ=\"10010\">/locationPolicyAnnc0001</ch>\n" + 
				"</m2m:csrA>";
		
		String Request_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:req xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-request-v1_0_0.xsd\" rn=\"req_0001\">\n" + 
				"	<ty>12</ty><!--resourceType-->\n" + 
				"	<ri>req_000000001</ri><!--resourceID-->\n" + 
				"	<pi>csebase</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<lbl>req01 label</lbl> <!--labels-->\n" + 
				"	<acpi>acp001 acp002</acpi>  <!--accessControlPolicyIDs-->\n" + 
				"	<st>0</st><!--stateTag-->\n" + 
				"	<opn>1</opn><!--operation-->\n" + 
				"	<tg>//iot.herit.net/gw00001</tg><!--target-->\n" + 
				"	<og>//iot.herit.net/ap00001</og><!--originator-->\n" + 
				"	<rid>ap00001_req0000012</rid><!--requestID-->\n" + 
				"	<mi>\n" + 
				"		<!-- request parameters -->\n" + 
				"      <ty>4</ty><!--resourceType-->\n" + 
				"		<nm>String</nm><!--name-->\n" + 
				"		<ot>20150603T122321</ot><!--originatingTimestamp-->\n" + 
				"		<rqet>20150603T122321</rqet><!--requestExpirationTimestamp-->\n" + 
				"		<rset>20150603T122321</rset><!--resultExpirationTimestamp-->\n" + 
				"		<oet>20150603T122321</oet><!--operationalExecutionTime-->\n" + 
				"		<rt><rtv>3</rtv>\n" +
				"			 <nu>http://test.net http://test2.net</nu>\n" +
				"		</rt><!--responseType-->\n" + 
				"		<rp>P42D</rp><!--resultPersistence-->\n" + 
				"		<rcn>1</rcn><!--resultContent-->\n" + 
				"		<ec>eventCategory\n" + 
				"			<!--<ect>3</ect> - eventCatType \n" + 
				"			<ecn>0</ecn> -eventCatNo -->" + 
				"		</ec><!--eventCategory-->\n" + 
				"		<da>true</da><!--deliveryAggregation-->\n" + 
				"		<gid>String</gid><!--groupRequestIdentifier-->\n" + 
				"		<fc>\n" + 
				"			<crb>20150603T122321</crb><!--createdBefore-->\n" + 
				"			<cra>20150603T122321</cra><!--createdAfter-->\n" + 
				"			<ms>20150603T122321</ms><!--modifiedSince-->\n" + 
				"			<us>20150603T122321</us><!--unmodifiedSince-->\n" + 
				"			<sts>2</sts><!--stateTagSmaller-->\n" + 
				"			<stb>0</stb><!--stateTagBigger-->\n" + 
				"			<exb>20150603T122321</exb><!--expireBefore-->\n" + 
				"			<exa>20150603T122321</exa><!--expireAfter-->\n" + 
				"			<lbl>token</lbl><!--labels-->\n" + 
				"			<ty>3</ty><!--resourceType-->\n" + 
				"			<sza>0</sza><!--sizeAbove-->\n" + 
				"			<szb>2</szb><!--sizeBelow-->\n" + 
				"			<cty>String</cty><!--contentType-->\n" + 
				"			<atr>\n" + 
				"				<nm>NCName</nm><!--name-->\n" + 
				"				<val>text</val><!--value-->\n" + 
				"			</atr><!--attribute-->\n" + 
				"			<fu>2</fu><!--filterUsage-->\n" + 
				"			<lim>0</lim><!--limit-->\n" + 
				"		</fc><!--filterCriteria-->\n" + 
				"		<drt>2</drt><!--discoveryResultType-->\n" + 
				"	</mi><!--metaInformation-->\n" + 
				"	<pc>\n" + 
				"		<!-- content parameter of original request -->\n" + 
				"	</pc><!--content-->\n" + 
				"	<rs>4</rs> <!-- completed:1, failed:2, pending:3, forwarded:4 --> <!--requestStatus-->\n" + 
				"	<ol>\n" + 
				"		<!-- response parameters of requset -->\n" + 
				"		<pc>\n" + 
				"			<!-- content parameter of response -->\n" + 
				"		</pc><!--content-->\n" + 
				"		<ec>4</ec><!--eventCategory-->\n" + 
				"		<fr>//target of request</fr><!--from-->\n" + 
				"		<to>//originator of request</to><!--to-->\n" + 
				"		<ot>20150603T122321</ot><!--originatingTimestamp-->\n" + 
				"		<rqi>token</rqi><!--requestIdentifier-->\n" + 
				"		<rset>20150603T122321</rset><!--resultExpirationTimestamp-->\n" + 
				"		<rsc>4005</rsc><!--responseStatusCode-->\n" + 
				"	</ol><!--operationResult-->\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:req>";
		
		
		String Schedule_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:sch xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-schedule-v1_0_0.xsd\" rn=\"sce_0001\">\n" + 
				"	<ty>3</ty><!--resourceType-->\n" + 
				"	<ri>schd_0001</ri><!--resourceID-->\n" + 
				"	<pi>rcse_0001</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<lbl>gw1_schedule</lbl> <!--labels-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<at>//onem2m.hubiss.com/cse1 //onem2m.hubiss.com/cse2</at> <!--announceTo-->\n" + 
				"	<aa>attr_list</aa> <!--announcedAttribute-->\n" + 
				"	<se>\n" + 
				"		<sce>* 0-5 2,6,10 * * *</sce><!--scheduleEntry-->\n" + 
				"		<!-- scheduleEntry 목록 -->\n" + 
				"	</se><!--scheduleElement-->\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:sch>";
		
		String ScheduleAnnc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:schA xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-schedule-v1_0_0.xsd\" rn=\"schA_0001\">\n" + 
				"	<ty>10018</ty><!--resourceType-->\n" + 
				"	<ri>schdannc_0001</ri><!--resourceID-->\n" + 
				"	<pi>rcse_0001</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<!-- <lbl>gw1_schedule</lbl> --> <!--labels-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<link>//homeiot.herit.net/csebase/lp0001/schda_00001</link>\n" + 
				"	<se>\n" + 
				"		<sce>* 0-5 2,6,10 * * *</sce> scheduleEntry	\n" + 
				"	</se> scheduleElement\n" + 
				"</m2m:schA>";
		
		
		String ServiceSubscribedAppRule_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:asar xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-serviceSubscribedAppRule-v1_0_0.xsd\" rn=\"ssapprule1\">\n" + 
				"	<ty>19</ty><!--resourceType-->\n" + 
				"	<ri>ssapprule_0001</ri><!--resourceID-->\n" + 
				"	<pi>casebase</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<!-- <lbl>ssapprule</lbl> --> <!--labels-->\n" + 
				"	<!-- <acpi>acp0001 acp0002</acpi> --> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<apci>C123*X C124*X C125*X</apci><!--applicableCredIDs-->\n" + 
				"	<aai>R111/HomeApp* R111/PublicApp*</aai>  <!--allowedApp-IDs-->\n" + 
				"	<aae>S109*</aae> <!-- allowedAEs -->\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:asar>";
		
		String ServiceSubscribedNode_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:svsn xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-serviceSubscribedNode-v1_0_0.xsd\" rn=\"ssnode1\">\n" + 
				"	<ty>20</ty><!--resourceType-->\n" + 
				"	<ri>ssnode_00001</ri><!--resourceID-->\n" + 
				"	<pi>casebase</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<!-- <lbl>service_subscription_node</lbl> --> <!--labels-->\n" + 
				"	<!-- <acpi>acp0001 acp0002 acp0003</acpi> --> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<ni>FFFFDDDD0EB0</ni><!--nodeID-->\n" + 
				"	<csi>CSE00001</csi> <!-- CSE-ID -->\n" + 
				"	<di>urn:dev:ops:001122-HERITGW1-GW0032432 urn:dev:ops:001122-HERITGW1-GW0032433 urn:dev:ops:001122-HERITGW1-GW0032434</di> <!-- deviceIdentifier -->\n" + 
				"	<ruleLinks>ssapprule_0001 ssapprule_0002 ssapprule_0003</ruleLinks> <!-- When empty, no applications are allowed to register on the CSE indicated by the CSE-ID attribute -->\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:svsn>";
		
		String StatsCollect_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:stcl xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-statsCollect-v1_0_0.xsd\" rn=\"statcol_00001\">\n" + 
				"	<ty>21</ty><!--resourceType-->\n" + 
				"	<ri>statcol_00001</ri><!--resourceID-->\n" + 
				"	<pi>csebase</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<!-- <lbl>stat_collect_1</lbl> --> <!--labels-->\n" + 
				"	<!-- <acpi>acp0001 acp0002</acpi> --> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<cr>incse_herit</cr><!--creator-->\n" + 
				"	<sci>stat_id_00001</sci><!--statsCollectID-->\n" + 
				"	<cei>ae_id_of_accountingsystem</cei><!--collectingEntityID-->\n" + 
				"	<cdi>S190XX7T</cdi><!--collectedEntityID-->\n" + 
				"	<srs>1</srs> <!-- 1:Active, 2:Inactive --> <!--statsRuleStatus-->\n" + 
				"	<sm>1</sm> <!-- 1:eventbased --> <!--statModel-->\n" + 
				"	<cp>\n" + 
				"		<sce>* 0-5 2,6,10 * * *</sce> <!-- scheduleEntry -->\n" + 
				"		<sce>* * 8-20 * * *</sce> <!-- scheduleEntry --> \n" + 
				"	</cp> <!-- collectPeriod -->\n" + 
				"	<evi>eventcfg_id_0001</evi> <!-- eventID -->\n" + 
				"	<!--" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:stcl>";
		
		String StatsConfig_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<!--Sample XML file generated by XMLSpy v2015 rel. 3 sp1 (http://www.altova.com)-->\n" + 
				"<m2m:stcg xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-statsConfig-v1_0_0.xsd\" rn=\"statconfig1\">\n" + 
				"	<ty>22</ty><!--resourceType-->\n" + 
				"	<ri>statconfig_0001</ri><!--resourceID-->\n" + 
				"	<pi>csebase</pi><!--parentID-->\n" + 
				"	<ct>20150603T122321</ct><!--creationTime-->\n" + 
				"	<lt>20150603T122321</lt><!--lastModifiedTime-->\n" + 
				"	<!-- <lbl>stat_config</lbl> --> <!--labels-->\n" + 
				"	<!-- <acpi>acp0001 acp0002</acpi> --> <!--accessControlPolicyIDs-->\n" + 
				"	<et>20150603T122321</et><!--expirationTime-->\n" + 
				"	<cr>incse_id_or_inae_id</cr><!--creator-->\n" + 
				"	<!--" + 
				"	<eventConfig>0..n</eventConfig>\n" + 
				"	<subscription>0..n</subscription>\n" + 
				"	-->" + 
				"    <ch nm=\"eventConfig0001\" typ=\"7\">/eventConfig0001</ch>\n" + 
				"    <ch nm=\"subscription0001\" typ=\"23\">/subscription0001</ch>\n" + 
				"</m2m:stcg>";
		
		String RequestPrimitive_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				" <m2m:rqp xsi:schemaLocation=\"http://www.onem2m.org/xml/protocols CDT-requestPrimitive-v1_6_0.xsd\" xmlns:m2m=\"http://www.onem2m.org/xml/protocols\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" + 
				"   <op>1</op>\n" + 
				"   <to>/herit-cse</to>\n" + 
				"   <fr>S</fr>\n" + 
				"   <rqi>req_222224</rqi>\n" + 
				"   <ty>2</ty>\n" + 
				"   <pc>\n" + 
				"      <m2m:ae>\n" + 
				"         <lbl>home</lbl>\n" + 
				"         <et>20161231T235555</et>\n" + 
				"         <apn>TEST</apn>\n" + 
				"         <api>testApp</api>\n" + 
				"      </m2m:ae>\n" + 
				"   </pc>\n" + 
				"</m2m:rqp> ";
		
		
		try {
			//-----------------------------------------------------------------------------------------
			String xml2;
//			XMLConvertor<AE> XC = new XMLConvertor<AE>(AE.class);
//			AE ae = (AE)XC.unmarshal(AE_xml);
//			//System.out.println("resourceName: " + ae.getResourceName());
//			
//			List<Resource> resources = ae.getContainerOrGroupOrAccessControlPolicy();
//			Container container = new Container();
//			container.setStateTag(BigInteger.TEN);
//			resources.add(container);
//			Group group = new Group();
//			group.setCreator("test");
//			resources.add(group);
//
//			xml2 = XC.marshal(ae, AE.SCHEMA_LOCATION);
//			//System.out.println("xml2: " + xml2);

			
			//-----------------------------------------------------------------------------------------
			// AE, Container, ContentInstance, RemoteCSE, AccessControlPolicy, CSEBase, Group
			// PollingChannel, Subscription, AEAnnc, AccessControlPolicyAnnc, ContainerAnnc
			// ContentInstanceAnnc, Delivery, EventConfig, ExecInstance, GroupAnnc
			// LocationPolicy, LocationPolicyAnnc, M2MServiceSubscriptionProfile, MgmtCmd, MgmtObj(?????), MgmtObjAnnc(??????) 
			// Node, NodeAnnc, RemoteCSEAnnc, Request, Schedule, ScheduleAnnc, ServiceSubscribedAppRule
			// ServiceSubscribedNode, StatsCollect, StatsConfig
			
			String a = "Abced\n"
					+ "<fefed>agag";
			//System.out.println(a.replace("<fefed>", "<AAAA>"));
			
			//System.out.println(RequestPrimitive_xml.indexOf("<m2m:rqp"));
			//System.out.println(RequestPrimitive_xml.indexOf("</m2m:rqp>"));
			if(RequestPrimitive_xml.indexOf("<m2m:rqp") > 0 && RequestPrimitive_xml.indexOf("</m2m:rqp>") > 0) {
				RequestPrimitive_xml = RequestPrimitive_xml.replace("m2m:rqp", "rqp");
				RequestPrimitive_xml = RequestPrimitive_xml.replace("/m2m:rqp", "/rqp");
				//System.out.println(RequestPrimitive_xml);
			}
			
			XMLConvertor<AE> XC2 = 
					new XMLConvertor<AE>(AE.class, AE.SCHEMA_LOCATION);
			AE resource = (AE)XC2.unmarshal(AE_xml);
//			if(resource.getPointOfAccess() == null)
//				//System.out.println("PointOfAccess is null: " + resource.getPointOfAccess());
//			else //System.out.println("PointOfAccess is " + resource.getPointOfAccess());
//			
//			//System.out.println("resourceName: " + resource.getResourceName());
			xml2 = XC2.marshal(resource);
			//System.out.println(xml2);

//			////System.out.println(resource.getChildResource().get(0).getValue());
//			
//			JSONConvertor<AE> JC = new JSONConvertor<AE>(AE.class);
//			String json = JC.marshal(resource);
//			//System.out.println(json);
//	
//			//System.out.println(Double.valueOf((Double)3.1415923332233335));
						
		} catch (Exception e) {
			log.debug("Handled exception", e);
		}
	}
	
}
