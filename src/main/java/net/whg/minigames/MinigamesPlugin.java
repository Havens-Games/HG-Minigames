package net.whg.minigames;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import net.whg.minigames.framework.MinigameCommand;
import net.whg.minigames.framework.MinigameManager;
import net.whg.minigames.jump_pad.JumpPadFactory;
import net.whg.minigames.pac_man_lights_out.PacManLightsOutFactory;

public class MinigamesPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        updateDefaultConfig();

        var minigameManager = new MinigameManager(this);

        getCommand("mg").setExecutor(new MinigameCommand(minigameManager));

        minigameManager.registerMinigameType(new JumpPadFactory());
        minigameManager.registerMinigameType(new PacManLightsOutFactory());

        Bukkit.getLogger().log(Level.INFO, "HG-Minigames has been enabled.");
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);

        Bukkit.getLogger().log(Level.INFO, "HG-Minigames has been disabled.");
    }

    private void updateDefaultConfig() {
        var config = getConfig();

        config.addDefault("MinigameWorld", "world");
        config.addDefault("ArenaDistance", 2500);

        config.options().copyDefaults(true);
        saveConfig();
    }
}
