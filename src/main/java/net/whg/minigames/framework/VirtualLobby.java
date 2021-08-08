package net.whg.minigames.framework;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import net.whg.minigames.MinigamesPlugin;
import net.whg.minigames.framework.exceptions.PlayerAlreadyInMinigameException;
import net.whg.utils.StringUtils;
import net.whg.utils.player.CmdPlayer;

/**
 * Contains a list of players that are waiting in a lobby for a game to start.
 */
public class VirtualLobby implements Listener {
    private final List<Player> lobby = new ArrayList<>();
    private final MinigameManager manager;
    private final MinigameFactory factory;
    private final String minigameName;

    /**
     * Creates a new virtual lobby.
     * 
     * @param manager      - The minigame manager for this lobby.
     * @param minigameName - The name of the minigame this lobby is for.
     */
    public VirtualLobby(MinigameManager manager, String minigameName) {
        this.manager = manager;
        this.minigameName = minigameName;
        factory = manager.getMinigameFactory(minigameName);
    }

    /**
     * Adds a player to this game's virtual lobby. If the minigame meets all
     * requirements to begin, the minigame instance will be started instantly.
     * 
     * @param player - The player to add.
     */
    void addPlayerToLobby(Player player) {
        if (lobby.contains(player))
            return;

        lobby.add(player);
        MinigamesPlugin.logInfo("%s has the joined the lobby for %s.", player.getName(), minigameName);

        if (shouldStart()) {
            var minigame = manager.initializeMinigame(minigameName);
            populateInstance(minigame);
        } else {
            var sender = new CmdPlayer(player);
            var friendlyName = StringUtils.splitCamelCase(minigameName);
            sender.sendConfirmation("You have joined the lobby for %s!", friendlyName);
        }
    }

    /**
     * Removes a player from this game's virtual lobby.
     * 
     * @param player
     */
    void removePlayerFromLobby(Player player) {
        lobby.remove(player);
    }

    /**
     * Gets all players currently in this lobby.
     * 
     * @return The players in the lobby.
     */
    List<Player> getPlayersInLobby() {
        return lobby;
    }

    /**
     * Puts as many players from this lobby into the minigame instance as possible.
     * 
     * @param minigame - The minigame.
     */
    public void populateInstance(Minigame minigame) {
        while (!lobby.isEmpty() && minigame.getSize() < factory.getMaxPlayers()) {
            var player = lobby.remove(0);
            var currentMinigame = manager.getCurrentMinigame(player);
            if (currentMinigame != null)
                currentMinigame.removePlayer(player);

            try {
                minigame.addPlayer(player);
            } catch (PlayerAlreadyInMinigameException e) {
                // Already handled above, so this should never be called.
                e.printStackTrace();
            }
        }
    }

    /**
     * Gets the number of players currently in this lobby.
     * 
     * @return The number of players.
     */
    public int getSize() {
        return lobby.size();
    }

    /**
     * If a player leaves the server, remove them from this lobby incase they are in
     * it.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        removePlayerFromLobby(e.getPlayer());
    }

    /**
     * Gets the name of the minigame this lobby is for.
     * 
     * @return The minigame name.
     */
    public String getMinigame() {
        return minigameName;
    }

    /**
     * Checks whether or not the minigame is ready to be initialized.
     * 
     * @return True if the minigame instance is ready to be started. False
     *         otherwise.
     */
    private boolean shouldStart() {
        return getSize() >= factory.getMinPlayers();
    }
}
