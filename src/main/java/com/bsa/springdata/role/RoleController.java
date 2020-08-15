package com.bsa.springdata.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

@DeleteMapping("/{role}")
@ResponseStatus(code = HttpStatus.NO_CONTENT)
    void deleteRole(@PathVariable String role) {
        roleService.deleteRole(role);
    }
}
