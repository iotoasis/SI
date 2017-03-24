package net.herit.common.excel.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class ExcelDownloadView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		File excelFile = (File) model.get("excelFile");
		String excelFileName = (String) model.get("excelFileName");
		
		//컨텐트 타입 및 헤더 정의
		response.setContentType(getContentType());
		response.setContentLength((int) excelFile.length());
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(excelFileName, "UTF-8") + ";");
		
		OutputStream outputStream = response.getOutputStream();
		
		FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(excelFile);
			
			FileCopyUtils.copy(inputStream, outputStream);
		} catch (IOException e) {
			throw e;
		} finally {
			if(inputStream != null){
				inputStream.close();
			}
			
			if(excelFile.exists()){
				String excelFilePath = excelFile.getAbsolutePath();
				//다운로드를 위해 임시로 생성하였던 엑셀 파일을 삭제한다.
				deleteTempExcelFile(excelFilePath);
			}
		}
		outputStream.flush();
	}
	
	private void deleteTempExcelFile(String excelFilePath){
		File excelTempFile = new File(excelFilePath);
		excelTempFile.delete();
	}

}
