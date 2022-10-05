package sg.darren.batchjob.demo._03_database_to_csv;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;

@Configuration
public class DatabaseToCsvJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    @Qualifier("databaseToCsvJob")
    public Job databaseToCsvJob() {
        return jobBuilderFactory.get("databaseToCsvJob")
                .incrementer(new RunIdIncrementer())
                .flow(databaseToCsvStep())
                .end()
                .build();
    }

    @Bean
    @Qualifier("databaseToCsvStep")
    public Step databaseToCsvStep() {
        return stepBuilderFactory.get("databaseToCsv")
                .<DatabaseToCsvEntity, DatabaseToCsvEntity>chunk(10)
                .reader(databaseToCsvReader())
                .processor(databaseToCsvProcessor())
                .writer(databaseToCsvWriter())
                .build();
    }

    @Bean
    @Qualifier("databaseToCsvReader")
    public JdbcCursorItemReader<DatabaseToCsvEntity> databaseToCsvReader() {
        JdbcCursorItemReader<DatabaseToCsvEntity> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT id, first_name, last_name, email FROM database_to_csv");
        reader.setRowMapper((rs, rowNum) -> DatabaseToCsvEntity.builder()
                .id(rs.getInt("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .build());
        return reader;
    }

    @Bean
    @Qualifier("databaseToCsvProcessor")
    public ItemProcessor<DatabaseToCsvEntity, DatabaseToCsvEntity> databaseToCsvProcessor() {
        return input -> {
            input.setEmail(input.getEmail() != null ? input.getEmail().toUpperCase() : "");
            return input;
        };
    }

    @Bean
    @Qualifier("databaseToCsvWriter")
    public FlatFileItemWriter<DatabaseToCsvEntity> databaseToCsvWriter() {
        BeanWrapperFieldExtractor<DatabaseToCsvEntity> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[]{"id", "firstName", "lastName", "email"});

        DelimitedLineAggregator<DatabaseToCsvEntity> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setFieldExtractor(extractor);

        FlatFileItemWriter<DatabaseToCsvEntity> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("_03_database_to_csv/output.csv"));
        writer.setLineAggregator(lineAggregator);

        return writer;
    }

}
