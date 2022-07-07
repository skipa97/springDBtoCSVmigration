package com.aventure.work.demo.batchconfig;


import com.aventure.work.demo.mapper.CustomerMapper;
import com.aventure.work.demo.model.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
public class BatchConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    DataSource dataSource;
    @Autowired
    private CustomerMapper customerMapper;

    @Bean(name="generateCSVCustomerData")
    public Job generateCSV(){
        return jobBuilderFactory.get("generateCSV").incrementer(new RunIdIncrementer()).start(processingCustomerData()).build();
    }

    @Bean
    public Step processingCustomerData(){
        return stepBuilderFactory.get("processingCustomerData").<Customer, Customer>chunk(100)
                .reader(jdbcCursorItemReader())
                .writer(flatFileItemWriter())
                .build();

    }

    @Bean
    public JdbcCursorItemReader<Customer> jdbcCursorItemReader(){
        JdbcCursorItemReader<Customer>jdbcCursorItemReader = new JdbcCursorItemReader<>();

        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql("Select * from Sales.Customer");
        jdbcCursorItemReader.setRowMapper(customerMapper);
        return jdbcCursorItemReader;

    }

    @Bean
    public FlatFileItemWriter<Customer> flatFileItemWriter(){
        FlatFileItemWriter<Customer> flatFileItemWriter = new FlatFileItemWriter<>();

        flatFileItemWriter.setResource(new FileSystemResource("C:\\Users\\a237938\\Documents\\Integration work\\migration\\MigrationCSVResults\\Sale.Customer.csv"));
        flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<Customer>(){{
            setDelimiter(",");
            setFieldExtractor(new BeanWrapperFieldExtractor<Customer>(){{
                setNames(new String[] {"CustomerID","PersonID","StoreID","TerritoryID","AccountNumber","rowguid","ModifiedDate"});
            }});

        }});

        flatFileItemWriter.setHeaderCallback(writer -> writer.write("CustomerID,PersonID,StoreID,TerritoryID,AccountNumber,rowguid,ModifiedDate"));
        return flatFileItemWriter;
    }
}
