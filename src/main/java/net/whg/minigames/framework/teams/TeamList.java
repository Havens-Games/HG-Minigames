package net.whg.minigames.framework.teams;

import java.util.List;

import org.bukkit.entity.Player;

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
    }

    /**
     * Gets the team that the given player is currently part of.
     * 
     * @param player - The player.
     * @return The team the player is in, or null if the player is not part of a
     *         team.
     */
    public Team getTeam(Player player) {
        for (var team : teams) {
            if (team.getPlayers().contains(player))
                return team;
        }

        return null;
    }

    /**
     * Gets a read-only list of all teams currently in this team list.
     * 
     * @return A list of all teams.
     */
    public List<Team> getTeams() {
        return teams.asReadOnly();
    }
}
