package com.example.demoProject.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;

@Component("itemProcessorCSVToDB")
public class TransactionProcessorCSVToDB implements ItemProcessor<Transaction,Transaction> {

	@Override
	public Transaction process(Transaction item) throws Exception {
		
		item.setDescription(item.getDescription().toUpperCase());
		return item;
	}

}
