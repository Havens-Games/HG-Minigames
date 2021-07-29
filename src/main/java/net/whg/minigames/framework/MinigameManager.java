package net.whg.minigames.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

/**
 * A manager object for handling active minigames, factory objects, and active
 * players within those minigames.
 */
public class MinigameManager {
    private final List<MinigameFactory> minigameTypes = new ArrayList<>();
    private final List<Minigame> activeMinigames = new ArrayList<>();
    private final Map<Player, Minigame> activePlayers = new HashMap<>();
    private final Plugin plugin;

    /**
     * Creates a new minigame manager.
     * 
     * @param plugin - The plugin that owns this manager.
     */
    public MinigameManager(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Registers a new minigame type. This will allow the given factory to be used
     * to initialize future minigame instances.
     * 
     * @param factory - The minigame factory.
     */
    public void registerMinigameType(MinigameFactory factory) {
        minigameTypes.add(factory);
    }

    /**
     * Initialized a new minigame instance for the given minigame name.
     * 
     * @param name - The name of the minigame to initialize.
     * @return The new minigame instance, or null if there is no existing minigame
     *         with the given name.
     */
    public Minigame initializeMinigame(String name) {
        for (var factory : minigameTypes) {
            if (factory.getName().equals(name)) {
                var minigame = factory.createInstance(this);
                activeMinigames.add(minigame);

                plugin.getServer().getPluginManager().registerEvents(minigame, plugin);
                return minigame;
            }
        }

        return null;
    }

    /**
     * A package-level function for marking a minigame instance as completed.
     * 
     * @param minigame - The minigame instance.
     */
    void endMinigame(Minigame minigame) {
        activeMinigames.remove(minigame);
        HandlerList.unregisterAll(minigame);
    }

    /**
     * A package-level function for marking a player as currently part of the given
     * minigame instance.
     * 
     * @param player   - The player.
     * @param minigame - The minigame instance.
     */
    void addPlayer(Player player, Minigame minigame) {
        activePlayers.put(player, minigame);
    }

    /**
     * A package-level function for marking a player as no longer part of any
     * minigame instances.
     * 
     * @param player - The player.
     */
    void removePlayer(Player player) {
        activePlayers.remove(player);
    }

    /**
     * Gets the minigame instance that the given player is currently part of.
     * 
     * @param player - The player.
     * @return The active minigame instance, or null if the player is not currently
     *         part of any minigames.
     */
    public Minigame getCurrentMinigame(Player player) {
        return activePlayers.get(player);
    }
}
