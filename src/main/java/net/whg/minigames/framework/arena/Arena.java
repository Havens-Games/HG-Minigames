package net.whg.minigames.framework.arena;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;

import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * Represents an arena instance in the world.
 */
public class Arena {
    private final ArenaDistributor distributor;
    private final Location location;
    private final String name;
    private final File file;

    /**
     * Creates a new arena instance.
     * 
     * @param distributor - The arena distributor that made this arena instance.
     * @param location    - The location of this arena.
     * @param name        - The name of this arena type.
     */
    Arena(ArenaDistributor distributor, Location location, String name) {
        this.distributor = distributor;
        this.location = location;
        this.name = name;

        var worldEdit = Bukkit.getPluginManager().getPlugin("WorldEdit");
        var schematicFolder = new File(worldEdit.getDataFolder(), "schematics");
        file = new File(schematicFolder, name + ".schem");
    }

    /**
     * Gets the name of this arena type.
     * 
     * @return The name.
     */
    public String getName() {
        return name;
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
     * Loads the schematic file that is associated with this arena type. The
     * schematic file is a WorldEdit schematic that has the same name as this arena.
     * 
     * @throws IOException - If the schematic file could not be loaded.
     */
    public Clipboard loadSchematic() throws IOException {
        var format = ClipboardFormats.findByFile(file);
        try (var reader = format.getReader(new FileInputStream(file))) {
            return reader.read();
        }
    }

    /**
     * Creates a task to start building the arena schematic.
     * 
     * @return The active build task.
     * @throws IOException If the schematic file could not be loaded.
     */
    public ArenaBuildTask buildArena() throws IOException {
        var schematic = loadSchematic();

        var placeholders = distributor.getPlaceholders(name);
        var task = new ArenaBuildTask(placeholders, this, location, schematic);

        var plugin = Bukkit.getPluginManager().getPlugin("HavensGames-Minigames");
        task.runTaskTimer(plugin, 1, 1);

        return task;
    }
}
