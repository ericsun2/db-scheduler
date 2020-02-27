package com.github.kagkarlsson.scheduler;

import com.github.kagkarlsson.scheduler.task.OnStartup;
import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.schedule.ScheduleData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MapScheduleRepository implements ScheduleRepository {
    private static final Logger LOG = LoggerFactory.getLogger(MapScheduleRepository.class);

    private final Map<String, Task> _taskMap;

    public MapScheduleRepository() {
        this._taskMap = new HashMap<>();
    }

    @Override
    public boolean add(Task task) {
        if (task == null) {
            return false;
        }
        _taskMap.put(task.getName(), task);
        return true;
    }

    @Override
    public Task getTask(String name) {
        return _taskMap.get(name);
    }

    @Override
    public List<Task> getAllTasks() {
        return _taskMap.values().stream().collect(Collectors.toList());
    }

    // this function is not needed
    @Override
    public boolean add(ScheduleData schedule) {
        return true;
    }

    // this function is not needed
    @Override
    public ScheduleData get(String name) {
        return null;
    }

    // this function is not needed
    @Override
    public List<ScheduleData> getAll() {
        return null;
    }

    @Override
    public List<OnStartup> getOnStartups(List<OnStartup> onStartup) {
        return onStartup;
    }

}
