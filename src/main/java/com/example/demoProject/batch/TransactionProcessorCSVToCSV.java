package com.example.demoProject.batch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;


@Component("itemProcessorCSVToCSV")
public class TransactionProcessorCSVToCSV implements ItemProcessor<Transaction,Transaction>  {

	@Override
	public Transaction process(Transaction item) throws Exception {
		
		DateFormat df1 = new SimpleDateFormat("dd-MMM-yy");
		DateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
		String inputDate = item.getDate();
		Date d = df1.parse(inputDate);
		String outputDate = df2.format(d);
		item.setDate(outputDate);
		return item;
	}
	
	

}
