package com.bsa.springdata.user;

import com.bsa.springdata.user.dto.CreateUserDto;
import com.bsa.springdata.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable UUID id) {
        return userService.getUserById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @PostMapping
    public UUID createUser(@RequestBody CreateUserDto user) {
        return userService.createUser(user)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not create user."));
    }

    @DeleteMapping("/experience/{exp}")
    public long deleteUserByExperience(@PathVariable int exp) {
        return userService.deleteByExperience(exp);
     }

    @GetMapping("/city/{city}")
    public List<UserDto> findByCity(@PathVariable String city) {
        return userService.findByCity(city);
    }

    @GetMapping("/roomCity")
    public List<UserDto> findByRoomCity(@RequestParam Map<String, String> query) {
        return userService.findByRoomAndCity(query.get("city"), query.get("room"));
    }

    @GetMapping("/experience/{exp}")
    public List<UserDto> getByExperience(@PathVariable int exp){
        return userService.findByExperience(exp);
    }

    @GetMapping("/lastName")
    public List<UserDto> getByLastName(@RequestParam Map<String, String> query) {
        return userService.findByLastName(
                query.get("lastName"),
                Integer.parseInt(query.get("page")),
                Integer.parseInt(query.get("size"))
        );
    }
}
