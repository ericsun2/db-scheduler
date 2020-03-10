package com.github.kagkarlsson.scheduler.task.schedule;

import java.time.ZoneId;


public class ScheduleData {
    public enum ScheduleType {
        FIXED_DELAY,
        CRON,
    }

    public String name;
    public ScheduleType type;
    public String parameter;
    public Class executionClass;
    public Object executionParameter;
    public ZoneId zone;
    public boolean active;
    public long createTime;
    public long modifyTime;

    public ScheduleData() {
    }

    public ScheduleData(String name, ScheduleType type, String parameter, Class executionClass,
        Object executionParameter, ZoneId zone, boolean active, long createTime, long modifyTime) {
        this.name = name;
        this.type = type;
        this.parameter = parameter;
        this.executionClass = executionClass;
        this.executionParameter = executionParameter;
        this.zone = zone;
        this.active = active;
        this.createTime = createTime;
        this.modifyTime = modifyTime;
    }
}
