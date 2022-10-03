package sg.darren.batchjob.demo._04_project_one;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootTest(classes = {ProjectOneTest.TestConfig.class,
        ProjectOneConfig.class})
class ProjectOneTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("projectOneJob")
    private Job projectOneJob;

    @Test
    void test() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("inputPath", new JobParameter("classpath:persons.json"))
                .addParameter("outputPath", new JobParameter("output/personsOutput.json"))
                .addParameter("chunkSize", new JobParameter(1L))
                .toJobParameters();
        jobLauncherTestUtils.setJob(projectOneJob);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        Assertions.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
    }

    @Configuration
    @EnableBatchProcessing
    static class TestConfig {

        @Bean
        public JobLauncherTestUtils jobLauncherTestUtils() {
            return new JobLauncherTestUtils();
        }
    }
}
