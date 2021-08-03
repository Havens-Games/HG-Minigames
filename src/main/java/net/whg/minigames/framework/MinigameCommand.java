package net.whg.minigames.framework;

import net.whg.utils.cmdformat.CommandHandler;

public class MinigameCommand extends CommandHandler {
    public MinigameCommand(MinigameManager manager) {
        actions.add(new MinigameJoinAction(manager));
        actions.add(new MinigameLeaveAction(manager));
    }

    @Override
    public String getName() {
        return "mg";
    }
}
