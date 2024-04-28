-- recreating schema and inserting values needed for jpa tests.

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

insert into fantasy_users (username, email, fantasy_total_points, fantasy_weekly_points, is_account_non_expired,
                           is_account_non_locked, is_credentials_non_expired, is_enabled, name, password, role, surname)
values ('kdemirov', 'test@example.com', 0, 0, true, true, true, true, 'Test', 'test',
        'ROLE_USER', 'Test');

insert into fantasy_users (username, email, fantasy_total_points, fantasy_weekly_points, is_account_non_expired,
                           is_account_non_locked, is_credentials_non_expired, is_enabled, name, password, role, surname)
values ('admin', 'test@example.com', 0, 0, true, true, true, true, 'Test', 'test',
        'ROLE_ADMIN', 'Test');

insert into team (id, code, conference, image_url, name, players_url)
values (1, null, 'division', '/imageUrl', 'teamName', '/playerUrl');

insert into team (id, code, conference, image_url, name, players_url)
values (2, null, 'division', '/imageUrl', 'teamName2', '/playerUrl');

insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (1, 31, '1.1.1994', 0, 0, '185', 'Center Player', '1', '/imageUrl', '/playerUrl', 'C', 420.0, 'School', 0,
        '45', 1, 'R');

insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (2, 31, '1.1.1994', 0, 0, '185', 'Forward Right', '1', '/imageUrl', '/playerUrl', 'F', 420.0, 'School',
        0, '45', 1, 'R');
insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (3, 31, '1.1.1994', 0, 0, '185', 'Forward Left', '1', '/imageUrl', '/playerUrl', 'F', 420.0, 'School', 0,
        '45', 1, 'R');
insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (4, 31, '1.1.1994', 0, 0, '185', 'Guard Right', '1', '/imageUrl', '/playerUrl', 'G', 420.0, 'School', 0,
        '45', 1, 'R');
insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (5, 31, '1.1.1994', 0, 0, '185', 'Guard Left', '1', '/imageUrl', '/playerUrl', 'G', 420.0, 'School', 0,
        '45', 1, 'R');
insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (6, 31, '1.1.1994', 0, 0, '185', 'Center Player 2', '1', '/imageUrl', '/playerUrl', 'C', 420.0, 'School', 0,
        '45', 2, 'R');
insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (7, 31, '1.1.1994', 0, 0, '185', 'Forward Right', '1', '/imageUrl', '/playerUrl', 'F', 420.0, 'School',
        0, '45', 2, 'R');
insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (8, 31, '1.1.1994', 0, 0, '185', 'Forward Left', '1', '/imageUrl', '/playerUrl', 'F', 420.0, 'School', 0,
        '45', 2, 'R');
insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (9, 31, '1.1.1994', 0, 0, '185', 'Guard player right', '1', '/imageUrl', '/playerUrl', 'G', 420.0, 'School', 0,
        '45', 2, 'R');
insert into player (id, age, birth_date, fantasy_point_per_game, fantasy_points_weekly, height, name, number,
                    player_image_url, player_url, position, price, school, total_fantasy_points, weight_in_lbs,
                    team_id, experience)
values (10, 31, '1.1.1994', 0, 0, '185', 'Guard player left', '1', '/imageUrl', '/playerUrl', 'G', 420.0, 'School', 0,
        '45', 2, 'R');

insert into fantasy_users_my_team (user_username, my_team_id)
values ('kdemirov', 1);

insert into fantasy_groups (id, name)
values (1, 'GroupName');

insert into fantasy_groups_users(group_id, users_username)
values (1, 'kdemirov');

insert into fantasy_users_groups(user_username, groups_id)
values ('kdemirov', 1);

insert into game(id, day_begin, game_details_url, points_home_team, points_away_team, time, week, home_team_id,
                 away_team_id)
values (1, 'Tuesday 14 May', '/gameDetails/boxScore', 111, 111, 'Final', '1', 1, 2);

insert into game(id, day_begin, game_details_url, points_home_team, points_away_team, time, week, home_team_id,
                 away_team_id)
values (2, 'Tuesday 14 May', null, 0, 0, '1:30 PM ET', '1', 2, 1);
