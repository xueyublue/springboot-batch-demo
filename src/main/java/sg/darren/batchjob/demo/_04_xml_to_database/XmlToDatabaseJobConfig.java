package sg.darren.batchjob.demo._04_xml_to_database;

import com.thoughtworks.xstream.security.ExplicitTypePermission;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class XmlToDatabaseJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private XmlToDatabaseRepository xmlToDatabaseRepository;

    @Bean
    @Qualifier("xmlToDatabaseJob")
    public Job xmlToDatabaseJob() {
        return jobBuilderFactory.get("xmlToDatabaseJob")
                .incrementer(new RunIdIncrementer())
                .flow(xmlToDatabaseStep())
                .end()
                .build();
    }

    @Bean
    @Qualifier("xmlToDatabaseStep")
    public Step xmlToDatabaseStep() {
        return stepBuilderFactory.get("xmlToDatabaseJobStep")
                .<XmlToDatabaseDto, XmlToDatabaseEntity>chunk(10)
                .reader(xmlToDatabaseReaderXStream())
                .processor(xmlToDatabaseProcessor())
                .writer(xmlToDatabaseWriterWithRepository())
                .build();
    }

    @Bean
    @Qualifier("xmlToDatabaseReader")
    public ItemReader<XmlToDatabaseDto> xmlToDatabaseReaderXStream() {
        Map<String, String> aliasMap = new HashMap<>();
        aliasMap.put("XmlToDatabase", XmlToDatabaseDto.class.getName());

        XStreamMarshaller marshaller = new XStreamMarshaller();
        marshaller.setAliases(aliasMap);
        marshaller.setTypePermissions(
                new ExplicitTypePermission(
                        new Class[]{XmlToDatabaseDto.class}
                )
        );

        StaxEventItemReader<XmlToDatabaseDto> reader = new StaxEventItemReader<>();
        reader.setResource(new FileSystemResource("_04_xml_to_database/input.xml"));
        reader.setFragmentRootElementName("XmlToDatabase");
        reader.setUnmarshaller(marshaller);

        return reader;
    }

    @Bean
    @Qualifier("xmlToDatabaseReaderJaxb2")
    public ItemReader<XmlToDatabaseDto> xmlToDatabaseReaderJaxb2() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(XmlToDatabaseDto.class);

        return new StaxEventItemReaderBuilder<XmlToDatabaseDto>()
                .name("xmlToDatabaseReader")
                .resource(new FileSystemResource("_04_xml_to_database/input.xml"))
                .addFragmentRootElements("XmlToDatabase")
                .unmarshaller(marshaller)
                .build();
    }

    @Bean
    @Qualifier("xmlToDatabaseProcessor")
    public ItemProcessor<XmlToDatabaseDto, XmlToDatabaseEntity> xmlToDatabaseProcessor() {
        return input -> {
            return XmlToDatabaseEntity.builder()
                    .id(input.getId())
                    .firstName(input.getFirstName())
                    .lastName(input.getLastName())
                    .email(input.getEmail() != null ? input.getEmail().toUpperCase() : "")
                    .build();
        };
    }

    @Bean
    @Qualifier("xmlToDatabaseWriter")
    public ItemWriter<XmlToDatabaseEntity> xmlToDatabaseWriter() {
        JdbcBatchItemWriter<XmlToDatabaseEntity> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO xml_to_database (id, first_name, last_name, email) values (?, ?, ?, ?)");
        writer.setItemPreparedStatementSetter((item, ps) -> {
            ps.setInt(1, item.getId());
            ps.setString(2, item.getFirstName());
            ps.setString(3, item.getLastName());
            ps.setString(4, item.getEmail());
        });
        return writer;
    }

    @Bean
    @Qualifier("xmlToDatabaseWriterWithRepository")
    public ItemWriter<XmlToDatabaseEntity> xmlToDatabaseWriterWithRepository() {
        return list -> xmlToDatabaseRepository.saveAll(list);
    }
}
