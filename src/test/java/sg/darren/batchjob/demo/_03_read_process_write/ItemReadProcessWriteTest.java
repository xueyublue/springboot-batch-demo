package sg.darren.batchjob.demo._03_read_process_write;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

@SpringBootTest(classes = ItemReadProcessWriteTest.TestConfig.class)
class ItemReadProcessWriteTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void test() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("inputPath", new JobParameter("classpath:input.json"))
                .addParameter("outputPath", new JobParameter("output/output.json"))
                .toJobParameters();

        // then
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // expect
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Configuration
    @EnableBatchProcessing
    static class TestConfig {

        @Autowired
        private JobBuilderFactory jobBuilderFactory;

        @Autowired
        private StepBuilderFactory stepBuilderFactory;

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }

        @Bean
        public Job readWriteJob() {
            return jobBuilderFactory.get("readWriteJob")
                    .start(step())
                    .build();
        }

        @Bean
        public Step step() {
            return stepBuilderFactory.get("readWriteJsonStep")
                    .<Input, Output>chunk(1)
                    .reader(reader(null))
                    .processor(processor())
                    .writer(writer(null))
                    .build();
        }

        @Bean
        @StepScope
        public JsonItemReader<Input> reader(@Value("#{jobParameters['inputPath']}") String inputPath) {
            File file = null;
            try {
                file = ResourceUtils.getFile(inputPath);
            } catch (FileNotFoundException ex) {
                throw new IllegalArgumentException(ex);
            }
            return new JsonItemReaderBuilder<Input>()
                    .name("jsonItemReader")
                    .jsonObjectReader(new JacksonJsonObjectReader<>(Input.class))
                    .resource(new FileSystemResource(file))
                    .build();
        }

        @Bean
        public ItemProcessor<Input, Output> processor() {
            return new ItemProcessor<Input, Output>() {
                @Override
                public Output process(Input input) throws Exception {
                    Output output = new Output();
                    output.value = input.value;
                    output.formattedValue = input.value == null ? "" : input.value.toUpperCase();
                    output.date = new Date();
                    return output;
                }
            };
        }

        @Bean
        @StepScope
        public JsonFileItemWriter<Output> writer(@Value("#{jobParameters['outputPath']}") String outputPath) {
            Resource outputResource = new FileSystemResource(outputPath);
            return new JsonFileItemWriterBuilder<Output>()
                    .name("jsonItemWriter")
                    .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                    .resource(outputResource)
                    .build();
        }

        public static class Input {
            public String value;

            @Override
            public String toString() {
                return "Input{" +
                        "value='" + value + '\'' +
                        '}';
            }
        }

        public static class Output {
            public String value;
            public String formattedValue;
            public Date date;

            @Override
            public String toString() {
                return "Output{" +
                        "value='" + value + '\'' +
                        '}';
            }
        }
    }

}
