package sg.darren.batchjob.demo._01_csv_to_database.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private Long id;

    private String name;

    private String dept;

    private Integer salary;

    private Date lastUpdatedDate;

}
