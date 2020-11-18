package springcore.company;

import org.springframework.stereotype.Component;

@Component
public class Company {

    private int vacancies;

    public Company() {
    }

    public void openVacancy() {
        this.vacancies += 1;
    }

    public void closeVacancy() {
        this.vacancies -= 1;
    }

    public int getVacanciesCount() {
        return vacancies;
    }
}
