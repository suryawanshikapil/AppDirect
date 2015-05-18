package com.appdirect.qe.appdirectintegration.tests;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.appdirect.qe.appdirectintegration.components.AppDirectSignUpPage;
import com.appdirect.qe.appdirectintegration.utilities.AppDirectExcelUtils;
import com.appdirect.qe.appdirectintegration.utilities.AppDirectUtility;
import com.thoughtworks.selenium.SeleniumException;


public class SignUpNegative {
	
	private String homepage;
	private String existingEmailError;
	private String sTestCaseName;
	private String testDataPath;
	private String testCasesPath;
	private int iTestCaseRow;
	static WebDriver driver;
	static AppDirectSignUpPage signUpPage;
	private static Logger logger = Logger.getLogger(new Exception()
			.getStackTrace()[0].getClassName());
	private Object[][] testObjArray = null;

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

	@Parameters("browser")
	@BeforeMethod
	public void setUp(String browser) throws IOException {
		AppDirectUtility util = new AppDirectUtility();
		homepage = util.getPropValues("config.properties", "HOMEPAGE");
		existingEmailError = util.getPropValues("config.properties","existingEmailError");
		logger.info("Writing new email to dataprovider");
		AppDirectExcelUtils.writeEmail(testDataPath);
		driver = AppDirectUtility.invokeDriver(browser,homepage);
		signUpPage = PageFactory.initElements(driver, AppDirectSignUpPage.class);
		
	}

	

	@Test(testName = "SignUpNegative", dataProvider = "AppDirectTestData", priority=2)
	public void testErrorScenarios(String TestCaseId, String TestCaseName,String Key, String email) throws Exception {
		try{
		logger.info("Navigate to sign up page");
		AppDirectUtility.navigateToSignUpPage(driver);
		signUpPage.typeEmail(email);
		signUpPage.clickSignUpbtn();
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		if(Key.equals("Existing")){
		String errorMsg =signUpPage.errorForExistingEmail(driver);
		errorMsg = errorMsg.trim();
		existingEmailError=existingEmailError.trim();
		logger.info("Checking error message on signup page");
		logger.info(existingEmailError);
		logger.info(errorMsg);
		Assert.assertEquals(errorMsg, existingEmailError);
		
		logger.info("Updating the test result PASS in test case file");
		AppDirectExcelUtils.updateTestResult(testCasesPath,AppDirectExcelUtils.ResultType.PASS,TestCaseId);
		
		} else {
			Boolean isConfirmPanelPrsent=signUpPage.validateConfirmationPanelPresence();
			if(!isConfirmPanelPrsent){
			AppDirectExcelUtils.updateTestResult(testCasesPath,AppDirectExcelUtils.ResultType.PASS,TestCaseId);
			} else {
				AppDirectExcelUtils.updateTestResult(testCasesPath,AppDirectExcelUtils.ResultType.FAIL,TestCaseId);
			}
		}
		

	} catch (AssertionError e) {
		logger.info("Failing the test case");
		Assert.assertTrue(false);
		logger.info("Updating the test result FAIL in test case file");
		AppDirectExcelUtils.updateTestResult(testCasesPath,AppDirectExcelUtils.ResultType.FAIL,TestCaseId);

	} catch (Exception e) {
		logger.info("Failing the test case");
		Assert.assertTrue(false);
		logger.info("Caught exception e.g ElementNotFound");
		logger.info("Updating the test result FAIL in test case file");
		AppDirectExcelUtils.updateTestResult(testCasesPath,	AppDirectExcelUtils.ResultType.FAIL,TestCaseId);
	}


	}


	
	
	@AfterMethod
	public void teardown() {
		driver.quit();
	}

}