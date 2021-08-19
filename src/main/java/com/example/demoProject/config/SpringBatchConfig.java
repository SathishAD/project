package com.example.demoProject.config;

import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;

@Component
@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	Logger logger=LoggerFactory.getLogger(SpringBatchConfig.class);
	
	@Autowired
	@Qualifier("csvReader")
	ItemReader<Transaction> csvReader;
	
	@Autowired
	@Qualifier("dbWriter")
	ItemWriter<Transaction>dbWriter;
	
	
	@Autowired
	@Qualifier("itemProcessorCSVToDB")
	ItemProcessor<Transaction,Transaction> itemProcessorCSVToDB;
	
	@Autowired
	@Qualifier("dbReader")
	ItemReader<Transaction> dbReader;
	
	@Autowired
	@Qualifier("csvWriter")
	ItemWriter<Transaction> csvWriter;
	
	
	@Autowired
	@Qualifier("itemProcessorDBToCSV")
	ItemProcessor<Transaction,Transaction> itemProcessorDBToCSV;
	
	@Autowired
	@Qualifier("csvReaderCSVToCSV")
	ItemReader<Transaction> csvReaderCSVToCSV;
	
	
	@Autowired
	@Qualifier("csvWriterCSVToCSV")
	ItemWriter<Transaction> csvWriterCSVToCSV;
	
	@Autowired
	@Qualifier("itemProcessorCSVToCSV")
	ItemProcessor<Transaction,Transaction> itemProcessorCSVToCSV;
	
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	private Resource output_db_to_csv = new FileSystemResource("./src/main/resources/output_db_to_csv.csv");
	private Resource output_csv_to_csv = new FileSystemResource("./src/main/resources/output_csv_to_csv.csv");
	
	@Bean(name="jobCSVToDB")
	public Job jobCSVToDB(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory
			){
		
		logger.info("step stepCSVToDB is build " );
		Step stepCSVToDB=stepBuilderFactory.get("Transaction-File-load-CSV-TO-DB")
				                    .<Transaction,Transaction>chunk(100)
				                    .reader(csvReader)
				                    .processor(itemProcessorCSVToDB)
				                    .writer(dbWriter)
				                    .build();
		return jobBuilderFactory.get("Transaction-Load-CSV-TO-DB")
		                 .incrementer(new RunIdIncrementer())
		                 .start(stepCSVToDB)   
		                 .build();
	}
	
	@Bean(name="jobDBToCSV")
	public Job jobDBToCSV(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory
			){
	
		
		Step stepDBToCSV=stepBuilderFactory.get("Transaction-File-load-DB-TO-CSV")
                .<Transaction,Transaction>chunk(100)
                .reader(dbReader)
                .processor(itemProcessorDBToCSV)
                .writer(csvWriter)
                .build();
		
		logger.info("step stepDBToCSV is build " );
		return jobBuilderFactory.get("Transaction-Load-DB-TO-CSV")
		                 .incrementer(new RunIdIncrementer())
		                 .start(stepDBToCSV)   
		                 .build();
	}
	
	@Bean(name="jobCSVToCSV")
	public Job jobCSVToCSV(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory
			){
	
		
		Step stepCSVToCSV=stepBuilderFactory.get("Transaction-File-load-CSV-TO-CSV")
                .<Transaction,Transaction>chunk(100)
                .reader(csvReaderCSVToCSV)
                .processor(itemProcessorCSVToCSV)
                .writer(csvWriterCSVToCSV)
                .build();
		
		logger.info("step stepCSVToCSV is build " );
		return jobBuilderFactory.get("Transaction-Load-CSV-TO-CSV")
		                 .incrementer(new RunIdIncrementer())
		                 .start(stepCSVToCSV)   
		                 .build();
	}
	
	@Bean(name="csvReader")
	public FlatFileItemReader<Transaction> csvReader(){
		
		FlatFileItemReader<Transaction> flatFileItemReader=new FlatFileItemReader<Transaction>();
		flatFileItemReader.setResource(new FileSystemResource("./src/main/resources/1000BTRecords.csv"));
	    flatFileItemReader.setName("CSV-READER");
		
		flatFileItemReader.setLinesToSkip(1);
		DefaultLineMapper<Transaction> defaultLineMapper = new DefaultLineMapper<Transaction>();

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(",");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames(new String[] {"Date", "Description", "Deposits","Withdrawals","Balance" });

		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<Transaction> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<Transaction>();
		beanWrapperFieldSetMapper.setTargetType(Transaction.class);
		
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		
		flatFileItemReader.setLineMapper(defaultLineMapper);
		
		return flatFileItemReader;
	}
	
	@Bean(name="csvReaderCSVToCSV")
	public FlatFileItemReader<Transaction> csvReaderCSVToCSV(){
		
		FlatFileItemReader<Transaction> flatFileItemReader=new FlatFileItemReader<Transaction>();
		flatFileItemReader.setResource(output_db_to_csv);
		flatFileItemReader.setName("CSV-READER-CSV-CSV");
		DefaultLineMapper<Transaction> defaultLineMapper = new DefaultLineMapper<Transaction>();

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(";");
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames(new String[] {"Date", "Description", "Deposits","Withdrawals","Balance" });

		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<Transaction> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<Transaction>();
		beanWrapperFieldSetMapper.setTargetType(Transaction.class);
		
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		flatFileItemReader.setLineMapper(defaultLineMapper);
		
		return flatFileItemReader;
	}
	
	@Bean("csvWriter")
	ItemWriter<Transaction> csvWriter() {
		FlatFileItemWriter<Transaction> csvFileWriter = new FlatFileItemWriter<>();
		
		csvFileWriter.setResource(output_db_to_csv);
		csvFileWriter.setLineAggregator(createTransactionLineAggregator());
		return csvFileWriter;
	}

	@Bean("csvWriterCSVToCSV")
	ItemWriter<Transaction> csvWriterCSVToCSV() {
		FlatFileItemWriter<Transaction> csvFileWriter = new FlatFileItemWriter<>();

		csvFileWriter.setResource(output_csv_to_csv);
		csvFileWriter.setLineAggregator(createTransactionLineAggregator());
		return csvFileWriter;
	}

	private LineAggregator<Transaction> createTransactionLineAggregator() {
		DelimitedLineAggregator<Transaction> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(";");

		lineAggregator.setFieldExtractor(createTransactionFieldExtractor());
		return lineAggregator;
	}

	private FieldExtractor<Transaction> createTransactionFieldExtractor() {
		BeanWrapperFieldExtractor<Transaction> extractor = new BeanWrapperFieldExtractor<>();
		extractor.setNames(new String[] { "Date", "Description", "Deposits", "Withdrawals", "Balance" });
		return extractor;
	}

}
