package net.whg.minigames.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import net.whg.minigames.MinigamesPlugin;
import net.whg.minigames.framework.arena.Arena;
import net.whg.minigames.framework.events.ArenaCompletedEvent;
import net.whg.minigames.framework.events.JoinMinigameEvent;
import net.whg.minigames.framework.events.LeaveMinigameEvent;
import net.whg.minigames.framework.events.MinigameEndEvent;
import net.whg.minigames.framework.events.MinigameReadyEvent;
import net.whg.minigames.framework.exceptions.PlayerAlreadyInMinigameException;
import net.whg.minigames.framework.teams.TeamList;
import net.whg.utils.math.Vec3;
import net.whg.utils.player.InventorySnapshot;

/**
 * A minigame is a collection of event handlers that are enabled or disabled for
 * players depending on whether they are currently inside that minigame instance
 * or not.
 */
public abstract class Minigame extends AbstractPlayerManager {
    private final Map<Player, InventorySnapshot> inventorySnapshots = new HashMap<>();
    private final TeamList teamList = new TeamList();
    private MinigameManager manager;
    private MinigameID id;
    private Arena arena;
    private boolean instanced;

    /**
     * Called right when a new minigame instance is created to assign proper flags.
     * 
     * @param manager   - The minigame manager instance this minigame is bound to.
     * @param id        - The ID of this minigame.
     * @param arena     - The arena instance this minigame is using.
     * @param instanced - True if this minigame is instance, false if this minigame
     *                  is not instanced.
     */
    final void init(MinigameManager manager, MinigameID id, Arena arena, boolean instanced) {
        this.manager = manager;
        this.id = id;
        this.arena = arena;
        this.instanced = instanced;

        MinigamesPlugin.logInfo("Minigame %s has been initialized.", id.instanceName());
    }

    /**
     * Adds a new player to this minigame. Calls a cancellable JoinMinigameEvent.
     * This will also create a snapshot of the player's current inventory and stats,
     * while clearing their inventory.
     * 
     * @param player - The player to add.
     * @throws PlayerAlreadyInMinigameException If the player is already in a
     *                                          minigame.
     */
    @Override
    public void addPlayer(Player player) {
        var currentMinigame = manager.getCurrentMinigame(player);
        if (currentMinigame != null)
            throw new PlayerAlreadyInMinigameException(player, this, currentMinigame);

        addPlayerToList(player);

        var invSnapshot = InventorySnapshot.createSnapshotAndClear(player);
        inventorySnapshots.put(player, invSnapshot);

        var event = new JoinMinigameEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);
    }

    /**
     * Removes a player from this minigame. Calls a LeaveMinigameEvent. This
     * function preforms no action is the player is not currently part of this
     * minigame. This function will also restore the player's inventory that was
     * saved before adding them to the minigame.
     * 
     * @param player - The player to remove.
     */
    @Override
    public void removePlayer(Player player) {
        if (!getPlayers().contains(player))
            return;

        removePlayerFromList(player);

        var invSnapshot = inventorySnapshots.get(player);
        InventorySnapshot.apply(player, invSnapshot);
        inventorySnapshots.remove(player);

        for (var team : teamList.getTeams())
            team.removePlayer(player);

        var event = new LeaveMinigameEvent(player, this);
        Bukkit.getPluginManager().callEvent(event);

        MinigamesPlugin.logInfo("%s has left the minigame %s.", player.getName(), id.instanceName());

        if (isEmptyPlayerList() && isInstanced()) {
            manager.endMinigame(this);

            var endEvent = new MinigameEndEvent(this);
            Bukkit.getPluginManager().callEvent(endEvent);

            MinigamesPlugin.logInfo("Minigame %s has ended.", id.instanceName());
        }
    }

    /**
     * Gets the ID for this minigame.
     * 
     * @return The minigame ID.
     */
    public final MinigameID getID() {
        return id;
    }

    /**
     * Gets the arena instance this minigame instance is using.
     * 
     * @return The arena.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Gets whether or not this minigame is instanced.
     * 
     * @return True if this minigame is instanced. False otherwise.
     */
    public boolean isInstanced() {
        return instanced;
    }

    /**
     * Searches through all entities to locate placeholder entities with the given
     * name. A placeholder entity is represented as an armor stand with a matching
     * name tag. These placeholder entities are removed and their locations are
     * returned.
     * 
     * @param placeholder - The placeholder string to search for.
     * @return A list of locations that were found.
     */
    protected List<Location> findPlaceholders(String placeholder) {
        var list = new ArrayList<Location>();

        var location = getArena().getLocation();
        var schematic = getArena().getSchematic();
        var world = location.getWorld();
        var loc = new Vec3(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var offset = loc.subtract(schematic.getOrigin());

        var min = schematic.getMinimumPoint().add(offset);
        var max = schematic.getMaximumPoint().add(offset);

        for (var entity : world.getEntities()) {
            if (!(entity instanceof ArmorStand))
                continue;

            var eLoc = entity.getLocation();
            var entityPos = new Vec3(eLoc.getBlockX(), eLoc.getBlockY(), eLoc.getBlockZ());
            if (!entityPos.isInBounds(min, max))
                continue;

            if (!entity.getName().equals(placeholder))
                continue;

            list.add(eLoc);
            entity.remove();
        }

        MinigamesPlugin.logInfo("Loaded %s placeholders for %s in the minigame %s.", list.size(), placeholder,
                id.instanceName());

        return list;
    }

    /**
     * Gets the list of teams present in this minigame.
     * 
     * @return The list of teams.
     */
    protected TeamList getTeamList() {
        return teamList;
    }

    @EventHandler
    public void onArenaFinished(ArenaCompletedEvent e) {
        if (e.getArena() != getArena())
            return;

        var event = new MinigameReadyEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }
}
