package sg.darren.batchjob.demo.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sg.darren.batchjob.demo._03_database_to_csv.DatabaseToCsvEntity;
import sg.darren.batchjob.demo._03_database_to_csv.DatabaseToCsvRepository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class JobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("csvToDatabaseJob")
    private Job csvToDatabaseJob;

    @Autowired
    @Qualifier("databaseToCsvJob")
    private Job databaseToCsvJob;

    @Autowired
    @Qualifier("xmlToDatabaseJob")
    private Job xmlToDatabaseJob;

    @Autowired
    private DatabaseToCsvRepository databaseToCsvRepository;

    @GetMapping("/csvToDatabase")
    public BatchStatus csvToDatabase()
            throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        Map<String, JobParameter> map = new HashMap<>();
        map.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(map);
        JobExecution jobExecution = jobLauncher.run(csvToDatabaseJob, jobParameters);
        while (jobExecution.isRunning()) {
            log.info("Running...");
        }
        log.info("Completed.");
        return jobExecution.getStatus();
    }

    @GetMapping("/databaseToCsv")
    public BatchStatus databaseToCsv()
            throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        // insert initial data
        if (databaseToCsvRepository.findAll().isEmpty()) {
            for (int i = 0; i < 5; i++) {
                databaseToCsvRepository.save(DatabaseToCsvEntity.builder()
                        .firstName(new Date().toString())
                        .lastName(new Date().toString())
                        .email(new Date().toString() + "@gmail.com")
                        .build());
            }
        }
        // run job
        Map<String, JobParameter> map = new HashMap<>();
        map.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(map);
        JobExecution jobExecution = jobLauncher.run(databaseToCsvJob, jobParameters);
        while (jobExecution.isRunning()) {
            log.info("Running...");
        }
        log.info("Completed.");
        return jobExecution.getStatus();
    }

    @GetMapping("/xmlToDatabase")
    public BatchStatus xmlToDatabase()
            throws JobInstanceAlreadyCompleteException,
            JobExecutionAlreadyRunningException,
            JobParametersInvalidException,
            JobRestartException {
        Map<String, JobParameter> map = new HashMap<>();
        map.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(map);
        JobExecution jobExecution = jobLauncher.run(xmlToDatabaseJob, jobParameters);
        while (jobExecution.isRunning()) {
            log.info("Running...");
        }
        log.info("Completed.");
        return jobExecution.getStatus();
    }
}
