package com.bsa.springdata.office;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/office")

public class OfficeController {
    @Autowired
    private OfficeService officeService;

    @GetMapping("/technology/{tec}")
    public List<OfficeDto> getOfficeService(@PathVariable String tec) {
        return officeService.getByTechnology(tec);
    }

    @GetMapping
    public OfficeDto updateAddress(@RequestParam Map<String, String> query) {
            return officeService.updateAddress(query.get("oldAddress"), query.get("newAddress"))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "office with users in oldAddress not found"));

    }
}
