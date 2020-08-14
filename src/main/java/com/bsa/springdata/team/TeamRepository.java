package com.bsa.springdata.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<Team, UUID> {

    Optional<Team> findByName(String name);

    @Modifying
    @Transactional
    @Query(value = "update teams t " +
            "set name = t.name || '_' || sub.projectName || '_' || sub.technologyName " +
            "from " +
                "(select t.id as teamId, p.name as projectName, tec.name as technologyName " +
                "from " +
                    "teams t join projects p on t.project_id = p.id " +
                    "join technologies tec on t.technology_id = tec.id " +
                "where t.name = ?1) as sub " +
            "where t.id = sub.teamId",
            nativeQuery = true
    )
    int normalizeName(String hipsters);

    @Query(value="select count(t) from Team t join t.technology tec where tec.name = :newTechnology")
    int countByTechnologyName(String newTechnology);

    @Query(value =
            "select * from teams t where t.id in " +
                    "(select res.teamId from " +
                        "(select t.id as teamId, count(*) as numberUsers " +
                        "from " +
                            "teams t join technologies tec on t.technology_id = tec.id " +
                            "left join users on t.id = users.team_id " +
                        "where tec.name = ?2 " +
                        "group by t.id" +
                        ") as res " +
                    "where res.numberUsers < ?1)",
            nativeQuery = true)
    List<Team> getTeamsWithTechnologyAndNumberUsers(int devsNumber, String oldTechnologyName);


}
