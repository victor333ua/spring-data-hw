package com.bsa.springdata.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class RoleDto {
    private final UUID id;
    private final String name;
    private final String code;

    public static RoleDto fromEntity(Role role) {
        return RoleDto
                .builder()
                .id(role.getId())
                .name(role.getName())
                .code(role.getCode())
                .build();
    }
}
