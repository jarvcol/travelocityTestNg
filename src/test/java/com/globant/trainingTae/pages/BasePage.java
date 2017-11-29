package com.globant.trainingTae.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class BasePage {

	private WebDriver driver;
	private WebDriverWait wait;
	
	public BasePage(WebDriver pDriver){
		PageFactory.initElements(pDriver, this);
		wait = new WebDriverWait(pDriver, 10);
		driver = pDriver;
	}
	
	public BasePage(WebDriver pDriver, int timeOutInSec){
		PageFactory.initElements(pDriver, this);
		wait = new WebDriverWait(pDriver, timeOutInSec);
		driver = pDriver;
	}
	
	public WebDriverWait getWait(){
		return wait;
	}
	
	public WebDriver getDriver(){
		return driver;
	}
	
	public void dispose(){
		if (driver != null){
			driver.quit();
		}
	}
}
