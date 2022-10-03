package sg.darren.batchjob.demo.csv_to_database.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Entity()
@Table(name = "user")
@Data
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String dept;

    private Integer salary;

}
