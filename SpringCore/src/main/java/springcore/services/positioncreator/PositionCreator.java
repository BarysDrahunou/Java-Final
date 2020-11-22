package springcore.services.positioncreator;

import springcore.position.Position;

/**
 * The interface Position creator.
 */
public interface PositionCreator {

    /**
     * Create position and get position.
     *
     * @return the position which was created
     */
    Position createPositionAndGet();
}
