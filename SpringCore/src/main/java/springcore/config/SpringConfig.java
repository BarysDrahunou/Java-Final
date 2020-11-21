package springcore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import springcore.dao.*;
import springcore.services.companyservices.SalaryService;
import springcore.services.companyservices.SalaryServiceImplementation;


@Configuration
@PropertySource("classpath:configuration.properties")
@ComponentScan("springcore")
public class SpringConfig {

    @Bean
    @Autowired
    public SalaryService getSalaryService(PositionsImplDb positionsImplDb,
                                          EmployeesImplDb employeesImplDb) {
        return new SalaryServiceImplementation(positionsImplDb, employeesImplDb);
    }
}
