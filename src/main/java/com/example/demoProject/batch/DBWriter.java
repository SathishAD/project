package com.example.demoProject.batch;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;
import com.example.demoProject.repository.TransactionRepository;

@Component("dbWriter")
public class DBWriter implements ItemWriter<Transaction> {

	@Autowired
	TransactionRepository transactionRepo;

	@Override
	public void write(List<? extends Transaction> items) throws Exception {
		
		transactionRepo.saveAll(items);
		
	}

}
