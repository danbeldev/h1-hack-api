CREATE TABLE projects
(
    id   bigint generated always as identity,
    name varchar(128),

    CONSTRAINT PK__projects__key PRIMARY KEY (id)
);

CREATE TABLE tasks
(
    id         bigint generated always as identity,
    data       jsonb  not null,
    properties jsonb  not null,
    project_id bigint not null,

    CONSTRAINT PK__tasks__key PRIMARY KEY (id),
    CONSTRAINT FK__tasks__project FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE task_updates
(
    id            bigint generated always as identity,
    project_id    bigint        not null,
    property_name varchar(64)   not null,
    old_value     varchar(1080) not null,
    new_value     varchar(1080) default null,
    data          timestamp     not null,

    CONSTRAINT PK__task_updates__key PRIMARY KEY (id),
    CONSTRAINT FK__task_updates__project FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE reports
(
    id            bigint generated always as identity,
    name          varchar(256) not null,
    project_id    bigint       not null,
    date_creation timestamp    not null,
    date_updated  timestamp default null,

    CONSTRAINT PK__reports__key PRIMARY KEY (id),
    CONSTRAINT FK__reports__project FOREIGN KEY (project_id) REFERENCES projects (id)
);

CREATE TABLE report_elements
(
    id        bigint generated always as identity,
    "order"   int    not null,
    data      jsonb default null,
    styles    jsonb default null,
    report_id bigint not null,

    CONSTRAINT PK__report_elements__key PRIMARY KEY (id),
    CONSTRAINT FK__report_elements__report FOREIGN KEY (report_id) REFERENCES reports (id),
    CONSTRAINT UQ__report_elements__id_order UNIQUE (id, "order")
);