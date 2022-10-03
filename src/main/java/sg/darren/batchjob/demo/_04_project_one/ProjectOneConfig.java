package sg.darren.batchjob.demo._04_project_one;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class ProjectOneConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @Qualifier("projectOneJob")
    public Job projectOneJob() {
        return jobBuilderFactory.get("projectOneJob")
                .start(step())
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .<Person, Person>chunk(1)
                .reader(reader(null))
                .writer(writer(null))
                .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<Person> reader(@Value("#{jobParameters['inputPath']}") String inputPath) {
        File file = null;
        try {
            file = ResourceUtils.getFile(inputPath);
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException(ex);
        }
        return new JsonItemReaderBuilder<Person>()
                .name("jsonItemReader")
                .jsonObjectReader(new JacksonJsonObjectReader<>(Person.class))
                .resource(new FileSystemResource(file))
                .build();
    }

    @Bean
    @StepScope
    public JsonFileItemWriter<Person> writer(@Value("#{jobParameters['outputPath']}") String outputPath) {
        Resource outputResource = new FileSystemResource(outputPath);
        return new JsonFileItemWriterBuilder<Person>()
                .name("jsonItemWriter")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(outputResource)
                .build();
    }
}
