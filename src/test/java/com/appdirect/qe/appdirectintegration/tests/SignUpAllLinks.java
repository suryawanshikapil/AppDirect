package com.appdirect.qe.appdirectintegration.tests;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import junit.framework.Assert;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.appdirect.qe.appdirectintegration.components.AppDirectSignUpPage;
import com.appdirect.qe.appdirectintegration.utilities.AppDirectExcelUtils;
import com.appdirect.qe.appdirectintegration.utilities.AppDirectUtility;

public class SignUpAllLinks {
	
	private static Logger logger = Logger.getLogger(new Exception()
	.getStackTrace()[0].getClassName());
	static WebDriver driver;
	static AppDirectSignUpPage signUpPage;
	private static String testCasesPath;
	private String homepage;
	private String existingEmailError;
	private String sTestCaseName;
	private String testDataPath;
	private int iTestCaseRow;
	
	@Parameters("browser")
	@BeforeMethod
	public void setUp(String browser) throws IOException {
		AppDirectUtility util = new AppDirectUtility();
		homepage = util.getPropValues("config.properties", "HOMEPAGE");
		driver = AppDirectUtility.invokeDriver(browser,homepage);
		signUpPage = PageFactory.initElements(driver, AppDirectSignUpPage.class);
	}
	
	@DataProvider(name = "AppDirectTestData")
	public Object[][] TestData() throws Exception {
		AppDirectUtility util = new AppDirectUtility();
		logger.info("Setting up the test data file");
		testDataPath = util.getPropValues("config.properties", "testDataPath");
		testCasesPath = util.getPropValues("config.properties", "testCasesPath");
		AppDirectExcelUtils.setExcelFile(testDataPath,"TestCase_Data");
		sTestCaseName = this.toString();
		logger.info("Getting test case name ");
		sTestCaseName = AppDirectExcelUtils.getTestCaseName(this.toString());
		logger.info("Getting test case row number");
		iTestCaseRow = AppDirectExcelUtils.getRowContains(sTestCaseName, 1);
		Object[][] testObjArray = AppDirectExcelUtils.getTableArray(testDataPath,0,sTestCaseName,iTestCaseRow);
		return (testObjArray);

	}
	
	@Test(testName ="SignUpPositive", dataProvider = "AppDirectTestData",priority=3)
	public static void testSignupPageLinks(String TestCaseId, String TestCaseName,	String Key, String email) throws Exception{
		logger.info("Navigate to sign up page");
		AppDirectUtility.navigateToSignUpPage(driver);
		List<WebElement>allLinks = signUpPage.findAllLinks();
		AppDirectUtility utility = new AppDirectUtility();
		
		try{
		for(WebElement element: allLinks){
			try{
				logger.info("URL: " + element.getAttribute("href")+ " returned resonse " + utility.testLinkBroken((new URL(element.getAttribute("href")))));

			}
			
			catch(Exception e){
				logger.info("At " + element.getAttribute("innerHTML") + " Exception occured -&gt; " + e.getMessage());

			}
		
			

			}
		logger.info("Updating the test result PASS in test case file");
		AppDirectExcelUtils.updateTestResult(testCasesPath,AppDirectExcelUtils.ResultType.PASS,TestCaseId);
		
		
	} catch(Exception e){
		logger.info(e);
		logger.info("Updating the test result FAIL in test case file");
		AppDirectExcelUtils.updateTestResult(testCasesPath,AppDirectExcelUtils.ResultType.FAIL,TestCaseId);
	}
}
		
		
		
		
		
	
	
	@AfterMethod
	public void teardown() {
		driver.quit();
	}
}
