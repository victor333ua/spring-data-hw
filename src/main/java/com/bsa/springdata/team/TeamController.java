package com.bsa.springdata.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/team")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @PostMapping("/updateTec")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void updateTeamToNewTec(@RequestParam Map<String, String> query) {
        teamService.updateTechnology(
                Integer.parseInt(query.get("devNumber")),
                query.get("oldTechnology"),
                query.get("newTechnology")
        );
    }
    @PostMapping("/normalizeName/{name}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    void normalizeTeamName(@PathVariable String name) {
        teamService.normalizeName(name);
    }
}
