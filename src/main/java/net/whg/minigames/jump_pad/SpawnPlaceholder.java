package net.whg.minigames.jump_pad;

import org.bukkit.Location;

import net.md_5.bungee.api.ChatColor;
import net.whg.minigames.framework.SchematicPlaceholder;
import net.whg.minigames.framework.arena.Arena;

public class SpawnPlaceholder implements SchematicPlaceholder {

    @Override
    public String getNameTag() {
        return ChatColor.DARK_AQUA + "Spawn Loction";
    }

    @Override
    public String getMinigameType() {
        return "JumpPad";
    }

    @Override
    public void registerLocation(Arena arena, Location location) {
        // TODO Auto-generated method stub
        
    }
}
