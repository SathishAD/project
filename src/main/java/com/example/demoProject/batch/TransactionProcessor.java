package com.example.demoProject.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;

@Component("transactionProcessor")
public class TransactionProcessor implements ItemProcessor<Transaction,Transaction> {

	@Override
	public Transaction process(Transaction item) throws Exception {
		// TODO Auto-generated method stub
		item.setDescription(item.getDescription().toUpperCase());
		return item;
	}

}
