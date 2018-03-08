package net.herit.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.herit.common.conf.HeritProperties;
import net.herit.security.dto.Account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping(value="/index.do")
	public String goIndexView(HttpServletRequest request, ModelMap modelMap) {
		//System.out.println("/index.do : first");
		
		String returnView = null;
		String indexPageView = HeritProperties.getProperty("Globals.MainPage");
		//String mIndexPageView = HeritProperties.getProperty("Globals.mMainPage");
		String loginPageView = HeritProperties.getProperty("Globals.LoginPage");
		String mLoginPageView = HeritProperties.getProperty("Globals.mLoginPage");
		String mIndexPageView = HeritProperties.getProperty("Globals.mFirstPage");
		String GContextPath = HeritProperties.getProperty("Globals.contextPath");

		//본 컨트롤러에 url 요청이 들어왔을 경우 세션 체크를 통해 dispatch 할 index View(index.jsp 혹은 login.jsp) 를 결정한다.
		HttpSession session = request.getSession(false);
		String contextPath = request.getContextPath();
		//System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>> contextPath " + contextPath);
	
		if (session != null) {
			Account loginAccount = (Account) session.getAttribute("account");
			if (loginAccount != null && contextPath.equals(GContextPath)) {
				returnView = indexPageView;
			} else/* if (loginAccount != null && contextPath.equals("/mobile")) */{
				returnView = mIndexPageView;
			} /*else if (contextPath.equals("/hdm")){
				returnView = loginPageView;
			} else if (contextPath.equals("/mobile")) {
				returnView = mLoginPageView;
			}*/
		} else {
			returnView = loginPageView;
		}


		//System.out.println("/index.do : returnView = " + returnView);
		modelMap.addAttribute("error_txt", "");
		return "redirect:" + returnView +".do";
	}
}