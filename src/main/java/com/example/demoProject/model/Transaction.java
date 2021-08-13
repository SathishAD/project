package com.example.demoProject.model;



import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@NamedQueries({
    @NamedQuery(name = "Transaction.findAll", query = "select t from Transaction t")
})

@Entity
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long transactionId;
	private String date;
	private String deposits;
	private String withdrawals;
	private String balance;
	

	private String description;
	public String getDeposits() {
		return deposits;
	}
	public void setDeposits(String deposits) {
		this.deposits = deposits;
	}
	public String getWithdrawals() {
		return withdrawals;
	}
	public void setWithdrawals(String withdrawals) {
		this.withdrawals = withdrawals;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}
