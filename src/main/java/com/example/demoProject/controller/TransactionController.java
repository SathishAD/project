package com.example.demoProject.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
	
	@Autowired
	Job jobCSVToDB;
	
	@Autowired
	Job jobDBToCSV;
	
	@Autowired
	Job jobCSVToCSV;
	
	@Autowired
	JobLauncher jobLauncher;
	
	@RequestMapping("/load")
	public String load() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
		
		System.out.println("In controller");
		
		Map<String,JobParameter> maps=new HashMap<>();
		maps.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobparameters=new JobParameters(maps);
		jobLauncher.run(jobCSVToDB, jobparameters);
		
		Map<String,JobParameter> maps1=new HashMap<>();
		maps1.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobparameters1=new JobParameters(maps1);
		jobLauncher.run(jobDBToCSV, jobparameters1);
		
		Map<String,JobParameter> maps2=new HashMap<>();
		maps2.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobparameters2=new JobParameters(maps2);
		jobLauncher.run(jobCSVToCSV, jobparameters2);
		return "job executed successfully";
	}
	
}
