package springcore.employee;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static springcore.constants.VariablesConstants.*;

@Service
public class EmployeeCreator {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, List<String>> hashNamesSurnamesMap = new HashMap<>();
    private final List<String> names;
    private final List<String> surnames;

    static {
        hashNamesSurnamesMap.put(DEFAULT_NAMES, Collections.singletonList(DEFAULT_NAME));
        hashNamesSurnamesMap.put(DEFAULT_SURNAMES, Collections.singletonList(DEFAULT_SURNAME));
    }

    @Autowired
    public EmployeeCreator(@Value("${names.path}") String namesPath,
                           @Value("${surnames.path}") String surnamesPath) {
        this.names = getField(namesPath, DEFAULT_NAMES, Collections.singletonList(DEFAULT_NAME));
        this.surnames = getField(surnamesPath, DEFAULT_SURNAMES, Collections.singletonList(DEFAULT_SURNAME));
    }

    private List<String> getField(String path, String defaultNameInMap, List<String> defaultList) {
        if (hashNamesSurnamesMap.containsKey(path)) {
            return hashNamesSurnamesMap.get(path);
        } else {
            try {
                List<String> data = Files.readAllLines(Paths.get(path));
                hashNamesSurnamesMap.put(path, data);

                return data.size() > 0 ? data : defaultList;
            } catch (IOException e) {
                LOGGER.error(e);

                return hashNamesSurnamesMap.get(defaultNameInMap);
            }
        }
    }

    public Employee createEmployeeAndGet() {
        Random random = new Random();

        String randomName = names.get(random.nextInt(names.size()));
        String randomSurname = surnames.get(random.nextInt(surnames.size()));

        return new Employee(randomName, randomSurname);
    }
}
