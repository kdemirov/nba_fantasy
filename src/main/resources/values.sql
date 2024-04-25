insert into fantasy_users (username, email, fantasy_total_points, fantasy_weekly_points, is_account_non_expired,
                           is_account_non_locked, is_credentials_non_expired, is_enabled, name, password, role, surname)
values ('kdemirov', 'test@example.com', 0, 0, true, true, true, true, 'Kjazim', 'test',
        'ROLE_USER', 'Demirov');

insert into fantasy_users (username, email, fantasy_total_points, fantasy_weekly_points, is_account_non_expired,
                           is_account_non_locked, is_credentials_non_expired, is_enabled, name, password, role, surname)
values ('admin', 'test@example.com', 0, 0, true, true, true, true, 'Kjazim', 'test',
        'ROLE_ADMIN', 'Demirov');