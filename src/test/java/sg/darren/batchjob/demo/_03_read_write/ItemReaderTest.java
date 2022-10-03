package sg.darren.batchjob.demo._03_read_write;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;

@SpringBootTest(classes = ItemReaderTest.TestConfig.class)
class ItemReaderTest {

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
                    .start(readerStep())
                    .build();
        }

        @Bean
        public Step readerStep() {
            return stepBuilderFactory.get("readJsonStep")
                    .chunk(1)
                    .reader(reader())
                    .writer(System.out::println)
                    .build();
        }

        @Bean
        public JsonItemReader<Input> reader() {
            File file = null;
            try {
                file = ResourceUtils.getFile("classpath:input.json");
            } catch (FileNotFoundException ex) {
                throw new IllegalArgumentException(ex);
            }
            return new JsonItemReaderBuilder<Input>()
                    .jsonObjectReader(new JacksonJsonObjectReader<>(Input.class))
                    .resource(new FileSystemResource(file))
                    .name("jsonItemReader")
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
    }

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void readWriteJobTest() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .toJobParameters();

        // then
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // expect
        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

}
