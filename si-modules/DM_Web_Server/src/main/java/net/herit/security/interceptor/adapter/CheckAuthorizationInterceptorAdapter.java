package net.herit.security.interceptor.adapter;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.herit.security.dto.Account;
import net.herit.security.dto.URIResource;
import net.herit.security.service.SecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class CheckAuthorizationInterceptorAdapter extends HandlerInterceptorAdapter {
	
    protected static final Logger LOGGER = LoggerFactory.getLogger(CheckAuthorizationInterceptorAdapter.class);
	
	@Resource(name = "SecurityService")
	private SecurityService securityService;
	
	//return 이 false 일 경우 해당 controller 로 요청이 진행되지 않음.
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		try {

			//webapp의 루트 패스
			String contextPath = request.getContextPath();
			//요청이 들어온 URI
			String originRequestURI = request.getRequestURI();
			String requestURI = originRequestURI.replaceAll(contextPath, "");
			
			// v2 디자인 작업 편의를 위한 임시 코드 - 로그인 없이 페이지 접근 가능 
			//if (requestURI.indexOf("v2") != -1) {
			//	return true;
			//}
			
			if (requestURI.indexOf("/lwm2m") > -1 || requestURI.indexOf("/tr069") > -1) {
				return true;
			}
			
			//세션 체크를 위하여 세션을 꺼냄
//			HttpSession session = request.getSession(false);
			
			List<URIResource> uriResourceList = securityService.getURIResourceList();
			
			if(securityService.compareRequestURIAndURIResources(requestURI, uriResourceList)){
				return true;
			}else{
				//세션 체크를 위하여 존재하는 세션을 가져옴
				HttpSession session = request.getSession(false);
				//사용자 인증이 완료되지 않은 요청일 경우
				if(session == null){
					response.sendRedirect(contextPath + "/security/authenticate.do");
					return false;
				
				/*
				 * @author 헤리트 웹서비스플래폼 임효섭
				 * 로그아웃을 진행할 경우 session.invalidate() 를 호출한다.. 
				 * 그러나 invalidate() 호출 이 후 session 내부 정보와 jsessionId 는 파기되나 해당 session 객체는 바로 gc 되지 않는다.
				 * 로그아웃 후 재로그인 시 해당 세션 객체는 살아 있으므로 세션 객체 내에 사용자의 정보가 있는지 한 번 더 체크하는 아래와 같은 로직이 필요하다.
				 * (더 간결하고 우수한 로직이 있을 경우 아래 로직을 수정할 필요가 있음.)
				 */
				//사용자 인증이 완료된 요청(session이 사용자 정보를 저장하고 있음)이거나 혹은 로그아웃 후 로그인 시도 일 경우(저장된 사용자 정보가 없음)
				}else{
					Account loginAccount = (Account) session.getAttribute("account");
					if(loginAccount != null){
						securityService.setAuthorizationForRequest(session, requestURI);
						return true;
					}
					response.sendRedirect(contextPath + "/security/authenticate.do");
					return false;
				}
			}
		} catch (Exception ex) {
			
			// db가 연결되지 않는 경우 예외를 반환하지 않고 true를 반환하도록 함.
			return true;
		}
		
	}
	
}
