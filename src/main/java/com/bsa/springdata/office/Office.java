package com.bsa.springdata.office;

import com.bsa.springdata.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "offices")
public class Office {
    @Id
    @GeneratedValue
    private UUID id;
    private String city;
    private String address;

    @OneToMany(mappedBy = "office")
    private final List<User> users = new ArrayList<>();
}
