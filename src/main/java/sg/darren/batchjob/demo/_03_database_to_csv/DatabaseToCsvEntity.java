package sg.darren.batchjob.demo._03_database_to_csv;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "database_to_csv")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DatabaseToCsvEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

}
