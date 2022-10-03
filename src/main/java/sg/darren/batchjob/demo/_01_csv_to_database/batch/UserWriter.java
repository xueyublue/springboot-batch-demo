package sg.darren.batchjob.demo._01_csv_to_database.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import sg.darren.batchjob.demo._01_csv_to_database.model.User;
import sg.darren.batchjob.demo._01_csv_to_database.repository.UserRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserWriter implements ItemWriter<User> {

    private final UserRepository userRepository;

    @Override
    public void write(List<? extends User> list) throws Exception {
        userRepository.saveAll(list);
    }
}
