package com.cybertek.tests.crm;

import com.cybertek.pages.crm.PipelinePage;
import com.cybertek.pages.loginpage.LoginPage;
import com.cybertek.utilities.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PipelineTest extends TestBase {

    @Test
    public void Test1() {
        extentLogger = report.createTest("Verify that second opportunity "+
                "Expected Revenue value on the Pivot board should be " +
                "the same as the Expected revenue column value on the list board.");
        LoginPage loginpage = new LoginPage();
        PipelinePage pipelinePage = new PipelinePage();

        // Login
        String username = ConfigurationReader.getProperty("username");
        String password = ConfigurationReader.getProperty("password");
        loginpage.login(username,password);
        SeleniumUtils.waitPlease(5);

        // Go to CRM module
        BriteUtils.navigateToModule("CRM");

        // Select Pivot view
        pipelinePage.clickViewButton("pivot");

        // Select Opportunity
        // Select "name" for Opportunity
        pipelinePage.selectPivotOption("name");

        // Choose Expected Revenue amount from second row
        List<Double> pivotList = pipelinePage.getPivotColumnList("Expected Revenue","Opportunity");
        System.out.println("this is pivot list");
        System.out.println(pivotList);
        double pivotExpectedRevenue = pivotList.get(1);

        // Select list view
        pipelinePage.clickViewButton("list");

        // Choose Expected Revenue amount from second row
        List<Double> listList = pipelinePage.getListColumnList("Expected Revenue");
        System.out.println("this is list list");
        System.out.println(listList);
        double listExpectedRevenue = listList.get(1);

        //verify they have same revenue
        Assert.assertTrue(pivotExpectedRevenue == listExpectedRevenue);
    }

    @Test
    public void Test2() {
        extentLogger = report.createTest("Verify that on the pivot table, " +
                "the total expected revenue should be the sum of all opportunities’ expected revenue.");
        LoginPage loginpage = new LoginPage();
        PipelinePage pipelinePage = new PipelinePage();

        // 1. Login
        String username = ConfigurationReader.getProperty("username");
        String password = ConfigurationReader.getProperty("password");
        loginpage.login(username,password);

        // 2. Go to CRM module
        BriteUtils.navigateToModule("CRM");

        // 3. Select Pivot view
        pipelinePage.clickViewButton("pivot");

        // 4. Select Opportunity
        // select name to select opportunity
        pipelinePage.selectPivotOption("name");

        // Verify that on the pivot table, the total expected revenue
        // should be the sum of all opportunities’ expected revenue.

        // calculate the pivot total from the lines
        List<Double> pivotList = pipelinePage.getPivotColumnList("Expected Revenue","Opportunity");
        double pivotTotal = 0;
        for (Double each:pivotList) {
            pivotTotal += each;
        }

        // compared the total line with the calculated amount
        Assert.assertTrue(pivotTotal==pipelinePage.getPivotTotal());
    }
}


//*
//        Verify that second opportunity’ Expected Revenue value on the
//        Pivot board should be the sameas the Expected revenue column value on the list board.
//
//        */

//    Verify that on the pivot table, the total expected revenue should be the sum of all opportunities’ expected revenue.
// 1.log in
// 2.click CRM button
// 3.click pivot icon
// 4. click new and choose opportunity
// 5.choose second row and second column /on list board it is first one
//verify they have same revenue

// Verify that on the pivot table, the total expected revenue should be the sum of all opportunities’ expected revenue.
//1. PreCondition- each CRM manager user should create at least 3 opportunities on the CRM module.
// (create 2 opportunity just in case)
// 2.Pre-condition two: on Pivot table expand total and select opportunity from the dropdown.
