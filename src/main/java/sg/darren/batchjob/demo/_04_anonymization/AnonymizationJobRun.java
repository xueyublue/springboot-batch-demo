package sg.darren.batchjob.demo._04_anonymization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class AnonymizationJobRun implements AnonymizationJobParameterKeys {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("anonymizationJob")
    private Job job;

    public void runJob(File file) {
        log.info("Processing file: {}", file);
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter(INPUT_PATH, new JobParameter("classpath:persons.json"))
                .addParameter(OUTPUT_PATH, new JobParameter("output/personsOutput.json"))
                .addParameter(CHUNK_SIZE, new JobParameter(1L))
                .toJobParameters();
        try {
            jobLauncher.run(job, jobParameters);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }
}
