package net.whg.minigames.framework.arena;

import org.bukkit.Location;

/**
 * A handler for receiving schematic placeholders within the arena on build.
 */
public interface SchematicPlaceholder {
    /**
     * Gets the name tag to listen for. This should contain color codes if color
     * codes are present in the entity's name tag as well.
     * 
     * @return The name tag string to listen for.
     */
    String getNameTag();

    /**
     * Gets the minigame type this placeholder is listening to.
     * 
     * @return The minigame type.
     */
    String getMinigameType();

    /**
     * Called when an entity with the indicated name tag is discovered while
     * building the arena.
     * 
     * @param arena    - The arena that is being built.
     * @param location - The location of the entity.
     */
    void registerLocation(Arena arena, Location location);
}
