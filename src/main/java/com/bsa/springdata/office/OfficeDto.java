package com.bsa.springdata.office;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
public class OfficeDto {
    private final UUID id;
    private final String city;
    private final String address;

    public static OfficeDto fromEntity(Office office) {
        return OfficeDto
                .builder()
                .id(office.getId())
                .address(office.getAddress())
                .city(office.getCity())
                .build();
    }
}
