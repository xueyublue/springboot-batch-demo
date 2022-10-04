package sg.darren.batchjob.demo._04_anonymization;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sg.darren.batchjob.demo._04_anonymization.config.AnonymizationJobConfig;
import sg.darren.batchjob.demo._04_anonymization.config.AnonymizationJobParameterKeys;
import sg.darren.batchjob.demo._04_anonymization.listeners.FileHandlingJobExecutionListener;

import java.io.File;

@SpringBootTest(classes = {AnonymizationJobTest.TestConfig.class,
        AnonymizationJobConfig.class})
class AnonymizationJobTest implements AnonymizationJobParameterKeys {

    private static final String INPUT_FILE = "classpath:persons-unit-test.json";
    private static final String INPUT_FILE2 = "classpath:persons-unit-test2.json";
    private static final String OUTPUT_FILE = "public-unit-test/personsOutput.json";

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    @Qualifier("anonymizationJob")
    private Job job;

    @MockBean
    private FileHandlingJobExecutionListener fileHandlingJobExecutionListener;

    @Test
    void test() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter(INPUT_PATH, new JobParameter(INPUT_FILE))
                .addParameter(OUTPUT_PATH, new JobParameter(OUTPUT_FILE))
                .addParameter(CHUNK_SIZE, new JobParameter(1L))
                .toJobParameters();
        jobLauncherTestUtils.setJob(job);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        // expect
        Assertions.assertThat(jobExecution.getStatus())
                .isNotNull()
                .isEqualByComparingTo(BatchStatus.COMPLETED);
        String output = Assertions.contentOf(new File(OUTPUT_FILE));
        Assertions.assertThat(output).doesNotContain("Daliah Shah");
        // to verify if listener is used to handle before/after "jobExecution"
        Mockito.verify(fileHandlingJobExecutionListener).beforeJob(jobExecution);
        Mockito.verify(fileHandlingJobExecutionListener).afterJob(jobExecution);
    }

    @Test
    void test_anonymizationFlagTrue() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter(INPUT_PATH, new JobParameter(INPUT_FILE))
                .addParameter(OUTPUT_PATH, new JobParameter(OUTPUT_FILE))
                .addParameter(ANONYMIZATION_FLAG, new JobParameter("true"))
                .addParameter(CHUNK_SIZE, new JobParameter(1L))
                .toJobParameters();
        jobLauncherTestUtils.setJob(job);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        // expect
        Assertions.assertThat(jobExecution.getStatus())
                .isNotNull()
                .isEqualByComparingTo(BatchStatus.COMPLETED);
        String output = Assertions.contentOf(new File(OUTPUT_FILE));
        Assertions.assertThat(output).doesNotContain("Daliah Shah");
        // to verify if listener is used to handle before/after "jobExecution"
        Mockito.verify(fileHandlingJobExecutionListener).beforeJob(jobExecution);
        Mockito.verify(fileHandlingJobExecutionListener).afterJob(jobExecution);
    }

    @Test
    void test_invalidInputFileExtension() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter(INPUT_PATH, new JobParameter("persons.xml"))
                .addParameter(OUTPUT_PATH, new JobParameter(OUTPUT_FILE))
                .addParameter(CHUNK_SIZE, new JobParameter(1L))
                .toJobParameters();
        jobLauncherTestUtils.setJob(job);
        // expect
        Assertions.assertThatThrownBy(() -> jobLauncherTestUtils.launchJob(jobParameters))
                .isInstanceOf(JobParametersInvalidException.class)
                .hasMessageContaining("Input file must be in JSON format");

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
