package sg.darren.batchjob.demo._91_anonymization.model;

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
