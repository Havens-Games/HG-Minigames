package net.whg.minigames.framework;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.whg.utils.WraithLib;
import net.whg.utils.cmdformat.CommandException;
import net.whg.utils.cmdformat.CommandStateException;
import net.whg.utils.cmdformat.Subcommand;

public class MinigameLeaveAction extends Subcommand {
    private final MinigameManager minigameManager;

    public MinigameLeaveAction(MinigameManager minigameManager) {
        this.minigameManager = minigameManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) throws CommandException {
        var player = (Player) sender;
        var minigame = minigameManager.getCurrentMinigame(player);
        if (minigame == null)
            throw new CommandStateException("You are not in a minigame!");

        minigame.removePlayer(player);
        WraithLib.log.sendMessage(sender, "You have left %s.", minigame.getID().getMinigameType());
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public boolean requiresNoConsole() {
        return true;
    }
}
