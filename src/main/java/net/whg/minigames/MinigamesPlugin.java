package net.whg.minigames;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import net.whg.minigames.framework.MinigameCommand;
import net.whg.minigames.framework.MinigameManager;
import net.whg.utils.messaging.MessageUtils;

public class MinigamesPlugin extends JavaPlugin {
    private static MinigamesPlugin instance;

    private MinigameManager minigameManager;

    /**
     * Logs an informational message to the console.
     * 
     * @param message - The message to log.
     * @param args    - The message arguments.
     */
    public static void logInfo(String message, Object... args) {
        MessageUtils.logInfo(instance, message, args);
    }

    /**
     * Logs a warning message to the console.
     * 
     * @param message - The message to log.
     * @param args    - The message arguments.
     */
    public static void logWarning(String message, Object... args) {
        MessageUtils.logWarn(instance, message, args);
    }

    /**
     * Logs an error message to the console.
     * 
     * @param message - The message to log.
     * @param args    - The message arguments.
     */
    public static void logError(String message, Object... args) {
        MessageUtils.logError(instance, message, args);
    }

    @Override
    public void onEnable() {
        instance = this;

        updateDefaultConfig();

        minigameManager = new MinigameManager(this);

        getCommand("mg").setExecutor(new MinigameCommand(minigameManager));
        Bukkit.getLogger().log(Level.INFO, "HG-Minigames has been enabled.");
    }

    @Override
    public void onDisable() {
        instance = null;

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
