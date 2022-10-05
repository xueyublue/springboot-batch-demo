package sg.darren.batchjob.demo._05_database_to_xml;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "database_to_xml")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseToXmlEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

}
