package com.github.kagkarlsson.scheduler;

import com.github.kagkarlsson.scheduler.task.OnStartup;
import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.schedule.ScheduleData;
import java.util.List;


/*
this servers both memory schedule repo and db repo
it has functions for both or for either because two repos are not the same
this is a way to encapsulate the persistence layer without changing a lot to the original open source project
 */
public interface ScheduleRepository {

    // schedule data
    boolean add(ScheduleData schedule);
    ScheduleData get(String name);
    List<ScheduleData> getAll();

    // task
    boolean add(Task task);
    Task getTask(String name);
    List<Task> getAllTasks();

    // startup
    List<OnStartup> getOnStartups(List<OnStartup> onStartup);
}
