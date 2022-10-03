package sg.darren.batchjob.demo._01_csv_to_database.batch;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sg.darren.batchjob.demo._01_csv_to_database.model.Employee;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class EmployeeProcessor implements ItemProcessor<Employee, Employee> {

    private static final Map<String, String> DEPT_NAMES = new HashMap<>();

    public EmployeeProcessor() {
        DEPT_NAMES.put("001", "Information Technology");
        DEPT_NAMES.put("002", "Operations");
        DEPT_NAMES.put("003", "Sales and Marketing");
    }

    @Override
    public Employee process(Employee user) {
        String deptIn = user.getDept();
        String deptConverted = DEPT_NAMES.get(deptIn);
        user.setDept(deptConverted);
        log.info(String.format("Converted Dept from %s to %s", deptIn, deptConverted));
        user.setLastUpdatedDate(new Date());
        return user;
    }
}
