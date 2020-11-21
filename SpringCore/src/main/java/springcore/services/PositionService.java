package springcore.services;

import springcore.company.Company;
import springcore.position.PositionCreator;


public interface PositionService {

    void addPositions(PositionCreator positionCreator) throws Exception;

    void assignPositions() throws Exception;

    void clearPositions() throws Exception;

    void closePositions() throws Exception;

    void changePosition() throws Exception;

    void setCompany(Company company);
}
