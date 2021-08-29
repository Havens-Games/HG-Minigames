package net.whg.minigames.framework.teams;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import net.whg.utils.SafeArrayList;

/**
 * Contains a list of teams that are present within a minigame instance.
 */
public class TeamList {
    private final SafeArrayList<Team> teams = new SafeArrayList<>();

    /**
     * Creates and registers a new team in this list. This should be done at the
     * beginning of a match.
     * 
     * @param team - The team to add.
     */
    public void createTeam(Team team) {
        if (teams.contains(team))
            return;

        teams.add(team);

        var plugin = Bukkit.getPluginManager().getPlugin("HG-Minigames");
        Bukkit.getPluginManager().registerEvents(team, plugin);
    }

    /**
     * Disposes and cleans up listeners for all teams currently in this list. This
     * must be called at the end of each minigame to avoid memory leaks.
     */
    public void dispose() {
        for (var team : teams)
            HandlerList.unregisterAll(team);

        teams.clear();
    }

    /**
     * Gets the team that the given player is currently part of. If the player is in
     * multiple teams at once, only the first team is returned.
     * 
     * @param player - The player.
     * @return The team the player is in, or null if the player is not part of a
     *         team.
     * @see #getTeams(Player)
     */
    public Team getTeam(Player player) {
        for (var team : teams) {
            if (team.getPlayers().contains(player))
                return team;
        }

        return null;
    }

    /**
     * Gets a list of all teams that the player is currently part of.
     * 
     * @param player - The player.
     * @return A list of all teams that the player is currently in.
     * @see #getTeam(Player)
     */
    public List<Team> getTeams(Player player) {
        var list = new ArrayList<Team>();

        for (var team : teams) {
            if (team.getPlayers().contains(player))
                list.add(team);
        }

        return list;
    }

    /**
     * Gets a read-only list of all teams currently in this team list.
     * 
     * @return A list of all teams.
     */
    public List<Team> getTeams() {
        return teams.asReadOnly();
    }

    /**
     * Gets the team with the given name.
     * 
     * @param name - The name to look for.
     * @return The team with the given name, or null if the team does not exist.
     */
    public Team getTeam(String name) {
        for (var team : teams) {
            if (team.getName().equals(name))
                return team;
        }

        return null;
    }
}
