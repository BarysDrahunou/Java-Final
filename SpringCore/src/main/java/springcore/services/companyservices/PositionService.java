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
     * @throws Exception if positions cannot be created
     */
    void addPositions(PositionCreator positionCreator) throws Exception;

    /**
     * Assign employees to opened positions into a company
     *
     * @throws Exception if employees cannot be assigned to positions
     */
    void assignPositions() throws Exception;

    /**
     * Clear positions after employees has been fired
     *
     * @throws Exception if positions cannot be cleared
     */
    void clearPositions() throws Exception;

    /**
     * Close positions if company doesn't need any more of current position .
     *
     * @throws Exception if position cannot be closed
     */
    void closePositions() throws Exception;

    /**
     * Change position for employee
     *
     * @throws Exception if position cannot be changed
     */
    void changePosition() throws Exception;

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
