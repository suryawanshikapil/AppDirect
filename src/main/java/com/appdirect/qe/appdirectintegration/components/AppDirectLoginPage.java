package com.appdirect.qe.appdirectintegration.components;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AppDirectLoginPage {
	
	
	
	@FindBy(id="username")
	public WebElement txtemail;
	
	@FindBy(id="password")
	public WebElement txtpassword;
	
	@FindBy(xpath="//a[contains(@href, './forgotPassword')]")
	public WebElement lnkforgotpassword;
	
	@FindBy(id="rememberMe")
	public WebElement chkrememberMe;
	
	@FindBy(xpath="//button[@name='signin']")
	public WebElement btnsignin;
	
	@FindBy(xpath="//a[contains(@href, './signup')]")
	public WebElement lnksignup;
	
	@FindBy(id="yahooLoginButton")
	public WebElement btnYahoo;
	
	
	public void clickSignUpLink () {
		lnksignup.click();
	}

   
    
    
}
