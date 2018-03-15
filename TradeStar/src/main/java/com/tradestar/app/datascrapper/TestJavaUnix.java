package com.tradestar.app.datascrapper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestJavaUnix {
	
	public static void scrape(){
		
		String link = "http://settri.blogspot.in";
		String osname = System.getProperty("os.name");
		System.out.println(osname);
		System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("headless");
        	options.addArguments("window-size=1200x600");		
		WebDriver driver = new ChromeDriver(options);
		driver.get(link);
		System.out.println("Opened the broswer");
		String header = driver.findElement(By.xpath("//*[@id='header-inner']/div[1]/h1")).getText();
		System.out.println("header "+header);
		driver.close();
	}
	
	public static void main(String[] args) {
		TestJavaUnix.scrape();
	}
}
