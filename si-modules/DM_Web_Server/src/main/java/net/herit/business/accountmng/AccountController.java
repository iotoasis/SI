package net.herit.business.accountmng;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.herit.business.accountmng.service.AccountService;
import net.herit.business.accountmng.service.AccountVO;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;
import net.herit.common.util.PagingUtil;
import net.herit.common.util.StringUtil;
import net.herit.security.dto.GroupAuthorization;
import net.herit.common.util.SecurityModule;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(value="/admin/account")
public class AccountController {

	@Resource(name = "AccountService")
	private AccountService accountService;

    @RequestMapping(value="/list.do")
    public String accountList(@ModelAttribute("accountVO") AccountVO accountVO,
    		                   HttpServletRequest request,
    		                   Locale locale,
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


        //조회 페이지의 검색키워드 보안 취약점 보완
        String currPage = SecurityModule.VulnerabilityChek(StringUtil.nvl(request.getParameter("currPage"), "1"), 1, "common", "xss|sqlinjection");
        int nPage = Integer.parseInt(currPage);
        if (nPage < 1) {
            nPage = 1;
        }


        //조회 페이지의 검색키워드 보안 취약점 보완
        accountVO.setSearchVal(SecurityModule.VulnerabilityChek(StringUtil.nvl(accountVO.getSearchVal()), 1, "common", "xss|sqlinjection"));
        PagingUtil resultPagingUtil = accountService.accountPagingList(nPage, accountVO);
        List mngAccountGroupIdList = accountService.getMngAccountGroupIdList();

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("accountVO", accountVO);
		model.addAttribute("currPage", currPage);
		model.addAttribute("mngAccountGroupIdList", mngAccountGroupIdList);
		model.addAttribute("resultPagingUtil", resultPagingUtil);

    	return "/herit/business/admin/accountList";
    }

    @RequestMapping(value="/info.do")
    public String accountInfo(@ModelAttribute("accountVO") AccountVO accountVO,
		    		                   HttpServletRequest request,
		    		                   Locale locale,
		    		                   ModelMap model)
            throws Exception {
    	String viewName = "";

        List mngAccountGroupIdList = accountService.getMngAccountGroupIdList();
    	AccountVO resultVO = accountService.accountInfo(accountVO);
    	List ipLimitResultList = accountService.ipLimitList(accountVO);

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("mngAccountGroupIdList", mngAccountGroupIdList);
		model.addAttribute("accountVO", resultVO);
		model.addAttribute("ipLimitResultList", ipLimitResultList);

    	if(accountVO.getActionType().equals("V")){
    		viewName = "/herit/business/admin/accountInfo";
    	}else if(accountVO.getActionType().equals("I")){
    		viewName = "/herit/business/admin/accountInsert";
    	}else if(accountVO.getActionType().equals("U")){
    		viewName = "/herit/business/admin/accountUpdate";
    	}else{
    		viewName = "redirect:/admin/account/list.do";
    	}

    	return viewName;
    }


    @RequestMapping(value="/insert.do")
    public String insertAccount(@ModelAttribute("accountVO") AccountVO accountVO,
		    		                   HttpServletRequest request,
		    		                   Locale locale,
		    		                   ModelMap model)
            throws Exception {

    	//파라미터에 대한 보안 모듈 적용
    	accountVO = setSecuritVO(accountVO);

		model.addAttribute("accountVO", accountVO);

		Pattern p = Pattern.compile("([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])");
		Matcher m = p.matcher(accountVO.getLoginPwd());

		if(accountVO.getLoginPwd().matches(".*"+accountVO.getLoginId()+".*")){
	        List mngAccountGroupIdList = accountService.getMngAccountGroupIdList();
	    	List ipLimitResultList = accountService.ipLimitList(accountVO);
			model.addAttribute("mngAccountGroupIdList", mngAccountGroupIdList);
			model.addAttribute("ipLimitResultList", ipLimitResultList);

    		model.addAttribute("EX_ERR_OBJ", "");
    		model.addAttribute("message", "비밀번호에 아이디를 포함할 수 없습니다.");
    		model.addAttribute("retCod", "1");
		}else{
			if (m.find()){
		    	try{
		    		ProcessResult result = accountService.insertAccount(accountVO);

		    		model.addAttribute("result", result);
		    		model.addAttribute("message", result.getRetMsg());
		    		model.addAttribute("retCod", result.getRetCod());

		    	}catch(BizException ex){
		    		model.addAttribute("EX_ERR_OBJ", ex);
		    		model.addAttribute("message", ex.getErrMsg());
		    		model.addAttribute("retCod", "1");

		    	}catch(UserSysException ex){
		    		model.addAttribute("EX_ERR_OBJ", ex);
		    		model.addAttribute("message", ex.getMessage());
		    		model.addAttribute("retCod", "1");
		    	}
			}else{
		        List mngAccountGroupIdList = accountService.getMngAccountGroupIdList();
		    	List ipLimitResultList = accountService.ipLimitList(accountVO);
				model.addAttribute("mngAccountGroupIdList", mngAccountGroupIdList);
				model.addAttribute("ipLimitResultList", ipLimitResultList);

	    		model.addAttribute("EX_ERR_OBJ", "");
	    		model.addAttribute("message", "비밀번호를 알파벳과 숫자, 특수문자를 조합하여 입력하세요.");
	    		model.addAttribute("retCod", "1");
			}
		}




    	return "/herit/business/admin/accountInsert";
    }

