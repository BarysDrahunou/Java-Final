package springcore.mappers;

/**
 * Interface to map objects.
 *
 * @param <MAP_SOURCE>   map source from
 * @param <MAP_TARGET>   map target to
 * @param <UPDATE_SOURCE> update source from
 * @param <UPDATE_TARGET> update target to
 */
public interface Mapper<MAP_SOURCE, MAP_TARGET, UPDATE_SOURCE, UPDATE_TARGET> {

    /**
     * Map objects
     *
     * @param from source object to map
     * @return target object
     * @throws Exception if data cannot be retrieved
     */
    MAP_TARGET map(MAP_SOURCE from) throws Exception;

    /**
     * Add objects
     *
     * @param from source object to add
     * @param to   target object to add
     * @throws Exception if data cannot be added
     */
    void add(UPDATE_SOURCE from, UPDATE_TARGET to) throws Exception;

    /**
     * Update objects
     *
     * @param from source object to update
     * @param to   target object to update
     * @throws Exception if data cannot be updated
     */
    void update(UPDATE_SOURCE from, UPDATE_TARGET to) throws Exception;
}
