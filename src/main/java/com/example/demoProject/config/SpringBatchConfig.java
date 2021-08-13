package com.example.demoProject.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.support.H2PagingQueryProvider;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.example.demoProject.model.Transaction;
import com.example.demoProject.orm.JpaQueryProviderImpl;
import com.example.demoProject.repository.TransactionRepository;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	@Autowired
	@Qualifier("csvReader")
	ItemReader<Transaction> csvReader;
	
	@Autowired
	@Qualifier("dbWriter")
	ItemWriter<Transaction>itemWriter;
	
	
	@Autowired
	@Qualifier("transactionProcessor")
	ItemProcessor<Transaction,Transaction> itemProcessor;
	
	
	@Autowired
	@Qualifier("dbReader")
	ItemReader<Transaction> dbReader;
	
	/*@Autowired
	TransactionRepository transactionRepo;*/
	
	@Autowired
	@Qualifier("csvWriter")
	ItemWriter<Transaction> csvWriter;
	
	
	@Autowired
	EntityManagerFactory entityManagerFactory;
	
	private Resource resource = new FileSystemResource("D:/demoProject/src/main/resources/output.csv");
	
	@Bean
	public Job jobCSVToDB(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory
			){
	
		Step stepCSVToDB=stepBuilderFactory.get("Transaction-File-load-CSV-TO-DB")
				                    .<Transaction,Transaction>chunk(100)
				                    .reader(csvReader)
				                    .processor(itemProcessor)
				                    .writer(itemWriter)
				                    .build();
		/*
		
		Step stepCSVToCSV=stepBuilderFactory.get("Transaction-File-load-CSV-TO-CSV")
                .<Transaction,Transaction>chunk(100)
                .reader(csvToDBitemReader)
                .writer(itemWriter)
                .build();
		*/
		return jobBuilderFactory.get("Transaction-Load-CSV-TO-DB")
		                 .incrementer(new RunIdIncrementer())
		                 .start(stepCSVToDB)    //PARALLEL
		                 .build();
	}
	@Bean
	public Job jobDBToCSV(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory
			){
	
		
		Step stepDBToCSV=stepBuilderFactory.get("Transaction-File-load-DB-TO-CSV")
                .<Transaction,Transaction>chunk(100)
                .reader(dbReader)
                .writer(csvWriter)
                .build();
		
		return jobBuilderFactory.get("Transaction-Load-DB-TO-CSV")
		                 .incrementer(new RunIdIncrementer())
		                 .start(stepDBToCSV)    //PARALLEL
		                 .build();
	}
	
	@Bean(name="csvReader")
	public FlatFileItemReader<Transaction> csvReader(@Value("${input}") Resource resource){
		
		FlatFileItemReader<Transaction> flatFileItemReader=new FlatFileItemReader<Transaction>();
		flatFileItemReader.setResource(resource);
		flatFileItemReader.setName("CSV-READER");
		
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setLineMapper(lineMapper());
		
		return flatFileItemReader;
	}
	
	@Bean
	public DefaultLineMapper<Transaction> lineMapper() {

		DefaultLineMapper<Transaction> defaultLineMapper = new DefaultLineMapper<Transaction>();

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setDelimiter(DelimitedLineTokenizer.DELIMITER_COMMA);
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames(new String[] {"Date", "Description", "Deposits","Withdrawals","Balance" });

		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<Transaction> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<Transaction>();
		beanWrapperFieldSetMapper.setTargetType(Transaction.class);
		
		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
		return defaultLineMapper;

	}
	
	/*@Bean(name = "dbReader")
	public JdbcPagingItemReader<Transaction> databaseToCSVitemReader(DataSource dataSource){
		
		JdbcPagingItemReader<Transaction> jdbcPagingItemReader=new JdbcPagingItemReader<Transaction>();
		
		jdbcPagingItemReader.setDataSource(dataSource);
		jdbcPagingItemReader.setPageSize(1);
		jdbcPagingItemReader.setRowMapper(new BeanPropertyRowMapper<>(Transaction.class));

        PagingQueryProvider queryProvider = createQueryProvider();
        jdbcPagingItemReader.setQueryProvider(queryProvider);
        
		
		return jdbcPagingItemReader;
	}
	*/
	
	
	/*@Bean(name = "dbReader")
	public RepositoryItemReader<Transaction> databaseToCSVitemReader(TransactionRepository transactionRepo) throws Exception{
		
		RepositoryItemReader<Transaction> repoItemReader=new RepositoryItemReader<Transaction>();
		repoItemReader.setRepository(transactionRepo);
		repoItemReader.setMethodName("findAll");
		repoItemReader.setPageSize(10);
		Map<String, Direction> sorts = new HashMap<>();
        sorts.put("transactionId", Direction.ASC);
		repoItemReader.setSort(sorts);
		repoItemReader.setSaveState(false);
		repoItemReader.afterPropertiesSet();
		
		return repoItemReader;
	}*/
	
	/*private PagingQueryProvider createQueryProvider() {
        H2PagingQueryProvider queryProvider = new H2PagingQueryProvider();

        queryProvider.setSelectClause("SELECT TRANSACTION_ID,DATE,BALANCE,DEPOSITS,DESCRIPTION,WITHDRAWALS");
        queryProvider.setFromClause("FROM TRANSACTION");
        queryProvider.setSortKeys(sortByTransactionIdAsc());
        return queryProvider;
    }
     private Map<String, Order> sortByTransactionIdAsc() {
	        Map<String, Order> sortConfiguration = new HashMap<>();
	        sortConfiguration.put("TRANSACTION_ID", Order.ASCENDING);
	        return sortConfiguration;
	    }
    
    */
	
	/*@Bean(name = "dbReader")
    public JpaPagingItemReader<Transaction> databaseToCSVitemReader(EntityManagerFactory entityManagerFactory) throws Exception {
        JpaPagingItemReader<Transaction> transactionItemReader = new JpaPagingItemReader<>();
        
        transactionItemReader.setEntityManagerFactory(entityManagerFactory);
        
        JpaQueryProviderImpl<Transaction> jpaQueryProvider = new JpaQueryProviderImpl<>();
        jpaQueryProvider.setQuery("Transaction.findAll");
        transactionItemReader.setQueryProvider(jpaQueryProvider);
        
        transactionItemReader.setPageSize(1000);
	    transactionItemReader.afterPropertiesSet();
        return transactionItemReader;
    }
	*/
	
	 
	 //@Value("${output}") Resource resource
	@Bean("csvWriter")
	ItemWriter<Transaction> csvWriter() {
		FlatFileItemWriter<Transaction> csvFileWriter = new FlatFileItemWriter<>();
		
		csvFileWriter.setResource(resource);
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
	        extractor.setNames(new String[] {"Date", "Description", "Deposits","Withdrawals","Balance"});
	       return extractor;
	    }
	
}
