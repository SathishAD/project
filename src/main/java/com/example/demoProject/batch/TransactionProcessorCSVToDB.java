package com.example.demoProject.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;

@Component("itemProcessorCSVToDB")
public class TransactionProcessorCSVToDB implements ItemProcessor<Transaction,Transaction> {
	
	
	Logger logger=LoggerFactory.getLogger(TransactionProcessorCSVToDB.class);
	@Override
	public Transaction process(Transaction item) throws Exception {
		
		item.setDescription(item.getDescription().toUpperCase());
		logger.info("The description is converted into uppercase. "+item.getDescription());
		return item;
		
	}

}
