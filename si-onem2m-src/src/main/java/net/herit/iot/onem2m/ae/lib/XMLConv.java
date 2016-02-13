package net.herit.iot.onem2m.ae.lib;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.xml.sax.InputSource;

import net.herit.iot.onem2m.ae.lib.dto.LGUAuthData;

public class XMLConv<T> {

	private final Class<T> t;
	private JAXBContext context;
	private Unmarshaller um;
	private Marshaller m;
	
//	private final static String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

	public XMLConv(Class<T> type) {
		this.t = type;
		
		try {
			context = JAXBContext.newInstance(t);
			if(context == null) {
				throw new Exception("JAXBContext newInstance failed");
			}
			um = context.createUnmarshaller();
		
			m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_ENCODING, "utf-8");
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	@SuppressWarnings("unchecked")
	public T unmarshal(String xml) throws Exception {

		InputSource ins = new InputSource(new StringReader(xml));
		
		T obj = null;
		
		if(um == null) System.out.println("um is null");
		
		obj = (T)um.unmarshal(ins);

		return obj;
	}

	public String marshal(T obj) {

		String xml = null;
		Writer writer = new StringWriter();
		
		if(m == null) System.out.println("m is null");
		
		try {
			m.marshal(obj, writer);
			 xml = writer.toString();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
		return xml;
	}

	public static void main(String[] args) {
	
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" + 
				"<authdata>\n" + 
				"    <http>\n" + 
				"        <entityId>entityId</entityId>\n" + 
				"        <enrmtKey>key</enrmtKey>\n" + 
				"        <token>abcd-efa-teafdvadasdf</token>\n" + 
				"    </http>\n" + 
				"    <coap>\n" + 
				"        <entityId>entityId</entityId>\n" + 
				"        <enrmtKey>key</enrmtKey>\n" + 
				"        <token>abcd-efa-teafdvadasdf</token>\n" + 
				"        <encryptionMethod>tls_psk_aes_128_ccm_8</encryptionMethod>\n" + 
				"    </coap>\n" + 
				"    <mqtt>\n" + 
				"        <entityId>entityId</entityId>\n" + 
				"        <enrmtKey>key</enrmtKey>\n" + 
				"        <token>abcd-efa-teafdvadasdf</token>\n" + 
				"        <certUrl>http://xxx.xxx.xxx./mef/cert/xxxxxxxx</certUrl>\n" + 
				"    </mqtt>\n" + 
				"</authdata>";
		
		
		try {
			//-----------------------------------------------------------------------------------------

			XMLConv<LGUAuthData> XC2 = new XMLConv<LGUAuthData>(LGUAuthData.class);
			LGUAuthData resource = (LGUAuthData)XC2.unmarshal(xml);
			
			System.out.println(resource);
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
