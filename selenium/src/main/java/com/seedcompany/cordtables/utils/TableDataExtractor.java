package com.seedcompany.cordtables.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.seedcompany.cordtables.model.TablesOption;

/**
 * Table data extractor that can be used to extract the data from table.
 * 
 * @author swati
 *
 */
public class TableDataExtractor {

	private WebElement container;

	private WebDriver driver;

	public TableDataExtractor(WebElement container, WebDriver driver) {
		this.driver = driver;
		this.container = container;
	}

	private SearchContext tableRoot() {

		SearchContext tableRoot = SeleniumUtils
				.expand_shadow_element(this.container.findElement(By.tagName("table-root")));
		return tableRoot;

	}

	public SearchContext table(TablesOption option) {

		SearchContext dataComponent = SeleniumUtils
				.expand_shadow_element(this.tableRoot().findElement(By.cssSelector(option.getTag())));

		SearchContext table = SeleniumUtils.expand_shadow_element(dataComponent.findElement(By.cssSelector("cf-table")));

		return table;

	}

	public List<WebElement> header(WebElement table) {

		SearchContext headerRow = SeleniumUtils.expand_shadow_element(table.findElement(By.cssSelector("cf-row")));

		return headerRow.findElement(By.xpath("//*[@id=\'header-row\']")).findElements(By.cssSelector("cf-cell2"));

	}

	public List<WebElement> rows(SearchContext table) {

		SearchContext body = SeleniumUtils.expand_shadow_element(table.findElement(By.cssSelector("cf-table-body")));

		return body.findElements(By.cssSelector("cf-row"));

	}

	public List<WebElement> columns(WebElement row) {

		SearchContext datarow = SeleniumUtils.expand_shadow_element(row);

		return datarow.findElements(By.cssSelector("cf-cell2"));

	}

	public String columnData(WebElement column) {

		SearchContext dataCol = SeleniumUtils.expand_shadow_element(column);
		return dataCol.findElement(By.className("value-wrapper")).findElement(By.tagName("span")).getText();

	}

	public List<List<String>> extractData(TablesOption option) {

		List<List<String>> result = new ArrayList<List<String>>();
		List<String> rowData = null;
		SearchContext table = this.table(option);
		List<WebElement> records = this.rows(table);
		for (WebElement record : records) {
			List<WebElement> data = this.columns(record);
			rowData = new ArrayList<>();
			for (WebElement col : data) {
				// System.out.println("Data::" + this.columnData(col));
				rowData.add(this.columnData(col));
			}
			result.add(rowData);
		}

		return result;
	}

	public List<List<WebElement>> readTable(TablesOption option) {

		List<List<WebElement>> result = new ArrayList<>();
		List<WebElement> rowData = null;
		SearchContext table = this.table(option);
		List<WebElement> records = this.rows(table);
		for (WebElement record : records) {
			List<WebElement> data = this.columns(record);
			rowData = new ArrayList<>();
			for (WebElement col : data) {
				// System.out.println("Data::" + this.columnData(col));
				rowData.add(col);
			}
			result.add(rowData);
		}

		return result;
	}

}
