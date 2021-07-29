package net.whg.minigames.framework;

/**
 * A major game is the primary type of minigame that is focused on the server.
 * It has a clear objective and goal for players to achieve. Depending on
 * whether the player wins, loses, or ties, they will usual receive scores,
 * rewards, stat changes, titles, or achievements.
 */
public abstract class MajorGame extends Minigame {
    protected MajorGame(MinigameManager manager) {
        super(manager);
    }
}
