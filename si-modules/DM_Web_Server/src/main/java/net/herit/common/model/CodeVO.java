/*****************************************************************************
 * 프로그램명  : CodeVO.java
 * 설     명  : ȸ��공통부품(코드/이름데이터빈)
 * 참고  사항  : 없음
 *****************************************************************************
 * Date       Author  Version Description
 * ---------- ------- ------- -----------------------------------------------
 * 2013.09.11   LSH    1.0     초기작성
 *****************************************************************************/

package net.herit.common.model;

import net.herit.common.util.StringUtil;




/**
 * 코드・명칭정보
 *
 * @author  tnc
 * @version 0.1
 */

public class CodeVO {
	/** 코드 */
	private String code = "";
	/** 명칭 */
	private String name = "";


	/**
	 * 생성자
	 */
	public CodeVO() {}

	/**
	 * 생성자(기본값을 설정한다)
	 * @param strCode　코드
	 * @param strName　코드명칭
	 */
    public CodeVO (String strCode, String strName) {
		setCode(strCode);
		setName(strName);
	}


	/**
	 * 코드값취득
	 * @return String　코드값
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 코드값설정
	 * @param code 코드값
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 코드명칭취득
	 * @return String　코드명칭
	 */
	public String getName() {
		return StringUtil.nvl(name);
	}
	/**
	 * 코드명칭설정
	 * @param name 코드명칭
	 */
	public void setName(String name) {
		this.name = name;
	}

}
