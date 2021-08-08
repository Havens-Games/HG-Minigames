package net.whg.minigames.framework;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

import net.whg.minigames.framework.arena.ArenaManager;
import net.whg.minigames.framework.events.JoinLobbyEvent;
import net.whg.minigames.framework.events.MinigameStartEvent;

/**
 * A manager object for handling active minigames, factory objects, and active
 * players within those minigames.
 */
public class MinigameManager {
    private final List<MinigameFactory> minigameTypes = new ArrayList<>();
    private final List<Minigame> activeMinigames = new ArrayList<>();
    private final List<VirtualLobby> lobbies = new ArrayList<>();
    private final Plugin plugin;
    private final ArenaManager arenaDistributor;

    /**
     * Creates a new minigame manager.
     * 
     * @param plugin - The plugin that owns this manager.
     */
    public MinigameManager(Plugin plugin) {
        this.plugin = plugin;
        arenaDistributor = new ArenaManager(plugin);
    }

    /**
     * Registers a new minigame type. This will allow the given factory to be used
     * to initialize future minigame instances. If the minigame type is not
     * instanced, the minigame will be initialized instantly.
     * 
     * @param factory - The minigame factory.
     */
    public void registerMinigameType(MinigameFactory factory) {
        minigameTypes.add(factory);

        if (factory.isInstanced()) {
            var lobby = new VirtualLobby(this, factory.getName());
            Bukkit.getPluginManager().registerEvents(lobby, plugin);
            lobbies.add(lobby);
        } else {
            initializeMinigame(factory.getName());
        }
    }

    /**
     * Initialized a new minigame instance for the given minigame name.
     * 
     * @param name - The name of the minigame to initialize.
     * @return The new minigame instance, or null if there is no existing minigame
     *         with the given name.
     */
    public Minigame initializeMinigame(String name) {
        var factory = getMinigameFactory(name);
        if (factory == null)
            return null;

        var id = generateID(name);
        var arena = arenaDistributor.getArena(id);

        var minigame = factory.createInstance();
        minigame.init(this, id, arena, factory.isInstanced());
        activeMinigames.add(minigame);

        try {
            arena.buildArena();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, e, () -> "Failed to build arena for " + name + "!");
        }

        Bukkit.getPluginManager().registerEvents(minigame, plugin);

        var event = new MinigameStartEvent(minigame);
        Bukkit.getPluginManager().callEvent(event);

        return minigame;
    }

    /**
     * Generates an available MinigameID object for the given minigame type.
     * 
     * @param minigameType - The name of the minigame type.
     * @return A new MinigameID.
     */
    private MinigameID generateID(String minigameType) {
        var typeID = minigameTypes.indexOf(getMinigameFactory(minigameType));
        var instanceID = 0;

        while (findMinigame(typeID, instanceID) != null)
            instanceID++;

        return new MinigameID(minigameType, typeID, instanceID);
    }

    /**
     * Finds the active minigame with the given type ID and instance ID.
     * 
     * @param typeID     - The type ID.
     * @param instanceID - The instance ID.
     * @return The active minigame, or null if the minigame could not be found.
     */
    private Minigame findMinigame(int typeID, int instanceID) {
        for (var minigame : activeMinigames) {
            var id = minigame.getID();
            if (id.getTypeID() == typeID && id.getInstanceID() == instanceID)
                return minigame;
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
     * Gets the minigame instance that the given player is currently part of.
     * 
     * @param player - The player.
     * @return The active minigame instance, or null if the player is not currently
     *         part of any minigames.
     */
    public Minigame getCurrentMinigame(Player player) {
        for (var minigame : activeMinigames) {
            if (minigame.getPlayers().contains(player))
                return minigame;
        }

        return null;
    }

    /**
     * Gets the minigame factory with the give name.
     * 
     * @param name - The name of the minigame.
     * @return The minigame factory with the given name, or null if there is no
     *         registered minigame with the given name.
     */
    public MinigameFactory getMinigameFactory(String name) {
        for (var factory : minigameTypes) {
            if (factory.getName().equals(name))
                return factory;
        }

        return null;
    }

    /**
     * Gets the lobby for the given minigame.
     * 
     * @param name - The name of the minigame.
     * @return The minigame lobby.
     */
    private VirtualLobby getLobby(String name) {
        for (var lobby : lobbies) {
            if (lobby.getMinigame().equals(name))
                return lobby;
        }

        return null;
    }

    /**
     * Adds a player to the given lobby. This will remove the player from all other
     * lobbies.
     * 
     * @param player   - The player to add.
     * @param minigame - The name of the minigame.
     */
    public void addToLobby(Player player, String minigame) {
        var lobby = getLobby(minigame);
        if (lobby == null)
            throw new IllegalArgumentException("Minigame: '" + minigame + "' does not have a lobby or does not exist!");

        var event = new JoinLobbyEvent(player, minigame);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;

        for (var lob : lobbies)
            lob.removePlayerFromLobby(player);

        lobby.addPlayerToLobby(player);
    }

    /**
     * Gets the first active minigame with the given name. If multiple instances of
     * the minigame are active, this method returns the oldest instance.
     * 
     * @param name - The name of the minigame/
     * @return The active minigame instance, or null if there are no active
     *         minigames with the given name.
     */
    public Minigame findActiveMinigame(String name) {
        for (var minigame : activeMinigames) {
            if (minigame.getID().getMinigameType().equals(name))
                return minigame;
        }

        return null;
    }

    /**
     * Removes the given player from any minigames or lobbies that they are
     * currently in.
     * 
     * @param player - The player.
     */
    public void removeFromAllMinigames(Player player) {
        for (var minigame : activeMinigames) {
            minigame.removePlayer(player);
        }

        for (var lobby : lobbies) {
            lobby.removePlayerFromLobby(player);
        }
    }

    /**
     * Gets the arena distributor associated with this server.
     * 
     * @return The arena distributor.
     */
    public ArenaManager getArenaDistributor() {
        return arenaDistributor;
    }
}
