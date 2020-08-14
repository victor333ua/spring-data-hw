package com.bsa.springdata.user;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    List<User> findByExperienceGreaterThanEqualOrderByExperienceDesc(int experience);

    @Query(value = "select u from User u join u.office o where o.city = ?1 order by u.lastName")
    List<User> findAllByCity(String city);

    @Query(value = "select u from User u join u.office o join u.team t where t.room = ?1 and o.city = ?2")
    List<User> findAllByRoomCity(String room, String city, Sort sort);

    @Query(value = "select * from delete_users(?1)", nativeQuery=true)
    long deleteByExperience(int experience);
}
