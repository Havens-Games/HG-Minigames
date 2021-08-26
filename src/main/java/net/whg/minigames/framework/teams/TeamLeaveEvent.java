package net.whg.minigames.framework.teams;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Called when a player leaves a team within a minigame. This event is not
 * guaranteed to be called when a minigame ends.
 */
public class TeamLeaveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    private final Player player;
    private final Team team;

    public TeamLeaveEvent(Player player, Team team) {
        this.player = player;
        this.team = team;
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
     * Gets the team involved in this event.
     * 
     * @return The event.
     */
    public Team getTeam() {
        return team;
    }
}
