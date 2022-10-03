package sg.darren.batchjob.demo._02_hello_world;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@SpringBootTest(classes = HelloWorldTest.TestConfig.class)
class HelloWorldTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void test() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("output", new JobParameter("Hello World"))
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
        public Job helloWorldJob() {
            Step step = stepBuilderFactory.get("step1")
                    .tasklet((stepContribution, chunkContext) -> {
                        Map<String, Object> map = chunkContext.getStepContext().getJobParameters();
                        System.out.println(map.get("output"));
                        return RepeatStatus.FINISHED;
                    }).build();

            return jobBuilderFactory.get("helloWorldJob")
                    .start(step)
                    .build();
        }
    }
}
