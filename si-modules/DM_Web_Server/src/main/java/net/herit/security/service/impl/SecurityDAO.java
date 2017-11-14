package net.herit.security.service.impl;

import java.util.List;

import net.herit.common.dataaccess.HeritHdpAbstractDAO;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;
import net.herit.security.dto.Account;
import net.herit.security.dto.GroupAuthorization;
import net.herit.security.dto.URIResource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("SecurityDAO")
public class SecurityDAO extends HeritHdpAbstractDAO{
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";

    protected static final Log LOG = LogFactory.getLog(SecurityDAO.class);

    public Account getAccount(Account account) throws Exception {
    	Account authenticatedAccount = (Account) getSqlMapClientTemplate().queryForObject("SecurityDAO.getAccount", account);
    	return authenticatedAccount;
    }

    @SuppressWarnings("unchecked")
	public List<GroupAuthorization> getGroupAuthorizationList(int accountGroupId){
    	List<GroupAuthorization> groupAuthorizationList
    		= getSqlMapClientTemplate().queryForList("SecurityDAO.getGroupAuthorizationList", accountGroupId);
    	return groupAuthorizationList;
    }

    public List<URIResource> getURIResourceList(){
    	List<URIResource> uriResourceList = getSqlMapClientTemplate().queryForList("SecurityDAO.getURIResourceList");
    	return uriResourceList;
    }


    public int getIpLimitCount(Account account){
    	int ipLimitCount = (Integer) getSqlMapClientTemplate().queryForObject("SecurityDAO.getIpLimitCount", account);
    	return ipLimitCount;
    }
    public int isAvailableIpLimitCheck(Account account){
    	int isAvailableIpLimitCheck = (Integer) getSqlMapClientTemplate().queryForObject("SecurityDAO.isAvailableIpLimitCheck", account);
    	return isAvailableIpLimitCheck;
    }

    public ProcessResult updateLoginFailCount(Account vo) throws BizException, UserSysException {
    	METHOD_NAME = "updateLoginFailCount";
    	ProcessResult result = null;
    	int retCode = -1;

		try {
			Object value = null;
			value = update("SecurityDAO.updateLoginFailCount", vo);
			if(Integer.parseInt(value.toString()) == 1){
    			retCode = 0;
    		}
    		if(retCode<0){
    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "로그인 실패 횟수 수정시 에러가 발생했습니다.");
    		}else{
    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 수정 처리되었습니다.");
    		}
		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "로그인 실패 횟수 수정시 에러가 발생했습니다.", ex);
    	}
		return result;
    }
    public ProcessResult updateLoginFailCountReset(Account vo) throws BizException, UserSysException {
    	METHOD_NAME = "updateLoginFailCountReset";
    	ProcessResult result = null;
    	int retCode = -1;

		try {
			Object value = null;
			value = update("SecurityDAO.updateLoginFailCountReset", vo);
			if(Integer.parseInt(value.toString()) == 1){
    			retCode = 0;
    		}
    		if(retCode<0){
    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "로그인 실패 횟수 초기화 수정시 에러가 발생했습니다.");
    		}else{
    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 수정 처리되었습니다.");
    		}
		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "로그인 실패 횟수 초기화 수정시 에러가 발생했습니다.", ex);
    	}
		return result;
    }

}
