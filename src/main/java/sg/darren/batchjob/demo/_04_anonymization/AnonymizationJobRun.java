package sg.darren.batchjob.demo._04_anonymization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sg.darren.batchjob.demo._04_anonymization.config.AnonymizationJobParameterKeys;
import sg.darren.batchjob.demo.utils.Utils;

import java.io.File;

@Service
@Slf4j
public class AnonymizationJobRun implements AnonymizationJobParameterKeys {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("anonymizationJob")
    private Job job;

    public void runJob(File uploadedFile) {
        log.info("Processing file: {}", uploadedFile);

        String uploadedFilePath = uploadedFile.getAbsolutePath();

        String completedDirectory = Utils.getWorkDirSubDirectory("public/completed");
        String errorDirectory = Utils.getWorkDirSubDirectory("public/error");
        String processingDirectory = Utils.getWorkDirSubDirectory("private/processing");

        String outputPath = Utils.getFilePathForDifferentDirectory(uploadedFile, completedDirectory);
        String errorPath = Utils.getFilePathForDifferentDirectory(uploadedFile, errorDirectory);
        String processingPath = Utils.getFilePathForDifferentDirectory(uploadedFile, processingDirectory);

        JobParameters jobParameters = new JobParametersBuilder()
                .addString(INPUT_PATH, processingPath)
                .addString(OUTPUT_PATH, outputPath)
                .addString(ERROR_PATH, errorPath)
                .addString(UPLOAD_PATH, uploadedFilePath)
                .toJobParameters();

        try {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException | JobRestartException | JobParametersInvalidException e) {
            log.error(e.getMessage());
        }
    }
}
