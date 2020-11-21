package springcore.dao;


import springcore.services.ConnectTemporary;

/**
 * The interface Positions dao to working with positions.
 */
public interface PositionsDao<SOURCE, TARGET> {

    /**
     * Add positions.
     *
     * @param positions the bundle of positions
     * @throws Exception if positions cannot be inserted
     */
    void addPositions(SOURCE positions) throws Exception;

    /**
     * Gets all positions.
     *
     * @return the all positions
     * @throws Exception the exception if positions cannot be got
     */
    TARGET getAllPositions() throws Exception;

    /**
     * Gets positions.
     *
     * @param argument the first parameter which describes needed positions
     * @param value    the second parameter which describes needed positions
     * @return the positions
     * @throws Exception the exception if positions cannot be got
     */
    TARGET getPositions(String argument, Object value) throws Exception;

    /**
     * Update positions.
     *
     * @param positions the bundle of positions
     * @throws Exception the exception if positions cannot be got
     */
    void updatePositions(SOURCE positions) throws Exception;

    /**
     * Gets connect temporary.
     *
     * @return the connect temporary
     */
    ConnectTemporary getConnectTemporary();
}
