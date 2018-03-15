package com.tradestar.app.datascrapper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import com.tradestar.app.models.TradeEntry;
import com.tradestar.app.repository.TradeStarRepository;
import com.tradestar.app.repository.TradeStarRepositoryImpl;

@Component
public class WebScrapper {
	
	@Autowired(required = true)
	private TradeStarRepositoryImpl repository;
	
	private WebDriver driver = null;
	
	private final String scrapper_config = "./scrapper-config.yaml";
	private Map<String, Object> scrapper_config_holder;
	
	public WebScrapper() {
		
		 Yaml yaml = new Yaml();
		 try {
			 System.out.println(new File(".").getCanonicalPath());
	         InputStream ios = new FileInputStream(new File(scrapper_config));
	         // Parse the YAML file and return the output as a series of Maps and Lists
	         scrapper_config_holder = (Map<String,Object>)yaml.load(ios);
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		 String osname = System.getProperty("os.name");
		 ChromeOptions options = null;
		
		 if(osname.equals("Windows 7")){
			 System.setProperty("webdriver.chrome.driver", "C:/_Kazeon/Softwares/selenium/chromedriver_win32/chromedriver.exe");
			 driver = new ChromeDriver();
		 }
		 else if(osname.equals("Linux")){
			 // pointing out chrome driver location
			 System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
			 options = new ChromeOptions();
			 options.addArguments("headless");
	         options.addArguments("window-size=1200x600");
	         driver = new ChromeDriver(options);
		 }
	}
	
	//Scrapping the given link for data
	public void scrape(String link,double strikewidth,int lot_size,String scrip_name,Calendar cal){
		
		//driver = new ChromeDriver();
		driver.get(link);
		List<WebElement> cells = driver.findElements(By.xpath("//div[@class='opttbldata']/table/tbody/tr"));
		cells.remove(cells.size() - 1);
		
		//Getting the spot price 
		WebElement spotelement = driver.findElement(By.xpath("//*[@id='wrapper_btm']/table[1]/tbody/tr/td[2]/div/span[1]/b"));
		double spotPrice = Double.parseDouble(spotelement.getText().split(" ")[1]);
		
		List<Double> validStrikes = getStrikes(spotPrice,strikewidth);
		for(WebElement cell : cells){
			Double strike = Double.parseDouble(cell.findElement(By.xpath("./td[12]")).getText());
			if(validStrikes.contains(strike)){
				System.out.println("Strikes "+strike);
				System.out.println(cell.findElement(By.xpath("./td[6]")).getText());
				
				DateFormat format = new SimpleDateFormat("ddMMMyyyy");
				Date expiry = null;
				try {
					expiry = format.parse(getLastThursday(cal));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Creating entry for CALL option
				String option_type = "CE";
				Date date_today = Calendar.getInstance().getTime();
				
				Double premium = null;
				String tmpTxt = cell.findElement(By.xpath("./td[6]")).getText();
				if(!tmpTxt.equals("-")){
					premium = Double.parseDouble(tmpTxt.replaceAll(",", ""));
				}
				
				Double implied_volatility = null;
				tmpTxt = cell.findElement(By.xpath("./td[5]")).getText();
				if(!tmpTxt.equals("-")){
					implied_volatility = Double.parseDouble(tmpTxt.replaceAll(",", ""));
				}
				
				Long open_interest = null;
				tmpTxt = cell.findElement(By.xpath("./td[2]")).getText();
				if(!tmpTxt.equals("-")){
					open_interest = Long.parseLong(tmpTxt.replaceAll(",", ""));
				}
				
				Long change_in_oi = null;
				tmpTxt = cell.findElement(By.xpath("./td[3]")).getText();
				if(!tmpTxt.equals("-")){
					change_in_oi = Long.parseLong(tmpTxt.replaceAll(",", ""));
				}
				
				Long volume = null;
				tmpTxt = cell.findElement(By.xpath("./td[4]")).getText();
				if(!tmpTxt.equals("-")){
					volume = Long.parseLong(tmpTxt.replaceAll(",", ""));
				}
				
				TradeEntry tradeEntry = new TradeEntry(scrip_name, expiry, strike, option_type, lot_size, date_today, premium, implied_volatility, open_interest, change_in_oi, volume);
				writeToFile(tradeEntry.toString());
				saveTradeEntry(tradeEntry);
				
				// Creating entry for PUT option
				option_type = "PE";
				
				premium = null;
				tmpTxt = cell.findElement(By.xpath("./td[18]")).getText();
				if(!tmpTxt.equals("-")){
					premium = Double.parseDouble(tmpTxt.replaceAll(",", ""));
				}
				
				implied_volatility = null;
				tmpTxt = cell.findElement(By.xpath("./td[19]")).getText();
				if(!tmpTxt.equals("-")){
					implied_volatility = Double.parseDouble(tmpTxt.replaceAll(",", ""));
				}
				
				open_interest = null;
				tmpTxt = cell.findElement(By.xpath("./td[22]")).getText();
				if(!tmpTxt.equals("-")){
					open_interest = Long.parseLong(tmpTxt.replaceAll(",", ""));
				}
				
				change_in_oi = null;
				tmpTxt = cell.findElement(By.xpath("./td[21]")).getText();
				if(!tmpTxt.equals("-")){
					change_in_oi = Long.parseLong(tmpTxt.replaceAll(",", ""));
				}
				
				volume = null;
				tmpTxt = cell.findElement(By.xpath("./td[20]")).getText();
				if(!tmpTxt.equals("-")){
					volume = Long.parseLong(tmpTxt.replaceAll(",", ""));
				}
				
				tradeEntry = new TradeEntry(scrip_name, expiry, strike, option_type, lot_size, date_today, premium, implied_volatility, open_interest, change_in_oi, volume);
				writeToFile(tradeEntry.toString());
				saveTradeEntry(tradeEntry);
			}
		}
	}
	
	//This gives 10 strikes each side of spot for data parsing
	private List<Double> getStrikes(double spot,double strikewidth){
		
		double lowerStrike = spot - spot%strikewidth;
		List<Double> strikes = new ArrayList<>();
		for(double i = lowerStrike-(10*strikewidth);i<lowerStrike+(10*strikewidth);i += strikewidth){
			
			strikes.add(i);
		}
		return strikes;
	}
	
	// Getting Expiry date of the month
	public String getLastThursday(Calendar cal){
		
		int thursdayNumber = 5;
		DateFormat dateFormat = new SimpleDateFormat("ddMMMyyyy");
		//Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		int numDaysInMonth = cal.getActualMaximum(Calendar.DATE);
		Date today = cal.getTime();
		
		@SuppressWarnings("deprecation")
		int daysLeftInMonth = numDaysInMonth-today.getDate();
		int leftdaysInMonth = daysLeftInMonth%7;
		int dayOfLastDay = (dayOfWeek+leftdaysInMonth)%7;
		int daystoSubtract = 0;
		
		if(dayOfLastDay < thursdayNumber){
			daystoSubtract = (dayOfLastDay+7) - thursdayNumber;
		}
		else if(dayOfLastDay > thursdayNumber){
			daystoSubtract = dayOfLastDay - thursdayNumber;
		}
		
		cal.add(Calendar.DATE, (daysLeftInMonth - daystoSubtract));
		//System.out.println(dateFormat.format(cal.getTime()).toUpperCase());
		return dateFormat.format(cal.getTime()).toUpperCase();
	}
	
	// function to build links for option chain
	public String linkBuilder(String scripName,Calendar cal){
		
		String expiryDate = getLastThursday(cal);
		String link = "https://www.nseindia.com/live_market/dynaContent/live_watch/option_chain/optionKeys.jsp?segmentLink=17&instrument=OPTSTK&symbol="+scripName+"&date="+expiryDate;
		return link;
	}
	
	public void dataProcessor(){
		
		if(scrapper_config_holder == null){
			System.out.println("Error");
			return;
		}
		List<Object> objectList = (List<Object>) scrapper_config_holder.get("config");
		Map<String,Object> entryMap = null;
		for(Object entry : objectList){
			
			//Get the chain for current month
			Calendar cal = Calendar.getInstance();
			entryMap = (Map<String,Object>)entry;
			
			String link = linkBuilder((String)entryMap.get("scrip"),cal);
			scrape(link,(double)entryMap.get("strikewidth"),(int)entryMap.get("lot_size"),(String)entryMap.get("scrip"),cal);
		}
		driver.quit();
	}
	
	public void writeToFile(String line){
		
		FileWriter fw = null;
        try {
        	String filename= "SBIN.txt";
            fw = new FileWriter(filename,true);
            fw.write(line+"\n");
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
        	try {
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	public void saveTradeEntry(Object obj){
		
		if(repository != null){
			repository.save(obj);
		}
		else{
			System.out.println("repository is null");
			repository.save(obj);
		}
		
	}
	
	public static void main(String[] args) {
		
		/*WebScrapper scrapper = new WebScrapper();
		Calendar cal = Calendar.getInstance();
		System.out.println(scrapper.linkBuilder("VEDL", cal));*/
		
		 /*final String fileName = "C:/_Kazeon/spring-suite/workspace/TradeStar/src/scrapper-config.yaml";
		 Yaml yaml = new Yaml();
		 
		 try {
	         InputStream ios = new FileInputStream(new File(fileName));
	         
	         // Parse the YAML file and return the output as a series of Maps and Lists
	         Map<String,Object> result = (Map<String,Object>)yaml.load(ios);
	         List<Object> objectList = (List<Object>) result.get("config");
	         Map<String,Object> res = null;
	         for(Object entry: objectList){
	        	 res = (Map<String,Object>)entry;
	        	 System.out.println(res.get("scrip"));
	         }
	         
	      } catch (Exception e) {
	         e.printStackTrace();
	      }*/
		
		WebScrapper scrapper = new WebScrapper();
		scrapper.dataProcessor();
		
		
	}
	

}
