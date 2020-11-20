package springcore.services;

import springcore.company.Company;

import java.sql.SQLException;

public interface PositionService {

    void addPositions() throws SQLException;

    void assignPositions() throws SQLException;

    void clearPositions() throws SQLException;

    void closePositions() throws SQLException;

    void changePosition() throws SQLException;

    void setCompany(Company company);
}
