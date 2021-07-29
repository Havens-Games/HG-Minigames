package net.whg.minigames.framework.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.whg.minigames.framework.Minigame;

/**
 * An event that is thrown whenever a player tries to join a minigame. This is
 * called before the player actually joins the minigame.
 */
public class JoinMinigameEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Player player;
    private final Minigame minigame;
    private boolean cancelled = false;

    /**
     * Creates a new JoinMinigameEvent.
     * 
     * @param player   - The player that is attempting to join the minigame.
     * @param minigame - The minigame that is being joined.
     */
    public JoinMinigameEvent(Player player, Minigame minigame) {
        this.player = player;
        this.minigame = minigame;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
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
