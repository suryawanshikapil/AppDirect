package com.appdirect.qe.appdirectintegration.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class AppDirectExcelUtils {
//	private static XSSFSheet ExcelWSheet;
//	private static XSSFWorkbook ExcelWBook;
	private static XSSFSheet ExcelWSheet;
	private static XSSFWorkbook ExcelWBook;
	
	private static XSSFCell Cell;
	private static XSSFRow Row;
	private static Logger logger = Logger.getLogger(new Exception().getStackTrace()[0].getClassName());
	private String sTestCaseName;
	private int iTestCaseRow;
	private static String email;
	private static String key;
	public enum ResultType {
		PASS, FAIL;
	}

	/*
	 * Use this method to get data from data provider
	 * 
	 * @param filePath
	 * 
	 * @param index of sheet number
	 * 
	 * @return two dimension data array object
	 */

	public static Object[][] getTableArray(String filePath, int index,
			String testCaseName, int iTestCaseRow) throws Exception {

		String[][] tabArray = null;

		try {
			FileInputStream ExcelFile = new FileInputStream(filePath);
			// Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheetAt(index);
			int startRow = getFirstTestCaseRow(testCaseName, 1);
			int startCol = 0;
			int ci, cj;
			int lastRows = getLastTestCaseRow(startRow, testCaseName, 1);
			int totalRows = lastRows - startRow;
			int totalCols = ExcelWSheet.getRow(0).getLastCellNum();
			tabArray = new String[totalRows][totalCols];
			ci = 0;
			for (int i = startRow; i < lastRows; i++, ci++) {
				cj = 0;
				for (int j = startCol; j < totalCols; j++, cj++) {
					Cell cell = ExcelWSheet.getRow(i).getCell(j);
					tabArray[ci][cj] = getCellValueAsString(cell);
				}
			}
		} catch (Exception e) {
			logger.info("Could not read the Excel sheet");
			e.printStackTrace();
		}

		finally {

			ExcelWBook.close();
		}

		return (tabArray);

	}

	/*
	 * Use this method to set the file path and open Excel file
	 * 
	 * @param path of the sheet
	 * 
	 * @param name of the sheet
	 */

	public static void setExcelFile(String path, String SheetName)
			throws Exception {
		try {
			// Open the Excel file
			FileInputStream ExcelFile = new FileInputStream(path);
			// Access the required test data sheet
			ExcelWBook = new XSSFWorkbook(ExcelFile);
//			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheet(SheetName);
		} catch (Exception e) {
			throw (e);
		}

	}

	/*
	 * Use this method to get string value from a cell
	 * 
	 * @param Cell
	 * 
	 * @return string value from the cell
	 */
	public static String getCellValueAsString(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}

		int cellType = cell.getCellType();
		switch (cellType) {
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BLANK:
			cellValue = "";
			break;
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_BOOLEAN:
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_ERROR:
			cellValue = "ERROR";
			break;
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_NUMERIC:
			cellValue = String.valueOf(cell.getNumericCellValue());
			break;
		case org.apache.poi.ss.usermodel.Cell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue();
			break;
		default:
			break;
		}

		return cellValue;
	}

	/*
	 * Use this method to write email generated on the fly to Excel file Based
	 * on the key(gmail or yahoo) in the data provider and it will generate
	 * email address
	 * 
	 * @param filePath path of the file
	 */

	public static void writeEmail(String FilePath) throws IOException {
		String[][] tabArray = null;
		try {
			FileInputStream ExcelFile = new FileInputStream(FilePath);

			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheetAt(0);
			int startRow = 1;
			int startCol = 0;
			int ci, cj;
			int totalRows = ExcelWSheet.getLastRowNum();
			int totalCols = ExcelWSheet.getRow(0).getLastCellNum();
			tabArray = new String[totalRows][totalCols];
			ci = 0;
			for (int i = startRow; i < totalRows; i++) {
				cj = 1;
				Cell cell = ExcelWSheet.getRow(i).getCell(3);
				// get key to generate email
				Cell cell1 = ExcelWSheet.getRow(i).getCell(2);
				key = cell1.getStringCellValue();
				
				if (!(key.equals("Existing")||key.equals("Invalid"))) {
					email = AppDirectUtility.generateRandomEmail(key);
					cell.setCellValue("");
					cell.setCellValue(email);
				}
				FileOutputStream fileOut = new FileOutputStream(FilePath);
				ExcelWBook.write(fileOut);
				fileOut.flush();
				fileOut.close();
				
			}

		} catch (Exception e) {
			logger.info("Could not read the Excel sheet");
			e.printStackTrace();
		} finally {
			ExcelWBook.close();
		}
	}

	/*
	 * Use this method to get test case name
	 */

	public static String getTestCaseName(String sTestCase) throws Exception {
		String value = sTestCase;
		try {
			int posi = value.indexOf("@");
			value = value.substring(0, posi);
			posi = value.lastIndexOf(".");
			value = value.substring(posi + 1);
			return value;
		} catch (Exception e) {
			throw (e);
		}
	}

	/*
	 * Use this method to get row number that contains passed test case name
	 * 
	 * @param sTestCaseName name of the test case in data provider
	 * 
	 * @param column number in data provider
	 * 
	 * @return row number
	 */

	public static int getRowContains(String sTestCaseName, int colNum)
			throws Exception {
		int i;
		try {
			int rowCount = AppDirectExcelUtils.getRowUsed();
			int count = 1;
			for (i = 0; i < rowCount; i++) {
				Cell cell = ExcelWSheet.getRow(i).getCell(colNum);
				if (AppDirectExcelUtils.getCellValueAsString(cell)
						.equalsIgnoreCase(sTestCaseName)) {
					count++;
				}
			}
			return count;
		} catch (Exception e) {
			throw (e);
		}
	}

	/*
	 * Use this method to get first row that contains passed test case name
	 * 
	 * @param sTestCaseName name of the test case in data provider
	 * 
	 * @param column number in data provider
	 * 
	 * @return row number
	 */

	public static int getFirstTestCaseRow(String sTestCaseName, int colNum)
			throws Exception {
		int i;
		try {
			int rowCount = AppDirectExcelUtils.getRowUsed();
			for (i = 1; i <= rowCount; i++) {
				Cell cell = ExcelWSheet.getRow(i).getCell(colNum);
				if (AppDirectExcelUtils.getCellValueAsString(cell)
						.equalsIgnoreCase(sTestCaseName)) {
					break;
				}
			}
			return i;
		} catch (Exception e) {
			throw (e);
		}
	}

	/*
	 * Use this method to get first row that contains passed test case name
	 * 
	 * @param sTestCaseName name of the test case in data provider
	 * 
	 * @param column number in data provider
	 * 
	 * @return row number
	 */

	public static int getLastTestCaseRow(int startRow, String sTestCaseName,
			int colNum) throws Exception {
		int i;
		try {
			int rowCount = AppDirectExcelUtils.getRowUsed();
			int count = 0;
			for (i = 1; i <= rowCount; i++) {
				Cell cell = ExcelWSheet.getRow(i).getCell(colNum);
				if (AppDirectExcelUtils.getCellValueAsString(cell)
						.equalsIgnoreCase(sTestCaseName)) {
					count++;
				}
			}
			return startRow + count;
		} catch (Exception e) {
			throw (e);
		}
	}

	/*
	 * Use this method to get rows count
	 */

	public static int getRowUsed() throws Exception {
		try {
			int RowCount = ExcelWSheet.getLastRowNum();
			return RowCount;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			throw (e);
		}
	}

	/*
	 * Use this method to write test result to excel file
	 * 
	 * @path excel file path
	 * 
	 * @result type Pass/Fail
	 */

	public static void updateTestResult(String path, ResultType type, String testCaseId)
			throws IOException {
		String[][] tabArray = null;
		try {
			FileInputStream ExcelFile = new FileInputStream(path);
			ExcelWBook = new XSSFWorkbook(ExcelFile);
//			ExcelWBook = new XSSFWorkbook(ExcelFile);
			ExcelWSheet = ExcelWBook.getSheetAt(0);
			int startRow = 1;
			int startCol = 0;
			int ci, cj;
			int totalRows = ExcelWSheet.getLastRowNum();
			int totalCols = ExcelWSheet.getRow(0).getLastCellNum();
			tabArray = new String[totalRows][totalCols];
			ci = 0;
			for (int i = startRow; i <= totalRows; i++) {
				cj = 1;
				Cell cell = ExcelWSheet.getRow(i).getCell(8);
				Cell testcaseIDcell = ExcelWSheet.getRow(i).getCell(1);
				String key = testcaseIDcell.getStringCellValue();
				if(key.equals(testCaseId)){
				cell.setCellValue(type.toString());
				}
				FileOutputStream fileOut = new FileOutputStream(path);
				ExcelWBook.write(fileOut);
				fileOut.flush();
				fileOut.close();
			}
		} catch (Exception e) {
			logger.info("Could not read the Excel sheet");
			e.printStackTrace();
		} finally {
			ExcelWBook.close();
		}
	}
	
	
}
