package net.whg.minigames.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.whg.minigames.framework.arena.Arena;
import net.whg.minigames.framework.events.JoinMinigameEvent;
import net.whg.minigames.framework.events.LeaveMinigameEvent;
import net.whg.minigames.framework.events.MinigameEndEvent;
import net.whg.minigames.framework.exceptions.PlayerAlreadyInMinigameException;
import net.whg.utils.player.InventorySnapshot;

/**
 * A minigame is a collection of event handlers that are enabled or disabled for
 * players depending on whether they are currently inside that minigame instance
 * or not.
 */
public abstract class Minigame implements Listener {
    private final List<Player> players = new ArrayList<>();
    private final Map<Player, InventorySnapshot> inventorySnapshots = new HashMap<>();
    private MinigameManager manager;
    private MinigameID id;
    private Arena arena;
    private boolean instanced;

    /**
     * Called right when a new minigame instance is created to assign proper flags.
     * 
     * @param manager   - The minigame manager instance this minigame is bound to.
     * @param id        - The ID of this minigame.
     * @param arena     - The arena instance this minigame is using.
     * @param instanced - True if this minigame is instance, false if this minigame
     *                  is not instanced.
     */
    final void init(MinigameManager manager, MinigameID id, Arena arena, boolean instanced) {
        this.manager = manager;
        this.id = id;
        this.arena = arena;
        this.instanced = instanced;
    }

    /**
     * Adds a new player to this minigame. Calls a cancellable JoinMinigameEvent.
     * This will also create a snapshot of the player's current inventory and stats,
     * while clearing their inventory.
     * 
     * @param player - The player to add.
     * @throws PlayerAlreadyInMinigameException If the player is already in a
     *                                          minigame.
     */
    public final void addPlayer(Player player) throws PlayerAlreadyInMinigameException {
        var currentMinigame = manager.getCurrentMinigame(player);
        if (currentMinigame != null)
            throw new PlayerAlreadyInMinigameException(player, this, currentMinigame);

        players.add(player);

        var invSnapshot = InventorySnapshot.createSnapshotAndClear(player);
        inventorySnapshots.put(player, invSnapshot);

        var event = new JoinMinigameEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Removes a player from this minigame. Calls a LeaveMinigameEvent. This
     * function preforms no action is the player is not currently part of this
     * minigame. This function will also restore the player's inventory that was
     * saved before adding them to the minigame.
     * 
     * @param player - The player to remove.
     */
    public final void removePlayer(Player player) {
        if (!players.contains(player))
            return;

        players.remove(player);

        var invSnapshot = inventorySnapshots.get(player);
        InventorySnapshot.apply(player, invSnapshot);
        inventorySnapshots.remove(player);

        var event = new LeaveMinigameEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);

        if (players.isEmpty() && isInstanced()) {
            manager.endMinigame(this);

            var endEvent = new MinigameEndEvent(this);
            Bukkit.getPluginManager().callEvent(endEvent);
        }
    }

    /**
     * Called whenever a player leaves the server. If this happens, we should remove
     * them from the minigame properly.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removePlayer(e.getPlayer());
    }

    /**
     * Gets the number of players currently in this minigame.
     * 
     * @return The number of players.
     */
    public int getSize() {
        return players.size();
    }

    /**
     * Gets a list of all players in this minigame. This list should not be edited.
     * 
     * @return The list of players in this minigame.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the ID for this minigame.
     * 
     * @return The minigame ID.
     */
    public final MinigameID getID() {
        return id;
    }

    /**
     * Gets the arena instance this minigame instance is using.
     * 
     * @return The arena.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Gets whether or not this minigame is instanced.
     * 
     * @return True if this minigame is instanced. False otherwise.
     */
    public boolean isInstanced() {
        return instanced;
    }
}
