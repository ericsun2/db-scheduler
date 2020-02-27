package com.github.kagkarlsson.scheduler;

import com.github.kagkarlsson.jdbc.JdbcRunner;
import com.github.kagkarlsson.jdbc.ResultSetMapper;
import com.github.kagkarlsson.jdbc.SQLRuntimeException;
import com.github.kagkarlsson.scheduler.task.OnStartup;
import com.github.kagkarlsson.scheduler.task.Task;
import com.github.kagkarlsson.scheduler.task.VoidExecutionHandler;
import com.github.kagkarlsson.scheduler.task.helper.Tasks;
import com.github.kagkarlsson.scheduler.task.schedule.ScheduleData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JdbcScheduleRepository implements ScheduleRepository {
    public static final String DEFAULT_TABLE_NAME = "schedules";

    private static final Logger LOG = LoggerFactory.getLogger(JdbcScheduleRepository.class);
    private final JdbcRunner jdbcRunner;
    private final String tableName;

    public JdbcScheduleRepository(DataSource dataSource) {
        this(dataSource, DEFAULT_TABLE_NAME);
    }

    public JdbcScheduleRepository(DataSource dataSource, String tableName) {
        this.tableName = tableName;
        this.jdbcRunner = new JdbcRunner(dataSource);
    }

    @Override
    public boolean add(ScheduleData schedule) {
        try {
            jdbcRunner.execute(
                "insert into " + tableName + "(name, type, parameter, execution_class, zone, active, create_time, modify_time)"
                    + " values(?, ?, ?, ?, ?, ?, ?, ?)",
                (PreparedStatement p) -> {
                    p.setString(1, schedule.name != null ? schedule.name : "");
                    p.setString(2, schedule.type.toString());
                    p.setString(3, schedule.parameter != null ? schedule.parameter : "");
                    p.setString(4, schedule.executionClass.getName());
                    p.setString(5, schedule.zone != null ? schedule.zone.toString() : "");
                    p.setBoolean(6, schedule.active);
                    p.setLong(7, schedule.createTime);
                    p.setLong(8, schedule.modifyTime);
                });
            return true;

        } catch (SQLRuntimeException e) {
            LOG.debug("Exception when inserting schedule. Assuming it to be a constraint violation.", e);
        }
        return false;
    }

    @Override
    public ScheduleData get(String name) {
        List<ScheduleData> r = jdbcRunner.query(
            "select * from " + tableName + " where name=?",
            (PreparedStatement p) -> {
                p.setString(1, name);
            },
            new ScheduleDataResultSetMapper()
        );
        return r.size() >= 1 ? r.get(0) : null;
    }

    @Override
    public List<ScheduleData> getAll() {
        return jdbcRunner.query(
            "select * from " + tableName,
            (PreparedStatement p) -> { },
            new ScheduleDataResultSetMapper()
        );
    }

    /*
    only return tasks from active schedules
     */
    @Override
    public Task getTask(String name) {
        ScheduleData r = get(name);
        return r != null && r.active ? Tasks.fromScheduleData(r) : null;
    }

    /*
    only return tasks from active schedules
     */
    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        List<ScheduleData> r = getAll();
        if (r == null) {
            return tasks;
        }
        r.forEach(sd -> {
            if (sd != null && sd.active) {
                Task task = Tasks.fromScheduleData(sd);
                if (task != null) {
                    tasks.add(task);
                }
            }
        });
        return tasks;
    }

    // this function is not needed
    @Override
    public boolean add(Task task) {
        return true;
    }

    /*
    load tasks from db and start them
    onStartup parameter is ignored
     */
    @Override
    public List<OnStartup> getOnStartups(List<OnStartup> onStartup) {
        List<Task> tasks = getAllTasks();
        if (tasks == null) {
            return null;
        }
        return tasks.stream()
            .filter(task -> task != null && task instanceof OnStartup)
            .map(task -> (OnStartup) task)
            .collect(Collectors.toList());
    }

    private class ScheduleDataResultSetMapper implements ResultSetMapper<List<ScheduleData>> {

        private final ArrayList<ScheduleData> list;
        private final ScheduleDataResultSetConsumer delegate;

        private ScheduleDataResultSetMapper() {
            this.list = new ArrayList<>();
            this.delegate = new ScheduleDataResultSetConsumer(list::add);
        }

        @Override
        public List<ScheduleData> map(ResultSet resultSet) throws SQLException {
            this.delegate.map(resultSet);
            return this.list;
        }
    }

    private class ScheduleDataResultSetConsumer implements ResultSetMapper<Void> {

        private final Consumer<ScheduleData> consumer;

        private ScheduleDataResultSetConsumer(Consumer<ScheduleData> consumer) {
            this.consumer = consumer;
        }

        @Override
        public Void map(ResultSet rs) throws SQLException {
            while (rs.next()) {
                try {
                    Class c = Class.forName(rs.getString("execution_class"));
                    String zoneStr = rs.getString("zone");
                    ZoneId zone = zoneStr != null && zoneStr.length() > 1 ? ZoneId.of(zoneStr) : null;

                    this.consumer.accept(new ScheduleData(rs.getString("name"), ScheduleData.ScheduleType.valueOf(rs.getString("type")),
                        rs.getString("parameter"), c, zone, rs.getBoolean("active"),
                        rs.getLong("create_time"), rs.getLong("modify_time")));
                } catch (Exception e) {
                    LOG.error("failed to read schedule from db: {}", e.getMessage());
                }
            }

            return null;
        }
    }

}
