package net.herit.common.excel.transformer;

import java.util.List;
import java.util.Map;

import net.sf.jxls.transformer.XLSTransformer;

public abstract class HeritAbstractExcelTransformer {

	protected XLSTransformer transformer;
	
	public HeritAbstractExcelTransformer() {
		transformer = new XLSTransformer();
	}
	
	protected abstract void tranformDataToExcel(String fileName, String srcFilePath, String destFilePath, 
			Object headerData, List<Map<String, Object>> dataList);
	
}
