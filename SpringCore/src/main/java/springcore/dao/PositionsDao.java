package springcore.dao;


import springcore.position.Position;
import springcore.services.ConnectTemporary;

import java.util.List;

/**
 * The interface Positions dao to working with positions.
 */
public interface PositionsDao {

    /**
     * Add positions.
     *
     * @param positions the list of positions
     */
    void addPositions(List<Position> positions);

    /**
     * Gets all positions.
     *
     * @return all positions
     */
    List<Position> getAllPositions();

    /**
     * Gets positions.
     *
     * @param argument the first parameter which describes needed positions
     * @param value    the second parameter which describes needed positions
     * @return the positions
     */
    List<Position> getPositions(String argument, Object value);

    /**
     * Update positions.
     *
     * @param positions the list of positions
     */
    void updatePositions(List<Position> positions);
}
