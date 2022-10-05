package sg.darren.batchjob.demo._05_database_to_xml;

import com.thoughtworks.xstream.security.ExplicitTypePermission;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import sg.darren.batchjob.demo._04_xml_to_database.XmlToDatabaseDto;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DatabaseToXmlJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    public DatabaseToXmlJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.dataSource = dataSource;
    }

    @Bean
    @Qualifier("databaseToXmlJob")
    public Job databaseToXmlJob() {
        return jobBuilderFactory.get("databaseToXmlJob")
                .incrementer(new RunIdIncrementer())
                .flow(databaseToXmlStep())
                .end()
                .build();
    }

    @Bean
    @Qualifier("databaseToXmlStep")
    public Step databaseToXmlStep() {
        return stepBuilderFactory.get("xmlToDatabaseJobStep")
                .<DatabaseToXmlDto, DatabaseToXmlDto>chunk(10)
                .reader(databaseToXmlReader())
                .processor(databaseToXmlProcessor())
                .writer(databaseToXmlWriter())
                .build();
    }

    @Bean
    @Qualifier("databaseToXmlReader")
    public ItemReader<DatabaseToXmlDto> databaseToXmlReader() {
        JdbcCursorItemReader<DatabaseToXmlDto> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT id, first_name, last_name, email FROM database_to_xml");
        reader.setRowMapper((rs, rowNum) -> DatabaseToXmlDto.builder()
                .id(rs.getInt("id"))
                .firstName(rs.getString("first_name"))
                .lastName(rs.getString("last_name"))
                .email(rs.getString("email"))
                .build());
        return reader;
    }

    @Bean
    @Qualifier("databaseToXmlProcessor")
    public ItemProcessor<DatabaseToXmlDto, DatabaseToXmlDto> databaseToXmlProcessor() {
        return input -> {
            input.setEmail(input.getEmail() != null ? input.getEmail().toUpperCase() : "");
            return input;
        };
    }

    @Bean
    @Qualifier("databaseToXmlWriter")
    public ItemWriter<DatabaseToXmlDto> databaseToXmlWriter() {
        Map<String, String> aliasMap = new HashMap<>();
        aliasMap.put("DatabaseToXml", XmlToDatabaseDto.class.getName());

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliasMap);
        marshaller.setTypePermissions(
                new ExplicitTypePermission(
                        new Class[]{XmlToDatabaseDto.class}
                )
        );

        StaxEventItemWriter<DatabaseToXmlDto> writer = new StaxEventItemWriter<>();
        writer.setResource(new FileSystemResource("_05_database_to_xml/output.xml"));
        writer.setMarshaller(marshaller);
        writer.setRootTagName("data");
        writer.setOverwriteOutput(true);

        return writer;
    }
}
