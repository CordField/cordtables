package com.seedcompany.cordtables.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.seedcompany.cordtables.model.TablesOption;

/**
 * Table data extractor that can be used to extract the data from table.
 * 
 * @author swati
 *
 */
public class TableDataUtils {

	private static Logger logger = LoggerFactory.getLogger(TableDataUtils.class);
	
	private WebElement container;

	private WebDriver driver;

	public TableDataUtils(WebElement container, WebDriver driver) {
		this.driver = driver;
		this.container = container;
	}

	/**
	 * @return
	 */
	private SearchContext tableRoot() {

		SearchContext tableRoot = SeleniumUtils
				.expand_shadow_element(this.container.findElement(By.tagName("table-root")));
		return tableRoot;

	}

	/**
	 * @param option
	 * @return
	 */
	public SearchContext table(TablesOption option) {

		SearchContext dataComponent = SeleniumUtils
				.expand_shadow_element(this.tableRoot().findElement(By.cssSelector(option.getTag())));

		SearchContext table = SeleniumUtils
				.expand_shadow_element(dataComponent.findElement(By.cssSelector("cf-table")));

		return table;

	}

	/**
	 * @param table
	 * @return
	 */
	public List<WebElement> header(WebElement table) {

		SearchContext headerRow = SeleniumUtils.expand_shadow_element(table.findElement(By.cssSelector("cf-row")));

		return headerRow.findElement(By.xpath("//*[@id=\'header-row\']")).findElements(By.cssSelector("cf-cell2"));

	}

	/**
	 * @param table
	 * @return
	 */
	public List<WebElement> rows(SearchContext table) {

		SearchContext body = SeleniumUtils.expand_shadow_element(table.findElement(By.cssSelector("cf-table-body")));

		return body.findElements(By.cssSelector("cf-row"));

	}

	/**
	 * @param row
	 * @return
	 */
	public List<WebElement> columns(WebElement row) {

		SearchContext datarow = SeleniumUtils.expand_shadow_element(row);

		return datarow.findElements(By.cssSelector("cf-cell2"));

	}

	/**
	 * @param column
	 * @return
	 */
	public String columnData(WebElement column) {

		SearchContext dataCol = SeleniumUtils.expand_shadow_element(column);
		return dataCol.findElement(By.className("value-wrapper")).getText();

	}

	/**
	 * @param option
	 * @return
	 */
	public List<List<String>> extractData(TablesOption option) {

		List<List<String>> result = new ArrayList<List<String>>();
		List<String> rowData = null;
		SearchContext table = this.table(option);
		List<WebElement> records = this.rows(table);
		for (WebElement record : records) {
			List<WebElement> data = this.columns(record);
			rowData = new ArrayList<>();
			for (WebElement col : data) {
				rowData.add(this.columnData(col));
			}
			result.add(rowData);
		}

		return result;
	}

	/**
	 * @param option
	 * @return
	 */
	public List<List<WebElement>> readTable(TablesOption option) {

		List<List<WebElement>> result = new ArrayList<>();
		List<WebElement> rowData = null;
		SearchContext table = this.table(option);
		List<WebElement> records = this.rows(table);
		for (WebElement record : records) {
			List<WebElement> data = this.columns(record);
			rowData = new ArrayList<>();
			for (WebElement col : data) {
				rowData.add(col);
			}
			result.add(rowData);
		}

		return result;
	}

	/**
	 * This method is use to delete the row from up prayer request form table.
	 */
	public void deleteRecord(String id) {

		try {

			List<List<WebElement>> records = this.readTable(TablesOption.ADMIN_PEOPLE);
			records.forEach(r -> {
				WebElement idCol = r.get(0);
				if (id.equalsIgnoreCase(this.columnData(idCol))) {

					SearchContext dataCol = SeleniumUtils.expand_shadow_element(idCol);
					dataCol.findElement(By.className("delete-span")).click();
					dataCol.findElement(By.className("save-icon")).click();

				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param peopleId
	 * @param option
	 * @return
	 */
	public List<String> findRecord(String id, TablesOption option) {
		List<String> data = null;
		try {
			List<List<WebElement>> records = this.readTable(option);
			for (List<WebElement> r : records) {
				WebElement idCol = r.get(0);
				if (id.equalsIgnoreCase(this.columnData(idCol))) {

					data = r.stream().map(c -> this.columnData(c)).collect(Collectors.toList());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * @param option
	 * @return
	 */
	public List<String> findFirstRecord(TablesOption option) {
		SeleniumUtils.scrollToElement(this.container, driver);
		List<List<String>> records = this.extractData(option);
		return records.stream().map(r -> {
			return r.get(0);
		}).collect(Collectors.toList());
	}

}
