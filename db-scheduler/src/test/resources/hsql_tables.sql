create table scheduled_tasks (
	task_name varchar(100),
	task_instance varchar(100),
	task_data blob,
	execution_time TIMESTAMP WITH TIME ZONE,
	picked BIT,
	picked_by varchar(50),
	last_success TIMESTAMP WITH TIME ZONE,
	last_failure TIMESTAMP WITH TIME ZONE,
	consecutive_failures INT,
	last_heartbeat TIMESTAMP WITH TIME ZONE,
	version BIGINT,
	PRIMARY KEY (task_name, task_instance)
);

create table schedules (
   name varchar(40) not null,
   type varchar(16) not null,
   parameter varchar(256) not null,
   execution_class varchar(512) not null,
   execution_parameter_class varchar(512),
   execution_parameter blob,
   zone varchar(64),
   active bit default 1,
   create_time bigint,
   modify_time bigint,

   PRIMARY KEY (name)
);
