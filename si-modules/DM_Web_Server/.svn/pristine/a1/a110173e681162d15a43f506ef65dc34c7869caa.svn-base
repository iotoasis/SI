package net.herit.security.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;
import net.herit.security.dto.Account;
import net.herit.security.dto.GroupAuthorization;
import net.herit.security.dto.URIResource;

public interface SecurityService {
	public Account authenticate(Account account) throws Exception;
	public List<GroupAuthorization> getGroupAuthorizationList(int groupId);
	public List<URIResource> getURIResourceList();
	public boolean compareRequestURIAndURIResources(String requestURI, List<URIResource> uriResourceList);
	public void setAuthorizationForRequest(HttpSession session, String requestURI);
	public int getIpLimitCount(Account account) throws Exception;
	public int isAvailableIpLimitCheck(Account account) throws Exception;

	public ProcessResult updateLoginFailCount(Account vo) throws Exception, BizException, UserSysException;
	public ProcessResult updateLoginFailCountReset(Account vo) throws Exception, BizException, UserSysException;
}