    @RequestMapping(value="/update.do")
    public String updateAccount(@ModelAttribute("accountVO") AccountVO accountVO,
		    		                   HttpServletRequest request,
		    		                   Locale locale,
		    		                   ModelMap model)
            throws Exception {


    	//파라미터에 대한 보안 모듈 적용
    	accountVO = setSecuritVO(accountVO);

		model.addAttribute("accountVO", accountVO);

		Pattern p = Pattern.compile("([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])");
		Matcher m = p.matcher(accountVO.getLoginPwd());


		if(accountVO.getLoginPwd().matches(".*"+accountVO.getLoginId()+".*")){
	        List mngAccountGroupIdList = accountService.getMngAccountGroupIdList();
	    	List ipLimitResultList = accountService.ipLimitList(accountVO);
			model.addAttribute("mngAccountGroupIdList", mngAccountGroupIdList);
			model.addAttribute("ipLimitResultList", ipLimitResultList);

    		model.addAttribute("EX_ERR_OBJ", "");
    		model.addAttribute("message", "비밀번호에 아이디를 포함할 수 없습니다.");
    		model.addAttribute("retCod", "1");
		}else{
			if (m.find()){
				try{
		    		ProcessResult result = accountService.updateAccount(accountVO);

		    		model.addAttribute("result", result);
		    		model.addAttribute("message", result.getRetMsg());
		    		model.addAttribute("retCod", result.getRetCod());

		    	}catch(BizException ex){

		            List mngAccountGroupIdList = accountService.getMngAccountGroupIdList();
		        	AccountVO resultVO = accountService.accountInfo(accountVO);
		        	List ipLimitResultList = accountService.ipLimitList(accountVO);

		    		model.addAttribute("mngAccountGroupIdList", mngAccountGroupIdList);
		    		model.addAttribute("accountVO", resultVO);
		    		model.addAttribute("ipLimitResultList", ipLimitResultList);

		    		model.addAttribute("EX_ERR_OBJ", ex);
		    		model.addAttribute("message", ex.getErrMsg());
		    		model.addAttribute("retCod", "1");

		    	}catch(UserSysException ex){
		    		model.addAttribute("EX_ERR_OBJ", ex);
		    		model.addAttribute("message", ex.getMessage());
		    		model.addAttribute("retCod", "1");
		    	}
			}else{
		        List mngAccountGroupIdList = accountService.getMngAccountGroupIdList();
		    	List ipLimitResultList = accountService.ipLimitList(accountVO);
				model.addAttribute("mngAccountGroupIdList", mngAccountGroupIdList);
				model.addAttribute("ipLimitResultList", ipLimitResultList);

	    		model.addAttribute("EX_ERR_OBJ", "");
	    		model.addAttribute("message", "비밀번호를 알파벳과 숫자, 특수문자를 조합하여 입력하세요.");
	    		model.addAttribute("retCod", "1");
			}
		}




    	return "/herit/business/admin/accountUpdate";
    }

