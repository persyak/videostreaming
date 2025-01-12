package org.ohorodnik.videostreaming.web.controller;

import lombok.RequiredArgsConstructor;
import org.ohorodnik.videostreaming.dto.StatisticsDto;
import org.ohorodnik.videostreaming.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/statistics")
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/{uuid}")
    protected StatisticsDto retrieveStatistics(@PathVariable UUID uuid){
        return statisticsService.retrieveStatistics(uuid);
    }
}
