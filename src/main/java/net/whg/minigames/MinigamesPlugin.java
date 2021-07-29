package net.whg.minigames;

import org.bukkit.plugin.java.JavaPlugin;

import net.whg.minigames.framework.arena.ArenaCommand;

public class MinigamesPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getCommand("arena").setExecutor(new ArenaCommand());
    }
}
