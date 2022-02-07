package com.seedcompany.cordtables.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used as a test data provider and can be used to load the test
 * data from test data sheet.
 * 
 * @author swati
 *
 */
public class TestDataProvider {

	private static Logger logger = LoggerFactory.getLogger(TestDataProvider.class);

	/**
	 * This method can be used to load the test data from the test data sheet.
	 * 
	 * @param fileName
	 * @param sheetName
	 * @return
	 * @throws IOException
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 */
	public List<List<String>> getTestData(String filePath, String sheetName) {
		List<List<String>> testScenarios = new ArrayList<>();

		try {
			// Path of the excel file
			// final ClassLoader classLoader =
			// Thread.currentThread().getContextClassLoader();
			// InputStream inp = classLoader.getResourceAsStream(fileName);

			// Creating a workbook
			InputStream inp = new FileInputStream(filePath);

			Workbook workbook = WorkbookFactory.create(inp);

			Sheet sheet = workbook.getSheet(sheetName);

			if (sheet == null) {
				sheet = workbook.getSheetAt(0);
			}

			Iterator<Row> dataRows = sheet.rowIterator();
			while (dataRows.hasNext()) {
				Iterator<Cell> dataColumns = dataRows.next().cellIterator();
				List<String> testData = new ArrayList<String>();
				while (dataColumns.hasNext()) {
					testData.add(dataColumns.next().getStringCellValue());
				}
				testScenarios.add(testData);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
		}

		return testScenarios;
	}

}
