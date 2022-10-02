package sg.darren.batchjob.demo.csv_to_database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.darren.batchjob.demo.csv_to_database.model.User;

public interface UserRepository extends JpaRepository<Long, User> {
}
