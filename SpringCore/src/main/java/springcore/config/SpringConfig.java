package springcore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import springcore.dao.*;
import springcore.services.companyservices.SalaryService;
import springcore.services.companyservices.SalaryServiceImplementation;


/**
 * The type Spring config.
 */
@Configuration
@PropertySource("classpath:configuration.properties")
@ComponentScan("springcore")
public class SpringConfig {

    /**
     * Gets the bean of the SalaryService.class
     *
     * @param positionsImplDb bean of the PositionsImplDb.class
     * @param employeesImplDb bean of the EmployeesImplDb.class
     * @return the new salary service bean
     */
    @Bean
    @Autowired
    public SalaryService<?, ?> getSalaryService(PositionsImplDb positionsImplDb,
                                                EmployeesImplDb employeesImplDb) {
        return new SalaryServiceImplementation(positionsImplDb, employeesImplDb);
    }
}
