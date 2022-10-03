package sg.darren.batchjob.demo;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import sg.darren.batchjob.demo._02_hello_world.TriggerBatchJobService;

@SpringBootApplication
@EnableBatchProcessing
public class BatchDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BatchDemoApplication.class, args);

        TriggerBatchJobService triggerBatchJobService = context.getBean(TriggerBatchJobService.class);
        try {
            triggerBatchJobService.runJob();
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }

}
