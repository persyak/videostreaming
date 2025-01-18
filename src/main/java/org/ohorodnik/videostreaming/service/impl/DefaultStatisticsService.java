package org.ohorodnik.videostreaming.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ohorodnik.videostreaming.dto.StatisticsDto;
import org.ohorodnik.videostreaming.mapper.StatisticsMapper;
import org.ohorodnik.videostreaming.repository.StatisticsRepositoryCustom;
import org.ohorodnik.videostreaming.service.StatisticsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultStatisticsService implements StatisticsService {

    private final StatisticsRepositoryCustom statisticsRepositoryCustom;
    private final StatisticsMapper statisticsMapper;

    @Override
    @Transactional
    public StatisticsDto retrieveStatistics(UUID uuid) {
        log.info("Retrieve statistics for video {}", uuid);
        return statisticsMapper.toStatisticsDto(statisticsRepositoryCustom.retrieveStatistics(uuid));
    }
}
