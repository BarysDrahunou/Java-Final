package springcore.employee;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static springcore.constants.VariablesConstants.*;

public class EmployeeCreator {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, List<String>> hashNamesSurnamesMap = new HashMap<>();
    private final List<String> names;
    private final List<String> surnames;

    static {
        hashNamesSurnamesMap.put(DEFAULT_NAMES, Collections.singletonList(DEFAULT_NAME));
        hashNamesSurnamesMap.put(DEFAULT_SURNAMES, Collections.singletonList(DEFAULT_SURNAME));
    }

    public EmployeeCreator(String namesPath, String surnamesPath) {
        this.names = getField(namesPath, DEFAULT_NAMES);
        this.surnames = getField(surnamesPath, DEFAULT_SURNAMES);
    }

    private List<String> getField(String path, String defaultNameInMap) {
        if (hashNamesSurnamesMap.containsKey(path)) {
            return hashNamesSurnamesMap.get(path);
        } else {
            try {
                List<String> data = Files.readAllLines(Paths.get(path));
                hashNamesSurnamesMap.put(path, data);
                return data;
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
