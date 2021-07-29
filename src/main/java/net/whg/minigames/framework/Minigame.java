package net.whg.minigames.framework;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.whg.minigames.framework.events.JoinMinigameEvent;
import net.whg.minigames.framework.events.LeaveMinigameEvent;
import net.whg.minigames.framework.exceptions.PlayerAlreadyInMinigameException;

/**
 * A minigame is a collection of event handlers that are enabled or disabled for
 * players depending on whether they are currently inside that minigame instance
 * or not.
 */
public abstract class Minigame implements Listener, Closeable {
    private final List<Player> players = new ArrayList<>();
    private final MinigameManager manager;

    /**
     * Creates a new minigame instance.
     * 
     * @param manager - The minigame manager instance this minigame is bound to.
     */
    protected Minigame(MinigameManager manager) {
        this.manager = manager;
    }

    /**
     * Adds a new player to this minigame. Calls a cancellable JoinMinigameEvent.
     * 
     * @param player - The player to add.
     * @throws PlayerAlreadyInMinigameException If the player is already in a
     *                                          minigame.
     */
    public final void addPlayer(Player player) throws PlayerAlreadyInMinigameException {
        if (players.contains(player))
            throw new PlayerAlreadyInMinigameException(player, this, this);

        var currentMinigame = manager.getCurrentMinigame(player);
        if (currentMinigame != null)
            throw new PlayerAlreadyInMinigameException(player, this, currentMinigame);

        var event = new JoinMinigameEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        players.add(player);
        manager.addPlayer(player, this);
    }

    /**
     * Removes a player from this minigame. Calls a LeaveMinigameEvent. This
     * function preforms no action is the player is not currently part of this
     * minigame.
     * 
     * @param player - The player to remove.
     */
    public final void removePlayer(Player player) {
        if (!players.contains(player))
            return;

        players.remove(player);

        var event = new LeaveMinigameEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);
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
     * Gets the name of this minigame.
     * 
     * @return The name.
     */
    public abstract String getName();
}
