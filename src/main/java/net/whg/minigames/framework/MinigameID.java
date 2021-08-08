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
     * @param typeID       - The minigame type ID or -1 if not a real minigame type.
     * @param instanceID   - The minigame instance ID or -1 if not a real minigame
     *                     instance.
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
     * @return The type ID, or -1 if this object does not represent an actual
     *         minigame instance.
     */
    public int getTypeID() {
        return typeID;
    }

    /**
     * Gets the instance ID for the given minigame type. Instance IDs are based
     * solely within active minigame instances within a single minigame type. These
     * are reused as minigames end.
     * 
     * @return The instance ID, or -1 if this object does not represent an actual
     *         minigame instance.
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

    /**
     * Checks if this minigame ID represents an actual minigame instance.
     * 
     * @return True if this ID is a real instance. False otherwise.
     */
    public boolean isReal() {
        return typeID >= 0 && instanceID >= 0;
    }

    /**
     * Gets the instance name of this Minigame ID. An instance name is simply the
     * minigame type followed by the instance ID, with a space between. Useful for
     * debugging while using a human-friendly display name.
     * 
     * @return The instance name.
     */
    public String instanceName() {
        return minigameType + " " + instanceID;
    }
}
