package springcore.services.companyservices;

import springcore.company.Company;
import springcore.services.PositionCreator;


public interface PositionService {

    void addPositions(PositionCreator positionCreator) throws Exception;

    void assignPositions() throws Exception;

    void clearPositions() throws Exception;

    void closePositions() throws Exception;

    void changePosition() throws Exception;

    void setCompany(Company company);
}
