package org.ohorodnik.videostreaming.repository;

import org.ohorodnik.videostreaming.entity.Statistics;

import java.util.UUID;

public interface StatisticsRepositoryCustom {

    Statistics retrieveStatistics(UUID uuid);
}
