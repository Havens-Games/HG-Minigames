package net.whg.minigames.framework;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.whg.utils.SafeArrayList;

/**
 * An abstract class for handling groupings of players that are within this
 * container.
 */
public abstract class AbstractPlayerManager implements Listener {
    private final SafeArrayList<Player> players = new SafeArrayList<>();
    protected final Random random = new Random();

    /**
     * An internal method for adding a new player to this list to be managed.
     * 
     * @param player - The player to add.
     */
    protected void addPlayerToList(Player player) {
        players.add(player);
    }

    /**
     * An internal method for removing a player from this list.
     * 
     * @param player - The player to remove.
     */
    protected void removePlayerFromList(Player player) {
        players.remove(player);
    }

    /**
     * Gets a read-only list of all players currently within this container.
     * 
     * @return A list of players.
     */
    public List<Player> getPlayers() {
        return players.asReadOnly();
    }

    /**
     * Checks whether or not this player list is currently empty.
     * 
     * @return True if the player list is empty. False otherwise.
     */
    public boolean isEmptyPlayerList() {
        return players.isEmpty();
    }

    /**
     * Gets the number of players currently within this player list.
     * 
     * @return The player count.
     */
    public int getPlayerCount() {
        return players.size();
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
     * Teleports all players within this container to the target location.
     * 
     * @param location - The target location.
     */
    public void teleportAll(Location location) {
        for (var player : players)
            player.teleport(location);
    }

    /**
     * Shuffles the ordering of players within this player list.
     */
    public void shufflePlayerOrder() {
        Collections.shuffle(players);
    }

    /**
     * Adds a new player to this player list, trigging events as needed.
     * 
     * @param player - The player to add.
     */
    public abstract void addPlayer(Player player);

    /**
     * Removes a player from this player list, trigging events as needed.
     * 
     * @param player - The player to remove.
     */
    public abstract void removePlayer(Player player);
}
