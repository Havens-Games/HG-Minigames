package net.whg.minigames.framework.arena;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains a list of subtasks. This class will automatically handling executing
 * each subtask to completion in order until all subtasks are complete.
 */
public class ArenaBuildStates {
    private final List<ArenaBuildSubtask> subtasks = new ArrayList<>();
    private int state;

    /**
     * Adds a new task to the end of this queue. This function should not be called
     * after the updates have already started.
     * 
     * @param task - The task to add.
     */
    public void addTask(ArenaBuildSubtask task) {
        subtasks.add(task);
    }

    /**
     * Updates the next task in the queue. If a task is still be executed, this
     * function will continue to execute that task. Once the task is complete, it
     * will execute the next task on the next tick. If all tasks in this queue are
     * completed, this function preforms no action.
     */
    public void update() {
        if (isDone())
            return;

        var task = subtasks.get(state);
        if (task.update())
            state++;
    }

    /**
     * Checks whether or not all tasks in this task queue are complete.
     * 
     * @return True if all tasks are completed. False otherwise.
     */
    public boolean isDone() {
        return state >= subtasks.size();
    }
}
