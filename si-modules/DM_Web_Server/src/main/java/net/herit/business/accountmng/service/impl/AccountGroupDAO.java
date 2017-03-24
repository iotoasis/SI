package net.herit.business.accountmng.service.impl;

import java.util.List;

import net.herit.business.accountmng.service.AccountGroupVO;
import net.herit.business.accountmng.service.MenuMasterVO;
import net.herit.common.dataaccess.HeritHdpAbstractDAO;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.ProcessResult;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("AccountGroupDAO")
public class AccountGroupDAO extends HeritHdpAbstractDAO{
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";



    public List<AccountGroupVO> accountGroupList(AccountGroupVO vo) throws UserSysException {
    	METHOD_NAME = "accountGroupList";
    	List resultList = null;

    	try {
    		resultList = list("AccountGroupDAO.accountGroupList", vo);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "권한그룹 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}
    	return resultList;
    }

    public AccountGroupVO accountGroupInfo(AccountGroupVO vo) throws UserSysException {
    	METHOD_NAME = "accountGroupInfo";
    	AccountGroupVO resultVO = null;

    	try {
    		resultVO = (AccountGroupVO)selectByPk("AccountGroupDAO.accountGroupInfo", vo);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "권한그룹 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}

    	return resultVO;
    }

    public ProcessResult insertAccountGroup(AccountGroupVO vo) throws BizException, UserSysException {
    	METHOD_NAME = "insertAccountGroup";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;


			value = insert("AccountGroupDAO.insertAccountGroup", vo);

    		if(value == null){
    			retCode = 0;
    		}
    		if(retCode<0){
    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "권한그룹 등록시 에러가 발생했습니다.");
    		}else{
    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
    		}

		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "권한그룹 등록시 에러가 발생했습니다.", ex);
    	}
//		catch (Exception e) {
//			// TODO: handle exception
//    		System.out.println("e.getMessage() = " + e.getMessage());
//    		System.out.println("e.getCause() = " + e.getCause());
//    		System.out.println("e = " + e);
//
//    		throw new BizException (CLASS_NAME, METHOD_NAME, retCode, "권한그룹 등록/수정시 에러가 발생했습니다.");
//		}

		return result;
    }

    public ProcessResult updateAccountGroup(AccountGroupVO vo) throws BizException, UserSysException {
    	METHOD_NAME = "updateAccountGroup";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;
			value = update("AccountGroupDAO.updateAccountGroup", vo);
			if(Integer.parseInt(value.toString()) == 1){
    			retCode = 0;
    		}
    		if(retCode<0){
    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "권한그룹 수정시 에러가 발생했습니다.");
    		}else{
    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 수정 처리되었습니다.");
    		}



		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "권한그룹 수정시 에러가 발생했습니다.", ex);
    	}

		return result;
    }

    public ProcessResult deleteAccountGroup(AccountGroupVO vo) throws BizException, UserSysException {
    	METHOD_NAME = "deleteAccountGroup";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;

			String[] checkList = vo.getCheckList();
	    	for (int i = 0 ; i < checkList.length ; i++) {

	    		vo.setId(checkList[i]);



	    		int count = (Integer) getSqlMapClientTemplate().queryForObject("AccountGroupDAO.checkMngAccount", vo);
	    		if(count > 0){

	    	    	AccountGroupVO resultVO = (AccountGroupVO)selectByPk("AccountGroupDAO.accountGroupInfo", vo);
	    			//throw new BizException(CLASS_NAME, METHOD_NAME, retCode, (i+1)+"번째 선택한 권한그룹에 속한 사용자가 있어 삭제가 불가능합니다.");
	    	    	throw new BizException(CLASS_NAME, METHOD_NAME, retCode, resultVO.getGroupName()+"("+resultVO.getGroupCode()+")에 속한 사용자가 있어 삭제가 불가능합니다.");
	    		}


	    		value = delete("AccountGroupDAO.deleteAccountGroupMenuList", vo);
/*	    		
				if(Integer.parseInt(value.toString()) > 0){
	    			retCode = 0;
	    		}
	    		if(retCode<0){
	    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "권한그룹설정 삭제시 에러가 발생했습니다.");
	    		}else{
	    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 삭제 처리되었습니다.");
	    		}
*/
	    		value = delete("AccountGroupDAO.deleteAccountGroup", vo);
				if(Integer.parseInt(value.toString()) > 0){
	    			retCode = 0;
	    		}
	    		if(retCode<0){
	    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "권한그룹 삭제시 에러가 발생했습니다.");
	    		}else{
	    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 삭제 처리되었습니다.");
	    		}


	    	}

		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "권한그룹 삭제시 에러가 발생했습니다.", ex);
    	}

		return result;
    }


	public boolean isExistGroupCode(AccountGroupVO vo) throws UserSysException{
    	METHOD_NAME = "isExistGroupCode";

		int count = 0;
		try {
			count = (Integer) getSqlMapClientTemplate().queryForObject("AccountGroupDAO.isExistGroupCode", vo);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "권한그룹코드 중복체크 도중 에러가 발생했습니다.", ex);
    	}

		return count > 0;
	}




    public List<MenuMasterVO> accountGroupInitMenuList(MenuMasterVO vo) throws UserSysException {
    	METHOD_NAME = "accountGroupInitMenuList";
    	List resultList = null;

    	try {
    		resultList = list("AccountGroupDAO.accountGroupInitMenuList", vo);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "메뉴마스터 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}
    	return resultList;
    }

    public List<AccountGroupVO> accountGroupSelectMenuList(AccountGroupVO vo) throws UserSysException {
    	METHOD_NAME = "accountGroupSelectMenuList";
    	List resultList = null;

    	try {
    		resultList = list("AccountGroupDAO.accountGroupSelectMenuList", vo);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "메뉴마스터 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}
    	return resultList;
    }

}
