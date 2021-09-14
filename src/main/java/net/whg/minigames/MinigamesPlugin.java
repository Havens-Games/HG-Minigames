package net.whg.minigames;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import net.whg.minigames.framework.MinigameCommand;
import net.whg.minigames.framework.MinigameManager;

public class MinigamesPlugin extends JavaPlugin {
    private MinigameManager minigameManager;

    @Override
    public void onEnable() {
        updateDefaultConfig();

        minigameManager = new MinigameManager(this);

        getCommand("mg").setExecutor(new MinigameCommand(minigameManager));
        Bukkit.getLogger().log(Level.INFO, "HG-Minigames has been enabled.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        minigameManager = null;

        Bukkit.getLogger().log(Level.INFO, "HG-Minigames has been disabled.");
    }

    private void updateDefaultConfig() {
        var config = getConfig();

        config.addDefault("MinigameWorld", "world");
        config.addDefault("ArenaDistance", 2500);

        config.options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Gets the minigame manager associated with this plugin.
     * 
     * @return The minigame manager.
     */
    public MinigameManager getMinigameManager() {
        return minigameManager;
    }
}
