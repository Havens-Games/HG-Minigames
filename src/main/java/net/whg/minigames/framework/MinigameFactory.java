package net.whg.minigames.framework;

/**
 * A factory object for creating minigame instances.
 */
public interface MinigameFactory {
    /**
     * Creates a new instance of the indicated minigame.
     * 
     * @param manager - The minigame manager instance that triggered this action.
     * @param id      - The minigame ID.
     * @return The new minigame instance.
     */
    Minigame createInstance(MinigameManager manager, MinigameID id);

    /**
     * Gets the name of the minigame.
     * 
     * @return The minigame name.
     */
    String getName();

    /**
     * Gets whether or not this minigame is instanced. Non-instanced minigames are
     * created instantly upon registering them, and players are instantly added to
     * them at any time. Instanced minigames are given a virtual lobby to join and
     * all players join it at the same time.
     * 
     * @return True if the minigame is instanced. False if the minigame has only one
     *         instance.
     */
    boolean isInstanced();

    /**
     * Gets the minimum number of players required to create a new minigame
     * instance.
     * 
     * @return The minimum number of players to start this minigame.
     */
    int getMinPlayers();

    /**
     * Gets the maximum number of players allowed to join a new minigame instance.
     * 
     * @return The maximum number of players allowed into this minigame.
     */
    int getMaxPlayers();

    /**
     * Gets an array of all schematic placeholders for this minigame type to
     * register.
     * 
     * @return The schematic placeholders.
     */
    SchematicPlaceholder[] getPlaceholders();
}
