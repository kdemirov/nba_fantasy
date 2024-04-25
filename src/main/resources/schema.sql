drop table fantasy_users if exists;
drop table team if exists;
drop table player if exists;
drop table game if exists;
drop table fantasy_users_my_team if exists;
drop table fantasy_groups if exists;
drop table fantasy_users_groups if exists;
drop table fantasy_groups_users if exists;
drop table notifications if exists;
drop table fantasy_users_notifications if exists;
drop table confirmation_token if exists;

create table fantasy_users
(
    username                   varchar(255) primary key not null,
    email                      varchar(255),
    fantasy_total_points       integer,
    fantasy_weekly_points      integer,
    is_account_non_expired     boolean                  not null,
    is_account_non_locked      boolean                  not null,
    is_credentials_non_expired boolean                  not null,
    is_enabled                 boolean                  not null,
    name                       varchar(255),
    password                   varchar(255),
    role                       varchar(255),
    surname                    varchar(255)
);



create table team
(
    id          integer identity primary key,
    code        varchar(255),
    conference  varchar(255),
    image_url   varchar(255),
    name        varchar(255),
    players_url varchar(255)
);

create table player
(
    id                     integer identity primary key not null,
    age                    integer,
    birth_date             varchar(255),
    fantasy_point_per_game integer,
    fantasy_points_weekly  integer,
    height                 varchar(255),
    name                   varchar(255),
    number                 integer,
    player_image_url       varchar(255),
    player_url             varchar(255),
    position               varchar(255),
    price                  double precision             not null,
    school                 varchar(255),
    total_fantasy_points   integer,
    weight_in_lbs          varchar(255),
    team_id                bigint,
    experience             varchar(255),
    foreign key (team_id) references team (id)
);



create table game
(
    id identity primary key not null,
    day_begin        varchar(255),
    game_details_url varchar(255),
    points_away_team integer,
    points_home_team integer,
    time             varchar(255),
    week             varchar(255),
    away_team_id     bigint,
    home_team_id     bigint,
    foreign key (home_team_id) references team (id)
        match simple on update no action on delete no action,
    foreign key (away_team_id) references team (id)
        match simple on update no action on delete no action
);

create table fantasy_users_my_team
(
    user_username varchar(255) not null,
    my_team_id    bigint       not null,
    foreign key (my_team_id) references player (id)
        match simple on update no action on delete no action,
    foreign key (user_username) references fantasy_users (username)
        match simple on update no action on delete no action
);

create table fantasy_groups
(
    id   bigint identity primary key not null,
    name varchar(255)
);

create table notifications
(
    id       integer identity primary key not null,
    group_id bigint,
    foreign key (group_id) references fantasy_groups (id)
        match simple on update no action on delete no action
);


create table fantasy_users_notifications
(
    user_username    varchar(255) not null,
    notifications_id bigint       not null,
    foreign key (notifications_id) references notifications (id)
        match simple on update no action on delete no action,
    foreign key (user_username) references fantasy_users (username)
        match simple on update no action on delete no action
);



create table fantasy_users_groups
(
    user_username varchar(255) not null,
    groups_id     bigint       not null,
    foreign key (user_username) references fantasy_users (username)
        match simple on update no action on delete no action,
    foreign key (groups_id) references fantasy_groups (id)
        match simple on update no action on delete no action
);

create table fantasy_groups_users
(
    group_id       bigint       not null,
    users_username varchar(255) not null,
    foreign key (group_id) references fantasy_groups (id)
        match simple on update no action on delete no action,
    foreign key (users_username) references fantasy_users (username)
        match simple on update no action on delete no action
);

create table confirmation_token
(
    id                 bigint primary key not null,
    confirmation_token varchar(255),
    user_username      varchar(255),
    foreign key (user_username) references fantasy_users (username)
        match simple on update no action on delete no action
);
