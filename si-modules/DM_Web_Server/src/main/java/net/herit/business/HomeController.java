package net.herit.business;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.herit.common.conf.HeritProperties;
import net.herit.security.dto.GroupAuthorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}

	@RequestMapping(value = "/{menu}/{page}")
	public String commonController(@PathVariable("menu") String menu,
			@PathVariable("page") String page, HttpServletRequest request,
			ModelMap model)
            throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}

    	return "/herit/business/"+menu+"/"+page;
    }


	@RequestMapping(value = "/v2/{menu}/{page}")
	public String commonControllerV2(@PathVariable("menu") String menu,
			@PathVariable("page") String page, HttpServletRequest request,
			ModelMap model)
            throws Exception {
		System.out.println("commonControllerV2");
		
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}
		
		model.addAttribute("menu", menu);
		model.addAttribute("page", page);

    	return "/v2/"+menu+"/"+page;
    }

	@RequestMapping(value = "/v2/{page}")
	public String loginFormV2(@PathVariable("page") String page, HttpServletRequest request,
			ModelMap model)
            throws Exception {
		System.out.println("loginFormV2");
		HttpSession session = request.getSession(false);
		String contextPath = request.getContextPath();
		
		if (page.equalsIgnoreCase("login")) {

			if(session != null && contextPath.equals("/hdm")) {
				//페이지 권한 확인
				GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
				if(requestAuth != null) {
					return "redirect:" + HeritProperties.getProperty("Globals.MainPage") +".do";
				} 
			} 
			return "/v2/login";
			
		} else {
			return "/v2/"+page;			
		}		
    }
	
	/*@RequestMapping(value = "/v2/{page}")
	public String loginFormV2(@PathVariable("page") String page, HttpServletRequest request,
			ModelMap model)
            throws Exception {
		System.out.println("loginFormV2");
		HttpSession session = request.getSession(false);
		String contextPath = request.getContextPath();
		
		if (page.equalsIgnoreCase("login")) {

			if(session != null && contextPath.equals("/hdm")) {
				//페이지 권한 확인
				GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
				if(requestAuth != null) {
					return "redirect:" + HeritProperties.getProperty("Globals.MainPage") +".do";
				} 
			} else if(session != null && contextPath.equals("/mobile")) {
				GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
				if(requestAuth != null) {
					return "redirect:" + HeritProperties.getProperty("Globals.mMainPage") +".do";
				} 
			}
			if (contextPath.equals("/hdm")) {
				return "/v2/login";
			}
			return "/v2/mLogin";
		} else {
			return "/v2/"+page;			
		}		
    }*/
	
	@RequestMapping(value = "/mobile/{page}")
	public String mobileLogin(@PathVariable("page") String page, 
							  HttpServletRequest request,
							  ModelMap model)
            throws Exception {
		System.out.println("mobileLogin");
		HttpSession session = request.getSession(false);
		String contextPath = request.getContextPath();
		
		if (page.equalsIgnoreCase("mLogin")) {

			if(session != null && contextPath.equals("/mobile")) {
				GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
				if(requestAuth != null) {
					return "redirect:" + HeritProperties.getProperty("Globals.mMainPage") +".do";
				} 
			}
			return "/mobile/mLogin";
		} else {
			return "/mobile/"+page;			
		}		
    }
	
	@RequestMapping(value = "/moblie/{menu}/{page}")
	public String mobileController(@PathVariable("menu") String menu,
									 @PathVariable("page") String page, 
									 HttpServletRequest request, 
									 ModelMap model)
            throws Exception {
		System.out.println("mobileController");
		HttpSession session = request.getSession(false);
		
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "사용자관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.mMainPage");
			}
		}
		
		model.addAttribute("menu", menu);
		model.addAttribute("page", page);

    	return "/mobile/"+menu+"/"+page;
    }
	
}
