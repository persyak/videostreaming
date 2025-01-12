package org.ohorodnik.videostreaming.service;

import org.ohorodnik.videostreaming.dto.StatisticsDto;

import java.util.UUID;

public interface StatisticsService {

    StatisticsDto retrieveStatistics(UUID uuid);
}
