package sg.darren.batchjob.demo._04_project_one;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@SpringBootTest(classes = {ProjectOneTest.TestConfig.class,
        ProjectOneConfig.class})
class ProjectOneTest implements JobParameterKeys {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("projectOneJob")
    private Job projectOneJob;

    @Test
    void test() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter(INPUT_PATH, new JobParameter("classpath:persons.json"))
                .addParameter(OUTPUT_PATH, new JobParameter("output/personsOutput.json"))
                .addParameter(CHUNK_SIZE, new JobParameter(1L))
                .toJobParameters();
        jobLauncherTestUtils.setJob(projectOneJob);
        // expect
        Assertions.assertThat(jobLauncherTestUtils.launchJob(jobParameters).getStatus())
                .isNotNull()
                .isEqualByComparingTo(BatchStatus.COMPLETED);
        String output = Assertions.contentOf(new File("output/output.json"));
        Assertions.assertThat(output).doesNotContain("Daliah Shah");
    }

    @Test
    void test_invalidInputFileExtension() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter(INPUT_PATH, new JobParameter("classpath:persons.xml"))
                .addParameter(OUTPUT_PATH, new JobParameter("output/personsOutput.json"))
                .addParameter(CHUNK_SIZE, new JobParameter(1L))
                .toJobParameters();
        jobLauncherTestUtils.setJob(projectOneJob);
        // expect
        Assertions.assertThatThrownBy(() -> jobLauncherTestUtils.launchJob(jobParameters))
                .isInstanceOf(JobParametersInvalidException.class)
                .hasMessageContaining("Input file  must be in JSON format");

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
