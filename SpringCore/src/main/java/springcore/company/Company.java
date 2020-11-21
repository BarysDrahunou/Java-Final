package springcore.company;

public class Company {

    private int vacancies;

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
