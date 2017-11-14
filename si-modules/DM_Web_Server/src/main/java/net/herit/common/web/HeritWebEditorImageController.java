package net.herit.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.herit.common.conf.HeritProperties;
import net.herit.common.util.HeritFormBasedFileUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 웹에디터 이미지 upload 처리 Controller
 * @author 공통컴포넌트개발팀 한성곤
 * @since 2009.08.26
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.26  한성곤          최초 생성
 *
 * </pre>
 */
@Controller
public class HeritWebEditorImageController {

    /** 톰캣 위치 지정 */
    private final String tomcatDir = HeritProperties.getProperty("Globals.tomcatDir");
    /** 첨부파일 위치 지정 */
    private final String uploadDir = HeritProperties.getProperty("Globals.uploadDir");
    /** 첨부파일 위치 지정 */
    private final String imgFileUploadDir = tomcatDir + uploadDir;

    /** 첨부 최대 파일 크기 지정 */
    private final long maxFileSize = 1024 * 1024 * 100;   //업로드 최대 사이즈 설정 (100M)

    /**
     * 이미지 view를 제공한다.
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/common/web/imageSrc.do",method=RequestMethod.GET)
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String subPath = request.getParameter("path");
		String physical = request.getParameter("physical");
		String mimeType = request.getParameter("contentType");

		HeritFormBasedFileUtil.viewFile(response, imgFileUploadDir, subPath, physical, mimeType);
    }
}
