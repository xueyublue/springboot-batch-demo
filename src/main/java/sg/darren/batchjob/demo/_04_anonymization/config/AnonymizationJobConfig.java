package sg.darren.batchjob.demo._04_anonymization.config;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
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
import sg.darren.batchjob.demo._04_anonymization.listeners.FileHandlingJobExecutionListener;
import sg.darren.batchjob.demo._04_anonymization.model.Person;
import sg.darren.batchjob.demo.utils.Utils;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class AnonymizationJobConfig implements AnonymizationJobParameterKeys {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final FileHandlingJobExecutionListener fileHandlingJobExecutionListener;

    @Bean
    @Qualifier("anonymizationJob")
    public Job anonymizationJob() {
        return jobBuilderFactory.get("anonymizationJob")
                .start(step())
                .validator(new AnonymizationJobParameterValidator())
                .listener(fileHandlingJobExecutionListener)
                .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .<Person, Person>chunk(1)
                .reader(reader(null))
                .processor(processor(null))
                .writer(writer(null))
                .build();
    }

    @Bean
    @StepScope
    public JsonItemReader<Person> reader(@Value(INPUT_PATH_REF) String inputPath) {
        FileSystemResource resource = Utils.getFileResource(inputPath);
        return new JsonItemReaderBuilder<Person>()
                .name("jsonItemReader")
                .jsonObjectReader(new JacksonJsonObjectReader<>(Person.class))
                .resource(resource)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<Person, Person> processor(@Value(ANONYMIZATION_FLAG_REF) String flag) {
        return new ItemProcessor<Person, Person>() {
            @Override
            public Person process(Person input) throws Exception {
                if (Boolean.FALSE.equals(input.getIsCustomer())) {
                    return null;
                }
                boolean anonymizationFlag = flag != null && flag.equalsIgnoreCase("true");
                return Person.builder()
                        .name(anonymizationFlag ? "John Doe" : input.getName())
                        .email(anonymizationFlag ? "" : input.getEmail())
                        .birthday(input.getBirthday())
                        .revenue(input.getRevenue())
                        .isCustomer(input.getIsCustomer())
                        .build();
            }
        };
    }

    @Bean
    @StepScope
    public JsonFileItemWriter<Person> writer(@Value(OUTPUT_PATH_REF) String outputPath) {
        Resource outputResource = new FileSystemResource(outputPath);
        return new JsonFileItemWriterBuilder<Person>()
                .name("jsonItemWriter")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(outputResource)
                .build();
    }
}
