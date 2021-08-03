package net.whg.minigames.framework;

import java.util.UUID;

/**
 * Used to identify minigame active IDs.
 */
public class MinigameID {
    private final String minigameType;
    private final int typeID;
    private final int instanceID;
    private final UUID uniqueID;

    /**
     * Creates a new MinigameID object.
     * 
     * @param minigameType - The minigame type name.
     * @param typeID       - The minigame type ID.
     * @param instanceID   - The minigame instance ID.
     */
    public MinigameID(String minigameType, int typeID, int instanceID) {
        this.minigameType = minigameType;
        this.typeID = typeID;
        this.instanceID = instanceID;
        uniqueID = UUID.randomUUID();
    }

    /**
     * Gets the minigame type name.
     * 
     * @return The minigame type name.
     */
    public String getMinigameType() {
        return minigameType;
    }

    /**
     * Gets the minigame type ID. This is used to identify the minigame type.
     * 
     * @return The type ID.
     */
    public int getTypeID() {
        return typeID;
    }

    /**
     * Gets the instance ID for the given minigame type. Instance IDs are based
     * solely within active minigame instances within a single minigame type. These
     * are reused as minigames end.
     * 
     * @return The instance ID.
     */
    public int getInstanceID() {
        return instanceID;
    }

    /**
     * Gets the globally unique ID of the minigame instance. This is randomly
     * generated and is always unique to this specific game instance, even across
     * server restarts and game types.
     * 
     * @return The unique ID.
     */
    public UUID getUniqueID() {
        return uniqueID;
    }
}
