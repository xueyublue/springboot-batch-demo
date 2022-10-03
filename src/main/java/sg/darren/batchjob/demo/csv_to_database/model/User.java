package sg.darren.batchjob.demo.csv_to_database.model;

import lombok.*;

import javax.persistence.*;

@Entity()
@Table(name = "users")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String dept;

    private Integer salary;

}
