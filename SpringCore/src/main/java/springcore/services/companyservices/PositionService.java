package springcore.services.companyservices;

import springcore.company.Company;
import springcore.dao.EmployeesDao;
import springcore.dao.PositionsDao;
import springcore.services.PositionCreator;


/**
 * The interface to work with positions into the company.
 */
public interface PositionService<T,U> {

    /**
     * Create new positions via positionCreator and add them to a company.
     *
     * @param positionCreator the position creator
     */
    void addPositions(PositionCreator positionCreator);

    /**
     * Assign employees to opened positions into a company
     */
    void assignPositions();

    /**
     * Clear positions after employees has been fired
     */
    void clearPositions();

    /**
     * Close positions if company doesn't need any more of current position .
     */
    void closePositions();

    /**
     * Change position for employee
     */
    void changePosition();

    /**
     * Sets company to this service
     *
     * @param company company for which current service will operate
     */
    void setCompany(Company company);

    /**
     * Gets positionsDao.
     *
     * @return positionsDao
     */
    PositionsDao<T> getPositionsDao();

    /**
     * Gets employeesDao.
     *
     * @return employeesDao
     */
    EmployeesDao<U> getEmployeesDao();
}
