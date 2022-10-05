package sg.darren.batchjob.demo._04_xml_to_database;

import org.springframework.data.jpa.repository.JpaRepository;

public interface XmlToDatabaseRepository extends JpaRepository<XmlToDatabaseEntity, Integer> {

}
