package com.github.kagkarlsson.scheduler.helper;

import com.github.kagkarlsson.scheduler.task.ExecutionContext;
import com.github.kagkarlsson.scheduler.task.TaskInstance;
import com.github.kagkarlsson.scheduler.task.VoidExecutionHandler;


public class TestExecutionClass implements VoidExecutionHandler<Void> {
    private TestExecutionParameter _p;

    public TestExecutionClass(TestExecutionParameter p) {
        this._p = p;
    }

    public TestExecutionClass() {

    }

    @Override
    public void execute(TaskInstance<Void> taskInstance, ExecutionContext executionContext) {
        System.out.println(String.format("thread: %s, task: %s, timestamp %d, contextParameter %s, executionParameter %s",
            Thread.currentThread().getName(), taskInstance.getTaskName(), System.currentTimeMillis(),
            executionContext.getParameter() != null ? executionContext.getParameter().toString() : "null",
            _p != null ? _p.getTestStr() : "null"));
    }
}
