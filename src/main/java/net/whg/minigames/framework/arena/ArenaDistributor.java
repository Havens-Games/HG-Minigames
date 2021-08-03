package net.whg.minigames.framework.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

import net.whg.minigames.framework.MinigameID;

/**
 * A handler class for creating new arena instances as needed for new minigame
 * instances.
 */
public class ArenaDistributor {
    private final List<SchematicPlaceholder> placeholders = new ArrayList<>();
    private final int arenaDistance;
    private final World world;

    /**
     * Creates a new ArenaDistributor object.
     * 
     * @param world - The world this distributor operates in.
     */
    public ArenaDistributor(Plugin plugin) {
        var config = plugin.getConfig();

        arenaDistance = config.getInt("ArenaDistance");
        world = Bukkit.getWorld(config.getString("MinigameWorld"));
    }

    /**
     * Gets the target location for the given minigame type ID and instance ID.
     * 
     * @param typeID     - The type ID of the minigame.
     * @param instanceID - The instance ID of the minigame.
     * @return The arena location for the given instance.
     */
    public Location getLocation(int typeID, int instanceID) {
        var x = (typeID + 1) * arenaDistance;
        var y = 64;
        var z = (instanceID + 1) * arenaDistance;
        return new Location(world, x, y, z);
    }

    /**
     * Creates a new arena instance for the given minigame ID.
     * 
     * @param id - The minigame ID.
     * @return The arena instance.
     */
    public Arena getArena(MinigameID id) {
        var location = getLocation(id.getTypeID(), id.getInstanceID());
        return new Arena(this, location, id.getMinigameType());
    }

    /**
     * Returns an unsupervised arena instance at the given location with the given
     * minigame type.
     * 
     * @param minigameType - The minigame type.
     * @param location     - The location of the arena.
     * @return A new arena instance.
     */
    public Arena getArenaUnsupervised(String minigameType, Location location) {
        return new Arena(this, location, minigameType);
    }

    /**
     * Registers a new placeholder type to listen for when building new arena
     * instances.
     * 
     * @param placeholder - The placeholder.
     */
    public void registerPlaceholder(SchematicPlaceholder placeholder) {
        placeholders.add(placeholder);
    }

    /**
     * Gets a list of all registered placeholders for the requested minigame type.
     * 
     * @param minigameType - The minigame type.
     * @return A list of placeholders.
     */
    List<SchematicPlaceholder> getPlaceholders(String minigameType) {
        return placeholders.stream().filter(p -> p.getMinigameType().equals(minigameType)).toList();
    }
}
