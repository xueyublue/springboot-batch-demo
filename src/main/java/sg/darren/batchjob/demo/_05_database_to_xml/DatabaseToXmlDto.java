package sg.darren.batchjob.demo._05_database_to_xml;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseToXmlDto {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

}
