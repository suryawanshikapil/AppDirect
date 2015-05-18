package com.appdirect.qe.appdirectintegration.components;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class AppDirectHomePage {
	
	
	@FindBy(xpath="//a[contains(@href, 'https://www.appdirect.com/login')]")
	public WebElement lnkLogin;
	

    public void clickLoginLink( ){
    	
    	lnkLogin.click();
    	
    	
    }
    
   
    
}
