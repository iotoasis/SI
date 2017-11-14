package net.herit.business.accountmng.service;

import java.util.List;

import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;



public interface AccountGroupService {	

	public List<AccountGroupVO> accountGroupList(AccountGroupVO vo) throws UserSysException;
	
	public AccountGroupVO accountGroupInfo(AccountGroupVO vo) throws UserSysException;    

	public ProcessResult insertAccountGroup(AccountGroupVO vo) throws BizException, UserSysException;
	
	public ProcessResult updateAccountGroup(AccountGroupVO vo) throws BizException, UserSysException;
	
	public ProcessResult deleteAccountGroup(AccountGroupVO vo) throws BizException, UserSysException;

	public boolean isExistGroupCode(AccountGroupVO vo) throws UserSysException;    
	
	public List<MenuMasterVO> accountGroupInitMenuList(MenuMasterVO vo) throws UserSysException;

	public List<AccountGroupVO> accountGroupSelectMenuList(AccountGroupVO vo) throws UserSysException;
}
