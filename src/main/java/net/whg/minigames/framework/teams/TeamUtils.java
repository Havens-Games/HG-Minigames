package net.whg.minigames.framework.teams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

/**
 * A collection of utility functions for working with teams.
 */
public class TeamUtils {
    /**
     * Randomly distributes all provided players into one of the provided teams.
     * Teams are attempted to be balanced in size where possible. If the number of
     * players does not cleanly divide into the number of teams, players are placed
     * into the list of teams in order of first to list within the team list.
     * 
     * @param players - The list of players.
     * @param teams   - The list of teams to place the players in.
     * @see #shuffleTeams(List, List, Map)
     */
    public static void shuffleTeams(List<Player> players, List<? extends Team> teams) {
        shuffleTeams(players, teams, null);
    }

    /**
     * Randomly distributes all provided players into one of the provided teams.
     * Teams are attempted to be balanced in size where possible. If the number of
     * players does not cleanly divide into the number of teams, players are placed
     * into the list of teams in order of first to list within the team list. This
     * function also allows for an optional maximum player count per team.
     * 
     * @param players - The list of players.
     * @param teams   - The list of teams to place the players in.
     * @param maxSize - A map containing the maximum number of players that can be
     *                added to each team. This list must contain one entry per team.
     *                If maxSize is null, there is no maximum value of players to
     *                add.
     */
    public static void shuffleTeams(List<Player> players, List<? extends Team> teams,
            Map<? extends Team, Integer> maxSize) {
        if (players.isEmpty())
            throw new IllegalArgumentException("Player list cannot be empty!");

        if (teams.isEmpty())
            throw new IllegalArgumentException("Team list cannot be empty!");

        if (maxSize != null) {
            if (maxSize.values().stream().anyMatch(i -> i <= 0))
                throw new IllegalArgumentException("All teams must have at least one player!");

            if (maxSize.size() != teams.size())
                throw new IllegalArgumentException("Max size map does not match team list!");

            if (maxSize.keySet().stream().anyMatch(team -> !teams.contains(team)))
                throw new IllegalArgumentException("Max size map does not match team list!");

            if (maxSize.values().stream().reduce(0, Integer::sum) < players.size())
                throw new IllegalArgumentException("There are more players than the maximum team sizes allow!");
        }

        // It's faster to make a copy of the player list and shuffle it
        // and distribute those in order.
        players = new ArrayList<>(players);
        Collections.shuffle(players);

        // To keep track of how many players we've already added to each team.
        var count = new HashMap<Team, Integer>();
        teams.forEach(team -> count.put(team, 0));

        int teamIndex = 0;
        while (!players.isEmpty()) {
            // Remove from the end to avoid shifting the entire array
            var player = players.remove(players.size() - 1);
            Team team;

            do {
                team = teams.get(teamIndex);
                teamIndex = (teamIndex + 1) % teams.size();
            } while (maxSize != null && count.get(team) >= maxSize.get(team));

            team.addPlayer(player);
            count.put(team, count.get(team) + 1);
        }
    }

    private TeamUtils() {
    }
}
