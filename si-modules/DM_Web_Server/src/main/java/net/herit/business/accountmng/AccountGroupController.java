package net.herit.business.accountmng;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import net.herit.business.accountmng.service.AccountGroupService;
import net.herit.business.accountmng.service.AccountGroupVO;
import net.herit.common.conf.HeritProperties;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;
import net.herit.common.util.SecurityModule;
import net.herit.common.util.StringUtil;
import net.herit.security.dto.GroupAuthorization;

@Controller
@RequestMapping(value="/admin/accountGroup")
public class AccountGroupController {

	@Resource(name = "AccountGroupService")
	private AccountGroupService accountGroupService;


    @RequestMapping(value="/list.do")
    public String accountGroupList(@ModelAttribute("accountGroupVO") AccountGroupVO accountGroupVO,
    		                   HttpServletRequest request,
    		                   Locale locale,
    		                   ModelMap model)
            throws Exception {

		HttpSession session = request.getSession(false);
		if(session != null){
			//페이지 권한 확인
			GroupAuthorization requestAuth = (GroupAuthorization) session.getAttribute("requestAuth");
			if(!requestAuth.getAuthorizationDBRead().equals("1")){
				model.addAttribute("authMessage", "권한그룹관리 메뉴는 읽기 권한이 없습니다.");
				return "forward:" + HeritProperties.getProperty("Globals.MainPage");
			}
		}


        //조회 페이지의 검색키워드 보안 취약점 보완
		accountGroupVO.setSearchVal(SecurityModule.VulnerabilityChek(StringUtil.nvl(accountGroupVO.getSearchVal()), 1, "common", "xss|sqlinjection"));
		List resultList = accountGroupService.accountGroupList(accountGroupVO);

		/**
		 * 데이터 셋팅
		 */
		model.addAttribute("accountGroupVO", accountGroupVO);
		model.addAttribute("resultList", resultList);

    	return "/v2/admin/accountGroupList";
    }

    @RequestMapping(value="/info.do")
    @ResponseBody
    public Map<String, Object> accountGroupInfo(@ModelAttribute("accountGroupVO") AccountGroupVO accountGroupVO) throws Exception {
    	
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", false);

    	AccountGroupVO resultVO = accountGroupService.accountGroupInfo(accountGroupVO);
    	if (null != resultVO) {
    		dataMap.put("result", true);
    		dataMap.put("data", resultVO);
    	}

    	return dataMap;
    }


    @RequestMapping(value="/insert.do")
    @ResponseBody
    public Map<String, Object> insertAccountGroup(@ModelAttribute("accountGroupVO") AccountGroupVO accountGroupVO) throws Exception {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", false);
    	
		// check groupCode
		if (accountGroupService.isExistGroupCode(accountGroupVO)) {
    		dataMap.put("message", "이미 등록된 그룹 코드입니다!");

    		return dataMap;
		}
		
    	try {
    		ProcessResult result = accountGroupService.insertAccountGroup(accountGroupVO);

    		dataMap.put("result", result);
    		dataMap.put("message", result.getRetMsg());
    		dataMap.put("retCod", result.getRetCod());

    	} catch(BizException ex) {
    		dataMap.put("EX_ERR_OBJ", ex);
    		dataMap.put("message", ex.getErrMsg());
    		dataMap.put("retCod", "1");

    	} catch(UserSysException ex) {
    		dataMap.put("EX_ERR_OBJ", ex);
    		dataMap.put("message", ex.getMessage());
    		dataMap.put("retCod", "1");
    	}

    	return dataMap;
    }

    
    @RequestMapping(value="/update.do")
    @ResponseBody
    public Map<String, Object> updateAccountGroup(@ModelAttribute("accountGroupVO") AccountGroupVO accountGroupVO) throws Exception {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", false);

		try {
    		ProcessResult result = accountGroupService.updateAccountGroup(accountGroupVO);

    		dataMap.put("result", result);
    		dataMap.put("message", result.getRetMsg());
    		dataMap.put("retCod", result.getRetCod());

    	} catch(BizException ex) {
    		dataMap.put("EX_ERR_OBJ", ex);
    		dataMap.put("message", ex.getErrMsg());
    		dataMap.put("retCod", "1");

    	} catch(UserSysException ex) {
    		dataMap.put("EX_ERR_OBJ", ex);
    		dataMap.put("message", ex.getMessage());
    		dataMap.put("retCod", "1");
    	}

    	return dataMap;
    }

    @RequestMapping(value="/delete.do")
    @ResponseBody
    public Map<String, Object> deleteAccountGroup(@ModelAttribute("accountGroupVO") AccountGroupVO accountGroupVO) throws Exception {

		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("result", false);
    	
    	try{
    		ProcessResult result = accountGroupService.deleteAccountGroup(accountGroupVO);

    		dataMap.put("result", result);
    		dataMap.put("message", result.getRetMsg());
    		dataMap.put("retCod", result.getRetCod());

    	} catch(BizException ex) {
    		dataMap.put("EX_ERR_OBJ", ex);
    		dataMap.put("message", ex.getErrMsg());
    		dataMap.put("retCod", "1");

    	} catch(UserSysException ex) {
    		dataMap.put("EX_ERR_OBJ", ex);
    		dataMap.put("message", ex.getMessage());
    		dataMap.put("retCod", "1");
    	}

    	return dataMap;
    }

}
