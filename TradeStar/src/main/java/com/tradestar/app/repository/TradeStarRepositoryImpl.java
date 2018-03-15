package com.tradestar.app.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TradeStarRepositoryImpl implements TradeStarRepository{
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public void save(Object objTosave) {
		
		mongoTemplate.save(objTosave);
	}

}
