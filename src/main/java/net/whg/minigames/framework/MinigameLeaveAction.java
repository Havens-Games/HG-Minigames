package net.whg.minigames.framework;

import net.whg.utils.cmdformat.Subcommand;
import net.whg.utils.exceptions.CommandException;
import net.whg.utils.exceptions.CommandStateException;
import net.whg.utils.exceptions.NoConsoleException;
import net.whg.utils.player.CmdPlayer;

public class MinigameLeaveAction extends Subcommand {
    private final MinigameManager minigameManager;

    public MinigameLeaveAction(MinigameManager minigameManager) {
        this.minigameManager = minigameManager;
    }

    @Override
    public void execute(CmdPlayer sender, String[] args) throws CommandException {
        if (!sender.isPlayer())
            throw new NoConsoleException("You must be a player to join a minigame!");

        var player = sender.getPlayer();
        var minigame = minigameManager.getCurrentMinigame(player);
        if (minigame == null)
            throw new CommandStateException("You are not in a minigame!");

        minigame.removePlayer(player);
        sender.sendConfirmation("You have left %s.", minigame.getID().getMinigameType());
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getUsage() {
        return "";
    }
}
