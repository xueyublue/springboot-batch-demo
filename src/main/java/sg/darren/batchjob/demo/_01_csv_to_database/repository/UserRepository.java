package sg.darren.batchjob.demo._01_csv_to_database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.darren.batchjob.demo._01_csv_to_database.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
