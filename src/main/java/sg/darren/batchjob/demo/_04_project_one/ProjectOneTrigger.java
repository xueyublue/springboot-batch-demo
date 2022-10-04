package sg.darren.batchjob.demo._04_project_one;

import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProjectOneTrigger implements JobParameterKeys {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("projectOneJob")
    private Job projectOneJob;

    public void runJob()
            throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder()
                .addParameter(INPUT_PATH, new JobParameter("classpath:persons.json"))
                .addParameter(OUTPUT_PATH, new JobParameter("output/personsOutput.json"))
                .addParameter(CHUNK_SIZE, new JobParameter(1L))
                .toJobParameters();
        jobLauncher.run(projectOneJob, jobParameters);
    }
}
