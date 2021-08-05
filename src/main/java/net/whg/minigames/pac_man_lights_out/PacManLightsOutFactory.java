package net.whg.minigames.pac_man_lights_out;

import net.whg.minigames.framework.Minigame;
import net.whg.minigames.framework.MinigameFactory;

public class PacManLightsOutFactory implements MinigameFactory {
    @Override
    public Minigame createInstance() {
        return new PacManLightsOut();
    }

    @Override
    public String getName() {
        return "PacManLightsOut";
    }

    @Override
    public boolean isInstanced() {
        return true;
    }

    @Override
    public int getMinPlayers() {
        return 4;
    }

    @Override
    public int getMaxPlayers() {
        return 4;
    }
}
