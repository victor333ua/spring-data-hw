package com.bsa.springdata.user;

import com.bsa.springdata.office.OfficeRepository;
import com.bsa.springdata.team.TeamRepository;
import com.bsa.springdata.user.dto.CreateUserDto;
import com.bsa.springdata.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private TeamRepository teamRepository;

    public Optional<UUID> safeCreateUser(CreateUserDto userDto) {
        var office = officeRepository.findById(userDto.getOfficeId());
        var team = teamRepository.findById((userDto.getTeamId()));

        return office.flatMap(o -> team.map(t -> {
            var user = User.fromDto(userDto, o, t);
            var result = userRepository.save(user);
            return result.getId();
        }));
    }

    public Optional<UUID> createUser(CreateUserDto userDto) {
        try {
            var office = officeRepository.getOne(userDto.getOfficeId());
            var team = teamRepository.getOne(userDto.getTeamId());

            var user = User.fromDto(userDto, office, team);
            var result = userRepository.save(user);
            return Optional.of(result.getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<UserDto> getUserById(UUID id) {
        return userRepository.findById(id).map(UserDto::fromEntity);
    }

    public List<UserDto> getUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserDto> findByLastName(String lastName, int page, int size) {
        // TODO: Use a single query. Use class Sort to sort users by last name. Try to avoid @Query annotation here

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("lastName"));

        ExampleMatcher exampleMatcher = ExampleMatcher
                .matchingAny()
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<User> example = Example.of(User.builder().lastName(lastName).build(), exampleMatcher);

        return userRepository
                .findAll(example, pageable)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserDto> findByCity(String city, int size, int page) {
        // TODO: Use a single query. Sort users by last name
        return userRepository
                .findAllByCity(city, PageRequest.of(page, size))
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserDto> findByExperience(int experience) {
        // TODO: Use a single query. Sort users by experience by descending. Try to avoid @Query annotation here

        return userRepository
                .findByExperienceGreaterThanEqualOrderByExperienceDesc(experience)
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<UserDto> findByRoomAndCity(String city, String room) {
        // TODO: Use a single query. Use class Sort to sort users by last name.
        return userRepository
                .findAllByRoomCity(room, city, Sort.by("lastName"))
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    public long deleteByExperience(int experience) {
        // TODO: Use a single query. Return a number of deleted rows
        var resp = userRepository.deleteByExperience(experience);
//        return resp.size();
        return resp;
    }
}
