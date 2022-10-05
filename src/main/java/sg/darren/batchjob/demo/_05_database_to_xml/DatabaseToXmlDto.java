package sg.darren.batchjob.demo._05_database_to_xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "DatabaseToXml")
@XStreamAlias("DatabaseToXml")
public class DatabaseToXmlDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

}
