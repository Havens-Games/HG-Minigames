package net.whg.minigames.framework.arena;

import java.util.function.Supplier;

/**
 * A subtask for arena building that iterates over a 3D region and preforms an
 * update on that position in 3D space..
 */
public class ArenaBuildSubtask {
    private final RegionIterator iterator;
    private final Supplier<Boolean> onUpdate;
    private final int iterationsPerTick;

    /**
     * Creates a new ArenaBuildSubtask.
     * 
     * @param iterator          - The region iterator this subtask operates on.
     * @param iterationsPerTick - The maximum number of iterations to preform each
     *                          tick. This only counts iterations where onUpdate
     *                          returns true.
     * @param onUpdate          - The update function to call for each position.
     *                          Returns true if the iteration counter should be
     *                          increased, or false to not increase the iteration
     *                          counter and move on to the next position instantly.
     */
    public ArenaBuildSubtask(RegionIterator iterator, int iterationsPerTick, Supplier<Boolean> onUpdate) {
        this.iterator = iterator;
        this.iterationsPerTick = iterationsPerTick;
        this.onUpdate = onUpdate;
    }

    /**
     * Calls the onUpdate function for as many positions within the region as
     * possible. This should be called every tick.
     * 
     * @return True if the subtask is completed. False otherwise.
     */
    public boolean update() {
        var updated = 0;
        while (updated < iterationsPerTick && !iterator.isDone()) {
            if (Boolean.TRUE.equals(onUpdate.get()))
                updated++;

            iterator.update();
        }

        return iterator.isDone();
    }
}
