package net.whg.minigames;

import org.bukkit.plugin.java.JavaPlugin;

import net.whg.minigames.framework.MinigameCommand;
import net.whg.minigames.framework.MinigameManager;
import net.whg.minigames.framework.arena.ArenaCommand;
import net.whg.minigames.jump_pad.JumpPadFactory;

public class MinigamesPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        updateDefaultConfig();

        var minigameManager = new MinigameManager(this);

        getCommand("mg").setExecutor(new MinigameCommand(minigameManager));
        getCommand("arena").setExecutor(new ArenaCommand());

        minigameManager.registerMinigameType(new JumpPadFactory());
    }

    private void updateDefaultConfig() {
        var config = getConfig();

        config.addDefault("MinigameWorld", "world");
        config.addDefault("ArenaDistance", 2500);

        config.options().copyDefaults(true);
        saveConfig();
    }
}
