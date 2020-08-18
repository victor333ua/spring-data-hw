create or replace function delete_users_record (ex integer ) returns setof users as
$$
begin
    delete from user2role ur where ur.user_id in
                                   (select u.id from users u where u.experience < ex);
    return query
        with deleted as (
            delete from users u where u.experience < ex
                returning *
        )
        select * from deleted;
end;
$$ language plpgsql;


create or replace function delete_users (ex integer ) returns bigint as
$$
    declare
         num bigint;
begin
    with delUser2Role as (
        delete from user2role ur where ur.user_id in
                                       (select u.id from users u where u.experience < ex)
    ), delUser as (
        delete from users u where u.experience < ex
            returning *
    )
    select count(*) into num from delUser;
    return num;
end;
$$ language plpgsql;



create or replace function biggest_project () returns setof projects as
$$
declare
    n_projects integer;
begin

    select count(*) into n_projects from (select * from projects_with_max_teams()) sub;
    if n_projects = 1 then
            return query select * from projects p where p.id in (select id from projects_with_max_teams());
    end if;

    select count(*) into n_projects from (select * from projects_with_max_users()) sub;
    if n_projects = 1 then
            return query select * from projects p where p.id in (select id from projects_with_max_users());
    end if;

    return query
        select * from projects p
        where p.id in
              (select id from projects_with_max_users()) order by p.name DESC limit 1 ;

end;
$$ language plpgsql;

create or replace function projects_with_max_teams ()
                    returns TABLE(id uuid, maxTeamsCount bigint) as
$$
begin
    return query (
        with projectTeamCount as
                 (select p.id as projectId, count(*) as teamsCount
                  from
                      projects p join teams t on p.id = t.project_id
                  group by p.id
                 ),
             projectsWithMaxTeams as
                 (select projectId, teamsCount as maxTeamsCount
                  from projectTeamCount
                  where teamsCount = (select max(teamsCount) from projectTeamCount)
                 )
        select * from projectsWithMaxTeams
    );
end;
$$ language plpgsql;

create or replace function projects_with_max_users ()
                     returns TABLE(id uuid, maxUsersCount numeric) as
$$
begin
    return query (
        with projectUserCount as
                 (select p.id as projectId, sum(teamUsersCount) as usersCount
                  from
                      projects p join
                      (select t.id as teamId, t.project_id as projectId, count(*) as teamUsersCount
                       from
                           teams t join users u on t.id = u.team_id
                       group by t.id, t.project_id) sub
                      on p.id = projectId
                  group by p.id
                 ),
             projectsWithMaxUsers as
                 (select projectId, usersCount as maxUsersCount
                  from projectUserCount
                  where usersCount = (select max(usersCount) from projectUserCount)
                 )
        select * from projectsWithMaxUsers
    );
end;
$$ language plpgsql;


create or replace view project_summary as

SELECT p.name,
       subteams.teams_number,
       subusers.developers_number,
       subtec.technologies
FROM projects p
         JOIN ( WITH projects_with_tec AS (
                    SELECT p_1.id AS projectid,
                        array_to_string(array_agg(tec.name), ','::text)::character varying(50) AS technologies
                    FROM projects p_1
                        JOIN teams t ON p_1.id = t.project_id
                        JOIN technologies tec ON tec.id = t.technology_id
                    GROUP BY p_1.id
                )
                SELECT projects_with_tec.projectid, projects_with_tec.technologies
                FROM
                     projects_with_tec) subtec ON p.id = subtec.projectid
                     JOIN ( WITH projectteamcount AS (
                                SELECT p_1.id AS projectid, count(*) AS teams_number
                                FROM
                                     projects p_1 JOIN teams t ON p_1.id = t.project_id
                                 GROUP BY p_1.id
                            )
                             SELECT projectteamcount.projectid, projectteamcount.teams_number
                             FROM
                                projectteamcount) subteams ON p.id = subteams.projectid
                                JOIN ( WITH projectusercount AS (
                                            SELECT p_1.id AS projectid,
                                                 sum(sub.teamuserscount)::bigint AS developers_number
                                            FROM
                                                projects p_1
                                                JOIN ( SELECT t.id AS teamid,
                                                             t.project_id AS projectid, count(*) AS teamuserscount
                                                FROM teams t
                                                         JOIN users u ON t.id = u.team_id
                                                GROUP BY t.id, t.project_id) sub
                            ON p_1.id = sub.projectid
                            GROUP BY p_1.id
                        )
                        SELECT projectusercount.projectid, projectusercount.developers_number
                        FROM projectusercount) subusers
                    ON p.id = subusers.projectid;