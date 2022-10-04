package sg.darren.batchjob.demo._04_project_one;

import lombok.*;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private String name;
    private String birthday;
    private String email;
    private BigInteger revenue;
    private Boolean isCustomer;

}
