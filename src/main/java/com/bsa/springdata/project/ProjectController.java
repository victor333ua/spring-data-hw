package com.bsa.springdata.project;

import com.bsa.springdata.project.dto.CreateProjectRequestDto;
import com.bsa.springdata.project.dto.ProjectDto;
import com.bsa.springdata.project.dto.ProjectSummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @GetMapping("/biggest")
    ProjectDto findBiggest() {
        return projectService.findTheBiggest().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "biggest project not found"));
    }

    @GetMapping
    List<ProjectDto> find5ByTechnology(@RequestParam Map<String, String> tec) {
        return projectService.findTop5ByTechnology(tec.get("technology"));
    }

    @GetMapping("/summary")
    List<ProjectSummaryDto> getProjectsInformation() {
        return projectService.getSummary();
    }

    @GetMapping("/role")
    int getCountProjectsWithRole(@RequestParam Map<String, String> role) {
       return projectService.getCountWithRole(role.get("role"));
    }

    @PostMapping("/create")
    UUID createNewProject(@RequestBody CreateProjectRequestDto project) {
        return projectService.createWithTeamAndTechnology(project);
    }

}