package net.herit.security.web;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.herit.common.conf.HeritProperties;
import net.herit.common.model.ProcessResult;
import net.herit.common.util.SecurityUtil;
import net.herit.common.util.StringUtil;
import net.herit.security.dto.Account;
import net.herit.security.dto.GroupAuthorization;
import net.herit.security.service.SecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("security")
public class SecurityController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(SecurityController.class);

	@Resource(name = "SecurityService")
	private SecurityService securityService;

	// @RequestMapping(value="/authenticate.do", method=RequestMethod.POST)
	@RequestMapping(value = "/authenticate.do")
	public String authenticate(HttpServletRequest request,
			@ModelAttribute Account account, ModelMap modelMap)
			throws Exception {

		//String indexControllerURI = "redirect:/v2/monitor/index.do";
		//String loginPageViewName = "/herit/common/authentication/login";
		String GContextPath = HeritProperties.getProperty("Globals.contextPath");
		
		String contextPath = request.getContextPath();
		////////////////////////////System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>contextPath " + contextPath);
		String indexControllerURI = null;
		String loginPageViewName = null;
		String returnView = null;
		
		if (contextPath.equals(GContextPath)) {
			indexControllerURI = "redirect:"+HeritProperties.getProperty("Globals.MainPage")+".do";
			loginPageViewName = "redirect:"+HeritProperties.getProperty("Globals.LoginPage")+".do";
		} else if (contextPath.equals("/mobile")){
			//indexControllerURI = "redirect:/herit/business/mobile/device/detail.do";
			indexControllerURI = "redirect:"+HeritProperties.getProperty("Globals.mFirstPage")+".do";
			loginPageViewName = HeritProperties.getProperty("Globals.mLoginPage");
		}
		
		

		// 사용자정보 클릭 시 재인증을 위한 로직
		String actionType = StringUtil.nvl(request.getParameter("actionType"));
		if (actionType.equals("LOGIN_POP")) {
			indexControllerURI = "/herit/business/env/userLoginPop";
			loginPageViewName = "/herit/business/env/userLoginPop";
		}

		String superAdminUserId = HeritProperties.getProperty("Globals.SuperAdminUserId");
		String loginId = account.getLoginId();
		// 1. 입력한 비밀번호를 암호화한다.
		String enwordpass = SecurityUtil.encryptWordpass(account.getLoginWordpass());
		
		Account loginAccount = securityService.authenticate(account);

		if (loginId != null) {

			if (loginAccount != null) {

				String clientIp = request.getRemoteAddr();
				//////////////////////////////System.out.println("clientIp = " + clientIp);
				// remote IP 셋팅
				account.setIp(clientIp);

				// 접속 허용 IP 목록이 0이면 모든 IP 접속 가능. (기능 삭제함)
				// int ipLimitCount = securityService.getIpLimitCount(account);

				// 로그인 실패 횟수가 많을 경우
				if (loginAccount.getFailCount() == Integer.parseInt(HeritProperties.getProperty("Globals.LoginFailCount"))) {
					modelMap.addAttribute("error_txt", "로그인이 5회 실패하여 접속이 불가능합니다.<br>관리자에게 문의하여 주십시오.");
					returnView = loginPageViewName;
				} else {

					// SUPER ADMIN LOGIN ID일 경우는 접속 IP 상관 없이 접속 가능함
					if (superAdminUserId.equals(loginId)) {

						if (!loginAccount.getDisabled().equals("1")) {
							modelMap.addAttribute("error_txt", "사용할수 없는 계정입니다.<br>관리자에게 문의하여 주십시오.");
							returnView = loginPageViewName;
						} else if (!loginAccount.wordpassMatches(enwordpass)) {
							ProcessResult result = securityService.updateLoginFailCount(account);
							modelMap.addAttribute("error_txt", "비밀번호가 올바르지 않습니다. 로그인 실패 : " + (loginAccount.getFailCount() + 1) + "회");
							returnView = loginPageViewName;
						} else {
							// 로그인 성공
							ProcessResult result = securityService.updateLoginFailCountReset(account);
							modelMap.addAttribute("result", result);
							modelMap.addAttribute("retCod", "0");
							modelMap.addAttribute("error_txt", "");
							loginAccount.setLoginSuccessYN("Y");
							// remote IP 셋팅
							account.setIp(clientIp);
							int groupId = loginAccount.getGroupId();
							List<GroupAuthorization> groupAuthorizationList = securityService.getGroupAuthorizationList(groupId);
							HttpSession session = checkSessionValid(request);

							// //////////////////////////////////////////////////////////////////////
							long logonTime = session.getCreationTime(); // 세션이 만들어진 시간
							long lastAccessTime = session.getLastAccessedTime(); // 최종접속시간

							// 날짜 타입으로 변환합니다.
							SimpleDateFormat sFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

							Date logonTimeF = new Date(logonTime);
							Date lastAccessTimeF = new Date(lastAccessTime);

							String logonTimeStr = sFormat.format(logonTimeF);
							String lastAccessTimeStr = sFormat.format(lastAccessTimeF);
							// //////////////////////////////////////////////////////////////////////

							// 클라이언트 상에서 해당 객체의 값을 사용하고 싶은 경우 account 가 key 가
							// 된다. ex) loginAccount.loginId
							session.setAttribute("account", loginAccount);
							session.setAttribute("groupAuthorizationList", groupAuthorizationList);
							returnView = indexControllerURI;
						}

					} else {

						int isAvailableIpLimitCheck = securityService.isAvailableIpLimitCheck(account);
						isAvailableIpLimitCheck = 1;	// IP 체크기능 비활성화 by 이인석 2015/1/20
						if (isAvailableIpLimitCheck > 0) {
							if (!loginAccount.getDisabled().equals("1")) {
								modelMap.addAttribute("error_txt", "사용할수 없는 계정입니다.<br>관리자에게 문의하여 주십시오.");
								returnView = loginPageViewName;
							} else if (!loginAccount.wordpassMatches(enwordpass)) {
								ProcessResult result = securityService.updateLoginFailCount(account);
								modelMap.addAttribute("error_txt", "비밀번호가 올바르지 않습니다. 로그인 실패 : " + (loginAccount.getFailCount() + 1) + "회");
								returnView = loginPageViewName;
							} else {
								// 로그인 성공
								ProcessResult result = securityService.updateLoginFailCountReset(account);
								modelMap.addAttribute("result", result);
								modelMap.addAttribute("retCod", "0");
								modelMap.addAttribute("error_txt", "");
								loginAccount.setLoginSuccessYN("Y");
								int groupId = loginAccount.getGroupId();
								List<GroupAuthorization> groupAuthorizationList = securityService.getGroupAuthorizationList(groupId);
								HttpSession session = checkSessionValid(request);

								// //////////////////////////////////////////////////////////////////////
								long logonTime = session.getCreationTime(); // 세션이 만들어진 시간
								long lastAccessTime = session.getLastAccessedTime(); // 최종접속시간

								// 날짜 타입으로 변환합니다.
								SimpleDateFormat sFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

								Date logonTimeF = new Date(logonTime);
								Date lastAccessTimeF = new Date(lastAccessTime);

								String logonTimeStr = sFormat.format(logonTimeF);
								String lastAccessTimeStr = sFormat.format(lastAccessTimeF);
								// //////////////////////////////////////////////////////////////////////

								// 클라이언트 상에서 해당 객체의 값을 사용하고 싶은 경우 account 가 key
								// 가 된다. ex) loginAccount.loginId
								session.setAttribute("account", loginAccount);
								session.setAttribute("groupAuthorizationList", groupAuthorizationList);
								returnView = indexControllerURI;
							}

						} else {
							modelMap.addAttribute("error_txt", "접속이 허용되지 않은 IP입니다.<br>관리자에게 문의하여 주십시오.");
							returnView = loginPageViewName;
						}

					} // SUPER ADMIN LOGIN ID일 경우는 접속 IP 상관 없이 접속 가능함 end

				} // 로그인 실패 횟수 end

			} else {
				//modelMap.addAttribute("error_txt", "아이디가 존재하지 않습니다.");
				modelMap.addAttribute("error_txt", "");
				returnView = loginPageViewName;
			}
		} else {
			modelMap.addAttribute("error_txt", "");
			returnView = loginPageViewName;
		}

		/*
		 * if(loginId != null){ Account loginAccount =
		 * securityService.authenticate(account); //인증 성공 if(loginAccount !=
		 * null){ int groupId = loginAccount.getGroupId();
		 * List<GroupAuthorization> groupAuthorizationList =
		 * securityService.getGroupAuthorizationList(groupId);
		 *
		 * HttpSession session = checkSessionValid(request); //클라이언트 상에서 해당 객체의
		 * 값을 사용하고 싶은 경우 account 가 key 가 된다. ex) loginAccount.loginId
		 * session.setAttribute("account", loginAccount);
		 * session.setAttribute("groupAuthorizationList",
		 * groupAuthorizationList); returnView = indexControllerURI; //인증 실패
		 * }else{ modelMap.addAttribute("error_txt", "로그인에 실패했습니다."); returnView
		 * = loginPageViewName; } }else{ returnView = loginPageViewName; }
		 */

		////////////////////////////System.out.println("authenticate.do : returnView = " + returnView);

		return returnView;
	}
	
	@RequestMapping(value = "/logout.do")
	public String logout(HttpServletRequest request, ModelMap model)
			throws Exception {
		String contextPath = request.getContextPath();
		String loginPageViewName = null;
		
		if (contextPath.equals("/hdm")) {
			loginPageViewName = HeritProperties.getProperty("Globals.LoginPage")+".do";
		} else if (contextPath.equals("/mobile")) {
			loginPageViewName = HeritProperties.getProperty("Globals.mLoginPage")+".do";
		}
		

		HttpSession validSession = request.getSession(false);
		if (validSession != null) {
			// 세션을 파기한다.
			validSession.invalidate();
		}
		model.addAttribute("error_txt", "");
		return "redirect:"+loginPageViewName;
	}

	/**
	 * 기존 세션이 존재하는지 검사하고 세션이 존재할 경우 해당 세션을 파기한 후 새로운 세션을 리턴한다. 세션이 없는 경우에는 새로운
	 * 세션을 리턴한다.
	 *
	 * @param request
	 * @return session
	 */
	private HttpSession checkSessionValid(HttpServletRequest request) {
		HttpSession validSession = request.getSession(false);
		if (validSession != null) {
			// 기존 세션 파기
			validSession.invalidate();
		}
		HttpSession newSession = request.getSession();
		return newSession;
	}

}
