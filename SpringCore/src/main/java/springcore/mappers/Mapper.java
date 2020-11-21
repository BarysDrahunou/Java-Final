package springcore.mappers;

public interface Mapper<MAP_SOURCE, MAP_TARGET, UPDATE_SOURCE, UPDATE_TARGET> {

    MAP_TARGET map(MAP_SOURCE from) throws Exception;

    void add(UPDATE_SOURCE from, UPDATE_TARGET to) throws Exception;

    void update(UPDATE_SOURCE from, UPDATE_TARGET to) throws Exception;
}
