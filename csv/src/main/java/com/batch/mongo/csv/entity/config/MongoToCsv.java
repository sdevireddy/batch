package com.batch.mongo.csv.entity.config;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;

/**
 * Hello world!
 *
 */
@SpringBootApplication//(exclude = MongoAutoConfiguration.class)
@EnableBatchProcessing


//@ComponentScan("com.batch.**")
public class MongoToCsv 
{
	public static void main(String[] args) {
		SpringApplication.run(MongoToCsv.class, args);
	}
}
