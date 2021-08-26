package net.whg.minigames.framework.teams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
     */
    public static void shuffleTeams(List<Player> players, List<Team> teams) {
        if (players.isEmpty())
            throw new IllegalArgumentException("Player list cannot be empty!");

        if (teams.isEmpty())
            throw new IllegalArgumentException("Team list cannot be empty!");

        // It's faster to make a copy of the player list and shuffle it
        // and distribute those in order.
        players = new ArrayList<>(players);
        Collections.shuffle(players);

        int teamIndex = 0;
        while (!players.isEmpty()) {
            // Remove from the end to avoid shifting the entire array
            var player = players.remove(players.size() - 1);
            teams.get(teamIndex).addPlayer(player);
            teamIndex = (teamIndex + 1) % teams.size();
        }
    }

    private TeamUtils() {
    }
}
