package net.herit.business.accountmng.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.herit.business.accountmng.service.AccountGroupService;
import net.herit.business.accountmng.service.AccountGroupVO;
import net.herit.business.accountmng.service.AccountVO;
import net.herit.business.accountmng.service.MenuMasterVO;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("AccountGroupService")
public class AccountGroupServiceImpl implements AccountGroupService{

    protected static final Logger LOGGER = LoggerFactory.getLogger(AccountGroupServiceImpl.class);
	
	@Resource(name = "AccountGroupDAO")
	private AccountGroupDAO accountGroupDAO;
	

	
    @Override
    public List<AccountGroupVO> accountGroupList(AccountGroupVO vo) throws UserSysException {
    	List<AccountGroupVO> resultList = accountGroupDAO.accountGroupList(vo);    	
    	return resultList;
    }
    
    @Override
    public AccountGroupVO accountGroupInfo(AccountGroupVO vo) throws UserSysException {
    	AccountGroupVO resultVO = accountGroupDAO.accountGroupInfo(vo);    	
    	return resultVO;
    }
    
    @Override
    public ProcessResult insertAccountGroup(AccountGroupVO vo) throws BizException, UserSysException {
    	ProcessResult result = accountGroupDAO.insertAccountGroup(vo);
    	return result;
    }
    
    @Override
    public ProcessResult updateAccountGroup(AccountGroupVO vo) throws BizException, UserSysException {
    	ProcessResult result = accountGroupDAO.updateAccountGroup(vo);
    	return result;
    }
    
    @Override
    public ProcessResult deleteAccountGroup(AccountGroupVO vo) throws BizException, UserSysException {
    	ProcessResult result = accountGroupDAO.deleteAccountGroup(vo);
    	return result;
    }
    

    @Override
    public boolean isExistGroupCode(AccountGroupVO vo) throws UserSysException {    	
    	boolean result = accountGroupDAO.isExistGroupCode(vo);    	
    	return result;
    }    
    
    @Override
    public List<MenuMasterVO> accountGroupInitMenuList(MenuMasterVO vo) throws UserSysException {
    	List<MenuMasterVO> resultList = accountGroupDAO.accountGroupInitMenuList(vo);    	
    	return resultList;
    }
    
    @Override
    public List<AccountGroupVO> accountGroupSelectMenuList(AccountGroupVO vo) throws UserSysException {
    	List<AccountGroupVO> resultList = accountGroupDAO.accountGroupSelectMenuList(vo);    	
    	return resultList;
    }
    
    

}
