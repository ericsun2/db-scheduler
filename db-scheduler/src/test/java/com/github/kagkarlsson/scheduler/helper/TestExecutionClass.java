package com.github.kagkarlsson.scheduler.helper;

import com.github.kagkarlsson.scheduler.task.ExecutionContext;
import com.github.kagkarlsson.scheduler.task.TaskInstance;
import com.github.kagkarlsson.scheduler.task.VoidExecutionHandler;


public class TestExecutionClass implements VoidExecutionHandler<Void> {
    public TestExecutionClass() {

    }

    @Override
    public void execute(TaskInstance<Void> taskInstance, ExecutionContext executionContext) {
        System.out.println(String.format("thread: %s, task: %s, timestamp %d",
            Thread.currentThread().getName(), taskInstance.getTaskName(), System.currentTimeMillis()));
    }
}
