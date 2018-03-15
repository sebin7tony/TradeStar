package com.tradestar.app.datascrapper;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.yaml.snakeyaml.Yaml;

public class WebScrapperTest {
	
	private WebDriver driver;
	private double spot = 242.0;
	private double strikewidth = 5.0;
	
	public List<Double> getStikes(double spot,double strikewidth){
		
		double lowerStrike = spot - spot%strikewidth;
		List<Double> strikes = new ArrayList<>();
		for(double i = lowerStrike-(5*strikewidth);i<lowerStrike+(5*strikewidth);i += strikewidth){
			
			strikes.add(i);
		}
		return strikes;
	}
	
	public void test(){
		System.setProperty("webdriver.chrome.driver", "C:/_Kazeon/Softwares/selenium/chromedriver_win32/chromedriver.exe");
		driver = new ChromeDriver();
		driver.get("https://www.nseindia.com/live_market/dynaContent/live_watch/option_chain/optionKeys.jsp?segmentLink=17&instrument=OPTSTK&symbol=VEDL&date=25MAY2017");
		List<WebElement> cells = driver.findElements(By.xpath("//div[@class='opttbldata']/table/tbody/tr"));
		System.out.println("hello "+cells.size());
		/*String strike = cells.get(20).findElement(By.xpath("./td[12]")).getText();
		System.out.println("Strike "+strike);*/
		cells.remove(cells.size() - 1);
		List<Double> strikes = getStikes(spot,strikewidth);
		List<Double> checkedStrike = new ArrayList<>();
		for(WebElement cell : cells){
			String strike = cell.findElement(By.xpath("./td[12]")).getText();
			if(strikes.contains(Double.parseDouble(strike))){
				checkedStrike.add(Double.parseDouble(strike));
			}
		}
		System.out.println(strikes.toString());
		System.out.println(checkedStrike.toString());
		driver.quit();
	}

	public static void main(String args[]){
		
		WebScrapperTest scrapper = new WebScrapperTest();
		scrapper.test();
		Yaml yaml = new Yaml();
		

	}
}
