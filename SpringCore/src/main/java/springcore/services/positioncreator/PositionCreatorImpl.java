package springcore.services.positioncreator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import springcore.currency.Usd;
import springcore.position.Position;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static springcore.constants.VariablesConstants.*;

/**
 * Class for creating new Positions with random jobs from input files.
 */
@Service
public class PositionCreatorImpl implements PositionCreator {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Map<String, List<String>> hashJobsMap = new HashMap<>();
    private final List<String> jobs;

    static {
        hashJobsMap.put(DEFAULT_JOBS, Collections.singletonList(DEFAULT_JOB));
    }

    /**
     * Instantiates a new Position creator.
     *
     * @param jobsPath the path to file with list of jobs from which will be chosen job for
     *                 position. If path is wrong, the default job are assigned to all positions
     *                 created by this creator.
     */
    @Autowired
    public PositionCreatorImpl(@Value("${jobs.path}") String jobsPath) {
        this.jobs = getJobs(jobsPath);
    }

    /**
     * Create position and get position.
     *
     * @return the position
     */
    @Override
    public Position createPositionAndGet() {
        Random random = new Random();

        String positionName = jobs.get(random.nextInt(jobs.size()));
        Position position = new Position(positionName);

        position.setVacancies(INITIAL_VACANCIES);
        position.setActiveWorkers(INITIAL_WORKERS);
        position.setSalary(new Usd(INITIAL_SALARY));

        return position;
    }

    private List<String> getJobs(String path) {
        if (hashJobsMap.containsKey(path)) {
            return hashJobsMap.get(path);
        } else {
            try {
                List<String> data = Files.readAllLines(Paths.get(path));
                hashJobsMap.put(path, data);

                return data.size() > DECIMAL_BASE ? data : Collections.singletonList(DEFAULT_JOB);
            } catch (IOException e) {
                LOGGER.error(e);

                return hashJobsMap.get(DEFAULT_JOBS);
            }
        }
    }
}