    @RequestMapping(value="/delete.do")
    public String deleteAccount(@ModelAttribute("accountVO") AccountVO accountVO,
		    		                   HttpServletRequest request,
		    		                   Locale locale,
		    		                   ModelMap model)
            throws Exception {


		model.addAttribute("accountVO", accountVO);

    	try{
    		ProcessResult result = accountService.deleteAccount(accountVO);

    		model.addAttribute("result", result);
    		model.addAttribute("message", result.getRetMsg());
    		model.addAttribute("retCod", result.getRetCod());

    	}catch(BizException ex){
    		model.addAttribute("EX_ERR_OBJ", ex);
    		model.addAttribute("message", ex.getErrMsg());
    		model.addAttribute("retCod", "1");

    	}catch(UserSysException ex){
    		model.addAttribute("EX_ERR_OBJ", ex);
    		model.addAttribute("message", ex.getMessage());
    		model.addAttribute("retCod", "1");
    	}


        //조회 페이지의 검색키워드 보안 취약점 보완
        String currPage = SecurityModule.VulnerabilityChek(StringUtil.nvl(request.getParameter("currPage"), "1"), 1, "common", "xss|sqlinjection");
        int nPage = Integer.parseInt(currPage);
        if (nPage < 1) {
            nPage = 1;
        }

        PagingUtil resultPagingUtil = accountService.accountPagingList(nPage, accountVO);

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("accountVO", accountVO);
		model.addAttribute("currPage", currPage);
		model.addAttribute("resultPagingUtil", resultPagingUtil);

    	return "/herit/business/admin/accountList";

    }


    @RequestMapping(value="/isExistId.do")
	@ResponseBody
	public boolean isExistId(@ModelAttribute("accountVO") AccountVO accountVO,
											HttpServletRequest request,
											@RequestBody String loginId)
				throws UserSysException{


		boolean checkFlagValue = accountService.isExistId(accountVO);

		return checkFlagValue;
	}

    @RequestMapping(value="/isExistId_temp.do")
	@ResponseBody
	public Map<String, Object> isExistId_temp(@ModelAttribute("accountVO") AccountVO accountVO,
											HttpServletRequest request,
											@RequestBody String loginId)
				throws UserSysException{


		boolean checkFlagValue = accountService.isExistId(accountVO);

		Map<String, Object> response = new HashMap<String, Object>();
		response.put("data", checkFlagValue);
		response.put("methodName", "checkExistId");
		response.put("status", "OK");
		response.put("code", "0");
		if (checkFlagValue) {
			response.put("message", "사용할 수 있는 로그인ID입니다.");
			response.put("checkIdCode", "0");
		} else {
			response.put("message", "사용할 수 없는 로그인ID입니다.");
			response.put("checkIdCode", "1");
		}
		return response;
	}



    public AccountVO setSecuritVO(AccountVO accountVO){

    	//파라미터에 대한 보안 모듈 적용
    	accountVO.setMngAccountGroupId(SecurityModule.VulnerabilityChek(accountVO.getMngAccountGroupId(), 1, "common", "xss|sqlinjection"));
    	accountVO.setLoginId(SecurityModule.VulnerabilityChek(accountVO.getLoginId(), 1, "common", "xss|sqlinjection"));
    	accountVO.setName(SecurityModule.VulnerabilityChek(accountVO.getName(), 1, "common", "xss|sqlinjection"));
    	accountVO.setDepartment(SecurityModule.VulnerabilityChek(accountVO.getDepartment(), 1, "common", "xss|sqlinjection"));
    	accountVO.setEmail(SecurityModule.VulnerabilityChek(accountVO.getEmail(), 1, "common", "xss|sqlinjection"));
    	accountVO.setPhone(SecurityModule.VulnerabilityChek(accountVO.getPhone(), 1, "common", "xss|sqlinjection"));
    	accountVO.setLoginId(SecurityModule.VulnerabilityChek(accountVO.getLoginId(), 1, "common", "xss|sqlinjection"));
    	accountVO.setMobile(SecurityModule.VulnerabilityChek(accountVO.getMobile(), 1, "common", "xss|sqlinjection"));
    	accountVO.setDisabled(SecurityModule.VulnerabilityChek(accountVO.getDisabled(), 1, "common", "xss|sqlinjection"));

    	return accountVO;
    }

}
