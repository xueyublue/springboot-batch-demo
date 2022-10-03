package sg.darren.batchjob.demo._02_hello_world;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class HelloWorldConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @Qualifier("helloWorldJob")
    public Job helloWorldJob() {
        Step step = stepBuilderFactory.get("step")
                .tasklet((stepContribution, chunkContext) -> {
                    Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
                    Object output = jobParameters.get("output");
                    log.info(output == null ? "" : output.toString());
                    return RepeatStatus.FINISHED;
                }).build();

        return jobBuilderFactory.get("helloWorldJob")
                .start(step)
                .build();
    }
}
