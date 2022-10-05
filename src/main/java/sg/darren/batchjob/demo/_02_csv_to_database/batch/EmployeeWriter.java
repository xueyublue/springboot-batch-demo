package sg.darren.batchjob.demo._02_csv_to_database.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import sg.darren.batchjob.demo._02_csv_to_database.model.Employee;
import sg.darren.batchjob.demo._02_csv_to_database.repository.EmployeeRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeWriter implements ItemWriter<Employee> {

    private final EmployeeRepository employeeRepository;

    @Override
    public void write(List<? extends Employee> list) throws Exception {
        employeeRepository.saveAll(list);
    }
}
