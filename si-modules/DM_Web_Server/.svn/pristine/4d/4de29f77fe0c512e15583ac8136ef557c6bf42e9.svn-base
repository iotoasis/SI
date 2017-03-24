package net.herit.business.env;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.herit.common.util.SecurityModule;
import net.herit.business.accountmng.service.AccountService;
import net.herit.business.accountmng.service.AccountVO;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;
import net.herit.security.dto.Account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/env/userInfo")
public class UserInfoController {

	@Resource(name = "AccountService")
	private AccountService accountService;


    @RequestMapping(value="/info.do")
    public String accountInfo(@ModelAttribute("accountVO") AccountVO accountVO,
		    		                   HttpServletRequest request,
		    		                   Locale locale,
		    		                   ModelMap model)
            throws Exception {
    	String viewName = "";

		HttpSession session = request.getSession(false);
		if(session != null){
			Account loginAccount = (Account) session.getAttribute("account");
	    	accountVO.setLoginId(loginAccount.getLoginId());
		}
        List mngAccountGroupIdList = accountService.getMngAccountGroupIdList();
    	AccountVO resultVO = accountService.accountInfo(accountVO);
    	List ipLimitResultList = accountService.ipLimitList(accountVO);

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("mngAccountGroupIdList", mngAccountGroupIdList);
		model.addAttribute("accountVO", resultVO);
		model.addAttribute("ipLimitResultList", ipLimitResultList);


    	if(accountVO.getActionType().equals("") || accountVO.getActionType().equals("V")){
    		viewName = "/v2/env/userInfo";
    	}else if(accountVO.getActionType().equals("U")){
    		viewName = "/v2/env/userInfoUpdate";
    	}else if(accountVO.getActionType().equals("IP_LIMIT_I")){
    		viewName = "/herit/business/env/ipLimitInsertPop";
    	}else{
    		viewName = "redirect:/env/userInfo/info.do";
    	}

		
    	return viewName;
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


    	return "/v2/env/userInfoUpdate";
    }

    @RequestMapping(value="/login.do")
    public String login(@ModelAttribute("accountVO") AccountVO accountVO,
		    		                   HttpServletRequest request,
		    		                   Locale locale,
		    		                   ModelMap model)
            throws Exception {

		model.addAttribute("accountVO", accountVO);
		model.addAttribute("error_txt", "");

    	return "/herit/business/env/userLoginPop";
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
