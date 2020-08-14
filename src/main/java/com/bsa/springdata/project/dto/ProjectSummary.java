package com.bsa.springdata.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSummary {
    private String name;
    private long teamsNumber;
    private long developersNumber;
    private String technologies;
}
