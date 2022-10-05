package sg.darren.batchjob.demo._01_hello_world;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TriggerBatchJobService {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("helloWorldJob")
    private Job job;

    public void runJob()
            throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter("output", new JobParameter("Hello World"))
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }

}
