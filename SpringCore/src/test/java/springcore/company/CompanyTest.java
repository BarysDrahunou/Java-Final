package springcore.company;

import org.junit.Test;

import static org.junit.Assert.*;

public class CompanyTest {

    Company company=new Company();

    @Test
    public void companyTest() {

        assertEquals(0, company.getVacanciesCount());
        company.openVacancy();
        assertEquals(1, company.getVacanciesCount());
        company.openVacancy();
        company.openVacancy();
        assertEquals(3, company.getVacanciesCount());
        company.closeVacancy();
        assertEquals(2,company.getVacanciesCount());
    }
}