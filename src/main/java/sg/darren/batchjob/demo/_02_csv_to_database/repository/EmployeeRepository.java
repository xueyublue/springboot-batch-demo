package sg.darren.batchjob.demo._02_csv_to_database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.darren.batchjob.demo._02_csv_to_database.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
