package springcore.dao;


import springcore.services.ConnectTemporary;

/**
 * The interface Positions dao to working with positions.
 */
public interface PositionsDao<T> {

    /**
     * Add positions.
     *
     * @param positions the bundle of positions
     */
    void addPositions(T positions);

    /**
     * Gets all positions.
     *
     * @return the all positions
     */
    T getAllPositions();

    /**
     * Gets positions.
     *
     * @param argument the first parameter which describes needed positions
     * @param value    the second parameter which describes needed positions
     * @return the positions
     */
    T getPositions(String argument, Object value);

    /**
     * Update positions.
     *
     * @param positions the bundle of positions
     */
    void updatePositions(T positions);

    /**
     * Gets connect temporary.
     *
     * @return the connect temporary
     */
    ConnectTemporary getConnectTemporary();
}
