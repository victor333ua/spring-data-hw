package com.bsa.springdata.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query(value =
     "delete from roles r where r.code = :code and r.id in " +
         "(select r.id from " +
             "roles r left outer join user2role ur on r.id = ur.role_id " +
          "where ur.user_id is null) " +
     "returning *",
      nativeQuery = true)
    public Role deleteByNameAbsentUser(String code);
}
