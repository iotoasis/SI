package net.herit.business.accountmng.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.herit.business.accountmng.service.AccountVO;
import net.herit.common.dataaccess.HeritHdpAbstractDAO;
import net.herit.common.exception.BizException;
import net.herit.common.exception.UserSysException;
import net.herit.common.model.CodeVO;
import net.herit.common.model.ProcessResult;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("AccountDAO")
public class AccountDAO extends HeritHdpAbstractDAO{
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";


    public List<CodeVO> getMngAccountGroupIdList() throws UserSysException {
    	METHOD_NAME = "getMngAccountGroupIdList";
    	List resultList = null;

		HashMap<String, String> whereQuery = new HashMap<String, String>();

    	try {
    		resultList = list("AccountDAO.getMngAccountGroupIdList", whereQuery);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "권한그룹코드 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}
    	return resultList;
    }

    public List<AccountVO> accountPagingList(AccountVO vo) throws UserSysException {
    	METHOD_NAME = "accountPagingList";

    	List resultPagingList = new ArrayList();
    	List resultList = null;

    	try {
    		resultList = list("AccountDAO.accountPagingList", vo);

       		int nTotal = 0;
       		if (resultList != null) {
       			nTotal = (Integer) getSqlMapClientTemplate().queryForObject("AccountDAO.accountPagingListSize", vo);
       		}
       		resultPagingList.add(nTotal);
       		resultPagingList.add(resultList);

    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}
    	return resultPagingList;
    }



    public AccountVO accountInfo(AccountVO vo) throws UserSysException {
    	METHOD_NAME = "accountInfo";
    	AccountVO resultVO = null;

    	try {
    		resultVO = (AccountVO)selectByPk("AccountDAO.accountInfo", vo);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}

    	return resultVO;
    }

    public ProcessResult insertAccount(AccountVO vo) throws Exception, BizException, UserSysException {
    	METHOD_NAME = "insertAccount";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;

			//사용자정보 등록
			value = insert("AccountDAO.insertAccount", vo);
    		if(value == null){
    			retCode = 0;
    		}
    		if(retCode<0){
    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "사용자관리 등록시 에러가 발생했습니다.");
    		}else{
    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
    		}

    		//접속허용IP 등록
			String[] ipArrayList = vo.getIpArrayList();
	    	for (int i = 0 ; i < ipArrayList.length ; i++) {

	    		if(!ipArrayList[i].equals("")){
		    		//IP 정보 셋팅
		    		vo.setIp(ipArrayList[i]);

					value = insert("AccountDAO.insertIpLimit", vo);

		    		if(value == null){
		    			retCode = 0;
		    		}
		    		if(retCode<0){
		    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "접속IP 등록시 에러가 발생했습니다.");
		    		}else{
		    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
		    		}
	    		}
	    	}


		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "사용자관리 등록시 에러가 발생했습니다.", ex);
    	}
