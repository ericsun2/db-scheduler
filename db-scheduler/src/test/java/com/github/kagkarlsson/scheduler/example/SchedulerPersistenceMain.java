package com.github.kagkarlsson.scheduler.example;

import com.github.kagkarlsson.scheduler.HsqlTestDatabaseRule;
import com.github.kagkarlsson.scheduler.Scheduler;
import com.github.kagkarlsson.scheduler.helper.TestExecutionClass;
import com.github.kagkarlsson.scheduler.helper.TestExecutionParameter;
import com.github.kagkarlsson.scheduler.task.schedule.ScheduleData;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SchedulerPersistenceMain {
	private static final Logger LOG = LoggerFactory.getLogger(SchedulerPersistenceMain.class);

	private static void example(DataSource dataSource) {

        final Scheduler scheduler = Scheduler
            .create(dataSource)
            .persistToDB()
            .persistHistory()
            .setContextParameter(new Integer(12))
            .build();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                LOG.info("Received shutdown signal.");
                scheduler.stop();
            }
        });

        scheduler.start();

        TestExecutionParameter parameter = new TestExecutionParameter("123");

        ScheduleData sdCron = new ScheduleData("testCron", ScheduleData.ScheduleType.CRON, "*/10 * * * * ?",
            TestExecutionClass.class, parameter, null, true, 0, 0);
        scheduler.addSchedule(sdCron);

        ScheduleData sdFixedDelay = new ScheduleData("testFixedDelay", ScheduleData.ScheduleType.FIXED_DELAY, "PT30S",
            TestExecutionClass.class, null, null, true, 0, 0);
        scheduler.addSchedule(sdFixedDelay);

        sleep(3000);
	}

	private static void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}

	public static void main(String[] args) throws Throwable {
		try {
			final HsqlTestDatabaseRule hsqlRule = new HsqlTestDatabaseRule();
			hsqlRule.before();
			final DataSource dataSource = hsqlRule.getDataSource();

			example(dataSource);
		} catch (Exception e) {
			LOG.error("Error", e);
		}

	}

}
