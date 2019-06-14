package com.batch.mongo.csv.entity.config;

import java.util.Collections;
import java.util.HashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.data.MongoItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import com.batch.mongo.csv.entity.MongoDBEntity;

@Configuration
public class ApplicationConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Bean
	public MongoItemReader<MongoDBEntity> reader() {
		System.out.println("REader");
		MongoItemReader<MongoDBEntity> reader = new MongoItemReader<MongoDBEntity>();
		reader.setTemplate(mongoTemplate);
		reader.setQuery("{}");
		reader.setTargetType(MongoDBEntity.class);
		reader.setTargetType((Class<? extends MongoDBEntity>) MongoDBEntity.class);
		reader.setSort(new HashMap<String, Sort.Direction>() {
            {
                put("_id", Direction.ASC);
            }
        });
        return reader;

	}

	@Bean
	public FlatFileItemWriter<MongoDBEntity> writer() {
		System.out.println("Writer");
		FlatFileItemWriter<MongoDBEntity> writer = new FlatFileItemWriter<MongoDBEntity>();
		writer.setResource(new FileSystemResource(
				"c://outputs//temp.all.csv"));
		writer.setLineAggregator(new DelimitedLineAggregator<MongoDBEntity>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<MongoDBEntity>() {
					{
						setNames(new String[] { "id", "name" });
					}
				});
			}
		});

		return writer;
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<MongoDBEntity, MongoDBEntity> chunk(10).reader(reader())
				.writer(writer()).build();
	}

	@Bean
	public Job exportUserJob() {
		return jobBuilderFactory.get("exportUserJob")
				.incrementer(new RunIdIncrementer()).flow(step1()).end()
				.build();
	}
	
/*	@Bean
	public CustomConversions mongoCustomConversions() {
		return new CustomConversions(Collections.emptyList());
	}*/

}
