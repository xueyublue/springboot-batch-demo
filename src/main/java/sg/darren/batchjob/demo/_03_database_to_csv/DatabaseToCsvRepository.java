package sg.darren.batchjob.demo._03_database_to_csv;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DatabaseToCsvRepository extends JpaRepository<DatabaseToCsvEntity, Integer> {

}
