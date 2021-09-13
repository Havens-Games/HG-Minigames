package net.whg.minigames.framework.exceptions;

import org.bukkit.entity.Player;

import net.whg.minigames.framework.Minigame;
import net.whg.utils.player.CmdPlayer;

/**
 * Thrown whenever a player tries to join a minigame while already part of
 * another minigame.
 */
public class PlayerAlreadyInMinigameException extends RuntimeException {
    private static final String CONSOLE_MESSAGE = "Cannot add player, %s, to minigame: %s. Already part of minigame: %s";
    private static final String PLAYER_MESSAGE = "You are already part of the minigame: %s!";

    private final transient Player player;
    private final transient Minigame current;
    private final transient Minigame target;

    public PlayerAlreadyInMinigameException(Player player, Minigame current, Minigame target) {
        super(String.format(CONSOLE_MESSAGE, player.getName(), target.getID().getMinigameType(),
                current.getID().getMinigameType()));

        this.player = player;
        this.current = current;
        this.target = target;
    }

    public Player getPlayer() {
        return player;
    }

    public Minigame getCurrentMinigame() {
        return current;
    }

    public Minigame getTargetMinigame() {
        return target;
    }

    public void printToCommandSender(CmdPlayer sender) {
        sender.sendError(PLAYER_MESSAGE, current.getID().getMinigameType());
    }
}
