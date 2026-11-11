package com.lunar.navigator.controller;

import com.lunar.navigator.model.CandidateSiteDTO;
import com.lunar.navigator.model.SiteTelemetryDTO;
import com.lunar.navigator.service.LunarEphemerisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/lunar")
public class LandingSiteController {

    private final LunarEphemerisService ephemerisService;

    public LandingSiteController(LunarEphemerisService ephemerisService) {
        this.ephemerisService = ephemerisService;
    }

    @GetMapping("/sites")
    public ResponseEntity<List<CandidateSiteDTO>> getCandidateSites() {
        return ResponseEntity.ok(ephemerisService.getAllCandidateSites());
    }

    @GetMapping("/sites/{id}/telemetry")
    public ResponseEntity<SiteTelemetryDTO> getTelemetry(
            @PathVariable String id,
            @RequestParam(required = false) String date,
            @RequestParam(defaultValue = "12") int hour) {
        SiteTelemetryDTO telemetry = ephemerisService.calculateTelemetry(id, date, hour);
        return ResponseEntity.ok(telemetry);
    }
}