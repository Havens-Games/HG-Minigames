package net.whg.minigames;

import org.bukkit.plugin.java.JavaPlugin;

import net.whg.minigames.framework.MinigameCommand;
import net.whg.minigames.framework.MinigameManager;
import net.whg.minigames.framework.arena.ArenaCommand;

public class MinigamesPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        var minigameManager = new MinigameManager(this);

        getCommand("mg").setExecutor(new MinigameCommand(minigameManager));
        getCommand("arena").setExecutor(new ArenaCommand());
    }
}
