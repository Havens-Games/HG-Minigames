package net.whg.minigames.framework.utilities;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player clicks on a selection floater.
 */
public class FloaterSelectedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Player player;
    private final SelectionFloater floater;

    /**
     * Creates a new FloaterSelectedEvent.
     * 
     * @param player  - The player who selected the floater.
     * @param floater - The selection floater that was selected.
     */
    public FloaterSelectedEvent(Player player, SelectionFloater floater) {
        this.player = player;
        this.floater = floater;
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
     * Gets the selection floater that was selected.
     * 
     * @return The selection floater.
     */
    public SelectionFloater getSelectionFloater() {
        return floater;
    }
}
