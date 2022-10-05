package sg.darren.batchjob.demo;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sg.darren.batchjob.demo._01_hello_world.TriggerBatchJobService;

@SpringBootApplication
@EnableBatchProcessing
public class BatchDemoApplication {

    public static void main(String[] args)
            throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        ConfigurableApplicationContext context = SpringApplication.run(BatchDemoApplication.class, args);

        TriggerBatchJobService triggerBatchJobService = context.getBean(TriggerBatchJobService.class);
        triggerBatchJobService.runJob();
    }

}
