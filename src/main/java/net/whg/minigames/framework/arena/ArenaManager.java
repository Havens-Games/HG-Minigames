package net.whg.minigames.framework.arena;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import net.whg.minigames.framework.MinigameID;
import net.whg.minigames.framework.SchematicPlaceholder;
import net.whg.minigames.framework.events.ArenaCompletedEvent;
import net.whg.whsculpt.events.FinishedBuildingSchematicEvent;

/**
 * A handler class for creating new arena instances as needed for new minigame
 * instances.
 */
public class ArenaManager implements Listener {
    private final List<Arena> arenas = new ArrayList<>();
    private final List<SchematicPlaceholder> placeholders = new ArrayList<>();
    private final int arenaDistance;
    private final World world;

    /**
     * Creates a new ArenaDistributor object.
     * 
     * @param world - The world this distributor operates in.
     */
    public ArenaManager(Plugin plugin) {
        var config = plugin.getConfig();

        arenaDistance = config.getInt("ArenaDistance");
        world = Bukkit.getWorld(config.getString("MinigameWorld"));

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Gets the target location for the given minigame type ID and instance ID.
     * 
     * @param typeID     - The type ID of the minigame.
     * @param instanceID - The instance ID of the minigame.
     * @return The arena location for the given instance.
     */
    private Location getLocation(int typeID, int instanceID) {
        var x = (typeID + 1) * arenaDistance;
        var y = 64;
        var z = (instanceID + 1) * arenaDistance;
        return new Location(world, x, y, z);
    }

    /**
     * Gets or creates a new arena instance for the given minigame ID.
     * 
     * @param id - The minigame ID.
     * @return The arena instance.
     */
    public Arena getArena(MinigameID id) {
        for (var arena : arenas) {
            var arenaID = arena.getID();
            if (arenaID.getTypeID() == id.getTypeID() && arenaID.getInstanceID() == id.getInstanceID())
                return arena;
        }

        var location = getLocation(id.getTypeID(), id.getInstanceID());
        var arena = new Arena(location, id);
        arenas.add(arena);

        return arena;
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

    /**
     * Gets the arena with the corresponding location object. This checks for
     * variable instance equality rather than matching coordinates.
     * 
     * @param location - The location instance to look for.
     * @return
     */
    private Arena getArena(Location location) {
        for (var arena : arenas)
            if (arena.getLocation() == location)
                return arena;

        return null;
    }

    @EventHandler
    public void onSchematicBuildFinished(FinishedBuildingSchematicEvent e) {
        var arena = getArena(e.getLocation());
        if (arena == null)
            return;

        arena.setState(ArenaState.READY);

        var event = new ArenaCompletedEvent(arena);
        Bukkit.getPluginManager().callEvent(event);
    }
}
