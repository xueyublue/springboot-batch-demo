package sg.darren.batchjob.demo._05_database_to_xml;

import org.springframework.data.jpa.repository.JpaRepository;
import sg.darren.batchjob.demo._03_database_to_csv.DatabaseToCsvEntity;

public interface DatabaseToXmlRepository extends JpaRepository<DatabaseToXmlEntity, Integer> {

}
