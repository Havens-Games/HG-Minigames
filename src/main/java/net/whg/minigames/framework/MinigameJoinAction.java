package net.whg.minigames.framework;

import net.whg.minigames.framework.exceptions.PlayerAlreadyInMinigameException;
import net.whg.utils.CmdPlayer;
import net.whg.utils.cmdformat.Subcommand;
import net.whg.utils.exceptions.CommandException;
import net.whg.utils.exceptions.NoConsoleException;
import net.whg.utils.exceptions.UnknownArgumentException;

public class MinigameJoinAction extends Subcommand {
    private final MinigameManager minigameManager;

    public MinigameJoinAction(MinigameManager minigameManager) {
        this.minigameManager = minigameManager;
    }

    @Override
    public void execute(CmdPlayer sender, String[] args) throws CommandException {
        if (!sender.isPlayer())
            throw new NoConsoleException("You must be a player to join a minigame!");

        var player = sender.getPlayer();

        var minigame = minigameManager.getMinigameFactory(args[0]);
        if (minigame == null)
            throw new UnknownArgumentException("Minigame '%s' does not exist!", args[0]);

        var lobbyGame = args.length == 2 ? getLobbyGame(args[1]) : null;

        if (minigame.isInstanced()) {
            minigameManager.removeFromAllMinigames(player);
            var startedGame = minigameManager.addToLobby(player, args[0]);

            if (!startedGame && lobbyGame != null) {
                try {
                    lobbyGame.addPlayer(player);
                } catch (PlayerAlreadyInMinigameException e) {
                    // This should not be called.
                    e.printStackTrace();
                }
            }
        } else {
            if (lobbyGame != null)
                throw new UnknownArgumentException("%s does not have a lobby!", args[0]);

            minigameManager.removeFromAllMinigames(player);
            var active = minigameManager.getActiveMinigame(args[0]);

            try {
                active.addPlayer(player);
            } catch (PlayerAlreadyInMinigameException e) {
                // This should not be called.
                e.printStackTrace();
            }
        }
    }

    private Minigame getLobbyGame(String name) throws UnknownArgumentException {
        var factory = minigameManager.getMinigameFactory(name);
        if (factory == null)
            throw new UnknownArgumentException("Minigame '%s' does not exist!", name);

        if (factory.isInstanced())
            throw new UnknownArgumentException("%s is not a lobby game!", name);

        return minigameManager.getActiveMinigame(name);
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getUsage() {
        return "<minigame> [lobby]";
    }
}
