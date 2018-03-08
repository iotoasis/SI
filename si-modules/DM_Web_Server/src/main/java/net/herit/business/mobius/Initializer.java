package net.herit.business.mobius;

import java.lang.reflect.Field;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.json.JSONObject;

import net.herit.business.mobius.converter.JSONConverter;
import net.herit.business.onem2m.resource.AE;



public class Initializer implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		// ae 존재 체크
		//System.out.println("DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD");
		// ae 생성
		
		AE ae = new AE();
		JSONConverter jcvt = new JSONConverter(ae);
		JSONObject resourceJson = jcvt.convert();
		//System.out.println(resourceJson);
		
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		
		
	}

	
}
