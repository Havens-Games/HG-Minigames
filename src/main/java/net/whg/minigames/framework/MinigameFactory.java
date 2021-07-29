package net.whg.minigames.framework;

/**
 * A factory object for creating minigame instances.
 */
public interface MinigameFactory {
    /**
     * Creates a new instance of the indicated minigame.
     * 
     * @param manager - The minigame manager instance that triggered this action.
     * @return The new minigame instance.
     */
    public Minigame createInstance(MinigameManager manager);

    /**
     * Gets the name of the minigame.
     * 
     * @return The minigame name.
     */
    public String getName();
}
