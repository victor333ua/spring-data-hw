package com.bsa.springdata.project;

import com.bsa.springdata.project.dto.ProjectSummary;
import com.bsa.springdata.project.dto.ProjectSummaryDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    @Query(value =
     "select * " +
     "from " +
         "projects p join " +
                 "(select t.project_id as projectId, count(*) as usersCount " +
                 "from " +
                     "teams t join technologies tec on t.technology_id = tec.id " +
                     "join users u on u.team_id = t.id " +
                 "where tec.name = ?1 " +
                 "group by projectId) as res " +
     "on p.id = res.projectId " +
     "order by usersCount DESC " +
     "limit 5",
    nativeQuery = true)
    List<Project> findTop5ByTechnology(String name);


    @Query(value = "select * from biggest_project()", nativeQuery = true)
    Project findBiggest();


    @Query(value = "select ps.name as name, ps.teams_number as teamsNumber," +
            " ps.developers_number as developersNumber, ps.technologies as technologies" +
            " from project_summary as ps", nativeQuery=true)
    List<ProjectSummaryDto> projectSummary();

    @Query(value =
/*
            "select count(distinct(p.id)) " +
            "from " +
                "projects p join teams t on p.id = t.project_id " +
                "join " +
                    "(select u.team_id as teamId " +
                    "from " +
                            "users u join user2role ur on u.id = ur.user_id " +
                            "join roles  r on ur.role_id = r.id " +
                            "where r.name = ?1) subUsers " +
                "on teamId = t.id",
            nativeQuery = true)
*/
    "select count( distinct p ) " +
            "from " +
                "User u join u.team t join t.project p join u.roles r " +
            "where r.name = :role")
    int getCountWithRole(String role);
}