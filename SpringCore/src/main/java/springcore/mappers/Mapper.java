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
     */
    MAP_TARGET map(MAP_SOURCE from);

    /**
     * Add objects
     *
     * @param from source object to add
     * @param to   target object to add
     */
    void add(UPDATE_SOURCE from, UPDATE_TARGET to);

    /**
     * Update objects
     *
     * @param from source object to update
     * @param to   target object to update
     */
    void update(UPDATE_SOURCE from, UPDATE_TARGET to);
}
