package net.whg.minigames.framework.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import net.whg.minigames.framework.arena.Arena;

/**
 * Called when an arena has finished being constructed.
 */
public class ArenaCompletedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Arena arena;

    /**
     * Creates a new ArenaCompletedEvent.
     * 
     * @param arena - The arena that has finished construction.
     */
    public ArenaCompletedEvent(Arena arena) {
        this.arena = arena;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return getHandlerList();
    }

    /**
     * Gets the arena involved in this event.
     * 
     * @return The arena.
     */
    public Arena getArena() {
        return arena;
    }
}
