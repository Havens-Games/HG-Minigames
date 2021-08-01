package net.whg.minigames.framework.arena;

import net.whg.utils.cmdformat.CommandHandler;

public class ArenaCommand extends CommandHandler {
    public ArenaCommand() {
        actions.add(new BuildArenaAction());
    }

    @Override
    public String getName() {
        return "arena";
    }
}
