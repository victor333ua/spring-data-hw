package com.bsa.springdata.project;

import com.bsa.springdata.team.Team;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "project")
    private final List<Team> teams = new ArrayList<>();
}
