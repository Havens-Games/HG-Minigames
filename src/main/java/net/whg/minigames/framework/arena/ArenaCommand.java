package net.whg.minigames.framework.arena;

import net.whg.utils.cmdformat.CommandHandler;

public class ArenaCommand extends CommandHandler {
    public ArenaCommand(ArenaDistributor arenaDistributor) {
        actions.add(new BuildArenaAction(arenaDistributor));
    }

    @Override
    public String getName() {
        return "arena";
    }
}
