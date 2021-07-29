package net.whg.minigames.framework.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.whg.minigames.framework.Minigame;

public class LeaveMinigameEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Player player;
    private final Minigame minigame;

    /**
     * Creates a new LeaveMinigameEvent.
     * 
     * @param player   - The player leaving the minigame.
     * @param minigame - The minigame being left.
     */
    public LeaveMinigameEvent(Player player, Minigame minigame) {
        this.player = player;
        this.minigame = minigame;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }

    /**
     * Gets the player involved in this event.
     * 
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the minigame that is involved in this event.
     * 
     * @return The minigame.
     */
    public Minigame getMinigame() {
        return minigame;
    }
}
