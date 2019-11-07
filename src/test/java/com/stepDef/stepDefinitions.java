package com.stepDef;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import gherkin.ast.Step;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Driver;
import java.util.List;
import java.util.Properties;


public class stepDefinitions {
    final static Logger logger = Logger.getLogger(stepDefinitions.class);
    DriverUtility util = new DriverUtility();

    @Before
    public void runBeforeScenario() {

        logger.info("RUNNING BEFORE SCENARIO");
        try {

            String browserName = getInputProperty("BrowserName");
            util.instantiateWebDriver(getInputProperty("BrowserName"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @After
    public void runAfterScenario() throws Exception {

        System.out.println("Running after scenario");
        util.closeBrowser();
    }

    @Given("^I have logged into ebay$")
    public void iHaveLoggedIntoEbay() throws Exception {
        try {
            String app = util.getProperty("AUT");
            util.launchApp(app);
            util.waitForElement("input_box");
            String title = util.getTitle();
            if (title.toLowerCase().contains("ebay")) {
                System.out.println("Loggin Step is Passed");
            } else {
                System.out.println("Loggin Step is not working");
                throw new Exception("Login Failure");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @And("^Search for a Product$")
    public void searchForAProduct() throws Exception {
        try {
            String product = getInputProperty("Product");
            util.getElement("input_box").sendKeys(product + " new condition");
            util.getElement("search").click();
            int resultCount = util.getMultipleElements("resultList").size();
            if (resultCount > 1) {
                System.out.println("Items are Available in Ebay");
                logger.info("Items are Availables in EBay");
            } else {
                System.out.println("Items are not available for this product");
                logger.warn("Items are not available for this Product");
                throw new Exception("Items are not available for this product");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @And("^i add top (\\d+) listings to cart$")
    public void iAddTopListingsToCart(int arg0) throws Exception {
        try {
            util.getElement("BuyItNow").click();
//            util.getElement("ConditionMenu").click();
//            Thread.sleep(10000);
//            util.getElement("NewCondition").click();
            util.waitForElement("resultList");
            List<WebElement> list = util.getMultipleElements("item");
            int size = 0;
            int total=0;
            if (list.size() >= 5) {
                total = 5;
            } else {
                total = list.size();
            }
            while (size<total) {
                util.scrollIntoView(util.getMultipleElements("item").get(size));
                util.getMultipleElements("item").get(size).click();
                if(util.waitForElement("Add_to_cart")==null)
                {
                    util.getElement("BackToResults").click();
                    util.waitForElement("input_box");
                    logger.info("Not Adding Item " + size + " into Cart, As it is For Bidding");
                    size=size+1;

                    continue;
                }
                util.scrollIntoView(util.getElement("Add_to_cart"));
//
//                util.scrollIntoView(util.getElement("Add_to_cart"));
                util.getElement("Add_to_cart").click();
                util.waitForElement("close");
                util.getElement("close").click();
                util.getElement("BackToResults").click();
                util.waitForElement("input_box");
                System.out.println("Added Item " + size + " into Cart and Back to Results page");
                logger.info("Added Item " + size + " into Cart and Back to Results page");
                size=size+1;
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Then("^i proceed to checkout cart$")
    public void i_proceed_to_checkout_cart() throws Exception {
        try {
            util.getElement("shopping_cart").click();
            util.waitForElement("shopping_cart_count");
            String value = util.getElement("shopping_cart_count").getText();
            if (value.contains("5")) {
                System.out.println("5 Items are added to Cart");
                logger.info("5 Items are added to Cart");
            } else {
                System.out.println("Less Items are added to Cart");
                logger.warn("Less then 5 Items are added to Cart");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private String getInputProperty(String property) {
        Properties prop = null;
        try {
            InputStream input1 = new FileInputStream("src/test/resources/InputFile.properties");
            prop = new Properties();
            prop.load(input1);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Property returning is"+prop.getProperty(property));
        return prop.getProperty(property);
    }


}
