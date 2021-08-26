package net.whg.minigames.framework.teams;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.whg.utils.SafeArrayList;

/**
 * A team is a collection of players that are working together towards a common
 * goal within a minigame.
 */
public abstract class Team {
    private final SafeArrayList<Player> players = new SafeArrayList<>();

    /**
     * Adds a player to this team.
     * 
     * @param player - The player to add.
     */
    public void addPlayer(Player player) {
        if (players.contains(player))
            return;

        players.add(player);

        var event = new TeamJoinEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Removes a player from this team.
     * 
     * @param player - The player to remove.
     */
    public void removePlayer(Player player) {
        if (!players.contains(player))
            return;

        players.remove(player);

        var event = new TeamLeaveEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Gets a read-only list of all players on this team.
     * 
     * @return A list of players.
     */
    public List<Player> getPlayers() {
        return players.asReadOnly();
    }

    /**
     * Gets the name of this team.
     * 
     * @return The team name.
     */
    public abstract String getName();
}
