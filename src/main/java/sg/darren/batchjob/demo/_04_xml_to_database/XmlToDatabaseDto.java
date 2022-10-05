package sg.darren.batchjob.demo._04_xml_to_database;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "XmlToDatabase")
public class XmlToDatabaseDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

}
