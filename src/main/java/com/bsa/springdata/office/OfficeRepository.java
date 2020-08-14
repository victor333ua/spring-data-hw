package com.bsa.springdata.office;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OfficeRepository extends JpaRepository<Office, UUID> {

    @Query(value = "select distinct o from User u join u.office o join u.team t join t.technology tec where tec.name = :tec")
    List<Office>  getOfficesByTechnology(String tec);

    Optional<Office> getByAddress(String address);

    @Modifying
    @Transactional
    @Query(value = "update Office o set o.address = :newAddress " +
                    "where o.id in (select distinct o.id from User u join u.office o where o.address = :oldAddress)")
    void updateAddress(String oldAddress, String newAddress);
}
