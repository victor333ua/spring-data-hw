package com.bsa.springdata.team.dto;

import com.bsa.springdata.team.Technology;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class TechnologyDto {
    private final UUID id;
    private final String name;
    private final String description;
    private final String link;

    public static TechnologyDto fromEntity(Technology technology) {
        return TechnologyDto
                .builder()
                .id(technology.getId())
                .name(technology.getName())
                .description(technology.getDescription())
                .link(technology.getLink())
                .build();
    }
}
