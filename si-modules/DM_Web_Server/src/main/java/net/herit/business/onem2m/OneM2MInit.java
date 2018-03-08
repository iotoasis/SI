package net.herit.business.onem2m;

import java.lang.reflect.Field;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.herit.business.api.service.OneM2MApiService;
import net.herit.business.onem2m.resource.AE;
import net.herit.common.conf.HeritProperties;


public class OneM2MInit implements ServletContextListener {
	
	// private Logger log = LoggerFactory.getLogger(HubissEmulator.class);
	
	
	// 컨테이너 처음 구동될 때 실행
	@Override
	public void contextInitialized(ServletContextEvent init) {
		
		////System.out.println("initialized!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		try {
			/*
			 * <Context docBase="HIT_DM_ADMIN_2014" path="/hdm"
			 * reloadable="true"
			 * source="org.eclipse.jst.jee.server:HIT_DM_ADMIN_2014" /> <!--
			 * <Context docBase="HIT_DM_ADMIN_2014" path="/mobile"
			 * reloadable="true"
			 * source="org.eclipse.jst.jee.server:HIT_DM_ADMIN_2014" /> -->
			 */
			//Logger log = LoggerFactory.getLogger("oneM2MInit");
			String servletContextPath = init.getServletContext().getContextPath();
			//log.debug("ServletContextPath=[{}]", servletContextPath);

			if ("/hdm".equals(servletContextPath))
				aeInit();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 초기화
	//@SuppressWarnings("unused")
	public void aeInit() throws Exception {
		
		if (HeritProperties.getProperty("Globals.UseOneM2M").equalsIgnoreCase("true")) {
			//System.out.println("OneM2MInit.aeInit called!");
			OneM2MApiService oneM2MSvc = OneM2MApiService.getInstance();
			oneM2MSvc.init();	
		} else {  
			//System.out.println("OneM2MInit.aeInit ignored!");
		}
		
	}
	
	// 컨테이너 종료될 때 실행
	@Override
	public void contextDestroyed(ServletContextEvent dest) {

	}
}
