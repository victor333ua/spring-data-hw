package com.bsa.springdata.team;

import com.bsa.springdata.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class TeamService {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TechnologyRepository technologyRepository;

    public void updateTechnology(int devsNumber, String oldTechnologyName, String newTechnologyName) {
        // TODO: You can use several queries here. Try to keep it as simple as possible

// search id for new technology
        var newTech = technologyRepository.findByName(newTechnologyName);
        if (newTech.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "new technology doesn't exist");

// search for teams with old technology and number of users < devsNumber
        var listOfTeams =  teamRepository.getTeamsWithTechnologyAndNumberUsers(devsNumber, oldTechnologyName);
        if (listOfTeams.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "team with such parameters doesn't exist");

        listOfTeams.forEach(t -> t.setTechnology(newTech.get()));

        teamRepository.saveAll(listOfTeams);
        teamRepository.flush();
    }

    public void normalizeName(String hipsters) {
        // TODO: Use a single query. You need to create a native query
        if(teamRepository.normalizeName(hipsters) == 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "team with such name doesn't exist");
    }
}
