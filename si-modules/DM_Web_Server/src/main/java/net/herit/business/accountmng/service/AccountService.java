package net.herit.business.accountmng.service;

import java.util.List;

import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.CodeVO;
import net.herit.common.model.ProcessResult;
import net.herit.common.util.PagingUtil;



public interface AccountService {

	public List<CodeVO> getMngAccountGroupIdList() throws UserSysException;

	public PagingUtil accountPagingList(int nPage, AccountVO vo) throws UserSysException;

	public AccountVO accountInfo(AccountVO vo) throws UserSysException;

	public ProcessResult insertAccount(AccountVO vo) throws Exception, BizException, UserSysException;

	public ProcessResult updateAccount(AccountVO vo) throws Exception, BizException, UserSysException;

	public ProcessResult deleteAccount(AccountVO vo) throws BizException, UserSysException;

	public boolean isExistId(AccountVO vo) throws UserSysException;

	public List<AccountVO> ipLimitList(AccountVO vo) throws UserSysException;

	public AccountVO ipLimitInfo(AccountVO vo) throws UserSysException;

	public ProcessResult insertIpLimit(AccountVO vo) throws BizException, UserSysException;

	public ProcessResult updateIpLimit(AccountVO vo) throws BizException, UserSysException;

	public ProcessResult deleteIpLimit(AccountVO vo) throws BizException, UserSysException;

}
