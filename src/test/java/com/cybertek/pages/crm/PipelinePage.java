package com.cybertek.pages.crm;

import com.cybertek.pages.loginpage.LoginPage;
import com.cybertek.utilities.BriteUtils;
import com.cybertek.utilities.ConfigurationReader;
import com.cybertek.utilities.Driver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PipelinePage {

    public PipelinePage() {
        PageFactory.initElements(Driver.getDriver(), this);
    }

    @FindBy(css = "td.o_pivot_header_cell_closed")
    public WebElement newButton;

    @FindBy(css = "div.o_pivot table thead tr:last-of-type th")
    public List<WebElement> pivotHeaderList;

    @FindBy(css = "table.o_list_view thead th.o_column_sortable")
    public List<WebElement> listHeaderList;

    @FindBy(css = "button[data-view-type='list']")
    public WebElement listButtonLocator;

    @FindBy(css = "table tbody tr:nth-of-type(3) td:nth-of-type(9)")
    public WebElement listMatchingTableLocator;

    @FindBy(css = "table tbody tr:nth-of-type(1) td:nth-of-type(2)")
    public WebElement totalExpectedRevenueLocator;

    @FindBy(css = "div[class='btn-group btn-group-sm o_cp_switch_buttons']>button[accesskey='l']")
    public WebElement listIconLocator;

    @FindBy(css = "table[class^='o_list_view table table-condensed']")
    public WebElement listTable;

    @FindBy(css = "table[class^='o_list_view table table-condensed']>tbody>tr")
    public WebElement firstOptionOfTable;

    @FindBy(xpath = "//button[contains(text(),'Action')]")
    public WebElement actionButtonLOcator;

    @FindBy(css = "div[class='modal-footer']>button>span")
    public WebElement okButtonLocator;

    @FindBy(xpath = "//ol[@class='breadcrumb']//a[contains(text(),'Pipeline')]")
    public WebElement pageNameLocator;

    @FindBy(xpath = "//li[@class='active']//span[@class='oe_menu_text'][contains(text(),'Pipeline')]")
    public WebElement pipelinePage;

    /**
     * click on the specified view type passed as a String argument
     * possible values list, kanban, calendar, graph
     * @param viewType
     */
    public void clickViewButton(String viewType) {
        WebElement viewButton = Driver.getDriver().findElement(By.cssSelector("div.o_cp_switch_buttons button[data-view-type='" + viewType + "']"));
        BriteUtils.clickOn(Driver.getDriver(), viewButton, 20);
    }

    /**
     * select pivot table option passed as a String argument
     * @param option
     */
    public void selectPivotOption(String option) {
        BriteUtils.clickOn(newButton, 20);
        WebElement pivotOption = Driver.getDriver().findElement(By.cssSelector("ul.o_pivot_field_menu li[data-field='" + option + "']"));
        BriteUtils.clickOn(pivotOption, 20);
    }

    /**
     * returns the pivot total
     * @return
     */
    public double getPivotTotal(){
        String totalstr = Driver.getDriver().findElement(By.cssSelector("div.o_pivot table tbody tr:nth-of-type(1) td[class^='o_pivot_cell_value']:nth-of-type(2)")).getText();
        double total = 0;
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        try {
            total = nf.parse(totalstr).doubleValue();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return total;
    }
    /**
     * return list of the pivot column
     * @param columnName
     * @param option
     * @return
     */
    public List<Double> getPivotColumnList(String columnName, String option){
        // create a String arraylist to hold the headers
        List<Double> values = new ArrayList<>();

        int columnNumber = 0;
        //we are looping though collection of headings
        for(int i = 0; i < pivotHeaderList.size(); i++){
            //we are looking for position of heading
            if(pivotHeaderList.get(i).getText().equals(columnName)){
                columnNumber = i+1;
                break;
            }
        }
        //based on position that we found in previous loop, we allocating column
        List<WebElement> columnList = Driver.getDriver().findElements(By.xpath("//div[starts-with(@class,'o_pivot')]//tbody//tr/td[@data-original-title=\"" + option + "\"]/..//td[starts-with(@class,'o_pivot_cell_value')]["+columnNumber+"]"));

//        for (int i = 0; i < columnList.size(); i++) {
//            NumberFormat nf = NumberFormat.getInstance(Locale.US);
//            double value = Double.parseDouble(columnList.get(i).getText());
//            values.add(value);
//        }

        for(WebElement columnValue: columnList){
            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            double value = 0;
            try {
                value = nf.parse(columnValue.getText()).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            values.add(value);
        }

        return values;
    }

    /**
     * return list of the pivot column
     * @param columnName
     * @return
     */
    public List<Double> getListColumnList(String columnName){
        // create a String arraylist to hold the headers
        List<Double> values = new ArrayList<>();

        int columnNumber = 0;
        //we are looping though collection of headings
        for(int i = 0; i < listHeaderList.size(); i++){
            //we are looking for position of heading
            if(listHeaderList.get(i).getText().equals(columnName)){
                columnNumber = i+1;
                break;
            }
        }
        //based on position that we found in previous loop, we allocating column
        List<WebElement> columnList = Driver.getDriver().findElements(By.cssSelector("table.o_list_view tbody tr td.o_data_cell:nth-of-type(" + columnNumber + ")"));

        for(WebElement columnValue: columnList){
            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            double value = 0;
            try {
                value = nf.parse(columnValue.getText()).doubleValue();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            values.add(value);
        }
        return values;
    }

    public void selectAction(String actionName) {
        String optionLocator = "//a[contains(@data-section,'other') and contains(text(),'" + actionName + "')]";
        BriteUtils.waitForVisibility(Driver.getDriver().findElement(By.xpath(optionLocator)), 5);
        Driver.getDriver().findElement(By.xpath(optionLocator)).click();
    }

    public void verifyThatOpportunityDeleted(String opportunity) {
        String locator = "//td[text()='" + opportunity + "']";
        List<WebElement> elements = Driver.getDriver().findElements(By.xpath(locator));
        Assert.assertTrue(elements.isEmpty());
    }

}

//    @FindBy(css = "div.o_cp_switch_buttons button[data-view-type='pivot']")
//    @FindBy(css = "button[data-original-title='Pivot']")
//    public WebElement pivotButton;
//
//    @FindBy(css = "li[data-field='name']>a")
//    public WebElement DropDownOpportunityLocator;

//    @FindBy(css = "table tbody tr:nth-of-type(4) td:nth-of-type(2)")
//    public WebElement secondRowSecondColumnLocator;