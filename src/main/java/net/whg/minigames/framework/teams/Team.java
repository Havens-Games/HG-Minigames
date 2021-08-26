package net.whg.minigames.framework.teams;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.whg.minigames.MinigamesPlugin;
import net.whg.utils.SafeArrayList;

/**
 * A team is a collection of players that are working together towards a common
 * goal within a minigame.
 */
public abstract class Team {
    private final SafeArrayList<Player> players = new SafeArrayList<>();
    private String displayName;

    /**
     * Creates a new Team instance.
     */
    protected Team() {
        displayName = getName();
    }

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

        MinigamesPlugin.logInfo("%s has joined the team %s.", player.getName(), getDisplayName());
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

        MinigamesPlugin.logInfo("%s has left the team %s.", player.getName(), getDisplayName());
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
     * Gets the display name of this team.
     * 
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the display name of this team.
     * 
     * @param name - The display name.
     */
    public void setDisplayName(String name) {
        displayName = name;
    }

    /**
     * Gets the name of this team.
     * 
     * @return The team name.
     */
    public abstract String getName();
}
