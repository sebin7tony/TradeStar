package com.tradestar.app.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class TradeEntry {
	
	@Id
	private String id;
	private String scrip_name;
	private Date expiry;
	private Double strike;
	private String option_type;
	//private String symbol;
	private Integer lot_size;
	private Date date_today;
	private Double premium;
	private Double implied_volatility;//in percentage
	private Long open_interest;
	private Long change_in_oi;
	private Long volume;
	private Double greek_delta;
	private Double greek_gamma;
	private Double greek_theta;
	private Double greek_vega;
	
	
	public TradeEntry(String scrip_name, Date expiry, Double strike, String option_type, int lot_size, Date date_today,
			Double premium, Double implied_volatility, Long open_interest, Long change_in_oi, Long volume) {
		super();
		this.scrip_name = scrip_name;
		this.expiry = expiry;
		this.strike = strike;
		this.option_type = option_type;
		this.lot_size = lot_size;
		this.date_today = date_today;
		this.premium = premium;
		this.implied_volatility = implied_volatility;
		this.open_interest = open_interest;
		this.change_in_oi = change_in_oi;
		this.volume = volume;
		
		//Setting symbol
		
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getScrip_name() {
		return scrip_name;
	}
	public void setScrip_name(String scrip_name) {
		this.scrip_name = scrip_name;
	}
	public Date getExpiry() {
		return expiry;
	}
	public void setExpiry(Date expiry) {
		this.expiry = expiry;
	}
	public double getStrike() {
		return strike;
	}
	public void setStrike(double strike) {
		this.strike = strike;
	}
	public String getOption_type() {
		return option_type;
	}
	public void setOption_type(String option_type) {
		this.option_type = option_type;
	}
	
	public int getLot_size() {
		return lot_size;
	}
	public void setLot_size(int lot_size) {
		this.lot_size = lot_size;
	}
	public Date getDate_today() {
		return date_today;
	}
	public void setDate_today(Date date_today) {
		this.date_today = date_today;
	}
	public double getPremium() {
		return premium;
	}
	public void setPremium(double premium) {
		this.premium = premium;
	}
	public double getImplied_volatility() {
		return implied_volatility;
	}
	public void setImplied_volatility(double implied_volatility) {
		this.implied_volatility = implied_volatility;
	}
	public long getOpen_interest() {
		return open_interest;
	}
	public void setOpen_interest(long open_interest) {
		this.open_interest = open_interest;
	}
	public long getChange_in_oi() {
		return change_in_oi;
	}
	public void setChange_in_oi(long change_in_oi) {
		this.change_in_oi = change_in_oi;
	}
	public long getVolume() {
		return volume;
	}
	public void setVolume(long volume) {
		this.volume = volume;
	}
	public double getGreek_delta() {
		return greek_delta;
	}
	public void setGreek_delta(double greek_delta) {
		this.greek_delta = greek_delta;
	}
	public double getGreek_gamma() {
		return greek_gamma;
	}
	public void setGreek_gamma(double greek_gamma) {
		this.greek_gamma = greek_gamma;
	}
	public double getGreek_theta() {
		return greek_theta;
	}
	public void setGreek_theta(double greek_theta) {
		this.greek_theta = greek_theta;
	}
	public double getGreek_vega() {
		return greek_vega;
	}
	public void setGreek_vega(double greek_vega) {
		this.greek_vega = greek_vega;
	}
	
	// Calculate and set the greeks
	public void calculateGreeks(){
		
		this.greek_delta = null;
		this.greek_gamma = null;
		this.greek_theta = null;
		this.greek_vega = null;
	}
	
	// Checks if all the properties are got filled
	public Boolean isFullyFilled(){
		
		if(this.scrip_name != null && this.expiry != null && this.strike != null && this.option_type != null &&
				this.lot_size != 0 && this.date_today != null && this.premium != null && this.implied_volatility != null &&
				this.open_interest != null && this.change_in_oi != null && this.volume != null && this.greek_delta != null &&
				this.greek_gamma != null && this.greek_theta != null && this.greek_vega != null){
			
			return true;
		}
		return false;
	}
	
	// Checks if all the properties except greeks are got filled. Call this for validity before calculate greeks.
	public Boolean isPartiallyFilled(){
		
		if(this.scrip_name != null && this.expiry != null && this.strike != null && this.option_type != null &&
				this.lot_size != 0 && this.date_today != null && this.premium != null && this.implied_volatility != null &&
				this.open_interest != null && this.change_in_oi != null && this.volume != null){
			
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		
		return "scrip_name "+this.scrip_name+" expiry "+this.expiry+" Strike "+this.strike+
				" option_type "+this.option_type+" lotsize "+this.lot_size+" date-today "+this.date_today+
				" premium "+this.premium+ " iv "+this.implied_volatility+" OI "+this.open_interest+
				"change in oi "+this.change_in_oi+" volume "+this.volume;
		}


}
