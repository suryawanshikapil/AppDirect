package com.appdirect.qe.appdirectintegration.tests;

import java.io.IOException;

import org.testng.annotations.Test;
import org.testng.AssertJUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
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


public class SignUpPositive {
	WebDriver driver;
	private String homepage;
	private String confirmationMessage;
	private String sTestCaseName;
	private int iTestCaseRow;
	private String testDataPath;
	private String testCasesPath;
	private String browser;
	static AppDirectSignUpPage signUpPage;
	private static Logger logger = Logger.getLogger(new Exception().getStackTrace()[0].getClassName());

	@DataProvider(name = "AppDirectTestData")
	public Object[][] TestData() throws Exception {
		logger.info("Setting up the test data file");
		AppDirectUtility util = new AppDirectUtility();
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
		confirmationMessage = util.getPropValues("config.properties","confirmationMessage");
		logger.info("Writing new email to dataprovider");
		AppDirectExcelUtils.writeEmail(testDataPath);
		driver = AppDirectUtility.invokeDriver(browser,homepage);
		signUpPage = PageFactory.initElements(driver, AppDirectSignUpPage.class);

	}
	@Parameters("browser")
	@Test(testName ="SignUpPositive", dataProvider = "AppDirectTestData",priority=1)
	public void testAppDirectSignUp(String TestCaseId, String TestCaseName,	String Key, String email) throws Exception {
		try {
			
			logger.info("Navigate to sign up page");
			AppDirectUtility.navigateToSignUpPage(driver);
			signUpPage.typeEmail(email);
			signUpPage.clickSignUpbtn();
			logger.info("Wait till confirmation message appear");
			AppDirectUtility.waitForElementToAppear(driver,	signUpPage.signupConfirmationPanel);
			logger.info("Getting confirmation text from confirmation panel");
			String actualConfirmationMsg = signUpPage.getConfirmationPanelText();
			confirmationMessage = AppDirectUtility.getFormatedMessage("config.properties","confirmationMessage" , email);
			logger.info(actualConfirmationMsg);
			Assert.assertEquals(actualConfirmationMsg, confirmationMessage);
			logger.info("Updating the test result PASS in test case file");
			AppDirectExcelUtils.updateTestResult(testCasesPath,AppDirectExcelUtils.ResultType.PASS,TestCaseId);

		} catch (AssertionError e) {
			logger.info("Failing the test case");
			Assert.assertTrue(false);
			logger.info("Updating the test result FAIL in test case file");
			AppDirectExcelUtils.updateTestResult(testCasesPath,AppDirectExcelUtils.ResultType.FAIL,TestCaseId);

		} catch (Exception e) {
			logger.info("Failing the test case");
			logger.info("Caught exception e.g ElementNotFound");
			Assert.assertTrue(false);
			logger.info("Updating the test result FAIL in test case file");
			AppDirectExcelUtils.updateTestResult(testCasesPath,AppDirectExcelUtils.ResultType.FAIL,TestCaseId);
		}

	}



	@AfterMethod
	public void teardown() {
		driver.quit();
	}

}