//		catch (Exception e) {
//			// TODO: handle exception
//    		//System.out.println("e.getMessage() = " + e.getMessage());
//    		//System.out.println("e.getCause() = " + e.getCause());
//    		//System.out.println("e = " + e);
//
//    		throw new BizException (CLASS_NAME, METHOD_NAME, retCode, "사용자관리 등록/수정시 에러가 발생했습니다.");
//		}

		return result;
    }

    public ProcessResult updateAccount(AccountVO vo) throws Exception, BizException, UserSysException {
    	METHOD_NAME = "updateAccount";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;


			//이전에 설정한 비밀번호로 수정 금지
    		String agoPwd = (String)getSqlMapClientTemplate().queryForObject("AccountDAO.accountAgoPwdInfo", vo);

    		if(agoPwd.equals(vo.getLoginPwd())){

    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "이전에 사용했던 비밀번호는 다시 사용하실 수 없습니다.");

    		}else{

    	    	if(!vo.getChangeYn().equals("Y")){
    	    		vo.setLoginPwd(agoPwd);
    	    	}

    			//사용자정보 수정
    			value = update("AccountDAO.updateAccount", vo);
    			if(Integer.parseInt(value.toString()) == 1){
        			retCode = 0;
        		}
        		if(retCode<0){
        			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "사용자관리 수정시 에러가 발생했습니다.");
        		}else{
        			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 수정 처리되었습니다.");
        		}

        		//접속허용IP 목록삭제
        		int ipLimitCount = (Integer) getSqlMapClientTemplate().queryForObject("AccountDAO.getIpLimitCount", vo);
        		if(ipLimitCount > 0){
    	    		//접속 허용 IP 삭제
    	    		value = delete("AccountDAO.deleteAllIpLimit", vo);
    				if(Integer.parseInt(value.toString()) > 0){
    	    			retCode = 0;
    	    		}
    	    		if(retCode<0){
    	    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "사용자관리 삭제시 에러가 발생했습니다.");
    	    		}else{
    	    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 삭제 처리되었습니다.");
    	    		}
        		}

        		//접속허용IP 등록
    			String[] ipArrayList = vo.getIpArrayList();
    	    	for (int i = 0 ; ipArrayList != null && i < ipArrayList.length ; i++) {

    	    		if(!ipArrayList[i].equals("")){
    		    		//IP 정보 셋팅
    		    		vo.setIp(ipArrayList[i]);

    					value = insert("AccountDAO.insertIpLimit", vo);

    		    		if(value == null){
    		    			retCode = 0;
    		    		}
    		    		if(retCode<0){
    		    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "접속IP 등록시 에러가 발생했습니다.");
    		    		}else{
    		    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
    		    		}
    	    		}
    	    	}

    		}




		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "사용자관리 수정시 에러가 발생했습니다.", ex);
    	}

		return result;
    }

    public ProcessResult deleteAccount(AccountVO vo) throws BizException, UserSysException {
    	METHOD_NAME = "deleteAccount";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;

			String[] checkList = vo.getCheckList();
	    	for (int i = 0 ; i < checkList.length ; i++) {

	    		vo.setLoginId(checkList[i]);


	    		int ipLimitCount = (Integer) getSqlMapClientTemplate().queryForObject("AccountDAO.getIpLimitCount", vo);
	    		if(ipLimitCount > 0){
		    		//접속 허용 IP 삭제
		    		value = delete("AccountDAO.deleteAllIpLimit", vo);
					if(Integer.parseInt(value.toString()) > 0){
		    			retCode = 0;
		    		}
		    		if(retCode<0){
		    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "사용자관리 삭제시 에러가 발생했습니다.");
		    		}else{
		    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 삭제 처리되었습니다.");
		    		}

	    		}

	    		//사용자정보 삭제
	    		value = delete("AccountDAO.deleteAccount", vo);
				if(Integer.parseInt(value.toString()) > 0){
	    			retCode = 0;
	    		}
	    		if(retCode<0){
	    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "사용자관리 삭제시 에러가 발생했습니다.");
	    		}else{
	    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 삭제 처리되었습니다.");
	    		}

	    	}

		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "사용자관리 삭제시 에러가 발생했습니다.", ex);
    	}

		return result;
    }


	public boolean isExistId(AccountVO vo) throws UserSysException{
    	METHOD_NAME = "isExistId";

		int count = 0;
		try {
			count = (Integer) getSqlMapClientTemplate().queryForObject("AccountDAO.isExistId", vo);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "관리자 아이디 중복체크 도중 에러가 발생했습니다.", ex);
    	}

		return count <= 0;
	}


    public List<AccountVO> ipLimitList(AccountVO vo) throws UserSysException {
    	METHOD_NAME = "ipLimitList";
    	List resultList = null;

    	try {
    		resultList = list("AccountDAO.ipLimitList", vo);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "접속IP 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}
    	return resultList;
    }

    public AccountVO ipLimitInfo(AccountVO vo) throws UserSysException {
    	METHOD_NAME = "ipLimitInfo";
    	AccountVO resultVO = null;

    	try {
    		resultVO = (AccountVO)selectByPk("AccountDAO.ipLimitInfo", vo);
    	} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "접속IP 데이터 취득 처리에서 에러가 발생했습니다.", ex);
    	}

    	return resultVO;
    }

    public ProcessResult insertIpLimit(AccountVO vo) throws BizException, UserSysException {
    	METHOD_NAME = "insertIpLimit";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;


			value = insert("AccountDAO.insertIpLimit", vo);

    		if(value == null){
    			retCode = 0;
    		}
    		if(retCode<0){
    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "접속IP 등록시 에러가 발생했습니다.");
    		}else{
    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 등록 처리되었습니다.");
    		}

		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "접속IP 등록시 에러가 발생했습니다.", ex);
    	}
//		catch (Exception e) {
//			// TODO: handle exception
//    		//System.out.println("e.getMessage() = " + e.getMessage());
//    		//System.out.println("e.getCause() = " + e.getCause());
//    		//System.out.println("e = " + e);
//
//    		throw new BizException (CLASS_NAME, METHOD_NAME, retCode, "접속IP 등록/수정시 에러가 발생했습니다.");
//		}

		return result;
    }

    public ProcessResult updateIpLimit(AccountVO vo) throws BizException, UserSysException {
    	METHOD_NAME = "updateIpLimit";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;
			value = update("AccountDAO.updateIpLimit", vo);
			if(Integer.parseInt(value.toString()) == 1){
    			retCode = 0;
    		}
    		if(retCode<0){
    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "접속IP 수정시 에러가 발생했습니다.");
    		}else{
    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 수정 처리되었습니다.");
    		}



		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "접속IP 수정시 에러가 발생했습니다.", ex);
    	}

		return result;
    }

    public ProcessResult deleteIpLimit(AccountVO vo) throws BizException, UserSysException {
    	METHOD_NAME = "deleteIpLimit";

    	ProcessResult result = null;
    	int retCode = -1;

		try {

			Object value = null;

    		value = delete("AccountDAO.deleteIpLimit", vo);
			if(Integer.parseInt(value.toString()) > 0){
    			retCode = 0;
    		}
    		if(retCode<0){
    			throw new BizException(CLASS_NAME, METHOD_NAME, retCode, "접속IP 삭제시 에러가 발생했습니다.");
    		}else{
    			result = new ProcessResult(CLASS_NAME, METHOD_NAME, retCode, "정상적으로 삭제 처리되었습니다.");
    		}


		} catch (SqlMapException ex) {
    		throw new UserSysException (CLASS_NAME, METHOD_NAME, "접속IP 삭제시 에러가 발생했습니다.", ex);
    	}

		return result;
    }

}
