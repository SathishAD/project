package com.example.demoProject.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;

@Component("itemProcessorDBToCSV")
public class TransactionProcessorDBToCSV implements ItemProcessor<Transaction,Transaction> {

	@Override
	public Transaction process(Transaction item) throws Exception {
		
		item.setDescription(item.getDescription().toLowerCase());
		return item;
	}

}
