package net.herit.security.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;
import net.herit.security.dto.Account;
import net.herit.security.dto.GroupAuthorization;
import net.herit.security.dto.URIResource;
import net.herit.security.service.SecurityService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("SecurityService")
public class SecurityServiceImpl implements SecurityService {

	protected static final Logger LOGGER = LoggerFactory.getLogger(SecurityServiceImpl.class);

	@Resource(name = "SecurityDAO")
	private SecurityDAO securityDAO;

	@Override
	public Account authenticate(Account account) throws Exception {

    	// 1. 입력한 비밀번호를 암호화한다.
//    	String enpassword = SecurityUtil.encryptPassword(account.getLoginPassword());
//    	account.setLoginPassword(enpassword);

		Account authenticatedAccount = securityDAO.getAccount(account);
		return authenticatedAccount;
	}

	@Override
	public List<GroupAuthorization> getGroupAuthorizationList(int groupId) {
		List<GroupAuthorization> groupAuthorizationList = securityDAO.getGroupAuthorizationList(groupId);
		return groupAuthorizationList;
	}

	@Override
	public List<URIResource> getURIResourceList() {
		List<URIResource> uriResourceList = securityDAO.getURIResourceList();
		return uriResourceList;
	}

	@Override
	public boolean compareRequestURIAndURIResources(String requestURI, List<URIResource> uriResourceList){
		for(URIResource uriResource : uriResourceList){
			String uri = uriResource.getUri();
			if(requestURI.equals(uri)){
				return true;
			}else{
				//정규식 표현을 통해 패턴 매치를 진행하는 것이 정석이나, ** 하나만 제거하는 기능이므로 replaceAll 과 contains를 사용하여 해결한다.
				if(uri.contains("**")){
					uri = uri.replaceAll("\\*\\*", "");
					if(requestURI.contains(uri)){
						return true;
					}
				}
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setAuthorizationForRequest(HttpSession session, String requestURI) {
		List<GroupAuthorization> groupAuthorizationList
			= (List<GroupAuthorization>) session.getAttribute("groupAuthorizationList");
		for(GroupAuthorization groupAuthorization : groupAuthorizationList){
			String resource = groupAuthorization.getResource();
//			if(resource.equals(requestURI)){
//				session.setAttribute("requestAuth", groupAuthorization);
//			}
			if(resource.contains("**")){
				resource = resource.replaceAll("\\*\\*", "");
				if(requestURI.contains(resource)){
					session.setAttribute("requestAuth", groupAuthorization);
				}
			}

		}
	}


	@Override
	public int getIpLimitCount(Account account) {
		int ipLimitCount = securityDAO.getIpLimitCount(account);
		return ipLimitCount;
	}

	@Override
	public int isAvailableIpLimitCheck(Account account) {
		int isAvailableIpLimitCheck = securityDAO.isAvailableIpLimitCheck(account);
		return isAvailableIpLimitCheck;
	}

    @Override
    public ProcessResult updateLoginFailCount(Account vo) throws BizException, UserSysException {
    	ProcessResult result = securityDAO.updateLoginFailCount(vo);
    	return result;
    }

    @Override
    public ProcessResult updateLoginFailCountReset(Account vo) throws BizException, UserSysException {
    	ProcessResult result = securityDAO.updateLoginFailCountReset(vo);
    	return result;
    }
}
