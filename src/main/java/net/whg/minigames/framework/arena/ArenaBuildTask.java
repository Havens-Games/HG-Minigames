package net.whg.minigames.framework.arena;

import java.util.List;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.entity.Entity;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.entity.ExtentEntityCopy;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.function.visitor.EntityVisitor;
import com.sk89q.worldedit.math.BlockVector3;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A BukkitRunnable task that builds a WorldEdit schematic in the world over the
 * span of several Minecraft ticks.
 */
public class ArenaBuildTask extends BukkitRunnable {
    private static final int BLOCKS_PER_TICK = 500;
    private static final int UPDATE_BLOCKS_STATE = 0;
    private static final int CLEAR_ENTITIES_STATE = 1;
    private static final int PLACE_ENTITIES_STATE = 2;
    private static final int FINISHED_STATE = 3;

    private final Location location;
    private final Clipboard schematic;
    private final RegionIterator iterator;
    private int offsetX, offsetY, offsetZ;
    private int editState = UPDATE_BLOCKS_STATE;

    public ArenaBuildTask(Location location, Clipboard schematic) {
        this.location = location;
        this.schematic = schematic;

        var origin = schematic.getOrigin();
        var min = schematic.getMinimumPoint();
        var max = schematic.getMaximumPoint();

        iterator = new RegionIterator(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
        offsetX = min.getX() - origin.getX() + location.getBlockX();
        offsetY = min.getY() - origin.getY() + location.getBlockY();
        offsetZ = min.getZ() - origin.getZ() + location.getBlockZ();
    }

    public boolean isDone() {
        return editState == FINISHED_STATE;
    }

    @Override
    public void run() {
        switch (editState) {
            case UPDATE_BLOCKS_STATE:
                updateBlocksState();
                break;

            case CLEAR_ENTITIES_STATE:
                clearEntities();
                break;

            case PLACE_ENTITIES_STATE:
                spawnEntities();
                break;

            case FINISHED_STATE:
                cancel();
                break;

            default:
                throw new IllegalStateException("Unexpected edit state!");
        }
    }

    private void updateBlocksState() {
        var updated = 0;
        while (updated < BLOCKS_PER_TICK && !iterator.isDone()) {
            if (handleBlock())
                updated++;

            iterator.update();
        }

        if (iterator.isDone())
            editState++;
    }

    private boolean handleBlock() {
        var world = location.getWorld();
        var block = world.getBlockAt(iterator.getX() + offsetX, iterator.getY() + offsetY, iterator.getZ() + offsetZ);
        var target = schematic.getFullBlock(BlockVector3.at(iterator.getX(), iterator.getY(), iterator.getZ()));

        var currentData = block.getBlockData();
        var targetData = BukkitAdapter.adapt(target);

        if (currentData.matches(targetData))
            return false;

        block.setBlockData(targetData, false);
        return true;
    }

    private void clearEntities() {
        var world = location.getWorld();

        for (var entity : world.getEntities()) {
            var loc = entity.getLocation();
            if (loc.getX() >= iterator.getMinX() + offsetX && loc.getX() <= iterator.getMaxX() + offsetX
                    && loc.getZ() >= iterator.getMinZ() + offsetZ && loc.getZ() <= iterator.getMaxZ() + offsetZ)
                entity.remove();
        }

        editState++;
    }

    private void spawnEntities() {
        var from = schematic.getOrigin();
        var to = BlockVector3.at(offsetX, offsetY, offsetZ);
        var world = BukkitAdapter.adapt(location.getWorld());
        ExtentEntityCopy entityCopy = new ExtentEntityCopy(from.toVector3(), world, to.toVector3(), null);
        List<? extends Entity> entities = Lists.newArrayList(schematic.getEntities());
        EntityVisitor entityVisitor = new EntityVisitor(entities.iterator(), entityCopy);
        try {
            Operations.complete(entityVisitor);
        } catch (WorldEditException e) {
            e.printStackTrace();
        }

        editState++;
    }
}
