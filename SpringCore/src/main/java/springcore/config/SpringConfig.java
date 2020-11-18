package springcore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import springcore.orm.*;
import springcore.services.SalaryService;


@Configuration
@PropertySource("classpath:configuration.properties")
@ComponentScan("springcore")
public class SpringConfig {
    @Bean
    @Autowired
    public SalaryService getSalaryService(PositionsOrm positionsOrm,
                                          EmployeesOrm employeesOrm) {
        return new SalaryService(positionsOrm, employeesOrm);
    }
}
