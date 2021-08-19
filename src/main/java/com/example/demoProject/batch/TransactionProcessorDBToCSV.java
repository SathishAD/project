package com.example.demoProject.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;

@Component("itemProcessorDBToCSV")
public class TransactionProcessorDBToCSV implements ItemProcessor<Transaction,Transaction> {

	Logger logger=LoggerFactory.getLogger(TransactionProcessorDBToCSV.class);
	
	@Override
	public Transaction process(Transaction item) throws Exception {
		
		
		item.setDescription(item.getDescription().toLowerCase());
		logger.info("description is converted to lowercase. "+item.getDescription());
		return item;
	}

}
