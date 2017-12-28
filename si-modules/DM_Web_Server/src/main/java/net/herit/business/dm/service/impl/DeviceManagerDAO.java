package net.herit.business.dm.service.impl;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

import net.herit.business.dm.service.NotificationVO;
import net.herit.common.dataaccess.HeritHdmAbstractDAO;
import net.herit.common.exception.UserSysException;

@Repository("DeviceManagerDAO")
public class DeviceManagerDAO extends HeritHdmAbstractDAO {
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";

	public NotificationVO getCurrentNotificationInfo() throws UserSysException {
		METHOD_NAME = "getCurrentNotificationInfo";
		
		NotificationVO notiVo = null;
		
		try {
			HashMap<String, String> po = new HashMap<String,String>();
			
			notiVo = (NotificationVO)getSqlMapClientTemplate().queryForObject(
					"DeviceManagerDAO.getCurrentNotificationInfo", po);
			
			return notiVo;

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"사용자관리 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
	}
}
