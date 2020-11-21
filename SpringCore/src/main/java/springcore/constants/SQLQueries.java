package springcore.constants;

public final class SQLQueries {

    public static final String EMPLOYEES_TABLE_TRUNCATE_SQL =
            "TRUNCATE TABLE SPRING.EMPLOYEES";
    public static final String POSITIONS_TABLE_TRUNCATE_SQL =
            "TRUNCATE TABLE SPRING.POSITIONS";
    public static final String ADD_EMPLOYEES_QUERY =
            "INSERT INTO SPRING.EMPLOYEES (NAME,SURNAME,STATUS,PERSONAL_BONUSES) " +
                    "VALUES (?,?,?,?)";
    public static final String GET_EMPLOYEES_BY_STATUS_QUERY =
            "SELECT * FROM SPRING.EMPLOYEES WHERE STATUS = ?";
    public static final String UPDATE_EMPLOYEES_QUERY =
            "UPDATE SPRING.EMPLOYEES SET NAME=?, SURNAME=?, STATUS=?, POSITION=?," +
                    " PERSONAL_BONUSES=?, TIME_WORKED=? WHERE ID = ?";
    public static final String UPDATE_EMPLOYEES_STATUS_BY_STATUS_QUERY =
            "UPDATE SPRING.EMPLOYEES SET STATUS =? WHERE STATUS = ?";
    public static final String ADD_POSITIONS_QUERY =
            "INSERT INTO SPRING.POSITIONS (POSITION, VACANCIES) VALUES (?,?)";
    public static final String GET_ALL_POSITIONS_QUERY =
            "SELECT * FROM SPRING.POSITIONS";
    public static final String GET_EXACT_POSITIONS_QUERY =
            "SELECT * FROM SPRING.POSITIONS WHERE %s ?";
    public static final String GET_POSITION_SALARY_QUERY =
            "SELECT SALARY FROM SPRING.POSITIONS WHERE POSITION = ?";
    public static final String UPDATE_POSITIONS_QUERY =
            "UPDATE SPRING.POSITIONS SET VACANCIES = ?, ACTIVE_WORKERS =?," +
                    " SALARY=? WHERE POSITION =?";
    public static final String ASSIGN_SALARIES_QUERY =
            "UPDATE SPRING.POSITIONS SET SALARY = ? WHERE POSITION =?";

    public static final String NAME = "NAME";
    public static final String SURNAME = "SURNAME";
    public static final String ID = "ID";
    public static final String STATUS = "STATUS";
    public static final String POSITION = "POSITION";
    public static final String PERSONAL_BONUSES = "PERSONAL_BONUSES";
    public static final String TIME_WORKED = "TIME_WORKED";
    public static final String VACANCIES = "VACANCIES";
    public static final String ACTIVE_WORKERS = "ACTIVE_WORKERS";
    public static final String SALARY = "SALARY";
    public static final String OPENED_VACANCIES_QUERY = "VACANCIES !=";
    public static final String POSITION_QUERY = "POSITION =";
    public static final String SALARY_QUERY = "SALARY =";
}
