package sg.darren.batchjob.demo._03_database_to_csv;

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
                .<Student, Student>chunk(10)
                .reader(databaseToCsvReader())
                .writer(databaseToCsvWriter())
                .build();
    }

    @Bean
    @Qualifier("databaseToCsvReader")
    public JdbcCursorItemReader<Student> databaseToCsvReader() {
        JdbcCursorItemReader<Student> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT id, first_name, last_name, email FROM student");
        reader.setRowMapper((rs, rowNum) -> Student.builder()
                .id(rs.getLong("id"))
                .firstName(rs.getString("firstName"))
                .lastName(rs.getString("lastName"))
                .email(rs.getString("email"))
                .build());
        return reader;
    }

    @Bean
    @Qualifier("databaseToCsvWriter")
    public FlatFileItemWriter<Student> databaseToCsvWriter() {
        BeanWrapperFieldExtractor<Student> extractor = new BeanWrapperFieldExtractor<>();
        extractor.setNames(new String[]{"id", "firstName", "lastName", "email"});

        DelimitedLineAggregator<Student> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setFieldExtractor(extractor);

        FlatFileItemWriter<Student> writer = new FlatFileItemWriter<>();
        writer.setResource(new FileSystemResource("_02_/databaseToCsvOutput.csv"));
        writer.setLineAggregator(lineAggregator);

        return writer;
    }

}
