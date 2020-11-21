package springcore.services;

import springcore.company.Company;


public interface PositionService {

    void addPositions() throws Exception;

    void assignPositions() throws Exception;

    void clearPositions() throws Exception;

    void closePositions() throws Exception;

    void changePosition() throws Exception;

    void setCompany(Company company);
}
