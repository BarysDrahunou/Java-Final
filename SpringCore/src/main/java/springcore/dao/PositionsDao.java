package springcore.dao;

import springcore.position.Position;

import java.util.List;

public interface PositionsDao {

    void addPositions(List<Position> positions) throws Exception;

    List<Position> getAllPositions() throws Exception;

    List<Position> getPositions(String argument, Object value) throws Exception;

    void updatePositions(List<Position> positions) throws Exception;
}
