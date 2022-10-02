package sg.darren.batchjob.demo.csv_to_database.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import sg.darren.batchjob.demo.csv_to_database.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class Processor implements ItemProcessor<User, User> {

    private static final Map<String, String> DEPT_NAMES = new HashMap<>();

    public Processor() {
        DEPT_NAMES.put("001", "Information Technology");
        DEPT_NAMES.put("002", "Operations");
        DEPT_NAMES.put("003", "Sales and Marketing");
    }

    @Override
    public User process(User user) throws Exception {
        return null;
    }
}
