package com.appdirect.qe.appdirectintegration.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.internal.FileHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.IInvokedMethod;
import org.testng.ITestResult;

import com.appdirect.qe.appdirectintegration.components.AppDirectHomePage;
import com.appdirect.qe.appdirectintegration.components.AppDirectLoginPage;
import com.appdirect.qe.appdirectintegration.components.AppDirectSignUpPage;


public class AppDirectUtility {
	private static WebDriver driver;
	 private static int MAX_WAIT = 60;
	 private static int EACH_WAIT = 5;
	 private static int WAIT_COUNT = MAX_WAIT / (EACH_WAIT * 2);
	private static WebElement element = null;
	private static Logger logger = Logger.getLogger(new Exception().getStackTrace()[0].getClassName());
	
	 
	/*
	 * Use this method to wait for certain element to appear 
	 * @param webelement
	 */
	
	
    public static void waitForElementToAppear(WebDriver driver, WebElement element) throws Exception {
        boolean found = false;
        logger.info("Waiting for element: ");

        driver.manage().timeouts().implicitlyWait(EACH_WAIT, TimeUnit.SECONDS);
        for (int i = 0; i < WAIT_COUNT; i++) {
            try {
                Thread.sleep(EACH_WAIT * 1000);
             
                if (element != null) {
                    found = true;
                    break;
                }
            } catch (Exception ex) {
                
            }
        }
        // turn off implicit wait
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        if( !found ) {
            throw new Exception("Could not locate: " + element);
        }
    }
    
  /*
   * Use this method to read from properties file
   * @param .properties file name
   * @param key 
   * @return string value 
   */
    
    
    public  String getPropValues(String propFileName, String key) throws IOException {
    	Properties prop = new Properties();
    	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
    	if (inputStream != null) {
			prop.load(inputStream);
		} else {
			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
		}
 		return prop.getProperty(key);
	}
    
    /*
     * Use this method to read formated from properties file
     * @param .properties file name
     * @param key 
     * @return string value 
     */
      
      
      public  static String getFormatedMessage(String propFileName, String key,String email) throws IOException {
      	Properties prop = new Properties();
      	InputStream inputStream =FileHelper.class.getClassLoader().getResourceAsStream(propFileName);
      	if (inputStream != null) {
  			prop.load(inputStream);
  		} else {
  			throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
  		}
      	String message =prop.getProperty(key);
//      	return MessageFormat.format((String) prop.get(key), "abcd");
      	MessageFormat mf = new MessageFormat(message);
   		return mf.format((new Object[]{email})).toString();
  	}
    
  /*
   * Use this method to generate unique email address 
   * @param key (gmail or yahoo) based on this it will generate email id 
   * @return email 
   */
    
    
    public static String generateRandomEmail(String key){
    	
    	 Random rand = new Random();
    	 String email;
    	 int randomPIN = (int)(Math.random()*9000)+1000;
    	
    	 if (key.equals("gmail")){
    	  email = "AppDirect"+randomPIN+"@gmail.com";
    	 } else if (key.equals("yahoo")){
    		 email = "AppDirect"+randomPIN+"@yahoo.com";
        	 } 
    	
    	 else {
    		 email = "AppDirect"+randomPIN+"@AppDirect.com";
    		 
    	 }
    	
    	return email;
    	
    }
    
    /*
     * Use this method to invoke a driver
     * @param driver name 
     * @return driver
     */
    
    
    public static WebDriver invokeDriver(String param, String url){
    	
    	switch(param) {
    	
    	case "firefox":
    		driver = new FirefoxDriver();
    		driver.get(url);
			driver.manage().window().maximize();
    		return driver;
    		
    		
    	case "IE":
    		    		
    		System.setProperty("webdriver.ie.driver", "src/main/resources/IEDriverServer.exe");
    		driver = new InternetExplorerDriver();
    		driver.get(url);
			driver.manage().window().maximize();
    		return driver;
    		
    	case "chrome" : 
    		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
    		driver = new ChromeDriver();
    		driver.get(url);
			driver.manage().window().maximize();
    		return driver;
    		
    	
    	
    	}
    	
    	driver = new FirefoxDriver();
		driver.get("https://www.appdirect.com/");
		driver.manage().window().maximize();
		return driver;
    	
    	}
    
    
    public static void navigateToSignUpPage(WebDriver driver) throws Exception{
    	
//    	invokeDriver("FF");
    	
    	logger.info("Open App Direct home page");
		AppDirectHomePage homePage = PageFactory.initElements(driver,
				AppDirectHomePage.class);
		logger.info("Wait till login link apears");
		AppDirectUtility.waitForElementToAppear(driver, homePage.lnkLogin);
		logger.info("Clicking on login link");
		homePage.clickLoginLink();

		AppDirectLoginPage loginPage = PageFactory.initElements(driver,
				AppDirectLoginPage.class);
		logger.info("Wait till Sign up link");
		AppDirectUtility
				.waitForElementToAppear(driver, loginPage.lnksignup);
		logger.info("Click on sign up link");
		loginPage.clickSignUpLink();
		logger.info("Open sign up page");
		AppDirectSignUpPage signUpPage = PageFactory.initElements(driver,
				AppDirectSignUpPage.class);
		logger.info("Wait till email field appears");
		AppDirectUtility.waitForElementToAppear(driver,
				signUpPage.txtemailAddress);
		
    	
    }
    
    /*
     * 
     * Use this method to get list of all links on the sign up page
     * 
     */
    
 public static List<WebElement> signUpPageLinks(String param, String url) throws Exception{
    	
    	invokeDriver(param,url);
    	
    	logger.info("Open App Direct home page");
		AppDirectHomePage homePage = PageFactory.initElements(driver,
				AppDirectHomePage.class);
		logger.info("Wait till login link apears");
		AppDirectUtility.waitForElementToAppear(driver, homePage.lnkLogin);
		logger.info("Clicking on login link");
		homePage.clickLoginLink();

		AppDirectLoginPage loginPage = PageFactory.initElements(driver,
				AppDirectLoginPage.class);
		logger.info("Wait till Sign up link");
		AppDirectUtility
				.waitForElementToAppear(driver, loginPage.lnksignup);
		logger.info("Click on sign up link");
		loginPage.clickSignUpLink();
		logger.info("Open sign up page");
		AppDirectSignUpPage signUpPage = PageFactory.initElements(driver,
				AppDirectSignUpPage.class);
		logger.info("Wait till email field appears");
		AppDirectUtility.waitForElementToAppear(driver,
				signUpPage.txtemailAddress);
		List<WebElement> allLinks = signUpPage.findAllLinks();
		
		return allLinks;
    	
    }
    
    /*
     * Method to test if link is broken
     */
    
    public String testLinkBroken(URL url) throws IOException{
		
		String response = "";
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		try {
			connection.connect();
			response = connection.getResponseMessage();
			connection.disconnect();
			return response;
		}catch(Exception e){
			return e.getMessage();
		}
	}
    	
    	
    }
    
    
    


 