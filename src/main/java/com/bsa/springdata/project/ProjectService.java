package com.bsa.springdata.project;

import com.bsa.springdata.project.dto.CreateProjectRequestDto;
import com.bsa.springdata.project.dto.ProjectDto;
import com.bsa.springdata.project.dto.ProjectSummaryDto;
import com.bsa.springdata.team.Team;
import com.bsa.springdata.team.TeamRepository;
import com.bsa.springdata.team.Technology;
import com.bsa.springdata.team.TechnologyRepository;
import com.bsa.springdata.user.User;
import com.bsa.springdata.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TechnologyRepository technologyRepository;
    @Autowired
    private TeamRepository teamRepository;

    public List<ProjectDto> findTop5ByTechnology(String technology) {
        // TODO: Use single query to load data. Sort by number of developers in a project
        //  Hint: in order to limit the query you can either use native query with limit or Pageable interface
       return projectRepository.findTop5ByTechnology(technology)
                .stream()
                .map(ProjectDto::fromEntity)
                .collect(Collectors.toList());
    }

    public Optional<ProjectDto> findTheBiggest() {
        // TODO: Use single query to load data. Sort by teams, developers, project name
        //  Hint: in order to limit the query you can either use native query with limit or Pageable interface
        return Optional.of(ProjectDto.fromEntity(projectRepository.findBiggest()));
    }

    public List<ProjectSummaryDto> getSummary() {
        // TODO: Try to use native query and projection first. If it fails try to make as few queries as possible

        return projectRepository.projectSummary();
    }

    public int getCountWithRole(String role) {
        // TODO: Use a single query
        return projectRepository.getCountWithRole(role);
    }

    @Transactional
    public UUID createWithTeamAndTechnology(CreateProjectRequestDto createProjectRequest) {
        // TODO: Use common JPARepository methods. Build entities in memory and then persist them

        var project = Project.builder()
                .name(createProjectRequest.getProjectName())
                .description(createProjectRequest.getProjectDescription())
                .build();
        var tech = Technology.builder()
                .description(createProjectRequest.getTechDescription())
                .link(createProjectRequest.getTechLink())
                .name(createProjectRequest.getTech())
                .build();
        var team = Team.builder()
                .area(createProjectRequest.getTeamArea())
                .name(createProjectRequest.getTeamName())
                .room(createProjectRequest.getTeamRoom())
                .technology(tech)
                .project(project)
                .build();

        project.getTeams().add(team);
        team = teamRepository.saveAndFlush(team);

        return team.getProject().getId();
    }
}
