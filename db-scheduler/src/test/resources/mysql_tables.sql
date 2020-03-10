create table scheduled_tasks (
  task_name varchar(40) not null,
  task_instance varchar(40) not null,
  task_data blob,
  execution_time timestamp(6) not null,
  picked BOOLEAN not null,
  picked_by varchar(50),
  last_success timestamp(6) null,
  last_failure timestamp(6) null,
  consecutive_failures INT,
  last_heartbeat timestamp(6) null,
  version BIGINT not null,
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
   active bool default true,
   create_time long,
   modify_time long,

   PRIMARY KEY (name)
);
