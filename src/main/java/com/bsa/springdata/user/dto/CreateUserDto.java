package com.bsa.springdata.user.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateUserDto {
    private final UUID id;
    private final String firstName;
    private final String lastName;
    private final int experience;
    private final UUID officeId;
    private final UUID teamId;
}