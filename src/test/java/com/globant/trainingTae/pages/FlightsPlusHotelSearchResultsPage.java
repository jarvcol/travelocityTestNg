package com.globant.trainingTae.pages;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.StaleElementReferenceException;

public class FlightsPlusHotelSearchResultsPage extends BasePage {

	public FlightsPlusHotelSearchResultsPage(WebDriver pDriver) {
		//Add 10 more seconds cause this page option tends to be so painful slow
		super(pDriver,20);
		getWait().until(ExpectedConditions.elementToBeClickable(priceSortButton));
	}
	
	//Locators......!!!!!!!!!!!!!!!!!
	
	@FindBy(css="button[data-opt-group='Price']")
	private WebElement priceSortButton;
	
	@FindBy(id="resultsContainer")
	private WebElement packageResults;
	
	@FindBy(id="hotelResultTitle")
	private WebElement packageResultHeader;
	
	@FindBy(id="googleMapContainer")
	private WebElement googleMapContainer;
	
	@FindBy(id="multiStepIndicatorContainer")
	private WebElement pkgStepsIndicator;
	
	@FindBy(id="sortContainer")
	private WebElement sortOptionsContainer;
		
		
	//Methods......!!!!!!!!!!!!!!!!!

	public void sortByOption(String sortOption){
		switch (sortOption) {
		case "Price":
			priceSortButton.click();
			break;
		default:
			break;
		}
		getWait().until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("article"))));
	}
	
	public BookRoomsSelectionPage selectPackageBy(String sortOption, String value){
		List<WebElement> resultsList = getDriver().findElements(By.tagName("article"));
		switch (sortOption) {
		case "Stars":
			return selectPackageByStars(resultsList,value);
		default:
			return null;
		}
	}
	
	public BookRoomsSelectionPage selectPackageByStars(List<WebElement> resultsList,String starsNumber){
		WebElement testElement; double starsHotelValue;
		for (WebElement element: resultsList) {
			getWait().until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOf(element)));
			testElement = element.findElement(By.xpath("//*[@class='icon icon-stars3-5 stars-grey value-title']"));
			starsHotelValue = Double.parseDouble(testElement.getAttribute("title"));
			if(starsHotelValue >= Double.parseDouble(starsNumber)){
				testElement.click();
				return new BookRoomsSelectionPage(getDriver());
			}	
		}
		//If the original list of hotels does not have 3 stars, but there are more options
		if(getDriver().findElement(By.xpath("button[@class='pagination-next']")) != null){
			getDriver().findElement(By.xpath("button[@class='pagination-next']")).click();
			getWait().until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.tagName("article"))));
			return selectPackageByStars(getDriver().findElements(By.tagName("article")),starsNumber);
		}
		//Default in case hotel match, selects first element
		resultsList.get(0).click();
		return new BookRoomsSelectionPage(getDriver()); 
	}
	
	
	//Test and assertion methods
		public boolean validateSortOfResults(String sortType){
			switch (sortType) {
			case "Price":
				List<WebElement> resultsList = getDriver().findElements(By.cssSelector(".actualPrice.price.fakeLink"));
				return validateSortByPrice(resultsList,0);
			default:
				return false;
			}
		}
		
		private boolean validateSortByPrice(List<WebElement> resultsList, double prevValue){
			double currValue=0;
			for (WebElement element: resultsList) {
				if(!(prevValue == 0)){
					currValue = Double.parseDouble(element.getText().replaceAll("\\D", ""));
					if(prevValue>currValue){
						return false;
					}
					prevValue = currValue;
				}else{
					prevValue = Double.parseDouble(element.getText().replaceAll("\\D", ""));
				}
			}
			return true;
		}
		
	public String getdataToValidateNextPage(){
		String hotelName, stars, price;
		hotelName = getDriver().findElement(By.cssSelector(".flex-card.visited-hotel .hotelName.fakeLink")).getText();
		stars = getDriver().findElement(By.cssSelector(".flex-card.visited-hotel .icon.stars-grey")).getAttribute("title");
		price = getDriver().findElement(By.cssSelector(".flex-card.visited-hotel .actualPrice.price.fakeLink ")).getText().replaceAll("\\D", "");
		return hotelName+"|"+stars+"|"+price;
	}
	
	//Getters
	
	public WebElement getPackageResultHeader() {
		return packageResultHeader;
	}

	public WebElement getPackageResults() {
		return packageResults;
	}

	public WebElement getGoogleMapContainer() {
		return googleMapContainer;
	}
	
	public WebElement getPkgStepIndicator() {
		return pkgStepsIndicator;
	}
	
	public WebElement getSortOptionsCont() {
		return sortOptionsContainer;
	}
}
