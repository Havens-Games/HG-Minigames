package net.whg.minigames.jump_pad;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.MinigameFactory;
import net.whg.minigames.framework.MinigameID;
import net.whg.minigames.framework.MinigameManager;
import net.whg.minigames.framework.SchematicPlaceholder;

public class JumpPadFactory implements MinigameFactory {

    @Override
    public Minigame createInstance(MinigameManager manager, MinigameID id) {
        return new JumpPad(manager, id);
    }

    @Override
    public String getName() {
        return "JumpPad";
    }

    @Override
    public boolean isInstanced() {
        return false;
    }

    @Override
    public int getMinPlayers() {
        return 0;
    }

    @Override
    public int getMaxPlayers() {
        return Integer.MAX_VALUE;
    }

    @Override
    public SchematicPlaceholder[] getPlaceholders() {
        // TODO Auto-generated method stub
        return null;
    }
}
