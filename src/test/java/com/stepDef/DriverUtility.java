package com.stepDef;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.io.*;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DriverUtility {

    private WebDriver driver=null;

    public DriverUtility()
    {
//        System.out.println("I am constructor getting called");
    }

    public void instantiateWebDriver(String browserName) throws Exception {
        try {
            if(browserName.equalsIgnoreCase("chrome"))
            {
                this.driver=this.getChromeDriver();

            }
            if(browserName.equalsIgnoreCase("IE"))
            {
                this.driver=this.getIEDriver();
            }
            if(browserName.equalsIgnoreCase("firefox"))
            {
                this.driver=this.getFireFox();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
//        return driver;
    }

    private WebDriver getChromeDriver() throws Exception {
        WebDriver driver=null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("disable-infobars");
            options.addArguments("start-maximized");

            WebDriverManager.chromedriver().version("78.0.3904.70").arch32().setup();
//            File file = new File("C:\\Users\\vijitha\\.m2\\repository\\webdriver\\chromedriver\\win32\\78.0.3904.70\\chromedriver.exe");
//            System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
            driver=new ChromeDriver(options);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return driver;
    }

    private WebDriver getIEDriver() throws Exception {
        WebDriver driver= null;
        try {
            WebDriverManager.iedriver().version("2.39").arch32().setup();
            driver = new InternetExplorerDriver();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return driver;
    }

    private WebDriver getFireFox() throws Exception {
        WebDriver driver= null;
        try {
            System.out.println("FireFox is getting Installed");
            WebDriverManager.firefoxdriver().arch32().setup();
            driver = new FirefoxDriver();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return driver;
    }

    public void launchApp(String AUT) throws Exception {
        try {
            this.driver.navigate().to(AUT);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getTitle() throws Exception {
        try {
            return driver.getTitle();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    public  String getProperty(String property) throws IOException {
        Properties prop = null;
        try {
            InputStream input = new FileInputStream("src/test/resources/ObjectRepo.properties");
            prop = new Properties();
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return prop.getProperty(property);

    }

    public  WebElement getElement(String elementName) throws IOException {
        String xpath=null;
        try {
            xpath=getProperty(elementName);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return driver.findElement(By.xpath(xpath));

    }

    public  WebElement waitForElement(String elementName) throws IOException {
        String xpath=null;
        WebElement ele=null;
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(120, SECONDS)
                .pollingEvery(5, SECONDS)
                .ignoring(NoSuchElementException.class);

        try {
            xpath=getProperty(elementName);
            ele=wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));

        } catch (Exception e) {
            ele=null;
            e.printStackTrace();
            //throw e;
        }
        return ele;

    }

    public  List<WebElement> getMultipleElements(String elementName) throws IOException {
        String xpath=null;
        try {
            xpath=getProperty(elementName);

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return driver.findElements(By.xpath(xpath));

    }

    public void scrollIntoView(WebElement element) throws Exception {

        try {
            JavascriptExecutor jse = (JavascriptExecutor) driver;
            jse.executeScript("arguments[0].scrollIntoView(true);", element);
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public void closeBrowser() throws Exception {
        try {
            driver.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


}
