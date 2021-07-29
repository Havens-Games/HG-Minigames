package net.whg.minigames.framework;

/**
 * Lobby games are minigame types that do not have any goals or objectives. They
 * exist primarily as waiting areas with very simple things to do while waiting
 * for standard games to start. <br/>
 * <br/>
 * In addition, lobby games usually only have one game instance active at any
 * given time, that is always active, regardless of the number of players
 * involved.
 */
public abstract class LobbyGame extends Minigame {
    protected LobbyGame(MinigameManager manager) {
        super(manager);
    }
}
