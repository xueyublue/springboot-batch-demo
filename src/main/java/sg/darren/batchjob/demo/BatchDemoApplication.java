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
import sg.darren.batchjob.demo._04_project_one.ProjectOneTrigger;

@SpringBootApplication
@EnableBatchProcessing
public class BatchDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(BatchDemoApplication.class, args);

        try {
            TriggerBatchJobService triggerBatchJobService = context.getBean(TriggerBatchJobService.class);
            triggerBatchJobService.runJob();

            ProjectOneTrigger projectOneTrigger = context.getBean(ProjectOneTrigger.class);
            projectOneTrigger.runJob();

        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }

}
