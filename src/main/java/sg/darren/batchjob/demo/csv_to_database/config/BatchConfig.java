package sg.darren.batchjob.demo.csv_to_database.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory) {

        Step step = null;

        return jobBuilderFactory.get("LoadUserFromCsvToDatabase")
                .incrementer(new RunIdIncrementer())
                .start(step)
                .build();
    }

}
