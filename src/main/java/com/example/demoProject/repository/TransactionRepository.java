package com.example.demoProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demoProject.model.Transaction;

@Component
@Repository

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
