package sg.darren.batchjob.demo._91_anonymization.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;
import sg.darren.batchjob.demo._91_anonymization.config.AnonymizationJobParameterKeys;
import sg.darren.batchjob.demo.utils.Utils;

import java.io.File;

@Component
@Slf4j
public class FileHandlingJobExecutionListenerImpl
        implements FileHandlingJobExecutionListener, AnonymizationJobParameterKeys {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String uploadedFile = jobParameters.getString(UPLOAD_PATH);
        String inputFile = jobParameters.getString(INPUT_PATH);
        Utils.moveFileToDirectory(new File(uploadedFile), new File(inputFile).getParent());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        JobParameters jobParameters = jobExecution.getJobParameters();
        String inputFile = jobParameters.getString(INPUT_PATH);

        if (jobExecution.getStatus().equals(BatchStatus.COMPLETED)) {
            Utils.deleteFile(inputFile);
        } else {
            String errorFile = jobParameters.getString(ERROR_PATH);
            Utils.moveFileToDirectory(new File(inputFile), new File(errorFile).getParent());
        }
    }

}
