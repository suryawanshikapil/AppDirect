package com.appdirect.qe.appdirectintegration.components;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AppDirectSignUpPage {
	
	@FindBy(name="emailAddress")
	public WebElement txtemailAddress;
	
	@FindBy(xpath="//button[@name='userSignupButton']")
	public WebElement btnsignup;
	
	@FindBy(css="span.idp-text")
	public WebElement lnkyahoosignup;
	
	@FindBy(linkText="Log In")
	public WebElement lnkLogin;
	
	@FindBy(css="#partnerRegisterLink > a")
	public WebElement lnkdeveloperRegister;
	
	@FindBy(xpath="//div[@class='round-white-cont confirm']")
    public WebElement signupConfirmationPanel;
	
	@FindBy(tagName="a")
	public List<WebElement> linkswithAnchorTag;
	
	
	
	public void typeEmail(String email){
		txtemailAddress.sendKeys(email);
	}
	
	public String getConfirmationPanelText(){
		return signupConfirmationPanel.getText();
	}
	
	public void clickSignUpbtn(){
		btnsignup.click();
	}
	
	
	
	
	 /*
	   *Use this method to get all the links on signup page 
	   */
	 public List<WebElement> findAllLinks(){
		 List<WebElement> elementList=new ArrayList<WebElement>();
		  for (WebElement element : linkswithAnchorTag)
		  {
			  if(element.getAttribute("href") != null && !element.getAttribute("href").contains("mailto"))
			  {
				  elementList.add(element);
			  }		  
		  }	
		  return elementList;
	  }
	 
	 
/*
 * Use this method to check presence of confirmation panel
 */
	 
	 public boolean validateConfirmationPanelPresence(){
		 try{
			 signupConfirmationPanel
						.isDisplayed();
			 return true;
				
				} catch (NoSuchElementException e){
				
					return false;
				}
	 }
	 
	 
/*
 * Use this method to check error for existing email address
 * 
 */
	 public String errorForExistingEmail(WebDriver driver){
		 	JavascriptExecutor executor = (JavascriptExecutor)driver;
//		 	 WebElement element =  (WebElement) executor.executeScript("return document.getElementsByClassName('feedback-error');");
		 	String id = driver.findElement(By.cssSelector(".feedback-error")).getAttribute("id");
		 	WebElement element =  (WebElement) executor.executeScript("return document.getElementById('"+id+"');");
//			  WebElement element =  (WebElement) executor.executeScript("return document.getElementById('id6');");
			 String errorMsg = (String) executor.executeScript("return arguments[0].textContent", element);
			 return errorMsg;

	 }
	 
	 
		  
}
    

