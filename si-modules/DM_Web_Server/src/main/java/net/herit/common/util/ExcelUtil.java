/*****************************************************************************
 * 프로그램명  : ExcelUtil.java
 * 설     명  : 엑셀 관련 클래스 util class.
 * 참고  사항  : 없음
 *****************************************************************************
 * Date       Author  Version Description
 * ---------- ------- ------- -----------------------------------------------
 * 2011.01.31  LSH    1.0     초기작성
 *****************************************************************************/
package net.herit.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.herit.common.model.HeritFormBasedFileVO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.util.LangUtil;

/**
 * 엑셀관련 클래스 util class.
 * @author  tnc
 * @version 1.0
 */
public class ExcelUtil {
	/** 로그처리 객체 */
    protected static Log logger = LogFactory.getLog(LangUtil.class);



    /** 엑셀파일로부터 데이터를 읽어 리턴한다.
     * @param path 확장자에 따라 2003문서인지 2007문서인지를 판단한다.
     * @return
     */
	public static List getExcelData_test(String path, HeritFormBasedFileVO vo) {
		List excelList = new ArrayList();

		try {
			InputStream is = new FileInputStream(path);
			Workbook wb = null;
			int indexDot = vo.getFileName().lastIndexOf(".");
			if (indexDot == -1)
				indexDot = 0;
			String fileExtention = vo.getFileName().substring(indexDot).toLowerCase();
			if (".xlsx".equals(fileExtention)) {
				XSSFWorkbook work = new XSSFWorkbook(is);

				int sheetNum = work.getNumberOfSheets();

				//System.out.println("\n# sheet num : " + sheetNum);

				for (int loop = 0; loop < sheetNum; loop++) {
					XSSFSheet sheet = work.getSheetAt(loop);

					int rows = sheet.getPhysicalNumberOfRows();

					//System.out.println("\n# sheet rows num : " + rows);

					for (int rownum = 0; rownum < rows; rownum++) {
						XSSFRow row = sheet.getRow(rownum);

						if (row != null) {
							int cells = row.getPhysicalNumberOfCells();

							//System.out.println("\n# row = " + row.getRowNum()+ " / cells = " + cells);

							String[] excelRow = new String[cells];
							for (int cellnum = 0; cellnum < cells; cellnum++) {
								XSSFCell cell = row.getCell(cellnum);

								if (cell != null) {
									String value = null;
									switch (cell.getCellType()) {
									case Cell.CELL_TYPE_FORMULA:
										value = cell.getCellFormula();
										break;

									case Cell.CELL_TYPE_NUMERIC:
										value = ""
												+ Integer
														.parseInt(String.valueOf(Math.round(cell
																.getNumericCellValue())));
										break;

									case Cell.CELL_TYPE_STRING:
										value = "" + cell.getStringCellValue();
										break;

									case Cell.CELL_TYPE_BLANK:
										value = "" + cell.getBooleanCellValue();
										break;

									case Cell.CELL_TYPE_ERROR:
										value = "" + cell.getErrorCellValue();
										break;
									default:
									}
									excelRow[cellnum] = value;

								}
							}
							excelList.add(excelRow);

						}
					}
				}

			} else {
				POIFSFileSystem fileSystem = new POIFSFileSystem(is);
				HSSFWorkbook work = new HSSFWorkbook(fileSystem);
				int sheetNum = work.getNumberOfSheets();

				for (int loop = 0; loop < sheetNum; loop++) {
					HSSFSheet sheet = work.getSheetAt(loop);

					int rows = sheet.getPhysicalNumberOfRows();

					//System.out.println("\n# sheet rows num : " + rows);

					for (int rownum = 0; rownum < rows; rownum++) {
						HSSFRow row = sheet.getRow(rownum);

						if (row != null) {
							int cells = row.getPhysicalNumberOfCells();

							//System.out.println("\n# row = " + row.getRowNum()+ " / cells = " + cells);

							String[] excelRow = new String[cells];
							for (int cellnum = 0; cellnum < cells; cellnum++) {
								HSSFCell cell = row.getCell(cellnum);

								if (cell != null) {
									String value = null;
									switch (cell.getCellType()) {
									case Cell.CELL_TYPE_FORMULA:
										value = cell.getCellFormula();
										break;

									case Cell.CELL_TYPE_NUMERIC:
										value = ""
												+ Integer
														.parseInt(String.valueOf(Math.round(cell
																.getNumericCellValue())));
										break;

									case Cell.CELL_TYPE_STRING:
										value = "" + cell.getStringCellValue();
										break;

									case Cell.CELL_TYPE_BLANK:
										value = "" + cell.getBooleanCellValue();
										break;

									case Cell.CELL_TYPE_ERROR:
										value = "" + cell.getErrorCellValue();
										break;
									default:
									}
									excelRow[cellnum] = value;

								}
							}
							excelList.add(excelRow);

						}
					}
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return excelList;
	}


    /** 엑셀파일로부터 데이터를 읽어 리턴한다.
     * @param path 확장자에 따라 2003문서인지 2007문서인지를 판단한다.
     * @return
     */
    public static List getExcelData(String path, HeritFormBasedFileVO vo) {
        List excelList = new ArrayList();

        try {
            InputStream is = new FileInputStream(path);
            Workbook wb = null;
			int indexDot = vo.getFileName().lastIndexOf(".");
			if (indexDot == -1)
				indexDot = 0;
			String fileExtention = vo.getFileName().substring(indexDot).toLowerCase();
            if (".xlsx".equals(fileExtention)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            int sheetNum = wb.getNumberOfSheets();

            for (int k=0; k<sheetNum; k++) {
                Sheet sheet = wb.getSheetAt(k);
                int rows = sheet.getPhysicalNumberOfRows();

                for (int r=0; r<rows; r++) {
                    Row row = sheet.getRow(r);

                    if (row != null) {
                        //int cells = row.getPhysicalNumberOfCells();
                    	//마지막 셀 위치까지 데이터 가져오도록 수정 2011.11.13 - LSH
                    	int cells = row.getLastCellNum();

                    	String[] excelRow = new String[cells];
                        for(int c=0; c<cells; c++) {
                            Cell cell = row.getCell(c);
                            if (cell != null) {
                                String value = null;

                                switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_FORMULA:
                                        value = cell.getCellFormula();
                                    break;

                                    case Cell.CELL_TYPE_NUMERIC:
                                        value = "" + Integer.parseInt(String.valueOf(Math.round(cell.getNumericCellValue())));
                                    break;

                                    case Cell.CELL_TYPE_STRING:
                                        value = "" + cell.getStringCellValue();
                                    break;

                                    case Cell.CELL_TYPE_BLANK:
                                        value = "" + cell.getBooleanCellValue();
                                    break;

                                    case Cell.CELL_TYPE_ERROR:
                                        value = "" + cell.getErrorCellValue();
                                    break;
                                    default:
                                }
                                excelRow[c] = value;
                            }
                        }
                        excelList.add(excelRow);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return excelList;
    }

    /** 엑셀파일로부터 데이터를 읽어 리턴한다.
     * 고정화된 형식이 아니라 넘어 오는 값에 따른 CELL의 갯수를 계산해서 마지막 값이 비어도 가지고 올수 있어야 한다. Once
     * @param path 확장자에 따라 2003문서인지 2007문서인지를 판단한다.
     * @return
     */
    public static List getExcelData(String path, int idxParam) {
        List excelList = new ArrayList();

        try {
            InputStream is = new FileInputStream(path);
            Workbook wb = null;
            int indexDot = path.lastIndexOf(".");
            if (indexDot == -1 ) indexDot = 0;
            String fileExtention = path.substring(indexDot).toLowerCase();
            if (".xlsx".equals(fileExtention)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            int sheetNum = wb.getNumberOfSheets();

            for (int k=0; k<sheetNum; k++) {
                Sheet sheet = wb.getSheetAt(k);
                int rows = sheet.getPhysicalNumberOfRows();

                for (int r=0; r<rows; r++) {
                    Row row = sheet.getRow(r);

                    if (row != null) {
                        //int cells = row.getPhysicalNumberOfCells();
                    	int cells = idxParam;

                    	String[] excelRow = new String[cells];
                        for(int c=0; c<cells; c++) {
                            Cell cell = row.getCell(c);
                            if (cell != null) {
                                String value = null;

                                switch (cell.getCellType()) {
                                    case Cell.CELL_TYPE_FORMULA:
                                        value = cell.getCellFormula();
                                    break;

                                    case Cell.CELL_TYPE_NUMERIC:
                                        value = "" + Integer.parseInt(String.valueOf(Math.round(cell.getNumericCellValue())));
                                    break;

                                    case Cell.CELL_TYPE_STRING:
                                        value = "" + cell.getStringCellValue();
                                    break;

                                    case Cell.CELL_TYPE_BLANK:
                                        value = "" + cell.getBooleanCellValue();
                                    break;

                                    case Cell.CELL_TYPE_ERROR:
                                        value = "" + cell.getErrorCellValue();
                                    break;
                                    default:
                                }
                                excelRow[c] = value;
                            }
                        }
                        excelList.add(excelRow);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return excelList;
    }

    /**
     * 엑셀파일로부터 데이터를 읽어 리턴한다.(inputstream으로 들어올 경우)     *
     * 2003, 2007 모두 읽기 가능
     */
    public static List getExcelData(InputStream is) throws Exception{
        List excelList = new ArrayList();
        Workbook wb = null;
        try {
            wb = new HSSFWorkbook(is);            //2003이하
        } catch (OfficeXmlFileException e) {    //2007
            is.reset();
            wb = new XSSFWorkbook(is);
        }
        excelList = getData(wb);
        return excelList;
    }
    /**
     * 엑셀파일로부터 데이터를 읽어 리턴한다.     *
     */
    public static List getData(Workbook wb) {
        List excelList = new ArrayList();

        int sheetNum = wb.getNumberOfSheets();

        for (int k=0; k<sheetNum; k++) {
            Sheet sheet = wb.getSheetAt(k);
            int rows = sheet.getPhysicalNumberOfRows();

            for (int r=0; r<rows; r++) {
                Row row = sheet.getRow(r);

                if (row != null) {
                    int cells = row.getPhysicalNumberOfCells();
                    String[] excelRow = new String[cells];
                    for(int c=0; c<cells; c++) {
                        Cell cell = row.getCell(c);
                        if (cell != null) {
                            String value = null;

                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_FORMULA:
                                    value = cell.getCellFormula();
                                break;

                                case Cell.CELL_TYPE_NUMERIC:
                                    value = "" + Integer.parseInt(String.valueOf(Math.round(cell.getNumericCellValue())));
                                break;

                                case Cell.CELL_TYPE_STRING:
                                    value = "" + cell.getStringCellValue();
                                break;

                                case Cell.CELL_TYPE_BLANK:
                                    value = "" + cell.getBooleanCellValue();
                                break;

                                case Cell.CELL_TYPE_ERROR:
                                    value = "" + cell.getErrorCellValue();
                                break;
                                default:
                            }
                            excelRow[c] = value;
                        }
                    }
                    excelList.add(excelRow);
                }
            }
        }
        return excelList;
    }






}
