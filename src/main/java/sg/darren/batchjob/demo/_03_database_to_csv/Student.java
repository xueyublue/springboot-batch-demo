package sg.darren.batchjob.demo._03_database_to_csv;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

}
