package com.example.demoProject.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.demoProject.model.Transaction;

@Component
@Repository

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	//Page<Transaction> findAll(Pageable pageable);
}
