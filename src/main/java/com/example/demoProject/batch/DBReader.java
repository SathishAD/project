package com.example.demoProject.batch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;
import com.example.demoProject.repository.TransactionRepository;

@Component("dbReader")
public class DBReader implements ItemReader<Transaction> {

	@Autowired
	TransactionRepository transactionRepo;
	
	private int count = 0;
	@Override
	public Transaction read()
			throws Exception {
		
		List<Transaction> list = new ArrayList<Transaction>();
	    list.addAll(transactionRepo.findAll());

		while (count < list.size()) {
			return list.get(count++);
		}
		return null;

	}
}
