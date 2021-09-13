package net.whg.minigames.framework.teams;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import net.whg.minigames.MinigamesPlugin;
import net.whg.minigames.framework.AbstractPlayerManager;
import net.whg.minigames.framework.events.LeaveMinigameEvent;

/**
 * A team is a collection of players that are working together towards a common
 * goal within a minigame.
 */
public abstract class Team extends AbstractPlayerManager {
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
        if (getPlayers().contains(player))
            return;

        addPlayerToList(player);

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
        if (!getPlayers().contains(player))
            return;

        removePlayerFromList(player);

        var event = new TeamLeaveEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);

        MinigamesPlugin.logInfo("%s has left the team %s.", player.getName(), getDisplayName());
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

    @EventHandler
    public void onLeaveMinigame(LeaveMinigameEvent e) {
        var player = e.getPlayer();
        removePlayer(player);
    }

    /**
     * Gets the name of this team.
     * 
     * @return The team name.
     */
    public abstract String getName();
}
