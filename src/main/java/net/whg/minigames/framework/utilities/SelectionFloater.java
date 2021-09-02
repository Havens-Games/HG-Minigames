package net.whg.minigames.framework.utilities;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Represents a floating, selectable entity that triggers and event when right
 * clicked by a player. This can be used for actions such as selecting a class
 * at the beginning of a minigame, or voting for a game mode. This object *must*
 * be disposed when it is no longer needed in order to avoid memory leaks.
 */
public abstract class SelectionFloater implements Listener {
    private final ArmorStand armorStand;
    private boolean disposed = false;

    /**
     * Creates a new SelectionFloater class.
     * 
     * @param location    - The location to spawn the floater at.
     * @param displayName - The display name to hover above the entity.
     */
    protected SelectionFloater(Location location, String displayName) {
        armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setGravity(false);
        armorStand.setCustomName(displayName);
        armorStand.setCustomNameVisible(true);

        var plugin = Bukkit.getPluginManager().getPlugin("HG-Minigames");
        Bukkit.getPluginManager().registerEvents(this, plugin);

        new BukkitRunnable() {
            @Override
            public void run() {
                decorateEntity(armorStand);
            }
        }.runTaskLater(plugin, 0L);
    }

    /**
     * Disposes this floater and removes the corresponding entity. This preforms no
     * action if the floater is already disposed.
     */
    public void dispose() {
        if (disposed)
            return;

        disposed = true;
        armorStand.remove();
        HandlerList.unregisterAll(this);
    }

    /**
     * Gets whether or not this float has already been disposed.
     * 
     * @return True if it has been disposed. False otherwise.
     */
    public boolean isDisposed() {
        return disposed;
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent e) {
        var entity = e.getRightClicked();
        var player = e.getPlayer();

        if (entity != armorStand)
            return;

        Bukkit.getPluginManager().callEvent(new FloaterSelectedEvent(player, this));
    }

    /**
     * Called when the floating entity is spawned in order to assign decorations.
     * 
     * @param armorStand - The armor stand entity to decorate.
     */
    protected abstract void decorateEntity(ArmorStand armorStand);
}
