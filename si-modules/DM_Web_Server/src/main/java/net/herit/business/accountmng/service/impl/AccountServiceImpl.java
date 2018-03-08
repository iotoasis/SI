package net.herit.business.accountmng.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.herit.business.accountmng.service.AccountService;
import net.herit.business.accountmng.service.AccountVO;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.CodeVO;
import net.herit.common.model.ProcessResult;
import net.herit.common.util.PagingUtil;
import net.herit.common.util.SecurityUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service("AccountService")
public class AccountServiceImpl implements AccountService{

    protected static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class);

	@Resource(name = "AccountDAO")
	private AccountDAO accountDAO;
    @Override
    public List<CodeVO> getMngAccountGroupIdList() throws UserSysException {
    	List<CodeVO> resultList = accountDAO.getMngAccountGroupIdList();
    	return resultList;
    }

    @Override
    public PagingUtil accountPagingList(int nPage, AccountVO vo) throws UserSysException {

    	vo.setPageStartIndex(PagingUtil.getPaingStartIndex(nPage, ""));
    	vo.setPageSize(PagingUtil.getPageSize(""));

    	List resultList = accountDAO.accountPagingList(vo);

    	int nTotal = (Integer)resultList.get(0);
    	List list = (List)resultList.get(1);
    	PagingUtil pageTable = null;
    	if(nTotal>0){
    		pageTable = new PagingUtil("", nTotal, nPage, vo);
    		pageTable.setCurrList(list);
    	}

    	return pageTable;
    }


    @Override
    public AccountVO accountInfo(AccountVO vo) throws UserSysException {
    	AccountVO resultVO = accountDAO.accountInfo(vo);
    	return resultVO;
    }

    @Override
    public ProcessResult insertAccount(AccountVO vo) throws Exception, BizException, UserSysException {

		//패스워드 암호화
		String pass = SecurityUtil.encryptWordpass(vo.getLoginPwd());
		vo.setLoginPwd(pass);
    	ProcessResult result = accountDAO.insertAccount(vo);
    	return result;
    }

    @Override
    public ProcessResult updateAccount(AccountVO vo) throws Exception, BizException, UserSysException {

    	if(vo.getChangeYn().equals("Y")){
        	//패스워드 암호화
    		String pass = SecurityUtil.encryptWordpass(vo.getLoginPwd());
    		vo.setLoginPwd(pass);
    	}else{
    		vo.setLoginPwd(null);
    	}
    	ProcessResult result = accountDAO.updateAccount(vo);
    	return result;
    }

    @Override
    public ProcessResult deleteAccount(AccountVO vo) throws BizException, UserSysException {
    	ProcessResult result = accountDAO.deleteAccount(vo);
    	return result;
    }

    @Override
    public boolean isExistId(AccountVO vo) throws UserSysException {
    	boolean result = accountDAO.isExistId(vo);
    	return result;
    }


    @Override
    public List<AccountVO> ipLimitList(AccountVO vo) throws UserSysException {
    	List<AccountVO> resultList = accountDAO.ipLimitList(vo);
    	return resultList;
    }

    @Override
    public AccountVO ipLimitInfo(AccountVO vo) throws UserSysException {
    	AccountVO resultVO = accountDAO.ipLimitInfo(vo);
    	return resultVO;
    }

    @Override
    public ProcessResult insertIpLimit(AccountVO vo) throws BizException, UserSysException {
    	ProcessResult result = accountDAO.insertIpLimit(vo);
    	return result;
    }

    @Override
    public ProcessResult updateIpLimit(AccountVO vo) throws BizException, UserSysException {
    	ProcessResult result = accountDAO.updateIpLimit(vo);
    	return result;
    }

    @Override
    public ProcessResult deleteIpLimit(AccountVO vo) throws BizException, UserSysException {
    	ProcessResult result = accountDAO.deleteIpLimit(vo);
    	return result;
    }


}
