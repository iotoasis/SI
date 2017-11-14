package net.herit.business.monitor.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.herit.common.dataaccess.HeritHdpAbstractDAO;
import net.herit.common.exception.UserSysException;

import org.springframework.stereotype.Repository;

import com.ibatis.sqlmap.client.SqlMapException;

@Repository("MonitorDAO")
public class MonitorDAO extends HeritHdpAbstractDAO {
	/** 클래스 명칭 */
	private final String CLASS_NAME = getClass().getName();
	/** 메소드명칭 */
	private String METHOD_NAME = "";

	public List<HashMap<String,Object>> getRowListForTotal() throws UserSysException {
		METHOD_NAME = "getRowList";

		List<HashMap<String,Object>> resultList = null;

		try {
			Map<String, Object> po = new HashMap<String, Object>();
			resultList = getSqlMapClientTemplate().queryForList(
					"layout.component.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"레이아웃 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}

	public List<HashMap<String,Object>> getRowListForModel(long modelId) throws UserSysException {
		METHOD_NAME = "getRowList";

		List<HashMap<String,Object>> resultList = null;

		try {
			Map<String, Object> po = new HashMap<String, Object>();
			po.put("deviceModelId", modelId);
			resultList = getSqlMapClientTemplate().queryForList(
					"layout.component.list", po);

		} catch (SqlMapException ex) {
			throw new UserSysException(CLASS_NAME, METHOD_NAME,
					"레이아웃 데이터 취득 처리에서 에러가 발생했습니다.", ex);
		}
		return resultList;
	}
	
}
