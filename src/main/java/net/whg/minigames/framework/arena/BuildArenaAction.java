package net.whg.minigames.framework.arena;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import net.whg.utils.CmdPlayer;
import net.whg.utils.cmdformat.Subcommand;
import net.whg.utils.exceptions.CommandException;
import net.whg.utils.exceptions.NoPermissionsException;

public class BuildArenaAction extends Subcommand {

    private class ReportWhenFinished extends BukkitRunnable {
        private final CmdPlayer sender;
        private final Arena arena;
        private final ArenaBuildTask task;
        private final long startTime;

        ReportWhenFinished(CmdPlayer sender, Arena arena, ArenaBuildTask task) {
            this.sender = sender;
            this.arena = arena;
            this.task = task;
            this.startTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            if (!task.isDone())
                return;

            var timeElapsed = System.currentTimeMillis() - startTime;
            var seconds = String.format("%.1f", (float) (timeElapsed / 1000.0));

            sender.sendConfirmation("Arena '%s' has finished construction. Took %s seconds.", arena.getName(), seconds);
            cancel();
        }
    }

    @Override
    public void execute(CmdPlayer sender, String[] args) throws CommandException {
        if (!sender.isOp())
            throw new NoPermissionsException("You do not have permission to build an arena!");

        var arenaType = args[0];
        var world = getWorld(args[1]);
        var x = getInteger(args[2]);
        var y = getInteger(args[3]);
        var z = getInteger(args[4]);

        var location = new Location(world, x, y, z);
        var arena = new Arena(location, arenaType);

        try {
            var task = arena.buildArena();
            sender.sendConfirmation("Construction on the arena has begun.");

            var plugin = Bukkit.getPluginManager().getPlugin("HavensGames-Minigames");
            new ReportWhenFinished(sender, arena, task).runTaskTimer(plugin, 1, 1);
        } catch (IOException e) {
            sender.sendError("Failed to load the arena schematic file! See console for more information.");
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return "build";
    }

    @Override
    public String getUsage() {
        return "<name> <world> <x> <y> <z>";
    }
}
