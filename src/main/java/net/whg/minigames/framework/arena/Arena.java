package net.whg.minigames.framework.arena;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import net.whg.minigames.framework.MinigameID;
import net.whg.whsculpt.schematic.Schematic;
import net.whg.whsculpt.schematic.SchematicBuildTask;

/**
 * Represents an arena instance in the world.
 */
public class Arena {
    private final Location location;
    private final MinigameID id;
    private final File file;
    private Schematic schematic;
    private ArenaState state = ArenaState.NOT_BUILT;

    /**
     * Creates a new arena instance.
     * 
     * @param location - The location of this arena.
     * @param id       - The ID of the minigame this arena is for.
     */
    Arena(Location location, MinigameID id) {
        this.location = location;
        this.id = id;

        // TODO Find a better way for store/load schematics.
        var worldEdit = Bukkit.getPluginManager().getPlugin("WorldEdit");
        var schematicFolder = new File(worldEdit.getDataFolder(), "schematics");
        file = new File(schematicFolder, id.getMinigameType() + ".schem");
    }

    /**
     * Gets the minigame ID of this arena.
     * 
     * @return The minigame ID.
     */
    public MinigameID getID() {
        return id;
    }

    /**
     * Gets the location of this arena instance.
     * 
     * @return The location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Creates a build task in WraithavenSculpt. to start building the arena
     * schematic.
     * 
     * @throws IOException If the schematic file could not be loaded.
     */
    public void buildArena() throws IOException {
        if (schematic == null)
            schematic = Schematic.loadSchematic(file);

        setState(ArenaState.BUILDING);
        new SchematicBuildTask(schematic, location).start();
    }

    /**
     * Gets the schematic associated with this arena. The schematic object is not
     * loaded until {@link #buildArena()} is called at least once.
     * 
     * @return The schematic, or null if the schematic has not been loaded yet.
     */
    public Schematic getSchematic() {
        return schematic;
    }

    /**
     * Gets the current state of this arena.
     * 
     * @return The current arena state.
     */
    public ArenaState getState() {
        return state;
    }

    /**
     * Sets the state of this arena.
     * 
     * @param state - The new state.
     */
    void setState(ArenaState state) {
        this.state = state;
    }
}
