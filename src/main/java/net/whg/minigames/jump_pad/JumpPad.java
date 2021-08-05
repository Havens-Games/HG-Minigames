package net.whg.minigames.jump_pad;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.md_5.bungee.api.ChatColor;
import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.events.ArenaCompletedEvent;
import net.whg.minigames.framework.events.JoinMinigameEvent;

/**
 * A lobby game that involves giving all players jump boast and knock back
 * sticks, then letting them all jump around on slime blocks for a while.
 */
public class JumpPad extends Minigame {
    private final List<Location> spawnPoints = new ArrayList<>();

    /**
     * When a player joints this minigame, this event listener will try to
     * initialize the player if the arena is loaded.
     * 
     * @param e - The event.
     */
    public void onJoinMinigame(JoinMinigameEvent e) {
        if (e.getMinigame() != this)
            return;

        var player = e.getPlayer();

        if (spawnPoints.isEmpty()) {
            player.sendMessage(ChatColor.GRAY + "Please wait while the arena for Jump Pad is finished being prepared.");
        } else {
            initializePlayer(player);
        }
    }

    /**
     * Prepares the player's inventory and teleports them into the game arena.
     * 
     * @param player - The player to initialize.
     */
    private void initializePlayer(Player player) {
        teleportToSpawn(player);
        prepareInventory(player);
    }

    /**
     * Gives the player all the required potion effects and items required to play
     * the game.
     * 
     * @param player - The player.
     */
    private void prepareInventory(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 7, true, false, true));

        var knockbackStick = new ItemStack(Material.STICK);
        knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 3);

        player.getInventory().addItem(knockbackStick);
    }

    /**
     * Teleports the player to a random spawn point for this minigame.
     * 
     * @param player - The player.
     */
    private void teleportToSpawn(Player player) {
        var spawnPoint = spawnPoints.get(random.nextInt(spawnPoints.size()));
        player.teleport(spawnPoint);
    }

    /**
     * When the arena is finished being built, this event listener looks for any
     * spawn point placeholders to reference. This event listener will also start
     * and players that are currently waiting on this arena.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onArenaFinished(ArenaCompletedEvent e) {
        if (e.getArena() != getArena())
            return;

        findSpawnPoints();

        // In case we had any players waiting for the arena to finish.
        for (var player : getPlayers()) {
            teleportToSpawn(player);
            player.sendMessage(
                    ChatColor.GRAY + "The arena has finished being prepared for Jump Pad. Please enjoy the game!");
        }
    }

    /**
     * Search through the arena area to find all spawn locations via placeholders.
     */
    private void findSpawnPoints() {
        spawnPoints.addAll(findPlaceholders(ChatColor.DARK_AQUA + "Spawn Point"));

        if (spawnPoints.isEmpty()) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to find and spawn point placeholders for JumpPad!");

            var location = getArena().getLocation();
            location.setY(location.getWorld().getHighestBlockYAt(location));
            spawnPoints.add(location);
        }
    }

    /**
     * When a player from this minigame is damaged, this event listener will change
     * the actual damage taken to 0. Basically this will make all damage sources act
     * like snowballs. It plays the damage animation, but nothing else.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!getPlayers().contains(e.getEntity()))
            return;

        e.setDamage(0);
    }

    /**
     * When a player's food level changes, this event listener cancels the event.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (!getPlayers().contains(e.getEntity()))
            return;

        e.setCancelled(true);
    }

    /**
     * When a player from this minigame breaks a block, this event listener cancels
     * the event.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (!getPlayers().contains(e.getPlayer()))
            return;

        e.setCancelled(true);
    }

    /**
     * When a player from this minigame drops an item, this event listener cancels
     * the event.
     * 
     * @param e - The event.
     */
    @EventHandler
    public void onItemDrop(EntityDropItemEvent e) {
        if (!getPlayers().contains(e.getEntity()))
            return;

        e.setCancelled(true);
    }
}
