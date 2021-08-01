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
import com.sk89q.worldedit.math.transform.Identity;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A BukkitRunnable task that builds a WorldEdit schematic in the world over the
 * span of several Minecraft ticks.
 */
public class ArenaBuildTask extends BukkitRunnable {
    private static final int BLOCKS_PER_TICK = 100;

    private final ArenaBuildStates states;

    public ArenaBuildTask(Location location, Clipboard schematic) {
        states = new ArenaBuildStates();

        var world = location.getWorld();

        var origin = schematic.getOrigin();
        var target = BlockVector3.at(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        var min = schematic.getMinimumPoint();
        var max = schematic.getMaximumPoint();

        var offsetX = location.getBlockX() - origin.getX();
        var offsetY = location.getBlockY() - origin.getY();
        var offsetZ = location.getBlockZ() - origin.getZ();

        var chunkMinX = (min.getX() + offsetX) >> 4;
        var chunkMaxX = (max.getX() + offsetX) >> 4;
        var chunkMinZ = (min.getZ() + offsetZ) >> 4;
        var chunkMaxZ = (max.getZ() + offsetZ) >> 4;

        var worldMinX = min.getX() + offsetX;
        var worldMinY = min.getY() + offsetY;
        var worldMinZ = min.getZ() + offsetZ;
        var worldMaxX = max.getX() + offsetX;
        var worldMaxY = max.getY() + offsetY;
        var worldMaxZ = max.getZ() + offsetZ;

        // === LOAD ALL CHUNKS ===
        var chunkLoadIterator = new RegionIterator(chunkMinX, 0, chunkMinZ, chunkMaxX, 0, chunkMaxZ);
        var chunkLoadTask = new ArenaBuildSubtask(chunkLoadIterator, 1, () -> {
            var chunk = world.getChunkAt(chunkLoadIterator.getX(), chunkLoadIterator.getZ());
            chunk.setForceLoaded(true);
            chunk.load(true);
            return true;
        });
        states.addTask(chunkLoadTask);

        // === DELETE EXISTING ENTITIES ===
        var entityRemoveIterator = new RegionIterator(chunkMinX, 0, chunkMinZ, chunkMaxX, 0, chunkMaxZ);
        var entityRemoveTask = new ArenaBuildSubtask(entityRemoveIterator, 1, () -> {
            var chunk = world.getChunkAt(chunkLoadIterator.getX(), chunkLoadIterator.getZ());
            for (var entity : chunk.getEntities()) {
                if (entity instanceof Player)
                    continue;

                var loc = entity.getLocation();
                if (loc.getBlockX() >= worldMinX && loc.getBlockX() <= worldMaxX && loc.getBlockY() >= worldMinY
                        && loc.getBlockY() <= worldMaxY && loc.getBlockZ() >= worldMinZ && loc.getBlockZ() <= worldMaxZ)
                    entity.remove();
            }

            return true;
        });
        states.addTask(entityRemoveTask);

        // === UPDATE ALL BLOCKS ===
        var buildIterator = new RegionIterator(min.getX(), min.getY(), min.getZ(), max.getX(), max.getY(), max.getZ());
        var buildTask = new ArenaBuildSubtask(buildIterator, BLOCKS_PER_TICK, () -> {
            var x = buildIterator.getX();
            var y = buildIterator.getY();
            var z = buildIterator.getZ();

            var block = world.getBlockAt(x + offsetX, y + offsetY, z + offsetZ);
            var targetBlock = schematic.getFullBlock(BlockVector3.at(x, y, z));

            var currentData = block.getBlockData();
            var targetData = BukkitAdapter.adapt(targetBlock);

            if (currentData.matches(targetData))
                return false;

            block.setBlockData(targetData, false);
            return true;
        });
        states.addTask(buildTask);

        // === SPAWN ALL ENTITIES ===
        var spawnEntitiesIterator = new RegionIterator(0, 0, 0, 0, 0, 0);
        var spawnEntitiesTask = new ArenaBuildSubtask(spawnEntitiesIterator, 1, () -> {
            ExtentEntityCopy entityCopy = new ExtentEntityCopy(origin.toVector3(), BukkitAdapter.adapt(world),
                    target.toVector3(), new Identity());
            List<? extends Entity> entities = Lists.newArrayList(schematic.getEntities());
            EntityVisitor entityVisitor = new EntityVisitor(entities.iterator(), entityCopy);

            try {
                Operations.complete(entityVisitor);
            } catch (WorldEditException e) {
                e.printStackTrace();
            }

            return true;
        });
        states.addTask(spawnEntitiesTask);

        // === UNLOAD ALL CHUNKS ===
        var chunkUnloadIterator = new RegionIterator(chunkMinX, 0, chunkMinZ, chunkMaxX, 0, chunkMaxZ);
        var chunkUnloadTask = new ArenaBuildSubtask(chunkUnloadIterator, 1, () -> {
            var chunk = world.getChunkAt(chunkLoadIterator.getX(), chunkLoadIterator.getZ());
            chunk.setForceLoaded(false);
            return false;
        });
        states.addTask(chunkUnloadTask);
    }

    public boolean isDone() {
        return states.isDone();
    }

    @Override
    public void run() {
        states.update();

        if (states.isDone())
            cancel();
    }
}
