package net.whg.minigames.framework;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import net.whg.minigames.framework.teams.Team;

/**
 * A minigame configuration that allows for a pre-configured minigame, canceling
 * events that are commonly disabled.
 */
public abstract class DefaultMinigame extends Minigame {
    private boolean disableFoodLevel = true;
    private boolean disableBlockBreak = true;
    private boolean disableItemDrop = true;
    private Team defaultTeam;

    /**
     * Sets whether or not food level changing should be disabled. This event is
     * disabled by default.
     * 
     * @param disabled - True if the event should be disabled.
     */
    protected void setFoodLevelChangeDisabled(boolean disabled) {
        disableFoodLevel = disabled;
    }

    /**
     * When a player's food level changes, this event listener cancels the event.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!disableFoodLevel)
            return;

        if (!getPlayers().contains(e.getEntity()))
            return;

        e.setCancelled(true);
    }

    /**
     * Sets whether or not block breaking should be disabled. This event is disabled
     * by default.
     * 
     * @param disabled - True if the event should be disabled.
     */
    protected void setBlockBreakDisabled(boolean disabled) {
        disableBlockBreak = disabled;
    }

    /**
     * When a player from this minigame breaks a block, this event listener cancels
     * the event.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!disableBlockBreak)
            return;

        if (!getPlayers().contains(e.getPlayer()))
            return;

        e.setCancelled(true);
    }

    /**
     * Sets whether or not item drops should be disabled. This event is disabled by
     * default.
     * 
     * @param disabled - True if the event should be disabled.
     */
    protected void setItemDropDisabled(boolean disabled) {
        disableItemDrop = disabled;
    }

    /**
     * When a player from this minigame drops an item, this event listener cancels
     * the event.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onItemDrop(EntityDropItemEvent e) {
        if (!disableItemDrop)
            return;

        if (!getPlayers().contains(e.getEntity()))
            return;

        e.setCancelled(true);
    }

    @Override
    public void addPlayer(Player player) {
        super.addPlayer(player);

        if (defaultTeam != null)
            defaultTeam.addPlayer(player);
    }

    /**
     * Gets the default team for this minigame.
     * 
     * @return The default team.
     * @see #setDefaultTeam(Team)
     */
    public Team getDefaultTeam() {
        return defaultTeam;
    }

    /**
     * Sets the default team for this minigame. All players who join this minigame
     * will automatically be added to this team.
     * 
     * @param team - The default team.
     */
    protected void setDefaultTeam(Team team) {
        defaultTeam = team;
    }
}
