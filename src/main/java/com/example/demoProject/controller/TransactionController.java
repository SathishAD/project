package com.example.demoProject.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {
	
	@Autowired
	@Qualifier("jobCSVToDB")
	Job jobCSVToDB;
	
	@Autowired
	@Qualifier("jobDBToCSV")
	Job jobDBToCSV;
	
	@Autowired
	@Qualifier("jobCSVToCSV")
	Job jobCSVToCSV;
	
	@Autowired
	JobLauncher jobLauncher;
	
	@RequestMapping("/csvtodb")
	public String csvtodb() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
		
		Logger logger=LoggerFactory.getLogger(TransactionController.class);
		
		logger.info("BATCH JOB FOR CSV TO DB ");
		Map<String,JobParameter> maps=new HashMap<>();
		maps.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobparameters=new JobParameters(maps);
		JobExecution jobCSVToDBExecution=jobLauncher.run(jobCSVToDB, jobparameters);
		if (jobCSVToDBExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("BATCH JOB FOR CSV TO DB COMPLETED SUCCESSFULLY");
        }
		
		return "job executed successfully";
	}
	@RequestMapping("/dbtocsv")
	public String dbtocsv() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
		
		Logger logger=LoggerFactory.getLogger(TransactionController.class);
		
		logger.info("BATCH JOB FOR DB TO CSV ");
		Map<String,JobParameter> maps1=new HashMap<>();
		maps1.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobparameters1=new JobParameters(maps1);
		JobExecution jobDBToCSVExecution=jobLauncher.run(jobDBToCSV, jobparameters1);
		if (jobDBToCSVExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("BATCH JOB FOR DB TO CSV COMPLETED SUCCESSFULLY");
        }

		return "job executed successfully";
	}
	@RequestMapping("/csvtocsv")
	public String csvtocsv() throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException{
		
		Logger logger=LoggerFactory.getLogger(TransactionController.class);
		
		logger.info("BATCH JOB FOR CSV TO CSV");
		Map<String,JobParameter> maps2=new HashMap<>();
		maps2.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobparameters2=new JobParameters(maps2);
		JobExecution jobCSVToCSVExecution=jobLauncher.run(jobCSVToCSV, jobparameters2);
		if (jobCSVToCSVExecution.getStatus() == BatchStatus.COMPLETED) {
            logger.info("BATCH JOB FOR CSV TO CSV COMPLETED SUCCESSFULLY");
        }
		
		return "job executed successfully";
	}
}
