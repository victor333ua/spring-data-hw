package com.bsa.springdata.team;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "technologies")
public class Technology {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String description;
    private String link;
}