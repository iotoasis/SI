/**
 * 
 */
package net.herit.common.dataaccess;

import javax.annotation.Resource;

import com.ibatis.sqlmap.client.SqlMapClient;


/**
 * HeritComAbstractDAO.java 클래스
 * 
 * @author 서준식
 * @since 2011. 9. 23.
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    -------------    ----------------------
 *   2011. 9. 23.   서준식        최초 생성
 * </pre>
 */
public abstract class HeritHdhAbstractDAO extends HeritAbstractDAO{

	@Resource(name="sqlMapClientHDH")
	public void setSuperSqlMapClient(SqlMapClient sqlMapClient) {
        super.setSuperSqlMapClient(sqlMapClient);
    }
	

}
