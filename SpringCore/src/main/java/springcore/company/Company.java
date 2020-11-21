package springcore.company;

/**
 * The type Company.
 */
public class Company {

    private int vacancies;

    /**
     * Open one new vacancy.
     */
    public void openVacancy() {
        this.vacancies += 1;
    }

    /**
     * Close one vacancy.
     */
    public void closeVacancy() {
        this.vacancies -= 1;
    }

    /**
     * Gets vacancies count.
     *
     * @return the vacancies count
     */
    public int getVacanciesCount() {
        return vacancies;
    }
}
