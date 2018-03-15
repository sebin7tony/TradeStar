package com.tradestar.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tradestar.app.datascrapper.TestJavaUnix;
import com.tradestar.app.datascrapper.WebScrapper;

//import com.tradestar.app.datascrapper.WebScrapper;

@RestController
@RequestMapping("/rest/api/tradestar")
public class TradeController {
	
	@Autowired
	public WebScrapper scrapper;

	@RequestMapping(value="/scrape",method = RequestMethod.GET)
	public void luanchScrapper(){
		
		scrapper.dataProcessor();
	}
	
	@RequestMapping(value="/scrapetes",method = RequestMethod.GET)
	public void testscrap(){
		
		TestJavaUnix.scrape();
	}
	
	public static void main(String[] args) {
		
		TradeController controller = new TradeController();
		controller.luanchScrapper();
	}
}
